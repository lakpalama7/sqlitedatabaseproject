package com.liveinbits.sqliteproject2020.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.liveinbits.sqliteproject2020.MainActivity;
import com.liveinbits.sqliteproject2020.R;
import com.liveinbits.sqliteproject2020.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class CardviewAdapter extends ArrayAdapter<String> {

    private static final String TAG = "CardviewAdapter";
    private Context context;
    private int layout;
    private LayoutInflater inflater;
    private List<String> contacts;
    public CardviewAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context=context;
        layout=resource;
        contacts=objects;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class MyViewHolder{
        TextView textView;
        ImageView lefticon;
        ImageView righticon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
            holder=new MyViewHolder();
            holder.textView=convertView.findViewById(R.id.number);
            holder.lefticon=convertView.findViewById(R.id.phone);
            holder.righticon=convertView.findViewById(R.id.message);
            convertView.setTag(holder);

        }
        else{
            holder=(MyViewHolder)convertView.getTag();
        }
        final String property=getItem(position);
        Log.d("property ",property);
        holder.textView.setText(property);

        if (property.contains("@")){
            holder.lefticon.setImageResource(context.getResources().getIdentifier("@drawable/ic_email",null,context.getPackageName()));
            holder.lefticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: email sending...");
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{property});
                    context.startActivity(intent);
                }
            });
        }
        else if((property.length()!=0)){
            holder.lefticon.setImageResource(context.getResources().getIdentifier("@drawable/ic_phone",null,context.getPackageName()));
            holder.lefticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((MainActivity)context).checkPermission(init.phone_permission)){
                        Log.d(TAG, "onClick: phone call .....");
                        Intent intent=new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",property,null));
                        context.startActivity(intent);
                    }
                    else{
                        ((MainActivity)context).requestPermission(init.phone_permission);                    }
                }
            });
            holder.righticon.setImageResource(context.getResources().getIdentifier("@drawable/ic_message",null,context.getPackageName()));
            holder.righticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: sms send");
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",property,null));
                    context.startActivity(intent);
                }
            });
        }


        return convertView;
    }
}
