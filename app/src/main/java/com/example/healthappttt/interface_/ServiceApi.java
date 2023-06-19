package com.example.healthappttt.interface_;

import com.example.healthappttt.Data.GetUserInfo;
import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.NearUsersData;
import com.example.healthappttt.Data.RecordData;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.Data.UserData;
import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.Data.UserKey;
import com.example.healthappttt.Data.UserProfile;
import com.example.healthappttt.Data.pkData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {
    @POST("/getuserkey")
    Call<List<UserProfile>> getuserprofile(@Body UserKey userKey); // 파라미터(User_PK)를 줘서 동적으로 변경 필요

    @POST("/routine/CreateRoutine")
    Call<List<Integer>> createRoutine(@Body RoutineData data);

//    @POST("/routine/CreateExercise")
//    Call<List<ExerciseResponse>> createExercise(@Body List<RoutineExerciseData> data);

    @POST("/routine/DeleteRoutine")
    Call<Integer> deleteRoutine(@Body pkData data);

    @POST("/routine/UpdateRoutine")
    Call<Integer> updateRoutine(@Body RoutineData data);

    @POST("/record/RecordExercise")
    Call<List<Integer>> recordExercise(@Body RecordData data);

    @POST("/GetNearUsers")
    Call<List<UserInfo>> GetNearUsers(@Body NearUsersData data);
    
    @POST("/saveUser")
    Call<ResponseBody> sendData(@Body UserData data);

    @GET("/getUserInfo/{useremail}")
    Call<GetUserInfo> getUserInfo(@Path("useremail") String useremail);

    @GET("/users")
    Call<List<User>> getUsers();

    @POST("messages")
    Call<Message> sendMessage(@Body Message message);

}