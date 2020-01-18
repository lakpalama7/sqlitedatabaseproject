package com.liveinbits.sqliteproject2020.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.liveinbits.sqliteproject2020.R;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private LayoutInflater inflater;
    private List<Contact> listcontact;
    private List<Contact> searchlist=new ArrayList<>(); //use for search
    private int layout;
    private String append;


    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects,String append) {
        super(context, resource, objects);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout=resource;
        this.context=context;
        this.append=append;
        listcontact=objects;
        searchlist.addAll(listcontact);

    }

    private static class ViewHolder{
        private TextView name;
        private CircleImageView image;
        private ProgressBar progressBar;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         final ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
            holder=new ViewHolder();
            holder.name=convertView.findViewById(R.id.name);
            holder.image=convertView.findViewById(R.id.profile_image);
            holder.progressBar=convertView.findViewById(R.id.progressbar);

            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        String name=getItem(position).getName();
        final String imagepath=getItem(position).getImage();
        holder.name.setText(name);


        ImageLoader loader=ImageLoader.getInstance();
       // final ViewHolder finalHolder = holder;
        loader.displayImage(append + imagepath, holder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
               // Log.d("path ",append+imagepath);
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);

            }
        });
        return convertView;
    }

    public void searchFilter(String text){
        text=text.toString().toLowerCase(Locale.getDefault());
        listcontact.clear();
        if(text.length()==0){
            listcontact.addAll(searchlist);
        }
        else{
            listcontact.clear();
            for(Contact contact: searchlist){
                if(contact.getName().toLowerCase().contains(text)){
                    listcontact.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }
}
