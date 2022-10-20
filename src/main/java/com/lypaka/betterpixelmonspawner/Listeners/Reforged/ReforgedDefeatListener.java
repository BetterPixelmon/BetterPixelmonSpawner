package com.lypaka.betterpixelmonspawner.Listeners.Reforged;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.ReforgedPokemonCounter;
import com.lypaka.betterpixelmonspawner.Utils.Reforged.ReforgedLegendaryListing;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemHelper;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.UUID;

public class ReforgedDefeatListener {

    @SubscribeEvent
    public void onDefeat (BeatWildPixelmonEvent event) {

        EntityPixelmon pokemon = event.wpp.controlledPokemon.get(0).entity;
        for (String tag : pokemon.getTags()) {

            if (tag.contains("SpawnedPlayerUUID:")) {

                String[] split = tag.split(":");
                UUID uuid = UUID.fromString(split[1]);
                ReforgedPokemonCounter.decrement(uuid);
                break;

            }

        }
        if (EnumSpecies.legendaries.contains(pokemon.getSpecies()) || EnumSpecies.ultrabeasts.contains(pokemon.getSpecies()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            pokemon.getTags().forEach(tag -> {

                if (tag.equalsIgnoreCase("SpawnedLegendary")) {

                    try {

                        ReforgedLegendaryListing.updatePokemonStatusKilled(pokemon, event.player);

                    } catch (ObjectMappingException e) {

                        e.printStackTrace();

                    }

                }

            });

        }

        if (!pokemon.getTags().stream().anyMatch(s -> s.contains("BossPokemon:Tier-"))) {

            List<ItemStack> drops = DropItemRegistry.getBossDrops(pokemon, event.player);
            for (ItemStack is : drops) {

                DropItemHelper.giveItemStack(event.player, is, false);

            }

        }

    }

}
