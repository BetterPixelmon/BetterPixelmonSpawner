package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class Intimidate {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Intimidate");

    }

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Intimidate");

    }

    public static EntityPixelmon tryGenerationsIntimidate (EntityPixelmon wildPokemon, EntityPixelmon playerPokemon) {

        int level = playerPokemon.getLvl().getLevel();
        int spawnLevel = wildPokemon.getLvl().getLevel();
        if (level > spawnLevel) {

            int difference = level - spawnLevel;
            if (difference >= 5) {

                if (RandomHelper.getRandomChance(50)) {

                    wildPokemon = null;

                }

            }

        }

        return wildPokemon;

    }

    public static com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon tryReforgedIntimidate (com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon wildPokemon, Pokemon playerPokemon) {

        int level = playerPokemon.getLevel();
        int spawnLevel = wildPokemon.getLvl().getLevel();
        if (level > spawnLevel) {

            int difference = level - spawnLevel;
            if (difference >= 5) {

                if (com.pixelmonmod.pixelmon.RandomHelper.getRandomChance(50)) {

                    wildPokemon = null;

                }

            }

        }

        return wildPokemon;

    }

}
