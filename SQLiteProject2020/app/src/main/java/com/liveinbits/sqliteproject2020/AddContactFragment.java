package com.liveinbits.sqliteproject2020;

import android.graphics.Bitmap;
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
import androidx.fragment.app.Fragment;

import com.liveinbits.EditFragmentContact;
import com.liveinbits.sqliteproject2020.database.DatabaseHelper;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.liveinbits.sqliteproject2020.utils.UniversalImageLoader;
import com.liveinbits.sqliteproject2020.utils.init;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactFragment extends Fragment implements DialogShow.onSelectImageListener {

    private static final String TAG = "AddContactFragment";
    private Toolbar toolbar;
    private TextView toolbartitle;
    private CircleImageView image;
    private EditText name;
    private EditText phone;
    private EditText email;
    private String imagepath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_contact,container,false);
            toolbar=view.findViewById(R.id.edit_toolbar);
            toolbartitle=view.findViewById(R.id.toolbartitle);

        image=view.findViewById(R.id.edit_image);
        name=view.findViewById(R.id.person1_edit1);
        phone=view.findViewById(R.id.person2_edit2);
        email=view.findViewById(R.id.person3_edit3);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbartitle.setText(getString(R.string.addtitle));

        ImageView edit_back=view.findViewById(R.id.edit_backarrow);
        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ImageView camera=view.findViewById(R.id.edit_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: camera");

                if(((MainActivity)getActivity()).checkPermission(init.camera_permission)){
                    DialogShow dialog = new DialogShow();
                    dialog.show(getFragmentManager(), getString(R.string.showdialog));
                    dialog.setTargetFragment(AddContactFragment.this,0);

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


        ImageView checksave=view.findViewById(R.id.edit_check);
        checksave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && name.getText().toString()!=null ){
                    Contact contact=new Contact(name.getText().toString(),phone.getText().toString(),email.getText().toString(),imagepath);
                    DatabaseHelper helper=new DatabaseHelper(getActivity());
                    if(helper.addContact(contact)){
                        Toast.makeText(getActivity(),"Insert success ",Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else{
                        Toast.makeText(getActivity(),"Insert Failed ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
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
