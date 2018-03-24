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
import android.support.v7.app.AppCompatActivity;
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

public class change_pwd extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    private boolean swstate=false;
    private RelativeLayout rv_round;
    private Button bt_ok_old;
    private Button bt_ok_new;
    private Button bt_save;
    private EditText et_old;
    private EditText et_new;
    private EditText et_renew;
    private TextView tv_change_pwd;
    private final DBConnection db = new DBConnection(change_pwd.this);
    private String newpwd;
    private String db_pwd;
    private String uname;
    private RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Adapter1 adp;
    private int pos;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        rv_sample= findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter1(this);
        List<Beanclass1> b = new ArrayList<>();
        Switch stegano = findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round= findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        bt_ok_old= findViewById(R.id.bt_ok_old);
        bt_ok_old.setOnClickListener(this);
        bt_ok_new= findViewById(R.id.bt_ok_new);
        bt_ok_new.setOnClickListener(this);
        bt_ok_new.setEnabled(false);
        bt_save= findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        bt_save.setEnabled(false);
        Button bt_cancel = findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        et_new= findViewById(R.id.et_new);
        et_new.setFocusable(false);
        et_renew= findViewById(R.id.et_renew);
        et_renew.setFocusable(false);
        et_old= findViewById(R.id.et_old);
        et_old.setFocusable(true);
        tv_change_pwd= findViewById(R.id.tv_change_pwd);
        tv_change_pwd.setVisibility(View.VISIBLE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert mSensorManager != null;
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
                String oldpwd = et_old.getText().toString();
                int empty_pwd;
                if(oldpwd.matches(""))
                {
                    empty_pwd =1;
                    et_old.setError("Enter password");
                }
                else
                {
                    empty_pwd =0;
                    if(swstate)
                    {
                        oldpwd = convrt(oldpwd);
                    }
                }
                if(empty_pwd ==0)
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
                        newpwd= convrt(newpwd);
                    }
                    et_renew.setFocusableInTouchMode(true);
                    et_renew.requestFocus();
                    et_new.setFocusable(false);
                    bt_ok_new.setEnabled(false);
                    bt_save.setEnabled(true);
                }
                break;
            case R.id.bt_save:
                String renewpwd = et_renew.getText().toString();
                if(swstate)
                {
                    renewpwd = convrt(renewpwd);
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
        return String.valueOf(strNum);
    }
    @Override
    public void onBackPressed()
    {
        Intent i2 = new Intent(change_pwd.this,home.class);
        startActivity(i2);
        finish();
    }
}

