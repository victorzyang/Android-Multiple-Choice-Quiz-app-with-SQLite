package com.example.androidsqlitesetup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //helper variables
    public static final String DATABASE_NAME = "Test.db";
    //tables
    public static final String QUESTIONS_TABLE = "Questions"; //questions table
    public static final String ANSWER_KEYS_TABLE = "Answer_Keys";
    public static final String SUBMISSION_INSTRUCTION_TABLE = "Submission_Instruction";
    public static final String TEST_TABLE = "Test";
    public static final String STUDENT_INFO_TABLE = "Student_Info";
    //column ids
    public static final String QNUM_TABLE_COLUMN_ID = "Question_Number";
    public static final String EMAIL_TABLE_COLUMN_ID = "Email";
    //column names
    public static final String Q_DESC_TABLE_COL = "Description";
    public static final String A_TABLE_COL = "A";
    public static final String B_TABLE_COL = "B";
    public static final String C_TABLE_COL = "C";
    public static final String D_TABLE_COL = "D";
    public static final String E_TABLE_COL = "E";
    public static final String ANSWER_TABLE_COL = "Answer";
    public static final String NAME_TABLE_COL = "Name";
    public static final String STUDENT_NUM_TABLE_COL = "Student_Number";

    public DBHelper(Context context){
        super(context, DATABASE_NAME , null, 1);
    }

    //Use the above constructor instead
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //I think the database gets initialized here
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //required method to implement / creates all the tables in database
        //Can I create multiple tables inside here? -Yes
        Log.i("DBHelper", "Creating the 5 tables in database");
        db.execSQL("create table " + QUESTIONS_TABLE + " (" + QNUM_TABLE_COLUMN_ID + " integer primary key autoincrement, " + Q_DESC_TABLE_COL + " text, " + A_TABLE_COL + " text, " + B_TABLE_COL + " text, " + C_TABLE_COL + " text, " + D_TABLE_COL + " text, " + E_TABLE_COL + " text)");
        db.execSQL("create table " + ANSWER_KEYS_TABLE + " (" + QNUM_TABLE_COLUMN_ID + " integer, " + ANSWER_TABLE_COL + " text)");
        db.execSQL("create table " + SUBMISSION_INSTRUCTION_TABLE + " (" + EMAIL_TABLE_COLUMN_ID + " text)");
        db.execSQL("create table " + TEST_TABLE + " (" + QNUM_TABLE_COLUMN_ID + " integer primary key autoincrement, " + ANSWER_TABLE_COL + " text)");
        db.execSQL("create table " + STUDENT_INFO_TABLE + " (" + NAME_TABLE_COL + " text, " + EMAIL_TABLE_COLUMN_ID + " text, " + STUDENT_NUM_TABLE_COL + " integer)");

        //db = db.openOrCreateDatabase("database", Context.MODE_PRIVATE, null); //There's an error here

        //db = DBHelper.this.openOrCreateDatabase("database", Context.MODE_PRIVATE, null); //I don't think I call openOrCreateDatabase() here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //required method to implement
        Log.i("DBHelper", "Upgrading all tables in database");
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ANSWER_KEYS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SUBMISSION_INSTRUCTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_INFO_TABLE);
        onCreate(db);
    }

    //inserts a new question to the Questions table
    public boolean insertQuestionData(String q_description, String[] options){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("DBHelper", "Inserting a new question to the Questions table");

        //Create ContentValues
        ContentValues values = new ContentValues();
        values.put(Q_DESC_TABLE_COL, q_description); //puts data into table column
        values.put(A_TABLE_COL, options[0]); //Option A
        values.put(B_TABLE_COL, options[1]); //Option B
        values.put(C_TABLE_COL, options[2]); //Option C
        values.put(D_TABLE_COL, options[3]); //Option D
        values.put(E_TABLE_COL, options[4]); //Option E
        long result = db.insert(QUESTIONS_TABLE, null, values); //insert() returns -1 if values are NOT inserted

        if(result == -1){
            return false; //this determines if data does NOT get inserted
        }
        return true; //this determines if data gets inserted
    }

    //May not need to use this insertQuestionData() method below
    public boolean insertQuestionData(String q_description, String option_a, String option_b,
                                  String option_c, String option_d, String option_e){
        SQLiteDatabase db = this.getWritableDatabase();

        //Create ContentValues
        ContentValues values = new ContentValues();
        values.put(Q_DESC_TABLE_COL, q_description); //puts data into table column
        values.put(A_TABLE_COL, option_a);
        values.put(B_TABLE_COL, option_b);
        values.put(C_TABLE_COL, option_c);
        values.put(D_TABLE_COL, option_d);
        values.put(E_TABLE_COL, option_e);
        db.insert(QUESTIONS_TABLE, null, values);

        return true; //this determines if data gets inserted
    }

    //inserts a new answer to the AnswerKey table
    public boolean insertAnswerKeyData(int q_num, String answer){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("DBHelper", "Inserting a new answer to the Answer Keys table");

        ContentValues values = new ContentValues();
        values.put(QNUM_TABLE_COLUMN_ID, q_num);
        values.put(ANSWER_TABLE_COL, answer);
        long result = db.insert(ANSWER_KEYS_TABLE, null, values);

        if(result == -1){
            return false;
        }

        return true;
    }

    //insert student's answer to the Test table
    public boolean insertTestData(String student_answer){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("DBHelper", "Inserting a new student answer to the Test table");

        ContentValues values = new ContentValues();
        values.put(ANSWER_TABLE_COL, student_answer);
        long result = db.insert(TEST_TABLE, null, values);

        if(result == -1){
            return false;
        }

        return true;
    }

    //insert student information to both the Submission Instruction table and Student Info table
    public boolean insertStudentData(String email, String name, int student_num){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("DBHelper", "Inserting a student email to Submission Instruction table");

        //Insert student email to the Submission Instruction table
        ContentValues values1 = new ContentValues(); //Create ContentValues
        values1.put(EMAIL_TABLE_COLUMN_ID, email);
        long result1 = db.insert(SUBMISSION_INSTRUCTION_TABLE, null, values1);

        Log.i("DBHelper", "Inserting student information to the Student Info table");

        //Inserts all student information to the Student Info table
        ContentValues values2 = new ContentValues(); //Create ContentValues
        values2.put(NAME_TABLE_COL, name);
        values2.put(EMAIL_TABLE_COLUMN_ID, email);
        values2.put(STUDENT_NUM_TABLE_COL, student_num);
        long result2 = db.insert(STUDENT_INFO_TABLE, null, values2);

        if (result1 == -1 || result2 == -1){
            return false;
        }

        return true;
    }

    //How do I get data from multiple tables? By having multiple get methods? -Yes
    public Cursor getQuestionsData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("DBHelper", "Select all questions in the Question table");
        Cursor res = db.rawQuery("select * from " + QUESTIONS_TABLE, null);
        return res;
    }

    public Cursor getAnswerKeysData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("DBHelper", "Select all answers in the Answer Key table");
        Cursor res = db.rawQuery("select * from " + ANSWER_KEYS_TABLE, null);
        return res;
    }

    public Cursor getSubmissionData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("DBHelper", "Select all student emails in the Submission Instruction table");
        Cursor res = db.rawQuery("select * from " + SUBMISSION_INSTRUCTION_TABLE, null);
        return res;
    }

    public Cursor getTestData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("DBHelper", "Select all results in the Test table");
        Cursor res = db.rawQuery("select * from " + TEST_TABLE, null);
        return res;
    }

    public Cursor getStudentInfoData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("DBHelper", "Select all student info in the Student Info table");
        Cursor res = db.rawQuery("select * from " + STUDENT_INFO_TABLE, null);
        return res;
    }

    //method for deleting data from the Answer Keys table
    public Integer deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DBHelper", "Deleting all data in the Answer Keys table");
        return db.delete(ANSWER_KEYS_TABLE, null, null);
        //db.delete(SUBMISSION_INSTRUCTION_TABLE, null, null);
        //db.delete(STUDENT_INFO_TABLE, null, null);
    }

    //Can create a boolean method that determines if database exists...
}
