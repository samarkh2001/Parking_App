package com.example.parking.ui.parking;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkData {
    public static List<String> CITIES = Arrays.asList("city1", "city2", "city3");
    public static Map<String, List<String>> PARK_MAP = new HashMap<>();

    public static int[][] slots = {
//            {-1, -1, 0, -1, -1},
//            {-1, 1, 0, 1, -1},
//            {0, 1, 1, 0, 1},
//            {1, 0, 0, 1, 1},
//            {-1, 0, 1, 1, -1}
            {0, 1, 1, 0},
            {1, 0, 0, 1},
            {0, 1, 0, 1},
            {1, 0, 1, 1}
    };

    public static int[] findClosestSlot(int[][] slots) {
        int[] closestSlot = new int[2];
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[i].length; j++) {
                if (slots[i][j] == 1) {
                    int distance = i + j;

                    if (distance < minDistance) {
                        minDistance = distance;
                        closestSlot[0] = i;
                        closestSlot[1] = j;
                    }
                }
            }
        }

        return closestSlot;
    }
}
