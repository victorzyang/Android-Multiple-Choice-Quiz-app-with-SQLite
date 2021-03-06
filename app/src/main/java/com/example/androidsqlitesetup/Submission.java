package com.example.androidsqlitesetup;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class Submission extends AppCompatActivity {

    DBHelper myDb; //DBHelper instance used for inserting student information and test data into database

    private EditText mNameText;
    private EditText mStudentNumberText;
    private Button submit;
    //private String name;
    //private String number;
    private  final String TAG = this.getClass().getSimpleName() + " @" + System.identityHashCode(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submission);

        myDb = new DBHelper(this);

        mNameText = (EditText) findViewById(R.id.nameEditText);
        mStudentNumberText = (EditText) findViewById(R.id.studentNumberEditText);
        submit = (Button) findViewById(R.id.submitButton);

        //final String email = getIntent().getStringExtra(MainActivity.EMAIL_KEY); //is this the correct code???

        //int[] array = getIntent().getIntArrayExtra(MainActivity.EXAM_KEY); //is this the correct code???

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameText.getText().toString();
                String number = mStudentNumberText.getText().toString();

                //int[] array = new int[10];
                int[] array = getIntent().getIntArrayExtra(MainActivity.EXAM_KEY); //is this the correct code???

                if(!TextUtils.isEmpty(name)==true/*name.length()!=0*/ && /*number.length()!=0*/!TextUtils.isEmpty(number)==true /*&& TextUtils.isDigitsOnly(number)*/){ //make sure that a name and number has been entered

                    Log.i(TAG, "Length og name: " + name.length()); //debugging
                    String email = getIntent().getStringExtra(MainActivity.EMAIL_KEY); //gets the email string from the MainActivity

                    //email the test results
                    //String emailURI = "mailto:" + email;
                    //Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(emailURI));
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
                    String emailSubject = "";

                    //call the database insertTestData() method here for each of the student's answers
                    for(int i = 0; i < 10; i++){ //goes through all of the student's answers in the identifier that was passed through explicit intents
                        Log.i(TAG, "" + array[i]); //debugging
                        boolean testDataInserted;
                        //determine what to write in the email body
                        if(array[i]==1){
                            emailSubject += "" + (i+1) + ") A\n";
                            testDataInserted = myDb.insertTestData("A");
                        }else if(array[i]==2){
                            emailSubject += "" + (i+1) + ") B\n";
                            testDataInserted = myDb.insertTestData("B");
                        }else if(array[i]==3){
                            emailSubject += "" + (i+1) + ") C\n";
                            testDataInserted = myDb.insertTestData("C");
                        }else if(array[i]==4){
                            emailSubject += "" + (i+1) + ") D\n";
                            testDataInserted = myDb.insertTestData("D");
                        }else if(array[i]==5){
                            emailSubject += "" + (i+1) + ") E\n";
                            testDataInserted = myDb.insertTestData("E");
                        }else{
                            emailSubject += "" + (i+1) + ") N/A\n";
                            testDataInserted = myDb.insertTestData("N/A");
                        }

                        if (testDataInserted){
                            Toast.makeText(Submission.this, "Test data has been inserted into db", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Submission.this, "Test data has NOT been inserted into db", Toast.LENGTH_LONG).show();
                        }
                    }

                    // call the database insertStudentData() method here for inserting student info
                    boolean studentDataInserted = myDb.insertStudentData(email, name, Integer.parseInt(number));
                    if (studentDataInserted) {
                        Toast.makeText(Submission.this, "Student data has been inserted into db", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Submission.this, "Student data has NOT been inserted into db", Toast.LENGTH_LONG).show();
                    }

                    emailIntent.putExtra(Intent.EXTRA_TEXT, emailSubject);

                    startActivity(Intent.createChooser(emailIntent, "Email Client..."));
                }
            }
        });
    }
}
