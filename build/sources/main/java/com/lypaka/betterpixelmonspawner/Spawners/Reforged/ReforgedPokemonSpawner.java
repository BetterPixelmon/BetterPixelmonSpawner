package com.lypaka.betterpixelmonspawner.Spawners.Reforged;

import com.lypaka.betterpixelmonspawner.API.Spawning.Reforged.ReforgedBossSpawnEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.Reforged.ReforgedHolidaySpawnEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.Reforged.ReforgedPokemonSpawnEvent;
import com.lypaka.betterpixelmonspawner.API.Spawning.Reforged.ReforgedShinySpawnEvent;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.DebugSystem.PokemonDebug;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.*;
import com.lypaka.betterpixelmonspawner.Holidays.Holiday;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.Counters.ReforgedPokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.PokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.Reforged.ReforgedFormIndexFromName;
import com.lypaka.betterpixelmonspawner.Utils.Reforged.ReforgedHeldItemUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.lypaka.lypakautils.WorldDimGetter;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumAggression;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import java.util.*;

public class ReforgedPokemonSpawner {

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

                for (Map.Entry<UUID, EntityPlayerMP> playerEntry : JoinListener.playerMap.entrySet()) {

                    List<String> usedNames = new ArrayList<>();
                    EntityPlayerMP player = playerEntry.getValue();
                    PokemonDebug.printPokemonDebugInformation(player);
                    if (DeadZone.getAreaFromLocation(player) != null) {

                        DeadZone deadZone = DeadZone.getAreaFromLocation(player);
                        List<String> entities = deadZone.getEntities();
                        if (entities.contains("pokemon")) continue;

                    }
                    if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) continue;
                    if (RepelHandler.hasRepel(player)) continue;
                    if (ReforgedPokemonCounter.getCount(player.getUniqueID()) >= ConfigGetters.maxPokemon) {

                        if (ConfigGetters.maxPokemon != 0) continue;

                    }
                    if (player.isCreative() && ConfigGetters.ignoreCreativePokemon) continue;
                    if (player.isSpectator() && ConfigGetters.ignoreSpectatorPokemon) continue;
                    String worldName = player.world.getWorldInfo().getWorldName();
                    if (ConfigGetters.worldBlacklist.contains(worldName)) continue;
                    String biomeID = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                    ArrayList<WorldTime> currentTimes = WorldTime.getCurrent(player.world);
                    FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
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

                                } else if (mount.onGround) {

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

                                } else if (player.onGround) {

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

                                selectedSpawn = FlashFire.tryFlashFireOnReforgedPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (Harvest.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = Harvest.tryHarvestOnReforgedPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (LightningRod.applies(finalFirstPartyPokemon) || Static.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = LightningRod.tryLightningRodOnReforgedPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (MagnetPull.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = MagnetPull.tryMagnetPullOnReforgedPokemon(selectedSpawn, possibleSpawns, player);

                            } else if (StormDrain.applies(finalFirstPartyPokemon)) {

                                selectedSpawn = StormDrain.tryStormDrainOnReforgedPokemon(selectedSpawn, possibleSpawns, player);

                            }
                            if (selectedSpawn == null) return;
                            String[] levelRange = selectedSpawn.getLevelRange().split("-");
                            int max = Integer.parseInt(levelRange[0]);
                            int min = Integer.parseInt(levelRange[1]);
                            String pokemonName = selectedSpawn.getName();
                            EntityPixelmon pokemon;
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
                                        pokemon = PokemonSpec.from(pokemonName).create(player.world);

                                    } else {

                                        String[] split = pokemonName.split("-");
                                        pokemonName = split[0];
                                        String form = "";
                                        for (int f = 1; f < split.length; f++) {

                                            form = form + "-" + split[f];

                                        }

                                        pokemon = PokemonSpec.from(pokemonName).create(player.world);
                                        int pokemonForm = ReforgedFormIndexFromName.getFormNumberFromFormName(pokemonName, form);
                                        pokemon.setForm(pokemonForm);

                                    }

                                } else {

                                    pokemon = PokemonSpec.from(pokemonName).create(player.world);

                                }

                                if (selectedSpawn.getHeldItemID() != null) {

                                    pokemon.getPokemonData().setHeldItem(new ItemStack(Item.getByNameOrId(selectedSpawn.getHeldItemID())));

                                } else {

                                    if (ConfigGetters.heldItemsEnabled) {

                                        ReforgedHeldItemUtils.tryApplyHeldItem(pokemonName, pokemon, finalFirstPartyPokemon);

                                    }

                                }

                                if (selectedSpawn.getTexture() != null) {

                                    pokemon.getPokemonData().setCustomTexture(selectedSpawn.getTexture());

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
                                if (gmax && !pokemon.getPokemonData().canGigantamax()) gmax = false;
                                boolean finalGmax = gmax;
                                BlockPos safeSpawn;
                                if (location.equalsIgnoreCase("air")) {

                                    spawnY = player.getPosition().getY() + yRadius;
                                    safeSpawn = new BlockPos(spawnX, spawnY, spawnZ);

                                } else {

                                    spawnY = player.getPosition().getY();
                                    BlockPos baseSpawn = new BlockPos(spawnX, spawnY, spawnZ);
                                    if (!location.equalsIgnoreCase("underground")) {

                                        if (WorldDimGetter.getDimID(player.world, player) == -1) {

                                            safeSpawn = baseSpawn;

                                        } else {

                                            if (location.contains("water")) {

                                                safeSpawn = new BlockPos(spawnX, player.getPosition().getY(), spawnZ);

                                            } else {

                                                safeSpawn = new BlockPos(spawnX, player.world.getTopSolidOrLiquidBlock(baseSpawn).getY(), spawnZ);

                                            }

                                        }

                                    } else {

                                        safeSpawn = baseSpawn; // not the safest thing to do as Pokemon could spawn in blocks underground, but there's not a lot of space down there anyway

                                    }

                                }
                                Block block = player.world.getBlockState(safeSpawn).getBlock();
                                if (block != Blocks.AIR && block != Blocks.TALLGRASS && block != Blocks.GRASS) {

                                    if (location.equalsIgnoreCase("water") && block != Blocks.WATER &&
                                            location.equalsIgnoreCase("water") && block != Blocks.FLOWING_WATER) {

                                        return;

                                    } else if (pokemon.getBaseStats().getType1() == EnumType.Fire || pokemon.getBaseStats().getType2() == EnumType.Fire) {

                                        if (block != Blocks.LAVA && block != Blocks.FLOWING_LAVA) {

                                            return;

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

                                    if ((int)((double)level + Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(playerX, playerY, playerZ)) / (double)ConfigGetters.blocksBeforePokemonIncrease + Math.random() * 3.0)) > ConfigGetters.maxPokemonScaleLevel) {

                                        level = ConfigGetters.maxPokemonScaleLevel;

                                    } else {

                                        int distance = (int) Math.floor(Math.sqrt(player.world.getSpawnPoint().distanceSq(playerX, playerY, playerZ)));
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
                                pokemon.getPokemonData().setLevel(level);
                                if (Intimidate.applies(finalFirstPartyPokemon) || KeenEye.applies(finalFirstPartyPokemon)) {

                                    pokemon = Intimidate.tryReforgedIntimidate(pokemon, finalFirstPartyPokemon);
                                    if (pokemon == null) return;

                                }
                                pokemon = PokemonUtils.validateReforgedPokemon(pokemon, level);
                                pokemon.setLocationAndAngles(x + RandomHelper.getRandomNumberBetween(2.75f, 6f), y, z + RandomHelper.getRandomNumberBetween(2.75f, 6f),0, 0);
                                pokemon.updateStats();
                                pokemon.setSpawnLocation(pokemon.getDefaultSpawnLocation());
                                if (ConfigGetters.aggressiveChance > 0) {

                                    if (RandomHelper.getRandomChance(ConfigGetters.aggressiveChance)) {

                                        pokemon.setAggression(EnumAggression.aggressive);

                                    }

                                }
                                if (Loader.isModLoaded("betterbosses")) {

                                    if (com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                                        checkBoss = false;

                                    }

                                }
                                float shinyChance = PixelmonConfig.shinyRate;
                                boolean shiny = RandomHelper.getRandomNumberBetween(1, shinyChance) == 1;
                                if (shiny) {

                                    pokemon.getPokemonData().setShiny(true);

                                }
                                if (!pokeModified) {

                                    if (HolidayHandler.activeHolidays != null && !HolidayHandler.activeHolidays.isEmpty()) {

                                        Map<String, List<String>> textureMap = new HashMap<>();
                                        for (Holiday h : HolidayHandler.activeHolidays) {

                                            List<String> textures = new ArrayList<>();
                                            if (h.getPokemon().contains(pokemon.getPokemonName()) || h.getPokemon().contains("shiny") && pokemon.getPokemonData().isShiny()) {

                                                if (textureMap.containsKey(h.getName())) {

                                                    textures = textureMap.get(h.getName());

                                                }

                                                textures.addAll(h.getSpecialTextures());
                                                textureMap.put(h.getName(), textures);

                                            }

                                        }

                                        String holiday;
                                        List<String> holidays = new ArrayList<>();
                                        for (Map.Entry<String, List<String>> entry : textureMap.entrySet()) {

                                            holidays.add(entry.getKey());

                                        }

                                        if (holidays.size() > 0) {

                                            // I know this is probably dumb, but every other time I've tried getting a random index from a list with a size of 1, it throws IndexOutOfBounds so this prevents any chance of that ever happening
                                            if (holidays.size() == 1) {

                                                holiday = holidays.get(0);

                                            } else {

                                                holiday = holidays.get(BetterPixelmonSpawner.random.nextInt(holidays.size()));

                                            }

                                            boolean doMessage = false;
                                            if (textureMap.containsKey(holiday)) {

                                                List<String> possibleTextures = textureMap.get(holiday);
                                                if (possibleTextures.size() > 0) {

                                                    if (RandomHelper.getRandomChance(ConfigGetters.textureChance)) {

                                                        String randomTexture = null;
                                                        for (String t : possibleTextures) {

                                                            if (t.contains(":")) {

                                                                String[] textSplit = t.split(":");
                                                                String species = textSplit[0];
                                                                if (species.contains("-")) {

                                                                    String pokeForm = species.split("-")[1];
                                                                    int formToBe;
                                                                    if (pokeForm.equalsIgnoreCase("alolan")) {

                                                                        formToBe = 1;

                                                                    } else if (pokeForm.equalsIgnoreCase("galarian")) {

                                                                        formToBe = 2;

                                                                    } else {

                                                                        formToBe = 3;

                                                                    }

                                                                    if (pokemon.getPokemonData().getForm() == formToBe && pokemon.getPokemonName().equalsIgnoreCase(species)) {

                                                                        randomTexture = textSplit[1];
                                                                        break;

                                                                    }

                                                                } else {

                                                                    if (pokemon.getPokemonName().equalsIgnoreCase(species)) {

                                                                        randomTexture = textSplit[1];
                                                                        break;

                                                                    }

                                                                }

                                                            }

                                                        }
                                                        if (randomTexture == null) {

                                                            if (possibleTextures.size() == 1) {

                                                                randomTexture = possibleTextures.get(0);

                                                            } else {

                                                                randomTexture = possibleTextures.get(BetterPixelmonSpawner.random.nextInt(possibleTextures.size()));

                                                            }

                                                        }

                                                        pokemon.getPokemonData().setCustomTexture(randomTexture);
                                                        doMessage = true;

                                                    }

                                                }

                                            }
                                            ReforgedHolidaySpawnEvent holidaySpawnEvent = new ReforgedHolidaySpawnEvent(holiday, pokemon, player);
                                            MinecraftForge.EVENT_BUS.post(holidaySpawnEvent);
                                            if (!holidaySpawnEvent.isCanceled()) {

                                                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                    holidaySpawnEvent.getPlayer().world.spawnEntity(holidaySpawnEvent.getPokemon());
                                                    ReforgedPokemonCounter.increment(holidaySpawnEvent.getPokemon(), player.getUniqueID());
                                                    ReforgedPokemonCounter.addPokemon(holidaySpawnEvent.getPokemon(), player.getUniqueID());

                                                });
                                                if (doMessage && !ConfigGetters.holidaySpawnMessage.equalsIgnoreCase("")) {

                                                    pokemon.world.getMinecraftServer().getPlayerList().sendMessage(FancyText.getFormattedText(ConfigGetters.holidaySpawnMessage
                                                            .replace("%holiday%", holiday)
                                                            .replace("%pokemon%", pokemon.getPokemonName())
                                                    ));

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

                                                        ReforgedBossSpawnEvent bossSpawnEvent = new ReforgedBossSpawnEvent(pokemon, player, selectedSpawn);
                                                        MinecraftForge.EVENT_BUS.post(bossSpawnEvent);
                                                        if (!bossSpawnEvent.isCanceled()) {

                                                            pokemon.setBoss(BossPokemonUtils.getReforgedBossMode());
                                                            pokeModified = true;
                                                            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                                                player.world.spawnEntity(bossSpawnEvent.getPokemon());
                                                                ReforgedPokemonCounter.increment(bossSpawnEvent.getPokemon(), player.getUniqueID());
                                                                // Sets this tag for the PokeClear to be able to know what a Boss is, in the event of a "normal" Boss
                                                                bossSpawnEvent.getPokemon().addTag("PixelmonDefaultBoss");
                                                                ReforgedPokemonCounter.addPokemon(bossSpawnEvent.getPokemon(), player.getUniqueID());

                                                            });
                                                            continue;

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }
                                if (shiny || pokemon.getPokemonData().isShiny()) {

                                    ReforgedShinySpawnEvent shinySpawnEvent = new ReforgedShinySpawnEvent(pokemon, player, selectedSpawn);
                                    MinecraftForge.EVENT_BUS.post(shinySpawnEvent);
                                    if (!shinySpawnEvent.isCanceled()) {

                                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                            shinySpawnEvent.getPokemon().getPokemonData().setGigantamaxFactor(finalGmax);
                                            shinySpawnEvent.getPokemon().getPokemonData().setShiny(true);
                                            player.world.spawnEntity(shinySpawnEvent.getPokemon());
                                            ReforgedPokemonCounter.increment(shinySpawnEvent.getPokemon(), player.getUniqueID());
                                            ReforgedPokemonCounter.addPokemon(shinySpawnEvent.getPokemon(), player.getUniqueID());

                                        });

                                    }

                                } else {

                                    ReforgedPokemonSpawnEvent pokemonSpawnEvent = new ReforgedPokemonSpawnEvent(pokemon, player, selectedSpawn, groupSize);
                                    MinecraftForge.EVENT_BUS.post(pokemonSpawnEvent);
                                    if (!pokemonSpawnEvent.isCanceled()) {

                                        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                                            pokemonSpawnEvent.getPokemon().getPokemonData().setGigantamaxFactor(finalGmax);
                                            player.world.spawnEntity(pokemonSpawnEvent.getPokemon());
                                            ReforgedPokemonCounter.increment(pokemonSpawnEvent.getPokemon(), player.getUniqueID());
                                            ReforgedPokemonCounter.addPokemon(pokemonSpawnEvent.getPokemon(), player.getUniqueID());

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
