package com.example.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapp.chats.Chats;
import com.example.chatapp.chats.ChatsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Setting up container settings
        final List<Chats> chatLogList = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.chat_log);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ChatsAdapter(chatLogList));

        // Fetching user ids
        final String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String otherPersonsId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String text = getResources().getString(R.string.talking_with, name);
        TextView t1 = findViewById(R.id.person_name);
        t1.setText(text);

        final EditText textBox = findViewById(R.id.editText);
        Button sendButton = findViewById(R.id.sendButton);

        // Listen for button clicks
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure message isn't blank
                if(textBox.getText().toString().trim().length() != 0) {
                    DatabaseTransaction.sendMessage(otherPersonsId, myId, textBox.getText().toString(), FirebaseFirestore.getInstance());
                }

                textBox.getText().clear();
            }

        });

        // Listen for messages
        DatabaseTransaction.showMessages(FirebaseFirestore.getInstance(), rv, chatLogList, otherPersonsId, myId);
    }
}
