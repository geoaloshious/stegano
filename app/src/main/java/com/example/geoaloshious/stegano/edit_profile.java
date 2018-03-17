package com.example.geoaloshious.stegano;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class edit_profile extends AppCompatActivity implements View.OnClickListener
{
    Button bt_save;
    int count=0;
    RadioGroup rg_gender;
    String name=null,dob=null,gender=null,email=null,phone=null,uname=null;
    EditText et_name,et_dob,et_email,et_phone;
    int flag=0;
    DBConnection db = new DBConnection(edit_profile.this);
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        bt_save=(Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        et_name=(EditText)findViewById(R.id.et_name);
        et_dob=(EditText)findViewById(R.id.et_dob);
        et_email=(EditText)findViewById(R.id.et_email);
        et_phone=(EditText)findViewById(R.id.et_phone);
        rg_gender=(RadioGroup)findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rd_male)
                {
                    gender="male";
                }
                else if (i==R.id.rd_female)
                {
                    gender="female";
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        String  sh_name ="MYDATA";
        SharedPreferences sh=getSharedPreferences(sh_name, Context.MODE_PRIVATE);
        uname = sh.getString("key1",null);
        name=et_name.getText().toString();
        dob=et_dob.getText().toString();
        phone=et_phone.getText().toString();
        email=et_email.getText().toString();
        count=0;
        if(name.isEmpty())
        {
            count++;
            db.openConnection();
            String query1= "select * from tbl_stegno where uname='"+uname+"'";
            Cursor cursor1 = db.selectData(query1);
            if(cursor1!=null)
            {
                if (cursor1.moveToNext())
                {
                    name = cursor1.getString(1);
                }
            }
            db.closeConnection();
        }
        if(dob.isEmpty())
        {
            count++;
            db.openConnection();
            String query2= "select * from tbl_stegno where uname='"+uname+"'";
            Cursor cursor2 = db.selectData(query2);
            if(cursor2!=null)
            {
                if (cursor2.moveToNext())
                {
                    dob=cursor2.getString(3);
                }
            }
            db.closeConnection();
        }
        if(phone.isEmpty())
        {
            count++;
            db.openConnection();
            String query3= "select * from tbl_stegno where uname='"+uname+"'";
            Cursor cursor3 = db.selectData(query3);
            if(cursor3!=null)
            {
                if (cursor3.moveToNext())
                {
                    phone=cursor3.getString(4);
                }
            }
            db.closeConnection();
        }
        if(email.isEmpty())
        {
            count++;
            db.openConnection();
            String query4= "select * from tbl_stegno where uname='"+uname+"'";
            Cursor cursor4 = db.selectData(query4);
            if(cursor4!=null)
            {
                if (cursor4.moveToNext())
                {
                    email=cursor4.getString(5);
                }
            }
            db.closeConnection();
        }
        else
        {
            if (email.matches(emailPattern))
            {
                flag=0;
            }
            else
            {
                et_email.setText("");
                et_email.setError("Invalid email");
                flag++;
            }
        }
        if(gender==null)
        {
            count++;
            db.openConnection();
            String query5= "select * from tbl_stegno where uname='"+uname+"'";
            Cursor cursor5 = db.selectData(query5);
            if(cursor5!=null)
            {
                if (cursor5.moveToNext())
                {
                    gender=cursor5.getString(2);
                }
            }
            db.closeConnection();
        }
        if(flag==0)
        {
            db.openConnection();
            String query2 = "update tbl_stegno set name='" + name + "',dob='" + dob + "',phone='" + phone + "',email='" + email + "',gender='" + gender + "' where uname='" + uname + "'";
            db.insertData(query2);
            db.closeConnection();
            if (count == 5) {
                Toast.makeText(edit_profile.this, "No changes made", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(edit_profile.this, "Changes saved", Toast.LENGTH_SHORT).show();
            }
            Intent i1 = new Intent(edit_profile.this, home.class);
            startActivity(i1);
            finish();
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent i2 = new Intent(edit_profile.this, home.class);
        startActivity(i2);
        finish();
    }
}
