package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;
import java.util.List;

public class LocationCommand {

    private static final List<String> WORDS = Arrays.asList("clear", "air", "water", "land");
    private static final SuggestionProvider<CommandSource> LOCATION_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.suggest(WORDS.stream(), builder);

    public LocationCommand (CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(
                Commands.literal("betterpixelmonspawner")
                        .then(
                                Commands.literal("location")
                                        .then(
                                                Commands.argument("location", StringArgumentType.word())
                                                        .suggests(LOCATION_SUGGESTIONS)
                                                        .executes(c -> {

                                                            if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                if (!PermissionHandler.hasPermission(player, "betterpixelmonspawner.command.location")) {

                                                                    player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                    return 0;

                                                                }

                                                                String loc = StringArgumentType.getString(c, "location");
                                                                if (!WORDS.contains(loc)) {

                                                                    player.sendMessage(FancyText.getFormattedText("&cInvalid argument!"), player.getUniqueID());
                                                                    return 0;

                                                                }

                                                                if (loc.equalsIgnoreCase("air") || loc.equalsIgnoreCase("water") || loc.equalsIgnoreCase("land") || loc.equalsIgnoreCase("underground")) {

                                                                    ConfigGetters.locationMap.put(player.getUniqueID().toString(), loc);
                                                                    player.sendMessage(FancyText.getFormattedText("&aSuccessfully set your spawner location to: &e" + loc + " &a."), player.getUniqueID());
                                                                    player.sendMessage(FancyText.getFormattedText("&eTo remove the filtering, type &b\"/bps loc clear\"&e."), player.getUniqueID());

                                                                } else if (loc.equalsIgnoreCase("clear")) {

                                                                    ConfigGetters.locationMap.entrySet().removeIf(entry -> entry.toString().equalsIgnoreCase(player.getUniqueID().toString()));
                                                                    player.sendMessage(FancyText.getFormattedText("&aSuccessfully removed your spawner location filter!"), player.getUniqueID());

                                                                }

                                                            }

                                                            return 1;

                                                        })
                                        )
                        )
        );

    }

}
