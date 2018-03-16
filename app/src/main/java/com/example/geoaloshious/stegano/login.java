package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    Switch stegano=null;
    boolean swstate=false;
    RelativeLayout rv_round;
    Button bt_login,bt_signup;
    EditText et_uname,et_pwd;
    String uname,pwd,pass,geo="geo",g="g",dpwd="1234";
    int flag=0;
    DBConnection db = new DBConnection(login.this);
    RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Adapter1 adp;
    List<Beanclass1> b;
    int a[]=new int[10];
    int ld=0,s=0,i1,pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rv_sample=(RecyclerView)findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter1(this);
        b=new ArrayList<>();
        stegano=(Switch)findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round=(RelativeLayout)findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_signup=(Button)findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(this);
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        et_uname=(EditText)findViewById(R.id.et_uname);
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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
            Toast.makeText(login.this,"Security turned on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            rv_round.setVisibility(View.GONE);
            Toast.makeText(login.this,"Security turned off", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bt_login:
                uname=et_uname.getText().toString();
                pwd=et_pwd.getText().toString();
                db.openConnection();
                String query1= "select * from tbl_stegno where uname='"+uname+"'";
                Cursor cursor = db.selectData(query1);
                if(cursor!=null)
                {
                    if(cursor.moveToNext())
                    {
                        pass=cursor.getString(7);
                        flag=1;
                    }
                }
                else
                {
                    flag=0;
                }
                db.closeConnection();
                if (flag == 1)
                {
                    if(swstate)
                    {
                        String pwd2 = convrt(pwd);
                        if (pwd2.equals(pass))
                        {
                            String  sh_name = "MYDATA";
                            SharedPreferences   sh= getSharedPreferences(sh_name , Context.MODE_PRIVATE);
                            SharedPreferences.Editor   editor = sh.edit();
                            editor.putString("key1",uname);
                            editor.commit();
                            Intent i1 = new Intent(login.this, home.class);
                            startActivity(i1);
                            Toast.makeText(login.this, "Logged In", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            et_pwd.setError("Invalid Password");
                            et_pwd.setText("");
                        }
                    }
                    else
                    {
                        if(pwd.equals(pass))
                        {
                            Toast.makeText(login.this,"Logged in", Toast.LENGTH_SHORT).show();
                            String  sh_name = "MYDATA";
                            SharedPreferences   sh= getSharedPreferences(sh_name , Context.MODE_PRIVATE);
                            SharedPreferences.Editor   editor = sh.edit();
                            editor.putString("key1",uname);
                            editor.commit();
                            Intent i3 = new Intent(login.this, home.class);
                            startActivity(i3);
                            finish();
                        }
                        else
                        {
                            et_pwd.setError("Invalid Password");
                            et_pwd.setText("");
                        }
                    }
                }
                else
                {
                    et_uname.setError("Invalid Username");
                    et_uname.setText("");
                    et_pwd.setText("");
                }
                break;
            case R.id.bt_signup:
                Intent i1 = new Intent(login.this, reg.class);
                startActivity(i1);
                finish();
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
        for(int num :  a2)
        {
            strNum.append(num);
        }
        String guess=String.valueOf(strNum);
        return guess;
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_delete).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("no", null).show();
    }
}
