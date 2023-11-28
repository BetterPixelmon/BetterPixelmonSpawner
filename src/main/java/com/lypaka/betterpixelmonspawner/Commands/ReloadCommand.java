package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.DeadZones.DeadZoneRegistry;
import com.lypaka.betterpixelmonspawner.Holidays.HolidayHandler;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Spawners.MiscSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.NPCSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.PokemonSpawner;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand {

    private static final List<String> OPTIONS = Arrays.asList("all", "pokemon", "holiday", "deadzones", "spawners", "helditems");
    private static final SuggestionProvider<CommandSource> OPTION_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(OPTIONS.stream(), builder);

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("reload")
                                            .then(
                                                    Commands.argument("module", StringArgumentType.word())
                                                            .suggests(OPTION_SUGGESTIONS)
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                        return 0;

                                                                    }

                                                                }
                                                                try {

                                                                    String module = StringArgumentType.getString(c, "module");
                                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&eStarting reloading of BPS, please wait..."), true);
                                                                    BetterPixelmonSpawner.configManager.load();
                                                                    ConfigGetters.load();

                                                                    switch (module) {

                                                                        case "pokemon":
                                                                            BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
                                                                            InfoRegistry.loadPokemonSpawnData();
                                                                            BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
                                                                            BossPokemonUtils.loadBossList();
                                                                            break;

                                                                        case "holiday":
                                                                            HolidayHandler.loadHolidays();
                                                                            break;

                                                                        case "deadzones":
                                                                            DeadZoneRegistry.loadAreas();
                                                                            break;

                                                                        case "spawners":
                                                                            BetterPixelmonSpawner.logger.info("Starting spawners...");
                                                                            PokemonSpawner.startTimer();
                                                                            LegendarySpawner.startTimer();
                                                                            NPCSpawner.startTimer();
                                                                            MiscSpawner.startTimer();
                                                                            ClearTask.startClearTask();
                                                                            break;

                                                                        case "helditems":
                                                                            HeldItemUtils.load();
                                                                            break;

                                                                        default:
                                                                            BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
                                                                            InfoRegistry.loadPokemonSpawnData();
                                                                            BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
                                                                            BossPokemonUtils.loadBossList();
                                                                            HolidayHandler.loadHolidays();
                                                                            DeadZoneRegistry.loadAreas();
                                                                            BetterPixelmonSpawner.logger.info("Starting spawners...");
                                                                            PokemonSpawner.startTimer();
                                                                            LegendarySpawner.startTimer();
                                                                            NPCSpawner.startTimer();
                                                                            MiscSpawner.startTimer();
                                                                            ClearTask.startClearTask();
                                                                            HeldItemUtils.load();
                                                                            break;

                                                                    }

                                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded BetterPixelmonSpawner."), true);

                                                                } catch (ObjectMappingException e) {

                                                                    e.printStackTrace();

                                                                }

                                                                return 1;

                                                            })
                                            )
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.admin")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                        return 0;

                                                    }

                                                }

                                                try {

                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&eStarting reloading of BPS, please wait..."), true);
                                                    BetterPixelmonSpawner.configManager.load();
                                                    ConfigGetters.load();
                                                    BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
                                                    InfoRegistry.loadPokemonSpawnData();
                                                    BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
                                                    BossPokemonUtils.loadBossList();
                                                    HolidayHandler.loadHolidays();
                                                    DeadZoneRegistry.loadAreas();
                                                    BetterPixelmonSpawner.logger.info("Starting spawners...");
                                                    PokemonSpawner.startTimer();
                                                    LegendarySpawner.startTimer();
                                                    NPCSpawner.startTimer();
                                                    MiscSpawner.startTimer();
                                                    ClearTask.startClearTask();
                                                    HeldItemUtils.load();

                                                } catch (ObjectMappingException e) {

                                                    e.printStackTrace();

                                                }

                                                c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded BetterPixelmonSpawner."), true);
                                                return 1;

                                            })
                            )
            );

        }

    }

}
