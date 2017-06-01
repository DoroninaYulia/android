package com.example.yulia.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yulia.myapplication.myclass.ConvertImage;
import com.example.yulia.myapplication.myclass.User;

import static com.example.yulia.myapplication.KeyOfActivity.*;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userDB";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONENUMBER = "phonenumber";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_SKILLS = "skills";
    public static final String COLUMN_IMAGE = "image";
    public static final String TABLE_NAME = "userTable";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT," +
                COLUMN_LASTNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PHONENUMBER + " INTEGER," +
                COLUMN_GENDER + " TEXT," +
                COLUMN_BIRTHDAY + " TEXT," +
                COLUMN_SKILLS + " TEXT," +
                COLUMN_IMAGE + " BLOB);";

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void action(User user, int key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_LASTNAME, user.getLastName());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PHONENUMBER, user.getPhoneNumber());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_BIRTHDAY, user.getBirthDay());
        cv.put(COLUMN_SKILLS, user.getSkills());
        cv.put(COLUMN_IMAGE, ConvertImage.getBytes(user.getBitmap()));

        switch (key) {
            case KEY_ADD:
                db.insert(TABLE_NAME, null, cv);
                break;
            case KEY_UPDATE:
                db.update(TABLE_NAME, cv, COLUMN_ID + " = " + user.getId(), null);
                break;
        }
        db.close();
    }

    public boolean deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            String queryDelete = "DELETE FROM " + TABLE_NAME;
            db.execSQL(queryDelete);
            db.close();
            return true;
        }

        db.close();
        return false;
    }

    public User findUser(Integer id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();

        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONENUMBER)));
            user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
            user.setBirthDay(cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)));
            user.setSkills(cursor.getString(cursor.getColumnIndex(COLUMN_SKILLS)));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            user.setBitmap(ConvertImage.getImage(blob));
            cursor.close();
        } else {
            user = null;
        }
        return user;
    }


}
