package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class StartPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        //Token session check
        if (ParseUser.getCurrentUser() != null) {
            Intent intentLogIn = new Intent(StartPage.this, MainActivity.class);
            startActivity(intentLogIn);
            finish();
        }


    }

    public void switchToSignUp(View buttonView) {
        Intent intent = new Intent(StartPage.this, SignUp.class);
        startActivity(intent);
        finish();
    }
}
