package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class Intimidate {

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Intimidate");

    }

    public static PixelmonEntity tryIntimidate (PixelmonEntity wildPokemon, Pokemon playerPokemon) {

        int level = playerPokemon.getPokemonLevel();
        int spawnLevel = wildPokemon.getLvl().getPokemonLevel();
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

}
