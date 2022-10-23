package com.lypaka.betterpixelmonspawner.API;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class HolidaySpawnEvent extends Event {

    private final String holiday;
    private PixelmonEntity pokemon;
    private final ServerPlayerEntity player;

    public HolidaySpawnEvent (String holiday, PixelmonEntity pokemon, ServerPlayerEntity player) {

        this.holiday = holiday;
        this.pokemon = pokemon;
        this.player = player;

    }

    public String getHoliday() {

        return this.holiday;

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

}
