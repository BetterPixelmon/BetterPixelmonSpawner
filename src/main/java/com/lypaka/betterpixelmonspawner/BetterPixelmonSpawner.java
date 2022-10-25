package com.lypaka.betterpixelmonspawner;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.ConfigUpdater;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZone;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
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

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterpixelmonspawner")
public class BetterPixelmonSpawner {

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

    public BetterPixelmonSpawner() throws IOException, ObjectMappingException {

        dir = ConfigUtils.checkDir(Paths.get("./config/betterpixelmonspawner"));
        String[] files = new String[]{"generalSettings.conf", "holidays.conf", "pokemonSpawner.conf",
                "legendarySpawner.conf", "npcSpawner.conf", "miscSpawner.conf", "storage.conf", "deadZones.conf",
                "last-spawn-list.conf", "heldItems.conf", "pokemonFiles.conf"};
        configManager = new BasicConfigManager(files, dir, BetterPixelmonSpawner.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        loadRegionalLists();
        ConfigGetters.load();
        PokemonConfig.init(dir);
        ConfigUpdater.updateConfig();

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
