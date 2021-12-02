package me.ablax.financemanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Collections;

public class UsersDb extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "users";

    public UsersDb(@Nullable final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    public void createUser(final String name) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `users` (`name`) VALUES (?)", Collections.singletonList(name).toArray(new String[0]));
    }

    public boolean hasUser() {
        final SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM `users`", new String[0])) {
            return cursor.getCount() > 0;
        }
    }

    public String getUserName() {
        final SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT `name` FROM `users`", new String[0])) {
            cursor.moveToNext();
            return cursor.getString(0);
        }
    }

    private void createDb(final SQLiteDatabase db) {
        db.compileStatement("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(20))").execute();
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        this.createDb(db);
    }


    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        this.createDb(db);
    }
}
