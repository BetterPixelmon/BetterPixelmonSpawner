package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.MiscSpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.Utils.Counters.MiscCounter;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.EntityUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.WormholeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class MiscSpawner {

    private static Timer timer;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.miscSpawnerEnabled) return;

        long interval = ConfigGetters.miscEntityInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                    for (Map.Entry<UUID, ServerPlayerEntity> playerEntry : JoinListener.playerMap.entrySet()) {

                        ServerPlayerEntity player = playerEntry.getValue();
                        if (ConfigGetters.miscOptOut.contains(player.getUniqueID().toString())) continue;
                        if (MiscCounter.getCount(player.getUniqueID()) > ConfigGetters.maxMiscEntities) {

                            if (ConfigGetters.maxMiscEntities > 0) {

                                continue;

                            }

                        }
                        if (player.isCreative() && ConfigGetters.ignoreCreativeMisc) continue;
                        if (player.isSpectator() && ConfigGetters.ignoreSpectatorMisc) continue;
                        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
                        if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                        if (ConfigGetters.unsafeMiscSpawnLocations) {

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
                        int xzRadius = ConfigGetters.miscRadiusXZ;
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
                        double sum = ConfigGetters.miscEntitySpawnMap.values().stream().mapToDouble(c -> c).sum();
                        double rng = BetterPixelmonSpawner.random.nextDouble() * sum;
                        String selectedID = null;
                        for (Map.Entry<String, Double> entry : ConfigGetters.miscEntitySpawnMap.entrySet()) {

                            if (Double.compare(rng, entry.getValue()) <= 0) {

                                selectedID = entry.getKey();
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
                        if (selectedID.equalsIgnoreCase("pixelmon:ultra_wormhole")) {

                            WormholeEntity entity = new WormholeEntity(player.world);
                            entity.setLocationAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
                            entity.setBoundingBox(new AxisAlignedBB(entity.getPosition()));
                            MiscSpawnEvent miscSpawnEvent = new MiscSpawnEvent(entity, player, selectedID);
                            MinecraftForge.EVENT_BUS.post(miscSpawnEvent);
                            if (!miscSpawnEvent.isCanceled()) {

                                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                    if (ConfigGetters.miscBroadcastMap.containsKey(miscSpawnEvent.getSelectedEntityID())) {

                                        String message = ConfigGetters.miscBroadcastMap.get(miscSpawnEvent.getSelectedEntityID());
                                        if (!message.equalsIgnoreCase("")) {

                                            String biome = miscSpawnEvent.getEntity().world.getBiome(miscSpawnEvent.getEntity().getPosition()).getRegistryName().toString();
                                            ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(message
                                                    .replace("%biome%", biome)
                                                    .replace("%player%", miscSpawnEvent.getPlayer().getName().getString())), p.getUniqueID()));

                                        }

                                    }
                                    player.world.addEntity(miscSpawnEvent.getEntity());
                                    MiscCounter.increment(miscSpawnEvent.getEntity(), miscSpawnEvent.getPlayer().getUniqueID());
                                    EntityUtils.handleDespawning(miscSpawnEvent.getEntity());

                                });

                            }

                        } else {

                            LivingEntity entity = EntityUtils.getEntityFromID(null, selectedID, player.world);
                            if (entity == null) {

                                BetterPixelmonSpawner.logger.error("Couldn't get an entity from ID: " + selectedID);
                                continue;

                            }
                            entity.setLocationAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
                            entity.setBoundingBox(new AxisAlignedBB(entity.getPosition()));
                            MiscSpawnEvent miscSpawnEvent = new MiscSpawnEvent(entity, player, selectedID);
                            MinecraftForge.EVENT_BUS.post(miscSpawnEvent);
                            if (!miscSpawnEvent.isCanceled()) {

                                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                    if (ConfigGetters.miscBroadcastMap.containsKey(miscSpawnEvent.getSelectedEntityID())) {

                                        String message = ConfigGetters.miscBroadcastMap.get(miscSpawnEvent.getSelectedEntityID());
                                        if (!message.equalsIgnoreCase("")) {

                                            String biome = miscSpawnEvent.getEntity().world.getBiome(miscSpawnEvent.getEntity().getPosition()).getRegistryName().toString();
                                            ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(message
                                                    .replace("%biome%", biome)
                                                    .replace("%player%", miscSpawnEvent.getPlayer().getName().getString())), p.getUniqueID()));

                                        }

                                    }
                                    player.world.addEntity(miscSpawnEvent.getEntity());
                                    MiscCounter.increment(miscSpawnEvent.getEntity(), miscSpawnEvent.getPlayer().getUniqueID());
                                    EntityUtils.handleDespawning(miscSpawnEvent.getEntity());

                                });

                            }

                        }

                    }

                });

            }

        }, 0, interval);

    }

}
