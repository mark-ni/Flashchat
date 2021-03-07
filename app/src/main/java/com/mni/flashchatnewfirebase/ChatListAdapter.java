package com.mni.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mChildEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, String displayName, DatabaseReference dr) {
        mActivity = activity;
        mDatabaseReference = dr;
        mDisplayName = displayName;
        mSnapshotList = new ArrayList<DataSnapshot>();

        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    static class ViewHolder {
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSnapshotList.get(i).getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) view.findViewById(R.id.author);
            holder.body = (TextView) view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            view.setTag(holder);
        }

        final InstantMessage message = (InstantMessage) getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.authorName.setText(message.getAuthor());
        holder.body.setText(message.getMessage());

        setChatRowAppearance(message.getAuthor().equals(mDisplayName), holder);

        return view;
    }

    public void cleanup() {
        mDatabaseReference.removeEventListener(mChildEventListener);
    }

    private void setChatRowAppearance(boolean isMe, ViewHolder holder) {
        if (isMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setBackgroundColor(Color.GREEN);
            holder.body.setBackgroundColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setBackgroundColor(Color.BLUE);
            holder.body.setBackgroundColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }
    }
}
