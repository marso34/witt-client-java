package com.example.healthappttt.interface_;

import com.example.healthappttt.Data.UserKey;
import com.example.healthappttt.Data.UserProfile;
import com.example.healthappttt.Data.ExerciseResponse;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.Data.RoutineExerciseData;
import com.example.healthappttt.Data.pkData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/CreateRoutine")
    Call<List<Integer>> createRoutine(@Body RoutineData data);

    @POST("/CreateExercise")
    Call<List<ExerciseResponse>> createExercise(@Body List<RoutineExerciseData> data);

    @POST("/DeleteRoutine")
    Call<Integer> deleteRoutine(@Body pkData data);

    @POST("/UpdateRoutine")
    Call<Integer> updateRoutine(@Body RoutineData data);

    @POST("/getuserkey")
    Call<List<UserProfile>> getuserprofile(@Body UserKey userKey); // 파라미터(User_PK)를 줘서 동적으로 변경 필요
}