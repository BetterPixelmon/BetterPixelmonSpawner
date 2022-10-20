package com.lypaka.betterpixelmonspawner.Listeners.Reforged;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class ReforgedBattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) {

        WildPixelmonParticipant wpp = null;
        PlayerParticipant pp = null;
        BattleControllerBase bcb = event.bc;
        if (bcb.participants.get(0) instanceof WildPixelmonParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(0);
            pp = (PlayerParticipant) bcb.participants.get(1);

        } else if (bcb.participants.get(1) instanceof WildPixelmonParticipant && bcb.participants.get(0) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bcb.participants.get(1);
            pp = (PlayerParticipant) bcb.participants.get(0);

        } else {

            return;

        }

        EntityPlayerMP player = pp.player;
        EntityPixelmon pokemon = wpp.controlledPokemon.get(0).entity;
        if (EnumSpecies.legendaries.contains(pokemon.getSpecies()) || EnumSpecies.ultrabeasts.contains(pokemon.getSpecies()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

            for (String tag : pokemon.getTags()) {

                if (tag.contains("LegendaryGracePeriod:")) {

                    String[] split = tag.split(":");
                    String uuid = split[1];
                    if (!uuid.equalsIgnoreCase(player.getUniqueID().toString())) {

                        event.setCanceled(true);
                        String name = JoinListener.playerMap.get(UUID.fromString(uuid)).getName();
                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&eGrace Period activated! Only " + name + " can battle this Pokemon!"));
                        break;

                    }

                }

            }

        }

    }

}
