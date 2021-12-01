package me.ablax.financemanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.ablax.financemanager.dto.Transaction;

public class SQLiteDB extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "purchases";

    public SQLiteDB(@Nullable final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.beginTransaction();
        this.createDb(db);
        db.endTransaction();
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.beginTransaction();
        db.execSQL("DROP TABLE IF EXISTS `payments`");
        this.createDb(db);
        db.endTransaction();
    }

    public void createUser(final String name) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `users` (`name`) VALUES (?)", Collections.singletonList(name).toArray(new String[0]));
    }

    public boolean hasUser() {
        final SQLiteDatabase db = this.getReadableDatabase();
        createDb(db);
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

    public void addTransaction(final Transaction transaction) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `payments` (`name`, `amount`) VALUES (?, ?)", Arrays.asList(transaction.getName(), transaction.getAmount()).toArray());
    }

    public List<Transaction> getTransactions() {
        final List<Transaction> transactionList = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT `id`, `name`, `amount` FROM `payments`", new String[0])) {
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    final Integer id = cursor.getInt(0);
                    final String name = cursor.getString(1);
                    final Double amount = cursor.getDouble(2);
                    transactionList.add(new Transaction(id, name, amount));
                }
            }
        }
        return transactionList;
    }


    private void createDb(final SQLiteDatabase db) {
        db.compileStatement("CREATE TABLE IF NOT EXISTS `payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(20), `amount` REAL )").execute();
        db.compileStatement("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(20))").execute();
    }
}
