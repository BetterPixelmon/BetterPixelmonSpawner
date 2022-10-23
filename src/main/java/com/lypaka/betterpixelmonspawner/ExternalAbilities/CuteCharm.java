package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class CuteCharm {

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("CuteCharm") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Cute Charm");

    }

    public static void tryApplyCuteCharmEffect (PixelmonEntity wildPokemon, Pokemon playersPokemon) {

        Gender playerPokemonGender = playersPokemon.getGender();
        if (playerPokemonGender == Gender.NONE) return;
        if (wildPokemon.getPokemon().getGender() == Gender.NONE) return;

        Gender opposite;
        if (playerPokemonGender == Gender.MALE) {

            opposite = Gender.FEMALE;

        } else {

            opposite = Gender.MALE;

        }

        if (wildPokemon.getPokemon().getForm().getMalePercentage() < 100 && wildPokemon.getPokemon().getForm().getMalePercentage() > 0) {

            // pokemon can be both male and female
            if (RandomHelper.getRandomChance(66.67)) {

                wildPokemon.getPokemon().setGender(opposite);

            }

        }

    }

}
