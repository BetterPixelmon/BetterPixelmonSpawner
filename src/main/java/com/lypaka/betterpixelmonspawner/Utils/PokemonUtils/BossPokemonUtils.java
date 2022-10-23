package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.*;

public class BossPokemonUtils {

    private static List<String> possibleBosses = new ArrayList<>();
    public static Map<UUID, Integer> amountMap = new HashMap<>();

    public static void loadBossList() {

        possibleBosses = new ArrayList<>();
        for (Species species : PixelmonSpecies.getAll()) {

            if (PixelmonSpecies.getLegendaries(false).contains(species.getDex())) {

                possibleBosses.add(species.getName());

            }
            if (PixelmonSpecies.getUltraBeasts().contains(species.getDex())) {

                possibleBosses.add(species.getName());

            }
            if (species.getDefaultForm().hasMegaForm()) {

                if (!possibleBosses.contains(species.getName())) {

                    possibleBosses.add(species.getName());

                }

            } else if (ConfigGetters.bossesCanBeNormal) {

                if (!possibleBosses.contains(species.getName())) {

                    possibleBosses.add(species.getName());

                }

            }

        }

    }

    public static boolean spawnBoss() {

        if (ConfigGetters.bossSpawnChance == 0) return false;
        return RandomHelper.getRandomChance(ConfigGetters.bossSpawnChance);

    }

    public static boolean isPossibleBoss (String name) {

        return possibleBosses.contains(name);

    }

    public static BossTier getBossMode() {

        BossTier mode = BossTierRegistry.getBossTierUnsafe("uncommon");
        double sum = ConfigGetters.bossSpawnMap.values().stream().mapToDouble(c -> c).sum();
        double rng = BetterPixelmonSpawner.random.nextDouble() * sum;
        for (Map.Entry<String, Double> entry : ConfigGetters.bossSpawnMap.entrySet()) {

            if (Double.compare(entry.getValue(), rng) <= 0) {

                mode = BossTierRegistry.getBossTierUnsafe(entry.getKey());
                break;

            } else {

                rng -= entry.getValue();

            }

        }

        return mode;

    }

    public static boolean canSpawn (ServerPlayerEntity player) {

        if (!amountMap.containsKey(player.getUniqueID())) return true;
        int amount = amountMap.get(player.getUniqueID());
        return amount < ConfigGetters.maxBosses;

    }

    public static void addCount (UUID uuid) {

        int amount = 0;
        if (amountMap.containsKey(uuid)) {

            amount = amountMap.get(uuid);

        }

        int updated = amount + 1;
        amountMap.put(uuid, updated);

    }

    public static void removeCount (UUID uuid) {

        if (amountMap.containsKey(uuid)) {

            int amount = amountMap.get(uuid);
            int updated = amount - 1;
            amountMap.put(uuid, updated);

        }

    }

}
