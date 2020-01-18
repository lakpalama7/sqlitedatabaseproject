package com.liveinbits.sqliteproject2020;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liveinbits.sqliteproject2020.database.DatabaseHelper;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.liveinbits.sqliteproject2020.utils.CustomAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";
    private static final String imagepath="encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRswvvedX3RsLAJYE_MgTkMHEpnq0uroMXSWx_VvWm6TmsjqMWl&s";

    private static final int NORMAL=0;
    private static final int SEARCH=1;
    private int appbarstate;

    private CustomAdapter adapter;
    private AppBarLayout viewcontact,searchcontact;
    private ListView listView;


    private ImageView search;
    private EditText searchitem;

    public interface addContactFragmentListener{
        void onClickAddContact();
    }
    private addContactFragmentListener addcontactlistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (onSelectedListener) context;
        addcontactlistener=(addContactFragmentListener)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contacts,container,false);
        Log.d(TAG, "onCreateView: fragment");

        viewcontact=view.findViewById(R.id.appbarlayout);
        searchcontact=view.findViewById(R.id.searchbarlayout);

        listView=view.findViewById(R.id.contactlist);

        searchitem=view.findViewById(R.id.editsearch);

        changeState(NORMAL);

        getListContact();

        FloatingActionButton fab=view.findViewById(R.id.floatingbtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: fab click");
                addcontactlistener.onClickAddContact();
            }
        });

        ImageView search=view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: search click");
                toggleToolbarState();
            }
        });

        ImageView backarrow=view.findViewById(R.id.backicon);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: backarrow click");
                toggleToolbarState();

            }
        });

        return view;
    }


    private void toggleToolbarState() {
        if(appbarstate==NORMAL){
            changeState(SEARCH);
        }else if(appbarstate==SEARCH){
            changeState(NORMAL);
        }
    }

    private void changeState(int state) {
        appbarstate = state;
        Log.d("changeState: state", "" + state);
        if (appbarstate == SEARCH) {
            viewcontact.setVisibility(View.GONE);
            searchcontact.setVisibility(View.VISIBLE);
            View view=getView();
            EditText text=view.findViewById(R.id.editsearch);
            text.requestFocus();

            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else if(appbarstate==NORMAL) {
            searchcontact.setVisibility(View.GONE);
            viewcontact.setVisibility(View.VISIBLE);

            View view = getView();
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                Log.d(TAG, "changeState: " + e.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeState(NORMAL);
    }


    private void getListContact(){
       /* final ArrayList<Contact> contacts=new ArrayList<>();
        contacts.add(new Contact("lakpa","123-123-222","lakpa1@gamil.com",imagepath));
        contacts.add(new Contact("lama","123-123-222","lakpa2@gamil.com",imagepath));
        contacts.add(new Contact("sherpa","123-123-222","lakpa3@gamil.com",imagepath));
        contacts.add(new Contact("hari","123-123-222","lakpa4@gamil.com",imagepath));
        contacts.add(new Contact("vinay","123-123-222","lakpa5@gamil.com",imagepath));
        contacts.add(new Contact("lakpa","123-123-222","lakpa6@gamil.com",imagepath));
*/
        DatabaseHelper helper=new DatabaseHelper(getActivity());
       Cursor cursor=helper.getAllContact();
       final ArrayList<Contact> contacts=new ArrayList<>();
       while(cursor.moveToNext()){
           contacts.add(new Contact(
                   cursor.getString(1),
                   cursor.getString(2),
                   cursor.getString(3),
                   cursor.getString(4)
           ));
       }
       cursor.close();

        adapter=new CustomAdapter(getActivity(),R.layout.single_contact_layout,contacts,"");

        searchitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text=searchitem.getText().toString().toLowerCase(Locale.getDefault());
                adapter.searchFilter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             listener.onSelectedContact(contacts.get(position));
         }
     });
    }

    public interface onSelectedListener{
        void onSelectedContact(Contact contact);
    }
    private onSelectedListener listener;
}
