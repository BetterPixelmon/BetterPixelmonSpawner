package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Listeners.JoinListener;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.Utils.Counters.PokemonCounter;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.UUID;

public class ForceClearCommand {

    public ForceClearCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("clear")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                        return 0;

                                                    }

                                                }

                                                ServerLifecycleHooks.getCurrentServer().deferTask(() -> {

                                                    JoinListener.pokemonMap.entrySet().removeIf(entry -> {

                                                        PokemonCounter.checkForDespawnPokemon(entry.getKey());
                                                        if (!com.lypaka.lypakautils.Listeners.JoinListener.playerMap.containsKey(entry.getKey())) {

                                                            return true;

                                                        }

                                                        return false;

                                                    });

                                                    String msg = ConfigGetters.pokeClearMessage.replace("%number%", String.valueOf(ClearTask.count));
                                                    if (ClearTask.count == 1 && msg.contains("have")) {

                                                        msg = msg.replace("have", "has");

                                                    }

                                                    String finalMsg = msg;
                                                    for (Map.Entry<UUID, ServerPlayerEntity> entry : com.lypaka.lypakautils.Listeners.JoinListener.playerMap.entrySet()) {

                                                        entry.getValue().sendMessage(FancyText.getFormattedText(finalMsg.replace("%number%", String.valueOf(ClearTask.count))), entry.getValue().getUniqueID());

                                                    }
                                                    ClearTask.count = 0;

                                                });

                                                return 1;

                                            })
                            )
            );

        }

    }

}
