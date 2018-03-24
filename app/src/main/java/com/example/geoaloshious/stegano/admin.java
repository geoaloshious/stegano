package com.example.geoaloshious.stegano;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class admin extends AppCompatActivity implements View.OnClickListener {
    private int i=1;
    private final DBConnection db = new DBConnection(admin.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextView tv3 = findViewById(R.id.tv3);
        TextView tv4 = findViewById(R.id.tv4);
        TextView tv5 = findViewById(R.id.tv5);
        TextView tv6 = findViewById(R.id.tv6);
        TextView tv7 = findViewById(R.id.tv7);
        TextView tv8 = findViewById(R.id.tv8);
        TextView tv9 = findViewById(R.id.tv9);
        TextView tv10 = findViewById(R.id.tv10);
        TextView tv11 = findViewById(R.id.tv11);
        TextView tv12 = findViewById(R.id.tv12);
        TextView tv13 = findViewById(R.id.tv13);
        TextView tv14 = findViewById(R.id.tv14);
        TextView tv15 = findViewById(R.id.tv15);
        TextView tv16 = findViewById(R.id.tv16);
        TextView tv17 = findViewById(R.id.tv17);
        TextView tv18 = findViewById(R.id.tv18);
        TextView tv19 = findViewById(R.id.tv19);
        TextView tv20 = findViewById(R.id.tv20);
        TextView tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        TextView tv_uname = findViewById(R.id.tv_uname);
        tv_uname.setVisibility(View.GONE);
        TextView tv_password = findViewById(R.id.tv_password);
        tv_password.setVisibility(View.GONE);
        Button bt_back = findViewById(R.id.bt_back);
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
                String uname = cursor.getString(6);
                String pass = cursor.getString(7);
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
