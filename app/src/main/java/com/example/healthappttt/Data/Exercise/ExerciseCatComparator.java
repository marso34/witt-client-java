package com.example.healthappttt.Data.Exercise;

import java.util.Comparator;

public class ExerciseCatComparator implements Comparator<ExerciseData> {
    @Override
    public int compare(ExerciseData e1, ExerciseData e2) {
        if (e1.getCat() > e2.getCat())      return 1;
        else if (e1.getCat() < e2.getCat()) return -1;
        return 0;
    }
}
