package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.LegendaryListing;
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

import java.time.format.DateTimeParseException;
import java.util.Map;

public class LastLegendList {

    public static void openList (ServerPlayerEntity player) {

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.listGUIRows).build();
        GooeyPage page = GooeyPage.builder()
                .title(FancyText.getFormattedString(ConfigGetters.listGUITitle))
                .template(template)
                .build();

        Map<String, Map<String, String>> map = ConfigGetters.listGUISlotMap;
        int slots = map.size();
        for (int i = 0; i < slots; i++) {

            Map<String, String> miniMap = map.get("Slot-" + i);
            page.getTemplate().getSlot(i).setButton(getButton(miniMap));

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getButton (Map<String, String> data) {

        Button button;
        ItemStack stack = null;
        String id = data.get("ID");
        if (id.contains("/")) {

            String[] split = id.split("/");
            id = split[0];
            String other = split[1];
            if (id.equalsIgnoreCase("pixelmon:pixelmon_sprite")) {

                String form = "";
                if (other.contains("Galarian")) {

                    form = "galarian";
                    other = other.replace("Galarian ", "");

                }
                if (other.contains("%pokemon")) {

                    String[] split2 = other.split("%pokemon ");
                    int listing = Integer.parseInt(split2[1].replace("%", ""));
                    other = other.replace("%pokemon " + listing + "%", LegendaryListing.getPokemonName(listing));

                }
                PixelmonEntity pokemon = PokemonBuilder.builder().species(other).build().getOrCreatePixelmon();
                if (!(form.equalsIgnoreCase(""))) {

                    pokemon.setForm(form);

                }

                stack = SpriteItemHelper.getPhoto(pokemon);

            }

        } else {

            stack = ItemStackBuilder.buildFromStringID(id);

        }

        if (stack == null) {

            stack = ItemStackBuilder.buildFromStringID("minecraft:air");

        }

        if (data.containsKey("Meta")) {

            int meta = Integer.parseInt(data.get("Meta"));
            stack.setDamage(meta);

        }

        String name = "";
        if (data.containsKey("Name")) {

            String stackName = data.get("Name");
            if (stackName.contains("%pokemon")) {

                String[] split = stackName.split("%pokemon ");
                int listing = Integer.parseInt(split[1].replace("%", ""));
                name = stackName.replace("%pokemon " + listing + "%", LegendaryListing.getPokemonName(listing));

            }

        }

        stack.setDisplayName(FancyText.getFormattedText(name));

        ListNBT lore = new ListNBT();
        if (data.containsKey("Status")) {

            String status = data.get("Status");
            if (status.contains("%status")) {

                String[] split = status.split("%status ");
                int listing = Integer.parseInt(split[1].replace("%", ""));
                status = status.replace("%status " + listing + "%", LegendaryListing.getStatus(listing));

            } else if (status.equalsIgnoreCase("captured by %player%")) {

                status = "Default setting";

            }

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(status))));

        }
        if (data.containsKey("Time")) {

            String time = data.get("Time");
            if (time.contains("%time")) {

                String[] split = time.split("%time ");
                int listing = Integer.parseInt(split[1].replace("%", ""));
                try {

                    time = time.replace("%time " + listing + "%", LegendaryListing.getTime(listing));

                } catch (DateTimeParseException er) {

                    time = "Default setting";

                }


            } else if (time.equalsIgnoreCase("time")) {

                time = "Default setting";

            }

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(time))));

        }

        stack.getOrCreateChildTag("display").put("Lore", lore);
        button = GooeyButton.builder()
                .display(stack)
                .build();
        return button;

    }

}
