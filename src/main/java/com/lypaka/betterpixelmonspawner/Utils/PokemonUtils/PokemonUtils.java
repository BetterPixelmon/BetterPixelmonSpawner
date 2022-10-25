package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class PokemonUtils {

    public static PixelmonEntity validatePokemon (PixelmonEntity pokemon, int spawnLevel) {

        if (!ConfigGetters.validateSpawns) return pokemon;
        int minimumLevel = pokemon.getForm().minLevel;
        if (spawnLevel < minimumLevel) {

            do {

                // Pokemon cannot naturally exist at this level, check for previous evolution forms
                if (pokemon.getForm().getPreEvolutions().size() > 0) {

                    String preEvo = pokemon.getForm().getPreEvolutions().get(0).getName();
                    PixelmonEntity newPokemon = PokemonBuilder.builder().species(preEvo).build().getOrCreatePixelmon();
                    newPokemon.getPokemon().setLevel(spawnLevel);
                    newPokemon.getPokemon().setLevelNum(spawnLevel);
                    newPokemon.setForm(pokemon.getPokemon().getForm());
                    pokemon = newPokemon;

                }

            } while (pokemon.getForm().getPreEvolutions().size() > 0);

        }

        pokemon.getPokemon().setLevel(spawnLevel);
        pokemon.getPokemon().setLevelNum(spawnLevel);
        return pokemon;

    }

}
