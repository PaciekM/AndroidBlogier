package com.example.blogier.ui.posts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.blogier.Database.Post;
import com.example.blogier.R;
import com.example.blogier.ui.home.HomeFragment;
import com.example.blogier.ui.profile.ProfileFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.example.blogier.navigation.postDatabase;

public class ViewPost extends Fragment {
    private TextView authorView;
    private TextView postTiTleView;
    private TextView postDateView;
    private TextView postContentView;
    private ImageView authorAvatar;
    private ImageView postImageView;
    private Button button;
    private int id;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_post, container, false);
        authorView = root.findViewById(R.id.authorLabel);
        postTiTleView = root.findViewById(R.id.postTitleView);
        postDateView = root.findViewById(R.id.postDateView);
        postContentView = root.findViewById(R.id.postContentView);
        authorAvatar = root.findViewById(R.id.authorAvatar);
        postImageView = root.findViewById(R.id.postImageView);
        button = root.findViewById(R.id.buttonView);
        Bundle b = getArguments();
        id  = b.getInt("post_id");
        Log.d("asd",""+id);
        Post post  = postDatabase.PostDao().findPost(id);
        authorView.setText(post.getAuthor());
        postTiTleView.setText(post.getTitle());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String strDate = dateFormat.format(post.getData());
        postDateView.setText(strDate);
        Log.d("Avatar",""+post.getPostImageAvatar());
        Log.d("Avatar",""+post.getPostImageAvatar().contains("https"));
        postContentView.setText(post.getContent());
        postContentView.setMovementMethod(new ScrollingMovementMethod());
        authorAvatar.setImageURI(Uri.parse(post.getPostImageAvatar()));
        postImageView.setImageURI(Uri.parse(post.getPostImage()));
        if(!(post.getPostImageAvatar().contains("https")))
            authorAvatar.setImageURI(Uri.parse(post.getPostImageAvatar()));
        else if(post.getPostImageAvatar().contains("https"))
            Glide.with(getContext()).load(String.valueOf(post.getPostImageAvatar())).into(authorAvatar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment backToProfile = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, backToProfile);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return root;
    }
}
