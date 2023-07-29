package com.alpertunademirbas.nakile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TransferDB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TRANSFERS = "transfers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_TRANSFER_DATE = "transferDate";
    public static final String COLUMN_NAME_SURNAME = "nameSurname";
    public static final String COLUMN_TRANSFER_LOCATION = "transferLocation";
    public static final String COLUMN_TRANSFER_PRICE = "transferPrice";
    public static final String COLUMN_CHARACTER = "character";

    private static final String CREATE_TABLE_TRANSFERS = "CREATE TABLE " + TABLE_TRANSFERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_SURNAME + " TEXT,"
            + COLUMN_PHONE_NUMBER + " TEXT,"
            + COLUMN_TRANSFER_DATE + " TEXT,"
            + COLUMN_NAME_SURNAME + " TEXT,"
            + COLUMN_TRANSFER_PRICE + " TEXT,"
            + COLUMN_CHARACTER + " TEXT,"
            + COLUMN_TRANSFER_LOCATION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSFERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSFERS);
        onCreate(db);
    }
    public void addTransfer(Transfer transfer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, transfer.getName());
        values.put(COLUMN_SURNAME, transfer.getSurname());
        values.put(COLUMN_PHONE_NUMBER, transfer.getPhoneNumber());
        values.put(COLUMN_TRANSFER_DATE, transfer.getTransferDate());
        values.put(COLUMN_NAME_SURNAME, transfer.getNameSurname());
        values.put(COLUMN_TRANSFER_LOCATION, transfer.getTransferLocation());
        values.put(COLUMN_TRANSFER_PRICE, transfer.getTransferPrice());
        values.put(COLUMN_CHARACTER, transfer.getCharacter());

        db.insert(TABLE_TRANSFERS, null, values);
        db.close();
    }

    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_SURNAME + ", " + COLUMN_PHONE_NUMBER + ", " + COLUMN_TRANSFER_DATE + ", " + COLUMN_NAME_SURNAME + ", " + COLUMN_TRANSFER_LOCATION + ", " + COLUMN_TRANSFER_PRICE + ", " + COLUMN_CHARACTER +" FROM " + TABLE_TRANSFERS, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int surnameIndex = cursor.getColumnIndex(COLUMN_SURNAME);
                int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
                int transferDateIndex = cursor.getColumnIndex(COLUMN_TRANSFER_DATE);
                int nameSurnameIndex = cursor.getColumnIndex(COLUMN_NAME_SURNAME);
                int transferLocationIndex = cursor.getColumnIndex(COLUMN_TRANSFER_LOCATION);
                int transferPriceIndex = cursor.getColumnIndex(COLUMN_TRANSFER_PRICE);
                int characterIndex = cursor.getColumnIndex(COLUMN_CHARACTER);

                if (idIndex != -1 && nameIndex != -1 && surnameIndex != -1 && phoneNumberIndex != -1 && transferDateIndex != -1 && nameSurnameIndex != -1 && transferLocationIndex != -1 && transferPriceIndex != -1 && characterIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String surname = cursor.getString(surnameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex);
                    String transferDate = cursor.getString(transferDateIndex);
                    String nameSurname = cursor.getString(nameSurnameIndex);
                    String transferLocation = cursor.getString(transferLocationIndex);
                    String transferPrice = cursor.getString(transferPriceIndex);
                    String character = cursor.getString(characterIndex);

                    Transfer transfer = new Transfer(name, surname, phoneNumber, transferDate, nameSurname, transferLocation, transferPrice, character);
                    transfer.setId(id);
                    transfers.add(transfer);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transfers;
    }

    public void deleteTransfer(Transfer transfer) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(transfer.getId()) };
        db.delete(TABLE_TRANSFERS, whereClause, whereArgs);
        db.close();
    }

    public boolean doesDateExist(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_TRANSFERS + " WHERE " + COLUMN_TRANSFER_DATE + " = ?", new String[]{date});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public List<Transfer> searchTransfers(String query) {
        List<Transfer> transfers = new ArrayList<>();
        String wildcardQuery = "%" + query + "%";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSFERS +
                        " WHERE " + COLUMN_NAME + " LIKE ?" +
                        " OR " + COLUMN_SURNAME + " LIKE ?" +
                        " OR " + COLUMN_PHONE_NUMBER + " LIKE ?" +
                        " OR " + COLUMN_TRANSFER_DATE + " LIKE ?" +
                        " OR " + COLUMN_NAME_SURNAME + " LIKE ?" +
                        " OR " + COLUMN_TRANSFER_LOCATION + " LIKE ?" +
                        " OR " + COLUMN_TRANSFER_PRICE + " LIKE ?" +
                        " OR " + COLUMN_CHARACTER + " LIKE ?",
                new String[]{wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int surnameIndex = cursor.getColumnIndex(COLUMN_SURNAME);
                int phoneNumberIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);
                int transferDateIndex = cursor.getColumnIndex(COLUMN_TRANSFER_DATE);
                int nameSurnameIndex = cursor.getColumnIndex(COLUMN_NAME_SURNAME);
                int transferLocationIndex = cursor.getColumnIndex(COLUMN_TRANSFER_LOCATION);
                int transferPriceIndex = cursor.getColumnIndex(COLUMN_TRANSFER_PRICE);
                int characterIndex = cursor.getColumnIndex(COLUMN_CHARACTER);

                if (idIndex != -1 && nameIndex != -1 && surnameIndex != -1 && phoneNumberIndex != -1 && transferDateIndex != -1 && nameSurnameIndex != -1 && transferLocationIndex != -1 && transferPriceIndex != -1 && characterIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    String surname = cursor.getString(surnameIndex);
                    String phoneNumber = cursor.getString(phoneNumberIndex);
                    String transferDate = cursor.getString(transferDateIndex);
                    String nameSurname = cursor.getString(nameSurnameIndex);
                    String transferLocation = cursor.getString(transferLocationIndex);
                    String transferPrice = cursor.getString(transferPriceIndex);
                    String character = cursor.getString(characterIndex);

                    Transfer transfer = new Transfer(name, surname, phoneNumber, transferDate, nameSurname, transferLocation, transferPrice, character);
                    transfer.setId(id);
                    transfers.add(transfer);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return transfers;
    }

    public void deleteAllTransfers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSFERS, null, null);
        db.close();
    }



}

