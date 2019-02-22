package com.example.a2braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private TextView questionTxt;
    private TextView scoreTxt;
    private TextView resultTxt;
    private TextView timerTxt;
    private TableLayout tableLayout;

    private int[] values = new int[4];
    private int[] btnIDs = {
            R.id.answer1Btn,
            R.id.answer2Btn,
            R.id.answer3Btn,
            R.id.answer4Btn
    };
    private Random rand = new Random();
    private int correctAnswer; // Correct answer to the displayed question.
    private int score = 0;  // Total score.
    private int questionNum = 0; // Total questions asked.
    private CountDownTimer countDownTimer;
    private long countDownPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countDownPeriod = 10000;
        this.setupViews();
    }

    private void setupViews() {
        this.startBtn = findViewById(R.id.startBtn);
        this.questionTxt = findViewById(R.id.questionTxt);
        this.timerTxt = findViewById(R.id.timerTxt);
        this.scoreTxt = findViewById(R.id.scoreTxt);
        this.resultTxt = findViewById(R.id.resultTxt);
        this.tableLayout = findViewById(R.id.myTableLayout);
    }

    private void startGame() {
        score = 0;
        questionNum = 0;

        this.changeQuestion();
        this.setPossibleAnswers();
        this.setTimer();
    }

    public void onStart(View view) {
        startBtn.setVisibility(View.INVISIBLE);
        countDownPeriod = 10000;
        this.startGame();
        tableLayout.setVisibility(View.VISIBLE);
        resultTxt.setText("");
    }

    private void changeQuestion() {


        // Randomize the question.
        // Arbitrary max value for A & B in the question.
        final int RANGE = 21;
        int a = this.rand.nextInt(RANGE);
        int b = this.rand.nextInt(RANGE);

        // Store the correct answer for later comparisons.
        this.correctAnswer = a + b;

        // Compute and display possible answers.
        this.setPossibleAnswers();

        // Display the question.
        this.questionTxt.setText(Integer.toString(a) + " + " + Integer.toString(b));
    }

    private void setPossibleAnswers() {
        // Generate 3 false answers.
        int[] fakeAnswers = {
                this.correctAnswer + 1,
                this.correctAnswer - 1,
                this.correctAnswer + 2};

        // Randomly place correct answer in array, fill empty spots with wrong answers.
        int test = this.rand.nextInt(this.values.length);
        this.values[test] = this.correctAnswer;
        for (int index = 0, fakeIndex = 0; index < this.values.length; index++) {
            // Skip over correct answer.
            if (this.values[index] == this.correctAnswer) {
                continue;
            }

            // Fill in fake answers.
            this.values[index] = fakeAnswers[fakeIndex++];
        }

        this.displayPossibleAnswers();
    }

    private void displayPossibleAnswers() {
        // Display all answers.
        for (int index = 0; index < this.btnIDs.length; index++) {
            Button btn = findViewById(this.btnIDs[index]);
            btn.setText(String.valueOf(values[index]));
        }
    }

    public void checkAnswer(View view) {
        String tag = (String) view.getTag();
        int chosenAnswer = this.values[Integer.parseInt(tag)];
        String msg = "";

        if (chosenAnswer == this.correctAnswer) {
            msg = "CORRECT :)";

            // Update and display score.
            this.score += 1;
            this.scoreTxt.setText(String.valueOf(this.score));

            // Update timer.
            countDownPeriod += 2000;
            countDownTimer.cancel();
            setTimer();

        } else {
            msg = "INCORRECT :(";

            // Update timer.
            countDownPeriod -= 2000;
            countDownTimer.cancel();
            setTimer();
        }

        // Display result.
        // Update question counter.
        this.questionNum += 1;
        this.scoreTxt.setText(String.valueOf(this.score) + "/" + String.valueOf(this.questionNum));
        this.resultTxt.setText(msg);

        // Change question and displayed possible answers.
        this.changeQuestion();
    }

    private void setTimer() {
        countDownTimer = new CountDownTimer(countDownPeriod, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTxt.setText(String.valueOf(millisUntilFinished / 1000) + "s");
                countDownPeriod = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                String msg = "You answered " + (int)Math.floor((score / (float)questionNum) * 100) + "% correctly!";
                resultTxt.setText(msg);
                endGame();
            }
        }.start();
    }

    private void endGame() {
        this.tableLayout.setVisibility(View.INVISIBLE);
        this.scoreTxt.setText("");
        this.questionTxt.setText("");
        this.timerTxt.setText("");
        this.startBtn.setVisibility(View.VISIBLE);
    }
}
