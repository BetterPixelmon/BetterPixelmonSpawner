package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinListener {

    public static Map<UUID, ServerPlayerEntity> pokemonMap = new HashMap<>();

    @SubscribeEvent
    public void onJoin (PlayerEvent.PlayerLoggedInEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (!pokemonMap.containsKey(player.getUniqueID())) {

            pokemonMap.put(player.getUniqueID(), player);

        }
        if (ConfigGetters.pokemonOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFormattedText("&eJust a reminder, you're currently opted out of Pokemon spawns!"), player.getUniqueID());

        }
        if (ConfigGetters.miscOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFormattedText("&eJust a reminder, you're currently opted out of misc entity spawns!"), player.getUniqueID());

        }
        if (ConfigGetters.npcOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFormattedText("&eJust a reminder, you're currently opted out of NPC spawns!"), player.getUniqueID());

        }
        if (ConfigGetters.legendaryOptOut.contains(player.getUniqueID().toString())) {

            player.sendMessage(FancyText.getFormattedText("&eJust a reminder, you're currently opted out of legendary spawns!"), player.getUniqueID());

        }

    }

}
