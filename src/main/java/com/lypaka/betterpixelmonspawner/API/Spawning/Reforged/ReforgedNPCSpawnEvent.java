package com.lypaka.betterpixelmonspawner.API.Spawning.Reforged;

import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a NPC is set to be spawned on a player
 */
@Cancelable
public class ReforgedNPCSpawnEvent extends Event {

    private EntityNPC npc;
    private final EntityPlayerMP player;

    public ReforgedNPCSpawnEvent (EntityNPC npc, EntityPlayerMP player) {

        this.npc = npc;
        this.player = player;

    }

    public EntityNPC getNPC() {

        return this.npc;

    }

    public void setNPC (EntityNPC npc) {

        this.npc = npc;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

}
