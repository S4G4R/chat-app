package com.example.chatapp.chats;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapp.R;

import java.util.List;

// Adapter for Chats

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_RIGHT = 0;
    public static final int ITEM_TYPE_LEFT = 1;

    private List<Chats> msgList = null;

    public ChatsAdapter(List<Chats> msgList) {
        this.msgList = msgList;
    }

    @Override
    public int getItemViewType(int position) {
        Chats chat = msgList.get(position);

        if (chat.isSender()) return ITEM_TYPE_RIGHT;
        else                 return ITEM_TYPE_LEFT;
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType==ITEM_TYPE_RIGHT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chatbubbleright, viewGroup, false);
            viewHolder = new ViewHolderOne(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chatbubbleleft, viewGroup, false);
            viewHolder = new ViewHolderTwo(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Chats chat = msgList.get(position);

        if (holder.getItemViewType() == ITEM_TYPE_RIGHT) {
            ViewHolderOne vholder = (ViewHolderOne) holder;
            vholder.message.setText(chat.getMessage());
        } else {
            ViewHolderTwo vholder = (ViewHolderTwo) holder;
            vholder.message.setText(chat.getMessage());
        }

    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView message;

        public ViewHolderOne(View view) {
            super(view);

            message = (TextView) view.findViewById(R.id.txt_msg);
        }
    }

    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        public TextView message;

        public ViewHolderTwo(View view) {
            super(view);

            message = (TextView) view.findViewById(R.id.txt_msg);
        }
    }
}
