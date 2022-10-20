package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.DeadZones.DeadZoneRegistry;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Config.PokemonConfig;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.Listeners.Generations.*;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.Generations.*;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedLegendarySpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedMiscSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedNPCSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.Reforged.ReforgedPokemonSpawner;
import com.lypaka.betterpixelmonspawner.Utils.Generations.GenerationsHeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.betterpixelmonspawner.Utils.Reforged.ReforgedHeldItemUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.lypakautils.PixelmonHandlers.PixelmonVersionDetector;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ReloadCommand extends CommandBase {

    @Override
    public String getName() {

        return "reload";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pspawner reload [module]";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                player.sendMessage(FancyText.getFormattedText("&cYou do not have permission to use this command!"));
                return;

            }

        }

        String module = "all";
        if (args.length == 2) {

            module = args[1];

        }

        try {

            sender.sendMessage(FancyText.getFormattedText("&eStarting reloading of BPS spawner, please wait..."));
            BetterPixelmonSpawner.configManager.load();
            ConfigGetters.load();
            if (module.equalsIgnoreCase("pokemon")) {

                PokemonConfig.load(PixelmonVersionDetector.VERSION);
                BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
                InfoRegistry.loadPokemonSpawnData();
                BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    if (Loader.isModLoaded("betterbosses")) {

                        if (!com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                            BossPokemonUtils.loadGenerationsBossList();

                        }

                    } else {

                        BossPokemonUtils.loadGenerationsBossList();

                    }

                } else {

                    BossPokemonUtils.loadReforgedBossList();

                }

            } else if (module.equalsIgnoreCase("holiday") || module.equalsIgnoreCase("holidays")) {

                // Loads holidays from config, not worth having config getters for it since it only loads on startup unless reload command is ran
                HolidayHandler.loadHolidays();

            } else if (module.equalsIgnoreCase("deadzones") || module.equalsIgnoreCase("deadzone")) {

                // Loads the dead zones
                DeadZoneRegistry.loadAreas();

            } else if (module.equalsIgnoreCase("spawners") || module.equalsIgnoreCase("tasks")) {

                BetterPixelmonSpawner.logger.info("Starting spawners...");
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    GenerationsPokemonSpawner.startTimer();
                    GenerationsLegendarySpawner.startTimer();
                    GenerationsNPCSpawner.startTimer();
                    GenerationsMiscSpawner.startTimer();

                } else {

                    ReforgedPokemonSpawner.startTimer();
                    ReforgedLegendarySpawner.startTimer();
                    ReforgedNPCSpawner.startTimer();
                    ReforgedMiscSpawner.startTimer();

                }
                ClearTask.startClearTask();

            } else if (module.equalsIgnoreCase("helditems")) {

                // Loads in the held item registry for all Pokemon that have held item data
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    GenerationsHeldItemUtils.load();

                } else {

                    ReforgedHeldItemUtils.load();

                }

            } else if (module.equalsIgnoreCase("all")) {

                PokemonConfig.load(PixelmonVersionDetector.VERSION);
                BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
                InfoRegistry.loadPokemonSpawnData();
                BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    if (Loader.isModLoaded("betterbosses")) {

                        if (!com.lypaka.betterbosses.Config.ConfigGetters.disableDefaultBosses) {

                            BossPokemonUtils.loadGenerationsBossList();

                        }

                    } else {

                        BossPokemonUtils.loadGenerationsBossList();

                    }

                } else {

                    BossPokemonUtils.loadReforgedBossList();

                }

                // Loads holidays from config, not worth having config getters for it since it only loads on startup unless reload command is ran
                HolidayHandler.loadHolidays();

                // Loads in the held item registry for all Pokemon that have held item data
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    GenerationsHeldItemUtils.load();

                } else {

                    ReforgedHeldItemUtils.load();

                }

                // Loads the dead zones
                DeadZoneRegistry.loadAreas();

                BetterPixelmonSpawner.logger.info("Starting spawners...");
                if (PixelmonVersionDetector.VERSION.equalsIgnoreCase("Generations")) {

                    GenerationsPokemonSpawner.startTimer();
                    GenerationsLegendarySpawner.startTimer();
                    GenerationsNPCSpawner.startTimer();
                    GenerationsMiscSpawner.startTimer();

                } else {

                    ReforgedPokemonSpawner.startTimer();
                    ReforgedLegendarySpawner.startTimer();
                    ReforgedNPCSpawner.startTimer();
                    ReforgedMiscSpawner.startTimer();

                }
                ClearTask.startClearTask();

            }

        } catch (ObjectMappingException e) {

            e.printStackTrace();

        }

        sender.sendMessage(FancyText.getFormattedText("&aSuccessfully reloaded BetterPixelmonSpawner."));

    }

}
