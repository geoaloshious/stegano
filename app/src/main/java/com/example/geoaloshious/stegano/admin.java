package com.example.geoaloshious.stegano;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class admin extends AppCompatActivity implements View.OnClickListener {
TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15,tv16,tv17,tv18,tv19,tv20,tv_msg,tv_uname,tv_password;
String uname,pass;
Button bt_back;
int i=1,k=1;
    DBConnection db = new DBConnection(admin.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);
        tv6=(TextView)findViewById(R.id.tv6);
        tv7=(TextView)findViewById(R.id.tv7);
        tv8=(TextView)findViewById(R.id.tv8);
        tv9=(TextView)findViewById(R.id.tv9);
        tv10=(TextView)findViewById(R.id.tv10);
        tv11=(TextView)findViewById(R.id.tv11);
        tv12=(TextView)findViewById(R.id.tv12);
        tv13=(TextView)findViewById(R.id.tv13);
        tv14=(TextView)findViewById(R.id.tv14);
        tv15=(TextView)findViewById(R.id.tv15);
        tv16=(TextView)findViewById(R.id.tv16);
        tv17=(TextView)findViewById(R.id.tv17);
        tv18=(TextView)findViewById(R.id.tv18);
        tv19=(TextView)findViewById(R.id.tv19);
        tv20=(TextView)findViewById(R.id.tv20);
        tv_msg=(TextView)findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        tv_uname=(TextView)findViewById(R.id.tv_uname);
        tv_uname.setVisibility(View.GONE);
        tv_password=(TextView)findViewById(R.id.tv_password);
        tv_password.setVisibility(View.GONE);
        bt_back=(Button)findViewById(R.id.bt_back);
        bt_back.setOnClickListener(this);
        db.openConnection();
        String query1= "select * from tbl_stegno";
        Cursor cursor = db.selectData(query1);
        if(cursor.moveToNext())
        {
            cursor.moveToPrevious();
            tv_uname.setVisibility(View.VISIBLE);
            tv_password.setVisibility(View.VISIBLE);
            while(cursor.moveToNext())
            {
                uname=cursor.getString(6);
                pass=cursor.getString(7);
                if(i==1)
                {
                    tv1.setText(uname);
                    tv11.setText(pass);
                }
                if(i==2)
                {
                    tv2.setText(uname);
                    tv12.setText(pass);
                }
                if(i==3)
                {
                    tv3.setText(uname);
                    tv13.setText(pass);
                }
                if(i==4)
                {
                    tv4.setText(uname);
                    tv14.setText(pass);
                }
                if(i==5)
                {
                    tv5.setText(uname);
                    tv15.setText(pass);
                }
                if(i==6)
                {
                    tv6.setText(uname);
                    tv16.setText(pass);
                }
                if(i==7)
                {
                    tv7.setText(uname);
                    tv17.setText(pass);
                }
                if(i==8)
                {
                    tv8.setText(uname);
                    tv18.setText(pass);
                }
                if(i==9)
                {
                    tv9.setText(uname);
                    tv19.setText(pass);
                }
                if(i==10)
                {
                    tv10.setText(uname);
                    tv20.setText(pass);
                }
                i++;
            }
        }
        else
        {
            tv_uname.setVisibility(View.GONE);
            tv_password.setVisibility(View.GONE);
            tv_msg.setVisibility(View.VISIBLE);
        }
    }
    public void assign_value(int i)
    {

    }
    @Override
    public void onBackPressed()
    {
        Intent i1 = new Intent(admin.this, login.class);
        startActivity(i1);
        finish();
    }

    @Override
    public void onClick(View v)
    {
        Intent i2 = new Intent(admin.this, login.class);
        startActivity(i2);
        finish();
    }
}
