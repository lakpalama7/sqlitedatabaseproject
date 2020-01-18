package com.liveinbits.sqliteproject2020.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.liveinbits.sqliteproject2020.model.Contact;

import java.util.EmptyStackException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="contact.db";
    private static final int VERSION=1;
    private static final String TABLE_NAME="contact";
    private static final String ID="id";
    private static final String NAME="name";
    private static final String PHONE="phone";
    private static final String EMAIL="email";
    private static final String IMAGE="image";

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=" CREATE TABLE " +
                TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT, " +
                PHONE + " TEXT, " +
                EMAIL + " TEXT, " +
                IMAGE + " TEXT )";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
    }

    public boolean addContact(Contact contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NAME,contact.getName());
        values.put(PHONE,contact.getPhoneno());
        values.put(EMAIL,contact.getEmail());
        values.put(IMAGE,contact.getImage());

        long result=db.insert(TABLE_NAME,null,values);
        db.close();
        if(result>0){
            return true;
        }
        else{
            return false;
        }
    }
    public Cursor getAllContact(){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor= database.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

    public boolean updateContact(Contact contact, int id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NAME,contact.getName());
        values.put(PHONE,contact.getPhoneno());
        values.put(EMAIL,contact.getEmail());
        values.put(IMAGE,contact.getImage());

        int result=db.update(TABLE_NAME,values,ID + " = ? " , new String[]{String.valueOf(id)});
        db.close();
        if(result!=1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getContactId(Contact contact){
        SQLiteDatabase database=this.getReadableDatabase();
        String sql="SELECT * FROM "+ TABLE_NAME +
                " WHERE " + NAME + " = '" +contact.getName() +
                "'" + " AND " + PHONE + " = '" + contact.getPhoneno() + "'";
        return database.rawQuery(sql,null);
    }

    public int deleteContact(int id){
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete(TABLE_NAME, ID + " = ? ",new String[]{String.valueOf(id)});
    }
}
