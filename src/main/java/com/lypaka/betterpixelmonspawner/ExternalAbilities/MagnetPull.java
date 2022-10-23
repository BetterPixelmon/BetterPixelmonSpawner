package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.*;

public class MagnetPull {

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("MagnetPull") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Magnet Pull");

    }

    public static LegendarySpawnInfo tryMagnetPullOnLegendary (LegendarySpawnInfo originalSpawn, List<LegendarySpawnInfo> possibleSpawns) {

        if (!RandomHelper.getRandomChance(50)) return originalSpawn;
        List<LegendarySpawnInfo> steelPokemon = new ArrayList<>();
        for (LegendarySpawnInfo info : possibleSpawns) {

            String pokemonName = info.getName();
            pokemonName = pokemonName.replace(".conf", "");
            PixelmonEntity pokemon;
            if (pokemonName.contains("-")) {

                if (pokemonName.equalsIgnoreCase("porygon-z")) {

                    pokemonName = "porygon-z";
                    pokemon = PokemonBuilder.builder()
                            .species(pokemonName)
                            .build()
                            .getOrCreatePixelmon();

                } else {

                    String[] split = pokemonName.split("-");
                    pokemonName = split[0];
                    String form = "";
                    for (int f = 1; f < split.length; f++) {

                        form = form + "_" + split[f];

                    }

                    form = form.substring(1); // removes that first _ from the String
                    pokemon = PokemonBuilder.builder()
                            .species(pokemonName)
                            .build()
                            .getOrCreatePixelmon();
                    pokemon.setForm(form);

                }

            } else {

                pokemon = PokemonBuilder.builder()
                        .species(pokemonName)
                        .build()
                        .getOrCreatePixelmon();

            }

            for (Element type : pokemon.getPokemon().getForm().getTypes()) {

                if (type == Element.STEEL) {

                    steelPokemon.add(info);
                    break;

                }

            }

        }

        if (steelPokemon.size() == 0) {

            return originalSpawn;

        } else {

            Map<Double, UUID> spawnChanceMap = new HashMap<>();
            Map<UUID, LegendarySpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
            List<Double> spawnChances = new ArrayList<>(steelPokemon.size());
            int spawnIndex = 0;
            double spawnChanceModifier = 1.0;
            for (LegendarySpawnInfo info : steelPokemon) {

                UUID randUUID = UUID.randomUUID();
                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                spawnChanceMap.put(spawnChance, randUUID);
                pokemonSpawnInfoMap.put(randUUID, info);
                spawnChances.add(spawnIndex, spawnChance);
                spawnIndex++;

            }
            Collections.sort(spawnChances);
            int spawnAmount = steelPokemon.size();
            LegendarySpawnInfo selectedSpawn = null;
            List<LegendarySpawnInfo> names = new ArrayList<>();
            for (int i = 0; i < spawnAmount; i++) {

                double spawnChance = spawnChances.get(i);
                UUID uuid = spawnChanceMap.get(spawnChance);
                LegendarySpawnInfo info = pokemonSpawnInfoMap.get(uuid);
                if (RandomHelper.getRandomChance(spawnChance)) {

                    if (!names.contains(info)) {

                        names.add(info);

                    }

                }

            }
            if (names.size() > 1) {

                selectedSpawn = RandomHelper.getRandomElementFromList(names);

            } else if (names.size() == 1) {

                selectedSpawn = names.get(0);

            }

            return selectedSpawn;

        }

    }

    public static PokemonSpawnInfo tryMagnetPullOnPokemon (PokemonSpawnInfo originalSpawn, List<PokemonSpawnInfo> possibleSpawns) {

        if (!RandomHelper.getRandomChance(50)) return originalSpawn;
        List<PokemonSpawnInfo> steelPokemon = new ArrayList<>();
        for (PokemonSpawnInfo info : possibleSpawns) {

            String pokemonName = info.getName();
            pokemonName = pokemonName.replace(".conf", "");
            PixelmonEntity pokemon;
            if (pokemonName.contains("-")) {

                if (pokemonName.equalsIgnoreCase("porygon-z")) {

                    pokemonName = "porygon-z";
                    pokemon = PokemonBuilder.builder()
                            .species(pokemonName)
                            .build()
                            .getOrCreatePixelmon();

                } else {

                    String[] split = pokemonName.split("-");
                    pokemonName = split[0];
                    String form = "";
                    for (int f = 1; f < split.length; f++) {

                        form = form + "_" + split[f];

                    }

                    form = form.substring(1); // removes that first _ from the String
                    pokemon = PokemonBuilder.builder()
                            .species(pokemonName)
                            .build()
                            .getOrCreatePixelmon();
                    pokemon.setForm(form);

                }

            } else {

                pokemon = PokemonBuilder.builder()
                        .species(pokemonName)
                        .build()
                        .getOrCreatePixelmon();

            }

            for (Element type : pokemon.getPokemon().getForm().getTypes()) {

                if (type == Element.STEEL) {

                    steelPokemon.add(info);
                    break;

                }

            }

        }

        if (steelPokemon.size() == 0) {

            return originalSpawn;

        } else {

            Map<Double, UUID> spawnChanceMap = new HashMap<>();
            Map<UUID, PokemonSpawnInfo> pokemonSpawnInfoMap = new HashMap<>();
            List<Double> spawnChances = new ArrayList<>(steelPokemon.size());
            int spawnIndex = 0;
            double spawnChanceModifier = 1.0;
            for (PokemonSpawnInfo info : steelPokemon) {

                UUID randUUID = UUID.randomUUID();
                double spawnChance = info.getSpawnChance() * spawnChanceModifier;
                spawnChanceMap.put(spawnChance, randUUID);
                pokemonSpawnInfoMap.put(randUUID, info);
                spawnChances.add(spawnIndex, spawnChance);
                spawnIndex++;

            }
            Collections.sort(spawnChances);
            int spawnAmount = steelPokemon.size();
            PokemonSpawnInfo selectedSpawn = null;
            List<PokemonSpawnInfo> names = new ArrayList<>();
            for (int i = 0; i < spawnAmount; i++) {

                double spawnChance = spawnChances.get(i);
                UUID uuid = spawnChanceMap.get(spawnChance);
                PokemonSpawnInfo info = pokemonSpawnInfoMap.get(uuid);
                if (RandomHelper.getRandomChance(spawnChance)) {

                    if (!names.contains(info)) {

                        names.add(info);

                    }

                }

            }
            if (names.size() > 1) {

                selectedSpawn = RandomHelper.getRandomElementFromList(names);

            } else if (names.size() == 1) {

                selectedSpawn = names.get(0);

            }

            return selectedSpawn;

        }

    }

}
