package com.lypaka.betterpixelmonspawner.Commands;

import com.lypaka.betterpixelmonspawner.GUIs.MainSpawnMenu;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class ListCommand {

    private static final SuggestionProvider<CommandSource> BIOME_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.func_212476_a(ForgeRegistries.BIOMES.getKeys().stream(), builder);

    public ListCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterPixelmonSpawnerCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("list")
                                            .then(
                                                    Commands.argument("biome", StringArgumentType.word())
                                                            .suggests(BIOME_SUGGESTIONS)
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    String biome = StringArgumentType.getString(c, "biome");
                                                                    MainSpawnMenu.open(player, biome);

                                                                }

                                                                return 1;

                                                            })
                                            )
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    String biome = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                                                    MainSpawnMenu.open(player, biome);

                                                }

                                                return 1;

                                            })
                            )
            );

        }

    }

}
