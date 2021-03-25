package com.schnellmet.testforreflex.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.schnellmet.testforreflex.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighScore";
    private TextView textViewHighScore;
    private AppCompatButton startBTN;
    private long highScore;
    private float highScoreF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadHighScore();
        clickListener();

    }

    private void init() {
        startBTN = findViewById(R.id.startBTN);
        textViewHighScore = findViewById(R.id.highScore);
    }

    private void clickListener() {
        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, QuizActivity.class), REQUEST_CODE_QUIZ);
            }
        });
    }

    private void loadHighScore() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = preferences.getLong(KEY_HIGHSCORE, 30000);
        float highScoreF = ((long) (float)highScore / (float) 1000);
        textViewHighScore.setText("Highscore: " + highScoreF  + " seconds");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ) {
            if(resultCode == RESULT_OK) {

                long score = data.getLongExtra(QuizActivity.EXTRA_SCORE, 30000);

                if(score < highScore) {
                    updateHighScore(score);
                }
            }
        }
    }

    public void updateHighScore(long highScoreNew) {
        highScore = highScoreNew;
        float highScoreF = ((long) (float)highScore / (float) 1000);
        textViewHighScore.setText("Highscore: " + highScoreF  + " seconds");

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(KEY_HIGHSCORE, highScore);
        editor.apply();
    }

}