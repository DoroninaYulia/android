package com.example.yulia.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yulia.myapplication.myclass.User;

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
                COLUMN_SKILLS + " TEXT);";

        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_LASTNAME, user.getLastName());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PHONENUMBER, user.getPhoneNumber());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_BIRTHDAY, user.getBirthDay());
        cv.put(COLUMN_SKILLS, user.getSkills());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void updateUser(User user) {
        String querySelect = "SELECT *FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + user.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySelect, null);

        if (cursor.moveToFirst()) {
            String queryUpdate = "UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_NAME + " = \"" + user.getName() + "\", " +
                    COLUMN_LASTNAME + " = \"" + user.getLastName() + "\", " +
                    COLUMN_EMAIL + " = \"" + user.getEmail() + "\", " +
                    COLUMN_PHONENUMBER + " = \"" + user.getPhoneNumber() + "\", " +
                    COLUMN_GENDER + " = \"" + user.getGender() + "\", " +
                    COLUMN_BIRTHDAY + " = \"" + user.getBirthDay() + "\", " +
                    COLUMN_SKILLS + " = \"" + user.getSkills() + "\" " +
                    "WHERE " + COLUMN_ID + " = " + user.getId();

            db.execSQL(queryUpdate);
        }

        db.close();
    }

    public void deleteAllUser() {
        String queryDelete = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryDelete);
        db.close();
    }

    public User findUser(Integer id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = \"" + id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();

        if (cursor.moveToFirst()) {
            user.setName(cursor.getString(1));
            user.setLastName(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPhoneNumber(cursor.getString(4));
            user.setGender(cursor.getString(5));
            user.setBirthDay(cursor.getString(6));
            user.setSkills(cursor.getString(7));
            cursor.close();
        } else {
            user = null;
        }

        return user;
    }


}
