package com.liveinbits;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.liveinbits.sqliteproject2020.DialogShow;
import com.liveinbits.sqliteproject2020.MainActivity;
import com.liveinbits.sqliteproject2020.R;
import com.liveinbits.sqliteproject2020.database.DatabaseHelper;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.liveinbits.sqliteproject2020.utils.UniversalImageLoader;
import com.liveinbits.sqliteproject2020.utils.init;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditFragmentContact extends Fragment implements DialogShow.onSelectImageListener {
    private static final String TAG = "EditFragmentContact";
    private CircleImageView image;
    private EditText name;
    private EditText phone;
    private EditText email;
    private Toolbar toolbar;
    private TextView edittoolbar;
    private String imagepath=null;

    private Contact contact;
    public EditFragmentContact(){
        super();
        setArguments(new Bundle()); //handle the hull pointer exception ;;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit_contact,container,false);
        getBundle();
        image=view.findViewById(R.id.edit_image);
        name=view.findViewById(R.id.person1_edit1);
        phone=view.findViewById(R.id.person2_edit2);
        email=view.findViewById(R.id.person3_edit3);
        toolbar=view.findViewById(R.id.edit_toolbar);

        edittoolbar=view.findViewById(R.id.toolbartitle);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        edittoolbar.setText(getString(R.string.edittitle));
        setHasOptionsMenu(true);

        ImageView edit_back=view.findViewById(R.id.edit_backarrow);
        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ImageView edit_save=view.findViewById(R.id.edit_check);
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: save");
                DatabaseHelper helper=new DatabaseHelper(getActivity());

                int contactid=-1;
                if(!name.getText().toString().isEmpty() && name.getText().toString()!=null){
                    Cursor cursor=helper.getContactId(contact);
                    while (cursor.moveToNext()){
                        contactid=cursor.getInt(0);
                    }
                    cursor.close();
                    if(contactid>-1){
                        if(imagepath!=null){
                            contact.setImage(imagepath);
                        }
                        contact.setName(name.getText().toString());
                        contact.setEmail(email.getText().toString());
                        contact.setPhoneno(phone.getText().toString());
                        if(helper.updateContact(contact,contactid)){
                            Toast.makeText(getActivity(),"update success ",Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getActivity(),"failed update ",Toast.LENGTH_LONG).show();

                        }
                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                }

            }
        });

        loadData();


        ImageView camera=view.findViewById(R.id.edit_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: camera");

                if(((MainActivity)getActivity()).checkPermission(init.camera_permission)){
                    DialogShow dialog = new DialogShow();
                    dialog.show(getFragmentManager(), getString(R.string.showdialog));
                    dialog.setTargetFragment(EditFragmentContact.this,0);

                }
                else{
                    ((MainActivity)getActivity()).requestPermission(init.camera_permission);
                }

              /*  for (int i=0;i<init.camera_permission.length;i++){
                    String [] permission={init.camera_permission[i]};
                    Log.i(TAG, "onClick: "+permission);
                    if(((MainActivity)getActivity()).checkPermission(permission)){
                        if(i==(init.camera_permission.length-1)) {
                            DialogShow dialog = new DialogShow();
                            dialog.show(getFragmentManager(), getString(R.string.showdialog));
                        }
                    }
                    else{
                        ((MainActivity)getActivity()).requestPermission(permission);
                    }
                }
*/
            }   
        });
        return view;
    }

    private void loadData(){
       name.setText(contact.getName());
       phone.setText(contact.getPhoneno());
       email.setText(contact.getEmail());
        UniversalImageLoader.loadImage(contact.getImage(),image,"");

    }

    private void getBundle(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            contact=bundle.getParcelable("contact");
        }
    }

    @Override
    public void getBitmapImage(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }

    @Override
    public void getImageFromMemory(File file) {
        imagepath=file.getPath().replace("/","//");
        Log.i(TAG, "getImageFromMemory: final path "+imagepath);
        UniversalImageLoader.loadImage(imagepath,image,"");
    }
}
