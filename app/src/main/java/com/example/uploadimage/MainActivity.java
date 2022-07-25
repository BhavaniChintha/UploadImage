package com.example.uploadimage;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button uploadImage;


    private ArrayList<String> imagePathList;
    private String imagePath;
    private RecyclerView rv_upload_gallery;
    private AdapterGallery adapterGallery;
    private ArrayList<Uri> arrayList_gallery=new ArrayList<>();
    private RoundedImageView iv_upload_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_upload_gallery = findViewById(R.id.rv_upload_gallery);
        iv_upload_gallery = findViewById(R.id.iv_upload_gallery);

        adapterGallery = new AdapterGallery(arrayList_gallery);
        rv_upload_gallery.setAdapter(adapterGallery);

        rv_upload_gallery.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        uploadImage=findViewById(R.id.upload_image_btn);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Pictures: "), 1);
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                }
            }
        });
    }

    private boolean checkPer() {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            if ((ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                (MainActivity.this).requestPermissions(new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, 22);
                return false;
            } else {
                return true;
            }
        } else {
            if ((ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (MainActivity.this).requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 22);
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            imageView.setImageURI(imageUri);
//        }
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        arrayList_gallery.add(data.getClipData().getItemAt(i).getUri());
                    }
                    adapterGallery.notifyDataSetChanged();
                }else if(data.getData()!=null){
                    Uri uri=data.getData();
                    arrayList_gallery.add(uri);
                    adapterGallery.notifyDataSetChanged();
                }
            }
        }
    }

    @SuppressLint("Range")
    private void getImageFilePath(Uri uri) {
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();
        }
    }


}