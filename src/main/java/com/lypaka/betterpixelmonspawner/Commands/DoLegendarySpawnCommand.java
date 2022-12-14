package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.API.LegendarySpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryInfoGetters;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.LegendaryUtils;
import com.lypaka.lypakautils.*;
import com.mojang.brigadier.CommandDispatcher;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.time.LocalDateTime;
import java.util.*;

public class DoLegendarySpawnCommand {

    public DoLegendarySpawnCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("dolegendaryspawn")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                        return 0;

                                                    }

                                                }

                                                List<ServerPlayerEntity> onlinePlayers = new ArrayList<>();
                                                for (Map.Entry<UUID, ServerPlayerEntity> entry : JoinListener.playerMap.entrySet()) {

                                                    if (!ConfigGetters.legendaryOptOut.contains(entry.getKey().toString())) {

                                                        if (DeadZone.getAreaFromLocation(entry.getValue()) != null) {

                                                            DeadZone deadZone = DeadZone.getAreaFromLocation(entry.getValue());
                                                            List<String> entities = deadZone.getEntities();
                                                            if (!entities.contains("legendaries")) {

                                                                onlinePlayers.add(entry.getValue());

                                                            }

                                                        } else {

                                                            onlinePlayers.add(entry.getValue());

                                                        }

                                                    }

                                                }

                                                if (onlinePlayers.size() == 0) {

                                                    return 0;

                                                }
                                                ServerPlayerEntity player;
                                                if (onlinePlayers.size() == 1) {

                                                    player = onlinePlayers.get(0);

                                                } else {

                                                    player = RandomHelper.getRandomElementFromList(onlinePlayers);

                                                }
                                                String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
                                                if (ConfigGetters.worldBlacklist.contains(worldName)) return 0;
                                                String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                                                if (!BiomeList.biomesToPokemon.containsKey(biomeID)) return 0;
                                                if (!BiomeList.biomeLegendaryMap.containsKey(biomeID)) return 0;

                                                List<LegendarySpawnInfo> possibleSpawns = new ArrayList<>();
                                                List<String> usedNames = new ArrayList<>();
                                                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                                    String location;
                                                    PlayerPartyStorage party = StorageProxy.getParty(player);
                                                    Pokemon firstPartyPokemon = null;
                                                    for (int i = 0; i < 6; i++) {

                                                        Pokemon p = party.get(0);
                                                        if (p != null) {

                                                            firstPartyPokemon = p;
                                                            break;

                                                        }

                                                    }
                                                    if (!ConfigGetters.locationMap.containsKey(player.getUniqueID().toString())) {

                                                        if (player.getRidingEntity() != null) {

                                                            Entity mount = player.getRidingEntity();
                                                            if (mount.isInWater()) {

                                                                location = "water";

                                                            } else if (mount.isOnGround()) {

                                                                if (mount.getPosition().getY() <= 63) {

                                                                    location = "underground";

                                                                } else {

                                                                    location = "land";

                                                                }

                                                            } else {

                                                                location = "air";

                                                            }

                                                        } else {

                                                            if (player.isInWater()) {

                                                                location = "water";

                                                            } else if (player.isOnGround()) {

                                                                if (player.getPosition().getY() <= 63) {

                                                                    location = "underground";

                                                                } else {

                                                                    location = "land";

                                                                }

                                                            } else {

                                                                location = "air";

                                                            }

                                                        }

                                                    } else {

                                                        location = ConfigGetters.locationMap.get(player.getUniqueID().toString());

                                                    }
                                                    Pokemon finalFirstPartyPokemon = firstPartyPokemon;
                                                    if (!ConfigGetters.legendarySpawnFilterEnabled) {

                                                        for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                                                            for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                                                                if (entry.getKey().equalsIgnoreCase(name)) {

                                                                    List<LegendarySpawnInfo> infos = entry.getValue();
                                                                    for (LegendarySpawnInfo info : infos) {

                                                                        if (!usedNames.contains(info.getName())) {

                                                                            usedNames.add(info.getName());
                                                                            possibleSpawns.add(info);

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    } else {

                                                        List<WorldTime> currentTimes = WorldTime.getCurrent(player.world);
                                                        String weather;
                                                        if (player.world.isRaining()) {

                                                            weather = "rain";

                                                        } else if (player.world.isThundering()) {

                                                            weather = "storm";

                                                        } else {

                                                            weather = "clear";

                                                        }

                                                        for (String name : BiomeList.biomesToPokemon.get(biomeID)) {

                                                            for (Map.Entry<String, List<LegendarySpawnInfo>> entry : LegendarySpawnInfo.infoMap.entrySet()) {

                                                                if (entry.getKey().equalsIgnoreCase(name)) {

                                                                    List<LegendarySpawnInfo> infos = entry.getValue();
                                                                    for (LegendarySpawnInfo info : infos) {

                                                                        if (currentTimes.contains(WorldTime.valueOf(info.getTime().toUpperCase()))) {

                                                                            if (info.getWeather().equalsIgnoreCase(weather)) {

                                                                                if (info.getSpawnLocation().contains(location)) {

                                                                                    if (!usedNames.contains(info.getName())) {

                                                                                        if (RandomHelper.getRandomChance(info.getSpawnChance())) {

                                                                                            possibleSpawns.add(info);
                                                                                            usedNames.add(info.getName());

                                                                                        }

                                                                                    }

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                    if (possibleSpawns.size() == 0) {

                                                        BetterPixelmonSpawner.logger.info("A legendary was not spawned!");
                                                        return;

                                                    }

                                                    Map<Double, UUID> spawnChanceMap = new HashMap<>();
                                                    Map<UUID, LegendarySpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
                                                    List<Double> spawnChances = new ArrayList<>(possibleSpawns.size());
                                                    int spawnIndex = 0;
                                                    double spawnChanceModifier = 1.0;
                                                    if (finalFirstPartyPokemon != null) {

                                                        if (ArenaTrap.applies(finalFirstPartyPokemon) || Illuminate.applies(finalFirstPartyPokemon) || NoGuard.applies(finalFirstPartyPokemon)) {

                                                            spawnChanceModifier = 2.0;

                                                        } else if (Infiltrator.applies(finalFirstPartyPokemon) || QuickFeet.applies(finalFirstPartyPokemon) || Stench.applies(finalFirstPartyPokemon) || WhiteSmoke.applies(finalFirstPartyPokemon)) {

                                                            spawnChanceModifier = 0.5;

                                                        }

                                                    }
                                                    for (LegendarySpawnInfo info : possibleSpawns) {

                                                        UUID randUUID = UUID.randomUUID();
                                                        double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                                                        spawnChanceMap.put(spawnChance, randUUID);
                                                        pokemonSpawnInfoMap.put(randUUID, info);
                                                        spawnChances.add(spawnIndex, spawnChance);
                                                        spawnIndex++;

                                                    }
                                                    Collections.sort(spawnChances);
                                                    int spawnAmount = possibleSpawns.size();
                                                    LegendarySpawnInfo selectedSpawn = null;
                                                    List<LegendarySpawnInfo> names = new ArrayList<>();
                                                    for (int i = 0; i < spawnAmount; i++) {

                                                        double spawnChance = spawnChances.get(i);
                                                        UUID uuid = spawnChanceMap.get(spawnChance);
                                                        LegendarySpawnInfo info = pokemonSpawnInfoMap.get(uuid);
                                                        if (RandomHelper.getRandomChance(spawnChance)) {

                                                            if (!names.contains(info)) {

                                                                names.add(info);

                                                            }

                                                        }

                                                    }
                                                    if (names.size() > 1) {

                                                        selectedSpawn = RandomHelper.getRandomElementFromList(names);

                                                    } else if (names.size() == 1) {

                                                        selectedSpawn = names.get(0);

                                                    }
                                                    if (FlashFire.applies(finalFirstPartyPokemon)) {

                                                        selectedSpawn = FlashFire.tryFlashFireOnLegendary(selectedSpawn, possibleSpawns);

                                                    } else if (Harvest.applies(finalFirstPartyPokemon)) {

                                                        selectedSpawn = Harvest.tryHarvestOnLegendary(selectedSpawn, possibleSpawns);

                                                    } else if (LightningRod.applies(finalFirstPartyPokemon) || Static.applies(finalFirstPartyPokemon)) {

                                                        selectedSpawn = LightningRod.tryLightningRodOnLegendary(selectedSpawn, possibleSpawns);

                                                    } else if (MagnetPull.applies(finalFirstPartyPokemon)) {

                                                        selectedSpawn = MagnetPull.tryMagnetPullOnLegendary(selectedSpawn, possibleSpawns);

                                                    } else if (StormDrain.applies(finalFirstPartyPokemon)) {

                                                        selectedSpawn = StormDrain.tryStormDrainOnLegendary(selectedSpawn, possibleSpawns);

                                                    }
                                                    if (selectedSpawn == null) {

                                                        BetterPixelmonSpawner.logger.info("No legendaries spawn in the selected player's biome!");
                                                        return;

                                                    }
                                                    String[] levelRange = selectedSpawn.getLevelRange().split("-");
                                                    int max = Integer.parseInt(levelRange[0]);
                                                    int min = Integer.parseInt(levelRange[1]);
                                                    String pokemonName = selectedSpawn.getName();
                                                    PixelmonEntity pokemon;
                                                    pokemonName = pokemonName.replace(".conf", "");
                                                    if (pokemonName.contains("-")) {

                                                        if (pokemonName.equalsIgnoreCase("porygon-z")) {

                                                            pokemonName = "porygon-z";
                                                            pokemon = PokemonBuilder.builder().species(pokemonName).build().getOrCreatePixelmon();

                                                        } else {

                                                            String[] split = pokemonName.split("-");
                                                            pokemonName = split[0];
                                                            String form = "";
                                                            for (int f = 1; f < split.length; f++) {

                                                                form = form + "-" + split[f];

                                                            }

                                                            form = form.substring(1); // removes that first _ from the String
                                                            pokemon = PokemonBuilder.builder().species(pokemonName).build().getOrCreatePixelmon();
                                                            pokemon.setForm(form);

                                                        }

                                                    } else {

                                                        pokemon = PokemonBuilder.builder().species(pokemonName).build().getOrCreatePixelmon();

                                                    }
                                                    int level = RandomHelper.getRandomNumberBetween(min, max);
                                                    if (Hustle.applies(finalFirstPartyPokemon) || Pressure.applies(finalFirstPartyPokemon) || VitalSpirit.applies(finalFirstPartyPokemon)) {

                                                        level = Hustle.tryApplyHustle(level, selectedSpawn);

                                                    }
                                                    pokemon.getLvl().setLevel(level);
                                                    if (Intimidate.applies(finalFirstPartyPokemon) || KeenEye.applies(finalFirstPartyPokemon)) {

                                                        pokemon = Intimidate.tryIntimidate(pokemon, finalFirstPartyPokemon);
                                                        if (pokemon == null) return;

                                                    }
                                                    pokemon.updateStats();

                                                    if (selectedSpawn.getHeldItemID() != null) {

                                                        pokemon.getPokemon().setHeldItem(ItemStackBuilder.buildFromStringID(selectedSpawn.getHeldItemID()));

                                                    } else {

                                                        if (ConfigGetters.heldItemsEnabled) {

                                                            HeldItemUtils.tryApplyHeldItem(pokemonName, pokemon, finalFirstPartyPokemon);

                                                        }

                                                    }

                                                    int xzRadius = ConfigGetters.legendarySpawnLocationXZ;
                                                    int yRadius = ConfigGetters.legendarySpawnLocationY;
                                                    int spawnX;
                                                    int spawnY;
                                                    int spawnZ;

                                                    if (RandomHelper.getRandomChance(50)) {

                                                        spawnX = player.getPosition().getX() + xzRadius;

                                                    } else {

                                                        spawnX = player.getPosition().getX() - xzRadius;

                                                    }
                                                    if (RandomHelper.getRandomChance(50)) {

                                                        spawnZ = player.getPosition().getZ() + xzRadius;

                                                    } else {

                                                        spawnZ = player.getPosition().getZ() - xzRadius;

                                                    }

                                                    BlockPos safeSpawn;
                                                    if (location.equalsIgnoreCase("air")) {

                                                        spawnY = player.getPosition().getY() + yRadius;
                                                        safeSpawn = new BlockPos(spawnX, spawnY, spawnZ);

                                                    } else {

                                                        spawnY = player.getPosition().getY();
                                                        BlockPos baseSpawn = new BlockPos(spawnX, spawnY, spawnZ);
                                                        if (!location.equalsIgnoreCase("underground")) {

                                                            if (WorldDimGetter.getDimID(player.world) == -1) {

                                                                safeSpawn = baseSpawn;

                                                            } else {

                                                                Heightmap.Type type = location.equalsIgnoreCase("water") ? Heightmap.Type.OCEAN_FLOOR : Heightmap.Type.WORLD_SURFACE;
                                                                safeSpawn = new BlockPos(spawnX, player.world.getChunk(baseSpawn).getTopBlockY(type, spawnX, spawnZ), spawnZ);

                                                            }

                                                        } else {

                                                            safeSpawn = baseSpawn; // not the safest thing to do as Pokemon could spawn in blocks underground, but there's not a lot of space down there anyway

                                                        }

                                                    }
                                                    if (CuteCharm.applies(finalFirstPartyPokemon)) {

                                                        CuteCharm.tryApplyCuteCharmEffect(pokemon, finalFirstPartyPokemon);

                                                    }
                                                    if (Synchronize.applies(finalFirstPartyPokemon)) {

                                                        Synchronize.applySynchronize(pokemon, finalFirstPartyPokemon);

                                                    }
                                                    pokemon.setLocationAndAngles(safeSpawn.getX() + BetterPixelmonSpawner.random.nextDouble(), safeSpawn.getY(), safeSpawn.getZ() + BetterPixelmonSpawner.random.nextDouble(),0, 0);

                                                    LegendarySpawnEvent legendarySpawnEvent = new LegendarySpawnEvent(pokemon, player, selectedSpawn);
                                                    MinecraftForge.EVENT_BUS.post(legendarySpawnEvent);
                                                    if (!legendarySpawnEvent.isCanceled()) {

                                                        int finalLevel = level;
                                                        ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                                            player.world.addEntity(legendarySpawnEvent.getPokemon());
                                                            legendarySpawnEvent.getPokemon().getPokemon().setLevelNum(finalLevel);
                                                            LegendaryUtils.handleGlowing(legendarySpawnEvent.getPokemon());
                                                            LegendaryUtils.handleGracePeriod(legendarySpawnEvent.getPokemon(), legendarySpawnEvent.getPlayer());
                                                            LegendaryInfoGetters.setLegendaryName(legendarySpawnEvent.getPokemon().getPokemonName());
                                                            LegendaryInfoGetters.setPokemon(legendarySpawnEvent.getPokemon());
                                                            LegendaryInfoGetters.setSpawnedPlayer(player);
                                                            LegendaryInfoGetters.setSpawnPos(legendarySpawnEvent.getPokemon().getPosition());
                                                            LegendaryInfoGetters.setTime(LocalDateTime.now());
                                                            LegendaryListing.updateListingConfig(legendarySpawnEvent.getPokemon());
                                                            legendarySpawnEvent.getPokemon().addTag("SpawnedLegendary"); // used for the last legend list, so the event listeners know what is a BPS spawned legendary
                                                            if (!ConfigGetters.legendarySpawnMessage.equalsIgnoreCase("")) {

                                                                for (Map.Entry<UUID, ServerPlayerEntity> entry : JoinListener.playerMap.entrySet()) {

                                                                    entry.getValue().sendMessage(FancyText.getFormattedText(ConfigGetters.legendarySpawnMessage
                                                                            .replace("%biome%", LegendarySpawner.getPrettyBiomeName(legendarySpawnEvent.getPokemon().world.getBiome(legendarySpawnEvent.getPokemon().getPosition()).getRegistryName().toString()))
                                                                            .replace("%pokemon%", legendarySpawnEvent.getPokemon().getPokemonName())
                                                                            .replace("%player%", legendarySpawnEvent.getPlayer().getName().getString())), entry.getValue().getUniqueID());

                                                                }

                                                            }

                                                        });

                                                    }

                                                });

                                                return 1;

                                            })
                            )
            );

        }

    }

}
