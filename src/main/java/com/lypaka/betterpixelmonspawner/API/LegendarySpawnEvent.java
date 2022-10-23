package com.lypaka.betterpixelmonspawner.API;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.LegendarySpawnInfo;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a legendary is set to be spawned
 */
@Cancelable
public class LegendarySpawnEvent extends Event {

    private PixelmonEntity pokemon;
    private final ServerPlayerEntity player;
    private final LegendarySpawnInfo info;

    public LegendarySpawnEvent (PixelmonEntity pokemon, ServerPlayerEntity player, LegendarySpawnInfo info) {

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

    public LegendarySpawnInfo getInfo() {

        return this.info;

    }

}
