package com.psalms34.travelmatics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        Firebaseutil.openfbReference("traveldeals");
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvdeal);
        final DealAdapter adapter =new DealAdapter();
        rvDeals.setAdapter(adapter);
        //LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        rvDeals.setLayoutManager(dealLayoutManager);
       // Firebaseutil.openfbReference("traveldeals",this);

    }
//    protected void onPause(){
//        super.onPause();
//        Firebaseutil.detachListener();
//    }
//    protected void onResume(){
//        super.onResume();
//        Firebaseutil.attachListener();
//    }
}
