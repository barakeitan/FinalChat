package com.barak.lin.finalchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {


    EditText firstNameET, lastNameET, emailET, phoneET, birthDateET, userNameET, passET;
    Button registerB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        firstNameET = (EditText) findViewById(R.id.firstNameEditText);
        lastNameET = (EditText) findViewById(R.id.lastNameEditText);
        emailET = (EditText) findViewById(R.id.emailEditText);
        phoneET = (EditText) findViewById(R.id.phoneEditText);
        birthDateET = (EditText) findViewById(R.id.birthDateEditText);
        userNameET = (EditText) findViewById(R.id.userNameEditText);
        passET = (EditText) findViewById(R.id.passEditText);
        registerB = (Button) findViewById(R.id.registerButton);

        userNameET.setText(message);

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                NewUser user = new NewUser(firstNameET.getText().toString(), lastNameET.getText().toString(),
//                        emailET.getText().toString(), phoneET.getText().toString(),
//                        birthDateET.getText().toString(), userNameET.getText().toString(),
//                        passET.getText().toString());
//
//                ConversationTask task = new ConversationTask(RegisterActivity.this);
//
//                // get example
//                String requestUrlG = "http://192.168.1.17:8080/HiberContactor/RegisterServlet?" +
//                        "first_name=" + user.getFirstName() + "&last_name=" + user.getLastName() + "&email=" + user.getEmail() +
//                        "&phone=" + user.getPhone() + "&birth_date=" + user.getBirthDate() + "&nick_name=" + user.getUserName() +
//                        "&password=" + user.getPassword();
//                task.execute(requestUrlG);
//
//                 post example
//                 String requestUrlP = "http://192.168.1.17:8080/HiberContactor/RegisterServlet";
//                 task.execute(requestUrlP);
                Intent i = new Intent(RegisterActivity.this, ChatsActivity.class);
                i.putExtra("user_name",userNameET.getText().toString());
                startActivity(i);
            }
        });

    }
}
