package com.gwnu.witt.Data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.gwnu.witt.Data.Chat.MSG;
import com.gwnu.witt.Data.Chat.UserChat;
import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.RecordData;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.Data.User.BlackListData;
import com.gwnu.witt.Data.User.ReviewListData;
import com.gwnu.witt.Data.User.WittListData;

import java.util.ArrayList;
import java.util.List;

public class SQLiteUtil { // 싱글톤 패턴으로 구현
    private static volatile SQLiteUtil instance; // volatile 메인 메모리에 저장
    private SQLiteDatabase db;
    private String table;
    private SQLiteUtil() {}

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
    public void setEmptyDB(){
        if(db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }
    public void setInitView(Context context, String tableName) {
        this.table = tableName;

        if (context != null) {
            DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);
            try {
                db = dbHelper.getWritableDatabase();
                dbHelper.onCreate(db);

            } catch (SQLiteException e) {
                e.printStackTrace();
                Log.e(table, "데이터베이스를 열 수 없음");
            }
        }


        // 다른 메서드들
    }
    /**
     * 이 메소드는 Witt_History_TB 데이터를 삽입하는 메서드입니다. ( 서버 유저테이블+Ex_Record )
     */
    public void insertWH(WittListData WittList){
        try{
            ContentValues values = new ContentValues();

            if(table.equals("Witt_History_TB")){
                values.put("RECORD_PK",WittList.getRECORD_PK());
                values.put("USER_FK",WittList.getUSER_FK());
                values.put("OUser_FK",WittList.getOUser_FK());
                values.put("TS", String.valueOf(WittList.getTS()));
                values.put("User_NM",WittList.getUser_NM());
                values.put("User_Img",WittList.getUser_Img());

//                long result = db.insert(table,null,values);
//                Log.d("Witt_History_TB",result+"성공");
            }else {
                Log.d("Witt_History_TB","매서드 형식 오류 ");
            }
        }finally {
            db.close();
        }


    }

    /**
     * 이 메소드는 REVIEW_TB 데이터를 삽입하는 메서드입니다.
     */

    public void insertRL(ReviewListData reviewList) {
        try{
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

//                long result = db.insert(table,null,values);
//                Log.d("ReviewTB",result+"성공");
            }else {
                Log.d("ReviewTB","매서드 형식 오류 ");
            }
        }finally {
            db.close();
        }

    }
    public void insert(AlarmInfo al) {
        if (table != null && table.equals("NOTIFY_TB")) {
            String query = "SELECT COUNT(*) FROM NOTIFY_TB WHERE USER_FK = ? AND NOTIFY_PK = ?";
            String[] selectionArgs = {String.valueOf(al.getNotifyKey()), String.valueOf(al.getNotifyKey())};

            Cursor cursor = db.rawQuery(query, selectionArgs);
            cursor.moveToFirst();
            int notifyCount = cursor.getInt(0);
            cursor.close();

            if (notifyCount == 0) {
                ContentValues values = new ContentValues();
                values.put("USER_FK", al.getUserKey());
                values.put("NOTIFY_PK", al.getNotifyKey());
                values.put("OUSER_NM",al.getOUSER_NM());
                values.put("OTHER_USER_FK", al.getOUserKey());
                values.put("NOTIFY_CATEGORY", al.getCat());
                values.put("LINK_PK", al.getLink());
                values.put("CREATED_TIME", al.getTS());
                values.put("READ_TIME", al.getTS());
                // 'IS_READ' 컬럼은 디폴트 값으로 설정되므로, 삽입 시 별도로 설정할 필요가 없습니다.

                long result = db.insert("NOTIFY_TB", null, values);
                if (result == -1) {
                    Log.e("INSERT", "레코드 삽입 실패");
                } else {
                    Log.d("INSERT", "레코드 삽입 성공");
                }
            } else {
                // 이미 해당 USER_FK와 NOTIFY_PK가 존재하는 경우에 대한 처리를 추가할 수 있습니다.
            }
        }
        db.close(); // 데이터베이스를 사용한 후 반드시 닫아줍니다.
    }


    /**
     * 이 메소드는 BLACK_LIST_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insertBL(BlackListData BlackList) {
        try {
            ContentValues values  = new ContentValues();

            if (table.equals("BLACK_LIST_TB")){
                values.put("BL_PK", BlackList.getBL_PK());
                values.put("User_NM", BlackList.getUser_NM());
                values.put("OUser_FK", BlackList.getOUser_FK());
                values.put("TS", BlackList.getTS());
                values.put("User_Img",BlackList.getUser_Img());

//                long result = db.insert(table,null,values);
//                Log.d("blackTB",result + "성공");
            } else  {
                Log.d("Black_TB","매서드 형식 오류 ");
            }
        }finally {
            db.close();
        }
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

    public long insert(int userKey, int myFlag, String message, int chatRoomId, int Success,String ts) {
        long lastKey = -1;
        try {
            if (table != null &&table.equals("CHAT_MSG_TB")) {
                Log.d(TAG, "insert : "+ts);
                ContentValues values = new ContentValues();
                values.put("USER_FK", userKey);
                values.put("MSG", message);
                values.put("CHAT_ROOM_FK", chatRoomId);
                values.put("TS", ts);
                values.put("MYFLAG", myFlag);
                values.put("SUCCESS", Success);
                long rowId =  db.insert("CHAT_MSG_TB", null, values);
                lastKey = rowId;
            }
        } catch (SQLException e) {
            Log.e(TAG, "데이터베이스 오류: " + e.getMessage());
        } finally {
            db.close();
            return lastKey;
        }
    }
    public void deleteOUserMsgs(String userKey){
        try {
            // MSG_PK 열의 마지막 튜플을 얻기 위한 쿼리를 작성합니다.
            if (table != null && table.equals("CHAT_MSG_TB")) {
                String query = "DELETE FROM CHAT_MSG_TB WHERE USER_FK != ?";
                String[] selectionArgs = {userKey};
            }
        } finally {
            db.close();

        }
    }

    public int getLastMyMsgPK(String chatRoomId,String userKey) {
        int lastMsgPK = 0;
        try {
            // MSG_PK 열의 마지막 튜플을 얻기 위한 쿼리를 작성합니다.
            if (table != null && table.equals("CHAT_MSG_TB")) {
                String query = "SELECT MAX(MSG_PK) FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ?";
                String[] selectionArgs = {userKey, chatRoomId,"1"};
                Cursor cursor = db.rawQuery(query, selectionArgs);
                if (cursor.moveToFirst()) {
                    lastMsgPK = cursor.getInt(0);
                }
                cursor.close();
            }
        } finally {
            db.close();
            return lastMsgPK;
        }
    }

    public int getLastAllMsgPK(String chatRoomId,String userKey) {
        int lastMsgPK = -1;
        try {
            // MSG_PK 열의 마지막 튜플을 얻기 위한 쿼리를 작성합니다.
            if (table != null && table.equals("CHAT_MSG_TB")) {
                String query = "SELECT MAX(MSG_PK) FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ?";
                String[] selectionArgs = {userKey, chatRoomId};
                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    lastMsgPK = cursor.getInt(0);
                }
                cursor.close();
            }
        }finally {
            db.close();
            return lastMsgPK;
        }
    }

    public int getLastOtherMsgPK(String chatRoomId,String userKey) {
        int lastMsgPK = -1;
        try {
            // MSG_PK 열의 마지막 튜플을 얻기 위한 쿼리를 작성합니다.
            if (table != null && table.equals("CHAT_MSG_TB")) {
                String query = "SELECT MAX(MSG_PK) FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ? AND MYFLAG = ?";
                String[] selectionArgs = {userKey, chatRoomId, "2"};
                Cursor cursor = db.rawQuery(query, selectionArgs);
                if (cursor.moveToFirst()) {
                    lastMsgPK = cursor.getInt(0);
                }
                cursor.close();
                db.close();
            }
        }finally {
            db.close();
            return lastMsgPK;
        }
    }

    public int insert(Context context, int chatPk, int userKey, int myFlag, String message, int chatRoomId, int Success, String ts) {
        DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int lastKey = -1;
        Log.d(TAG, "insert : " + chatPk);
        Log.d(TAG, "insert : " + ts);

        try {

                ContentValues values = new ContentValues();
                values.put("MSG_PK", chatPk);
                values.put("USER_FK", userKey);
                values.put("MSG", message);
                values.put("CHAT_ROOM_FK", chatRoomId);
                values.put("TS", ts);
                values.put("MYFLAG", myFlag);
                values.put("SUCCESS", Success);
                long rowId = db.insertOrThrow("CHAT_MSG_TB", null, values);
                lastKey = (int) rowId;

                Log.d(TAG, "insert: 삽인완료" + lastKey);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
            return lastKey;
        }
    }

    private int getIsReadValue(int userKey, int chatRoomId) {
        int isReadValue = -1;
        Cursor cursor = null;
        try {
            String query = "SELECT CHAT_ROOM_PK FROM CHAT_ROOM_TB WHERE CHAT_ROOM_PK = ? AND USER_FK = ?";
            String[] selectionArgs = {String.valueOf(chatRoomId),String.valueOf(userKey)};
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                isReadValue = cursor.getInt(cursor.getColumnIndexOrThrow("CHAT_ROOM_PK"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isReadValue;
    }










    /**
     * 이 메소드는 RT_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insert(RoutineData routine) {
        ContentValues values = new ContentValues();
        try {
            if (table.equals("RT_TB")) {
                values.put("PK", routine.getID());
                values.put("Time", routine.getTime());
                values.put("CAT", routine.getCat());
                values.put("Day_Of_Week", routine.getDayOfWeek());

                long result = db.insert(table, null, values);
                Log.d(table, result + "성공");
            } else {
                Log.d(table, "메서드 형식 오류");
            }
        } finally {
            db.close();
        }
    }

    /**
     * 이 메소드는 RECORD_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insert(RecordData record) {
        ContentValues values = new ContentValues();

        try {
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
        } finally {
            db.close();
        }
    }

    /**
     * 이 메소드는 EX_TB에 데이터를 삽입하는 메서드입니다.
     */
    public void insert(ExerciseData exercise, boolean isRecord) {
        ContentValues values = new ContentValues();

        try {
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
        } finally {
            db.close();
        }
    }
    public void deleteAllChatRoom(){
        try {
            String deleteQuery = "DELETE FROM CHAT_ROOM_TB";
            db.execSQL(deleteQuery, new Object[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public void deleteAllAlarms(Context context) {
        DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String deleteQuery = "DELETE FROM NOTIFY_TB";
            db.execSQL(deleteQuery, new Object[]{});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public void insert(Context context, int userKey, List<UserChat> U) {
        DBHelper dbHelper = null;
        SQLiteDatabase db = null;

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            for (UserChat u : U) {
                    // Check if the CHAT_ROOM_PK exists in the database
                        values.put("USER_FK", userKey);
                        values.put("CHAT_ROOM_PK", u.getChatRoomId());
                        values.put("LAST_MSG_INDEX", 1);
                        values.put("OTHER_USER_FK", u.getOtherUserKey());
                        values.put("OTHER_USER_NM", u.getUserNM());
                        values.put("FAV", 0);
                        // 헬스장 이름 넣기.
                        values.put("TS", u.getTS());
                        values.put("GYM_NM", u.getGNM());
                        values.put("ADR", u.getGADS());
                        long result = db.insert("CHAT_ROOM_TB", null, values);
                        Log.d("CHAT_ROOM_TB", result + "성공");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }


    public void delete(int PK) {
        String selection = "PK = ?";
        try {
            int result = db.delete(table, selection, new String[]{String.valueOf(PK)});
            Log.i(table, +result + "개 row delete 성공");
        } finally {
            db.close();
        }
    }

    public void deleteMulti(ArrayList<String> PK) { // 여러개
        try {
            String s = "delete  from " + table + " where PK NOT IN (";

            for (int i = 0; i < PK.size(); i++) {
                s += PK.get(i);
                if (i < PK.size()-1)
                    s += ", ";
            }
            s += ")";

            Log.d("삭제 sql", s);
            db.execSQL(s);
        } finally {
            db.close();
        }
    }

    //로컬 db에서 해당 pk를 가진 행을 삭제하는 매서드
    public void deleteFromBlackListTable(int pk) {
        try{
            db.execSQL("DELETE FROM BLACK_LIST_TB WHERE BL_PK =" + pk);
        }finally {
            db.close();
        }
    }

    public void deleteChatRoom(int userKey, int chatRoomPk) {
        try {
            String deleteQuery = "DELETE FROM CHAT_ROOM_TB WHERE CHAT_ROOM_PK = ? AND USER_FK = ?";
            db.execSQL(deleteQuery, new Object[]{chatRoomPk, userKey});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public void deleteChatRooms(Context context) {
        DBHelper dbHelper1 = null;
        SQLiteDatabase db1 = null;
        try {
            dbHelper1 = new DBHelper(context, "Witt", null, 1);
            db1 = dbHelper1.getWritableDatabase();
            db1.delete("CHAT_ROOM_TB", null,null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db1.close();
        }
    }


    public void deleteMSG(Context context, int chatPk) {
        DBHelper dbHelper = new DBHelper(context, "Witt", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {

                db.execSQL("DELETE FROM CHAT_MSG_TB WHERE MSG_PK =" + chatPk);

        } finally {
            db.close();
        }
    }

    public void Update(RoutineData routine) {
        ContentValues values = new ContentValues();

        try {
            if (table.equals("RT_TB")) {
                values.put("Time", routine.getTime());
                values.put("CAT", routine.getCat());
                values.put("Day_Of_Week", routine.getDayOfWeek());

                int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(routine.getID())});
                Log.d(table, result + " update 성공");
            } else {
                Log.d(table, "메서드 형식 오류");
            }
        } finally {
            db.close();
        }
    }

    public void Update(int RECORD_PK, int OUser_FK, int Start_Time, String End_Time, String Run_Time, int CAT, int PROMISE_FK, String TS) {
        ContentValues values = new ContentValues();

        // 이 코드는 사용 안 할 수도 있음. 하더라도 수정 필요

        try {
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
        } finally {
            db.close();
        }
    } // 운동 기록을 수정할 수 있게 할지는 아직 모름

    public void UpdateOrInsert(RoutineData routine) {
        ContentValues values = new ContentValues();

        try {
            values.put("PK", routine.getID());
            values.put("CAT", routine.getCat());
            values.put("Time", routine.getTime());
            values.put("Day_Of_Week", routine.getDayOfWeek());

            Cursor cursor = db.query(table, null,  "PK = ?", new String[]{String.valueOf(routine.getID())}, null, null, null);
            if (cursor.moveToFirst()) {
                // 존재하면 업데이트 수행
                int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(routine.getID())});
                Log.d(table, result + "업데이트 성공");
                cursor.close();
            } else {
                // 존재하지 않으면 삽입 수행
                long result = db.insert(table, null, values);
                Log.d(table, result + "삽입 성공");
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public void UpdateOrInsert(RecordData record) {
        ContentValues values = new ContentValues();

        try {
            values.put("PK", record.getID());
            values.put("OUser_FK", record.getoUserID());
            values.put("PROMISE_FK", record.getPromiseID());
            values.put("Start_Time", record.getStartTime());
            values.put("End_Time", record.getEndTime());
            values.put("Run_Time", record.getEndTime());
            values.put("CAT", record.getCat());

            Cursor cursor = db.query(table, null,  "PK = ?", new String[]{String.valueOf(record.getID())}, null, null, null);
            if (cursor.moveToFirst()) {
                // 존재하면 업데이트 수행
                int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(record.getID())});
                Log.d(table, result + "업데이트 성공");
                cursor.close();
            } else {
                // 존재하지 않으면 삽입 수행
                long result = db.insert(table, null, values);
                Log.d(table, result + "삽입 성공");
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public void UpdateOrInsert(ExerciseData exercise) {
        ContentValues values = new ContentValues();

        try {
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
        } finally {
            db.close();
        }
    }

    public void Update(int signal,int userPk,int chatPk,String ts) {
        ContentValues values = new ContentValues();
        if (table != null &&table.equals("CHAT_MSG_TB")) {
            values.put("MSG_PK",chatPk);
            values.put("SUCCESS", 1);
            values.put("TS",ts);
            int result = -1;
            try {
                result = db.update("CHAT_MSG_TB", values, "USER_FK = ? AND MSG_PK = ?", new String[]{String.valueOf(userPk),String.valueOf(signal)});
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

    public ArrayList<RoutineData> SelectAllRoutine() {
        ArrayList<RoutineData> routines = new ArrayList<>();

        try {
            if (table.equals("RT_TB")) {
                String sql = "SELECT * FROM " + table + ";";

                Log.d("SQLite SelectRoutine", sql);

                Cursor cursor = db.rawQuery(sql, null);


                while(cursor.moveToNext()) {
                    routines.add(new RoutineData(
                            cursor.getInt(0),    // ID
                            cursor.getInt(1),    // Time
                            cursor.getInt(2),    // cat
                            cursor.getInt(3)    // dayOfWeek
                    ));
                }
            } else {
                Log.d(table, " 잘못된 메서드 호출");
            }
        } finally {
            db.close();
            return routines;
        }
    }

    public ArrayList<RoutineData> SelectRoutine(int DayOfWeek) {
        ArrayList<RoutineData> routines = new ArrayList<>();

        try {
            if (table.equals("RT_TB")) {
                String sql = "SELECT * FROM " + table + " WHERE Day_Of_Week = " + DayOfWeek + ";";

                Log.d("SQLite SelectRoutine", sql);

                Cursor cursor = db.rawQuery(sql, null);


                while(cursor.moveToNext()) {
                    routines.add(new RoutineData(
                            cursor.getInt(0),    // ID
                            cursor.getInt(1),    // Time
                            cursor.getInt(2),    // cat
                            cursor.getInt(3)    // dayOfWeek
                    ));
                }
            } else {
                Log.d(table, " 잘못된 메서드 호출");
            }
        } finally {
            db.close();
            return routines;
        }
    }

    public ArrayList<RecordData> SelectRecord(String Date) {
        ArrayList<RecordData> records = new ArrayList<>();

        try {
            if (table.equals("RECORD_TB")) {
                String sql = "SELECT * FROM " + table + " WHERE TS LIKE '" + Date + "%';";

                Log.d("SQLite SelectRecord", sql);

                Cursor cursor = db.rawQuery(sql, null);


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
            } else {
                Log.d(table, " 잘못된 메서드 호출");
            }
        } finally {
            db.close();
            return records;
        }
    }

    public ArrayList<ExerciseData> SelectExercise(int RT_PK, boolean isRoutine) {
        ArrayList<ExerciseData> exercises = new ArrayList<>();

        try {
            if (table.equals("EX_TB")) {
                String sql = "SELECT * FROM " + table + " WHERE RT_FK = " + RT_PK + ";";

                Cursor cursor = db.rawQuery(sql, null);

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
            } else {
                Log.d(table, " 잘못된 메서드 호출");
            }
        } finally {
            db.close();
            return exercises;
        }
    }

    public ArrayList<BlackListData> SelectBlackUser() {
        try{
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
                            cursor.getString(4)        //User_Img
                    );
                    BlackListData.add(e);
                }
                return BlackListData;
            } else {
                Log.d(table, " 잘못된 메서드 호출");
                return null;
            }
        }finally {
            db.close();
        }

    }

    public ArrayList<ReviewListData> SelectEvaluation() {
        try{
            if (table.equals("REVIEW_TB")) {
                String sql = "SELECT Check_Box FROM REVIEW_TB"; // * 말고 Check_Box만 가져오기

                Cursor cursor = db.rawQuery(sql, null);

                ArrayList<ReviewListData> reviewListData = new ArrayList<>();

                while(cursor.moveToNext()) {
                    ReviewListData e = new ReviewListData( cursor.getInt(0) );    //Check_Box
                    reviewListData.add(e);
//                    Log.d("SQLite SelectEvaluation", "받은평가데이터: " + e.getCheck_Box());
                }
                return reviewListData;
            } else {
                Log.d(table, " 잘못된 메서드 호출");
                return null;
            }
        }finally {
            db.close();
        }

    }
  
    public ArrayList<ReviewListData> SelectReviewUser() {
        try{
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
                            cursor.getString(7)           //User_Img
                    );
                    reviewListData.add(e);
//                    Log.d("SQLite SelectReviewUser", "리뷰리스트데이터: " + e.getText_Con());
                }
                return reviewListData;
            } else {
                Log.d(table, " 잘못된 메서드 호출");
                return null;
            }
        }finally {
            db.close();
        }

    }
    public ArrayList<WittListData> SelectWittHistoryUser() {
        try {
            if (table.equals("Witt_History_TB")) {
                String sql = "SELECT * FROM Witt_History_TB ORDER BY TS DESC";//날짜별 내림차순으로 정렬하여 select

                Cursor cursor = db.rawQuery(sql, null);
                ArrayList<WittListData> wittListData = new ArrayList<>();

                while (cursor.moveToNext()) {
                    WittListData e = new WittListData(         // 순서 잘 지킬 것, 나중에 수정
                            cursor.getInt(0),           //RECORD_PK
                            cursor.getInt(1),           //
                            cursor.getInt(2),           //OUser_FK
                            cursor.getString(3),        // TS
                            cursor.getString(4),        //User_NM
                            cursor.getString(5)           //User_Img
                    );
                    wittListData.add(e);
//                    Log.d("SQLite SelectWittHis", "위트내역 리스트 데이터: " + e.getTS());
                }
                return wittListData;
            } else {
                Log.d(table, " 잘못된 메서드 호출");
                return null;
            }
        }
        finally {
            db.close();
        }

    }
    public int getMaxChatRoomPK(int userKey) {
        int maxChatRoomPK = -1;

        try (Cursor cursor = db.rawQuery("SELECT MAX(CHAT_ROOM_PK) AS MAX_PK FROM CHAT_ROOM_TB WHERE USER_FK = ?", new String[]{String.valueOf(userKey)})) {
            if (cursor != null && cursor.moveToFirst()) {
                maxChatRoomPK = cursor.getInt(cursor.getColumnIndexOrThrow("MAX_PK"));
            }
        } finally {
            db.close();
            return maxChatRoomPK;
        }

    }
    //TODO selectWittsended
    public boolean Wittsended(int PK) {
        String query = "SELECT OTHER_USER_FK FROM CHAT_ROOM_TB " +
                        "WHERE OTHER_USER_FK = ?";
        String[] selectionArgs = {String.valueOf(PK)};


        try(Cursor cursor = db.rawQuery(query, selectionArgs)){
            if(cursor != null && cursor.moveToFirst()) {
                Log.d(TAG,"상대 pk 있다");
                return true;
            }else{
                Log.d(TAG,"상대 pk 없다");
                return false;
            }
        }finally {
            db.close();
        }
    }


    public ArrayList<UserChat> selectChatRoom(Context context, String userKey) {
        ArrayList<UserChat> uc = new ArrayList<>();
        DBHelper dbHelper = null;
        SQLiteDatabase db = null;

        String query = "SELECT CHAT_ROOM_PK, OTHER_USER_FK, OTHER_USER_NM, TS, GYM_NM, ADR " +
                "FROM CHAT_ROOM_TB " +
                "WHERE USER_FK = ?";

        String[] selectionArgs = { userKey };

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();

            try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Log.d(TAG, "selectChatRoom: sss");
                        int CHAT_ROOM_PK = cursor.getInt(cursor.getColumnIndexOrThrow("CHAT_ROOM_PK"));
                        int OTHER_USER_FK = cursor.getInt(cursor.getColumnIndexOrThrow("OTHER_USER_FK"));
                        String OTHER_USER_NM = cursor.getString(cursor.getColumnIndexOrThrow("OTHER_USER_NM"));
                        String TS = cursor.getString(cursor.getColumnIndexOrThrow("TS"));
                        String GYM_NM = cursor.getString(cursor.getColumnIndexOrThrow("GYM_NM"));
                        String ADR = cursor.getString(cursor.getColumnIndexOrThrow("ADR"));
                        uc.add(new UserChat(OTHER_USER_NM, OTHER_USER_FK, CHAT_ROOM_PK, TS, GYM_NM, ADR, 1));
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }

        return uc;
    }

    public ArrayList<UserChat> selectChatRoom(String userKey) {
        ArrayList<UserChat> uc = new ArrayList<>();

        String query = "SELECT CHAT_ROOM_PK, OTHER_USER_FK, OTHER_USER_NM, TS, GYM_NM, ADR " +
                "FROM CHAT_ROOM_TB " +
                "WHERE USER_FK = ?";

        String[] selectionArgs = { userKey };

        try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Log.d(TAG, "selectChatRoom: sss");
                    int CHAT_ROOM_PK = cursor.getInt(cursor.getColumnIndexOrThrow("CHAT_ROOM_PK"));
                    int OTHER_USER_FK = cursor.getInt(cursor.getColumnIndexOrThrow("OTHER_USER_FK"));
                    String OTHER_USER_NM = cursor.getString(cursor.getColumnIndexOrThrow("OTHER_USER_NM"));
                    String TS = cursor.getString(cursor.getColumnIndexOrThrow("TS"));
                    String GYM_NM = cursor.getString(cursor.getColumnIndexOrThrow("GYM_NM"));
                    String ADR = cursor.getString(cursor.getColumnIndexOrThrow("ADR"));
                    uc.add(new UserChat(OTHER_USER_NM, OTHER_USER_FK, CHAT_ROOM_PK, TS, GYM_NM, ADR,1));
                } while (cursor.moveToNext());
            }
        } finally {
            db.close();
        }

        return uc;
    }


    public MSG SelectMSG(Context context, String userKey, int chatPk) {
        MSG m = null;
        DBHelper dbHelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();

                String sql = "SELECT MSG, CHAT_ROOM_FK, TS, MYFLAG, SUCCESS FROM CHAT_MSG_TB WHERE USER_FK = ? AND MSG_PK = ? ";
                String[] selectionArgs = {userKey, String.valueOf(chatPk)};
                cursor = db.rawQuery(sql, selectionArgs);

                if (cursor != null && cursor.moveToFirst()) {
                    int CHAT_ROOM_FK = -1;
                    int CHAT_ROOM_Index = cursor.getColumnIndex("CHAT_ROOM_FK");
                    String message = "값 없음";
                    String TS = "";
                    String mfl = "1";
                    int successIndex = cursor.getColumnIndex("SUCCESS");
                    int success = -1;
                    int MSGIndex = cursor.getColumnIndex("MSG");
                    int mf = cursor.getColumnIndex("MYFLAG");
                    int TSIndex = cursor.getColumnIndex("TS");

                    if (CHAT_ROOM_Index != -1) {
                        CHAT_ROOM_FK = cursor.getInt(CHAT_ROOM_Index);
                    }
                    if (MSGIndex != -1) {
                        message = cursor.getString(MSGIndex);
                        // 메시지 처리 로직 작성
                    } else {
                        // 컬럼이 존재하지 않을 경우 처리 로직 작성
                    }
                    if (successIndex != -1) {
                        success = cursor.getInt(successIndex);
                    }
                    if (TSIndex != -1) {
                        TS = cursor.getString(TSIndex);
                    }
                    if (mf != -1) {
                        mfl = cursor.getString(mf);
                    }
                    m = new MSG(chatPk, Integer.parseInt(mfl), CHAT_ROOM_FK, message, TS, success, 1);
                    Log.d(TAG, "SelectMSG: 값 얻어옴");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            return m;
        }
    }


    public List<MSG> selectAllMSG(String userKey, int chatRoomId,Context context) {
        List<MSG> messages = new ArrayList<>();

        SQLiteDatabase db = null;
        Cursor cursor = null;
        DBHelper dbHelper1 = new DBHelper(context, "Witt", null, 1);
        try {

            db = dbHelper1.getWritableDatabase();
            String[] columns = {"MSG_PK", "MSG", "CHAT_ROOM_FK", "TS", "MYFLAG", "SUCCESS"};
            String selection = "USER_FK=? AND CHAT_ROOM_FK=?";
            String[] selectionArgs = {userKey, String.valueOf(chatRoomId)};
            String orderBy = "TS, MSG_PK";

            cursor = db.query("CHAT_MSG_TB", columns, selection, selectionArgs, null, null, orderBy);

            while (cursor.moveToNext()) {
                int MSG_PK = cursor.getInt(cursor.getColumnIndexOrThrow("MSG_PK"));
                int mfl = cursor.getInt(cursor.getColumnIndexOrThrow("MYFLAG"));
                String message = cursor.getString(cursor.getColumnIndexOrThrow("MSG"));
                String TS = cursor.getString(cursor.getColumnIndexOrThrow("TS"));
                int success = cursor.getInt(cursor.getColumnIndexOrThrow("SUCCESS"));

                // CHAT_MSG_TB에서 MSG_PK로 검색된 컬럼의 ISREAD값을 2로 업데이트
                ContentValues values = new ContentValues();
                values.put("ISREAD", 2);
                int rowsAffected = db.update("CHAT_MSG_TB", values, "MSG_PK=?", new String[]{String.valueOf(MSG_PK)});
                if (rowsAffected > 0) {
                    Log.d("selectAllMSG", "MSG_PK: " + MSG_PK + " 업데이트 성공");
                } else {
                    Log.d("selectAllMSG", "MSG_PK: " + MSG_PK + " 업데이트 실패");
                }

                messages.add(new MSG(MSG_PK, mfl, chatRoomId, message, TS, success, 2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return messages;
    }


    public String selectOtherUserKey(Context context, String userKey, String chatRoomId) {
        String otherUserKey = null;
        DBHelper dbHelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();
                Log.d(TAG, "selectOtherUserKey: Executing query");
                String sql = "SELECT OTHER_USER_FK FROM CHAT_ROOM_TB WHERE USER_FK = ? AND CHAT_ROOM_PK = ?";
                String[] selectionArgs = {userKey, chatRoomId};
                cursor = db.rawQuery(sql, selectionArgs);

                if (cursor != null && cursor.moveToFirst()) {
                    int otherUserKeyIndex = cursor.getColumnIndex("OTHER_USER_FK");

                    if (otherUserKeyIndex != -1) {
                        otherUserKey = cursor.getString(otherUserKeyIndex);
                    }

                } else {
                    // cursor 객체가 null인 경우 로그 출력 후 메서드 종료
                    Log.d(TAG, "selectOtherUserKey: cursor is null");
                }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            return otherUserKey;
        }
    }

    // UserListAdapter.java (selectLastMsg 메서드 수정)
    public String selectOtherUserName(Context context, String userKey, String chatRoomId) {
        String otherUserName = null;
        DBHelper dbHelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();


                Log.d(TAG, "selectOtherUserName: Executing query");
                String sql = "SELECT OTHER_USER_NM FROM CHAT_ROOM_TB WHERE USER_FK = ? AND CHAT_ROOM_PK = ?";
                String[] selectionArgs = {userKey, chatRoomId};
                cursor = db.rawQuery(sql, selectionArgs);

                if (cursor != null && cursor.moveToFirst()) {
                    int otherUserNameIndex = cursor.getColumnIndex("OTHER_USER_NM");

                    if (otherUserNameIndex != -1) {
                        otherUserName = cursor.getString(otherUserNameIndex);
                    }

                } else {
                    // cursor 객체가 null인 경우 로그 출력 후 메서드 종료
                    Log.d(TAG, "selectOtherUserName: cursor is null");
                }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            return otherUserName;
        }
    }



    public MSG selectLastMsg(Context context, String chatRoomID, String userKey, int allLastFlag) {
        String msg = "";
        String TS = "-1";
        int MSG_PK = -1;
        int myFlag = -1;
        int isRead = -1;

        DBHelper dbHelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            dbHelper = new DBHelper(context, "Witt", null, 1);
            db = dbHelper.getWritableDatabase();

                Log.d(TAG, "selectLastMsgwwww:sssssaaaaa ");
                String sql = "";
                String value = "";
                if (allLastFlag == 1) { // all
                    sql = "SELECT MSG_PK, MSG, MYFLAG, TS, ISREAD FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ? AND SUCCESS = ? ORDER BY TS DESC, MSG_PK DESC LIMIT 1";
                    value = "1";
                } else if (allLastFlag == 2) { // other
                    sql = "SELECT MSG_PK, MSG, TS, ISREAD FROM CHAT_MSG_TB WHERE USER_FK = ? AND CHAT_ROOM_FK = ? AND MYFLAG = ? ORDER BY TS DESC, MSG_PK DESC LIMIT 1";
                    value = "2";
                }
                String[] selectionArgs = {userKey, chatRoomID, value};
                cursor = db.rawQuery(sql, selectionArgs);

                if (cursor != null && cursor.moveToFirst()) {
                    int isReadIndex = cursor.getColumnIndex("ISREAD");
                    int myFlagIndex = cursor.getColumnIndex("MYFLAG");
                    int MSGIndex = cursor.getColumnIndex("MSG");
                    int MSG_PK_Index = cursor.getColumnIndex("MSG_PK");
                    int TSIndex = cursor.getColumnIndex("TS");

                    if (MSG_PK_Index != -1) {
                        MSG_PK = cursor.getInt(MSG_PK_Index);
                        Log.d(TAG, "selectLastMsg: pkmsg" + MSG_PK);
                    }
                    if (myFlagIndex != -1) {
                        myFlag = cursor.getInt(myFlagIndex);
                    }
                    if (MSGIndex != -1) {
                        msg = cursor.getString(MSGIndex);
                    }
                    if (TSIndex != -1) {
                        TS = cursor.getString(TSIndex);
                    }
                    if (isReadIndex != -1) {
                        isRead = cursor.getInt(isReadIndex);
                    }
                } else {
                    // cursor 객체가 null인 경우 로그 출력 후 메서드 종료
                    Log.d(TAG, "cursor is null");
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            Log.d(TAG, "selectLastMsg: " + msg + "ssssss" + TS);
            if (allLastFlag == 1) {
                return new MSG(MSG_PK, myFlag, Integer.parseInt(chatRoomID), msg, TS, 1, isRead);
            } else {
                return new MSG(MSG_PK, 2, Integer.parseInt(chatRoomID), msg, TS, 1, isRead);
            }
        }
        // TS 값을 처리하는 로직 추가
    }


    public ArrayList<AlarmInfo> selectAlarms(String userKey) {
        ArrayList<AlarmInfo> alarmInfos = new ArrayList<>();

        try {
            if (table != null && table.equals("NOTIFY_TB")) {
                String sql = "SELECT NOTIFY_PK, OTHER_USER_FK, NOTIFY_CATEGORY, LINK_PK, CREATED_TIME, READ_TIME, OUSER_NM FROM NOTIFY_TB WHERE USER_FK = ?";
                String[] selectionArgs = {userKey};
                Cursor cursor = db.rawQuery(sql, selectionArgs);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int notifyKey = cursor.getInt(cursor.getColumnIndexOrThrow("NOTIFY_PK"));
                        int otherUserKey = cursor.getInt(cursor.getColumnIndexOrThrow("OTHER_USER_FK"));
                        int link = cursor.getInt(cursor.getColumnIndexOrThrow("LINK_PK"));
                        int cat = cursor.getInt(cursor.getColumnIndexOrThrow("NOTIFY_CATEGORY"));
                        String readTime = cursor.getString(cursor.getColumnIndexOrThrow("READ_TIME"));
                        String createTime = cursor.getString(cursor.getColumnIndexOrThrow("CREATED_TIME"));
                        String otherUserName = cursor.getString(cursor.getColumnIndexOrThrow("OUSER_NM"));

                        // Create the AlarmInfo object and add it to the list
                        AlarmInfo alarmInfo = new AlarmInfo(notifyKey, Integer.parseInt(userKey), otherUserKey, link, cat, readTime, createTime, otherUserName);
                        alarmInfos.add(alarmInfo);

                        // Check if the alarm has been read (based on your condition to determine if it's read)
                        // If it's read, update the READ_TIME in the database
                        if (notifyKey != -1 && !readTime.equals("READ")) {
                            ContentValues values = new ContentValues();
                            values.put("READ_TIME", "READ");
                            db.update("NOTIFY_TB", values, "NOTIFY_PK = ?", new String[]{String.valueOf(notifyKey)});
                        }
                    }

                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            return alarmInfos;
        }

    }

    public int SelectLastAlarm(String userKey){// 테스팅 안함
        int lastKey = -1;
        if (table != null && table.equals("NOTIFY_TB")) {
            String sql = "SELECT MAX(NOTIFY_PK) FROM NOTIFY_TB WHERE USER_FK = ?";
            String[] selectionArgs = {userKey};
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    lastKey = cursor.getInt(0);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        }
        else{
            Log.d(TAG, "SelectLastAlarm: 알람sql오류");
        }
        return lastKey;
    }

    public void DropUser(Context context) {
        try {
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
        }finally {
            db.close();
        }

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
