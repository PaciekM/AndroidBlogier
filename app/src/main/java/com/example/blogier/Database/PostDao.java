package com.example.blogier.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);

    @Query("DELETE FROM post")
    void deleteAll();

    @Query("SELECT * FROM post")
    LiveData<List<Post>> findAll();

    @Query("SELECT * FROM post")
    List<Post> findAlls();

    @Query("SELECT * FROM post WHERE author LIKE :author")
    List<Post> findMyPosts(String author);

    @Query("SELECT * FROM post WHERE post_id LIKE :post_id")
    Post findPost(int post_id);

    @Query("SELECT count(*) FROM post WHERE author LIKE :author")
    int numberOfPosts(String author);
    /*
    @Query("SELECT count(*) FROM user WHERE username LIKE :username AND password LIKE :password")
    int checkUser(String username, String password);

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM user where username LIKE :username OR email LIKE :email) THEN 1 ELSE 0 END")
    boolean checkInDatabase(String username, String email);

    @Query("SELECT email FROM user WHERE username LIKE :username")
    String returnUser(String username);
     */
}
