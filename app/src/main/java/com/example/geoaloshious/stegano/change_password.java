package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class change_password extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    Switch stegano=null;
    boolean swstate=false;
    RelativeLayout rv_round;
    Button bt_check,bt_save;
    EditText et_current,et_new;
    TextView tv_change_password;
    DBConnection db = new DBConnection(change_password.this);
    String currentp,newp,pass,uname;
    RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Adapter3 adp;
    List<Beanclass3> b;
    int a[]=new int[10];
    int ld=0,s=0,guessed=0,i1,pos,flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        rv_sample=(RecyclerView)findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter3(this);
        b=new ArrayList<>();
        stegano=(Switch)findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round=(RelativeLayout)findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_check=(Button)findViewById(R.id.bt_check);
        bt_check.setOnClickListener(this);
        bt_save=(Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        et_current=(EditText)findViewById(R.id.et_current);
        et_new=(EditText)findViewById(R.id.et_new);
        et_new.setFocusable(false);
        tv_change_password=(TextView)findViewById(R.id.tv_change_password);
        tv_change_password.setVisibility(View.VISIBLE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        swstate = b;
        if(swstate)
        {
            rv_round.setVisibility(View.VISIBLE);
            tv_change_password.setVisibility(View.GONE);
            Toast.makeText(change_password.this,"Security turned on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            rv_round.setVisibility(View.GONE);
            tv_change_password.setVisibility(View.VISIBLE);
            Toast.makeText(change_password.this,"Security turned off", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.bt_check:
                currentp=et_current.getText().toString();
                String  sh_name ="MYDATA";
                SharedPreferences sh=getSharedPreferences(sh_name, Context.MODE_PRIVATE);
                uname = sh.getString("key1",null);
                db.openConnection();
                String query1= "select * from tbl_stegno where uname='"+uname+"'";
                Cursor cursor = db.selectData(query1);
                if(cursor!=null)
                {
                    if(cursor.moveToNext())
                    {
                        pass=cursor.getString(7);
                    }
                }
                db.closeConnection();
                if(swstate)
                {
                    String currentp1=convrt(currentp);
                    if(currentp1.equals(pass))
                    {
                        et_new.setFocusableInTouchMode(true);
                        et_new.requestFocus();
                        et_current.setFocusable(false);
                    }
                    else
                    {
                        Toast.makeText(change_password.this,"Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(currentp.equals(pass))
                    {
                        et_new.setFocusableInTouchMode(true);
                        et_new.requestFocus();
                        et_current.setFocusable(false);
                    }
                    else
                    {
                        Toast.makeText(change_password.this,"Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.bt_save:
                newp=et_new.getText().toString();
                if(swstate)
                {
                    String newp2=convrt(newp);
                    db.openConnection();
                    String query2="update tbl_stegno set password='"+newp2+"' where uname='"+uname+"'";
                    db.insertData(query2);
                    db.closeConnection();
                    Toast.makeText(change_password.this,"Password Changed", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(change_password.this, home.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    db.openConnection();
                    String query2="update tbl_stegno set password='"+newp+"' where uname='"+uname+"'";
                    db.insertData(query2);
                    db.closeConnection();
                    Toast.makeText(change_password.this,"Password Changed", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(change_password.this, home.class);
                    startActivity(i1);
                    finish();
                }
                break;
        }
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
                adp = new Adapter3(this);
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
        Intent i2 = new Intent(change_password.this,home.class);
        startActivity(i2);
        finish();
    }
}
