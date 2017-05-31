package com.example.yulia.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yulia.myapplication.database.DB;
import com.example.yulia.myapplication.myclass.MyDatePicker;
import com.example.yulia.myapplication.myclass.User;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText firstName, lastName, email, phone, date;
    private Button btn1, btn2, btn3;
    private RadioGroup rgGender;
    private RadioButton female, male;
    private Spinner spSkills;
    private ImageView imageView;

    private DialogFragment dateDialog;
    private Intent mIntent;
    private User user;
    public static DB userDB;

    private String strName, strLastName, strEmail, strPhone, selectedSp, strDate, selectedRb;
    private Integer userID, idRadioButton;
    private BitmapDrawable bmd;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByID();

        dateDialog = new MyDatePicker();
        mIntent = new Intent(this, UserInfoActivity.class);
        userDB = new DB(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(Intent.ACTION_GET_CONTENT);
                intentImage.setType("image/*");
                startActivityForResult(intentImage, KeyOfActivity.SELECT_PICTURE);
            }
        });

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        date.setOnFocusChangeListener(this);
    }

    private void findViewByID() {
        firstName = (EditText) findViewById(R.id.etFirstName);
        lastName = (EditText) findViewById(R.id.etLastName);
        email = (EditText) findViewById(R.id.etEmail);
        phone = (EditText) findViewById(R.id.etPhone);
        date = (EditText) findViewById(R.id.etDate);
        btn1 = (Button) findViewById(R.id.btnShow);
        btn2 = (Button) findViewById(R.id.btnAdd);
        btn3 = (Button) findViewById(R.id.btnEdit);
        rgGender = (RadioGroup) findViewById(R.id.radioGroup);
        female = (RadioButton) findViewById(R.id.rbFemale);
        male = (RadioButton) findViewById(R.id.rbMale);
        spSkills = (Spinner) findViewById(R.id.spinner);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        getValues();

        if (checkEditText(firstName, lastName, email, phone, date)
                && idRadioButton != -1 && spSkills.getSelectedItemPosition() > 0) {
            switch (v.getId()) {
                case R.id.btnEdit:
                    updateUser();
                    break;
                case R.id.btnAdd:
                    addUser();
                    break;
            }
            clearForm(firstName, lastName, email, phone, date);
        } else if (v.getId() == R.id.btnShow){
            startActivityForResult(mIntent, KeyOfActivity.RESULT_CODE);
        }
        else {
            Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUser(){
        try{
            user = new User(userID, strName, strLastName, strEmail, strPhone, selectedRb, strDate, selectedSp, photo);
            userDB.updateUser(user);
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.GONE);
            Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Unable to update", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUser(){
        try{
            user = new User(strName, strLastName, strEmail, strPhone, selectedRb, strDate, selectedSp, photo);
            userDB.addUser(user);
            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Unable to add", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            dateDialog.show(getFragmentManager(), "datePicker");
        }
    }

    private boolean checkEditText(EditText... editTexts) {
        for (EditText e : editTexts) {
            if (e.getText().toString().trim().length() == 0) return false;
        }
        return true;
    }

    public void getValues() {
        strName = firstName.getText().toString();
        strLastName = lastName.getText().toString();
        strEmail = email.getText().toString();
        strDate = date.getText().toString();
        selectedSp = spSkills.getSelectedItem().toString();
        strPhone = phone.getText().toString();

        idRadioButton = rgGender.getCheckedRadioButtonId();
        if (idRadioButton != -1) {
            View rb = rgGender.findViewById(idRadioButton);
            int radioId = rgGender.indexOfChild(rb);
            RadioButton radioButton = (RadioButton) rgGender.getChildAt(radioId);
            selectedRb = (String) radioButton.getText();
        }

        bmd = ((BitmapDrawable) imageView.getDrawable());
        photo = bmd.getBitmap();
    }

    private void clearForm(EditText... editTexts) {
        for (EditText e : editTexts) {
            e.setText("");
        }

        rgGender.clearCheck();
        spSkills.setSelection(0);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case KeyOfActivity.SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    try{
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = true;
                        Uri uri = data.getData();
                        InputStream stream = getContentResolver().openInputStream(uri);
                        Bitmap selectImg = BitmapFactory.decodeStream(stream, null, options);
                        imageView.setImageBitmap(selectImg);
                    }catch(FileNotFoundException ex){
                        Toast.makeText(this, "Image was not found", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case KeyOfActivity.RESULT_CODE:
                if (data == null) return;
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.VISIBLE);

                userID = data.getExtras().getInt(KeyOfActivity.KEY_1);
                fillFields();
                break;
        }
    }

    private void fillFields() {
        User user = userDB.findUser(userID);
        if(user == null){
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
            return;
        }

        firstName.setText(user.getName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
        date.setText(user.getBirthDay());
        if (user.getGender().equals(female.getText())) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        ArrayAdapter adapter = (ArrayAdapter) spSkills.getAdapter();
        int posAdapter = adapter.getPosition(user.getSkills());
        spSkills.setSelection(posAdapter);
        imageView.setImageBitmap(user.getBitmap());
    }


}
