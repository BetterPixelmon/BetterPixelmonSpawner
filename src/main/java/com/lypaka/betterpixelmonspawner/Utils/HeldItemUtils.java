package com.lypaka.betterpixelmonspawner.Utils;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.CompoundEyes;
import com.lypaka.betterpixelmonspawner.ExternalAbilities.SuperLuck;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.item.ItemStack;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class HeldItemUtils {

    public static Map<String, Map<String, List<String>>> heldItemMap = new HashMap<>();

    public static void load() throws ObjectMappingException {

        // index 9
        heldItemMap = new HashMap<>();
        BetterPixelmonSpawner.logger.info("Registering wild Pokemon held items...");
        for (String name : ConfigGetters.pokemonFiles) {

            name = name.replace(".conf", "");
            if (!BetterPixelmonSpawner.configManager.getConfigNode(9, name).isVirtual()) {

                Map<String, List<String>> items = BetterPixelmonSpawner.configManager.getConfigNode(9, name).getValue(new TypeToken<Map<String, List<String>>>() {});
                heldItemMap.put(name, items);

            }

        }

    }

    public static void tryApplyHeldItem (String fileName, PixelmonEntity wildPokemon, Pokemon playersPokemon) {

        if (!heldItemMap.containsKey(fileName)) return;

        Map<String, List<String>> possibleItems = null;
        for (Map.Entry<String, Map<String, List<String>>> entry : heldItemMap.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(fileName)) {

                possibleItems = entry.getValue();
                break;

            }

        }
        if (possibleItems == null) return;
        ItemStack heldItem = null;
        List<Integer> percents = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

            int percent = Integer.parseInt(entry.getKey().replace("%", ""));
            percents.add(percent);

        }

        // Checking possible held items in order from rarest to most common
        Collections.sort(percents);
        for (int i = 0; i < percents.size(); i++) {

            int percent = percents.get(i);
            if (percent == 1) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 5;

                }

            } else if (percent == 5) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 20;

                }

            } else if (percent == 50) {

                if (CompoundEyes.applies(playersPokemon) || SuperLuck.applies(playersPokemon)) {

                    percent = 60;

                }

            }

            if (RandomHelper.getRandomChance(percent)) {

                List<String> ids = possibleItems.get(percent + "%");
                String id = RandomHelper.getRandomElementFromList(ids);
                heldItem = ItemStackBuilder.buildFromStringID(id);
                heldItem.setCount(1);
                wildPokemon.getPokemon().setHeldItem(heldItem);
                wildPokemon.update(EnumUpdateType.HeldItem);
                break;

            }

        }

    }

}
