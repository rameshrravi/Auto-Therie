package com.auto.autotherieneu;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "AutoTheoryDB.db";
    public static final String TABLE_NAME = "AnswersList";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CATEGORY ="Category";
    public static final String COLUMN_CATEGORY_ID ="CategoryID";
    public static final String COLUMN_CATEGORY_TYPE = "CategoryType";
    public static final String COLUMN_BLOCK_ID = "BlockID";
    public static final String COLUMN_QUESTION_ID ="QuestionID";
    public static final String COLUMN_CORRECT_ANSWER ="CorrectAnswer";
    public static final String COLUMN_USER_ANSWER="UserAnswer";
    public static final String COLUMN_TOTAL_NUMBER_OF_QUESTIONS="TotalQuestions";

    QuestionsModel questionsModel;

    public MyDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +TABLE_NAME+
                        "("+COLUMN_ID+ " integer primary key,"+COLUMN_CATEGORY+" text,"+COLUMN_CATEGORY_ID+" text,"+ COLUMN_CATEGORY_TYPE +" text,"+ COLUMN_BLOCK_ID +" text,"+ COLUMN_QUESTION_ID +" text,"+ COLUMN_CORRECT_ANSWER +" text,"+COLUMN_USER_ANSWER+" text,"+COLUMN_TOTAL_NUMBER_OF_QUESTIONS+" text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addAnswer(QuestionsModel questionsModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, questionsModel.getCategory());
        contentValues.put(COLUMN_CATEGORY_ID, questionsModel.getCategoryID());
        contentValues.put(COLUMN_CATEGORY_TYPE, questionsModel.getCategoryType());
        contentValues.put(COLUMN_BLOCK_ID, questionsModel.getBlockID());
        contentValues.put(COLUMN_QUESTION_ID, questionsModel.getQuestionID());
        contentValues.put(COLUMN_CORRECT_ANSWER, questionsModel.getCorrectAnswer());
        contentValues.put(COLUMN_USER_ANSWER,questionsModel.getUserAnswer());
        contentValues.put(COLUMN_TOTAL_NUMBER_OF_QUESTIONS,questionsModel.getTotalNoOfQuestions());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }
    public boolean updateAnswer(QuestionsModel questionsModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, questionsModel.getCategory());
        contentValues.put(COLUMN_CATEGORY_ID, questionsModel.getCategoryID());
        contentValues.put(COLUMN_CATEGORY_TYPE, questionsModel.getCategoryType());
        contentValues.put(COLUMN_BLOCK_ID, questionsModel.getBlockID());
        contentValues.put(COLUMN_QUESTION_ID, questionsModel.getQuestionID());
        contentValues.put(COLUMN_CORRECT_ANSWER, questionsModel.getCorrectAnswer());
        contentValues.put(COLUMN_USER_ANSWER,questionsModel.getUserAnswer());
        contentValues.put(COLUMN_TOTAL_NUMBER_OF_QUESTIONS,questionsModel.getTotalNoOfQuestions());
        db.update(TABLE_NAME, contentValues, COLUMN_BLOCK_ID +" = ? ", new String[] {questionsModel.getBlockID() } );
        db.close();
        return true;
    }

    @SuppressLint("Range")
    public List<QuestionsModel> getAllEntries(String category) {
        List<QuestionsModel> entryTableDetailsList = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+COLUMN_CATEGORY_ID+"= "+category, null );
        //  res.moveToFirst();

        while(res.moveToNext()){
            this.questionsModel =new QuestionsModel();

            this.questionsModel.setCategory(res.getString(res.getColumnIndex(COLUMN_CATEGORY)));
            this.questionsModel.setCategoryID(res.getString(res.getColumnIndex(COLUMN_CATEGORY_ID)));
            this.questionsModel.setCategoryType(res.getString(res.getColumnIndex(COLUMN_CATEGORY_TYPE)));
            this.questionsModel.setBlockID(res.getString(res.getColumnIndex(COLUMN_BLOCK_ID)));
            this.questionsModel.setQuestionID(res.getString(res.getColumnIndex(COLUMN_QUESTION_ID)));
            this.questionsModel.setCorrectAnswer(res.getString(res.getColumnIndex(COLUMN_CORRECT_ANSWER)));
            this.questionsModel.setUserAnswer(res.getString(res.getColumnIndex(COLUMN_USER_ANSWER)));
            this.questionsModel.setTotalNoOfQuestions(res.getString(res.getColumnIndex(COLUMN_TOTAL_NUMBER_OF_QUESTIONS)));


            entryTableDetailsList.add(this.questionsModel);
            // res.moveToNext();
        }
        res.close();
        db.close();
        return entryTableDetailsList;
    }

    public Integer deleteEntry (String id,String type,String blockID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_CATEGORY_ID +"= ? AND "+COLUMN_CATEGORY_TYPE+"=? AND "+COLUMN_BLOCK_ID+"=?",
                new String[] { id ,type,blockID});
    }

    public Cursor getData(String id,String type,String blockID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+ COLUMN_CATEGORY_ID +"='"+id+"' AND "+ COLUMN_CATEGORY_TYPE +"='"+type+"' AND "+ COLUMN_BLOCK_ID +"='"+blockID+"'", null );
        return res;
    }
    public Cursor getData(String id,String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+ COLUMN_CATEGORY_ID +"='"+id+"' AND "+ COLUMN_CATEGORY_TYPE +"='"+type+"' ", null );
        return res;
    }
}
