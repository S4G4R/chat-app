package com.example.chatapp.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.chatapp.ChatActivity;
import com.example.chatapp.R;

import java.util.List;

// Adapter for Users

public class UsersAdapter extends ArrayAdapter<Users> {

    public UsersAdapter(Context context, List<Users> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_chatbar,parent,false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.chatbar_name);

        final Users user = getItem(position);

        nameTextView.setText(user.getName());

        Button btn = (Button) convertView.findViewById(R.id.chat_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), ChatActivity.class);
                intent.putExtra("id", user.getId());
                intent.putExtra("name", user.getName());
                parent.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
