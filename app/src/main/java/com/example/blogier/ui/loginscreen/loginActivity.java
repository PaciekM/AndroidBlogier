package com.example.blogier.ui.loginscreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.blogier.Database.UserDatabase;
import com.example.blogier.R;
import com.example.blogier.navigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import static com.example.blogier.navigation.AvatarTag;


public class loginActivity extends AppCompatActivity {
    EditText login;
    EditText password;
    Button login_button;
    Button register_link;
    UserDatabase userDatabase;
    public static  GoogleSignInClient mGoogleSignInClient;
    private final static String TAG = "LoginActivityGoogle";
    public static String UserTag = "Username";
    public static String EmailTag = "Email";
    public static final int GOOGLE_SIGN_IN = 2;
    public final static String Login = "LOGIN_NORMAL";
    public static final int NORMAL_LOGIN = 1;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInIntent.putExtra(Login,GOOGLE_SIGN_IN);
        startActivityForResult(signInIntent, NORMAL_LOGIN);
    }
    public void  updateUI(GoogleSignInAccount account){
        if(account != null){
            Toast.makeText(this,R.string.login_success,Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,navigation.class).putExtra(Login,GOOGLE_SIGN_IN));
        }else {
            Toast.makeText(this,R.string.login_failed,Toast.LENGTH_LONG).show();
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loginscreen);
            userDatabase = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
            login = findViewById(R.id.login);
            password = findViewById(R.id.password);
            login_button = findViewById(R.id.loginButton);
            register_link = findViewById(R.id.registerButton);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            register_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent register = new Intent(loginActivity.this, registerActivity.class);
                    startActivity(register);
                }
            });
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = login.getText().toString();
                    String pass = password.getText().toString();
                    int check = userDatabase.userDao().checkUser(username, pass);
                    if (check == 1) {
                        Intent login = new Intent(loginActivity.this, navigation.class);
                        login.putExtra(UserTag, username);
                        login.putExtra(EmailTag, userDatabase.userDao().returnUser(username));
                        login.putExtra(Login, NORMAL_LOGIN);
                        login.putExtra(AvatarTag,userDatabase.userDao().returnImage(username));
                        startActivity(login);
                    } else {
                        Toast.makeText(loginActivity.this, R.string.wrong_data, Toast.LENGTH_LONG).show();
                    }
                }
            });

            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         switch (v.getId()) {
                                                                             case R.id.sign_in_button:
                                                                                 signIn();
                                                                                 break;
                                                                             // ...
                                                                         }
                                                                     }

                                                                 }

            );

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NORMAL_LOGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }
}
