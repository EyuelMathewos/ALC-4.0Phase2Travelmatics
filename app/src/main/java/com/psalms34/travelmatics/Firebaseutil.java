package com.psalms34.travelmatics;

import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;



public class Firebaseutil {
    public static FirebaseDatabase fbDatabase;
    public static DatabaseReference dbRef;
    private static Firebaseutil firebaseUtil;
    public static ArrayList<traveldeal> deals;
    public static FirebaseAuth mfirebaseAuth;
    public static FirebaseStorage fbStorage;
    public static StorageReference fbStorageRef;
    public static FirebaseAuth.AuthStateListener mauthListener;
    private static final int RC_SIGN_IN = 123;
    private static UserActivity caller;
    public static boolean isAdmin;
    private  Firebaseutil(){}
    public static void openfbReference(String ref, final UserActivity callerActivity){
        if(firebaseUtil==null){
            firebaseUtil = new Firebaseutil();
            fbDatabase = FirebaseDatabase.getInstance();
            mfirebaseAuth = FirebaseAuth.getInstance();
            //this is added
            caller = callerActivity;
            mauthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   if(firebaseAuth.getCurrentUser()==null){
                       Firebaseutil.signIn();
                   }else {
                       String userId = firebaseAuth.getUid();
                       checkAdmin(userId);
                   }

                    Toast.makeText(callerActivity.getBaseContext(),"Wellcome back!",Toast.LENGTH_LONG).show();
                }
            };
            connectStorage();

        }
        deals = new ArrayList<traveldeal>();
        dbRef = fbDatabase.getReference().child(ref);
    }
    public static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    //this is added
    public static void attachListener() {
        mfirebaseAuth.addAuthStateListener(mauthListener);
    }
    public static void detachListener() {
        mfirebaseAuth.removeAuthStateListener(mauthListener);
    }
    private static void checkAdmin(String uid) {
        Firebaseutil.isAdmin=false;
        DatabaseReference ref = fbDatabase.getReference().child("administrator").child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Firebaseutil.isAdmin=true;
                caller.showMenu();
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
        };
        ref.addChildEventListener(listener);
    }
    public static void connectStorage() {
        fbStorage = FirebaseStorage.getInstance();
        fbStorageRef = fbStorage.getReference().child("dealsPictures");
    }
}
