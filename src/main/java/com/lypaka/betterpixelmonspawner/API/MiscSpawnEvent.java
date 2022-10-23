package com.lypaka.betterpixelmonspawner.API;

import com.pixelmonmod.pixelmon.entities.WormholeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when spawning of other misc entities in Pixelmon (Zygarde Cells, Dynamax things, whatever)
 */
@Cancelable
public class MiscSpawnEvent extends Event {

    private LivingEntity entity;
    private WormholeEntity wormhole;
    private final ServerPlayerEntity player;
    private final String selectedEntityID;

    public MiscSpawnEvent (LivingEntity entity, ServerPlayerEntity player, String selectedEntityID) {

        this.entity = entity;
        this.player = player;
        this.selectedEntityID = selectedEntityID;

    }

    public MiscSpawnEvent (WormholeEntity wormhole, ServerPlayerEntity player, String selectedEntityID) {

        this.wormhole = wormhole;
        this.player = player;
        this.selectedEntityID = selectedEntityID;

    }

    public LivingEntity getEntity() {

        return this.entity;

    }

    public WormholeEntity getWormhole() {

        return this.wormhole;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public String getSelectedEntityID() {

        return this.selectedEntityID;

    }

}
