package com.example.clik.Feed;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clik.Adapter.UploadListAdpater;
import com.example.clik.MainActivity;
import com.example.clik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {

    private LinearLayout chooseImage;
    private LinearLayout cameraImg;
    private TextView post;
    private SocialAutoCompleteTextView discription;
    private RecyclerView photoRecyclr;

    private static final int IMAGE_PICK_CODE = 1;
    private static final int CAMERA_CODE = 2;
    private static final int PERMISSION_CODE_GALLERY = 1001;
    private static final int PERMISSION_CODE_CAMERA = 1002;
    private static int totalItemsSelected = 0;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<String> downloadUri;

    private StorageReference mStorage;
    private FirebaseUser fuser;

    private UploadListAdpater uploadListAdpater;
    private String postId;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        chooseImage = findViewById(R.id.add_img);
        post = findViewById(R.id.post);
        photoRecyclr = findViewById(R.id.post_images_recycler);
        discription = findViewById(R.id.description);
        cameraImg = findViewById(R.id.add_img_camera);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        downloadUri = new ArrayList<>();
        downloadUri.clear();
        fileNameList.clear();
        fileDoneList.clear();

        uploadListAdpater = new UploadListAdpater(fileNameList, fileDoneList);

        photoRecyclr.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclr.setHasFixedSize(true);
        photoRecyclr.setAdapter(uploadListAdpater);

        mStorage = FirebaseStorage.getInstance().getReference().child("posts");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allUpload()) {
                    uploadPost();
                } else {
                    Toast.makeText(AddPostActivity.this, "photos are not uploade till now", Toast.LENGTH_SHORT).show();
                }

            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE_GALLERY);

                } else {
                    pickImageFromGallery();
                }
            }
        });

        cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraHardware(AddPostActivity.this)) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.CAMERA};
                        requestPermissions(permissions, PERMISSION_CODE_CAMERA);

                    } else {
                        dispatchTakePictureIntent();
                    }
                }else{
                    Toast.makeText(AddPostActivity.this, "Device Do Not Have Camera", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean allUpload() {
        boolean isComplete = false;
        if (totalItemsSelected == fileDoneList.size()) {
            isComplete = true;
        }
        return isComplete;
    }

    private void uploadPost() {
        pd = new ProgressDialog(this);
        pd.setMessage("Adding Post");
        pd.show();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");

        postId = ref.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("discription", discription.getText().toString().trim());
        map.put("publisher", fuser.getUid());
        map.put("postId", postId);

        assert postId != null;
        ref.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < downloadUri.size(); i++) {
                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("imageUri", downloadUri.get(i));
                        ref.child(postId).push().setValue(map2);
                        map2.clear();
                    }
                    addHashTags();
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this, "Post Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddPostActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });
    }

    private void addHashTags() {
        DatabaseReference Hashtagref = FirebaseDatabase.getInstance().getReference().child("HashTags");
        List<String> hashTags = discription.getHashtags();
        if (!hashTags.isEmpty()) {
            for (String tag : hashTags) {
                HashMap<String, String> map = new HashMap<>();
                map.put("tag", tag.toLowerCase());
                map.put("postId", postId);

                Hashtagref.child(tag.toLowerCase()).setValue(map);
            }
        }

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(AddPostActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            case PERMISSION_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(AddPostActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ProgressDialog pd2 = new ProgressDialog(AddPostActivity.this);
        pd2.setMessage("Loading");
        pd2.show();
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getClipData() != null) {
                totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = System.currentTimeMillis() + "." + getFileExtension(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");

                    final StorageReference fileToUpload = mStorage.child("Images").child(fileName);

                    final int finalI = i;

                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String uri = fileToUpload.getDownloadUrl().toString();

                            downloadUri.add(uri);

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");

                            uploadListAdpater.notifyDataSetChanged();

                            pd2.dismiss();

                        }
                    });

                }

            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                uploadSingleImage(fileUri);
                pd2.dismiss();

            }

        }

        if(resultCode == RESULT_OK && requestCode == CAMERA_CODE){
            File f = new File(currentPhotoPath);
            assert data != null;
            Uri ImageUri = Uri.fromFile(f);
            uploadSingleImage(ImageUri);
            pd2.dismiss();
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.clik.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CODE);
            }
        }
    }

    private void uploadSingleImage(Uri fileUri) {
        String fileName = System.currentTimeMillis() + "." + getFileExtension(fileUri);

        fileNameList.add(fileName);
        fileDoneList.add("uploading");

        final StorageReference fileToUpload = mStorage.child("Images").child(fileName);

        fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String uri = fileToUpload.getDownloadUrl().toString();

                downloadUri.add(uri);
                fileDoneList.add("done");

                uploadListAdpater.notifyDataSetChanged();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

}