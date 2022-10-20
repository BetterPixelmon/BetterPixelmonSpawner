package com.lypaka.betterpixelmonspawner.Commands.Reforged;

import com.lypaka.betterpixelmonspawner.GUIs.Reforged.WhereMenu;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class ReforgedWhereCommand extends CommandBase {

    @Override
    public String getName() {

        return "where";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps where <pokemon>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.where")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

            if (args.length < 2) {

                player.sendMessage(FancyText.getFormattedText(getUsage(player)));
                return;

            }

            String pokemon = args[1];
            WhereMenu menu = new WhereMenu(player, pokemon);
            menu.open();

        }

    }

}
