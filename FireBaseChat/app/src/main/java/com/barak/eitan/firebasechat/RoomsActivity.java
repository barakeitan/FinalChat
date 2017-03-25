package com.barak.eitan.firebasechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RoomsActivity extends AppCompatActivity {

    public static final String ROOM_TAG = "ROOM_MODE";
    public static final int ROOM_MODE_PRIVATE   = 0;
    public static final int ROOM_MODE_GROUPS    = 1;
    public static final int ROOM_MODE_BROADCAST = 2;

    ListView list;
    ArrayList<String> data;
    ArrayAdapter<String> adapter;
    DatabaseReference ref, uRef;
    Map<String, Object> updateMap;
    FirebaseUser usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");

        updateMap = new HashMap<String, Object>();

        list = (ListView) findViewById(R.id.Chats_list);
        data = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RoomsActivity.this);
                dialog.setTitle("room name : ");
                final EditText etName = new EditText(RoomsActivity.this);
                etName.setHint("enter room's name... ");
                dialog.setView(etName);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put(etName.getText().toString(), "");
                        ref.updateChildren(map);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RoomsActivity.this, ChatRoomActivity.class);
                intent.putExtra("title", ((TextView)view).getText().toString());
//                intent.putExtra(ROOM_TAG,ROOM_MODE_BROADCAST);
                startActivity(intent);
            }
        });
        usr = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("rooms");
        uRef = FirebaseDatabase.getInstance().getReference().child("users/" + usr.getUid());
        updateMap.put("isOnline",true);
        uRef.updateChildren(updateMap);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> keys = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    keys.add(((DataSnapshot)i.next()).getKey());
                }
                data.clear();
                data.addAll(keys);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rooms_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.rooms_action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateMap.put("isOnline",false);
        uRef.updateChildren(updateMap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rooms_action_search:
                super.onOptionsItemSelected(item);
                break;
            case R.id.rooms_action_signout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                finish();
                item.setTitle("exit");
                break;
            case R.id.rooms_action_update:
                Intent i = new Intent(this, UpdateUserActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
