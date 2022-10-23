package com.lypaka.betterpixelmonspawner.API;

import com.pixelmonmod.pixelmon.entities.npcs.NPCEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a NPC is set to be spawned on a player
 */
@Cancelable
public class NPCSpawnEvent extends Event {

    private NPCEntity npc;
    private final ServerPlayerEntity player;

    public NPCSpawnEvent (NPCEntity npc, ServerPlayerEntity player) {

        this.npc = npc;
        this.player = player;

    }

    public NPCEntity getNPC() {

        return this.npc;

    }

    public void setNPC (NPCEntity npc) {

        this.npc = npc;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

}
