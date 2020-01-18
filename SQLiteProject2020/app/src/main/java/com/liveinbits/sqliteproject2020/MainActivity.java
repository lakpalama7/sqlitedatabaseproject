package com.liveinbits.sqliteproject2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.liveinbits.EditFragmentContact;
import com.liveinbits.sqliteproject2020.model.Contact;
import com.liveinbits.sqliteproject2020.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity
        implements ContactFragment.onSelectedListener, ProfileFragmentContact.OnEditProfileContact, ContactFragment.addContactFragmentListener{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: view");
        init();
        initImageLoader();
    }

    private void initImageLoader(){
        UniversalImageLoader loader=new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(loader.getConfig());
    }
    private void init(){
        ContactFragment fragment=new ContactFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSelectedContact(Contact contact) {
        ProfileFragmentContact fragmentContact=new ProfileFragmentContact();
        Bundle arg=new Bundle();
        arg.putParcelable(getResources().getString(R.string.contact),contact);
        fragmentContact.setArguments(arg);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentContact)
                .addToBackStack(getResources().getString(R.string.profile_fragment_contact)).commit();
    }
//multiple permission checks
    public void requestPermission(String[] permissions){
        Log.d(TAG, "verifyPermission: verify");
        ActivityCompat.requestPermissions(MainActivity.this,permissions,REQUEST_CODE);

    }
//single permission checks
    public boolean checkPermission(String[] permissions){
        Log.d(TAG, "checkPermission: check");
        int permission=ActivityCompat.checkSelfPermission(MainActivity.this,permissions[0]);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: not granted "+permissions[0]);
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                for(int i=0;i<permissions.length;i++){
                    if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: permission granted "+permissions[i]);
                    }
                    else {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void oneditprofilecontact(Contact contact) {
        EditFragmentContact contact1=new EditFragmentContact();
        Bundle bundle=new Bundle();
        bundle.putParcelable("contact",contact);
        contact1.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,contact1)
                .addToBackStack("EditFragmentContact").commit();


    }

    @Override
    public void onClickAddContact() {
        AddContactFragment fragment=new AddContactFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment)
                .addToBackStack(getResources().getString(R.string.addcontactFragment))
                .commit();
    }
}
