package com.schnellmet.testforreflex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.schnellmet.testforreflex.R;
import com.schnellmet.testforreflex.classes.Question;
import com.schnellmet.testforreflex.classes.QuizDBHelper;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private TextView textViewQuestion, textViewQuestionCount;
    private AppCompatButton optionA, optionB, optionC, optionD;
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private long backPressedTime;
    private int correctAnswer= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        init();
        clickListener();
        startChronometer(chronometer);
    }

    private void init() {
        textViewQuestion = findViewById(R.id.textViewQuestion);
        chronometer = findViewById(R.id.textViewCountdown);
        textViewQuestionCount = findViewById(R.id.textViewQuestionCount);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);


        QuizDBHelper dbHelper = new QuizDBHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);
        showNextQuestion();
    }

    private void clickListener() {
        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(1==currentQuestion.getAnswerNr()) {
                    correctAnswer++;
                }
                showNextQuestion();
            }
        });

        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(2==currentQuestion.getAnswerNr()) {
                    correctAnswer++;
                }
                showNextQuestion();
            }
        });

        optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(3==currentQuestion.getAnswerNr()) {
                    correctAnswer++;
                }
                showNextQuestion();
            }
        });

        optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(4==currentQuestion.getAnswerNr()) {
                    correctAnswer++;
                }
                showNextQuestion();
            }
        });
    }

    public void startChronometer(View v) {
        if(!running) {
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if(running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    private void showNextQuestion() {

        if(questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            optionA.setText(currentQuestion.getOptionA());
            optionB.setText(currentQuestion.getOptionB());
            optionC.setText(currentQuestion.getOptionC());
            optionD.setText(currentQuestion.getOptionD());

            questionCounter++;
            textViewQuestionCount.setText("Question: "+ questionCounter + "/" + questionCountTotal);
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        pauseChronometer(chronometer);
        if(questionCountTotal != correctAnswer) {
            long penalty = (questionCountTotal - correctAnswer) * 5000;
            float highScore = ((long) (float)(pauseOffset + penalty) / (float) 1000);
            Toast.makeText(this, "You did " + correctAnswer + "/" + questionCountTotal + " of your questions! Your score is " + highScore + " seconds.", Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCORE, (pauseOffset + penalty));
            setResult(RESULT_OK, resultIntent);
        } else {
            float highScore = ((long) (float)pauseOffset / (float) 1000);
            Toast.makeText(this, "You did " + correctAnswer + "/" + questionCountTotal + " of your questions! Your score is " + highScore + " seconds.", Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SCORE, pauseOffset);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish!", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}