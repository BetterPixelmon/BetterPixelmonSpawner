package com.lypaka.betterpixelmonspawner.Config;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.lypakautils.PixelmonHandlers.PixelmonVersionDetector;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUpdater {

    public static void updateConfig() throws ObjectMappingException {

        boolean needsSaving = false;

        /** Version 1.4.1 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Blocks-Before-Level-Increase").isVirtual()) {

            needsSaving = true;
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a Pokemon's level is increased");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a Pokemon's level is increased based on \"Blocks-Before-Level-Increase\"");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Level-Modifier").setValue(1);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a Pokemon can be on");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Scaled-Level").setValue(60);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Scale-Levels-By-Distance").setComment("If true, will scale wild Pokemon levels based on distance from world spawn");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Scale-Levels-By-Distance").setValue(false);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Blocks-Before-Level-Increase").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Blocks-Before-Level-Increase").setComment("If \"Scale-Levels-By-Distance\" = true, how many blocks away from spawn before a NPC Trainer's level is increased");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Blocks-Before-Level-Increase").setValue(30);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Level-Modifier").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Level-Modifier").setComment("If \"Scale-Levels-By-Distance\" = true, by how much a NPC Trainer's level is increased based on \"Blocks-Before-Level-Increase\"");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Level-Modifier").setValue(1);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Max-Scaled-Level").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Max-Scaled-Level").setComment("If \"Scale-Levels-By-Distance\" is enabled, the maximum level a NPC Trainer can be on");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Max-Scaled-Level").setValue(60);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Scale-Levels-By-Distance").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Scale-Levels-By-Distance").setComment("If true, will scale wild NPC Trainer levels based on distance from world spawn");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Scale-Levels-By-Distance").setValue(false);

        }

        /** Version 1.5.0 */
        if (!BetterPixelmonSpawner.configManager.getConfigNode(2, "Generate-Files").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Generate-Files").setValue(null);

        }

        /** Version 1.5.1 */
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts").isVirtual()) {

                if (!needsSaving) {

                    needsSaving = true;

                }
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts").setComment("Sets broadcast messages to go off when a misc entity is spawned");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts", "pixelmon:zygarde_cell").setValue("&dA Zygarde Cell has spawned in a %biome% biome!");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts", "pixelmon:dynamax_energy").setValue("&dA Dynamax Beam has spawned in a %biome% biome!");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts", "pixelmon:wishing_star").setValue("&dA Wishing Star has spawned in a %biome% biome!");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts", "pixelmon:space_time_distortion").setValue("&dA Space Time Distortion has spawned in a %biome% biome!");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts", "pixelmon:mysterious_ring").setValue("&dA Mysterious Ring has spawned in a %biome% biome!");

            }

        }

        /** Version 1.5.5 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(3, "Spawn-Interval-Max").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Spawn-Interval").setValue(null);
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Spawn-Interval-Max").setValue(3600);
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Spawn-Interval-Min").setValue(1800);

        }

        if (BetterPixelmonSpawner.configManager.getConfigNode(6, "Spawner-Filter").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            Map<String, String> uuidMap = new HashMap<>();
            BetterPixelmonSpawner.configManager.getConfigNode(6, "Spawner-Filter").setValue(uuidMap);

        }

        /** Version 1.6.0 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Held-Items").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Held-Items").setComment("If true, will enable the Held Item module which tries to put set held items on Pokemon from the heldItems.conf file");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Held-Items").setValue(true);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "External-Abilities").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "External-Abilities").setComment("If true, will enable the External Abilities module. For more information, see here: https://bulbapedia.bulbagarden.net/wiki/Category:Abilities_that_affect_appearance_of_wild_Pok%C3%A9mon");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "External-Abilities").setValue(true);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Creative").setValue(false);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Ignore-Spectator").setValue(false);

        }

        if (BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Creative").setValue(false);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            BetterPixelmonSpawner.configManager.getConfigNode(3, "Ignore-Spectator").setValue(false);

        }

        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Creative").setValue(false);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Ignore-Spectator").setValue(false);

        }

        if (BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Creative").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Creative").setComment("If true, will ignore (not spawn on) players in creative mode");
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Creative").setValue(false);

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Spectator").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Spectator").setComment("If true, will ignore (not spawn on) players in spectator mode");
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Ignore-Spectator").setValue(false);

        }

        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Enable-Group-Size").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Enable-Group-Size").setComment("If false, will disable the group size system, meaning only 1 Pokemon will spawn from a spawn attempt instead of like 2 or 3, ignoring the group-size setting in the Pokemon's .conf file");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Enable-Group-Size").setValue(true);

        }

        /** Version 1.6.1 */
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Alphas").isVirtual()) {

                if (!needsSaving) {

                    needsSaving = true;

                }
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Alphas").setComment("Sets the maximum allowed amount of Alpha Pokemon spawned per player");
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Alphas").setValue(3);

            }

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Bosses").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Bosses").setComment("Sets the maximum allowed amount of Boss Pokemon spawned per player");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Bosses").setValue(2);

        }
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Totems").isVirtual()) {

                if (!needsSaving) {

                    needsSaving = true;

                }
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Totems").setComment("Sets the maximum allowed amount of Totem Pokemon spawned per player");
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Totems").setValue(3);

            }

        }
        if (BetterPixelmonSpawner.configManager.getConfigNode(5, "Spawn-Interval").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Spawn-Interval").setComment("Sets the interval in which the misc entity spawner runs at, in seconds");
            BetterPixelmonSpawner.configManager.getConfigNode(5, "Spawn-Interval").setValue(240);

        }

        /** Version 1.6.3 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Spawn-Level-Randomization").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }

            BetterPixelmonSpawner.configManager.getConfigNode(2, "Spawn-Level-Randomization").setComment("If true, will apply a little randomization to the spawn level selection (only used if \"Scale-Levels-By-Distance\" = true)");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Spawn-Level-Randomization").setValue(true);
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Spawn-Level-Randomization-Value-Max").setValue(1.15);
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Spawn-Level-Randomization-Value-Min").setValue(0.75);

        }

        /** Version 1.6.5 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Level-Randomization").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }

            BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Level-Randomization").setComment("If true, will apply a little randomization to the spawn level selection (only used if \"Scale-Levels-By-Distance\" = true)");
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Level-Randomization").setValue(true);
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Level-Randomization-Value-Max").setValue(1.15);
            BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Level-Randomization-Value-Min").setValue(0.75);

        }

        /** Version 1.6.6 */
        if (BetterPixelmonSpawner.configManager.getConfigNode(2, "Play-Shiny-Noise").isVirtual()) {

            if (!needsSaving) {

                needsSaving = true;

            }

            BetterPixelmonSpawner.configManager.getConfigNode(2, "Play-Shiny-Noise").setComment("Plays the Pixelmon shiny noise (the one that Ninja wanted and had changed without asking anyone) when a shiny spawns, if set to true");
            BetterPixelmonSpawner.configManager.getConfigNode(2, "Play-Shiny-Noise").setValue(true);

        }

        /** Version 2.0.0 */
        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Reforged")) {

            List<String> blacklist = BetterPixelmonSpawner.configManager.getConfigNode(0, "PokeClear", "Pokemon-Blacklist").getList(TypeToken.of(String.class));
            if (blacklist.contains("totems")) {

                blacklist = new ArrayList<>();
                if (!needsSaving) {

                    needsSaving = true;

                }
                BetterPixelmonSpawner.logger.info("Detected Pixelmon Generations versions of the configs on Pixelmon Reforged, converting....");

                /** generalSettings.conf */
                blacklist.add("legendaries");
                blacklist.add("shinies");
                blacklist.add("bosses");
                blacklist.add("textures");
                BetterPixelmonSpawner.configManager.getConfigNode(0, "PokeClear", "Pokemon-Blacklist").setValue(blacklist);

                /** holidays.conf */
                Map<String, String> emptyMap = new HashMap<>();
                BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays").setComment("See a formatting example here: https://pastebin.com/CiMVU9Kj");
                BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays").setValue(emptyMap);

                /** pokemonSpawner.conf */
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Alpha-Spawn-Chance").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Marks-Enabled").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Alphas").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Max-Totems").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Play-Shiny-Noise").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Remove-Eternatus-From-Normal-Spawner").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Remove-Meltan-From-Normal-Spawner").setValue(null);
                BetterPixelmonSpawner.configManager.getConfigNode(2, "Totem-Spawn-Chance").setValue(null);

                /** legendarySpawner.conf */

                /** npcSpawner.conf */
                Map<String, Double> npcMap = new HashMap<>();
                npcMap.put("pixelmon:npc_chatting", 0.19);
                npcMap.put("pixelmon:npc_nursejoy", 0.08);
                npcMap.put("pixelmon:npc_oldfisherman", 0.04);
                npcMap.put("pixelmon:npc_questgiver", 0.02);
                npcMap.put("pixelmon:npc_relearner", 0.17);
                npcMap.put("pixelmon:npc_shopkeeper", 0.16);
                npcMap.put("pixelmon:npc_trader", 0.11);
                npcMap.put("pixelmon:npc_trainer", 0.19);
                npcMap.put("pixelmon:npc_tutor", 0.19);
                BetterPixelmonSpawner.configManager.getConfigNode(4, "Spawn-Map").setValue(npcMap);

                /** miscSpawner.conf */
                Map<String, String> broadcastMap = new HashMap<>();
                broadcastMap.put("pixelmon:ultra_wormhole", "&dAn Ultra Wormhole has spawned in a %biome% biome!");
                broadcastMap.put("pixelmon:den", "&dA Den has spawn in a %biome% biome!");
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Broadcasts").setValue(broadcastMap);
                Map<String, Double> miscMap = new HashMap<>();
                miscMap.put("pixelmon:ultra_wormhole", 0.12);
                miscMap.put("pixelmon:den", 0.02);
                BetterPixelmonSpawner.configManager.getConfigNode(5, "Spawn-Map").setValue(miscMap);

            }

        }

        if (needsSaving) {

            BetterPixelmonSpawner.configManager.save();

        }

    }

}
