package com.gwnu.witt.Data.Exercise;

import java.util.Comparator;

public class ExerciseComparator implements Comparator<ExerciseData> {
    @Override
    public int compare(ExerciseData e1, ExerciseData e2) {
        if (e1.getIndex() > e2.getIndex())      return 1;
        else if (e1.getIndex() < e2.getIndex()) return -1;
        return 0;
    }
}
