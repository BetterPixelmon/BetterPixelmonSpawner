package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.NPCSpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.DebugSystem.NPCDebug;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.EntityUtils;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.npcs.*;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GeneralNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class NPCSpawner {

    private static Timer timer;
    public static boolean debugEnabled = false;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.npcSpawnerEnabled) return;

        long interval = ConfigGetters.npcSpawnInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                    for (Map.Entry<UUID, ServerPlayerEntity> playerEntry : JoinListener.playerMap.entrySet()) {

                        ServerPlayerEntity player = playerEntry.getValue();
                        NPCDebug.printPlayerDebugInformation(player);
                        if (ConfigGetters.npcOptOut.contains(player.getUniqueID().toString())) continue;
                        if (NPCCounter.getCount(player.getUniqueID()) > ConfigGetters.maxNPCs) {

                            if (ConfigGetters.maxNPCs > 0) {

                                continue;

                            }

                        }
                        if (player.isCreative() && ConfigGetters.ignoreCreativeNPC) continue;
                        if (player.isSpectator() && ConfigGetters.ignoreSpectatorNPC) continue;
                        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
                        if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                        if (ConfigGetters.unsafeSpawnLocations) {

                            if (player.isInWater()) {

                                continue;

                            } else if (player.isOnGround()) {

                                if (player.getPosition().getY() <= 63) {

                                    continue;

                                }

                            }  else {

                                continue;

                            }

                        }

                        int xzRadius = ConfigGetters.npcSpawnLocationXZ;
                        int x;
                        int z;
                        int playerX = player.getPosition().getX();
                        int playerY = player.getPosition().getY();
                        int playerZ = player.getPosition().getZ();
                        if (RandomHelper.getRandomChance(50)) {

                            x = playerX + RandomHelper.getRandomNumberBetween(1, xzRadius);

                        } else {

                            x = playerX - RandomHelper.getRandomNumberBetween(1, xzRadius);

                        }
                        if (RandomHelper.getRandomChance(50)) {

                            z = playerZ + RandomHelper.getRandomNumberBetween(1, xzRadius);

                        } else {

                            z = playerZ - RandomHelper.getRandomNumberBetween(1, xzRadius);

                        }
                        BlockPos pos = new BlockPos(x, playerY, z);
                        Heightmap.Type type = Heightmap.Type.WORLD_SURFACE;
                        int y = player.world.getChunk(pos).getTopBlockY(type, x, z);
                        BlockPos spawn = new BlockPos(x, y, z);
                        double sum = ConfigGetters.npcSpawnMap.values().stream().mapToDouble(c -> c).sum();
                        double rng = sum * BetterPixelmonSpawner.random.nextDouble();
                        String selectedID = null;
                        for (Map.Entry<String, Double> entry : ConfigGetters.npcSpawnMap.entrySet()) {

                            if (debugEnabled) {

                                BetterPixelmonSpawner.logger.info("NPC DEBUG: Pinging " + entry.getKey() + " / " + entry.getValue());

                            }
                            if (Double.compare(rng, entry.getValue()) <= 0) {

                                selectedID = entry.getKey();
                                if (debugEnabled) {

                                    BetterPixelmonSpawner.logger.info("NPC DEBUG: " + selectedID + " was chosen for spawn");

                                }
                                break;

                            } else {

                                rng -= entry.getValue();

                            }

                        }

                        if (selectedID == null) continue;
                        if (DeadZone.getAreaFromLocation(player) != null) {

                            DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                            List<String> entities = deadZone.getEntities();
                            if (entities.contains(selectedID)) {

                                continue;

                            }

                        }
                        String biomeID = player.world.getBiome(spawn).getRegistryName().toString();
                        LivingEntity entity = EntityUtils.getEntityFromID(biomeID, selectedID, player.world);
                        if (entity == null) {

                            BetterPixelmonSpawner.logger.error("Couldn't get an entity from ID: " + selectedID);
                            continue;

                        }
                        if (entity instanceof NPCEntity) {

                            ((NPCEntity) entity).initWanderingAI();

                        }
                        if (entity instanceof NPCChatting) {

                            NPCChatting chatting = (NPCChatting) entity;
                            GeneralNPCData data = ServerNPCRegistry.villagers.getRandom();
                            chatting.init(data);
                            chatting.initWanderingAI();
                            chatting.setTextureIndex(data.getRandomChatIndex());

                        } else if (entity instanceof NPCTutor) {

                            NPCTutor tutor = (NPCTutor) entity;
                            tutor.init("Tutor");

                        } else if (entity instanceof NPCShopkeeper) {

                            NPCShopkeeper shopkeep = (NPCShopkeeper) entity;
                            ShopkeeperData shopData = ServerNPCRegistry.shopkeepers.getRandom();
                            if (shopData != null) {

                                shopkeep.init(shopData);

                            }

                        }
                        entity.setLocationAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
                        NPCSpawnEvent npcSpawnEvent = new NPCSpawnEvent((NPCEntity) entity, player);
                        MinecraftForge.EVENT_BUS.post(npcSpawnEvent);
                        if (!npcSpawnEvent.isCanceled()) {

                            ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                player.world.addEntity(npcSpawnEvent.getNPC());
                                NPCCounter.increment(npcSpawnEvent.getNPC(), npcSpawnEvent.getPlayer().getUniqueID());
                                if (npcSpawnEvent.getNPC() instanceof NPCTrainer) {

                                    if (ConfigGetters.scaleNPCLevelsByDistance) {

                                        int level = 3;
                                        if ((int) ((double) level + Math.floor(Math.sqrt(player.getServerWorld().getSpawnPoint().distanceSq(playerX, playerY, playerZ, true)) / (double) ConfigGetters.blocksBeforeNPCIncrease + Math.random() * 3.0)) > ConfigGetters.maxNPCScaleLevel) {

                                            level = ConfigGetters.maxNPCScaleLevel;

                                        } else {

                                            int distance = (int) Math.floor(Math.sqrt(player.getServerWorld().getSpawnPoint().distanceSq(playerX, playerY, playerZ, true)));
                                            if (distance > ConfigGetters.blocksBeforeNPCIncrease) {

                                                int mod = (distance / ConfigGetters.blocksBeforeNPCIncrease) * ConfigGetters.npcLevelModifier;
                                                if (ConfigGetters.npcSpawnLevelRandomizationEnabled) {

                                                    float randomMin = ConfigGetters.npcSpawnLevelRandomizationValueMin;
                                                    float randomMax = ConfigGetters.npcSpawnLevelRandomizationValueMax;
                                                    level = (int) (mod * RandomHelper.getRandomNumberBetween(randomMin, randomMax)) + level;


                                                } else {

                                                    level = mod + level;

                                                }

                                            }

                                        }

                                        NPCTrainer trainer = (NPCTrainer) npcSpawnEvent.getNPC();
                                        trainer.setPokemonLevel(level);

                                    }

                                }
                                EntityUtils.handleDespawning(npcSpawnEvent.getNPC());

                            });

                        }

                    }

                });

            }

        }, interval, interval);

    }

}
