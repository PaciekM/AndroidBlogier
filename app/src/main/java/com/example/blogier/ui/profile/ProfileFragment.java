package com.example.blogier.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.blogier.Database.User;
import com.example.blogier.R;

import static android.app.Activity.RESULT_OK;
import static com.example.blogier.navigation.ImageUri;
import static com.example.blogier.navigation.LoginType;
import static com.example.blogier.navigation.postDatabase;
import static com.example.blogier.navigation.userDatabase;
import static com.example.blogier.navigation.userName;

public class ProfileFragment extends Fragment {

    private ImageView avatar;
    private TextView nameLabel;
    private TextView accInfo;
    private Button changeAvatar;
    private Button editAccount;
    private Button changePassword;
    private Button deleteAccount;
    private CharSequence getName;
    private String getAvatar;
    public static int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        /*
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getName = bundle.getString(UserTag);
            getAvatar = bundle.getString(AvatarTag);
            Log.d("Tag",""+getName);
        }

         */
        avatar = view.findViewById(R.id.avatar);
        avatar.setImageURI(Uri.parse(ImageUri));
        nameLabel = view.findViewById(R.id.nameLabel);
        nameLabel.setText(userName);
        accInfo = view.findViewById(R.id.accInfo);
        int number = postDatabase.PostDao().numberOfPosts(userName);
        accInfo.setText(number + " "+ getString(R.string.postWritten));
        changeAvatar = view.findViewById(R.id.changeAvatar);
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        editAccount = view.findViewById(R.id.editAccount);
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment edit = new EditAccountFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, edit);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        changePassword = view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment changePassword = new ChangePasswordFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, changePassword);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        deleteAccount = view.findViewById(R.id.deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment delete = new DeleteFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, delete);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        if(LoginType == 2)
        {
            Glide.with(getContext()).load(String.valueOf(ImageUri)).into(avatar);
            changeAvatar.setVisibility(View.INVISIBLE);
            editAccount.setVisibility(View.INVISIBLE);
            changePassword.setVisibility(View.INVISIBLE);
            deleteAccount.setVisibility(View.INVISIBLE);
        }
        return view;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK)
                {
                    Uri uri = imageReturnedIntent.getData();
                    Log.d("UriData",""+ uri);
                    User user = userDatabase.userDao().returnuser(userName);
                    user.setImage(uri.toString());
                    userDatabase.userDao().update(user);
                    Toast.makeText(getContext(), R.string.avatarChanged, Toast.LENGTH_LONG).show();
                    avatar.setImageURI(uri);

                }
        }
    }
}