package com.barak.eitan.firebasechat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.barak.eitan.firebasechat.RoomsActivity.ROOM_MODE_GROUPS;
import static com.barak.eitan.firebasechat.RoomsActivity.ROOM_TAG;

//import okhttp3.Request;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String serverKey = "AAAAJYp868A:APA91bG66z31owfFAfU0YyrfW6584yaxg" +
            "DjTMSVBlVrY5UN4er9r-iAFDEeEq74vJtbJm2p9gs2QGJ7qDgIE" +
            "zAj0FhsxLNcdi3SaEnslxUDqAR6btFmxqgctsy-BGmQUpsCSnmYoOst3";

    FirebaseAuth auth;
    FirebaseUser usr;
    DatabaseReference ref, grpRef, prvRef;

    int mode;
    boolean isRunning = false;

    ListView lv;
    ArrayList<MessageItem> messages;
    ArrayList<String> users; // this list will contain the uids of the users for easier search
    MessageAdapter adapter;
    EditText etMessage;

    Thread httpSender;
    ArrayList<String> requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChatRoomToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mode = getIntent().getIntExtra(RoomsActivity.ROOM_TAG,RoomsActivity.ROOM_MODE_BROADCAST);
//        if(getIntent().getBooleanExtra("isNew", true)){
//            handleRoomMode(mode);
//        }

        auth = FirebaseAuth.getInstance();
        usr  = auth.getCurrentUser();
        ref  = FirebaseDatabase.getInstance().getReference().child(getModeStringRef());
        grpRef = FirebaseDatabase.getInstance().getReference().child("users");

        etMessage = (EditText) findViewById(R.id.etMessage);
        lv = (ListView) findViewById(R.id.MessagesLv);
        messages = new ArrayList<MessageItem>();
        adapter = new MessageAdapter(this,R.layout.message_layout,messages);
        users = new ArrayList<String>();
        requestQueue = new ArrayList<String>();
        lv.setAdapter(adapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String, Object> mapTop = new HashMap<String,Object>();
//                String tmpKey = ref.push().getKey();
//                mapTop.put(tmpKey,"");
//                ref.updateChildren(mapTop);
//
//                DatabaseReference childRef = ref.child(tmpKey);
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("name",usr.getUid());
//                map.put("message", etMessage.getText().toString());
//                childRef.updateChildren(map);
                MessageItem msgitm = new MessageItem(usr.getEmail(),etMessage.getText().toString(), new Date());
                ref.push().setValue(msgitm);
                etMessage.setText("");
            }
        });
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                refreshList(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateList(dataSnapshot);
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
        });

        String token = getSharedPreferences("Firebase",0).getString("Token",null);
        users.add(token);
        isRunning = true;
        requestQueue = new ArrayList<String>();
        requestQueue.add("str : hello world");
        requestQueue.add("str : what is up");
        httpSender = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://fcm.googleapis.com/fcm/send";
                MediaType type = MediaType.parse("application/json");
                OkHttpClient client = new OkHttpClient();
                String json;
                RequestBody body;
//                Request r;
                while(isRunning){
                    if (requestQueue.size() > 0) {
                        json = requestQueue.get(0);
//                        body = RequestBody.create(type,json);
//                        r = new Request.Builder()
//                                .url(url)
//                                .addHeader("Authorization:key",serverKey)
//                                .post(body)
//                                .build();
//                        try {
//                            Response response = client.newCall(r).execute();
//                            if(response.isSuccessful()){
//                                Toast.makeText(ChatRoomActivity.this,"sent notification", Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Toast.makeText(ChatRoomActivity.this,"notification failed",Toast.LENGTH_SHORT).show();
//                            }
//                        }catch (Exception e){
//                            Log.e(LogregActivity.FIREBASE_TAG,e.getMessage());
//                        }
                        final StringRequest request = new StringRequest(Request.Method.POST, url,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(ChatRoomActivity.this,response,Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(LogregActivity.FIREBASE_TAG, Log.getStackTraceString(error));
                                        try {
                                            Log.e(LogregActivity.FIREBASE_TAG, "Error: " + error
                                                    + ">>" + error.networkResponse.statusCode
                                                    + ">>" + error.networkResponse.data
                                                    + ">>" + error.getCause()
                                                    + ">>" + error.getMessage());
                                        }catch(Exception e){
                                            Log.e(LogregActivity.FIREBASE_TAG, Log.getStackTraceString(e));
                                            Log.e(LogregActivity.FIREBASE_TAG, e.getMessage());
                                        }
                                        Toast.makeText(ChatRoomActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("to", users.get(0));
//                                params.put("data", convertToJson(requestQueue.get(0)));
                                params.put("notification","{title:'hello', body:'this is a try'}");
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type","application/json");
                                headers.put("Authorization","key=" + serverKey);
                                return headers;
                            }
                        };

                        VolleyHandler.getHandler(ChatRoomActivity.this).addToRequestQueue(request);
                        requestQueue.remove(0);
                    }
                }
            }
        });
        httpSender.start();
    }

    private String getModeStringRef() {
        String title = getIntent().getStringExtra("title");
        int mode = getIntent().getIntExtra(ROOM_TAG, ROOM_MODE_GROUPS);
        String result = "";
        switch (mode){
            case RoomsActivity.ROOM_MODE_PRIVATE:
                result = ("private/"+getIntent().getStringExtra("private")+"/msgs");
                break;
            case ROOM_MODE_GROUPS:
                result = ("rooms/"+title+"/msgs");
                grpRef = FirebaseDatabase.getInstance().getReference("rooms/"+title+"/users");
                break;
            case RoomsActivity.ROOM_MODE_BROADCAST:
                result = ("broadcasts/"+title);
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private String convertToJson(String message) {
        String tos = "";
        String json;
        Iterator iter = users.iterator();
        while(iter.hasNext()){
            String name = (String)iter.next();
            if(iter.hasNext()){
                tos = tos + name + ",";
            }
            else{
                tos += name;
            }
        }
        json = "{" +
                "'data':{" + message +
                "}," +
                "'to':" + tos +
                "}";
        return json;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isRunning = true;
//        httpSender.start();
    }

    private void handleRoomMode(int mode) {
        grpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    users.add(((DataSnapshot) i.next()).getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        switch (mode){
            case RoomsActivity.ROOM_MODE_PRIVATE:
                ref.child("private/" + getIntent().getStringExtra("private"));
                break;
            case ROOM_MODE_GROUPS:
                ref.child("groups/" + getIntent().getStringExtra("title"));
                break;
            case RoomsActivity.ROOM_MODE_BROADCAST:
                ref.child("broadcasts/" + getIntent().getStringExtra("title"));
                break;
            default:
                break;
        }
    }

    private void addUsers() {

    }

    private void refreshList(DataSnapshot dataSnapshot){
//        Iterator iterator = dataSnapshot.getChildren().iterator();
        Set<MessageItem> set = new HashSet<MessageItem>();
//        while(iterator.hasNext()){
//            MessageItem msg = ((DataSnapshot)iterator.next()).getValue(MessageItem.class);
//            set.add(msg);
//        }
        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            MessageItem item = snapshot.getValue(MessageItem.class);
            set.add(item);
        }
        messages.clear();
        messages.addAll(set);
        adapter.notifyDataSetChanged();
        etMessage.setText("");
    }

    private void updateList(DataSnapshot dataSnapshot){
        try {
            MessageItem msg = dataSnapshot.getValue(MessageItem.class);
            messages.add(msg);
            etMessage.setText("");
            adapter.notifyDataSetChanged();
            lv.setSelection(messages.size());
        }catch(Exception e){
            Log.e(LogregActivity.FIREBASE_TAG, e.getMessage());
            FirebaseCrash.logcat(Log.ERROR, LogregActivity.FIREBASE_TAG,"application crashed");
            FirebaseCrash.report(e);
        }
    }
}