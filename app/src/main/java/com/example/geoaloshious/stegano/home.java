package com.example.geoaloshious.stegano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private String name;
    private String dob;
    private String gender;
    private String email;
    private String phone;
    private String uname;
    private final DBConnection db = new DBConnection(home.this);
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tv_name2 = findViewById(R.id.tv_name2);
        TextView tv_gender2 = findViewById(R.id.tv_gender2);
        TextView tv_dob2 = findViewById(R.id.tv_dob2);
        TextView tv_phone2 = findViewById(R.id.tv_phone2);
        TextView tv_email2 = findViewById(R.id.tv_email2);
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
                name=cursor.getString(1);
                gender=cursor.getString(2);
                dob=cursor.getString(3);
                phone=cursor.getString(4);
                email=cursor.getString(5);
            }
        }
        db.closeConnection();
        tv_name2.setText(name);
        tv_gender2.setText(gender);
        tv_dob2.setText(dob);
        tv_phone2.setText(phone);
        tv_email2.setText(email);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile)
        {
            Intent i1 = new Intent(home.this, edit_profile.class);
            startActivity(i1);
            finish();
        }
        else if (id == R.id.nav_delete_account)
        {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_delete_account).setTitle("Delete Account")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.openConnection();
                            String query2= "delete from tbl_stegno where uname='"+uname+"'";
                            db.insertData(query2);
                            db.closeConnection();
                            Toast.makeText(home.this, "Account deleted", Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(home.this, login.class);
                            startActivity(i2);
                            finish();
                        }
                    }).setNegativeButton("no", null).show();

        }
        else if (id == R.id.nav_change_password) {
            Intent i1 = new Intent(home.this, change_pwd.class);
            startActivity(i1);
            finish();

        }
        else if (id == R.id.nav_logout)
        {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_logout).setTitle("Logout")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(home.this, "Logged out", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(home.this, login.class);
                            startActivity(i1);
                            finish();
                        }
                    }).setNegativeButton("no", null).show();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
