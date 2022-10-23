package com.lypaka.betterpixelmonspawner.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.BiomeList;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;

import java.text.DecimalFormat;
import java.util.*;

public class PokemonSpawnList {

    private final ServerPlayerEntity player;
    private final String biome;
    private final Map<Integer, List<PokemonSpawnInfo>> spawns;
    private int min;
    private int max;
    private final List<Integer> pages;

    public PokemonSpawnList (ServerPlayerEntity player, String biome) {

        this.player = player;
        this.biome = biome;
        this.spawns = new HashMap<>();
        this.min = 0;
        this.max = 53;
        this.pages = new ArrayList<>();

    }

    public void build() {

        if (BiomeList.biomePokemonMap.containsKey(this.biome)) {

            List<WorldTime> currentTimes = WorldTime.getCurrent(this.player.world);
            String weather;
            if (this.player.world.isRaining()) {

                weather = "rain";

            } else if (this.player.world.isThundering()) {

                weather = "storm";

            } else {

                weather = "clear";

            }
            String location;
            if (this.player.getRidingEntity() != null) {

                Entity mount = this.player.getRidingEntity();
                if (mount.isInWater()) {

                    location = "water";

                } else if (mount.isOnGround()) {

                    if (mount.getPosition().getY() <= 63) {

                        location = "underground";

                    } else {

                        location = "land";

                    }

                } else {

                    location = "air";

                }

            } else {

                if (this.player.isInWater()) {

                    location = "water";

                } else if (this.player.isOnGround()) {

                    if (this.player.getPosition().getY() <= 63) {

                        location = "underground";

                    } else {

                        location = "land";

                    }

                } else {

                    location = "air";

                }

            }
            List<PokemonSpawnInfo> pokemonThatSpawn = BiomeList.biomePokemonMap.get(this.biome);
            List<String> pokemonNames = new ArrayList<>();
            for (PokemonSpawnInfo psi : pokemonThatSpawn) {

                if (!pokemonNames.contains(psi.getName())) {

                    pokemonNames.add(psi.getName());

                }

            }

            List<PokemonSpawnInfo> base = new ArrayList<>(pokemonNames.size());
            List<String> usedNames = new ArrayList<>();
            for (PokemonSpawnInfo pokemonSpawnInfo : pokemonThatSpawn) {

                if (currentTimes.contains(WorldTime.valueOf(pokemonSpawnInfo.getTime().toUpperCase()))) {

                    if (pokemonSpawnInfo.getWeather().equalsIgnoreCase(weather)) {

                        if (pokemonSpawnInfo.getSpawnLocation().contains(location)) {

                            if (!usedNames.contains(pokemonSpawnInfo.getName())) {

                                usedNames.add(pokemonSpawnInfo.getName());
                                base.add(pokemonSpawnInfo);

                            }

                        }

                    }

                }

            }

            List<PokemonSpawnInfo> pokemonToDisplay = arrangePokemon(base);
            int spawnAmount = pokemonNames.size(); // we use this list because of the different PokemonSpawnInfo objects for each Pokemon
            int pages = 1;
            if (spawnAmount > 54) {

                int dividedInt = spawnAmount / 54;
                double dividedDouble = (double) spawnAmount / 54;
                double dummyDouble = dividedInt + 0.0;
                if (dividedDouble > dummyDouble) {

                    pages = dividedInt + 1;

                }

            }

            for (int i = 1; i <= pages; i++) {

                this.pages.add(i);

            }

            for (int i = 1; i <= pages; i++) {

                setInts(i);
                List<PokemonSpawnInfo> pokemonToPutInMap = new ArrayList<>(pokemonToDisplay.size());
                for (int j = this.min; j < this.max; j++) {

                    try {

                        PokemonSpawnInfo spawnInfo = pokemonToDisplay.get(j);
                        pokemonToPutInMap.add(spawnInfo);

                    } catch (IndexOutOfBoundsException er) {

                        break;

                    }

                }

                List<PokemonSpawnInfo> arrangedList = arrangePokemon(pokemonToPutInMap);
                this.spawns.put(i, arrangedList);

            }

        }

    }

    public void open (int pageNum) {

        ChestTemplate template = ChestTemplate.builder(6).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString("&dSpawns: P" + pageNum))
                .build();

        List<PokemonSpawnInfo> pokemon = this.spawns.get(pageNum);
        int startingIndex = 0;
        if (pageNum > 1) {

            startingIndex = 1;
            int pageToGoTo = pageNum - 1;
            page.getTemplate().getSlot(0).setButton(getPrevButton(pageToGoTo));

        }
        for (PokemonSpawnInfo psi : pokemon) {

            page.getTemplate().getSlot(startingIndex).setButton(getPokemonSprite(psi.getName(), psi));
            startingIndex++;

        }
        int nextPage = pageNum + 1;
        if (this.spawns.containsKey(nextPage)) {

            page.getTemplate().getSlot(53).setButton(getNextButton(nextPage));

        }

        UIManager.openUIForcefully(this.player, page);

    }

    private List<PokemonSpawnInfo> arrangePokemon (List<PokemonSpawnInfo> pokemonList) {

        List<PokemonSpawnInfo> listToReturn = new ArrayList<>(pokemonList.size());
        List<Integer> pokedexNumbers = new ArrayList<>(pokemonList.size());
        Map<PokemonSpawnInfo, Integer> map = new HashMap<>();
        for (int i = 0; i < pokemonList.size(); i++) {

            PokemonSpawnInfo info = pokemonList.get(i);
            String name = info.getName().replace(".conf", "");
            PixelmonEntity pokemon;
            if (name.contains("-")) {

                if (name.equalsIgnoreCase("porygon-z")) {

                    name = "porygon-z";
                    pokemon = PokemonBuilder.builder()
                            .species(name)
                            .build()
                            .getOrCreatePixelmon();

                } else {

                    String[] split = name.split("-");
                    name = split[0];
                    String form = "";
                    for (int f = 1; f < split.length; f++) {

                        form = form + "_" + split[f];

                    }

                    form = form.substring(1); // removes that first _ from the String
                    pokemon = PokemonBuilder.builder()
                            .species(name)
                            .build()
                            .getOrCreatePixelmon();
                    pokemon.setForm(form);

                }

            } else {

                pokemon = PokemonBuilder.builder()
                        .species(name)
                        .build()
                        .getOrCreatePixelmon();

            }

            int dex = pokemon.getSpecies().getDex();
            pokedexNumbers.add(i, dex);
            map.put(info, dex);

        }

        Collections.sort(pokedexNumbers);
        for (int i = 0; i < pokemonList.size(); i++) {

            int dex = pokedexNumbers.get(i);
            int finalI = i;
            map.entrySet().removeIf(entry -> {

                if (entry.getValue() == dex) {

                    listToReturn.add(finalI, entry.getKey());
                    return true;

                }

                return false;

            });

        }

        return listToReturn;

    }

    private void setInts (int page) {

        if (page > 1) {

            this.min = this.max + 1;
            if (this.pages.contains(page + 1)) {

                this.max = this.min + 52;

            } else {

                this.max = this.min + 53;

            }

        }

    }

    private Button getNextButton (int pageToGoTo) {

        ItemStack icon = ItemStackBuilder.buildFromStringID("pixelmon:trade_holder_right");
        icon.setDisplayName(FancyText.getFormattedText("&eNext Page"));
        return GooeyButton.builder()
                .display(icon)
                .onClick(() -> open(pageToGoTo))
                .build();

    }

    private Button getPrevButton (int pageToGoTo) {

        ItemStack icon = ItemStackBuilder.buildFromStringID("pixelmon:trade_holder_left");
        icon.setDisplayName(FancyText.getFormattedText("&ePrevious Page"));
        return GooeyButton.builder()
                .display(icon)
                .onClick(() -> open(pageToGoTo))
                .build();

    }

    private Button getPokemonSprite (String name, PokemonSpawnInfo info) {

        name = name.replace(".conf", "");
        String fileName = name;
        PixelmonEntity pokemon;
        if (name.contains("-")) {

            if (name.equalsIgnoreCase("porygon-z")) {

                name = "porygon-z";
                pokemon = PokemonBuilder.builder()
                        .species(name)
                        .build()
                        .getOrCreatePixelmon();

            } else if (name.equalsIgnoreCase("ho-oh")) {

                name = "ho-oh";
                pokemon = PokemonBuilder.builder()
                        .species(name)
                        .build()
                        .getOrCreatePixelmon();

            } else {

                String[] split = name.split("-");
                name = split[0];
                String form = "";
                for (int f = 1; f < split.length; f++) {

                    form = form + "-" + split[f];

                }

                form = form.substring(1); // removes that first _ from the String
                pokemon = PokemonBuilder.builder()
                        .species(name)
                        .build()
                        .getOrCreatePixelmon();
                pokemon.setForm(form);

            }

        } else {

            pokemon = PokemonBuilder.builder()
                    .species(name)
                    .build()
                    .getOrCreatePixelmon();

        }

        ItemStack sprite = SpriteItemHelper.getPhoto(pokemon);
        double spawnChance = info.getSpawnChance();
        String percent;
        if (spawnChance >= 1.0) {

            percent = "100%";

        } else {

            DecimalFormat df = new DecimalFormat("#.##");
            percent = df.format(spawnChance * 100) + "%";

        }
        String spawnLocation = info.getSpawnLocation();
        sprite.setDisplayName(FancyText.getFormattedText("&e" + pokemon.getPokemonName()));
        ListNBT lore = new ListNBT();
        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&eSpawn Chance:&a " + percent))));
        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&eSpawn Location:&a " + spawnLocation))));
        if (HeldItemUtils.heldItemMap.containsKey(name)) {

            Map<String, List<String>> possibleItems = null;
            for (Map.Entry<String, Map<String, List<String>>> entry : HeldItemUtils.heldItemMap.entrySet()) {

                if (entry.getKey().equalsIgnoreCase(fileName)) {

                    possibleItems = entry.getValue();
                    break;

                }

            }
            if (possibleItems != null) {

                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&eHeld Items:"))));
                for (Map.Entry<String, List<String>> entry : possibleItems.entrySet()) {

                    lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&a" + entry.getKey() + ":"))));
                    for (String s : entry.getValue()) {

                        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&e" + s))));

                    }

                }

            }

        }
        sprite.getOrCreateChildTag("display").put("Lore", lore);
        return GooeyButton.builder().display(sprite).build();

    }

}
