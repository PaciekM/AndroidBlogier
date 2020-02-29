package com.example.blogier.Database;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int user_id;
    private String image;
    private String username;
    private String password;
    private String email;
}