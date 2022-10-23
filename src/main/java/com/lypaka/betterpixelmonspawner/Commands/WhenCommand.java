package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.time.Duration;
import java.time.LocalDateTime;

public class WhenCommand {

    public WhenCommand (CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(
                Commands.literal("betterpixelmonspawner")
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
                                                c.getSource().sendFeedback(FancyText.getFormattedText("&eThe next legendary spawn attempt will be in: " + makeTimeReadable(LegendarySpawner.nextSpawnAttempt, now)), true);

                                            } else {

                                                c.getSource().sendFeedback(FancyText.getFormattedText("&cThe legendary spawner has not yet run to determine when the next spawn attempt will be! Check again later!"), true);

                                            }

                                            return 1;

                                        })
                        )
        );

    }

    private static String makeTimeReadable (LocalDateTime node, LocalDateTime now) {

        Duration duration = Duration.between(now, node);
        return printSeconds(duration.getSeconds());

    }

    private static String printSeconds (long seconds) {

        StringBuilder timeString = new StringBuilder();
        if (timeString.length() != 0 || seconds >= 86400) timeString.append(seconds / 86400).append(" days, ");
        if (timeString.length() != 0 || seconds >= 3600) timeString.append(seconds % 86400 / 3600).append(" hours, ");
        if (timeString.length() != 0 || seconds >= 60) timeString.append(seconds % 3600 / 60).append(" minutes, ");
        timeString.append(seconds % 60).append(" seconds");
        return timeString.toString();

    }

}
