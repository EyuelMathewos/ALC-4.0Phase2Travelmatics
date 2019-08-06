package com.psalms34.travelmatics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    ArrayList <traveldeal> deals;
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    private ChildEventListener datachildlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
    //this is menu add
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activityusermenu, menu);
        MenuItem insertMenu = menu.findItem(R.id.insertMenu);
        if (Firebaseutil.isAdmin) {
            insertMenu.setVisible(true);
        }
        else {
            insertMenu.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insertMenu:
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                return true;
            case R.id.logoutMenu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User Logged Out");
                                Firebaseutil.attachListener();
                            }
                        });
                Firebaseutil.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //this is added
    protected void onPause(){
        super.onPause();
        Firebaseutil.detachListener();
    }
    protected void onResume(){
        super.onResume();
        Firebaseutil.openfbReference("traveldeal",this);
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvdeal);
        final DealAdapter adapter =new DealAdapter();
        rvDeals.setAdapter(adapter);
        //LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(dealLayoutManager);
        // Firebaseutil.openfbReference("traveldeals",this);
        Firebaseutil.attachListener();
    }
    public void showMenu() {
        invalidateOptionsMenu();
    }
}
