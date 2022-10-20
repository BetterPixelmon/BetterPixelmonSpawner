package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.FishingSpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.lypakautils.PixelmonHandlers.PixelmonVersionDetector;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class Hustle {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Hustle");

    }

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Hustle");

    }

    public static int tryApplyHustle (int level, PokemonSpawnInfo info) {

        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (!RandomHelper.getRandomChance(50)) return level;

        } else {

            if (!com.pixelmonmod.pixelmon.RandomHelper.getRandomChance(50)) return level;

        }

        String[] levelRange = info.getLevelRange().split("-");
        return Integer.parseInt(levelRange[0]);

    }

    public static int tryApplyHustle (int level, LegendarySpawnInfo info) {

        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (!RandomHelper.getRandomChance(50)) return level;

        } else {

            if (!com.pixelmonmod.pixelmon.RandomHelper.getRandomChance(50)) return level;

        }
        String[] levelRange = info.getLevelRange().split("-");
        return Integer.parseInt(levelRange[0]);

    }

    public static int tryApplyHustle (int level, FishingSpawnInfo info) {

        if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

            if (!RandomHelper.getRandomChance(50)) return level;

        } else {

            if (!com.pixelmonmod.pixelmon.RandomHelper.getRandomChance(50)) return level;

        }
        String[] levelRange = info.getLevelRange().split("-");
        return Integer.parseInt(levelRange[0]);

    }

}
