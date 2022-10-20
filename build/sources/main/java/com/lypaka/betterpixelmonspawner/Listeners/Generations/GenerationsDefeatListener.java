package com.lypaka.betterpixelmonspawner.Listeners.Generations;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.GenerationsPokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.Generations.GenerationsLegendaryListing;
import com.pixelmongenerations.api.events.BeatWildPixelmonEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.drops.EnumDropHandler;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.UUID;

public class GenerationsDefeatListener {

    @SubscribeEvent
    public void onDefeat (BeatWildPixelmonEvent event) {

        EntityPixelmon pokemon = event.getWildPokemon().controlledPokemon.get(0).pokemon;
        for (String tag : pokemon.getTags()) {

            if (tag.contains("SpawnedPlayerUUID:")) {

                String[] split = tag.split(":");
                UUID uuid = UUID.fromString(split[1]);
                GenerationsPokemonCounter.decrement(uuid);
                break;

            }

        }
        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        GenerationsLegendaryListing.updatePokemonStatusKilled(pokemon, event.getPlayer());

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }

        if (!pokemon.getTags().stream().anyMatch(s -> s.contains("BossPokemon:Tier-"))) {

            EnumDropHandler.dropFrom(pokemon, event.getPlayer());

        }

    }

}
