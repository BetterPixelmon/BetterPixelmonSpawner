package com.lypaka.betterpixelmonspawner.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigUpdater {

    public static void updateConfig() {

        boolean needsSaving = false;

        /** Version 1.4.1 */
        if (ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").isVirtual()) {

            needsSaving = true;
            ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a Pokemon's level is increased");
            ConfigManager.getConfigNode(2, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (ConfigManager.getConfigNode(2, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a Pokemon's level is increased based on \"Blocks-Before-Level-Increase\"");
            ConfigManager.getConfigNode(2, "Level-Modifier").setValue(1);

        }
        if (ConfigManager.getConfigNode(2, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a Pokemon can be on");
            ConfigManager.getConfigNode(2, "Max-Scaled-Level").setValue(60);

        }
        if (ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").setComment("If true, will scale wild Pokemon levels based on distance from world spawn");
            ConfigManager.getConfigNode(2, "Scale-Levels-By-Distance").setValue(false);

        }
        if (ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a NPC Trainer's level is increased");
            ConfigManager.getConfigNode(4, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (ConfigManager.getConfigNode(4, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a NPC Trainer's level is increased based on \"Blocks-Before-Level-Increase\"");
            ConfigManager.getConfigNode(4, "Level-Modifier").setValue(1);

        }
        if (ConfigManager.getConfigNode(4, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a NPC Trainer can be on");
            ConfigManager.getConfigNode(4, "Max-Scaled-Level").setValue(60);

        }
        if (ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").setComment("If true, will scale wild NPC Trainer levels based on distance from world spawn");
            ConfigManager.getConfigNode(4, "Scale-Levels-By-Distance").setValue(false);

        }

        /** Version 1.5.0 */
        if (!ConfigManager.getConfigNode(2, "Generate-Files").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(2, "Generate-Files").setValue(null);

        }

        /** Version 1.5.1 */
        if (ConfigManager.getConfigNode(5, "Broadcasts").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(5, "Broadcasts").setComment("Sets broadcast messages to go off when a misc entity is spawned");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:zygarde_cell").setValue("&dA Zygarde Cell has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:dynamax_energy").setValue("&dA Dynamax Beam has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:wishing_star").setValue("&dA Wishing Star has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:space_time_distortion").setValue("&dA Space Time Distortion has spawned in a %biome% biome!");
            ConfigManager.getConfigNode(5, "Broadcasts", "pixelmon:mysterious_ring").setValue("&dA Mysterious Ring has spawned in a %biome% biome!");

        }

        /** Version 1.5.5 */
        if (ConfigManager.getConfigNode(3, "Spawn-Interval-Max").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            ConfigManager.getConfigNode(3, "Spawn-Interval").setValue(null);
            ConfigManager.getConfigNode(3, "Spawn-Interval-Max").setValue(3600);
            ConfigManager.getConfigNode(3, "Spawn-Interval-Min").setValue(1800);

        }

        if (ConfigManager.getConfigNode(6, "Spawner-Filter").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            Map<String, String> uuidMap = new HashMap<>();
            ConfigManager.getConfigNode(6, "Spawner-Filter").setValue(uuidMap);

        }

        if (needsSaving) {

            ConfigManager.save();

        }

    }

}
