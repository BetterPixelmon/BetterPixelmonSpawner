package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class PokemonUtils {

    public static PixelmonEntity validatePokemon (PixelmonEntity pixelmon, int spawnLevel) {

        if (!ConfigGetters.validateSpawns) return pixelmon;
        Pokemon pokemon = pixelmon.getPokemon();
        int minimumLevel = pokemon.getForm().minLevel;
        int safetyIndex = 0; // used so the do/while loop will never ever crash a server
        if (spawnLevel < minimumLevel) {

            do {

                if (safetyIndex > 5) break; // no Pokemon has this many evolution stages so if we're looping this much there's something wrong and need to stop
                // Pokemon cannot naturally exist at this level, check for previous evolution forms
                if (pokemon.getForm().getPreEvolutions().size() > 0) {

                    String preEvo = pokemon.getForm().getPreEvolutions().get(0).getName();
                    Pokemon newPokemon = PokemonBuilder.builder().species(preEvo).build();
                    newPokemon.setLevel(spawnLevel);
                    newPokemon.setLevelNum(spawnLevel);
                    newPokemon.setForm(pokemon.getForm());
                    pokemon = newPokemon;
                    safetyIndex++;

                }

            } while (pokemon.getForm().getPreEvolutions().size() > 0);

        }

        pokemon.setLevel(spawnLevel);
        pokemon.setLevelNum(spawnLevel);
        return pokemon.getOrCreatePixelmon();

    }

}
