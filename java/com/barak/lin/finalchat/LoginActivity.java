package com.barak.lin.finalchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText userNameET, passET;
    TextView regiserPageLinkTV;
    Button loginB;

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameET = (EditText) findViewById(R.id.userNameEditText);
        passET = (EditText) findViewById(R.id.passEditText);
        regiserPageLinkTV = (TextView) findViewById(R.id.registerPageTextView);
        loginB = (Button) findViewById(R.id.loginButton);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                User user = new User(userNameET.getText().toString(), passET.getText().toString());
//
//                //userNameET.setText("hello");
//                ConversationTask task = new ConversationTask(LoginActivity.this);
//
//                // get example
//                String requestUrlG = "http://192.168.1.17:8080/HiberContactor/LoginServlet?nick_name=" + user.getUsername() + "&password=" + user.getPassword();
//                task.execute(requestUrlG);
//
//                // post example
//                // String requestUrlP = "http://192.168.1.17:8080/HiberContactor/LoginServlet";
//                // task.execute(requestUrlP);
                Intent i = new Intent(LoginActivity.this, ChatsActivity.class);
                i.putExtra("user_name",userNameET.getText().toString());
                startActivity(i);
            }
        });

        regiserPageLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        EditText editText = (EditText) findViewById(R.id.userNameEditText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}