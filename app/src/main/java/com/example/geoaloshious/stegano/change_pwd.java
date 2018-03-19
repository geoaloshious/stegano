package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class change_pwd extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    Switch stegano=null;
    boolean swstate=false;
    RelativeLayout rv_round;
    Button bt_ok_old,bt_ok_new,bt_save,bt_cancel;
    EditText et_old,et_new,et_renew;
    TextView tv_change_pwd;
    DBConnection db = new DBConnection(change_pwd.this);
    String oldpwd,newpwd,renewpwd,db_pwd,uname,oldpwd_original,newpwd_original,renewpwd_original;
    RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Adapter1 adp;
    List<Beanclass1> b;
    int a[]=new int[10];
    int i,i1,pos,flag=0,empty_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        rv_sample=(RecyclerView)findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter1(this);
        b=new ArrayList<>();
        stegano=(Switch)findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round=(RelativeLayout)findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_ok_old=(Button)findViewById(R.id.bt_ok_old);
        bt_ok_old.setOnClickListener(this);
        bt_ok_new=(Button)findViewById(R.id.bt_ok_new);
        bt_ok_new.setOnClickListener(this);
        bt_ok_new.setEnabled(false);
        bt_save=(Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        bt_save.setEnabled(false);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        et_new=(EditText)findViewById(R.id.et_new);
        et_new.setFocusable(false);
        et_renew=(EditText)findViewById(R.id.et_renew);
        et_renew.setFocusable(false);
        et_old=(EditText)findViewById(R.id.et_old);
        et_old.setFocusable(true);
        tv_change_pwd=(TextView)findViewById(R.id.tv_change_pwd);
        tv_change_pwd.setVisibility(View.VISIBLE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        swstate = b;
        if(swstate)
        {
            rv_round.setVisibility(View.VISIBLE);
            Toast.makeText(change_pwd.this,"Security turned on", Toast.LENGTH_SHORT).show();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                tv_change_pwd.setVisibility(View.VISIBLE);
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                tv_change_pwd.setVisibility(View.GONE);
            }
        }
        else
        {
            rv_round.setVisibility(View.GONE);
            Toast.makeText(change_pwd.this,"Security turned off", Toast.LENGTH_SHORT).show();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                tv_change_pwd.setVisibility(View.VISIBLE);
            }
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                tv_change_pwd.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.bt_cancel:
                Intent i6 = new Intent(change_pwd.this, home.class);
                startActivity(i6);
                finish();
                break;
            case R.id.bt_ok_old:
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
                        db_pwd=cursor.getString(7);
                    }
                }
                db.closeConnection();
                oldpwd=et_old.getText().toString();
                if(oldpwd.matches(""))
                {
                    empty_pwd=1;
                    et_old.setError("Enter password");
                }
                else
                {
                    empty_pwd=0;
                    if(swstate)
                    {
                        oldpwd_original = convrt(oldpwd);
                        oldpwd=oldpwd_original;
                    }
                }
                if(empty_pwd==0)
                {
                    if(oldpwd.equals(db_pwd))
                    {
                        bt_ok_old.setEnabled(false);
                        bt_ok_new.setEnabled(true);
                        et_new.setFocusableInTouchMode(true);
                        et_new.requestFocus();
                        et_old.setFocusable(false);
                    }
                    else
                    {
                        et_old.setError("Incorrect Password");
                        et_old.setText("");
                    }
                }
                break;
            case R.id.bt_ok_new:
                newpwd=et_new.getText().toString();
                if(newpwd.matches(""))
                {
                    et_new.setError("Enter password");
                }
                else
                {
                    if(swstate)
                    {
                        newpwd_original = convrt(newpwd);
                        newpwd=newpwd_original;
                    }
                    et_renew.setFocusableInTouchMode(true);
                    et_renew.requestFocus();
                    et_new.setFocusable(false);
                    bt_ok_new.setEnabled(false);
                    bt_save.setEnabled(true);
                }
                break;
            case R.id.bt_save:
                renewpwd=et_renew.getText().toString();
                if(swstate)
                {
                    renewpwd_original=convrt(renewpwd);
                    renewpwd=renewpwd_original;
                }
                if(newpwd.equals(renewpwd))
                {
                    db.openConnection();
                    String query2="update tbl_stegno set password='"+newpwd+"' where uname='"+uname+"'";
                    db.insertData(query2);
                    db.closeConnection();
                    Toast.makeText(change_pwd.this,"Password Changed", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(change_pwd.this, home.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    et_renew.setError("Passwords didn't match");
                    et_renew.setText("");
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
    public String convrt(String otp)
    {
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
        String otp_converted=String.valueOf(strNum);
        return otp_converted;
    }
    @Override
    public void onBackPressed()
    {
        Intent i2 = new Intent(change_pwd.this,home.class);
        startActivity(i2);
        finish();
    }
}

