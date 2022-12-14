package com.lypaka.betterpixelmonspawner.Listeners;

import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import com.lypaka.betterpixelmonspawner.PokeClear.ClearTask;
import com.lypaka.betterpixelmonspawner.PokemonSpawningInfo.InfoRegistry;
import com.lypaka.betterpixelmonspawner.Spawners.LegendarySpawner;
import com.lypaka.betterpixelmonspawner.Spawners.MiscSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.NPCSpawner;
import com.lypaka.betterpixelmonspawner.Spawners.PokemonSpawner;
import com.lypaka.betterpixelmonspawner.Utils.HeldItemUtils;
import com.lypaka.betterpixelmonspawner.Utils.PokemonUtils.BossPokemonUtils;
import com.lypaka.pixelboosters.PixelBoosters;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = BetterPixelmonSpawner.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) throws ObjectMappingException {

        if (ModList.get().isLoaded("pixelboosters")) {

            BetterPixelmonSpawner.isPixelBoostersLoaded = true;
            BetterPixelmonSpawner.logger.info("Detected PixelBoosters! Integrating...");

        }

        HeldItemUtils.load();
        BetterPixelmonSpawner.logger.info("Registering Pokemon spawns...");
        InfoRegistry.loadPokemonSpawnData();
        BetterPixelmonSpawner.logger.info("Registering Boss Pokemon spawns...");
        BossPokemonUtils.loadBossList();

        MinecraftForge.EVENT_BUS.register(new JoinListener());
        MinecraftForge.EVENT_BUS.register(new RespawnListener());
        MinecraftForge.EVENT_BUS.register(new PokemonSpawnListener());
        MinecraftForge.EVENT_BUS.register(new ShinySpawnListener());

        Pixelmon.EVENT_BUS.register(new BattleListener());
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new DefeatListener());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                //SpawnerCoordinator.PROCESSOR.shutdown();
                PixelmonSpawning.coordinator.deactivate();
                PixelmonSpawning.coordinator = null;

            }

        }, 3000);

        LegendarySpawner.startTimer();
        MiscSpawner.startTimer();
        NPCSpawner.startTimer();
        PokemonSpawner.startTimer();
        ClearTask.startClearTask();

    }

}
