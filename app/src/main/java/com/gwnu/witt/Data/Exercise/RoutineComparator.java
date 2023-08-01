package com.gwnu.witt.Data.Exercise;

import java.util.Comparator;

public class RoutineComparator implements Comparator<RoutineData> {
    @Override
    public int compare(RoutineData r1, RoutineData r2) {
        if (r1.getTime() > r2.getTime())      return 1;
        else if (r1.getTime() < r2.getTime()) return -1;

        return 0;
    }
}
