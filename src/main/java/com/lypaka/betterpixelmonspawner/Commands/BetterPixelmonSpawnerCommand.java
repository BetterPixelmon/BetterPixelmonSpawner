package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

/**
 * FUCK Brigadier
 */
@Mod.EventBusSubscriber(modid = BetterPixelmonSpawner.MOD_ID)
public class BetterPixelmonSpawnerCommand {

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new DoLegendarySpawnCommand(event.getDispatcher());
        new ForceClearCommand(event.getDispatcher());
        new LastLegendCommand(event.getDispatcher());
        new ListCommand(event.getDispatcher());
        new LocationCommand(event.getDispatcher());
        new OptCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new WhenCommand(event.getDispatcher());
        new WhereCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
