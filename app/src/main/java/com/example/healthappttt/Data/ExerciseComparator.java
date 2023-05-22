package com.example.healthappttt.Data;

import java.util.Comparator;

public class ExerciseComparator implements Comparator<Exercise> {
    @Override
    public int compare(Exercise e1, Exercise e2) {
        if (e1.getIndex() > e2.getIndex())      return 1;
        else if (e1.getIndex() < e2.getIndex()) return -1;

        return 0;
    }
}
