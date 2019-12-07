package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.DB;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.FileToUpload;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.ToastCompletionListener;
import com.groupassignment2019.bartertraderappgithuballsetup.models.Category;
import com.groupassignment2019.bartertraderappgithuballsetup.models.ItemData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostNewItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int PICK_VIDEO = 42;
    private static final int PICK_IMAGE = 41;
    private ImageView imageViewNewItemImage;
    private Button addVideoButton;
    private EditText editTextItemDescription;
    private EditText editTextNewItemTitle;
    private Button PostItemButton;
    private ProgressBar progressBar;



    private FirebaseUser user;
    private Uri imageLocalURI;
    private Uri videoLocalURI;
    private Button WatchVideoButton;
    private String categorySelected;
    private String newItemIDkey;
    private List<FileToUpload> filesToUpload;
    private ItemData newItem;
    private Uri uploadedImageURL;
    private Uri uploadedVideoURL;
    private boolean onlyPicture = true;
    private String newItemVideoFirebaseFilename;
    private String newItemImageFirebaseFilename;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case PICK_IMAGE:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "you heave to select some picture, sorry", Toast.LENGTH_LONG).show();
                    WatchVideoButton.setVisibility(View.GONE);
                    return;
                }
                if (data == null || data.getData() == null) {
                    Toast.makeText(this, "data is nulll   you heave to select some picture, sorry", Toast.LENGTH_LONG).show();
                    WatchVideoButton.setVisibility(View.GONE);
                    return;
                }
                imageLocalURI = data.getData();
                Picasso.get().load(imageLocalURI).into(imageViewNewItemImage);
                break;

            case PICK_VIDEO:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "video was not selected ", Toast.LENGTH_LONG).show();
                    WatchVideoButton.setVisibility(View.GONE);
                    return;
                }
                if (data == null || data.getData() == null) {
                    Toast.makeText(this, "data is nulll  video", Toast.LENGTH_LONG).show();
                    WatchVideoButton.setVisibility(View.GONE);
                    return;
                }
                videoLocalURI = data.getData();
                WatchVideoButton.setVisibility(View.VISIBLE);
                Toast.makeText(this, "vid " + videoLocalURI.toString(), Toast.LENGTH_LONG).show();
                Log.d("BOLO","vid URl :  "+ videoLocalURI.toString());
                break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_item);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        WatchVideoButton = findViewById(R.id.viewVideoButton);
        imageViewNewItemImage = (ImageView) findViewById(R.id.imageViewNewItemImage);
        addVideoButton = (Button) findViewById(R.id.addVideoButton);
        editTextItemDescription = (EditText) findViewById(R.id.editTextItemDescription);
        editTextNewItemTitle = (EditText) findViewById(R.id.editTextNewItemTitle);
        PostItemButton = (Button) findViewById(R.id.PostItemButton);

        user = DB.me;

        imageViewNewItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        WatchVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //videoLocalUri will not be null because button is gone if this is the case
                Intent intent = new Intent(PostNewItemActivity.this, VideoActivity.class);
                intent.putExtra("url",videoLocalURI.toString());
                startActivity(intent);
            }
        });

        PostItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_prepareUploads_SaveItemToDB();
            }
        });


        Spinner spinner = findViewById(R.id.spinner);

        final List<String> categoriesList = new ArrayList<>();
        categoriesList.add("Please select Category");
        categoriesList.addAll(Arrays.asList(Category.CATEGORIES));


        //ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.Cathegories,android.R.layout.simple_spinner_item );
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinnerstyle, categoriesList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinnerstyle);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = parent.getItemAtPosition(position).toString().toLowerCase();
        Toast.makeText(this, categorySelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        categorySelected = null;
        Toast.makeText(this, "you have to select one category", Toast.LENGTH_SHORT).show();
    }


    public void validate_prepareUploads_SaveItemToDB() {
        String imageViewNewItemImageValue;
        InputValidator iv = new InputValidator(this);

        boolean ok = iv.notEmpty(editTextNewItemTitle) && iv.notEmpty(editTextItemDescription) && imageLocalURI != null && categorySelected != null;
        if (!ok) {
            Toast.makeText(this, "complete the required data: image, description and title", Toast.LENGTH_LONG).show();
            return;
        }

        if (newItemIDkey == null) {
            newItemIDkey = DB.items.push().getKey();
        }

        newItem = new ItemData();
        newItem.setCategory(categorySelected);
        newItem.setId(newItemIDkey);
//        newItem.setPictureURI();
        newItem.setSeller_user_UUID(DB.myUid());
        newItem.setTitle(editTextNewItemTitle.getText().toString());
        newItem.setDescription(editTextItemDescription.getText().toString());
        newItemImageFirebaseFilename = newItemIDkey + getFileExtension(imageLocalURI);

       // filesToUpload = new ArrayList<>();
        FileToUpload imageFile = new FileToUpload(imageLocalURI, newItemImageFirebaseFilename, "item_images");
       // filesToUpload.add(imageFile);
progressBar.setVisibility(View.VISIBLE);
PostItemButton.setEnabled(false);

        if (videoLocalURI != null) {
            newItemVideoFirebaseFilename = newItemIDkey + getFileExtension(videoLocalURI);
            FileToUpload videoFile = new FileToUpload(videoLocalURI, newItemVideoFirebaseFilename, "item_videos");
           // filesToUpload.add(videoFile);
            onlyPicture = false;
            // todo upload picture
            uploadVideoFileAndCheckIfAllCompleted(videoFile);
        }
// what if only imag is uploaded , how do i tell when everything is completed ? RxJava seems overkill.
        // create list of files (only one or two files per item) and upload them all at each success marking item as done
        // in the future it may allow to upload many images with easyness

        //final int uploadURIcount = filesToUpload.size();

            uploadImageFileAndCheckIfAllCompleted(imageFile);



//        String videoToUploadPathToFile;
//        String addedVideoDownnlloadURL;//upload video;
//        String editTextVideoDescriptionValue = editTextItemDescription.getText().toString();
//        String editTextNewItemTitleValue = editTextNewItemTitle.getText().toString();
//        //String PostItemButtonValue =  PostItemButton.getText().toString();
    }

    private String getFileExtension(Uri uri){
        return "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    private void uploadVideoFileAndCheckIfAllCompleted(final FileToUpload fileToUpload) {
        final StorageReference uploadRef = FirebaseStorage.getInstance()
                .getReference("item_videos") // path contains appropriate folder , but now no list of files so no need for method call
                .child(fileToUpload.getRemoteFileName()); // filename
        uploadRef.putFile(fileToUpload.getLocalUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
Log.d("BOLO","success uploading video file");
                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("BOLO","success getting url downlad video file");
                        // TODO: 04/12/2019 Picture is not saved ?!
                        // todo make it dead simple two separate

//                        if (uri == null){
//                            throw new NullPointerException("image added but firebase send null back ?!!!!");
//                        }
//
                        PostItemButton.setText("video ok");
//                        fileToUpload.setDownloadUri(uri);
                        uploadedVideoURL = uri;
                        IfAllCompletedSaveToDB();
                    }
                });
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(PostNewItemActivity.this, " failed to upload the video - cancelled", Toast.LENGTH_SHORT).show();
            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostNewItemActivity.this, " failed to upload the video", Toast.LENGTH_SHORT).show();
                //should delete any item references from DB
            }
        });
    }

    private void uploadImageFileAndCheckIfAllCompleted(final FileToUpload fileToUpload) {
        final StorageReference uploadRef = FirebaseStorage.getInstance()
                .getReference("item_images") // path contains appropriate folder
                .child(fileToUpload.getRemoteFileName()); // filename
        uploadRef.putFile(fileToUpload.getLocalUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Log.d("BOLO","success uploading image file");
                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("BOLO","success getting url of image file");
                        // TODO: 04/12/2019 Picture is not saved ?!
                        // todo make it dead simple  - two separate ones , array of files to upload didnt work
                        if (uri == null){
                            throw new NullPointerException("image added but firebase send null back ?!!!!");
                        }
                        //fileToUpload.setDownloadUri(uri);
                        uploadedImageURL = uri;
                        IfAllCompletedSaveToDB();
                    }
                });
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(PostNewItemActivity.this, " failed to upload the picture - cancelled", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostNewItemActivity.this, " failed to upload the picture", Toast.LENGTH_SHORT).show();
                //should delete any item references from DB
            }
        });
    }

    /**
     * this function is called each time when file has finished uploading
     */
    private void IfAllCompletedSaveToDB() {
        // TODO: 04/12/2019 Picture is not saved ?!
Log.d("BOLO","ifallcompletedSaveTodb");
        //check if picture uploaded
        if (onlyPicture && uploadedImageURL != null){
            saveItemToDB();
        }

        if (!onlyPicture && uploadedImageURL != null && uploadedVideoURL != null) {
            saveItemToDB();
        }
//        boolean allCompleted = true;
//        // logical AND on all files being uploaded
//        for (int idx = 0; idx < filesToUpload.size(); idx++) {
//            FileToUpload file = filesToUpload.get(idx);
//            boolean fileCompleted = file.isCompleted() && file.getDownloadUri() != null; //todo redundant but somehow it was null
//            if (file.isCompleted() && file.getDownloadUri() != null) {
//                if (uploadedImageURL == null || file.getDownloadUri() == null){
//                    throw new NullPointerException("image added but firebase send null back ?!!!!");
//                }
//                switch (file.getRemotePath()) {
//                    case "item_images/":
//                        newItem.setPictureURI(String.valueOf(uploadedImageURL));
//                        break;
//                    case "item_videos/":
//                        newItem.setVideoURI(String.valueOf(uploadedVideoURL));
//                }
//            }
//            allCompleted = allCompleted && fileCompleted;
//        }
//
//        if (allCompleted && filesToUpload.size() > 0) {
//            saveItemToDB();
//        }
    }

    private void saveItemToDB() {
        if (uploadedVideoURL != null){
            newItem.setVideoURI(String.valueOf(uploadedVideoURL));// will be null if no video - its ok for Firebase
        }
        newItem.setPictureURI(String.valueOf(uploadedImageURL));

        ItemData fofofo = newItem;
       Log.d("BOLO","about to save to db");
        DB.items.child(newItemIDkey).setValue(newItem).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);
                            DB.items_by_user.child(DB.myUid()).child(newItemIDkey).setValue(true).addOnCompleteListener(new ToastCompletionListener(PostNewItemActivity.this, "items_by_user "));
                            DB.categories.child(categorySelected).child(newItemIDkey).setValue(true).addOnCompleteListener(new ToastCompletionListener(PostNewItemActivity.this, "categories "));
                            Intent intent = new Intent(PostNewItemActivity.this, ItemDetailsActivity.class);
                            intent.putExtra(ItemDetailsActivity.EXTRA_ITEM_KEY, newItem);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
