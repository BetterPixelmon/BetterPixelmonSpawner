package com.lypaka.betterpixelmonspawner.API;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a shiny Pokemon is set to be spawned
 */
@Cancelable
public class ShinySpawnEvent extends Event {

    private PixelmonEntity pokemon;
    private final ServerPlayerEntity player;
    private final PokemonSpawnInfo info;

    public ShinySpawnEvent (PixelmonEntity pokemon, ServerPlayerEntity player, PokemonSpawnInfo info) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;

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

}
