package com.example.rentacarros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userViewHolder>{
    public UserAdapter(ArrayList<Users> userList) {
        UserList = userList;
    }

    ArrayList<Users>UserList;
    @NonNull
    @Override
    public UserAdapter.userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vuser = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_user_item,null,false);
        return new userViewHolder(vuser);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.userViewHolder holder, int position) {
        holder.name.setText(UserList.get(position).getName().toString());
        holder.username.setText(UserList.get(position).getUsername().toString());
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        TextView name,username;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.etname);
            username = itemView.findViewById(R.id.etusername);
        }
    }
}
