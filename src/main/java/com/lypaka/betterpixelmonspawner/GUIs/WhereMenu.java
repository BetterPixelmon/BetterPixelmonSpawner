package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereMenu {

    private final ServerPlayerEntity player;
    private String pokemonName;

    public WhereMenu (ServerPlayerEntity player, String pokemonName) {

        this.player = player;
        this.pokemonName = pokemonName;

    }

    public void open() {

        PixelmonEntity pokemon;
        this.pokemonName = this.pokemonName.replace(".conf", "");
        if (this.pokemonName.contains("-")) {

            if (this.pokemonName.equalsIgnoreCase("ho-oh")) {

                this.pokemonName = "Ho-Oh";
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

            } else if (pokemonName.equalsIgnoreCase("Hakamo-o")) {

                this.pokemonName = "Hakamo-o";
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

            } else if (pokemonName.equalsIgnoreCase("Jangmo-o")) {

                this.pokemonName = "Jangmo-o";
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

            } else if (pokemonName.equalsIgnoreCase("Kommo-o")) {

                this.pokemonName = "Kommo-o";
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

            } else if (pokemonName.equalsIgnoreCase("Porygon-Z")) {

                this.pokemonName = "Porygon-Z";
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

            } else {

                String[] split = this.pokemonName.split("-");
                this.pokemonName = split[0];
                String form = "";
                for (int f = 1; f < split.length; f++) {

                    form = form + "-" + split[f];

                }

                form = form.substring(1); // removes that first _ from the String
                pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();
                pokemon.setForm(form);

            }

        } else {

            pokemon = PokemonBuilder.builder().species(this.pokemonName).build().getOrCreatePixelmon();

        }
        ChestTemplate template = ChestTemplate.builder(3).build();
        GooeyPage page = GooeyPage.builder()
                .title(FancyText.getFormattedString("&eBiomes..."))
                .template(template)
                .build();

        for (int i = 0; i < 27; i++) {

            page.getTemplate().getSlot(i).setButton(getGlass());

        }


        List<String> biomes = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : BiomeList.biomesToPokemon.entrySet()) {

            String biome = entry.getKey();
            for (String names : entry.getValue()) {

                if (names.contains(this.pokemonName)) {

                    if (!biomes.contains(biome)) {

                        biomes.add(biome);

                    }

                }

            }

        }

        ListNBT lore = new ListNBT();
        for (String s : biomes) {

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&e" + s))));

        }

        ItemStack sprite = SpriteItemHelper.getPhoto(pokemon);
        sprite.setDisplayName(FancyText.getFormattedText("&a" + pokemon.getPokemonName()));
        sprite.getOrCreateChildTag("display").put("Lore", lore);
        page.getTemplate().getSlot(13).setButton(GooeyButton.builder().display(sprite).build());

        UIManager.openUIForcefully(this.player, page);

    }

    private Button getGlass() {

        ItemStack glass = ItemStackBuilder.buildFromStringID("minecraft:red_stained_glass_pane");
        glass.setDisplayName(FancyText.getFormattedText(""));
        return GooeyButton.builder().display(glass).build();

    }

}
