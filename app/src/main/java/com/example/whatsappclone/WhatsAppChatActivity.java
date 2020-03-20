package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayList<String> chatList;
    private ArrayAdapter adapter;
    private String selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);

        chatListView = findViewById(R.id.chatListView);
        selectedUser = getIntent().getStringExtra("selectedUser");
        chatList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatList);
        chatListView.setAdapter(adapter);


        findViewById(R.id.btnSend).setOnClickListener(this);
        showMessages();


        //Thread for refreshing messaging
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(2000);
                        chatList.clear();
                        showMessages();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


    }


    @Override
    public void onClick(View view) {
        final EditText edtMessage = findViewById(R.id.edtMessage);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient", selectedUser);
        chat.put("waMessage", edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    chatList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });

    }


    private void showMessages() {
        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");


            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";

                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }

                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatList.add(waMessage);
                        }
                        adapter.notifyDataSetInvalidated();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

