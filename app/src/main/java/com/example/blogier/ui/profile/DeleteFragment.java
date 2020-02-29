package com.example.blogier.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import static com.example.blogier.navigation.userName;
import static com.example.blogier.ui.loginscreen.loginActivity.UserTag;


public class DeleteFragment extends Fragment {
    private Button delete;
    private Button back;
    private TextView text;
    static UserDatabase userDatabase;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_delete_account,container,false);
        userDatabase = Room.databaseBuilder(getContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
        delete = view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = userDatabase.userDao().returnuser(userName);
                userDatabase.userDao().delete(user);
                Intent intent  = new Intent(getContext(), loginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), R.string.deleteSuccessful, Toast.LENGTH_LONG).show();

            }
        });
        back = view.findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment backToProfile = new ProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, backToProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        text = view.findViewById(R.id.dearUser);
        text.setText(getString(R.string.dear) +" "+ userName);
        return view;
    }
}
