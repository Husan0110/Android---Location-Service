/*
 * Name: Husandeep Singh Brar
 * Student Number: 149046195
 * Midterm Test
 * Date: 24 June, 2021
 * */
package com.example.husandeep_friends;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class friend implements Parcelable, Serializable {
    private String name;
    private String email;
    private String phoneNum;
    private String address;

    public friend(){
        this.name = "";
        this.email="";
        this.address="";
        this.phoneNum="";
    }

    public friend(String name, String email, String phoneNum, String address) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    protected friend(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNum = in.readString();
        address = in.readString();
    }

    public static final Creator<friend> CREATOR = new Creator<friend>() {
        @Override
        public friend createFromParcel(Parcel in) {
            return new friend(in);
        }

        @Override
        public friend[] newArray(int size) {
            return new friend[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNum);
        dest.writeString(address);
    }
}
