package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnerCoordinator;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = BetterPixelmonSpawner.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) throws ObjectMappingException {

        HeldItemUtils.load();
        BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
        InfoRegistry.loadPokemonSpawnData();
        BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
        BossPokemonUtils.loadBossList();

        MinecraftForge.EVENT_BUS.register(new JoinListener());
        MinecraftForge.EVENT_BUS.register(new RespawnListener());
        MinecraftForge.EVENT_BUS.register(new PokemonSpawnListener());
        MinecraftForge.EVENT_BUS.register(new ShinySpawnListener());

        // I'll never understand what kind of egotistical group NEEDS their own event bus for no reason, but whatever, let's be different for funzies.
        Pixelmon.EVENT_BUS.register(new BattleListener());
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new DefeatListener());

        // Most importantly, we gotta turn their laggy shit off so the server doesn't shit itself and die.
        // Timer is needed to delay the task, as it will error out if not delayed, and I'm not aware of any event that fires later than this one.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                SpawnerCoordinator.PROCESSOR.shutdown();
                PixelmonSpawning.coordinator.deactivate();
                PixelmonSpawning.coordinator = null;

            }

        }, 3000);

    }

}
