package com.lypaka.betterpixelmonspawner.Holidays;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterpixelmonspawner.BetterPixelmonSpawner;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.time.LocalDate;
import java.util.*;

public class HolidayHandler {

    public static List<Holiday> activeHolidays = new ArrayList<>();
    private static Timer timer = null;

    public static void startHourTracker() {

        if (timer != null) {

            timer.cancel();

        }

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                BetterPixelmonSpawner.currentDay = LocalDate.now();
                BetterPixelmonSpawner.logger.info("Checking for holiday update...");
                if (HolidayHandler.activeHolidays != null) {

                    HolidayHandler.activeHolidays.removeIf(holiday -> {

                        if (!holiday.isWithinDate(holiday.getMonth(), holiday.getDayStart(), holiday.getDayEnd())) {

                            holiday.deactivate();
                            return true;

                        }

                        return false;

                    });

                }
                for (Holiday holiday : Holiday.holidayList) {

                    if (holiday.isWithinDate(holiday.getMonth(), holiday.getDayStart(), holiday.getDayEnd())) {

                        if (!holiday.isActive()) {

                            holiday.activate();

                        }

                    }

                }

            }

        },0, 3600000);

    }

    public static void loadHolidays() throws ObjectMappingException {

        BetterPixelmonSpawner.logger.info("Registering holidays...");
        Map<String, Map<String, Integer>> holidayMap = BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays").getValue(new TypeToken<Map<String, Map<String, Integer>>>() {});
        for (Map.Entry<String, Map<String, Integer>> entry : holidayMap.entrySet()) {

            String name = entry.getKey();
            Map<String, Integer> values = entry.getValue();
            int month = values.get("Month");
            int dayStart = values.get("Day-Start");
            int dayEnd = values.get("Day-End");
            List<String> particles = new ArrayList<>();
            if (!BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays", name, "Special-Textures").isVirtual()) {

                particles = BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays", name, "Particles").getList(TypeToken.of(String.class));

            }
            List<String> pokemon = BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays", name, "Pokemon").getList(TypeToken.of(String.class));
            List<String> textures = new ArrayList<>();
            if (!BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays", name, "Special-Textures").isVirtual()) {

                textures = BetterPixelmonSpawner.configManager.getConfigNode(1, "Holidays", name, "Special-Textures").getList(TypeToken.of(String.class));

            }
            Holiday holiday = new Holiday(name, month, dayStart, dayEnd, pokemon, particles, textures);
            holiday.register();

        }

        startHourTracker();

    }

}
