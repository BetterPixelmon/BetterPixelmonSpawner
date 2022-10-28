package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Utils.SpawnerUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;

public class OptCommand {

    private static final List<String> OPTIONS = Arrays.asList("in", "out");
    private static final SuggestionProvider<CommandSource> OPTION_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(OPTIONS.stream(), builder);

    private static final List<String> MODULES = Arrays.asList("all", "pokemon", "npc", "misc", "legendary");
    private static final SuggestionProvider<CommandSource> MODULE_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(MODULES.stream(), builder);

    public OptCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("opt")
                                            .then(
                                                    Commands.argument("option", StringArgumentType.word())
                                                            .suggests(OPTION_SUGGESTIONS)
                                                            .then(
                                                                    Commands.argument("module", StringArgumentType.word())
                                                                            .suggests(MODULE_SUGGESTIONS)
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    String option = StringArgumentType.getString(c, "option");
                                                                                    String module = StringArgumentType.getString(c, "module");

                                                                                    if (option.equalsIgnoreCase("in")) {

                                                                                        SpawnerUtils.remove(player, module);

                                                                                    } else {

                                                                                        SpawnerUtils.add(player, module);

                                                                                    }

                                                                                }

                                                                                return 1;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
