package com.example.healthappttt.Data;

import java.util.Comparator;

public class RoutineComparator implements Comparator<RoutineData> {
    @Override
    public int compare(RoutineData r1, RoutineData r2) {
        int comp = r1.getStartTime().compareTo(r2.getStartTime());

        if (comp > 0)      return 1;
        else if (comp < 0) return -1;

        return 0;
    }
}
