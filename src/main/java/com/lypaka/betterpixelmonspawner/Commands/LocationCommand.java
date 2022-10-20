package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class LocationCommand extends CommandBase {

    @Override
    public String getName() {

        return "location";

    }

    @Override
    public List<String> getAliases() {
        
        List<String> a = new ArrayList<>();
        a.add("loc");
        return a;
        
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps loc <location>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.location")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

            if (args.length < 2) {

                player.sendMessage(FancyText.getFormattedText(getUsage(player)));
                return;

            }
            
            String location = args[1];
            if (location.equalsIgnoreCase("air") || location.equalsIgnoreCase("water") || location.equalsIgnoreCase("land") || location.equalsIgnoreCase("underground")) {

                ConfigGetters.locationMap.put(player.getUniqueID().toString(), location);
                player.sendMessage(FancyText.getFormattedText("&aSuccessfully set your spawner location to: &e" + location + " &a."));
                player.sendMessage(FancyText.getFormattedText("&eTo remove the filtering, type &b\"/bps loc clear\"&e."));
                
            } else if (location.equalsIgnoreCase("clear")) {
                
                ConfigGetters.locationMap.entrySet().removeIf(entry -> entry.toString().equalsIgnoreCase(player.getUniqueID().toString()));
                player.sendMessage(FancyText.getFormattedText("&aSuccessfully removed your spawner location filter!"));
                
            } else {
                
                player.sendMessage(FancyText.getFormattedText("&eInvalid arguments! Use &c\"clear\"&e, &c\"air\"&e, &c\"land\"&e, &c\"water\"&e or &c\"underground\"&e."));
                
            }

        }

    }

}
