package com.example.blogier.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.NonNull;

@Database(entities = {Post.class}, version = 1, exportSchema = false)
public abstract class PostDatabase extends RoomDatabase
{
    public abstract PostDao PostDao();
    private static volatile PostDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PostDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (PostDatabase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostDatabase.class, "post.db")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                PostDao dao = INSTANCE.PostDao();
                dao.deleteAll();
            });

        }
    };
}
