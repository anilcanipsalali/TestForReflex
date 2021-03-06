package com.schnellmet.testforreflex.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.schnellmet.testforreflex.classes.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TestForReflexQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTIONA + " TEXT, " +
                QuestionsTable.COLUMN_OPTIONB + " TEXT, " +
                QuestionsTable.COLUMN_OPTIONC + " TEXT, " +
                QuestionsTable.COLUMN_OPTIOND + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("85-23 = ?", "61", "62", "63", "64", 2);
        addQuestion(q1);
        Question q2 = new Question("210+110 = ?", "320", "330", "310", "300", 1);
        addQuestion(q2);
        Question q3 = new Question("√(12) = ?", "3.4425463234", "3.45358334452", "3.46410161514", "3.473486835", 3);
        addQuestion(q3);
        Question q4 = new Question("86/4 = ?", "22.5", "24", "23.5", "21.5", 4);
        addQuestion(q4);
        Question q5 = new Question("((94-120) x 12 ) - 29 = ?", "-341", "-340", "-331", "-321", 1);
        addQuestion(q5);
        Question q6 = new Question("12  - 29 = ?", "-17", "17", "19", "-19", 1);
        addQuestion(q6);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTIONA, question.getOptionA());
        cv.put(QuestionsTable.COLUMN_OPTIONB, question.getOptionB());
        cv.put(QuestionsTable.COLUMN_OPTIONC, question.getOptionC());
        cv.put(QuestionsTable.COLUMN_OPTIOND, question.getOptionD());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOptionA(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTIONA)));
                question.setOptionB(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTIONB)));
                question.setOptionC(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTIONC)));
                question.setOptionD(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTIOND)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
