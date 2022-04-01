package com.example.androidsqlitesetup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar; //Does this fix issue?

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper myDb;

    private static final String TAG =  MainActivity.class.getSimpleName();
    private static final int RESULT = 0;
    public static final String EMAIL_KEY = "EMAIL_KEY";
    public static final String EXAM_KEY = "EXAM_KEY";

    //all the buttons
    private Button mButtonA;
    private Button mButtonB;
    private Button mButtonC;
    private Button mButtonD;
    private Button mButtonE;
    private Button mNextButton;
    private Button mPrevButton;
    private Button msubmitButton;
    private Button mViewQuestionData; //TODO: add button onClickListener
    private Button mViewAnswerKeyData;
    private Button mViewSubmissionInstructionData;
    private Button mViewTestData;
    private Button mViewStudentInfoData;
    private Button mDeleteAnswerKeyData;

    private TextView mQuestionTextView;
    private ArrayList<Question> questions; //Have an arraylist of Questions, and each Question has 5 options and an answer
    private ArrayList<Answer> answers; //Added for thesis

    private int mCurrentQuestionIndex; //used to determine which question user is on
    private static String QUESTION_INDEX_KEY = "question_index";

    private int[] buttons = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //used to determine which answer user has selected for each of the 10 questions
    private String emailString = "";

    Toolbar mToolBar;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //DO I NEED THIS METHOD???
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.aToolBar);
        setSupportActionBar(mToolBar);

        myDb = new DBHelper(this);
        //MainActivity.this.openOrCreateDatabase("database", null); //Do I not define it here?

        mButtonA = (Button) findViewById(R.id.a_button);
        mButtonB = (Button) findViewById(R.id.b_button);
        mButtonC = (Button) findViewById(R.id.c_button);
        mButtonD = (Button) findViewById(R.id.d_button);
        mButtonE = (Button) findViewById(R.id.e_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        msubmitButton = (Button) findViewById(R.id.submit_button);
        mViewQuestionData = findViewById(R.id.question_table_button);
        mViewAnswerKeyData = findViewById(R.id.answer_key_table_button);
        mViewSubmissionInstructionData = findViewById(R.id.submission_table_button);
        mViewTestData = findViewById(R.id.test_table_button);
        mViewStudentInfoData = findViewById(R.id.student_info_table_button);
        mDeleteAnswerKeyData = findViewById(R.id.delete_answer_key_data_button);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button A Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(1); //'get' returns the element at the specified position in this list
                //mCurrentSelectedButton = 1;
                buttons[mCurrentQuestionIndex] = 1;
                buttonColour();
            }
        });
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button B Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(2);
                buttons[mCurrentQuestionIndex] = 2;
                buttonColour();
            }
        });
        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button C Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(3);
                buttons[mCurrentQuestionIndex] = 3;
                buttonColour();
            }
        });
        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button D Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(4);
                buttons[mCurrentQuestionIndex] = 4;
                buttonColour();
            }
        });
        mButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button E Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(5);
                buttons[mCurrentQuestionIndex] = 5;
                buttonColour();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentQuestionIndex++;
                if(mCurrentQuestionIndex>=questions.size()) mCurrentQuestionIndex = 0; //goes back to first question
                mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
                buttonColour();
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentQuestionIndex--;
                if(mCurrentQuestionIndex<0) mCurrentQuestionIndex = questions.size() - 1; //goes to last question
                mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
                buttonColour();
            }
        });
        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Should I call the db.insertTestData() method here???

                Log.i(TAG, "Submit Button Clicked"); //print to console for debugging
                Intent intent = new Intent(MainActivity.this, Submission.class); //goes to a new page
                Log.i(TAG, "Submit Button Clicked"); //print to console for debugging
                intent.putExtra(EMAIL_KEY, emailString);

                intent.putExtra(EXAM_KEY, buttons); //have different keys
                //startActivityForResult(intent, RESULT);
                startForResult.launch(intent);
                //startActivity(intent);
            }
        });

        //Methods below are
        mViewQuestionData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getQuestionsData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Q#: " + res.getString(0) + "\n");
                    buffer.append("Description: " + res.getString(1) + "\n");
                    buffer.append("A: " + res.getString(2) + "\n");
                    buffer.append("B: " + res.getString(3) + "\n");
                    buffer.append("C: " + res.getString(4) + "\n");
                    buffer.append("D: " + res.getString(5) + "\n");
                    buffer.append("E: " + res.getString(6) + "\n\n");
                }

                //Show all data
                showMessage("Questions table Data", buffer.toString());
            }
        });

        mViewAnswerKeyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAnswerKeysData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Q#: " + res.getString(0) + "\n");
                    buffer.append("Answer: " + res.getString(1) + "\n\n");
                }

                //Show all data
                showMessage("Answer Keys Data", buffer.toString());
            }
        });

        mViewSubmissionInstructionData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getSubmissionData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Email:" + res.getString(0) + "\n\n");
                }

                //Show all data
                showMessage("Submission Instruction table Data", buffer.toString());
            }
        });

        mViewTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getTestData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Q#: " + res.getString(0) + "\n");
                    buffer.append("Answer: " + res.getString(1) + "\n\n");
                }

                //Show all data
                showMessage("Test table Data", buffer.toString());
            }
        });

        mViewStudentInfoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getStudentInfoData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Name: " + res.getString(0) + "\n");
                    buffer.append("Email: " + res.getString(1) + "\n");
                    buffer.append("Student #: " + res.getString(2) + "\n\n");
                }

                //Show all data
                showMessage("Student Info Data", buffer.toString());
            }
        });

        mDeleteAnswerKeyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteData();
            }
        });

        questions = null;
        answers = null;

        mCurrentQuestionIndex = 0;

        //Try to read resource data file with questions
        Exam emailObject = new Exam();
        ArrayList<Question> parsedModel = null;
        try {
            InputStream iStream = getResources().openRawResource(R.raw.comp2601exam);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            //ArrayList<Question> parsedModel = Exam.parseFrom(bs);
            //emailString = Exam.pullParseFrom(bReader);
            parsedModel = Exam.pullParseFrom(bReader); //calls this static method to parse the data and create the exam questions
            emailString = emailObject.getEmail(); //gets the email text
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        if(parsedModel == null || parsedModel.isEmpty())
            Log.i(TAG, "ERROR: Questions Not Parsed");
        questions = parsedModel;
        Log.i(TAG, "Email is: " + emailString);

        //Call the insertQuestionData() method below here
        for (int i = 0; i < questions.size(); i++){
            String questionDesc = questions.get(i).getQuestionString();
            boolean questionInserted = myDb.insertQuestionData(questionDesc, questions.get(i).getQuestionOptions());
            if (questionInserted) {
                Toast.makeText(MainActivity.this, "Question has been inserted into db", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Question has NOT been inserted into db", Toast.LENGTH_LONG).show();
            }
        }

        if(questions != null && questions.size() > 0)
            mQuestionTextView.setText("" + (mCurrentQuestionIndex + 1) + ") " +
                    questions.get(mCurrentQuestionIndex).toString());

        // Calls the insertAnswerKeyData() method below here

        ArrayList<Answer> parsedModel_Answers = null;
        try {
            InputStream iStream = getResources().openRawResource(R.raw.answerkey);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            parsedModel_Answers = AnswerKey.pullParseFrom(bReader);
            bReader.close();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
        if(parsedModel_Answers == null || parsedModel_Answers.isEmpty())
            Log.i(TAG, "ERROR: Answers Not Parsed");
        answers = parsedModel_Answers;
        Log.i(TAG, "Answers are: " + answers);

        for (int i = 0; i < answers.size(); i++){
            boolean answerInserted = myDb.insertAnswerKeyData(i+1, answers.get(i).getAnswerString());
            if (answerInserted) {
                Toast.makeText(MainActivity.this, "Answer has been inserted into db", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Answer has NOT been inserted into db", Toast.LENGTH_LONG).show();
            }
        }

        if(savedInstanceState != null){ //check for the possiblity that there is no saved state information
            mCurrentQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX_KEY, 0);
            buttons = (int[]) savedInstanceState.getSerializable("arr");
            //mCurrentSelectedButton = savedInstanceState.getInt(BUTTON_KEY, 0);
            mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
            Log.i(TAG, "Current question index: " + mCurrentQuestionIndex);
            //Log.i(TAG, "Current button selected: " + questions.get(mCurrentQuestionIndex).getButton());
            Log.i(TAG, "Current button selected: " + buttons[mCurrentQuestionIndex]);
            buttonColour();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){ //the state is restored in onCreate()
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(QUESTION_INDEX_KEY, mCurrentQuestionIndex); //saves the current question user is on
        savedInstanceState.putSerializable("arr", buttons); //saves all of the user's selected answers
        //savedInstanceState.putInt(BUTTON_KEY, mCurrentSelectedButton);
        Log.i(TAG, "onSaveInstanceState(Bundle)");
    }

    private void buttonColour(){
        buttonClear();

        if(buttons[mCurrentQuestionIndex]==1){
            mButtonA.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==2){
            mButtonB.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==3){
            mButtonC.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==4){
            mButtonD.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==5){
            mButtonE.setBackgroundColor(Color.BLUE);
        }
    }

    private void buttonClear(){
        mButtonA.setBackgroundColor(Color.GRAY);
        mButtonB.setBackgroundColor(Color.GRAY);
        mButtonC.setBackgroundColor(Color.GRAY);
        mButtonD.setBackgroundColor(Color.GRAY);
        mButtonE.setBackgroundColor(Color.GRAY);
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}