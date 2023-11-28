package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class WhenCommand {

    public WhenCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("when")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.when")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                        return 0;

                                                    }

                                                }

                                                if (LegendarySpawner.nextSpawnAttempt != null) {

                                                    LocalDateTime now = LocalDateTime.now();
                                                    LocalDateTime then = LegendarySpawner.nextSpawnAttempt;
                                                    long seconds = ChronoUnit.SECONDS.between(now, then);
                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&eThe next legendary spawn attempt will be in: " + seconds + " seconds"), true);

                                                } else {

                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&cThe legendary spawner has not yet run to determine when the next spawn attempt will be! Check again later!"), true);

                                                }

                                                return 1;

                                            })
                            )
            );

        }

    }

}
