package com.example.healthappttt.interface_;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.Chat.getMSGKey;
import com.example.healthappttt.Data.Exercise.GetRoutine;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.GetUserInfo;
import com.example.healthappttt.Data.User.NearUsersData;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.ReviewData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UploadResponse;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.UserProfile;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.Data.User.email;
import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.Data.WittSendData;
import com.example.healthappttt.Data.pkData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceApi {
    @POST("/sendDataToIP")
    Call<Integer> sendDataToIP(@Body MSD m);
  
    @POST("/profile/getuserkey")// 유저 테이블에 있는 자신의 데이터 가져오는 api
    Call<List<UserProfile>> getuserprofile(@Body UserKey userKey); // 파라미터(User_PK)를 줘서 동적으로 변경 필요

    @POST("/profile/getBlackList")// 차단목록에서 차단 유저 리스트 가져오는 api
    Call<List<BlackListData>> getBlackList(@Body UserKey userKey);
    @POST("/profile/insertBL")
    Call<String> insertBL(@Body pkData pD);
    @POST("/profile/getReviewList") //받은 후기에서 후기 리스트 가져오는 api
    Call<List<ReviewListData>> getReviewList(@Body UserKey userKey);

    @POST("/profile/deleteFromServer") //차단하기에서 차단 항목 삭제하는 api
    Call<Integer> deleteFromServer(@Body UserKey BL_PK);


    @POST("/profile/getWittHistory")
    Call<List<WittListData>> getWittHistory(@Body UserKey userKey);
    @POST("/leaveChatRoom")
    Call<String> leaveChatRoom(@Body pkData pk);
    @POST("/BlackChatRoom")
    Call<String> blackChatRoom(@Body pkData pk);
    @POST("/profile/EditBodyInfo")// 수정된 키 몸무게 정보 업데이트
    Call<String> EditBodyInfo(@Body Map<String, Object> BodyInfo);

    @POST("/profile/EditWeightVol")// 수정된 3대운동 정보 업데이트
    Call<String> EditWeightVol(@Body Map<String, Object> WeightVol);

    @POST("/profile/getGym")// 헬스장 정보 동기화
    Call<Map<String, Object>> getGym(@Body UserKey userKey);

    @POST("/profile/EditGYM")// 헬스장 정보 업데이트
    Call<String> EditGYM(@Body Map<String, Object> LocPKInfo);

    @POST("/profile/getOtherProfile")// 상세 프로필 가져오기
    Call<Map<String,Object>> getOtherProfile(@Body UserKey userKey);

    @POST("/profile/getOtherEval")// 상세 받은 평가 가져오기
    Call<ArrayList<ReviewListData>> getOtherEval(@Body UserKey userKey);

    @POST("/profile/getReport")//신고 내역
    Call<List<ReportHistory>> getReport(@Body UserKey userKey);

    @POST("/profile/deleteUser")//탈퇴 하기
    Call<String> deleteUser(@Body Map<String, Object> DROP);
    @POST("/updateAlarmList")
    Call<ArrayList<AlarmInfo>> getAlarmList(@Body pkData data);
    @POST("/profile/updateRPT")//신고하기
    Call<String> updateRPT(@Body Map<String, Object> RPT);

    @POST("/mail/reportmail") // 신고 메일 보내기
    Call<String> reportmail(@Body Map<String,String> data);

//    ----------------------------------------------------------------------------------------------
    @POST("/routine/CreateRoutine")
    Call<List<Integer>> createRoutine(@Body RoutineData data);

    @POST("/routine/DeleteRoutine")
    Call<Integer> deleteRoutine(@Body pkData data);

    @POST("/routine/UpdateRoutine")
    Call<Integer> updateRoutine(@Body RoutineData data);

    @POST("/routine/SelectRoutine")
    Call<List<RoutineData>> selectRoutine(@Body GetRoutine data);

    @POST("/routine/SelectAllRoutine")
    Call<List<RoutineData>> selectAllRoutine(@Body pkData data);

    @POST("/record/CreateRecord")
    Call<List<Integer>> createRecord(@Body RecordData data);

    @POST("/record/SelectAllRecord")
    Call<List<RecordData>> selectAllRecord(@Body pkData data);
//    ----------------------------------------------------------------------------------------------

    @POST("/review/SendReivew")
    Call<Integer> sendReivew(@Body ReviewData data);

//    ----------------------------------------------------------------------------------------------
    @POST("/getMSGFromServer")
    Call<List<MSG>> getMSGFromServer(@Body getMSGKey data);

    @POST("/GetNearUsers")
    Call<List<UserInfo>> GetNearUsers(@Body NearUsersData data);

    @POST("/CheckUser")
    Call<ResponseBody> CheckUser(@Body email email);


    @POST("/saveUser")
    Call<Integer> sendData(@Body UserClass data);


    @Multipart
    @POST("/saveUserWithImage")
    Call<Integer> sendDataWithImage(@Part MultipartBody.Part image, @Body UserClass userData);

    @POST("/getUserInfo/{useremail}")
    Call<GetUserInfo> getUserInfo(@Path("useremail") String useremail);

    @POST("/findChatUsers")
    Call<List<UserChat>> getUsers(@Body pkData userKey);

    @POST("messages")
    Call<MSG> sendMessage(@Body MSG message);

    @POST("/makeChatRoom")
    Call<Integer> makeChatRoom(@Body WittSendData wittSendData);

    @Multipart
    @POST("/uploadImage")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @GET("/getImage/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);


}