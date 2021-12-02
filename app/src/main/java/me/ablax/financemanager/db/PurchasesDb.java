package me.ablax.financemanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ablax.financemanager.dto.PurchaseDto;

public class PurchasesDb extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "purchases";

    public PurchasesDb(@Nullable final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void addPurchase(final PurchaseDto purchase) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `payments` (`transactionId`, `name`, `amount`) VALUES (?, ?, ?)", Arrays.asList(purchase.getTransactionId(), purchase.getName(), purchase.getQuantity()).toArray());
    }

    public void deletePurchase(final int transactionId, final int purchaseId) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `payments` WHERE `id` = ? AND `transactionId` = ? ", Arrays.asList(purchaseId, transactionId).toArray());
    }

    public void deleteAllPurchaseForTransaction(final int transactionId) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `payments`");
    }

    public List<PurchaseDto> getPurchaseForTransaction(final int transactionId) {
        final List<PurchaseDto> transactionList = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT `transactionId`, `id`, `name`, `amount` FROM `payments` WHERE transactionId = ?", Arrays.asList(transactionId).toArray(new String[0]))) {
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    final Integer id = cursor.getInt(0);
                    final String name = cursor.getString(1);
                    final Integer amount = cursor.getInt(2);
                    transactionList.add(new PurchaseDto(id, transactionId, name, amount));
                }
            }
        }
        return transactionList;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        this.createDb(db);
    }


    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `purchases`");
        this.createDb(db);
    }

    private void createDb(final SQLiteDatabase db) {
        db.compileStatement("CREATE TABLE IF NOT EXISTS `purchases` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `transactionId` INTEGER, `name` VARCHAR(20), `amount` INTEGER )").execute();
    }
}
