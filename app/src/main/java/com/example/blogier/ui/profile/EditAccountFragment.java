package com.example.blogier.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.blogier.Database.User;
import com.example.blogier.Database.UserDatabase;
import com.example.blogier.R;
import com.example.blogier.navigation;
import com.example.blogier.ui.loginscreen.loginActivity;
import com.example.blogier.ui.loginscreen.registerActivity;

import static com.example.blogier.navigation.userName;
import static com.example.blogier.ui.loginscreen.loginActivity.UserTag;

public class EditAccountFragment extends Fragment {
    private Button changeLogin;
    private Button changeEmail;
    private Button back;
    private EditText editLogin;
    private EditText email;
    private EditText emailRepeat;
    static UserDatabase userDatabase;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_edit_account,container,false);
        userDatabase = Room.databaseBuilder(getContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
        Bundle bundle = this.getArguments();
            email = view.findViewById(R.id.editEmail);
            emailRepeat = view.findViewById(R.id.editEmailRepeat);
        editLogin = view.findViewById(R.id.editLogin);
        changeLogin = view.findViewById(R.id.confirmEditedLogin);
        changeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String login = editLogin.getText().toString();
            boolean check = userDatabase.userDao().checkLogin(login);
            if(!check)
            {
                User user = userDatabase.userDao().returnuser(userName);
                user.setUsername(login);
                userDatabase.userDao().update(user);
                Toast.makeText(getContext(), R.string.loginEdited, Toast.LENGTH_LONG).show();
                Intent intent  = new Intent(getContext(), loginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), R.string.newLogin, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), R.string.errorExist, Toast.LENGTH_LONG).show();
            }
            }
        });
        changeEmail = view.findViewById(R.id.confirmEditedEmail);
        changeEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email_text = email.getText().toString();
            String email_rep = emailRepeat.getText().toString();
            boolean check = userDatabase.userDao().checkEmail(email_text);
            if(!check)
            {
                if(email_text.matches(emailPattern) && email_rep.matches(emailPattern) && email_text.equals(email_rep))
                {
                    User user = userDatabase.userDao().returnuser(userName);
                    user.setEmail(email_text);
                    userDatabase.userDao().update(user);
                    Toast.makeText(getContext(), R.string.mailEdited, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), loginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(), R.string.errorMail, Toast.LENGTH_LONG).show();
                }}
            else
            {
                Toast.makeText(getContext(), R.string.emailExist, Toast.LENGTH_LONG).show();
            }
        }
    });
        back = view.findViewById(R.id.backtoProfile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment backToProfile = new ProfileFragment();
                backToProfile.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, backToProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

}
