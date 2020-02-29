package com.example.blogier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.blogier.Database.PostDatabase;
import com.example.blogier.Database.UserDatabase;
import com.example.blogier.ui.posts.addPost;
import com.example.blogier.ui.home.HomeFragment;
import com.example.blogier.ui.loginscreen.loginActivity;
import com.example.blogier.ui.posts.myPostsFragment;
import com.example.blogier.ui.profile.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.blogier.ui.loginscreen.loginActivity.EmailTag;
import static com.example.blogier.ui.loginscreen.loginActivity.Login;
import static com.example.blogier.ui.loginscreen.loginActivity.UserTag;
import static com.example.blogier.ui.loginscreen.loginActivity.mGoogleSignInClient;

public class navigation extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView username;
    private TextView email;
    private ImageView avatar;
    private String personName;
    private String personEmail;
    private Uri personPhoto;
    private DrawerLayout drawer;
    private boolean logoutCheck;
    public static UserDatabase userDatabase;
    public static PostDatabase postDatabase;
    public static String userName;
    public static String ImageUri;
    public final static String AvatarTag = "Avatar";
    public static int LoginType;
    private GoogleSignInAccount acct;
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        userDatabase = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "user.db").allowMainThreadQueries().build();
        postDatabase = Room.databaseBuilder(getApplicationContext(),PostDatabase.class, "post.db").allowMainThreadQueries().build();
        Bundle bundle = getIntent().getExtras();
        acct = GoogleSignIn.getLastSignedInAccount(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment AddPost = new addPost();
                Bundle b = new Bundle();
                b.putInt("code",1);
                AddPost.setArguments(b);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, AddPost);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        LinearLayout mParent = (LinearLayout) navigationView.getHeaderView(0);
        username = mParent.findViewById(R.id.home_username);
        email = mParent.findViewById(R.id.home_email);
        avatar = mParent.findViewById(R.id.avatarInToolbar);
        if((int)bundle.get(Login) == 1)
        {
            LoginType = 1;
            userName = bundle.get(UserTag).toString();
            ImageUri = bundle.get(AvatarTag).toString();
            username.setText(bundle.get(UserTag).toString());
            email.setText(bundle.get(EmailTag).toString());
            avatar.setImageURI(Uri.parse(ImageUri));
        }
        else if((int)bundle.get(Login) == 2)
        {
            if (acct != null) {
                LoginType = 2;
                userName = personName;
                ImageUri = acct.getPhotoUrl().toString();
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                String personId = acct.getId();
                personPhoto = acct.getPhotoUrl();
                avatar.setImageURI(personPhoto);
                Glide.with(mParent.getContext()).load(String.valueOf(personPhoto)).into(avatar);
            }
            username.setText(personName);
            userName = personName;
            email.setText(personEmail);

        }
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.nav_myPosts, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
    }


    @Override
    public void onBackPressed() {

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        Bundle bundle = new Bundle();
        logoutCheck = false;
        switch(menuItem.getItemId()) {

            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_logout:
                fragmentClass = null;
                logoutCheck = true;
                if(LoginType == 1) {
                    Intent intent = new Intent(navigation.this, loginActivity.class);
                    startActivity(intent);
                }
                if(LoginType == 2)
                {
                    signOut();
                    Intent intent = new Intent(navigation.this, loginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_myPosts:
                fragmentClass = myPostsFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            if(fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
                if (bundle != null) {
                    fragment.setArguments(bundle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!logoutCheck) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            drawer.closeDrawers();
        }
    }
}
