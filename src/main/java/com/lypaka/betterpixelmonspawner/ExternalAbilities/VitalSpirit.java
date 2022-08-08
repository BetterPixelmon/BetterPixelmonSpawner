package com.lypaka.betterpixelmonspawner.ExternalAbilities;

import com.lypaka.betterpixelmonspawner.Config.ConfigGetters;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;

public class VitalSpirit {

    public static boolean applies (EntityPixelmon pokemon) {

        if (!ConfigGetters.externalAbilitiesEnabled) return false;
        if (pokemon == null) return false;
        return pokemon.getAbility().getLocalizedName().equalsIgnoreCase("VitalSpirit") || pokemon.getAbility().getLocalizedName().equalsIgnoreCase("Vital Spirit");

    }

}