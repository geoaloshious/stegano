package com.example.geoaloshious.stegano;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class reg extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    Switch stegano=null;
    boolean swstate=false;
    RelativeLayout rv_round;
    Button bt_ok,bt_signup;
    TextView tv_signup;
    RadioGroup rg_gender;
    EditText et_name,et_dob,et_phone,et_email,et_uname,et_pwd,et_repwd;
    String name,dob,gender,email,uname,pwd,repwd,phone;
    DBConnection db = new DBConnection(reg.this);
    RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Adapter2 adp;
    List<Beanclass2> b;
    int a[]=new int[10];
    int ld=0,s=0,guessed=0,i1,pos,flag=0,flg,err;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        rv_sample=(RecyclerView)findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter2(this);
        b=new ArrayList<>();
        stegano=(Switch)findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round=(RelativeLayout)findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
        bt_signup=(Button)findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(this);
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        et_repwd=(EditText)findViewById(R.id.et_repwd);
        et_repwd.setFocusable(false);
        et_name=(EditText)findViewById(R.id.et_name);
        et_dob=(EditText)findViewById(R.id.et_dob);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email=(EditText)findViewById(R.id.et_email);
        et_uname=(EditText)findViewById(R.id.et_uname);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
        tv_signup.setVisibility(View.VISIBLE);
        rg_gender=(RadioGroup)findViewById(R.id.rg_gender);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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
            if((flag==0)&&(swstate==true))
            {
                adp = new Adapter2(this);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        swstate = b;
        if(swstate)
        {
            rv_round.setVisibility(View.VISIBLE);
            tv_signup.setVisibility(View.GONE);
            Toast.makeText(reg.this,"Security turned on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            rv_round.setVisibility(View.GONE);
            tv_signup.setVisibility(View.VISIBLE);
            Toast.makeText(reg.this,"Security turned off", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bt_ok:
                pwd= et_pwd.getText().toString();
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
                break;
            case R.id.bt_signup:
                name=et_name.getText().toString();
                dob=et_dob.getText().toString();
                phone=et_phone.getText().toString();
                email=et_email.getText().toString();
                uname=et_uname.getText().toString();
                repwd=et_repwd.getText().toString();
                if (email.matches(emailPattern))
                {
                    err=0;
                }
                else
                {
                    et_email.setText("");
                    et_email.setError("Invalid email");
                    err++;
                }
                String repwd2 = convrt(repwd);
                db.openConnection();
                String query1= "select * from tbl_stegno where uname='"+uname+"'";
                Cursor cursor = db.selectData(query1);
                if(cursor.moveToNext())
                {
                    flg=0;
                }
                else
                {
                    flg=1;
                }
                db.closeConnection();
                if(flg==1)
                {
                    if(err==0)
                    {
                        if (swstate) {
                            if (pwd.equals(repwd2)) {
                                db.openConnection();
                                String query = "insert into tbl_stegno(name,gender,dob,phone,email,uname,password) values ('" + name + "','" + gender + "','" + dob + "','" + phone + "','" + email + "','" + uname + "','" + pwd + "')";
                                db.insertData(query);
                                db.closeConnection();
                                Toast.makeText(reg.this, "Registered", Toast.LENGTH_SHORT).show();
                                Intent i1 = new Intent(reg.this, login.class);
                                startActivity(i1);
                                finish();
                            } else {
                                Toast.makeText(reg.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (pwd.equals(repwd)) {
                                db.openConnection();
                                String query = "insert into tbl_stegno(name,gender,dob,phone,email,uname,password) values ('" + name + "','" + gender + "','" + dob + "','" + phone + "','" + email + "','" + uname + "','" + pwd + "')";
                                db.insertData(query);
                                db.closeConnection();
                                Toast.makeText(reg.this, "Registered", Toast.LENGTH_SHORT).show();
                                Intent i1 = new Intent(reg.this, login.class);
                                startActivity(i1);
                                finish();
                            } else {
                                Toast.makeText(reg.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
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
    public String convrt(String otp)
    {
        s=0;ld=0;
        int i=0,i1;
        a=adp.b;
        int a2[]=new int[otp.length()];
        for(i=0;i<otp.length();i++)
        {
            int j=Character.digit(otp.charAt(i),10);
            i1=0;
            while(i1<a.length)
            {
                if(a[i1]==j)
                {
                    pos=i1+1;
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
        String guess=String.valueOf(strNum);
        return guess;
    }
    @Override
    public void onBackPressed()
    {
        Intent i2 = new Intent(reg.this,login.class);
        startActivity(i2);
        finish();
    }
}
