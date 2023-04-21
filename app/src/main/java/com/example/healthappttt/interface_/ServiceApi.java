package com.example.healthappttt.interface_;

import com.example.healthappttt.Data.ExerciseResponse;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.Data.RoutineExerciseData;
import com.example.healthappttt.Data.RoutineResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/CreateRoutine")
    Call<List<RoutineResponse>> createRoutine(@Body RoutineData data);

    @POST("/CreateExercise")
    Call<List<ExerciseResponse>> createExercise(@Body List<RoutineExerciseData> data);
}
