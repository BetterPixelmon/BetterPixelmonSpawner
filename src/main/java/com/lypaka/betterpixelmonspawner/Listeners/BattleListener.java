package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class BattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) {

        WildPixelmonParticipant wpp = null;
        PlayerParticipant pp = null;
        BattleController bcb = event.bc;
        if (bcb.participants.get(0) instanceof WildPixelmonParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(0);
            pp = (PlayerParticipant) bcb.participants.get(1);

        } else if (bcb.participants.get(1) instanceof WildPixelmonParticipant && bcb.participants.get(0) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(1);
            pp = (PlayerParticipant) bcb.participants.get(0);

        } else {

            return;

        }

        ServerPlayerEntity player = pp.player;
        PixelmonEntity pokemon = wpp.controlledPokemon.get(0).entity;
        if (PixelmonSpecies.getLegendaries(false).contains(pokemon.getSpecies().getDex()) || PixelmonSpecies.getUltraBeasts().contains(pokemon.getSpecies().getDex()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            for (String tag : pokemon.getTags()) {

                if (tag.contains("LegendaryGracePeriod:")) {

                    String[] split = tag.split(":");
                    String uuid = split[1];
                    if (!uuid.equalsIgnoreCase(player.getUniqueID().toString())) {

                        event.setCanceled(true);
                        String name = JoinListener.playerMap.get(UUID.fromString(uuid)).getName().getString();
                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&eGrace Period activated! Only " + name + " can battle this Pokemon!"), player.getUniqueID());
                        break;

                    }

                }

            }

        }

    }

}
