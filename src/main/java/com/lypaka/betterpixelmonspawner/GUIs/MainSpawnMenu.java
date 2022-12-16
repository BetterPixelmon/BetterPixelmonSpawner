package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class MainSpawnMenu {

    public static void open (ServerPlayerEntity player, String biome) {

        ChestTemplate template = ChestTemplate.builder(3).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&dMain Menu"))
                .build();

        // 11 pokemon
        // 13 fish
        // 15 legendary
        for (int i = 0; i < 27; i++) {

            if (i == 11) {

                page.getTemplate().getSlot(i).setButton(getPokemonButton(player, biome));

            } else if (i == 13) {

                page.getTemplate().getSlot(i).setButton(getFishButton(player, biome));

            } else if (i == 15) {

                page.getTemplate().getSlot(i).setButton(getLegendaryButton(player, biome));

            } else {

                page.getTemplate().getSlot(i).setButton(getGlass());

            }

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getGlass() {

        ItemStack glass = ItemStackBuilder.buildFromStringID("minecraft:lime_stained_glass_pane");
        glass.setDisplayName(FancyText.getFormattedText(""));
        return GooeyButton.builder().display(glass).build();

    }

    private static Button getPokemonButton (ServerPlayerEntity player, String biome) {

        PokemonSpawnList list = new PokemonSpawnList(player, biome);
        list.build();
        Pokemon pixelmon = PokemonBuilder.builder().species("Bulbasaur").build();
        ItemStack bulbasaur = SpriteItemHelper.getPhoto(pixelmon);
        if (list.getSpawns().size() > 0) {

            bulbasaur.setDisplayName(FancyText.getFormattedText("&eClick me to view spawns for normal Pokemon!"));
            return GooeyButton.builder()
                    .display(bulbasaur)
                    .onClick(() -> {

                        list.open(1);

                    })
                    .build();

        } else {

            bulbasaur.setDisplayName(FancyText.getFormattedText("&eNo Pokemon spawn in this biome!"));
            return GooeyButton.builder()
                    .display(bulbasaur)
                    .build();

        }

    }

    private static Button getFishButton (ServerPlayerEntity player, String biome) {

        FishSpawnList list = new FishSpawnList(player, biome);
        list.build();
        Pokemon pixelmon = PokemonBuilder.builder().species("Magikarp").build();
        ItemStack magikarp = SpriteItemHelper.getPhoto(pixelmon);
        if (list.getSpawns().size() > 0) {

            magikarp.setDisplayName(FancyText.getFormattedText("&eClick me to view spawns for fishing!"));
            return GooeyButton.builder()
                    .display(magikarp)
                    .onClick(() -> {

                        list.open(1);

                    })
                    .build();

        } else {

            magikarp.setDisplayName(FancyText.getFormattedText("&eNo Pokemon spawn in this biome!"));
            return GooeyButton.builder()
                    .display(magikarp)
                    .build();

        }


    }

    private static Button getLegendaryButton (ServerPlayerEntity player, String biome) {

        LegendarySpawnList list = new LegendarySpawnList(player, biome);
        list.build();
        Pokemon pixelmon = PokemonBuilder.builder().species("Mew").build();
        ItemStack mew = SpriteItemHelper.getPhoto(pixelmon);
        if (list.getSpawns().size() > 0) {

            mew.setDisplayName(FancyText.getFormattedText("&eClick me to view spawns for legendary Pokemon!"));
            return GooeyButton.builder()
                    .display(mew)
                    .onClick(() -> {

                        list.open(1);

                    })
                    .build();

        } else {

            mew.setDisplayName(FancyText.getFormattedText("&eNo legendary Pokemon spawn in this biome!"));
            return GooeyButton.builder().display(mew).build();

        }

    }

}
