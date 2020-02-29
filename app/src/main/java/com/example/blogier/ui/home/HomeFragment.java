package com.example.blogier.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogier.Database.Post;
import com.example.blogier.R;
import com.example.blogier.ui.posts.ViewPost;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.blogier.navigation.postDatabase;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerview);
        List<Post> list = postDatabase.PostDao().findAlls();
        final PostAdapter adapter = new PostAdapter();
        adapter.setBooks(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    public class PostAdapter extends RecyclerView.Adapter<PostHolder> {
        private List<Post> posts = postDatabase.PostDao().findAlls();

        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder holder, int position) {
            if (posts != null) {
                Post post = posts.get(position);
                holder.bind(post);
            } else {
                Log.d("Navigation", "No posts");
            }
        }

        void setBooks(List<Post> postss) {
            posts = postss;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

    private class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView postImage;
        private TextView title;
        private TextView subtitle;
        private ImageView postAvatar;
        private TextView postDescription;
        private TextView postDate;
        Post post;

        private PostHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.post_list_item, parent, false));
            postImage = itemView.findViewById(R.id.postImage);
            title = itemView.findViewById(R.id.Title);
            subtitle = itemView.findViewById(R.id.Subtitle);
            postAvatar = itemView.findViewById(R.id.postAvatar);
            postDescription = itemView.findViewById(R.id.postDescription);
            postDate = itemView.findViewById(R.id.postDate);
            itemView.setOnClickListener(this);
        }

        private void bind(Post post) {
            this.post = post;
            if (post != null) {
                title.setText(post.getTitle());
                subtitle.setText(post.getAuthor());
                if (post.getContent().length() > 50) {
                    postDescription.setText(post.getContent().substring(0, 49) + "....");
                } else {
                    postDescription.setText(post.getContent());
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String strDate = dateFormat.format(post.getData());
                postDate.setText(strDate);
                postImage.setImageURI(Uri.parse(post.getPostImage()));
                String[] currencies = post.getAuthor().split(" ");
                if(currencies.length == 1)
                {
                    postAvatar.setImageURI(Uri.parse(post.getPostImageAvatar()));
                }
                else {
                    Glide.with(getActivity()).load(String.valueOf(post.getPostImageAvatar())).into(postAvatar);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Fragment edit = new ViewPost();
            Bundle b = new Bundle();
            b.putInt("post_id",post.getPost_id());
            edit.setArguments(b);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, edit);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }
}