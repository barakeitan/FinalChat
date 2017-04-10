package com.barak.eitan.firebasechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

/*
todo : - handle the on item selected to change between lists of rooms
       - add those three lists of rooms ucRooms, mcRooms, bcRooms
       - change the database to have users to contain rooms and private
       - the room will contain names and the private will contain uids
       - when creating a new room, add all participants the room name under rooms
       - have a new table for users and chat rooms.
 */
public class RoomsActivity extends AppCompatActivity {

    public static final String ROOM_TAG = "ROOM_MODE";
    public static final int ROOM_MODE_PRIVATE   = 0;
    public static final int ROOM_MODE_GROUPS    = 1;
    public static final int ROOM_MODE_BROADCAST = 2;
    int crrMode = ROOM_MODE_GROUPS;
    boolean[] checkedItems;

    ListView list;
    ArrayList<String> data, uniList, multiList, broadList;
    ArrayList<String> userNames;
    ArrayList<UserInfo> users;
    ArrayAdapter<String> adapter;
    DatabaseReference ref, uRef, usrRef, pvtRef; /*todo : remove ref and stay only with uniRef, multiRef, broadRef*/
    DatabaseReference uniRef, multiRef, broadRef;
    Map<String, Object> updateMap;
    Map<String, UserInfo> userIDs;
    Map<String,String> pvtChats;
    FirebaseUser usr;
    boolean isOpen = false;
    String UnicastChecked;
    String unicastKey = null;
    UserInfo uinfo;
    Animation fabOpen, fabClose, fabRopen, fabRclose;
    FloatingActionButton fabUc, fabMc, fabBc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chats");

        updateMap = new HashMap<String, Object>();
        userIDs = new HashMap<String, UserInfo>();
        pvtChats = new HashMap<String, String>();
        UnicastChecked = "";

        list = (ListView) findViewById(R.id.Chats_list);
        users = new ArrayList<UserInfo>();
        data = new ArrayList<String>();
        uniList = new ArrayList<String>();
        multiList = new ArrayList<String>();
        broadList = new ArrayList<String>();
        userNames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter);

        initFab();

        usr = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users/" + usr.getUid() + "/groups");
        uRef = FirebaseDatabase.getInstance().getReference().child("users/" + usr.getUid() + "/data");
        pvtRef = FirebaseDatabase.getInstance().getReference().child("users/" + usr.getUid() + "/private");
        usrRef = FirebaseDatabase.getInstance().getReference().child("users");
        uniRef = FirebaseDatabase.getInstance().getReference().child("private");
        multiRef = FirebaseDatabase.getInstance().getReference().child("rooms");
        broadRef = FirebaseDatabase.getInstance().getReference().child("broadcasts");
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
        usrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> names = new HashSet<String>();
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    UserInfo info = null;
                    try {
//                        info = snap.getValue(UserInfo.class);
                        DataSnapshot tmp = snap.getChildren().iterator().next();
                        info = tmp.getValue(UserInfo.class);
                        if(snap.getKey().equals(usr.getUid())){
                            uinfo = info;
                        }
                    } catch (Exception e) {
                        Log.e(LogregActivity.FIREBASE_TAG, e.getMessage());
                        Toast.makeText(RoomsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    users.add(info);
                    names.add(info.getDispName());
                    userIDs.put(snap.getKey(),info);
                }
                userNames.clear();
                userNames.addAll(names);
                checkedItems = new boolean[names.size()];
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pvtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> keys = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot tmp = (DataSnapshot)i.next();
                    keys.add(tmp.getKey());
                    pvtChats.put(tmp.getKey(),tmp.getValue().toString());
                }
                uniList.clear();
                uniList.addAll(keys);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        multiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> keys = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    keys.add(((DataSnapshot)i.next()).getKey());
                }
                multiList.clear();
                multiList.addAll(keys);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        broadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> keys = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    keys.add(((DataSnapshot)i.next()).getKey());
                }
                broadList.clear();
                broadList.addAll(keys);
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
//        MenuItem searchItem = menu.findItem(R.id.rooms_action_search);
//        SearchView sv = (SearchView) MenuItemCompat.getActionView(searchItem);

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
//            case R.id.rooms_action_search:
//                super.onOptionsItemSelected(item);
//                break;
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
            case R.id.rooms_action_unicast:
                displayUnicast();
                crrMode = ROOM_MODE_PRIVATE;
                break;
            case R.id.rooms_action_multicast:
                displayMulticast();
                crrMode = ROOM_MODE_GROUPS;
                break;
            case R.id.rooms_action_broadcast:
                displayBroadcast();
                crrMode = ROOM_MODE_BROADCAST;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFab(){
        fabUc = (FloatingActionButton) findViewById(R.id.FabUC);
        fabMc = (FloatingActionButton) findViewById(R.id.FabMC);
        fabBc = (FloatingActionButton) findViewById(R.id.FabBC);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open_anim);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close_anim);
        fabRopen = AnimationUtils.loadAnimation(this, R.anim.fab_ropen);
        fabRclose = AnimationUtils.loadAnimation(this, R.anim.fab_rclose);

        fabOpen.setInterpolator(new AccelerateInterpolator());
        fabClose.setInterpolator(new AccelerateInterpolator());
        fabRopen.setInterpolator(new AccelerateInterpolator());
        fabRclose.setInterpolator(new LinearInterpolator());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    fab.startAnimation(fabRclose);
                    fabBc.startAnimation(fabClose);
                    fabMc.startAnimation(fabClose);
                    fabUc.startAnimation(fabClose);
                    fabUc.setClickable(false);
                    fabMc.  setClickable(false);
                    fabBc.setClickable(false);
                    isOpen = false;
                }
                else{
                    fab.startAnimation(fabRopen);
                    fabUc.startAnimation(fabOpen);
                    fabMc.startAnimation(fabOpen);
                    fabBc.startAnimation(fabOpen);
                    fabUc.setClickable(true);
                    fabMc.setClickable(true);
                    fabBc.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fabBc.setOnClickListener(new View.OnClickListener() {
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
                        broadRef.updateChildren(map);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
                crrMode = ROOM_MODE_BROADCAST;
            }
        });
        fabMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] count = {0};
                final EditText titleText = new EditText(RoomsActivity.this);
                String[] names = new String[userNames.size()];
                userNames.toArray(names);
                titleText.setHint("enter room's name : ");
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomsActivity.this);
                builder.setTitle("choose users : ");
                builder.setMultiChoiceItems(names, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        checkedItems[i] = b;
                        if(b){
                            count[0]++;
                        }
                        else {
                            count[0]--;
                        }
                    }
                });
                builder.setPositiveButton("chat",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        AlertDialog.Builder b = new AlertDialog.Builder(RoomsActivity.this);
                        b.setTitle("name the room : ");
                        b.setView(titleText);
                        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> roomName = new HashMap<String, Object>();
                                Iterator<String> uids = userIDs.keySet().iterator();
                                String[] utu = new String[count[0]]; //users to update

                                roomName.put(titleText.getText().toString(), "");
                                multiRef.updateChildren(roomName);
                                DatabaseReference rf2 = multiRef.child(titleText.getText().toString()+"/users");
                                Map<String, Object> roomUsers = new HashMap<String, Object>();
                                int idNum = 0;
                                String name = "";
                                for (int j = 0; j < checkedItems.length; j++){
                                    name = uids.next();
                                    if(checkedItems[j]){
                                        roomUsers.put(userNames.get(j), name);
                                        if(name != null){
                                            utu[idNum] = name;
                                            idNum++;
                                        }
                                    }
                                }
                                rf2.updateChildren(roomUsers);
                                Map<String, Object> usrRoom = new HashMap<String, Object>();
                                usrRoom.put(titleText.getText().toString(), "");
                                for (int j = 0; j < utu.length; j++){
                                    usrRef.child(utu[j]+"/groups").updateChildren(usrRoom);
                                }
                            }
                        });
                        b.show();
                        crrMode = ROOM_MODE_GROUPS;
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < checkedItems.length; j++){
                            checkedItems[j] = false;
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        fabUc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomsActivity.this);
                String[] names = new String[userNames.size()];
                userNames.toArray(names);
                builder.setTitle("choose user");
                builder.setPositiveButton("chat",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (true) {
                            Map<String, Object> map = new HashMap<String, Object>();
//                        String[] res = UnicastChecked.split("gmail.com");
//                        res[0] = res[0].substring(0,res[0].length() - 1);
//                            uniRef.updateChildren(map);
                            unicastKey = uniRef.push().getKey();
                            map.put(unicastKey, "");
                            uniRef.updateChildren(map);
                            map.clear();
                            map.put(UnicastChecked, unicastKey);
                            pvtRef.updateChildren(map);
                            map.clear();
                            map.put(uinfo.getDispName(), unicastKey);
                            Set<Map.Entry<String, UserInfo>> set = userIDs.entrySet();
                            Iterator<Map.Entry<String, UserInfo>> iter = set.iterator();
                            String id = null;
                            while (iter.hasNext() && id == null) {
                                Map.Entry<String, UserInfo> entry = iter.next();
                                if (entry.getValue().getDispName().equals(UnicastChecked)) {
                                    id = entry.getKey();
                                }
                            }
                            if (id != null) {
                                DatabaseReference rf2 = FirebaseDatabase.getInstance().getReference().child("users/" + id + "/private");
                                rf2.updateChildren(map);
                                map.clear();
                            }
                        }
                    }
                });
                builder.setSingleChoiceItems(names, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UnicastChecked = userNames.get(i);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                crrMode = ROOM_MODE_PRIVATE;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RoomsActivity.this, ChatRoomActivity.class);
                intent.putExtra("title", ((TextView)view).getText().toString());
                intent.putExtra(ROOM_TAG,crrMode);
                if(crrMode == ROOM_MODE_PRIVATE){
                    intent.putExtra("private",pvtChats.get(((TextView)view).getText().toString()));
                }
                startActivity(intent);
            }
        });
    }
    private void displayUnicast(){
        data.clear();
        data.addAll(uniList);
        adapter.notifyDataSetChanged();
    }

    private void displayMulticast(){
        data.clear();
        data.addAll(multiList);
        adapter.notifyDataSetChanged();
    }

    private void displayBroadcast(){
        data.clear();
        data.addAll(broadList);
        adapter.notifyDataSetChanged();
    }
}
