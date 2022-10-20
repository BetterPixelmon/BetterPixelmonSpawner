package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Timer;
import java.util.TimerTask;

public class LegendaryUtils {

    public static void handleGenerationsGracePeriod (EntityPixelmon pokemon, EntityPlayerMP player) {

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

    public static void handleGenerationsGlowing (EntityPixelmon pokemon) {

        pokemon.setGlowing(ConfigGetters.legendarySpawnsGlowing);

    }

    public static void handleReforgedGracePeriod (com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon pokemon, EntityPlayerMP player) {

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

    public static void handleReforgedGlowing (com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon pokemon) {

        pokemon.setGlowing(true);

    }

}
