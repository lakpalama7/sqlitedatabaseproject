package com.liveinbits.sqliteproject2020;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.liveinbits.sqliteproject2020.database.DatabaseHelper;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.liveinbits.sqliteproject2020.utils.CardviewAdapter;
import com.liveinbits.sqliteproject2020.utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragmentContact extends Fragment {
    private static final String TAG = "ProfileFragmentContact";
    private Toolbar toolbar;
    private Contact contact;
    private TextView name;
    private CircleImageView image;
    private ListView listView;


    public interface OnEditProfileContact{
        void oneditprofilecontact(Contact contact);
    }
    private OnEditProfileContact listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (OnEditProfileContact) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile_layout,container,false);
        toolbar=view.findViewById(R.id.profile_toolbar2);

        name=view.findViewById(R.id.nameprofile);
        image=view.findViewById(R.id.imageprofile);

        listView=view.findViewById(R.id.listviewprofile);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        ImageView backarrow=view.findViewById(R.id.profile_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

/*        ImageView edit=view.findViewById(R.id.profile_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFragmentContact fragmentContact=new EditFragmentContact();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout,fragmentContact)
                        .addToBackStack(getResources().getString(R.string.edit_contact_fragment)).commit();
            }
        });*/

         contact=getBundleInfo();
         initData();

         ImageView profile_edit=view.findViewById(R.id.profile_edit);
         profile_edit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.oneditprofilecontact(contact);
             }
         });

        return view;
    }

    private void initData(){
        name.setText(contact.getName());
        UniversalImageLoader.loadImage(contact.getImage(),image,"");
        ArrayList<String> contactlist=new ArrayList<>();
        contactlist.add(contact.getPhoneno());
        contactlist.add(contact.getEmail());
        CardviewAdapter adapter=new CardviewAdapter(getContext(),R.layout.layout_cardview_profile,contactlist);
        listView.setAdapter(adapter);
        listView.setDividerHeight(1);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_layout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                Log.d(TAG, "onOptionsItemSelected: delete option");
                DatabaseHelper helper=new DatabaseHelper(getActivity());
                int contactid=-1;
                if(contact!=null){
                    Cursor cursor=helper.getContactId(contact);
                    while (cursor.moveToNext()){
                        contactid=cursor.getInt(0);
                    }
                    cursor.close();
                    if(contactid>-1){
                        if(helper.deleteContact(contactid)>0) {
                            Log.i(TAG, "onOptionsItemSelected: Delete success ");
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else{
                            Log.i(TAG, "onOptionsItemSelected: Cannot delete");
                        }
                    }
                    
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Contact getBundleInfo(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            return bundle.getParcelable(getResources().getString(R.string.contact));
        }
        return null;
    }
}
