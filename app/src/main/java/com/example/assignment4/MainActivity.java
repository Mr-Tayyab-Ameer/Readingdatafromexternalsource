package com.example.assignment4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;
    private Button selectImageButton;
    private Button saveImageButton;
    private Button deleteButton;
    private Button dis;
    private EditText imgid;
    private EditText imgtemp;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent in=getIntent();
        imageView = findViewById(R.id.img);
        selectImageButton = findViewById(R.id.insbtn);

        saveImageButton = findViewById(R.id.savebtn);
        imgid = findViewById(R.id.imageid);
        imgtemp = findViewById(R.id.imagetemp);
        deleteButton = findViewById(R.id.delbtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageFromDatabase();
            }
        });
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToDatabase();
            }
        });
    }
    private void saveImageToDatabase() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String imgtempa=imgtemp.getText().toString();
        String text=imgid.getText().toString();
        int imagid=Integer.parseInt(text);
        long newRowId=db.insertImage(imagid,imgtempa,bitmap);
        if (newRowId == -1) {
            Toast.makeText(this, "Error saving image to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Image saved to database", Toast.LENGTH_SHORT).show();
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void deleteImageFromDatabase() {
        String text=imgid.getText().toString();
        int imagid=Integer.parseInt(text);
        boolean deleted=db.delete(imagid);
        if (deleted) {
            Toast.makeText(this, "Image deleted from database", Toast.LENGTH_SHORT).show();
            // Optionally, you can reset the ImageView to a placeholder image or clear it
            // imageView.setImageResource(R.drawable.placeholder); // Placeholder image
        } else {
            Toast.makeText(this, "Error deleting image from database", Toast.LENGTH_SHORT).show();
        }
    }


}
