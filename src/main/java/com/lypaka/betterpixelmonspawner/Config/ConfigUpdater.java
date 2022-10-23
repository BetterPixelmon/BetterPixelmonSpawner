package com.lypaka.betterpixelmonspawner.Config;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUpdater {

    public static void updateConfig() throws ObjectMappingException {

        boolean needsSaving = false;

        /** Version 2.0.0 */
        List<String> blacklist = BetterPixelmonSpawner.configManager.getConfigNode(0, "PokeClear", "Pokemon-Blacklist").getList(TypeToken.of(String.class));
        if (blacklist.contains("totems")) {

            blacklist = new ArrayList<>();
            if (!needsSaving) {

                needsSaving = true;

            }
            BetterPixelmonSpawner.logger.info("Detected 1.12.2 Pixelmon Generations versions of the configs on 1.16.5 Pixelmon Reforged, converting....");

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

        if (needsSaving) {

            BetterPixelmonSpawner.configManager.save();

        }

    }

}
