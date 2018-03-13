package com.example.geoaloshious.stegano;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Geo Aloshious on 3/13/2018.
 */

public class Adapter2 extends RecyclerView.Adapter<Adapter2.MyViewHolder> {
    Context context;
    int a[] = {1,2,3,4,5,6,7,8,9,0};
    int b[]=new int[10];
    public Adapter2(reg reg)
    {
        this.context=reg;
    }
    public void rand()
    {
        Random rgen = new Random();
        for (int i=0; i<a.length; i++)
        {
            int randomPosition = rgen.nextInt(a.length);
            int temp = a[i];
            a[i] = a[randomPosition];
            a[randomPosition] = temp;
        }
        for(int j=0;j<a.length;j++)
        {
            b[j]=a[j];
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row2,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tv_name.setText(a[position]+"");
    }

    @Override
    public int getItemCount()
    {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
        }
    }
}
