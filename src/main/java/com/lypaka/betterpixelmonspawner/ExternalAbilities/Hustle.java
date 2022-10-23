package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;

public class Hustle {

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Hustle");

    }

    public static int tryApplyHustle (int level, PokemonSpawnInfo info) {

        if (!RandomHelper.getRandomChance(50)) return level;

        String[] levelRange = info.getLevelRange().split("-");
        return Integer.parseInt(levelRange[0]);

    }

    public static int tryApplyHustle (int level, LegendarySpawnInfo info) {

        if (!RandomHelper.getRandomChance(50)) return level;

        String[] levelRange = info.getLevelRange().split("-");
        return Integer.parseInt(levelRange[0]);

    }

}
