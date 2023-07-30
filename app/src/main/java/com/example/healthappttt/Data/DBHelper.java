package com.example.healthappttt.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String routineSql = "CREATE TABLE if not exists RT_TB ("
                + "PK INT primary key,"
                + "Time TYNYINT,"
                + "CAT SMALLINT,"
                + "Day_Of_Week TYNYINT);";

        db.execSQL(routineSql);

        String recordSql = "CREATE TABLE if not exists RECORD_TB ("
                + "PK INT primary key,"
                + "OUser_FK INT,"
                + "PROMISE_FK INT,"
                + "Start_Time TIME,"
                + "End_Time TIME,"
                + "Run_Time TIME,"
                + "CAT SMALLINT,"
                + "TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
//                + "FOREIGN KEY (PROMISE_FK) REFERENCES PROMISE_TB(PK));";

        db.execSQL(recordSql);

        String exerciseSql = "CREATE TABLE if not exists EX_TB ("
                + "PK INT primary key,"
                + "RT_FK INT,"
                + "RECORD_FK INT,"
                + "Ex_NM VARCHAR(20),"
                + "Set_Or_Time SMALLINT,"
                + "Volume SMALLINT,"
                + "Cnt_Or_Dis MEDIUMINT,"
                + "Sort_Index TINYINT,"
                + "CAT INT,"
                + "FOREIGN KEY (RT_FK) REFERENCES RT_TB(PK)"
                + "ON UPDATE CASCADE ON DELETE CASCADE,"
                + "FOREIGN KEY (RECORD_FK) REFERENCES RECORD_TB(PK)"
                + "ON UPDATE CASCADE ON DELETE CASCADE);";

        db.execSQL(exerciseSql);

        String blacklistsql = "CREATE TABLE if not exists BLACK_LIST_TB("
                + "BL_PK INT primary key,"
                + "User_NM VARCHAR(20),"
                + "OUser_FK INT,"
                + "TS TIMESTAMP,"
                + "User_Img BLOB);";

        db.execSQL(blacklistsql);

        String reviewrecdsql = "CREATE TABLE if not exists REVIEW_TB("
                + "Review_PK INT primary key,"
                + "User_FK INT,"
                + "RPT_User_FK INT,"
                + "Text_Con VARCHAR(30),"
                + "Check_Box SMALLINT,"
                + "TS TIMESTAMP,"
                + "User_NM VARCHAR(20),"
                + "User_Img BLOB);";

        db.execSQL(reviewrecdsql);

        String WittRecordsql = "Create Table if not exists Witt_History_TB("
                + "RECORD_PK INT primary key,"
                + "USER_FK INT,"
                + "OUser_FK INT,"
                + "TS TIMESTAMP,"
                + "User_NM VARCHAR(20),"
                + "User_Img BLOB);";

        db.execSQL(WittRecordsql);

        String createTableQuery = "CREATE TABLE if not exists CHAT_MSG_TB ("
                + "MSG_PK INTEGER,"
                + "USER_FK INT,"
                + "MYFLAG INT ,"
                + "CHAT_ROOM_FK INT, "
                + "MSG  VARCHAR(1500), "
                + "TS  VARCHAR(30),"
                + "SUCCESS INT,"
                + "ISREAD INT DEFAULT 1"
                + ");";
        db.execSQL(createTableQuery);

        String createChat_Room_TableQuery = "CREATE TABLE if not exists CHAT_ROOM_TB ("
                + "LOC_CHAT_ROOM_PK INTEGER,"
                + "USER_FK INT,"
                + "CHAT_ROOM_PK INT,"
                + "LAST_MSG_INDEX INT,"
                + "OTHER_USER_FK INT,"
                + "OTHER_USER_NM VARCHAR(30),"
                + "FAV INT ,"
                + "GYM_NM VARCHAR(30),"
                + "ADR VARCHAR(100),"
                + "TS  VARCHAR(30),"
                + "ISCLOSE INT DEFAULT 1"//열린상태 2는 닫힌상태
                + ");";
        db.execSQL(createChat_Room_TableQuery);

        String createNotifyTableQuery = "CREATE TABLE if not exists NOTIFY_TB ("
                + "LOC_NTF_PK INTEGER,"
                + "NOTIFY_PK INT,"
                + "USER_FK INT,"
                + "OTHER_USER_FK INTEGER DEFAULT -1,"
                + "OUSER_NM VARCHAR(30),"
                + "NOTIFY_CATEGORY INT,"
                + "LINK_PK INTE,"  // 넘겨줄 값 페이지 여는 키
                + "CREATED_TIME VARCHAR(30),"
                + "READ_TIME VARCHAR(30)"
                + ");";
        db.execSQL(createNotifyTableQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String routineSql = "DROP TABLE if exists RT_TB";
        db.execSQL(routineSql);

        String recordSql = "DROP TABLE if exists RECORD_TB";
        db.execSQL(recordSql);

        String exerciseSql = "DROP TABLE if exists EX_TB";
        db.execSQL(exerciseSql);

        String createTableQuery = "DROP TABLE if exists CHAT_MSG_TB";
        db.execSQL(createTableQuery);

        String NOTIFY_TBQuery = "DROP TABLE if exists NOTIFY_TB";
        db.execSQL(NOTIFY_TBQuery);
        onCreate(db);

        String CHAT_ROOM_TBQuery = "DROP TABLE if exists CHAT_ROOM_TB";
        db.execSQL(CHAT_ROOM_TBQuery);
        onCreate(db);
    }
}
