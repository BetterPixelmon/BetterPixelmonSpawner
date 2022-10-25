package com.lypaka.betterpixelmonspawner.Config;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class PokemonConfig {

    private static String[] FILE_NAMES = new String[ConfigGetters.pokemonFiles.size()];
    private static Path dir;
    private static Path[] filePath = new Path[FILE_NAMES.length];
    private static Path filesDir;
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> fileLoad = new ArrayList<>(FILE_NAMES.length);
    private static ArrayList<ConfigurationLoader<CommentedConfigurationNode>> configLoad = new ArrayList<ConfigurationLoader<CommentedConfigurationNode>>(FILE_NAMES.length);
    private static CommentedConfigurationNode[] configNode = new CommentedConfigurationNode[FILE_NAMES.length];
    private static HoconConfigurationLoader configurationLoader;

    public static void init (Path path) {

        dir = path;
        filesDir = checkDir(dir.resolve("pokemon"));

        if (ConfigGetters.pokemonFiles.size() > 0) {

            for (int i = 0; i < ConfigGetters.pokemonFiles.size(); i++) {

                FILE_NAMES[i] = ConfigGetters.pokemonFiles.get(i);

            }

            for (int j = 0; j < FILE_NAMES.length; j++) {

                filePath[j] = filesDir.resolve(FILE_NAMES[j]);

            }

            load();

        }

    }

    public static Path checkDir (Path dir) {

        if (!Files.exists(dir)) {

            try {

                Files.createDirectories(dir);

            } catch (IOException e) {

                throw new RuntimeException("Error creating dir! " + dir, e);

            }

        }

        return dir;

    }

    public static void load() {

        FILE_NAMES = new String[ConfigGetters.pokemonFiles.size()];
        filePath = new Path[FILE_NAMES.length];
        fileLoad = new ArrayList<>(FILE_NAMES.length);
        configLoad = new ArrayList<ConfigurationLoader<CommentedConfigurationNode>>(FILE_NAMES.length);
        configNode = new CommentedConfigurationNode[FILE_NAMES.length];

        try {

            for (int i = 0; i < ConfigGetters.pokemonFiles.size(); i++) {

                FILE_NAMES[i] = ConfigGetters.pokemonFiles.get(i);

            }

            for (int j = 0; j < FILE_NAMES.length; j++) {

                filePath[j] = filesDir.resolve(FILE_NAMES[j]);
                Path filePathString = Paths.get(".//config//betterpixelmonspawner//pokemon//" + FILE_NAMES[j]);
                if (!filePath[j].toFile().exists()) {

                    try {

                        Files.copy(BetterPixelmonSpawner.class.getClassLoader().getResourceAsStream("assets/betterpixelmonspawner/pokemon/" + FILE_NAMES[j]), filePathString, StandardCopyOption.REPLACE_EXISTING);

                    } catch (DirectoryNotEmptyException er) {

                        // do nothing

                    } catch (NullPointerException ner) {

                        String pokemonName;
                        if (FILE_NAMES[j].contains("-")) {

                            if (FILE_NAMES[j].contains("ho-oh")) {

                                pokemonName = "ho-oh";

                            } else if (FILE_NAMES[j].contains("hakomo-o")) {

                                pokemonName = "hakomo-o";

                            } else if (FILE_NAMES[j].contains("jangmo-o")) {

                                pokemonName = "jangmo-o";

                            } else if (FILE_NAMES[j].contains("kommo-o")) {

                                pokemonName = "kommo-o";

                            } else if (FILE_NAMES[j].contains("porygon-z")) {

                                pokemonName = "porygon-z";

                            } else {

                                String[] fileSplit = FILE_NAMES[j].split("-");
                                pokemonName = fileSplit[0];

                            }

                            if (!PixelmonSpecies.has(pokemonName)) {

                                BetterPixelmonSpawner.logger.error("Encountered some kind of error with " + FILE_NAMES[j]);

                            } else {

                                BetterPixelmonSpawner.logger.info("Detected new addition to the Pokemon list, creating file for " + FILE_NAMES[j] + "...");
                                Files.copy(BetterPixelmonSpawner.class.getClassLoader().getResourceAsStream("assets/betterpixelmonspawner/newPokemonTemplate.conf"), filePathString, StandardCopyOption.REPLACE_EXISTING);

                            }

                        }

                    }

                }
                configurationLoader = HoconConfigurationLoader.builder().setPath(filePath[j]).build();
                fileLoad.add(j, configurationLoader);
                configNode[j] = configurationLoader.load();
                configLoad.add(j, fileLoad.get(j));
                configLoad.get(j).save(configNode[j]);

            }

        } catch (IOException e) {

            BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner Pokemon configuration could not load.");
            e.printStackTrace();

        }

    }

    public static void save() {

        for (int j = 0; j < FILE_NAMES.length; j++) {

            try{

                configLoad.get(j).save(configNode[j]);

            }
            catch (IOException e) {

                BetterPixelmonSpawner.logger.error("BetterPixelmonSpawner could not save Pokemon configuration.");
                e.printStackTrace();

            }

        }

    }

    public static int getIndexFromName (String name) {

        int index = 0;
        int i = -1;
        for (String s : FILE_NAMES) {

            if (s.equalsIgnoreCase(name)) {

                i = index;
                break;

            }
            index++;

        }

        return i;

    }

    public static CommentedConfigurationNode getConfigNode (String name, Object... node) {

        return configNode[getIndexFromName(name)].getNode(node);

    }

}
