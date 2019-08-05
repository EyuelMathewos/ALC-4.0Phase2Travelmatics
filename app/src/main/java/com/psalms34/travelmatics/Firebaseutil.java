package com.psalms34.travelmatics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;



public class Firebaseutil {
    public static FirebaseDatabase fbDatabase;
    public static DatabaseReference dbRef;
    private static Firebaseutil firebaseUtil;
    public static ArrayList<traveldeal> deals;
    //public static FirebaseAuth firebaseAuth;
    //public static FirebaseAuth.AuthStateListener authListener;
    private  Firebaseutil(){}
    public static void openfbReference(String ref){
        if(firebaseUtil==null){
            firebaseUtil = new Firebaseutil();
            fbDatabase = FirebaseDatabase.getInstance();

        }
        deals = new ArrayList<traveldeal>();
        dbRef = fbDatabase.getReference().child(ref);
    }
}
