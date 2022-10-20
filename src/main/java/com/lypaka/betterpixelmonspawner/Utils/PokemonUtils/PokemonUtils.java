package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.api.pokemon.PokemonSpec;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;

public class PokemonUtils {

    public static EntityPixelmon validateGenerationsPokemon (EntityPixelmon pokemon, int spawnLevel) {

        if (!ConfigGetters.validateSpawns) return pokemon;
        int minimumLevel = pokemon.baseStats.spawnLevel;
        if (spawnLevel < minimumLevel) {

            do {

                // Pokemon cannot naturally exist at this level, check for previous evolution forms
                if (pokemon.baseStats.preEvolutions.length > 0) {

                    EnumSpecies preEvo = pokemon.baseStats.preEvolutions[pokemon.baseStats.preEvolutions.length - 1];
                    EntityPixelmon newPokemon = PokemonSpec.from(preEvo.getPokemonName()).create(pokemon.world);
                    newPokemon.setForm(pokemon.getForm());
                    pokemon = newPokemon;

                }

            } while (pokemon.baseStats.preEvolutions.length > 0);

        }

        return pokemon;

    }

    public static com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon validateReforgedPokemon (com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon pokemon, int spawnLevel) {

        if (!ConfigGetters.validateSpawns) return pokemon;
        int minimumLevel = pokemon.getBaseStats().spawnLevel;
        if (spawnLevel < minimumLevel) {

            do {

                // Pokemon cannot naturally exist at this level, check for previous evolution forms
                if (pokemon.getBaseStats().preEvolutions.length > 0) {

                    String preEvo = pokemon.getBaseStats().preEvolutions[pokemon.getBaseStats().preEvolutions.length - 1];
                    com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon newPokemon = com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec.from(preEvo).create(pokemon.world);
                    newPokemon.setForm(pokemon.getPokemonData().getForm());
                    pokemon = newPokemon;

                }

            } while (pokemon.getBaseStats().preEvolutions.length > 0);

        }

        return pokemon;

    }

}
