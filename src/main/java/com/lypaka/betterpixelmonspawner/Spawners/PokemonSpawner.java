package com.lypaka.betterpixelmonspawner.Spawners;

import com.lypaka.betterpixelmonspawner.API.BossSpawnEvent;
import com.lypaka.betterpixelmonspawner.API.HolidaySpawnEvent;
import com.lypaka.betterpixelmonspawner.API.PokemonSpawnEvent;
import com.lypaka.betterpixelmonspawner.API.ShinySpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.DebugSystem.PokemonDebug;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.Holidays.Holiday;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.PokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.lypaka.lypakautils.JoinListener;
import com.lypaka.lypakautils.WorldDimGetter;
import com.pixelmonmod.pixelmon.api.config.PixelmonConfigProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.aggression.Aggression;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class PokemonSpawner {

    private static Timer timer = null;

    public static void startTimer() {

        if (timer != null) {

            timer.cancel();

        }

        if (!ConfigGetters.spawnerEnabled) return;

        long interval = ConfigGetters.spawnInterval * 1000L;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (Map.Entry<UUID, ServerPlayerEntity> playerEntry : JoinListener.playerMap.entrySet()) {

                    List<String> usedNames = new ArrayList<>();
                    ServerPlayerEntity player = playerEntry.getValue();
                    PokemonDebug.printPokemonDebugInformation(player);
                    if (DeadZone.getAreaFromLocation(player) != null) {

                        DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                        List<String> entities = deadZone.getEntities();
                        if (entities.contains("pokemon")) continue;

                    }
                    if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) continue;
                    if (RepelHandler.hasRepel(player)) continue;
                    if (PokemonCounter.getCount(player.getUniqueID()) >= ConfigGetters.maxPokemon) {

                        if (ConfigGetters.maxPokemon != 0) continue;

                    }
                    if (player.isCreative() && ConfigGetters.ignoreCreativePokemon) continue;
                    if (player.isSpectator() && ConfigGetters.ignoreSpectatorPokemon) continue;
                    String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
                    if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                    String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                    List<WorldTime> currentTimes = WorldTime.getCurrent(player.world);
                    ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                        PlayerPartyStorage party = StorageProxy.getParty(player);
                        Pokemon firstPartyPokemon = null;
                        for (int i = 0; i < 6; i++) {

                            Pokemon p = party.get(i);
                            if (p != null) {

                                firstPartyPokemon = p;
                                break;

                            }

                        }
                        String weather;
                        if (player.world.isRaining()) {

                            weather = "rain";

                        } else if (player.world.isThundering()) {

                            weather = "storm";

                        } else {

                            weather = "clear";

                        }
                        String location;
                        if (!ConfigGetters.locationMap.containsKey(player.getUniqueID().toString())) {

                            if (player.getRidingEntity() != null) {

                                Entity mount = player.getRidingEntity();
                                if (mount.isInWater()) {

                                    location = "water";

                                } else if (mount.isOnGround()) {

                                    if (mount.getPosition().getY() <= 59) {

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

                                    if (player.getPosition().getY() <= 59) {

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
                        // Check if biome list contains biome ID
                        if (BiomeList.biomesToPokemon.containsKey(biomeID)) {

                            // Get all SpawnInfo objects for each Pokemon name in this biome
                            List<PokemonSpawnInfo> possibleSpawns = new ArrayList<>();
                            for (String pokemonNames : BiomeList.biomesToPokemon.get(biomeID)) {

                                for (Map.Entry<String, List<PokemonSpawnInfo>> entry : PokemonSpawnInfo.infoMap.entrySet()) {

                                    if (entry.getKey().equalsIgnoreCase(pokemonNames)) {

                                        List<PokemonSpawnInfo> infos = entry.getValue();
                                        for (PokemonSpawnInfo info : infos) {

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

                            if (possibleSpawns.size() == 0) return;

                            Map<Double, UUID> spawnChanceMap = new HashMap<>();
                            Map<UUID, PokemonSpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
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
                            for (PokemonSpawnInfo info : possibleSpawns) {

                                UUID randUUID = UUID.randomUUID();
                                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                                spawnChanceMap.put(spawnChance, randUUID);
                                pokemonSpawnInfoMap.put(randUUID, info);
                                spawnChances.add(spawnIndex, spawnChance);
                                spawnIndex++;

                            }
                            Collections.sort(spawnChances);
                            int spawnAmount = possibleSpawns.size();
                            PokemonSpawnInfo selectedSpawn = null;
                            List<PokemonSpawnInfo> names = new ArrayList<>();
                            for (int i = 0; i < spawnAmount; i++) {

                                double spawnChance = spawnChances.get(i);
                                UUID uuid = spawnChanceMap.get(spawnChance);
                                PokemonSpawnInfo info = pokemonSpawnInfoMap.get(uuid);
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

                                selectedSpawn = FlashFire.tryFlashFireOnPokemon(selectedSpawn, possibleSpawns);

                            } else if (Harvest.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = Harvest.tryHarvestOnPokemon(selectedSpawn, possibleSpawns);

                            } else if (LightningRod.applies(finalFirstPartyPokemon) || Static.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = LightningRod.tryLightningRodOnPokemon(selectedSpawn, possibleSpawns);

                            } else if (MagnetPull.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = MagnetPull.tryMagnetPullOnPokemon(selectedSpawn, possibleSpawns);

                            } else if (StormDrain.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = StormDrain.tryStormDrainOnPokemon(selectedSpawn, possibleSpawns);

                            }
                            if (selectedSpawn == null) return;
                            String[] levelRange = selectedSpawn.getLevelRange().split("-");
                            int max = Integer.parseInt(levelRange[0]);
                            int min = Integer.parseInt(levelRange[1]);
                            String pokemonName = selectedSpawn.getName();
                            PixelmonEntity pokemon;
                            int maxGroupSize = Integer.parseInt(selectedSpawn.getGroupSize().split("-")[0]);
                            int minGroupSize = Integer.parseInt(selectedSpawn.getGroupSize().split("-")[1]);
                            int groupSize = ConfigGetters.enableGroupSize ? RandomHelper.getRandomNumberBetween(minGroupSize, maxGroupSize) : 1;
                            pokemonName = pokemonName.replace(".conf", "");
                            boolean checkBoss = true;
                            boolean pokeModified = false;
                            for (int i = 0; i < groupSize; i++) {

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

                                if (selectedSpawn.getHeldItemID() != null) {

                                    pokemon.getPokemon().setHeldItem(ItemStackBuilder.buildFromStringID(selectedSpawn.getHeldItemID()));

                                } else {

                                    if (ConfigGetters.heldItemsEnabled) {

                                        HeldItemUtils.tryApplyHeldItem(pokemonName, pokemon, finalFirstPartyPokemon);

                                    }

                                }

                                int xzRadius = ConfigGetters.xzRadius;
                                int yRadius = ConfigGetters.yRadius;
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
                                boolean gmax = false;
                                if (ConfigGetters.gmaxChance > 0.0) {

                                    if (RandomHelper.getRandomChance(ConfigGetters.gmaxChance)) {

                                        gmax = true;

                                    }

                                }
                                if (gmax && !pokemon.getPokemon().canGigantamax()) gmax = false;
                                boolean finalGmax = gmax;
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

                                            if (location.contains("water")) {

                                                safeSpawn = new BlockPos(spawnX, player.getPosition().getY(), spawnZ);

                                            } else {

                                                Heightmap.Type type = location.equalsIgnoreCase("water") ? Heightmap.Type.OCEAN_FLOOR : Heightmap.Type.WORLD_SURFACE;
                                                safeSpawn = new BlockPos(spawnX, player.world.getChunk(baseSpawn).getTopBlockY(type, spawnX, spawnZ), spawnZ);

                                            }

                                        }

                                    } else {

                                        safeSpawn = baseSpawn; // not the safest thing to do as Pokemon could spawn in blocks underground, but there's not a lot of space down there anyway

                                    }

                                }
                                Block block = player.world.getBlockState(safeSpawn).getBlock();
                                if (block != Blocks.AIR && block != Blocks.TALL_GRASS && block != Blocks.GRASS) {

                                    if (location.equalsIgnoreCase("water") && block != Blocks.WATER) {

                                        return;

                                    } else {

                                        for (Element type : pokemon.getForm().getTypes()) {

                                            if (type == Element.FIRE) {

                                                if (block != Blocks.LAVA) {

                                                    return;

                                                }

                                            }

                                        }

                                    }

                                }
                                int x = safeSpawn.getX();
                                int y = safeSpawn.getY();
                                int z = safeSpawn.getZ();
                                if (CuteCharm.applies(finalFirstPartyPokemon)) {

                                    CuteCharm.tryApplyCuteCharmEffect(pokemon, finalFirstPartyPokemon);

                                }
                                if (Synchronize.applies(finalFirstPartyPokemon)) {

                                    Synchronize.applySynchronize(pokemon, finalFirstPartyPokemon);

                                }

                                pokemon.setLocationAndAngles(x + RandomHelper.getRandomNumberBetween(2.75f, 6f), y, z + RandomHelper.getRandomNumberBetween(2.75f, 6f),0, 0);
                                int level = 3;
                                int playerX = player.getPosition().getX();
                                int playerY = player.getPosition().getY();
                                int playerZ = player.getPosition().getZ();
                                if (ConfigGetters.scalePokemonLevelsByDistance) {

                                    if ((int) ((double) level + Math.floor(Math.sqrt(player.getServerWorld().getSpawnPoint().distanceSq(playerX, playerY, playerZ, true)) / (double) ConfigGetters.blocksBeforePokemonIncrease + Math.random() * 3.0)) > ConfigGetters.maxPokemonScaleLevel) {

                                        level = ConfigGetters.maxPokemonScaleLevel;

                                    } else {

                                        int distance = (int) Math.floor(Math.sqrt(player.getServerWorld().getSpawnPoint().distanceSq(playerX, playerY, playerZ, true)));
                                        if (distance > ConfigGetters.blocksBeforePokemonIncrease) {

                                            int mod = (distance / ConfigGetters.blocksBeforePokemonIncrease) * ConfigGetters.pokemonLevelModifier;
                                            if (ConfigGetters.pokemonSpawnLevelRandomizationEnabled) {

                                                float randomMin = ConfigGetters.pokemonSpawnLevelRandomizationValueMin;
                                                float randomMax = ConfigGetters.pokemonSpawnLevelRandomizationValueMax;
                                                level = (int) (mod * RandomHelper.getRandomNumberBetween(randomMin, randomMax)) + level;


                                            } else {

                                                level = mod + level;

                                            }

                                        }

                                    }

                                } else {

                                    level = RandomHelper.getRandomNumberBetween(min, max);

                                }
                                if (Hustle.applies(finalFirstPartyPokemon) || Pressure.applies(finalFirstPartyPokemon) || VitalSpirit.applies(finalFirstPartyPokemon)) {

                                    level = Hustle.tryApplyHustle(level, selectedSpawn);

                                }
                                pokemon.getPokemon().setLevel(level);
                                if (Intimidate.applies(finalFirstPartyPokemon) || KeenEye.applies(finalFirstPartyPokemon)) {

                                    pokemon = Intimidate.tryIntimidate(pokemon, finalFirstPartyPokemon);
                                    if (pokemon == null) return;

                                }
                                pokemon = PokemonUtils.validateReforgedPokemon(pokemon, level);
                                pokemon.setLocationAndAngles(x + RandomHelper.getRandomNumberBetween(2.75f, 6f), y, z + RandomHelper.getRandomNumberBetween(2.75f, 6f),0, 0);
                                pokemon.updateStats();
                                pokemon.setSpawnLocation(pokemon.getDefaultSpawnLocation());
                                if (ConfigGetters.aggressiveChance > 0) {

                                    if (RandomHelper.getRandomChance(ConfigGetters.aggressiveChance)) {

                                        pokemon.setAggression(Aggression.AGGRESSIVE);

                                    }

                                }

                                float shinyChance = PixelmonConfigProxy.getSpawning().getShinyRate();
                                boolean shiny = RandomHelper.getRandomNumberBetween(1, shinyChance) == 1;
                                if (shiny) {

                                    pokemon.getPokemon().setShiny(true);

                                }
                                if (!pokeModified) {

                                    if (HolidayHandler.activeHolidays != null && !HolidayHandler.activeHolidays.isEmpty()) {

                                        String holiday;
                                        List<String> holidays = new ArrayList<>();
                                        for (Holiday h : HolidayHandler.activeHolidays) {

                                            if (h.getPokemon().contains(pokemon.getPokemonName()) || h.getPokemon().contains("shiny") && pokemon.getPokemon().isShiny()) {

                                                if (!holidays.contains(h.getName())) {

                                                    holidays.add(h.getName());

                                                }

                                            }

                                        }

                                        if (holidays.size() > 0) {

                                            // I know this is probably dumb, but every other time I've tried getting a random index from a list with a size of 1, it throws IndexOutOfBounds so this prevents any chance of that ever happening
                                            if (holidays.size() == 1) {

                                                holiday = holidays.get(0);

                                            } else {

                                                holiday = holidays.get(BetterPixelmonSpawner.random.nextInt(holidays.size()));

                                            }

                                            HolidaySpawnEvent holidaySpawnEvent = new HolidaySpawnEvent(holiday, pokemon, player);
                                            MinecraftForge.EVENT_BUS.post(holidaySpawnEvent);
                                            if (!holidaySpawnEvent.isCanceled()) {

                                                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                                    holidaySpawnEvent.getPlayer().world.addEntity(holidaySpawnEvent.getPokemon());
                                                    PokemonCounter.increment(holidaySpawnEvent.getPokemon(), player.getUniqueID());
                                                    PokemonCounter.addPokemon(holidaySpawnEvent.getPokemon(), player.getUniqueID());

                                                });
                                                if (!ConfigGetters.holidaySpawnMessage.equalsIgnoreCase("")) {

                                                    PixelmonEntity finalPokemon = pokemon;
                                                    pokemon.world.getServer().getPlayerList().getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(ConfigGetters.holidaySpawnMessage
                                                            .replace("%holiday%", holiday)
                                                            .replace("%pokemon%", finalPokemon.getPokemonName())
                                                    ), p.getUniqueID()));

                                                }
                                                pokeModified = true;

                                            }

                                        }

                                    }

                                }
                                if (!pokeModified) {

                                    if (checkBoss) {

                                        if (BossPokemonUtils.isPossibleBoss(pokemon.getPokemonName())) {

                                            if (BossPokemonUtils.spawnBoss()) {

                                                if (RandomHelper.getRandomChance(ConfigGetters.bossSpawnChance)) {

                                                    if (BossPokemonUtils.canSpawn(player)) {

                                                        BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(pokemon, player, selectedSpawn);
                                                        MinecraftForge.EVENT_BUS.post(bossSpawnEvent);
                                                        if (!bossSpawnEvent.isCanceled()) {

                                                            pokemon.setBossTier(BossPokemonUtils.getBossMode());
                                                            pokeModified = true;
                                                            ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                                                player.world.addEntity(bossSpawnEvent.getPokemon());
                                                                PokemonCounter.increment(bossSpawnEvent.getPokemon(), player.getUniqueID());
                                                                // Sets this tag for the PokeClear to be able to know what a Boss is, in the event of a "normal" Boss
                                                                bossSpawnEvent.getPokemon().addTag("PixelmonDefaultBoss");
                                                                PokemonCounter.addPokemon(bossSpawnEvent.getPokemon(), player.getUniqueID());

                                                            });
                                                            continue;

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }
                                if (shiny || pokemon.getPokemon().isShiny()) {

                                    ShinySpawnEvent shinySpawnEvent = new ShinySpawnEvent(pokemon, player, selectedSpawn);
                                    MinecraftForge.EVENT_BUS.post(shinySpawnEvent);
                                    if (!shinySpawnEvent.isCanceled()) {

                                        ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                            shinySpawnEvent.getPokemon().getPokemon().setGigantamaxFactor(finalGmax);
                                            shinySpawnEvent.getPokemon().getPokemon().setShiny(true);
                                            player.world.addEntity(shinySpawnEvent.getPokemon());
                                            PokemonCounter.increment(shinySpawnEvent.getPokemon(), player.getUniqueID());
                                            PokemonCounter.addPokemon(shinySpawnEvent.getPokemon(), player.getUniqueID());

                                        });

                                    }

                                } else {

                                    PokemonSpawnEvent pokemonSpawnEvent = new PokemonSpawnEvent(pokemon, player, selectedSpawn, groupSize);
                                    MinecraftForge.EVENT_BUS.post(pokemonSpawnEvent);
                                    if (!pokemonSpawnEvent.isCanceled()) {

                                        ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                            pokemonSpawnEvent.getPokemon().getPokemon().setGigantamaxFactor(finalGmax);
                                            player.world.addEntity(pokemonSpawnEvent.getPokemon());
                                            PokemonCounter.increment(pokemonSpawnEvent.getPokemon(), player.getUniqueID());
                                            PokemonCounter.addPokemon(pokemonSpawnEvent.getPokemon(), player.getUniqueID());

                                        });

                                    }

                                }

                            }

                        }

                    });

                }

            }

        }, 0, interval);

    }

}
