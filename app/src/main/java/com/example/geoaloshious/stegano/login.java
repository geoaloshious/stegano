package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SensorEventListener {
    private boolean swstate=false;
    private RelativeLayout rv_round;
    private EditText et_uname;
    private EditText et_pwd;
    private String pass;
    private int flag=0;
    private final DBConnection db = new DBConnection(login.this);
    private RecyclerView rv_sample;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Adapter1 adp;
    private int pos;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rv_sample= findViewById(R.id.rv_sample);
        rv_sample.setLayoutManager(new GridLayoutManager(this, 3));
        rv_sample.setVisibility(View.INVISIBLE);
        adp=new Adapter1(this);
        List<Beanclass1> b = new ArrayList<>();
        Switch stegano = findViewById(R.id.stegano);
        stegano.setOnCheckedChangeListener(this);
        rv_round= findViewById(R.id.rv_round);
        rv_round.setVisibility(View.GONE);
        Button bt_signup = findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(this);
        Button bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        et_uname= findViewById(R.id.et_uname);
        et_pwd= findViewById(R.id.et_pwd);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert mSensorManager != null;
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
                String uname = et_uname.getText().toString();
                String pwd = et_pwd.getText().toString();
                String adminp = "0000";
                String adminu = "admin";
                if((uname.equals(adminu))&&(pwd.equals(adminp)))
                {
                    Intent i2 = new Intent(login.this, admin.class);
                    startActivity(i2);
                    finish();
                }
                else
                {
                    if(swstate)
                    {
                        pwd = convrt(pwd);
                    }
                    db.openConnection();
                    String query1= "select * from tbl_stegno where uname='"+ uname +"'";
                    Cursor cursor = db.selectData(query1);
                    int user_exists;
                    if(cursor.moveToNext())
                    {
                        pass=cursor.getString(7);
                        user_exists =1;
                    }
                    else
                    {
                        user_exists =0;
                    }
                    db.closeConnection();
                    if (user_exists == 1)
                    {
                        if (pwd.equals(pass))
                        {
                            String  sh_name = "MYDATA";
                            SharedPreferences   sh= getSharedPreferences(sh_name , Context.MODE_PRIVATE);
                            SharedPreferences.Editor   editor = sh.edit();
                            editor.putString("key1", uname);
                            editor.apply();
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
                        et_uname.setError("Invalid Username");
                        et_uname.setText("");
                        et_pwd.setText("");
                    }
                }
                break;
            case R.id.bt_signup:
                Intent i1 = new Intent(login.this, reg.class);
                startActivity(i1);
                finish();
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
        for(int num :  a2)
        {
            strNum.append(num);
        }
        return String.valueOf(strNum);
    }
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
