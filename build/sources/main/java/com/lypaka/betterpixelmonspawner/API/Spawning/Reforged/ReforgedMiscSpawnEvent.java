package com.lypaka.betterpixelmonspawner.API.Spawning.Reforged;

import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when spawning of other misc entities in Pixelmon (Zygarde Cells, Dynamax things, whatever)
 */
@Cancelable
public class ReforgedMiscSpawnEvent extends Event {

    private EntityLivingBase entity;
    private EntityWormhole wormhole;
    private final EntityPlayerMP player;
    private final String selectedEntityID;

    public ReforgedMiscSpawnEvent (EntityLivingBase entity, EntityPlayerMP player, String selectedEntityID) {

        this.entity = entity;
        this.player = player;
        this.selectedEntityID = selectedEntityID;

    }

    public ReforgedMiscSpawnEvent (EntityWormhole wormhole, EntityPlayerMP player, String selectedEntityID) {

        this.wormhole = wormhole;
        this.player = player;
        this.selectedEntityID = selectedEntityID;

    }

    public EntityLivingBase getEntity() {

        return this.entity;

    }

    public EntityWormhole getWormhole() {

        return this.wormhole;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public String getSelectedEntityID() {

        return this.selectedEntityID;

    }

}
