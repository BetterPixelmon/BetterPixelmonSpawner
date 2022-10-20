package com.lypaka.betterpixelmonspawner;

import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZoneRegistry;
import com.lypaka.betterpixelmonspawner.Commands.PixelmonSpawnerCommand;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigUpdater;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.Listeners.*;
import com.lypaka.betterpixelmonspawner.Listeners.Generations.*;
import com.lypaka.betterpixelmonspawner.Listeners.Reforged.*;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.Generations.*;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedLegendarySpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedMiscSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedNPCSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedPokemonSpawner;
import com.lypaka.betterpixelmonspawner.Utils.Generations.GenerationsHeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.Reforged.ReforgedHeldItemUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.PixelmonHandlers.PixelmonVersionDetector;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod(
        modid = BetterPixelmonSpawner.MOD_ID,
        name = BetterPixelmonSpawner.MOD_NAME,
        version = BetterPixelmonSpawner.VERSION,
        acceptableRemoteVersions = "*",
        dependencies = "required-after:lypakautils@[0.0.5,);required-after:pixelmon"
)
public class BetterPixelmonSpawner {

    /**
     * WAIT
     *
     * Before you go any further in browsing through my code
     *
     * Just know that
     *
     * I'm fully aware
     *
     * That this mod is extremely fucked up.
     *
     * I'm just not gonna be bothered making it clean for two dead versions of Pixelmon. If it works, it works. :shrug:
     */
    public static final String MOD_ID = "betterpixelmonspawner";
    public static final String MOD_NAME = "BetterPixelmonSpawner";
    public static final String VERSION = "2.0.0";
    public static Logger logger = LogManager.getLogger("Better Pixelmon Spawner");
    public static Path dir;
    public static List<String> alolans = new ArrayList<>();
    public static List<String> galarians = new ArrayList<>();
    public static List<String> hisuians = new ArrayList<>();
    public static Random random = new Random();
    public static LocalDate currentDay = LocalDate.now();
    public static MinecraftServer server;
    public static List<DeadZone> deadZones = new ArrayList<>();
    public static boolean isPixelBoostersLoaded = false;
    public static BasicConfigManager configManager;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) throws ObjectMappingException, IOException {

        dir = ConfigUtils.checkDir(Paths.get("./config/betterpixelmonspawner"));
        String[] files = new String[]{"generalSettings.conf", "holidays.conf", "pokemonSpawner.conf",
                "legendarySpawner.conf", "npcSpawner.conf", "miscSpawner.conf", "storage.conf", "deadZones.conf",
                "last-spawn-list.conf", "heldItems.conf"};
        configManager = new BasicConfigManager(files, dir, BetterPixelmonSpawner.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        loadRegionalLists();

    }

    @Mod.EventHandler
    public void onServerStarting (FMLServerStartingEvent event) throws ObjectMappingException {

        event.registerServerCommand(new PixelmonSpawnerCommand());

        // Loads holidays from config, not worth having config getters for it since it only loads on startup unless reload command is ran
        HolidayHandler.loadHolidays();

    }

    @Mod.EventHandler
    public void onServerStarted (FMLServerStartedEvent event) throws ObjectMappingException {

        // Doing this here so it can use the Utils mod's Pixelmon version detector
        ConfigUpdater.updateConfig();
        ConfigGetters.load();
        PokemonConfig.setup(dir.resolve("pokemon"), PixelmonVersionDetector.VERSION);

        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            // Loads in the held item registry for all Pokemon that have held item data
            GenerationsHeldItemUtils.load();

        } else {

            ReforgedHeldItemUtils.load();

        }

        logger.info("Registering Pokemon spawns...");
        InfoRegistry.loadPokemonSpawnData();
        logger.info("Registering Boss Pokemon spawns...");
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (Loader.isModLoaded("betterbosses")) {

                if (!com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                    BossPokemonUtils.loadGenerationsBossList();

                }

            } else {

                BossPokemonUtils.loadGenerationsBossList();

            }

        } else {

            BossPokemonUtils.loadReforgedBossList();

        }

        if (Loader.isModLoaded("pixelboosters")) {

            isPixelBoostersLoaded = true;
            logger.info("Detected PixelBoosters, integrating...");

        }
        server = FMLCommonHandler.instance().getMinecraftServerInstance();
        MinecraftForge.EVENT_BUS.register(new RespawnListener());
        MinecraftForge.EVENT_BUS.register(new CommandListener());
        MinecraftForge.EVENT_BUS.register(new JoinListener());
        logger.info("Starting spawners...");
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            GenerationsPokemonSpawner.startTimer();
            GenerationsLegendarySpawner.startTimer();
            GenerationsNPCSpawner.startTimer();
            GenerationsMiscSpawner.startTimer();
            MinecraftForge.EVENT_BUS.register(new GenerationsBattleListener());
            MinecraftForge.EVENT_BUS.register(new GenerationsCaptureListener());
            MinecraftForge.EVENT_BUS.register(new GenerationsDefeatListener());
            MinecraftForge.EVENT_BUS.register(new DespawnListener());
            MinecraftForge.EVENT_BUS.register(new GenerationsPokemonSpawnListener());
            MinecraftForge.EVENT_BUS.register(new GenerationsShinySpawnListener());
            MinecraftForge.EVENT_BUS.register(new GenerationsFishSpawner());

        } else {

            // Disables Reforged's fucked up natural spawners
            SpawnerCoordinator.PROCESSOR.shutdown();
            PixelmonSpawning.coordinator.deactivate();
            PixelmonSpawning.coordinator = null;

            ReforgedPokemonSpawner.startTimer();
            ReforgedLegendarySpawner.startTimer();
            ReforgedNPCSpawner.startTimer();
            ReforgedMiscSpawner.startTimer();
            Pixelmon.EVENT_BUS.register(new ReforgedBattleListener());
            Pixelmon.EVENT_BUS.register(new ReforgedCaptureListener());
            Pixelmon.EVENT_BUS.register(new ReforgedDefeatListener());
            Pixelmon.EVENT_BUS.register(new ReforgedPokemonSpawnListener());
            Pixelmon.EVENT_BUS.register(new ReforgedShinySpawnListener());

        }

        ClearTask.startClearTask();

        // Loads the areas
        DeadZoneRegistry.loadAreas();

    }

    @Mod.EventHandler
    public void onShutDown (FMLServerStoppingEvent event) {

        configManager.getConfigNode(6, "Legendary-Opt-Out").setValue(ConfigGetters.legendaryOptOut);
        configManager.getConfigNode(6, "Misc-Opt-Out").setValue(ConfigGetters.miscOptOut);
        configManager.getConfigNode(6, "NPC-Opt-Out").setValue(ConfigGetters.npcOptOut);
        configManager.getConfigNode(6, "Pokemon-Opt-Out").setValue(ConfigGetters.pokemonOptOut);
        configManager.getConfigNode(6, "Spawner-Filter").setValue(ConfigGetters.locationMap);
        configManager.save();

    }

    private static void loadRegionalLists() {

        alolans.add("Rattata");
        alolans.add("Raticate");
        alolans.add("Raichu");
        alolans.add("Sandshrew");
        alolans.add("Sandslash");
        alolans.add("Vulpix");
        alolans.add("Ninetales");
        alolans.add("Diglett");
        alolans.add("Dugtrio");
        alolans.add("Meowth");
        alolans.add("Persian");
        alolans.add("Geodude");
        alolans.add("Graveler");
        alolans.add("Golem");
        alolans.add("Grimer");
        alolans.add("Muk");
        alolans.add("Exeggutor");
        alolans.add("Marowak");

        galarians.add("Meowth");
        galarians.add("Ponyta");
        galarians.add("Rapidash");
        galarians.add("Slowpoke");
        galarians.add("Slowbro");
        galarians.add("Farfetchd");
        galarians.add("Weezing");
        galarians.add("MrMime");
        galarians.add("Articuno");
        galarians.add("Zapdos");
        galarians.add("Moltres");
        galarians.add("Slowking");
        galarians.add("Corsola");
        galarians.add("Zigzagoon");
        galarians.add("Linoone");
        galarians.add("Darumaka");
        galarians.add("Darmanitan");
        galarians.add("Yamask");
        galarians.add("Stunfisk");

        hisuians.add("Growlithe");
        hisuians.add("Arcanine");
        hisuians.add("Voltorb");
        hisuians.add("Electrode");
        hisuians.add("Typhlosion");
        hisuians.add("Qwilfish");
        hisuians.add("Sneasel");
        hisuians.add("Samurott");
        hisuians.add("Lilligant");
        hisuians.add("Zorua");
        hisuians.add("Zoroark");
        hisuians.add("Braviary");
        hisuians.add("Sliggoo");
        hisuians.add("Goodra");
        hisuians.add("Avalugg");
        hisuians.add("Decidueye");

    }

}
