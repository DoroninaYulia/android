package com.example.yulia.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static com.example.yulia.myapplication.database.DB.*;
import static com.example.yulia.myapplication.MainActivity.userDB;

public class UserInfoActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        SQLiteDatabase db = userDB.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_LASTNAME}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Database empty", Toast.LENGTH_SHORT).show();
            }else{
                String[] from = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_LASTNAME};
                int[] to = new int[]{R.id.tvId, R.id.tvName, R.id.tvLastName};
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to, 1);

                lv = (ListView) findViewById(R.id.idLvUsers);
                lv.setAdapter(adapter);
                lv.setTextFilterEnabled(true);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra(KeyOfActivity.KEY_1, (int)id);
                        setResult(KeyOfActivity.RESULT_CODE, intent);
                        finish();
                    }
                });
            }
        }

    }

}
