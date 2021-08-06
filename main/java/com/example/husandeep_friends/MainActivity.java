package com.example.husandeep_friends;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.husandeep_friends.databinding.ActivityFriendBinding;
import com.example.husandeep_friends.databinding.ActivityMainBinding;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
ActivityMainBinding main;
private ArrayList<friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.main = ActivityMainBinding.inflate(getLayoutInflater());
        View view = main.getRoot();
        setContentView(view);
        this.friendList = new ArrayList<friend>();
        this.main.buttonAdd.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v != null){
            if(validateInput()){
                addFriend();
                showFriendAddedAlert();
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_list:
                showFriends();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showFriendAddedAlert(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Congrats");
        alertBuilder.setMessage("You have made one New Friend!\nYou have " + this.friendList.size() + " friends");
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        alertBuilder.show();
    }

    private void reset(){
        this.main.nameEdit.setText("");
        this.main.emailEdit.setText("");
        this.main.PhoneEdit.setText("");
        this.main.addressEdit.setText("");
    }
    private void showFriends(){
        Intent showFriend = new Intent(this, FriendActivity.class);
        showFriend.putParcelableArrayListExtra("EXTRA_FRIEND_LIST", this.friendList);
        startActivity(showFriend);
    }

    private void addFriend(){
        friend temp = new friend();
        temp.setName(this.main.nameEdit.getText().toString());
        temp.setEmail(this.main.emailEdit.getText().toString());
        temp.setPhoneNum(this.main.PhoneEdit.getText().toString());
        temp.setAddress(this.main.addressEdit.getText().toString());

        this.friendList.add(temp);
    }
    private boolean validateInput(){
        boolean isValid = true;
        if(this.main.nameEdit.getText().toString().equals("")){
            this.main.nameEdit.setError("Name cannot be Empty!");
            isValid = false;
        } else if(!this.main.emailEdit.getText().toString().isEmpty()) {
            if(!this.main.emailEdit.getText().toString().contains("@")){
            this.main.emailEdit.setError("Enter a Valid Email!");
            this.main.emailEdit.setText("");
            isValid = false;}
        } else if (this.main.PhoneEdit.getText().toString().equals("")) {
            this.main.PhoneEdit.setError("Phone Number cannot be Empty!");
            isValid = false;
        } else if (this.main.PhoneEdit.getText().toString().length() < 10 || this.main.PhoneEdit.getText().toString().length() > 12) {
            this.main.PhoneEdit.setError("Phone Number should be between 10-12 digits only!");
            isValid = false;
        } else if (this.main.addressEdit.getText().toString().equals("")) {
            this.main.addressEdit.setError("Address cannot be Empty!");
            isValid = false;
        }
        return isValid;
    }

}