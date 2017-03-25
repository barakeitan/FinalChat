package com.barak.eitan.firebasechat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateUserActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference userRef;
    StorageReference stoRef;
    EditText etDisp, etEmail, etPass, etBirth, etPhone;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        stoRef = FirebaseStorage.getInstance().getReference();

        etDisp = (EditText) findViewById(R.id.UUname);
        etEmail = (EditText) findViewById(R.id.UUMail);
        etPass = (EditText) findViewById(R.id.UUpass);
        etBirth = (EditText) findViewById(R.id.UUbirth);
        etPhone = (EditText) findViewById(R.id.UUphone);

        etDisp.setText(user.getDisplayName());
        etEmail.setText(user.getEmail());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                UserProfileChangeRequest.Builder builder= new UserProfileChangeRequest.Builder();
                builder.setDisplayName(etDisp.getText().toString());
                user.updateProfile(builder.build());
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
