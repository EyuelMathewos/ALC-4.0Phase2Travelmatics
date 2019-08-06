package com.psalms34.travelmatics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {
    private FirebaseDatabase fbDatabase;
    private DatabaseReference dbRef;
    private static final int PICTURE_RESULT = 42;
    EditText title;
    EditText price;
    EditText disc;
    ImageView imageView;
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
        imageView = (ImageView) findViewById(R.id.image);
        Intent intent = getIntent();
        traveldeal deal = (traveldeal) intent.getSerializableExtra("Deal");
        if(deal==null){
            deal = new traveldeal();
        }
        this.deal = deal;
        title.setText(deal.getTitle());
        price.setText(deal.getPrice());
        disc.setText(deal.getDescription());
        showImage(deal.getImageurl());
        Button btnImage = findViewById(R.id.buttonimage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });
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
        if (Firebaseutil.isAdmin) {
            menu.findItem(R.id.deletemenu).setVisible(true);
            menu.findItem(R.id.savemenu).setVisible(true);
            enableEditTexts(true);
        }
        else {
            menu.findItem(R.id.deletemenu).setVisible(false);
            menu.findItem(R.id.savemenu).setVisible(false);
            enableEditTexts(false);
        }
        return true;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
//            Uri imageurl = data.getData();
//            StorageReference ref = Firebaseutil.fbStorageRef.child(imageurl.getLastPathSegment());
//            ref.putFile(imageurl).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    if (taskSnapshot.getMetadata() != null) {
//                        if (taskSnapshot.getMetadata().getReference() != null) {
//                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String imageUrl = uri.toString();
//                                    deal.setImageurl(imageUrl);
//
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//
//
//        }
//    }
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
        Uri imageurl = data.getData();
        final StorageReference ref = Firebaseutil.fbStorageRef.child(imageurl.getLastPathSegment());
        ref.putFile(imageurl).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String pictureName = taskSnapshot.getStorage().getPath();
               // deal.setImageName(pictureName);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        deal.setImageurl(uri.toString());
                        showImage(uri.toString());
                        showMessage();
                    }
                });
            }
        });
    }
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
        //deal.setImageurl("hello world kdkd");

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
//        if(deal.getImageName() != null && deal.getImageName().isEmpty() == false) {
//            StorageReference picRef = Firebaseutil.fbStorage.getReference().child(deal.getImageName());
//            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.d("Delete Image", "Image Successfully Deleted");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("Delete Image", e.getMessage());
//                }
//            });
//        }

    }
    private void backToList() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
    private void enableEditTexts(boolean isEnabled) {
        title.setEnabled(isEnabled);
        disc.setEnabled(isEnabled);
        price.setEnabled(isEnabled);
    }
    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
        }
    }
    private  void showMessage(){
        Toast.makeText(this,"Image Upload Complete Ready to be Saved",Toast.LENGTH_LONG).show();
    }
}
