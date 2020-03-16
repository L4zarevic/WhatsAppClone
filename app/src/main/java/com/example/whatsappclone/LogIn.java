package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Initialization
        edtEmail = findViewById(R.id.edtEmail_LogInActivity);
        edtPassword = findViewById(R.id.edtPassword_LogInActivity);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogIn);
                }
                return false;
            }
        });
        btnSignUp = findViewById(R.id.btnSignUp_LogInActivity);
        btnLogIn = findViewById(R.id.btnLogIn_LogInActivity);

        btnLogIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        //Token session check
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogIn_LogInActivity:
                if (edtEmail.getText().toString().trim().equals("") ||
                        edtPassword.getText().toString().trim().equals("")) {

                    FancyToast.makeText(LogIn.this, "Email,Password is required!", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

                } else {
                    ParseUser.logInInBackground(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LogIn.this, user.getUsername() + " is Logged in successfully ", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                transitionToSocialMediaActivity();
                            }
                        }
                    });
                }
                break;

            case R.id.btnSignUp_LogInActivity:
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
                break;
        }

    }

    //A method that allows us to hide the keyboard
    public void rootLoginLayout(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(LogIn.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
