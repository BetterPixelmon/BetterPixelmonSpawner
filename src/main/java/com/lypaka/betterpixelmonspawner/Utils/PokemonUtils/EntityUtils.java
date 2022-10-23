package com.lypaka.betterpixelmonspawner.Utils.PokemonUtils;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.lypaka.betterpixelmonspawner.Utils.Counters.MiscCounter;
import com.lypaka.betterpixelmonspawner.Utils.Counters.NPCCounter;
import com.pixelmonmod.pixelmon.entities.DenEntity;
import com.pixelmonmod.pixelmon.entities.npcs.*;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class EntityUtils {

    public static LivingEntity getEntityFromID (String biomeID, String id, World world) {

        switch (id) {

            case "pixelmon:npc_chatting":
                return new NPCChatting(world);

            case "pixelmon:npc_trainer":
                BaseTrainer baseTrainer = ServerNPCRegistry.trainers.getRandomBaseWithData();
                NPCTrainer trainer = new NPCTrainer(world);
                trainer.init(baseTrainer);
                return trainer;

            case "pixelmon:npc_trader":
                return new NPCTrader(world);

            case "pixelmon:npc_shopkeeper":
                NPCShopkeeper shopkeeper = new NPCShopkeeper(world);
                shopkeeper.initWanderingAI();
                shopkeeper.initRandom(biomeID);
                return shopkeeper;

            case "pixelmon:npc_tutor":
                return new NPCTutor(world);

            case "pixelmon:npc_relearner":
                return new NPCRelearner(world);

            case "pixelmon:npc_oldfisherman":
                return new NPCFisherman(world);

            case "pixelmon:npc_nursejoy":
                return new NPCNurseJoy(world);

            case "pixelmon:npc_questgiver":
                return new NPCQuestGiver(world);

            case "pixelmon:den":
                return new DenEntity(world);

            default:
                return null;

        }

    }

    public static void handleDespawning (LivingEntity entity) {

        long interval;
        if (entity instanceof NPCEntity) {

            if (ConfigGetters.npcDespawnTimer == 0) return;
            interval = ConfigGetters.npcDespawnTimer * 1000L;

        } else {

            if (ConfigGetters.miscDespawnTimer == 0) return;
            interval = ConfigGetters.miscDespawnTimer * 1000L;

        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (String tag : entity.getTags()) {

                    if (tag.contains("SpawnedPlayerUUID:")) {

                        String[] split = tag.split(":");
                        UUID uuid = UUID.fromString(split[1]);
                        if (entity instanceof NPCEntity) {

                            NPCCounter.decrement(uuid);

                        } else {

                            MiscCounter.decrement(uuid);

                        }
                        break;

                    }

                }

                entity.remove();

            }

        }, interval);

    }

}
