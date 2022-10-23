package com.lypaka.betterpixelmonspawner.API;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a regular Pokemon (not Boss, Totem, Alpha, shiny, etc) is set to be spawned
 */
@Cancelable
public class PokemonSpawnEvent extends Event {

    private PixelmonEntity pokemon;
    private final ServerPlayerEntity player;
    private final PokemonSpawnInfo info;
    private int groupSize;

    public PokemonSpawnEvent (PixelmonEntity pokemon, ServerPlayerEntity player, PokemonSpawnInfo info, int groupSize) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;
        this.groupSize = groupSize;

    }

    public PixelmonEntity getPokemon() {

        return this.pokemon;

    }

    public void setPokemon (PixelmonEntity pokemon) {

        this.pokemon = pokemon;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public PokemonSpawnInfo getInfo() {

        return this.info;

    }

    public int getGroupSize() {

        return this.groupSize;

    }

    public void setGroupSize (int size) {

        this.groupSize = size;

    }

}
