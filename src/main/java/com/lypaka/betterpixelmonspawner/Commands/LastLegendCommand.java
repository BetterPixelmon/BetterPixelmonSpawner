package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.LastLegendList;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class LastLegendCommand {

    public LastLegendCommand (CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(
                Commands.literal("betterpixelmonspawner")
                        .then(
                                Commands.literal("lastlegend")
                                        .executes(c -> {

                                            if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.lastlegend")) {

                                                    player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                    return 0;

                                                }

                                                LastLegendList.openList(player);

                                            }

                                            return 1;

                                        })
                        )
        );

    }

}
