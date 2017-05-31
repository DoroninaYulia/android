package com.example.yulia.myapplication.myclass;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.example.yulia.myapplication.R;

import static com.example.yulia.myapplication.database.DB.*;
import static com.example.yulia.myapplication.myclass.ConvertImage.getImage;

public class MyViewBinder implements ViewBinder {
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        switch (view.getId()) {
            case R.id.tvId:
                TextView userID = (TextView) view;
                String strID = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                userID.setText(strID);
                break;
            case R.id.tvName:
                TextView userName = (TextView) view;
                String strName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                userName.setText(strName);
                break;
            case R.id.tvLastName:
                TextView userLastName = (TextView) view;
                String strLastName = cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME));
                userLastName.setText(strLastName);
                break;
            case R.id.imageViewUser:
                ImageView userImage = (ImageView) view;
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                if(bytes != null){
                    userImage.setImageBitmap(getImage(bytes));
                }
                break;
        }

        return true;
    }
}
