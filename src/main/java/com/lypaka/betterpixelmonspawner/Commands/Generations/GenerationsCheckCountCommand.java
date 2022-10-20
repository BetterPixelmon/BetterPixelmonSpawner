package com.lypaka.betterpixelmonspawner.Commands.Generations;

import com.lypaka.betterpixelmonspawner.Utils.Counters.GenerationsPokemonCounter;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class GenerationsCheckCountCommand extends CommandBase {

    @Override
    public String getName() {

        return "checkcount";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("count");
        return a;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps checkcount <spawner>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.check")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

            String checkArg = args[0];
            if (args.length < 2) {

                player.sendMessage(FancyText.getFormattedText(getUsage(player)));
                return;

            }

            String spawnerArg = args[1];
            if (spawnerArg.equalsIgnoreCase("pokemon")) {

                int count = GenerationsPokemonCounter.getCount(player.getUniqueID());
                player.sendMessage(FancyText.getFormattedText("&eYou currently have " + count + " Pokemon spawned on you!"));

            }

        }

    }

}
