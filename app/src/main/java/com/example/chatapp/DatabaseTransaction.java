package com.example.chatapp;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.chatapp.chats.Chats;
import com.example.chatapp.chats.ChatsAdapter;
import com.example.chatapp.users.Users;
import com.example.chatapp.users.UsersAdapter;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DatabaseTransaction {

    public static void addUser(FirebaseUser user, FirebaseFirestore db) {
        Map<String, Object> usr = new HashMap<>();

        // Creates a new user
        usr.put("id", user.getUid());
        usr.put("name", user.getDisplayName());
        usr.put("status", "LOGGED_IN");

        db.collection("users").document(FirebaseAuth.getInstance().getUid()).set(usr, SetOptions.merge());
    }

    public static void logoutUser(FirebaseUser user, FirebaseFirestore db) {
        DocumentReference userRef = db.collection("users").document(user.getUid());

        // Log user out
        userRef.update("status", "LOGGED_OUT");
    }

    public static void listUsers(FirebaseFirestore db, final ListView chatList, final Context context) {
        CollectionReference users = db.collection("users");
        final String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final List<Users> usersList = new ArrayList<>();

        // Listen for user updates (user signed in, user signed out)
        users.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                UsersAdapter usersAdapter = new UsersAdapter(context, usersList);
                chatList.setAdapter(usersAdapter);

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    // Convert user to POJO
                    Users user = dc.getDocument().toObject(Users.class);

                    switch (dc.getType()) {
                        case ADDED:
                            if (!user.getName().equals(currentUserName) && user.getStatus().equals("LOGGED_IN")) {
                                usersList.add(user);
                            }
                            break;
                        case MODIFIED:
                            if (user.getStatus().equals("LOGGED_OUT")) {
                                usersList.remove(user);
                            } else if (!user.getName().equals(currentUserName)) {
                                usersList.add(user);
                            }
                            break;
                        case REMOVED:
                            usersList.remove(user);
                            break;
                    }
                    usersAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public static void sendMessage(String receiverId, String senderId, String message, FirebaseFirestore db) {
        String connectionId = getConnectionId(receiverId, senderId);

        // Message creation and dispatch
        Map<String, Object> msg = new HashMap<>();
        msg.put("timeStamp", Timestamp.now().toDate().toString());
        msg.put("message", message);
        msg.put("receiverId", receiverId);
        msg.put("senderId", senderId);

        db.collection("chats").document(connectionId).collection("log").add(msg);
    }

    public static void showMessages(FirebaseFirestore db, final RecyclerView chatLog, final List<Chats> chatLogList, String receiverId, String senderId) {
        String connectionId = getConnectionId(receiverId, senderId);

        // Fetch chats
        CollectionReference chats = db.collection("chats")
                .document(connectionId)
                .collection("log");

        // Set up an adapter to populate the chat list
        final ChatsAdapter chatsAdapter = new ChatsAdapter(chatLogList);
        chatLog.setAdapter(chatsAdapter);

        // Listen for new messages
        chats.orderBy("timeStamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    Chats chat = dc.getDocument().toObject(Chats.class);

                    switch (dc.getType()) {
                        case ADDED:
                            chatLogList.add(chat);
                            chatsAdapter.notifyDataSetChanged();
                            chatLog.scrollToPosition(chatsAdapter.getItemCount()-1);
                            break;
                    }
                }


            }
        });

    }

    private static String getConnectionId(String receiverId, String senderId) {
        // This will create a unique string based on the two ids
        if (receiverId.compareTo(senderId) < 0) {
            return receiverId.concat(senderId);
        }

        return senderId.concat(receiverId);
    }

}
