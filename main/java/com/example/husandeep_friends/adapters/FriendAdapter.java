/*
 * Name: Husandeep Singh Brar
 * Student Number: 149046195
 * Midterm Test
 * Date: 24 June, 2021
 * */

package com.example.husandeep_friends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.husandeep_friends.OnFriendClickListener;
import com.example.husandeep_friends.databinding.RvFriendlistBinding;
import com.example.husandeep_friends.friend;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private final Context context;
    private ArrayList<friend> friendArrayList;
    private final OnFriendClickListener clickListener;

    public FriendAdapter(Context context, ArrayList<friend> friendArrayList, OnFriendClickListener clickListener) {
        this.context = context;
        this.friendArrayList = friendArrayList;
        this.clickListener = clickListener;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(RvFriendlistBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        final friend currentFriend = this.friendArrayList.get(position);
        holder.bind(context, currentFriend, clickListener);
    }

    @Override
    public int getItemCount() {
        return this.friendArrayList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        RvFriendlistBinding binding;
        public FriendViewHolder(RvFriendlistBinding b){
            super(b.getRoot());
            this.binding = b;
        }

        public void bind(Context context, final friend currentFriend, OnFriendClickListener clickListener ){
            binding.friendName.setText(currentFriend.getName());
            binding.friendPhone.setText("Phone Number: " + currentFriend.getPhoneNum());
            binding.friendEmail.setText("Email: " + ((currentFriend.getEmail().isEmpty())?"Not Provided":currentFriend.getEmail()));
            binding.friendAddress.setText("Address:\n" + currentFriend.getAddress());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onFriendClicked(currentFriend);

                }
            });
        }
    }
}
