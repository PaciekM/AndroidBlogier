package com.example.blogier.Database;

import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
@Getter
@Setter
@Entity(tableName = "post")
public class Post
{
    @PrimaryKey(autoGenerate = true)
    private int post_id;
    private String author;
    private String title;
    @TypeConverters(DateConverter.class)
    private Date data;
    private String postImage;
    private String postImageAvatar;
    private String content;
}
