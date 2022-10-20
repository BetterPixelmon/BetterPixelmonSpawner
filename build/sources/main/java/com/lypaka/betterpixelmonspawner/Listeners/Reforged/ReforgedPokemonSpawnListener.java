package com.lypaka.betterpixelmonspawner.Listeners.Reforged;

import com.lypaka.betterpixelmonspawner.API.Spawning.Reforged.ReforgedPokemonSpawnEvent;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedPokemonSpawnListener {

    @SubscribeEvent
    public void onPokemonSpawn (ReforgedPokemonSpawnEvent event) {

        EntityPlayerMP player = event.getPlayer();
        EntityPixelmon pokemon = event.getPokemon();
        double ivPercent = pokemon.getPokemonData().getIVs().getPercentage(0);
        if (ivPercent >= 75) {

            if (!ConfigGetters.highIVSoundID.equalsIgnoreCase("")) {

                // I hate this
                FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(), "playsound " + ConfigGetters.highIVSoundID
                        + " master " + player.getName()
                        + " " + player.getPosition().getX()
                        + " " + player.getPosition().getY()
                        + " " + player.getPosition().getZ()
                );

            }

        }

    }

}
