package com.example.healthappttt.Data;

import java.util.Comparator;

public class RoutineComparator implements Comparator<Routine> {
    @Override
    public int compare(Routine r1, Routine r2) {
        int comp = r1.getStartTime().compareTo(r2.getStartTime());

        if (comp > 0)      return 1;
        else if (comp < 0) return -1;

        return 0;
    }
}
