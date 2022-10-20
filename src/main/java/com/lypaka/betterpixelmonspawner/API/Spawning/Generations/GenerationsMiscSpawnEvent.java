package com.lypaka.betterpixelmonspawner.API.Spawning.Generations;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when spawning of other misc entities in Pixelmon (Zygarde Cells, Dynamax things, whatever)
 */
@Cancelable
public class GenerationsMiscSpawnEvent extends Event {

    private EntityLivingBase entity;
    private final EntityPlayerMP player;
    private final String selectedEntityID;

    public GenerationsMiscSpawnEvent (EntityLivingBase entity, EntityPlayerMP player, String selectedEntityID) {

        this.entity = entity;
        this.player = player;
        this.selectedEntityID = selectedEntityID;

    }

    public EntityLivingBase getEntity() {

        return this.entity;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public String getSelectedEntityID() {

        return this.selectedEntityID;

    }

}
