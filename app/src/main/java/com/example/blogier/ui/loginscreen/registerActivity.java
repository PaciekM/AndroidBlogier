package com.example.blogier.ui.loginscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.blogier.Database.User;
import com.example.blogier.Database.UserDatabase;
import com.example.blogier.R;

import java.util.List;


public class registerActivity extends AppCompatActivity {
    EditText rLogin;
    EditText rPassword;
    EditText rPasswordRepeat;
    EditText email;
    Button register;
    static UserDatabase userDatabase;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userDatabase = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
        rLogin = findViewById(R.id.register_login);
        rPassword = findViewById(R.id.register_password);
        rPasswordRepeat = findViewById(R.id.register_password2);
        email = findViewById(R.id.register_email);
        register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = rLogin.getText().toString();
                String email_text = email.getText().toString();
                String password = rPassword.getText().toString();
                String password_confirm = rPasswordRepeat.getText().toString();
                boolean checkInDatabase = userDatabase.userDao().checkInDatabase(username, email_text);
                Log.d("RegisterActivity"," "+checkInDatabase);
                if (email_text.matches(emailPattern) &&  !checkInDatabase){
                    if (password.length() > 4 && username.length() > 0) {
                        if (password.equals(password_confirm)) {
                            User newUser = new User();
                            newUser.setUsername(username);
                            newUser.setPassword(password);
                            newUser.setEmail(email_text);
                            Uri uri = Uri.parse("android.resource://com.example.blogier/drawable/default_avatar");
                            newUser.setImage(uri.toString());
                            registerActivity.userDatabase.userDao().insert(newUser);
                            Toast.makeText(registerActivity.this, R.string.register_successful, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(registerActivity.this, loginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(registerActivity.this, R.string.password_error_match, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(registerActivity.this, R.string.password_error_length, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if(!(email_text.matches(emailPattern))) {
                        Toast.makeText(registerActivity.this, R.string.email_error, Toast.LENGTH_LONG).show();
                    }
                    if(checkInDatabase) {
                        Toast.makeText(registerActivity.this, R.string.data_repeat, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
