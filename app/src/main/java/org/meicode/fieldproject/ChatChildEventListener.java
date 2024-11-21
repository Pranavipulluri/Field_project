package org.meicode.fieldproject;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.List;

public class ChatChildEventListener implements ChildEventListener {

    private List<Message> messageList;
    private ChatAdapter chatAdapter;

    public ChatChildEventListener(List<Message> messageList, ChatAdapter chatAdapter) {
        this.messageList = messageList;
        this.chatAdapter = chatAdapter;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Message message = dataSnapshot.getValue(Message.class);
        if (message != null) {
            messageList.add(message);
            chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {}

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {}

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
