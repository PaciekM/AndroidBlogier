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
public interface UserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("SELECT * FROM user ORDER BY username")
    LiveData<List<User>> findAll();

    @Query("SELECT count(*) FROM user WHERE username LIKE :username AND password LIKE :password")
    int checkUser(String username, String password);

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM user where username LIKE :username OR email LIKE :email) THEN 1 ELSE 0 END")
    boolean checkInDatabase(String username, String email);

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM user where username LIKE :username) THEN 1 ELSE 0 END")
    boolean checkLogin(String username);

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM user where email LIKE :email) THEN 1 ELSE 0 END")
    boolean checkEmail(String email);

    @Query("SELECT image from user WHERE username LIKE :username")
    String returnImage(String username);

    @Query("SELECT email FROM user WHERE username LIKE :username")
    String returnUser(String username);

    @Query("SELECT * from user WHERE username LIKE :username")
    User returnuser(String username);
}
