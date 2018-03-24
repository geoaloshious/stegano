package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class edit_profile extends AppCompatActivity implements View.OnClickListener
{
    private String gender="Male";
    private EditText et_name;
    private EditText et_dob;
    private EditText et_email;
    private EditText et_phone;
    private final DBConnection db = new DBConnection(edit_profile.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button bt_save = findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        RadioButton rd_male = findViewById(R.id.rd_male);
        rd_male.setChecked(true);
        Button bt_cancel = findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        et_name= findViewById(R.id.et_name);
        et_dob= findViewById(R.id.et_dob);
        et_email= findViewById(R.id.et_email);
        et_phone= findViewById(R.id.et_phone);
        RadioGroup rg_gender = findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                if(i==R.id.rd_male)
                {
                    gender="Male";
                }
                else if (i==R.id.rd_female)
                {
                    gender="Female";
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bt_cancel:
                Intent i6 = new Intent(edit_profile.this, home.class);
                startActivity(i6);
                finish();
                break;
            case R.id.bt_save:
                String  sh_name ="MYDATA";
                SharedPreferences sh=getSharedPreferences(sh_name, Context.MODE_PRIVATE);
                String uname = sh.getString("key1", null);
                String name = et_name.getText().toString();
                String dob = et_dob.getText().toString();
                String phone = et_phone.getText().toString();
                String email = et_email.getText().toString();
                int empty_name;
                if(name.matches(""))
                {
                    et_name.setText("");
                    et_name.setError("Enter name");
                    empty_name =1;
                }
                else
                {
                    empty_name =0;
                }
                int empty_phone;
                if(phone.matches(""))
                {
                    et_phone.setText("");
                    et_phone.setError("Enter phone no.");
                    empty_phone =1;
                }
                else
                {
                    empty_phone =0;
                }
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                int error_email;
                if (email.matches(emailPattern))
                {
                    error_email =0;
                }
                else
                {
                    et_email.setText("");
                    et_email.setError("Invalid email");
                    error_email =1;
                }
                String datePattern = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9][0-9][0-9])$";
                int error_dob;
                if (dob.matches(datePattern))
                {
                    error_dob =0;
                }
                else
                {
                    et_dob.setText("");
                    et_dob.setError("Invalid format");
                    error_dob =1;
                }
                if((error_email ==0)&&(empty_name ==0)&&(error_dob ==0)&&(empty_phone ==0))
                {
                    db.openConnection();
                    String query2 = "update tbl_stegno set name='" + name + "',dob='" + dob + "',phone='" + phone + "',email='" + email + "',gender='" + gender + "' where uname='" + uname + "'";
                    db.insertData(query2);
                    db.closeConnection();
                    Toast.makeText(edit_profile.this, "Changes saved", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(edit_profile.this, home.class);
                    startActivity(i1);
                    finish();
                }
                break;
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
