package com.liveinbits.sqliteproject2020;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.liveinbits.sqliteproject2020.utils.init;

import java.io.File;

public class DialogShow extends DialogFragment {

    private static final String TAG = "DialogShow";
    private onSelectImageListener listener;

    public interface onSelectImageListener{
        void getBitmapImage(Bitmap bitmap);
        void getImageFromMemory(File file);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (onSelectImageListener) getTargetFragment();  //fragment to fragment -> targetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.change_photodialog,container,false);

        TextView changephoto=view.findViewById(R.id.changephoto);
        TextView takephoto=view.findViewById(R.id.takenewphoto);
        TextView takefrommemory=view.findViewById(R.id.changefrommemory);
        TextView cancel=view.findViewById(R.id.cancel);

        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: changephoto ");
            }
        });

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: takephoto");

                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, init.CAMERA_REQUESTCODE);
            }
        });
        takefrommemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: takefrommemory");

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,init.IMAGE_FROMMEMORY);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: cancel");
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==init.CAMERA_REQUESTCODE && resultCode== Activity.RESULT_OK){
            if(data!=null){
                Bitmap image= (Bitmap) data.getExtras().get("data");
                Log.i(TAG, "onActivityResult: image taken "+image);
                listener.getBitmapImage(image);
                getDialog().dismiss();
            }
        }
         if(requestCode==init.IMAGE_FROMMEMORY && resultCode==Activity.RESULT_OK){
            if(data!=null){
                Uri uri=data.getData();
                File file=new File(uri.toString());
                Log.i(TAG, "onActivityResult: file path "+file.getPath());
                listener.getImageFromMemory(file);
                getDialog().dismiss();
            }
        }
    }
}
