package com.example.attendenceapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    //class table
    private static final String DEPARTMENT_TABLE_NAME = "DEPARTMENT_TABLE";
    public static final String D_ID = "_DID";
    public static final String DEPARTMENT_NAME_KEY = "DEPARTMENT_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";
    private static final String CREATE_DEPARTMENT_TABLE =
            "CREATE TABLE " + DEPARTMENT_TABLE_NAME + "( " +
                    D_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    DEPARTMENT_NAME_KEY + " TEXT NOT NULL," +
                    SUBJECT_NAME_KEY + " TEXT NOT NULL," +
                    "UNIQUE (" + DEPARTMENT_NAME_KEY + "," + SUBJECT_NAME_KEY + ")" +
                    ");";
    private static final String DROP_DEPARTMENT_TABLE = " DROP TABLE IF EXISTS " + DEPARTMENT_TABLE_NAME;
    private static final String SELECT_DEPARTMENT_TABLE = " SELECT * FROM " + DEPARTMENT_TABLE_NAME;
    //student table
    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String S_ID = "_SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL";

    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "( " +
                    S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    D_ID + " INTEGER NOT NULL, " +
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    " FOREIGN KEY ( " + D_ID + " ) REFERENCES " + DEPARTMENT_TABLE_NAME + "(" + D_ID + ")" +
                    ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM " + STUDENT_TABLE_NAME;
    //status table
    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";

    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME +
                    "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY NOT NULL," +
                    S_ID + " INTEGER NOT NULL, " +
                    D_ID + " INTEGER NOT NULL, " +
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    " UNIQUE (" + S_ID + "," + DATE_KEY + ")," +
                    " FOREIGN KEY (" + S_ID + ") REFERENCES " + STUDENT_TABLE_NAME + "(" + S_ID + ")," +
                    " FOREIGN KEY (" + D_ID + ") REFERENCES " + DEPARTMENT_TABLE_NAME + "(" + D_ID + ")" +
                    ");";

    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM " + STATUS_TABLE_NAME;


     public DbHelper(@Nullable Context context) {

         super(context, "Attendence.db", null, VERSION);
     }

     @Override
     public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_DEPARTMENT_TABLE);
         db.execSQL(CREATE_STUDENT_TABLE);
         db.execSQL(CREATE_STATUS_TABLE);

     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         try {
             db.execSQL(DROP_DEPARTMENT_TABLE);
             db.execSQL(DROP_STUDENT_TABLE);
             db.execSQL(DROP_STATUS_TABLE);
         }catch (SQLException e) {
             e.printStackTrace();
         }
     }

     long addClass(String departmentName,String subjectName) {
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(DEPARTMENT_NAME_KEY,departmentName);
         values.put(SUBJECT_NAME_KEY,subjectName);

          return database.insert(DEPARTMENT_TABLE_NAME,null,values);
     }
     Cursor getClassTable() {
         SQLiteDatabase database = this.getReadableDatabase();

         return database.rawQuery(SELECT_DEPARTMENT_TABLE, null);
     }

     int deleteClass(long did) {
         SQLiteDatabase database = this.getReadableDatabase();
         return database.delete(DEPARTMENT_TABLE_NAME, D_ID + "=?",new String[]{String.valueOf(did)});
     }

     long updateClass(long did, String departmentName, String subjectName) {
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(DEPARTMENT_NAME_KEY, departmentName);
         values.put(SUBJECT_NAME_KEY, subjectName);

         return database.update(DEPARTMENT_TABLE_NAME, values, D_ID + "=?", new String[]{String.valueOf(did)});

     }

     long addStudent(long did, int roll, String name) {
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(D_ID, did);
         values.put(STUDENT_ROLL_KEY, roll);
         values.put(STUDENT_NAME_KEY, name);

         return database.insert(STUDENT_TABLE_NAME, null, values);

     }

     Cursor getStudentTable(long did) {
         SQLiteDatabase database = this.getReadableDatabase();

         return database.query(STUDENT_TABLE_NAME, null, D_ID + "=?", new String[]{String.valueOf(did)}, null, null, STUDENT_ROLL_KEY);
     }
     int deleteStudent(long sid){
         SQLiteDatabase database= this.getReadableDatabase();
         return database.delete(STUDENT_TABLE_NAME,S_ID+"=?",new String[]{String.valueOf(sid)});
     }
     long updateStudent(long sid, String name) {
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(STUDENT_NAME_KEY, name);

         return database.update(STUDENT_TABLE_NAME, values, S_ID + "=?", new String[]{String.valueOf(sid)});

     }
     long addStatus(long sid,long did,String date,String status){
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(S_ID,sid);
         values.put(D_ID,did);
         values.put(DATE_KEY,date);
         values.put(STATUS_KEY,status);
         return database.insert(STATUS_TABLE_NAME,null,values);
     }
     long updateStatus(long sid,String date,String status){
         SQLiteDatabase database = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(STATUS_KEY,status);
         String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
         final int update = database.update(STATUS_TABLE_NAME, values, whereClause, null);
         return update;
     }

     @SuppressLint("Range")
     String getStatus(long sid, String date){
         String status=null;
         SQLiteDatabase database = this.getReadableDatabase();
         String whereClause = DATE_KEY +"='"+date+"' AND "+S_ID+"="+sid;
         Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
         if(cursor.moveToFirst())
             status = cursor.getString(cursor.getColumnIndex(STATUS_KEY));
         return status;

     }
     Cursor getDistinctMonths(long did){
         SQLiteDatabase database = this.getReadableDatabase();
         return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},D_ID+"="+did,null,"substr("+DATE_KEY+",4,7)",null,null);//01.04.2020
     }
 }

