package com.lypaka.betterpixelmonspawner.API.Spawning.Generations;

import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.PokemonSpawnInfo;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a shiny Pokemon is set to be spawned
 */
@Cancelable
public class GenerationsShinySpawnEvent extends Event {

    private EntityPixelmon pokemon;
    private final EntityPlayerMP player;
    private PokemonSpawnInfo info;

    public GenerationsShinySpawnEvent (EntityPixelmon pokemon, EntityPlayerMP player, PokemonSpawnInfo info) {

        this.pokemon = pokemon;
        this.player = player;
        this.info = info;

    }

    public EntityPixelmon getPokemon() {

        return this.pokemon;

    }

    public void setPokemon (EntityPixelmon pokemon) {

        this.pokemon = pokemon;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public PokemonSpawnInfo getInfo() {

        return this.info;

    }

}
