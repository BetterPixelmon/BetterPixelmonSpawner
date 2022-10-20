package com.lypaka.betterpixelmonspawner.Listeners.Generations;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.Counters.GenerationsPokemonCounter;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.betterpixelmonspawner.Utils.Generations.GenerationsLegendaryListing;
import com.pixelmongenerations.api.events.CaptureEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerationsCaptureListener {

    // UUID of the player it was spawned on
    private static Map<UUID, EntityPixelmon> pokemonMap = new HashMap<>();

    @SubscribeEvent
    public void onCatchAttempt (CaptureEvent.StartCaptureEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        EntityPlayerMP player = event.getPlayer();

        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            for (String tag : pokemon.getTags()) {

                if (tag.contains("LegendaryGracePeriod:")) {

                    String[] split = tag.split(":");
                    String uuid = split[1];
                    if (!uuid.equalsIgnoreCase(player.getUniqueID().toString())) {

                        String name = JoinListener.playerMap.get(UUID.fromString(uuid)).getName();
                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&eGrace Period activated! Only " + name + " can battle this Pokemon!"));
                        break;

                    }

                }

            }

        } else {

            // handles that temporary despawning of a Pokemon when its in a ball
            for (String tag : pokemon.getTags()) {

                if (tag.contains("SpawnedPlayerUUID:")) {

                    String[] split = tag.split(":");
                    UUID uuid = UUID.fromString(split[1]);
                    pokemonMap.put(uuid, pokemon);
                    GenerationsPokemonCounter.removePokemon(pokemon, uuid);
                    break;

                }

            }

        }

    }

    @SubscribeEvent
    public void onFailedCapture (CaptureEvent.FailedCaptureEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        pokemonMap.entrySet().removeIf(entry -> {

            if (entry.getValue().getUniqueID().toString().equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                GenerationsPokemonCounter.addPokemon(entry.getValue(), entry.getKey());
                GenerationsPokemonCounter.increment(entry.getValue(), entry.getKey());
                return true;

            }

            return false;

        });

    }

    @SubscribeEvent
    public void onCatch (CaptureEvent.SuccessfulCaptureEvent event) {

        EntityPixelmon pokemon = event.getPokemon();
        if (pokemon.isGlowing()) {

            pokemon.setGlowing(false);

        }
        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        GenerationsLegendaryListing.updatePokemonStatusCaptured(pokemon, event.getPlayer());

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }
        pokemonMap.entrySet().removeIf(entry -> {

            if (entry.getValue().getUniqueID().toString().equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                return true;

            }

            return false;

        });

        pokemon.getTags().removeIf(tag -> tag.contains("SpawnedPlayerUUID:"));

    }

}
