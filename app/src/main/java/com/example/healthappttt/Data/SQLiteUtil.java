package com.example.healthappttt.Data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.WittListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SQLiteUtil { // 싱글톤 패턴으로 구현
    private static volatile SQLiteUtil instance; // volatile 메인 메모리에 저장
    private SQLiteDatabase db;
    private String table;

    private SQLiteUtil() {

    }

    public static SQLiteUtil getInstance() {
        if (instance == null) { // synchronized 성능 저하 문제 해결법, double check locking
            synchronized (SQLiteUtil.class) {
                if (instance == null) {
                    instance = new SQLiteUtil();
                }
            }
        }

        return instance;
    }

    public void setInitView(Context context, String tableName) {
        this.table = tableName;

        DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);

        try {
            db = dbHelper.getWritableDatabase();
            dbHelper.onCreate(db);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(table, " 데이터 베이스를 열 수 없음");
        }
    }
    
    // 다른 메서드들

    /**
     * 이 메소드는 Witt_History_TB 데이터를 삽입하는 메서드입니다. ( 서버 유저테이블+Ex_Record )
     */
    public void insertWH(WittListData WittList){
        ContentValues values = new ContentValues();

        if(table.equals("Witt_History_TB")){
            values.put("RECORD_PK",WittList.getRECORD_PK());
            values.put("USER_FK",WittList.getUSER_FK());
            values.put("OUser_FK",WittList.getOUser_FK());
            values.put("TS", String.valueOf(WittList.getTS()));
            values.put("User_NM",WittList.getUser_NM());
            values.put("User_Img",WittList.getUser_Img());

            long result = db.insert(table,null,values);
            Log.d("Witt_History_TB",result+"성공");
        }else {
            Log.d("Witt_History_TB","매서드 형식 오류 ");
        }
        db.close();
    }

    /**
     * 이 메소드는 REVIEW_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insertRL(ReviewListData reviewList) {
        ContentValues values = new ContentValues();

        if(table.equals("REVIEW_TB")){
            // 동일한 Review_PK 값이 이미 존재하는지 확인
//            int existingReviewPK = checkExistingReviewPK(reviewList.getReview_PK());
//            if (existingReviewPK != -1) {
//                // 이미 존재하는 경우
//                Log.d("REVIEW_TB_INSERT","리뷰pk 이미 존재한다");
//                return; // 삽입하지 않고 메서드 종료
//            }
            values.put("Review_PK",reviewList.getReview_PK());
            values.put("User_FK",reviewList.getUser_FK());
            values.put("RPT_User_FK",reviewList.getRPT_User_FK());
            values.put("Text_Con",reviewList.getText_Con());
            values.put("Check_Box",reviewList.getCheck_Box());
            values.put("TS",reviewList.getTS());
            values.put("User_NM",reviewList.getUser_NM());
            values.put("User_Img",reviewList.getUser_Img());

            long result = db.insert(table,null,values);
            Log.d("ReviewTB",result+"성공");
        }else {
            Log.d("ReviewTB","매서드 형식 오류 ");
        }
        db.close();

    }
  
    // 동일한 Review_PK 값이 이미 존재하는지 확인하는 메서드
    private int checkExistingReviewPK(int reviewPK) {
        String sql = "SELECT Review_PK FROM REVIEW_TB WHERE Review_PK = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(reviewPK)});
        int existingReviewPK = -1;
        if (cursor.moveToFirst()) {
            existingReviewPK = cursor.getInt(0);
        }
        cursor.close();
        return existingReviewPK;
    }

    public int insert(int userKey, int myFlag, String message, int chatRoomId, int Success) {
        int lastKey = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("USER_FK", userKey);
            values.put("MSG", message);
            values.put("CHAT_ROOM_FK", chatRoomId);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            values.put("TS", timestamp);
            values.put("MYFLAG", myFlag);
            values.put("SUCCESS", Success);
            long rowId = db.insertOrThrow("CHAT_MSG_TB", null, values);
            if (rowId != -1) {
                String getLastKeyQuery = "SELECT MSG_PK FROM CHAT_MSG_TB ORDER BY MSG_PK DESC LIMIT 1";
                Cursor cursor = db.rawQuery(getLastKeyQuery, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int lastKeyIndex = cursor.getColumnIndex("MSG_PK");
                    lastKey = cursor.getInt(lastKeyIndex);
                    cursor.close();
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "데이터베이스 오류: " + e.getMessage());
        } finally {
            db.close();
            return lastKey;
        }
    }


    /**
     * 이 메소드는 BLACK_LIST_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insertBL(BlackListData BlackList) {
        ContentValues values  = new ContentValues();

        if (table.equals("BLACK_LIST_TB")){
            values.put("BL_PK", BlackList.getBL_PK());
            values.put("User_NM", BlackList.getUser_NM());
            values.put("OUser_FK", BlackList.getOUser_FK());
            values.put("TS", BlackList.getTS());
            values.put("User_Img",BlackList.getUser_Img());

            long result = db.insert(table,null,values);
            Log.d("blackTB",result + "성공");
        } else  {
            Log.d("Black_TB","매서드 형식 오류 ");
        }
        db.close();
    }


    /**
     * 이 메소드는 RT_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insert(RoutineData routine) {
        ContentValues values = new ContentValues();

        if (table.equals("RT_TB")) {
            values.put("PK", routine.getID());
            values.put("Start_Time", routine.getStartTime());
            values.put("End_Time", routine.getEndTime());
            values.put("CAT", routine.getCat());
            values.put("Day_Of_Week", routine.getDayOfWeek());

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    }

    /**
     * 이 메소드는 RECORD_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insert(RecordData record) {
        ContentValues values = new ContentValues();

        if (table.equals("RECORD_TB")) {
            values.put("PK", record.getID());
            values.put("OUser_FK", record.getoUserID());
            values.put("PROMISE_FK", record.getPromiseID());
            values.put("Start_Time", record.getStartTime());
            values.put("End_Time", record.getEndTime());
            values.put("Run_Time", record.getRunTime());
            values.put("CAT", record.getCat());

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    }

    /**
     * 이 메소드는 EX_TB에 데이터를 삽입하는 메서드입니다.
     */
    public void insert(ExerciseData exercise, boolean isRecord) {
        ContentValues values = new ContentValues();

        if (table.equals("EX_TB") && !isRecord) {
            values.put("PK", exercise.getID());
            values.put("RT_FK", exercise.getParentID());
            values.put("Ex_NM", exercise.getExerciseName());
            values.put("Set_Or_Time", exercise.getSetOrTime());
            values.put("Volume", exercise.getVolume());
            values.put("Cnt_Or_Dis", exercise.getCntOrDis());
            values.put("Sort_Index", exercise.getIndex());
            values.put("CAT", exercise.getCat());

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else if (table.equals("EX_TB") && isRecord) {
            values.put("PK", exercise.getID());
            values.put("RECORD_FK", exercise.getParentID());
            values.put("Ex_NM", exercise.getExerciseName());
            values.put("Set_Or_Time", exercise.getSetOrTime());
            values.put("Volume", exercise.getVolume());
            values.put("Cnt_Or_Dis", exercise.getCntOrDis());
            values.put("Sort_Index", exercise.getIndex());
            values.put("CAT", exercise.getCat());

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    }
    public void insert(List<UserChat> U){

        ContentValues values = new ContentValues();
        for(UserChat u : U) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (table.equals("CHAT_ROOM_TB")) {
                values.put("CHAT_ROOM_PK", u.getChatRoomId());
                values.put("LAST_MSG_INDEX", 1);
                values.put("OTHER_USER_NM", u.getUserNM());
                values.put("FAV", 0);
                values.put("TS", timestamp);

                long result = db.insert(table, null, values);
                Log.d(table, result + "성공");
            } else Log.d(table, "실패 삽입 챗티방");
        }
        db.close();
    }
    public void delete(int PK) {
        String selection = "PK = ?";
        int result = db.delete(table, selection, new String[]{String.valueOf(PK)});
        Log.i(table, +result + "개 row delete 성공");
        db.close();
    }

    //로컬 db에서 해당 pk를 가진 행을 삭제하는 매서드
    public void deleteFromBlackListTable(int pk) {
        db.execSQL("DELETE FROM BLACK_LIST_TB WHERE BL_PK =" + pk);
        db.close();
    }
    
    public void deleteChatRoom(int chatRoomPk) {
        db.execSQL("DELETE FROM CHAT_MSG_TB WHERE CHAT_ROOM_FK =" + chatRoomPk);
        db.close();
        }
    public void deleteChatRoom() {
        db.execSQL("DELETE FROM CHAT_ROOM_TB");
        db.close();
    }

    public void Update(RoutineData routine) {
        ContentValues values = new ContentValues();

        if (table.equals("RT_TB")) {
            values.put("Start_Time", routine.getStartTime());
            values.put("End_Time", routine.getEndTime());
            values.put("CAT", routine.getCat());
            values.put("Day_Of_Week", routine.getDayOfWeek());

            int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(routine.getID())});
            Log.d(table, result + " update 성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    }


    public void Update(int RECORD_PK, int OUser_FK, int Start_Time, String End_Time, String Run_Time, int CAT, int PROMISE_FK, String TS) {
        ContentValues values = new ContentValues();

        // 이 코드는 사용 안 할 수도 있음. 하더라도 수정 필요
        
        if (table.equals("RECORD_TB")) {
            values.put("PK", RECORD_PK);
            values.put("OUser_FK", OUser_FK);
            values.put("Start_Time", Start_Time);
            values.put("End_Time", End_Time);
            values.put("Run_Time", Run_Time);
            values.put("CAT", CAT);
            values.put("PROMISE_FK", PROMISE_FK);
            values.put("TS", TS);

            int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(RECORD_PK)});
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    } // 운동 기록을 수정할 수 있게 할지는 아직 모름

    public void UpdateOrInsert(ExerciseData exercise) {
        ContentValues values = new ContentValues();

        values.put("PK", exercise.getID());
        values.put("RT_FK", exercise.getParentID());
        values.put("Ex_NM", exercise.getExerciseName());
        values.put("Set_Or_Time", exercise.getSetOrTime());
        values.put("Volume", exercise.getVolume());
        values.put("Cnt_Or_Dis", exercise.getCntOrDis());
        values.put("Sort_Index", exercise.getIndex());
        values.put("CAT", exercise.getCat());

        Cursor cursor = db.query(table, null,  "PK = ?", new String[]{String.valueOf(exercise.getID())}, null, null, null);
        if (cursor.moveToFirst()) {
            // 존재하면 업데이트 수행
            int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(exercise.getID())});
            Log.d(table, result + "업데이트 성공");
            cursor.close();
        } else {
            // 존재하지 않으면 삽입 수행
            long result = db.insert(table, null, values);
            Log.d(table, result + "삽입 성공");
            cursor.close();
        }
        db.close();
    }

    public void Update(int userPk,int chatPk) {
        ContentValues values = new ContentValues();
        if (table.equals("CHAT_MSG_TB")) {
            values.put("SUCCESS", 1);
            int result = -1;
            try {
                result = db.update("CHAT_MSG_TB", values, "USER_FK = ? AND MSG_PK = ?", new String[]{String.valueOf(userPk),String.valueOf(chatPk)});
            } catch (SQLException e) {
                Log.e(TAG, "데이터베이스 오류: " + e.getMessage());
            }

            if (result > 0) {
                String getSuccessQuery = "SELECT SUCCESS FROM CHAT_MSG_TB WHERE MSG_PK = " + chatPk;
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(getSuccessQuery, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int successIndex = cursor.getColumnIndex("SUCCESS");
                        int successValue = cursor.getInt(successIndex);
                        Log.d(table, "업데이트 성공 - SUCCESS 값: " + successValue);
                    } else {
                        Log.d(table, "업데이트 성공 - SUCCESS 값 조회 실패");
                    }
                } catch (SQLException e) {
                    Log.e(TAG, "데이터베이스 오류: " + e.getMessage());
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else {
                Log.d(table, "업데이트 실패 - 해당 chatPk를 가진 행이 없거나 업데이트 과정에서 오류 발생");
            }
        } else {
            Log.d(table, "메서드 형식 오류");
        }
        db.close();
    }


    public ArrayList<RoutineData> SelectRoutine(int DayOfWeek) {
        if (table.equals("RT_TB")) {
            String sql = "SELECT * FROM " + table + " WHERE Day_Of_Week = " + DayOfWeek + ";";

            Log.d("SQLite SelectRoutine", sql);

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<RoutineData> routines = new ArrayList<>();

            while(cursor.moveToNext()) {
                routines.add(new RoutineData(
                        cursor.getInt(0),    // ID
                        cursor.getString(1), // startTime
                        cursor.getString(2), // endTime
                        cursor.getInt(3),    // cat
                        cursor.getInt(4)     // dayOfWeek
                ));
            }

            return routines;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }

    public ArrayList<RecordData> SelectRecord(String Date) {
        if (table.equals("RECORD_TB")) {
            String sql = "SELECT * FROM " + table + " WHERE TS LIKE '" + Date + "%';";

            Log.d("SQLite SelectRecord", sql);

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<RecordData> records = new ArrayList<>();

            while(cursor.moveToNext()) {
                records.add(new RecordData(
                        cursor.getInt(0),    // ID
                        cursor.getInt(1),    // OUser_FK
                        cursor.getInt(2),    // PROMISE_FK
                        cursor.getString(3), // Start_Time
                        cursor.getString(4), // End_Time
                        cursor.getString(5), // Run_Time
                        cursor.getInt(6)
                ));
            }

            return records;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }

    public ArrayList<ExerciseData> SelectExercise(int RT_PK, boolean isRoutine) {
        if (table.equals("EX_TB")) {
            String sql = "SELECT * FROM " + table + " WHERE RT_FK = " + RT_PK + ";";

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<ExerciseData> exercises = new ArrayList<>();

            int parentID = 1;
            if (!isRoutine)  parentID = 2;

            while(cursor.moveToNext()) {
                ExerciseData e = new ExerciseData( // 순서 잘 지킬 것, 나중에 수정
                        cursor.getInt(0),   // PK,          ID
                        cursor.getInt(parentID),   // RT FK or RECORD__FK,       parentID,
                        cursor.getString(3),// Ex_NM,       title
                        cursor.getInt(8),   // CAT,         cat
                        cursor.getInt(4),   // Set_Or_Time, count:set
                        cursor.getInt(5),   // Volume,      volume
                        cursor.getInt(6),   // Cnt_Or_Dis,  num:Cnt
                        cursor.getInt(7)    // Sort_Index,  index
                );

                exercises.add(e);
            }

            return exercises;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }

    public ArrayList<BlackListData> SelectBlackUser() {
        if (table.equals("BLACK_LIST_TB")) {
            String sql = "SELECT * FROM BLACK_LIST_TB";

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<BlackListData> BlackListData = new ArrayList<>();

            while(cursor.moveToNext()) {
                BlackListData e = new BlackListData(        // 순서 잘 지킬 것, 나중에 수정
                        cursor.getInt(0),        //BL_PK
                        cursor.getString(1),     // User_NM,
                        cursor.getInt(2),        //OUser_FK
                        cursor.getString(3),     // TS
                        cursor.getBlob(4)        //User_Img
                );
                BlackListData.add(e);
            }
            return BlackListData;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }
  
    public ArrayList<ReviewListData> SelectReviewUser() {
        if (table.equals("REVIEW_TB")) {
            String sql = "SELECT * FROM REVIEW_TB";

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<ReviewListData> reviewListData = new ArrayList<>();

            while(cursor.moveToNext()) {
                ReviewListData e = new ReviewListData(         // 순서 잘 지킬 것, 나중에 수정
                        cursor.getInt(0),           //Review_PK
                        cursor.getInt(1),           //User_FK
                        cursor.getInt(2),           //RPT_User_FK
                        cursor.getString(3),        // Text_Con,
                        cursor.getInt(4),           //Check_Box
                        cursor.getString(5),        // TS
                        cursor.getString(6),        //User_NM
                        cursor.getBlob(7)           //User_Img
                );
                reviewListData.add(e);
                Log.d("SQLite SelectReviewUser", "리뷰리스트데이터: " + e.getText_Con());
            }
            return reviewListData;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }
    public ArrayList<WittListData> SelectWittHistoryUser() {
        if (table.equals("Witt_History_TB")) {
            String sql = "SELECT * FROM Witt_History_TB ORDER BY TS DESC";//날짜별 내림차순으로 정렬하여 select

            Cursor cursor = db.rawQuery(sql, null);
            Log.d("커서 sqliteutil",cursor.getColumnName(0));
            ArrayList<WittListData> wittListData = new ArrayList<>();

            while(cursor.moveToNext()) {
                WittListData e = new WittListData(         // 순서 잘 지킬 것, 나중에 수정

                        cursor.getInt(0),           //RECORD_PK
                        cursor.getInt(1),           //
                        cursor.getInt(2),           //OUser_FK
                        cursor.getString(3),        // TS
                        cursor.getString(4),        //User_NM
                        cursor.getBlob(5)           //User_Img
                );
                wittListData.add(e);
                Log.d("SQLite SelectWittHis", "위트내역 리스트 데이터: " + e.getTS());
            }
            return wittListData;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }
        db.close();
        return null;
    }
    public List<MSG> SelectAllMSG(String userKey,int chatRoomId){
        List<MSG> messages = null;
        if (table.equals("CHAT_MSG_TB")) {
            messages = new ArrayList<>();
            String sql = "SELECT MSG, CHAT_ROOM_FK, TS, MYFLAG,SUCCESS FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ? ORDER BY TS ASC";
            String[] selectionArgs = {userKey, String.valueOf(chatRoomId)};
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String message = "값 없음";
                    String TS = "";
                    String mfl = "1";
                    int successIndex = cursor.getColumnIndex("SUCCESS");
                    int success = -1;
                    int MSGIndex = cursor.getColumnIndex("MSG");
                    int mf =  cursor.getColumnIndex("MYFLAG");
                    int TSIndex = cursor.getColumnIndex("TS");
                    if (MSGIndex != -1) {
                        message = cursor.getString(MSGIndex);
                        // 메시지 처리 로직 작성
                    } else {
                        // 컬럼이 존재하지 않을 경우 처리 로직 작성
                    }
                    if(successIndex != -1){
                        success = cursor.getInt(successIndex);
                    }
                    if(TSIndex != -1){
                        TS = cursor.getString(TSIndex);
                    }
                    if(mf != -1)
                    {
                        mfl = cursor.getString(mf);
                    }
                    messages.add(new MSG(Integer.parseInt(mfl),chatRoomId,message,TS,success));
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return messages;
    }
    public int selectLastMsgPk(){
        String sql = "SELECT MSG_PK FROM CHAT_MSG_TB ORDER BY TS ASC LIMIT 1 OFFSET (SELECT COUNT(*) - 1 FROM CHAT_MSG_TB)";
        Cursor cursor = db.rawQuery(sql, null);
        int MSG_PK =  -1;
        if (cursor != null && cursor.moveToFirst()) {
            do {

                int MSG_PK_index = cursor.getColumnIndex("MSG_PK");
                if (MSG_PK_index != -1) {
                    MSG_PK = cursor.getInt(MSG_PK_index);
                }
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return MSG_PK;
    }



    public void DropUser(Context context) {

        DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        while (cursor.moveToNext()) {
            String tableName = cursor.getString(0);
            if (!tableName.equals("sqlite_sequence")) { //시스템 테이블 제외하고 모든 테이블 삭제
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            }
        }
        cursor.close();
        db.close();

    }

//    public List<MSG> SelectMSG(String userKey,int myFlag, int chatRoomId) {
//        List<MSG> messages = new ArrayList<>();
//
//        String[] columns = {"MSG","CHAT_ROOM_FK","TS"};
//        String selection = "USER_FK = ? AND CHAT_ROOM_FK=? AND MYFLAG=? AND READ = ?";
//        String[] selectionArgs = {userKey,String.valueOf(chatRoomId),String.valueOf(myFlag), String.valueOf(1)};
//        String orderBy = "TS ASC"; // 시간 순으로 정렬
//
//        Cursor cursor = db.query("CHAT_MSG_TB", columns, selection, selectionArgs, null, null, orderBy);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                String message = "값 없음";
//                String TS = "";
//                int MSGIndex = cursor.getColumnIndex("MSG");
//                int TSIndex = cursor.getColumnIndex("TS");
//                if (MSGIndex != -1) {
//                    message = cursor.getString(MSGIndex);
//                    // 메시지 처리 로직 작성
//                } else {
//                    // 컬럼이 존재하지 않을 경우 처리 로직 작성
//                }
//                if(TSIndex != -1){
//                    TS = cursor.getString(TSIndex);
//                }
//
//                messages.add(new MSG(myFlag,chatRoomId,message,TS));
//            } while (cursor.moveToNext());
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("READ", 0);
//        String updateSelection = "CHAT_ROOM_FK=? AND MYFLAG=? AND READ=?";
//        String[] updateSelectionArgs = {String.valueOf(chatRoomId), String.valueOf(myFlag), "1"};
//        db.update("CHAT_MSG_TB", contentValues, updateSelection, updateSelectionArgs);
//
//        if (cursor != null) {
//            cursor.close();
//        }
//        db.close();
//
//        return messages;
//    }
}
