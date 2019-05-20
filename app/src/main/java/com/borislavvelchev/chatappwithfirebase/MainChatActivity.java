package com.borislavvelchev.chatappwithfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    private String displayName;
    private ListView chatListView;
    private EditText inputText;
    private ImageButton sendButton;
    private DatabaseReference databaseReference;
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setDisplayName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");

        // Link the Views in the layout to the Java code
        inputText = (EditText) findViewById(R.id.messageInput);
        sendButton = (ImageButton) findViewById(R.id.sendButton);
        chatListView = (ListView) findViewById(R.id.chat_list_view);

        // Send the message when the "enter" button is pressed
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void setDisplayName() {
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        displayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);

        if (displayName == null) displayName = "Anonymous";
    }

    private void sendMessage() {
        Log.d("Chat", "I sent something");
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            InstantMessage chat = new InstantMessage(input, displayName);
//            databaseReference.child("messages").push().setValue(chat);
            databaseReference.child("messages").setValue(chat);
//            inputText.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new ChatListAdapter(this, databaseReference, displayName);
        chatListView.setAdapter(adapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.cleanup();
    }

}
