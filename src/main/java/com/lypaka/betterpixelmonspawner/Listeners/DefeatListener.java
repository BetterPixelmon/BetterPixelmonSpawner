package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.DropItemHelper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.UUID;

public class DefeatListener {

    @SubscribeEvent
    public void onDefeat (BeatWildPixelmonEvent event) {

        PixelmonEntity pokemon = event.wpp.controlledPokemon.get(0).entity;
        for (String tag : pokemon.getTags()) {

            if (tag.contains("SpawnedPlayerUUID:")) {

                String[] split = tag.split(":");
                UUID uuid = UUID.fromString(split[1]);
                PokemonCounter.decrement(uuid);
                break;

            }

        }
        if (PixelmonSpecies.getLegendaries(false).contains(pokemon.getSpecies().getDex()) || PixelmonSpecies.getUltraBeasts().contains(pokemon.getSpecies().getDex()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        LegendaryListing.updatePokemonStatusKilled(pokemon, event.player);

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }

        if (!pokemon.getTags().stream().anyMatch(s -> s.contains("BossPokemon:Tier-"))) {

            List<ItemStack> drops = DropItemRegistry.getDropsForPokemon(pokemon);;
            for (ItemStack is : drops) {

                DropItemHelper.giveItemStack(event.player, is, false);

            }

        }

    }

}
