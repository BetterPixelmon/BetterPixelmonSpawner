package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Timer;
import java.util.TimerTask;

public class LegendaryUtils {

    public static void handleGracePeriod (PixelmonEntity pokemon, ServerPlayerEntity player) {

        if (ConfigGetters.legendaryGracePeriod == 0) return;

        pokemon.addTag("LegendaryGracePeriod:" + player.getUniqueID());
        long delay = ConfigGetters.legendaryGracePeriod * 1000L;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                pokemon.getTags().removeIf(tag -> tag.equalsIgnoreCase("LegendaryGracePeriod:" + player.getUniqueID()));

            }

        }, delay);

    }

    public static void handleGlowing (PixelmonEntity pokemon) {

        pokemon.setGlowing(true);

    }

}
