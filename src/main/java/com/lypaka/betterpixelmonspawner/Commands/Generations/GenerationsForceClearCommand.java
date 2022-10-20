package com.lypaka.betterpixelmonspawner.Commands.Generations;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.JoinListener;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.Utils.Counters.GenerationsPokemonCounter;
import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Map;
import java.util.UUID;

public class GenerationsForceClearCommand extends CommandBase {

    @Override
    public String getName() {

        return "clear";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/bps clear";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

        }

        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {

                for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

                    GenerationsPokemonCounter.checkForDespawnPokemon(entry.getKey());

                }

                String msg = ConfigGetters.pokeClearMessage.replace("%number%", String.valueOf(ClearTask.count));
                if (ClearTask.count == 1 && msg.contains("have")) {

                    msg = msg.replace("have", "has");

                }

                BetterPixelmonSpawner.server.getPlayerList().sendMessage(FancyText.getFormattedText(msg
                        .replace("%number%", String.valueOf(ClearTask.count))
                ));

                ClearTask.count = 0;

            });

        });

    }

}
