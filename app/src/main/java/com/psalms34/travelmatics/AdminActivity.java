package com.psalms34.travelmatics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    EditText title;
    EditText price;
    EditText disc;
    traveldeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminactivity);
        fbDatabase = FirebaseDatabase.getInstance();
        dbRef = fbDatabase.getReference().child("traveldeal");
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        disc = findViewById(R.id.description);
        Intent intent = getIntent();
        traveldeal deal = (traveldeal) intent.getSerializableExtra("Deal");
        if(deal==null){
            deal = new traveldeal();
        }
        this.deal = deal;
        title.setText(deal.getTitle());
        price.setText(deal.getPrice());
        disc.setText(deal.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.savemenu:
                saveDeal();
                Toast.makeText(this,"Save menu is selected",Toast.LENGTH_LONG).show();
                clean();
                backToList();
                break;
            case R.id.deletemenu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show();
                backToList();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }
    public void saveDeal(){
//    String stringTitle= title.getText().toString();
//    String stringprice= price.getText().toString();
//    String stringDescr= disc.getText().toString();
//    traveldeal deal = new traveldeal(stringTitle,stringprice,stringDescr,"");
//        dbRef.push().setValue(deal);
        deal.setTitle(title.getText().toString());
        deal.setDescription(disc.getText().toString());
        deal.setPrice(price.getText().toString());
        if(deal.getId()==null) {
            dbRef.push().setValue(deal);
        }
        else {
            dbRef.child(deal.getId()).setValue(deal);
        }
    }
    public void clean(){
        title.setText("");
        price.setText("");
        disc.setText("");
        title.requestFocus();
    }
    private void deleteDeal() {
        if (deal == null) {
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
        }
        dbRef.child(deal.getId()).removeValue();

    }
    private void backToList() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
}
