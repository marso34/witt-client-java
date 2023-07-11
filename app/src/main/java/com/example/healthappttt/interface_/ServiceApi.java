package com.example.healthappttt.interface_;

import com.example.healthappttt.Data.Exercise.GetRoutine;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.GetUserInfo;
import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.User.NearUsersData;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.UserProfile;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.Data.User.email;
import com.example.healthappttt.Data.pkData;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {
    @POST("/sendDataToIP")
    Call<Integer> sendDataToIP(@Body MSD m);
  
    @POST("/profile/getuserkey")// 유저 테이블에 있는 자신의 데이터 가져오는 api
    Call<List<UserProfile>> getuserprofile(@Body UserKey userKey); // 파라미터(User_PK)를 줘서 동적으로 변경 필요

    @POST("/profile/getBlackList")// 차단목록에서 차단 유저 리스트 가져오는 api
    Call<List<BlackListData>> getBlackList(@Body UserKey userKey);

    @POST("/getMSGFromServer")
    Call<List<MSG>> getMSGFromServer(@Body pkData pk);

    @POST("/profile/getReviewList") //받은 후기에서 후기 리스트 가져오는 api
    Call<List<ReviewListData>> getReviewList(@Body UserKey userKey);

    @POST("/profile/deleteFromServer") //차단하기에서 차단 항목 삭제하는 api
    Call<Integer> deleteFromServer(@Body UserKey BL_PK);


    @POST("/profile/getWittHistory")//위트 내역 리스트 가져오기
    Call<List<WittListData>> getWittHistory(@Body UserKey userKey);

    @POST("/profile/EditProfile")// 수정된 프로필 정보 업데이트
    Call<String> EditProfile(@Body Map<String, Object> editData);

    @POST("/routine/CreateRoutine")
    Call<List<Integer>> createRoutine(@Body RoutineData data);

//    @POST("/routine/CreateExercise")
//    Call<List<ExerciseResponse>> createExercise(@Body List<RoutineExerciseData> data);

    @POST("/routine/DeleteRoutine")
    Call<Integer> deleteRoutine(@Body pkData data);

    @POST("/routine/UpdateRoutine")
    Call<Integer> updateRoutine(@Body RoutineData data);

    @POST("/routine/SelectRoutine")
    Call<List<RoutineData>> selectRoutine(@Body GetRoutine data);

    @POST("/record/RecordExercise")
    Call<List<Integer>> recordExercise(@Body RecordData data);

    @POST("/GetNearUsers")
    Call<List<UserInfo>> GetNearUsers(@Body NearUsersData data);

    @POST("/CheckUser")
    Call<ResponseBody> CheckUser(@Body email email);


    @POST("/saveUser")
    Call<ResponseBody> sendData(@Body UserClass data);

    @POST("/getUserInfo/{useremail}")
    Call<GetUserInfo> getUserInfo(@Path("useremail") String useremail);

    @POST("/findChatUsers")
    Call<List<UserChat>> getUsers(@Body UserKey userKey);

    @POST("messages")
    Call<MSG> sendMessage(@Body MSG message);

}

