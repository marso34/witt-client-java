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
                + "Start_Time INT,"
                + "End_Time TIME,"
                + "CAT SMALLINT,"
                + "Day_Of_Week TYNYINT);";

        db.execSQL(routineSql);

        String recordSql = "CREATE TABLE if not exists RECORD_TB ("
                + "PK INT primary key,"
                + "OUser_FK INT,"
                + "Start_Time INT,"
                + "End_Time TIME,"
                + "Run_Time TIME,"
                + "CAT SMALLINT,"
                + "PROMISE_FK INT,"
                + "TS TIMESTAMP);";
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

        String reviewrecdsql = "CREATE TABLE if not exists RREVIEW_TB("
                + "Review_PK INT primary key,"
                + "User_FK INT,"
                + "RPT_User_FK INT,"
                + "Text_Con VARCHAR(30),"
                + "Check_Box SMALLINT,"
                + "TS TIMESTAMP);";

        db.execSQL(reviewrecdsql);

        String createTableQuery = "CREATE TABLE if not exists CHAT_MSG_TB ("
                +"MSG_PK INTEGER primary key,"
                + "myFlag INT ,"
                + "CHAT_ROOM_FK INT, "
                + "MSG  VARCHAR(1500), "
                + "TS  VARCHAR(30)"
                + ");";
        db.execSQL(createTableQuery);
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
        onCreate(db);
    }
}
