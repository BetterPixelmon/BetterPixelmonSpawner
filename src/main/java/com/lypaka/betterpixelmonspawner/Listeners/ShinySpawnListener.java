package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.API.ShinySpawnEvent;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShinySpawnListener {

    @SubscribeEvent
    public void onShinySpawn (ShinySpawnEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        PixelmonEntity pokemon = event.getPokemon();
        double ivPercent = pokemon.getPokemon().getIVs().getPercentage(0);
        if (ivPercent >= 75) {

            if (!ConfigGetters.highIVSoundID.equalsIgnoreCase("")) {

                // I hate this
                player.world.getServer().getCommandManager().handleCommand(player.world.getServer().getCommandSource(), "playsound " + ConfigGetters.highIVSoundID
                        + " master " + player.getName().getString()
                        + " " + player.getPosition().getX()
                        + " " + player.getPosition().getY()
                        + " " + player.getPosition().getZ()
                );

            }

        }

    }

}
