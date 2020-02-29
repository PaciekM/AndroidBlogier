package com.example.blogier.ui.posts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.blogier.Database.Post;
import com.example.blogier.R;
import com.example.blogier.ui.home.HomeFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.blogier.navigation.ImageUri;
import static com.example.blogier.navigation.postDatabase;
import static com.example.blogier.navigation.userName;
import static com.example.blogier.ui.profile.ProfileFragment.RESULT_LOAD_IMAGE;

public class addPost extends Fragment {
    private Button loadImage;
    private ImageView postImage;
    private EditText postTitle;
    private EditText postContent;
    private Button addPost;
    private Uri uri;
    private Button takePicture;
    private int code;
    private int post_id;
    private static final int REQUEST_TAKE_PICTURE = 101;
    private String currentPhotoPath;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        loadImage = view.findViewById(R.id.loadButton);
        postImage = view.findViewById(R.id.loadedImage);
        postTitle = view.findViewById(R.id.postTitle);
        postContent = view.findViewById(R.id.postContent);
        addPost = view.findViewById(R.id.addPostButton);
        takePicture = view.findViewById(R.id.takePictureButton);
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        uri = photoURI;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
                    }
                }
            }
        });
        Bundle b = getArguments();
        code  = b.getInt("code");
        Log.d("Code",""+code);
        if(code == 1) { //add
            addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(postTitle.getText().toString().matches("")) && !(postContent.getText().toString().matches("")) && !postTitle.getText().toString().isEmpty() && !postContent.getText().toString().isEmpty() && !uri.toString().isEmpty()) {
                        Post post = new Post();
                        post.setAuthor(userName);
                        post.setPostImageAvatar(ImageUri);
                        post.setTitle(postTitle.getText().toString());
                        post.setContent(postContent.getText().toString());
                        Date currentTime = Calendar.getInstance().getTime();
                        post.setData(currentTime);
                        post.setPostImage(uri.toString());
                        postDatabase.PostDao().insert(post);
                        Toast.makeText(getContext(), R.string.postCreated, Toast.LENGTH_LONG).show();
                        Fragment home = new HomeFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, home);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.postCreatedError, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        if(code == 2) //edit
        {
            post_id = (int) b.get("postID");
            Post tmpPost = postDatabase.PostDao().findPost(post_id);
            postImage.setImageURI(Uri.parse(tmpPost.getPostImage()));
            postTitle.setText(tmpPost.getTitle());
            postContent.setText(tmpPost.getContent());
            addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(postTitle.getText().toString().matches("")) && !(postContent.getText().toString().matches("")) && !postTitle.getText().toString().isEmpty() && !postContent.getText().toString().isEmpty() && !uri.toString().isEmpty()) {
                        tmpPost.setTitle(postTitle.getText().toString());
                        tmpPost.setContent(postContent.getText().toString());
                        Date currentTime = Calendar.getInstance().getTime();
                        tmpPost.setData(currentTime);
                        tmpPost.setPostImage(uri.toString());
                        postDatabase.PostDao().update(tmpPost);
                        Toast.makeText(getContext(), R.string.postEdit, Toast.LENGTH_LONG).show();
                        //tutaj moze byc blad jak zmienisz tylko jedną rzecz to wypierdoli errora
                        Fragment home = new myPostsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, home);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.postEditError, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return view;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    uri = imageReturnedIntent.getData();
                    Toast.makeText(getContext(), R.string.postImageAdded, Toast.LENGTH_LONG).show();
                    postImage.setImageURI(uri);
                }
            case 101: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), R.string.postImageAdded, Toast.LENGTH_LONG).show();
                    postImage.setImageURI(uri);
                }
            }
        }
    }
}


