package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class SuctionCups {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("SuctionCups") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Suction Cups");

    }

    public static boolean applies (Pokemon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("SuctionCups") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Suction Cups");

    }

}
