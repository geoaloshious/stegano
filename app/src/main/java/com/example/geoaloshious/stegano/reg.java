package com.example.geoaloshious.stegano;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class reg extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    private boolean swstate=false;
    private RelativeLayout rv_round;
    private Button bt_ok;
    private TextView tv_signup;
    private EditText et_name;
    private EditText et_dob;
    private EditText et_phone;
    private EditText et_email;
    private EditText et_uname;
    private EditText et_pwd;
    private EditText et_repwd;
    private String gender="Male";
    private String pwd;
    private DBConnection db = new DBConnection(reg.this);
    private RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Adapter1 adp;
    private int pos;
    private int flag=0;
    private int user_exists;
    private int empty_pwd=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        rv_sample= findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter1(this);
        List<Beanclass1> b = new ArrayList<>();
        RadioButton rd_male = findViewById(R.id.rd_male);
        rd_male.setChecked(true);
        Switch stegano = findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round= findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_ok= findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
        Button bt_signup = findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(this);
        et_pwd= findViewById(R.id.et_pwd);
        et_repwd= findViewById(R.id.et_repwd);
        et_repwd.setFocusable(false);
        et_name= findViewById(R.id.et_name);
        et_dob= findViewById(R.id.et_dob);
        et_phone= findViewById(R.id.et_phone);
        et_email= findViewById(R.id.et_email);
        et_uname= findViewById(R.id.et_uname);
        tv_signup= findViewById(R.id.tv_signup);
        tv_signup.setVisibility(View.VISIBLE);
        RadioGroup rg_gender = findViewById(R.id.rg_gender);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
    public void onSensorChanged(SensorEvent event)
    {
        if (event.values[0] == 0)
        {
            if((flag==0)&&(swstate))
            {
                adp = new Adapter1(this);
                adp.rand();
                rv_sample.setAdapter(adp);
                rv_sample.setVisibility(View.VISIBLE);
                flag++;
            }
        }
        else
        {
            flag=0;
            rv_sample.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        swstate = b;
        if(swstate)
        {
            rv_round.setVisibility(View.VISIBLE);
            Toast.makeText(reg.this,"Security turned on", Toast.LENGTH_SHORT).show();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                tv_signup.setVisibility(View.VISIBLE);
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                tv_signup.setVisibility(View.GONE);
            }
        }
        else
        {
            rv_round.setVisibility(View.GONE);
            Toast.makeText(reg.this,"Security turned off", Toast.LENGTH_SHORT).show();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                tv_signup.setVisibility(View.VISIBLE);
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                tv_signup.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bt_ok:
                pwd= et_pwd.getText().toString();
                if(pwd.matches(""))
                {
                    empty_pwd=1;
                    et_pwd.setError("Enter password");
                }
                else
                {
                    empty_pwd=0;
                    String pwd1=pwd;
                    if(swstate)
                    {
                        String pwd2 = convrt(pwd);
                        pwd=pwd2;
                    }
                    else
                    {
                        pwd=pwd1;
                    }
                    et_repwd.setFocusableInTouchMode(true);
                    et_repwd.requestFocus();
                    et_pwd.setFocusable(false);
                    bt_ok.setEnabled(false);
                }
                break;
            case R.id.bt_signup:
                String name = et_name.getText().toString();
                String dob = et_dob.getText().toString();
                String phone = et_phone.getText().toString();
                String email = et_email.getText().toString();
                String uname = et_uname.getText().toString();
                String repwd = et_repwd.getText().toString();
                if(swstate)
                {
                    String repwd_original = convrt(repwd);
                    repwd = repwd_original;
                }
                int empty_name = 0;
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
                int empty_phone = 0;
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
                int error_email = 0;
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
                int error_dob = 0;
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
                int empty_uname = 0;
                if(uname.matches(""))
                {
                    et_uname.setText("");
                    et_uname.setError("Enter username");
                    empty_uname =1;
                }
                else
                {
                    empty_uname =0;
                    db.openConnection();
                    String query1= "select * from tbl_stegno where uname='"+ uname +"'";
                    Cursor cursor = db.selectData(query1);
                    if(cursor.moveToNext())
                    {
                        user_exists=1;
                    }
                    else
                    {
                        user_exists=0;
                    }
                    db.closeConnection();
                }
                if(user_exists==0)
                {
                    if((error_email ==0)&&(empty_name ==0)&&(error_dob ==0)&&(empty_phone ==0)&&(empty_uname ==0)&&(empty_pwd==0))
                    {
                        if (pwd.equals(repwd))
                        {
                            db.openConnection();
                            String query = "insert into tbl_stegno(name,gender,dob,phone,email,uname,password) values ('" + name + "','" + gender + "','" + dob + "','" + phone + "','" + email + "','" + uname + "','" + pwd + "')";
                            db.insertData(query);
                            db.closeConnection();
                            Toast.makeText(reg.this, "Registered", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(reg.this, login.class);
                            startActivity(i1);
                            finish();
                        }
                        else
                        {
                            et_repwd.setText("");
                            et_repwd.setError("Passwords didn't match");
                        }
                    }
                }
                else
                {
                    et_uname.setError("username already exists");
                    et_uname.setText("");
                }
                break;
        }
    }
    private String convrt(String otp)
    {
        int[] a = adp.a;
        int a2[]=new int[otp.length()];
        int i;
        for(i =0; i <otp.length(); i++)
        {
            int j=Character.digit(otp.charAt(i),10);
            int i1 = 0;
            while(i1 < a.length)
            {
                if(a[i1]==j)
                {
                    pos= i1 +1;
                    if (pos==10)
                        pos=0;
                }
                i1++;
            }
            a2[i]=pos;
        }
        StringBuilder strNum=new StringBuilder();
        for(int num : a2)
        {
            strNum.append(num);
        }
        String otp_converted=String.valueOf(strNum);
        return otp_converted;
    }
    @Override
    public void onBackPressed()
    {
        Intent i2 = new Intent(reg.this,login.class);
        startActivity(i2);
        finish();
    }
}
