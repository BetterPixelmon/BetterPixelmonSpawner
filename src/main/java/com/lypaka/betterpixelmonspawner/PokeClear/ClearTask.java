package com.lypaka.betterpixelmonspawner.PokeClear;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ClearTask {

    private static Timer warningTimer = null;
    private static Timer clearTimer = null;
    public static int count = 0;

    public static void startClearTask() {

        if (warningTimer != null) {

            warningTimer.cancel();

        }

        if (clearTimer != null) {

            clearTimer.cancel();

        }

        // checks this here for if on reload, clear task is toggled off. Clears the tasks should they aren't running for no reason basically
        if (!ConfigGetters.pokeClearEnabled) return;

        warningTimer = new Timer();
        clearTimer = new Timer();
        long msgInterval = ConfigGetters.clearWarningInterval * 1000L;
        long clearInterval = ConfigGetters.pokeClearInterval * 1000L;
        long interval = clearInterval - msgInterval;
        long actualFuckingInterval = clearInterval + interval;
        warningTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (Map.Entry<UUID, ServerPlayerEntity> entry : com.lypaka.lypakautils.JoinListener.playerMap.entrySet()) {

                    entry.getValue().sendMessage(FancyText.getFormattedText(ConfigGetters.clearWarningMessage), entry.getValue().getUniqueID());

                }
                clearTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                            JoinListener.pokemonMap.entrySet().removeIf(entry -> {

                                PokemonCounter.checkForDespawnPokemon(entry.getKey());
                                if (!com.lypaka.lypakautils.JoinListener.playerMap.containsKey(entry.getKey())) {

                                    return true;

                                }

                                return false;

                            });

                            String msg = ConfigGetters.pokeClearMessage.replace("%number%", String.valueOf(count));
                            if (count == 1 && msg.contains("have")) {

                                msg = msg.replace("have", "has");

                            }

                            String finalMsg = msg;
                            for (Map.Entry<UUID, ServerPlayerEntity> entry : com.lypaka.lypakautils.JoinListener.playerMap.entrySet()) {

                                entry.getValue().sendMessage(FancyText.getFormattedText(finalMsg.replace("%number%", String.valueOf(count))), entry.getValue().getUniqueID());

                            }

                            count = 0;

                        });

                    }

                }, interval);

            }

        }, 0, actualFuckingInterval);

    }

    public static boolean isBlacklisted (PixelmonEntity pokemon) {

        boolean blacklisted = false;
        if (pokemon.hasOwner()) {

            return true;

        }
        if (pokemon.battleController != null) {

            return true;

        }
        for (String entry : ConfigGetters.blacklistedClearPokemon) {

            if (entry.equalsIgnoreCase("legendaries") && PixelmonSpecies.getLegendaries(false).contains(pokemon.getSpecies().getDex()) ||
                entry.equalsIgnoreCase("legendaries") && PixelmonSpecies.getUltraBeasts().contains(pokemon.getSpecies().getDex()) ||
                entry.equalsIgnoreCase("legendaries") && pokemon.getTags().contains("SpecialLegendarySpawn")) {

                blacklisted = true;

            } else if (entry.equalsIgnoreCase("bosses")) {

                if (pokemon.getPokemon().isMega() || pokemon.isBossPokemon()) {

                    blacklisted = true;

                } else {

                    for (String tag : pokemon.getTags()) {

                        if (tag.equalsIgnoreCase("PixelmonDefaultBoss") || tag.contains("BossPokemon:Tier-")) {

                            blacklisted = true;
                            break;

                        }

                    }

                }

            } else if (entry.equalsIgnoreCase("shinies") && pokemon.getPokemon().isShiny()) {

                blacklisted = true;


            }

        }

        return blacklisted;

    }

}
