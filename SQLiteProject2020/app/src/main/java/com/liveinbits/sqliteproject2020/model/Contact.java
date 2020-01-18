package com.liveinbits.sqliteproject2020.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private String name;
    private String phoneno;
    private String email;
    private String image;
    private int id;

    public Contact(String name, String phoneno, String email, String image) {

        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.image = image;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        phoneno = in.readString();
        email = in.readString();
        image = in.readString();
        id=in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String device) {
        this.email = device;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneno='" + phoneno + '\'' +
                ", device='" + email + '\'' +
                ", image='" + image + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(email);
        dest.writeString(image);
    }
}
