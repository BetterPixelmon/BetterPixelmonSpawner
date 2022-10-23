package com.lypaka.betterpixelmonspawner.Utils;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.time.LocalDateTime;

public class LegendaryInfoGetters {

    private static PixelmonEntity pokemon;
    private static String legendaryName;
    private static ServerPlayerEntity spawnedPlayer;
    private static BlockPos spawnPos;
    public static String time;

    public static PixelmonEntity getPokemon() {

        return pokemon;

    }

    public static void setPokemon (PixelmonEntity poke) {

        pokemon = poke;

    }

    public static String getLegendaryName() {

        return legendaryName;

    }

    public static void setLegendaryName (String name) {

        legendaryName = name;

    }

    public static ServerPlayerEntity getSpawnedPlayer() {

        return spawnedPlayer;

    }

    public static void setSpawnedPlayer (ServerPlayerEntity player) {

        spawnedPlayer = player;

    }

    public static BlockPos getSpawnPos() {

        return spawnPos;

    }

    public static void setSpawnPos (BlockPos pos) {

        spawnPos = pos;

    }

    public static String getSpawnLocationString() {

        String name = spawnedPlayer.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
        return "World: " + name + " | X: " + spawnPos.getX() + " | Y: " + spawnPos.getY() + " | Z: " + spawnPos.getZ();

    }

    public static String getTime() {

        return time;

    }

    public static void setTime (LocalDateTime now) {

        time = now.getMonth().name() + " " + now.getDayOfMonth() + ", " + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond();

    }

}
