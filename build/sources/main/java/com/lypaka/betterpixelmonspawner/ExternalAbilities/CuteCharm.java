package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.stats.Gender;
import com.pixelmongenerations.core.util.helper.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class CuteCharm {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Cute Charm");

    }

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Cute Charm");

    }

    public static void tryApplyCuteCharmEffect (EntityPixelmon wildPokemon, EntityPixelmon playersPokemon) {

        Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == Gender.None) return;
        if (wildPokemon.getGender() == Gender.None) return;

        Gender opposite;
        if (playerPokemonGender == Gender.Male) {

            opposite = Gender.Female;

        } else {

            opposite = Gender.Male;

        }

        if (wildPokemon.baseStats.malePercent < 100 && wildPokemon.baseStats.malePercent > 0) {

            // pokemon can be both male and female
            if (RandomHelper.getRandomChance(66.67)) {

                wildPokemon.setGender(opposite);

            }

        }

    }

    public static void tryApplyCuteCharmEffect (com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon wildPokemon, Pokemon playersPokemon) {

        com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.None) return;
        if (wildPokemon.getPokemonData().getGender() == com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.None) return;

        com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender opposite;
        if (playerPokemonGender == com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.Male) {

            opposite = com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.Female;

        } else {

            opposite = com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender.Male;

        }

        if (wildPokemon.getBaseStats().malePercent < 100 && wildPokemon.getBaseStats().malePercent > 0) {

            // pokemon can be both male and female
            if (RandomHelper.getRandomChance(66.67)) {

                wildPokemon.getPokemonData().setGender(opposite);

            }

        }

    }

}
