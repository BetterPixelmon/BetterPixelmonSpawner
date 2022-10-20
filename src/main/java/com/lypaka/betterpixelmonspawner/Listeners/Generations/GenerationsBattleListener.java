package com.lypaka.betterpixelmonspawner.Listeners.Generations;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.pixelmongenerations.api.events.BattleStartEvent;
import com.pixelmongenerations.common.battle.controller.BattleControllerBase;
import com.pixelmongenerations.common.battle.controller.participants.PlayerParticipant;
import com.pixelmongenerations.common.battle.controller.participants.WildPixelmonParticipant;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class GenerationsBattleListener {

    @SubscribeEvent
    public void onBattleStart (BattleStartEvent event) {

        WildPixelmonParticipant wpp = null;
        PlayerParticipant pp = null;
        BattleControllerBase bcb = event.getBattleController();
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
        EntityPixelmon pokemon = wpp.controlledPokemon.get(0).pokemon;
        if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName()) || ConfigGetters.specialLegendaries.contains(pokemon.getPokemonName())) {

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
