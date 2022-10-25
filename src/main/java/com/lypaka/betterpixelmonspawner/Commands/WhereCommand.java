package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.GUIs.WhereMenu;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

public class WhereCommand {

    private static final SuggestionProvider<CommandSource> POKEMON_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(ConfigGetters.pokemonFiles.stream(), builder);

    public WhereCommand (CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(
                Commands.literal("betterpixelmonspawner")
                        .then(
                                Commands.literal("where")
                                        .then(
                                                Commands.argument("pokemon", StringArgumentType.word())
                                                        .suggests(POKEMON_SUGGESTIONS)
                                                        .executes(c -> {

                                                            if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.where")) {

                                                                    player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                    return 0;

                                                                }

                                                                String pokemon = StringArgumentType.getString(c, "pokemon");
                                                                WhereMenu menu = new WhereMenu(player, pokemon);
                                                                menu.open();

                                                            }

                                                            return 1;

                                                        })
                                        )
                        )
        );

    }

}
