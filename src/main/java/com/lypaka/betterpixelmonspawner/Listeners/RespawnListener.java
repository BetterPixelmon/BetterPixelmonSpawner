package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.Utils.Counters.MiscCounter;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class RespawnListener {

    @SubscribeEvent
    public void onRespawn (PlayerEvent.PlayerRespawnEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        putTheBitchBackIfMissing(player); // this fixes spawns breaking

    }

    private static void putTheBitchBackIfMissing (ServerPlayerEntity player) {

        UUID uuid = player.getUniqueID();
        com.lypaka.lypakautils.JoinListener.playerMap.entrySet().removeIf(entry -> entry.getKey().toString().equalsIgnoreCase(uuid.toString()));
        com.lypaka.lypakautils.JoinListener.playerMap.put(uuid, player);
        if (!MiscCounter.miscCountMap.containsKey(uuid)) {

            MiscCounter.miscCountMap.put(uuid, 0);

        }
        if (!NPCCounter.npcCountMap.containsKey(uuid)) {

            NPCCounter.npcCountMap.put(uuid, 0);

        }
        if (!PokemonCounter.pokemonCountMap.containsKey(uuid)) {

            PokemonCounter.pokemonCountMap.put(uuid, 0);

        }

    }

}
