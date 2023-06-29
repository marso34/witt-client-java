package com.example.healthappttt.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

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
    public void insert(Routine routine) {
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
    }

    /**
     * 이 메소드는 RECORD_TB 데이터를 삽입하는 메서드입니다.
     */
    public void insert(Routine routine, int OUser_FK, int PROMISE_FK, String TS) {
        ContentValues values = new ContentValues();

        if (table.equals("RECORD_TB")) {
            values.put("PK", routine.getID());
            values.put("OUser_FK", OUser_FK);
            values.put("Start_Time", routine.getStartTime());
            values.put("End_Time", routine.getEndTime());
            values.put("CAT", routine.getCat());
            values.put("PROMISE_FK", PROMISE_FK);
            values.put("TS", TS);

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
    }

    /**
     * 이 메소드는 EX_TB에 데이터를 삽입하는 메서드입니다.
     */
    public void insert(Exercise exercise) {
        ContentValues values = new ContentValues();

        if (table.equals("EX_TB")) {
            values.put("PK", exercise.getID());
            values.put("RT_FK", exercise.getParentID());
            values.put("Ex_NM", exercise.getTitle());
            values.put("Set_Or_Time", exercise.getCount());
            values.put("Volume", exercise.getVolume());
            values.put("Cnt_Or_Dis", exercise.getNum());
            values.put("Sort_Index", exercise.getIndex());
            values.put("CAT", exercise.getCat());

            Log.d("운동 세부 정보", "" +
                    " pk " + exercise.getID() +
                    " 부모 " + exercise.getParentID() +
                    " 이름" + exercise.getTitle() +
                    " 세트 " + exercise.getCount() +
                    " 무게 " + exercise.getVolume() +
                    " 횟수 " + exercise.getNum() +
                    " 순서 " + exercise.getIndex() +
                    " 부위 " + exercise.getCat()
            );

            long result = db.insert(table, null, values);
            Log.d(table, result + "성공");
        } else {
            Log.d(table, "메서드 형식 오류");
        }
    }

    public void delete(int PK) {
        String selection = "PK = ?";
        int result = db.delete(table, selection, new String[]{String.valueOf(PK)});
        Log.i(table, +result + "개 row delete 성공");
    }

    //로컬 db에서 해당 pk를 가진 행을 삭제하는 매서드
    public void deleteFromBlackListTable(int pk) {
        db.execSQL("DELETE FROM BLACK_LIST_TB WHERE BL_PK =" + pk);
    }


    public void Update(Routine routine) {
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
    }

    public void Update(int RECORD_PK, int OUser_FK, int Start_Time, String End_Time, String Run_Time, int CAT, int PROMISE_FK, String TS) {
        ContentValues values = new ContentValues();

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
    } // 운동 기록을 수정할 수 있게 할지는 아직 모름

    public void Update(Exercise exercise) {
        ContentValues values = new ContentValues();

        if (table.equals("EX_TB")) {
            values.put("Set_Or_Time", exercise.getCount());
            values.put("Volume", exercise.getVolume());
            values.put("Cnt_Or_Dis", exercise.getNum());
            values.put("Sort_Index", exercise.getIndex());

            int result = db.update(table, values, "PK = ?", new String[]{String.valueOf(exercise.getID())});
            Log.d(table, result + "성공");
        } else {
            Log.d(table, " 메서드 형식 오류");
        }
    }

    public ArrayList<Routine> SelectRoutine(int DayOfWeek) {
        if (table.equals("RT_TB")) {
            String sql = "SELECT * FROM " + table + " WHERE Day_Of_Week = " + DayOfWeek + ";";

            Log.d("SQLite SelectRoutine", sql);

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<Routine> routines = new ArrayList<>();

            while(cursor.moveToNext()) {
                routines.add(new Routine(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));
            }

            return routines;
        } else {
            Log.d(table, " 잘못된 메서드 호출");
        }

        return null;
    }

    public ArrayList<Exercise> SelectExercise(int RT_PK) {
        if (table.equals("EX_TB")) {
            String sql = "SELECT * FROM " + table + " WHERE RT_FK = " + RT_PK + ";";

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<Exercise> exercises = new ArrayList<>();

            while(cursor.moveToNext()) {
                Exercise e = new Exercise( // 순서 잘 지킬 것, 나중에 수정
                        cursor.getInt(0),   // PK,          ID
                        cursor.getInt(1),   // RT FK,       parentID,
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
        return null;
    }
    public ArrayList<ReviewListData> SelectReviewUser() {
        if (table.equals("REVIEW_TB")) {
            String sql = "SELECT * FROM REVIEW_TB";

            Cursor cursor = db.rawQuery(sql, null);

            ArrayList<ReviewListData> reviewListData = new ArrayList<>();//reviewListData r 대문자에서 소문자로 바꿈

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
                        cursor.getInt(1),           //User_FK
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
        return null;
    }


}
