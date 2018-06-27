package com.mikalh.purchaseorderonline;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikalh.purchaseorderonline.Model.User;
import com.mikalh.purchaseorderonline.Pager.ProfilePagger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    public static String IDSeller= "ID";
    TabLayout profile_tab;
    ViewPager profile_pagger;
    FloatingActionButton changImage_profile;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    ProfilePagger adapter;
    String userID;
    CircleImageView imageProfile;
    FloatingActionButton changeImage_profile;
    FirebaseStorage storage;
    StorageReference storageReference;
    int PICK_IMAGE_REQUEST = 1;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Uri filePath, imageResult;
    String ImageURL;
    User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = getIntent().getExtras().getString(home.USER_ID);
        profile_tab = findViewById(R.id.profile_tab);
        profile_pagger = findViewById(R.id.profile_pagger);
        imageProfile = findViewById(R.id.imageProfile);
        changeImage_profile = findViewById(R.id.changeImage_profile);
        changeImage_profile.setOnClickListener(this);
        firestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                userModel = snapshot.toObject(User.class);
                Glide.with(imageProfile.getContext()).load(userModel.getUrl_pictLogo()).into(imageProfile);
            }
        });
        changImage_profile = findViewById(R.id.changeImage_profile);
        if (!user.getUid().equals(userID)) {
            changImage_profile.hide();
        }
        adapter = new ProfilePagger(getSupportFragmentManager(), 2);
        profile_pagger.setAdapter(adapter);
        profile_pagger.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(profile_tab));
        profile_tab.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        profile_pagger.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void popUpAddFoto() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_add_picture);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final Button cameraPicture_add = dialog.findViewById(R.id.cameraPicture_add);
        final Button filePicture_add = dialog.findViewById(R.id.filePicture_add);
        cameraPicture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(Profile.this, permissionsRequired)) {
                    requestPermissions(permissionsRequired, 1);
                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM/Camera", "IMG_lwlwlwlwlwwl.jpg");
                    filePath = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                    startActivityForResult(i, 0);
                    dialog.dismiss();
                }

            }
        });
        filePicture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(Profile.this, permissionsRequired)) {

                    requestPermissions(permissionsRequired, 1);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                final Bitmap bitmap;
                bitmap = decodeSampledBitmapFromUri(filePath, imageProfile.getWidth(), imageProfile.getHeight());
                imageProfile.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 0 && resultCode == RESULT_OK && filePath != null) {
            Matrix mat = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(filePath.getPath());
                String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                int rotateangle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                    rotateangle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                    rotateangle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                    rotateangle = 270;
                mat.setRotate(rotateangle, (float) imageProfile.getWidth() / 2, (float) imageProfile.getHeight() / 2);

            } catch (IOException e) {
                Log.e("Image Error path", e.getMessage());
            }
            final Bitmap bitmap;
            bitmap = decodeSampledBitmapFromUri(filePath, imageProfile.getWidth(), imageProfile.getHeight());
            imageProfile.setImageBitmap(bitmap);
            uploadImage();
        }

    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)
                    , null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + user.getUid());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    imageResult = task.getResult();
                    final UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(filePath).build();
                    user.updateProfile(profileChangeRequest).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Crashlytics.logException(e);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    firestore.collection("Users").document(user.getUid()).update("url_pictLogo", imageResult.toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Crashlytics.logException(e);
                            progressDialog.dismiss();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Suscess change Profile", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Crashlytics.logException(e);
                    progressDialog.dismiss();
                }
            });

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View view) {
        if (view == changeImage_profile) {
            popUpAddFoto();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!user.getUid().equals(userID)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.goto_catalogue, menu);
            return super.onCreateOptionsMenu(menu);
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.catalogue_company:
                Intent i = new Intent(getApplicationContext(), SearchResult.class);
                i.putExtra(IDSeller, userID);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }
}
