package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CaptureListener {

    // UUID of the player it was spawned on
    private static Map<UUID, PixelmonEntity> pokemonMap = new HashMap<>();

    @SubscribeEvent
    public void onCatchAttempt (CaptureEvent.StartCapture event) {

        PixelmonEntity pokemon = event.getPokemon();
        ServerPlayerEntity player = event.getPlayer();

        if (PixelmonSpecies.getLegendaries(false).contains(pokemon.getSpecies().getDex()) || PixelmonSpecies.getUltraBeasts().contains(pokemon.getSpecies().getDex()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            for (String tag : pokemon.getTags()) {

                if (tag.contains("LegendaryGracePeriod:")) {

                    String[] split = tag.split(":");
                    String uuid = split[1];
                    if (!uuid.equalsIgnoreCase(player.getUniqueID().toString())) {

                        String name = JoinListener.playerMap.get(UUID.fromString(uuid)).getName().getString();
                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&eGrace Period activated! Only " + name + " can battle this Pokemon!"), player.getUniqueID());
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
                    PokemonCounter.removePokemon(pokemon, uuid);
                    break;

                }

            }

        }

    }

    @SubscribeEvent
    public void onFailedCapture (CaptureEvent.FailedCapture event) {

        PixelmonEntity pokemon = event.getPokemon();
        pokemonMap.entrySet().removeIf(entry -> {

            if (entry.getValue().getUniqueID().toString().equalsIgnoreCase(pokemon.getUniqueID().toString())) {

                PokemonCounter.addPokemon(entry.getValue(), entry.getKey());
                PokemonCounter.increment(entry.getValue(), entry.getKey());
                return true;

            }

            return false;

        });

    }

    @SubscribeEvent
    public void onCatch (CaptureEvent.SuccessfulCapture event) {

        PixelmonEntity pokemon = event.getPokemon();
        if (pokemon.isGlowing()) {

            pokemon.setGlowing(false);

        }
        if (PixelmonSpecies.getLegendaries(false).contains(pokemon.getSpecies().getDex()) || PixelmonSpecies.getUltraBeasts().contains(pokemon.getSpecies().getDex()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        LegendaryListing.updatePokemonStatusCaptured(pokemon, event.getPlayer());

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
