package com.example.blogier.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.blogier.Database.User;
import com.example.blogier.Database.UserDatabase;
import com.example.blogier.R;
import com.example.blogier.ui.loginscreen.registerActivity;

import static com.example.blogier.ui.loginscreen.loginActivity.UserTag;

public class ChangePasswordFragment extends Fragment {
    private EditText password;
    private EditText passwordRepeat;
    private Button button;
    static UserDatabase userDatabase;
    private String getName;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getName = bundle.getString(UserTag);
            Log.d("ChangeTag",""+getName);
        }
        userDatabase = Room.databaseBuilder(getContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
        password = root.findViewById(R.id.password);
        passwordRepeat = root.findViewById(R.id.passwordRepeat);
        button = root.findViewById(R.id.changePasswordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String passRep = passwordRepeat.getText().toString();
                if (!password.equals(passRep)) {
                    if (password.length() > 5 && passRep.length() > 5) {
                        User user = userDatabase.userDao().returnuser(getName);
                        user.setPassword(pass);
                        userDatabase.userDao().update(user);
                        Toast.makeText(getContext(), R.string.passwordUpdated, Toast.LENGTH_LONG).show();
                        Fragment backProfile = new ProfileFragment();
                        backProfile.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, backProfile);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(getContext(), R.string.password_error_length, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), R.string.password_error_match, Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}