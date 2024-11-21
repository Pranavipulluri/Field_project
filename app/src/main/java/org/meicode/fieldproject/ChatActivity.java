package org.meicode.fieldproject;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageView sendButton;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private DatabaseReference chatDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);


        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Get Firebase database reference
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference("chats");

        // Listen for incoming messages
        Query query = chatDatabaseReference.orderByKey();
        query.addChildEventListener(new ChatChildEventListener(messageList, chatAdapter));

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new message object
        Message message = new Message(messageText, System.currentTimeMillis());

        // Push the message to the Firebase database
        chatDatabaseReference.push().setValue(message);

        // Clear the input field
        messageEditText.setText("");
    }
}
