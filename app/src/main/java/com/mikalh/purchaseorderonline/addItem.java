package com.mikalh.purchaseorderonline;

import android.*;
import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.TextWatcher.CurcurencyFormater;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class addItem extends AppCompatActivity {
    EditText namaItem_add, hargaItem_add,unitItem_add,estimasiWaktu;
    Spinner pilihanWaktu,Kategori;
    ImageView imageItem_add;
    Button addItemDo;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    //Image
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Uri outputFile;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private FirebaseStorage storage;
    StorageReference ItemImage;
    StorageReference storagePath;
    byte[] dataImage;
    Uri urlImage;
    ArrayList<String> imageList = new ArrayList<>();
    Company company = new Company();
    String instanceId = FirebaseInstanceId.getInstance().getToken();
    String[] kategori = {"Barang Baku","Barang Mentah","Barang Dasar","Dll"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_add_item);
        // Firebase Setting all
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storagePath = storage.getReference();

        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    company = snapshot.toObject(Company.class);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error get data",e.getMessage());
            }
        });

        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        namaItem_add = findViewById(R.id.namaItem_add);
        //jenisItem_add = findViewById(R.id.jenisItem_add);
        unitItem_add = findViewById(R.id.unitItem_add);
        hargaItem_add = findViewById(R.id.hargaItem_add);
        addItemDo = findViewById(R.id.addItemDo);
        imageItem_add = findViewById(R.id.itemImage_add);
        Kategori = findViewById(R.id.Kategori);
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        ArrayAdapter kategoriAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,kategori);
        Kategori.setAdapter(kategoriAdapter);



        addItemDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
        imageItem_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(addItem.this,permissionsRequired)) {

                    requestPermissions(permissionsRequired,1);
                }else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"DCIM/Camera","IMG_lwlwlwlwlwwl.jpg");
                    outputFile = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
                    startActivityForResult(i,0);

                }
            }
        });

        // Camera

        // Creating image here

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK){
                        Log.d("Tag","outputFileUri Result_ok"+outputFile);
                        if (outputFile != null){
                            Matrix mat = new Matrix();
                            try {
                                ExifInterface exif = new ExifInterface(outputFile.getPath());
                                String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                                int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                                int rotateangle = 0;
                                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                                    rotateangle = 90;
                                if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                                    rotateangle = 180;
                                if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                                    rotateangle = 270;
                                mat.setRotate(rotateangle, (float) imageItem_add.getWidth() / 2, (float) imageItem_add.getHeight() / 2);

                            } catch (IOException e) {
                                Log.e("Image Error path",e.getMessage());
                            }
                            final Bitmap bitmap;
                            bitmap = decodeSampledBitmapFromUri(outputFile,imageItem_add.getWidth(),imageItem_add.getHeight());
                        /*if (databasePicture.Checker() != 0){
                            databasePicture.clearTable();
                        }*/

                            if (bitmap == null){
                                Toast.makeText(getApplicationContext(),"The Image Data Currop",Toast.LENGTH_LONG).show();
                            }else {
                                final CustomDialog customDialog = new CustomDialog(addItem.this);
                                customDialog.show();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                dataImage = baos.toByteArray();
                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                ItemImage = storagePath.child(user.getUid()+"/"+ts+".jpg");

                                UploadTask uploadTask = ItemImage.putBytes(dataImage);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        customDialog.dismiss();
                                        Log.e("Error Upload Gambar",e.getMessage());
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        urlImage = taskSnapshot.getDownloadUrl();
                                        imageItem_add.setImageBitmap(bitmap);
                                        customDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                    break;
                default:
                    Toast.makeText(this, "Something went wrong...",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    public static class GetImageThumbnail {

        private static int getPowerOfTwoForSampleRatio(double ratio) {
            int k = Integer.highestOneBit((int) Math.floor(ratio));
            if (k == 0)
                return 1;
            else
                return k;
        }

        public Bitmap getThumbnail(Uri uri, Context context)
                throws FileNotFoundException, IOException {
            InputStream input = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;// optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1)
                    || (onlyBoundsOptions.outHeight == -1))
                return null;

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
                    : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > 400) ? (originalSize / 350) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            input = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        }
    }



    void saveItem(){
        final CustomDialog customDialog = new CustomDialog(addItem.this);
        customDialog.show();
        String namaBarang = namaItem_add.getText().toString();
        String HargaBarang = hargaItem_add.getText().toString();
        String userId = user.getUid();
        String unitItem = unitItem_add.getText().toString();
        String urlItemBarang="";
        String kategori = Kategori.getSelectedItem().toString();
        if (urlImage!=null) {
            urlItemBarang = urlImage.toString();
        }

        Item item = new Item(namaBarang,userId,unitItem,company.getNama_perusahaan(),HargaBarang,urlItemBarang,instanceId,kategori);

        firestore.collection("Items").document()
                .set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(addItem.this,"item added Success",Toast.LENGTH_LONG).show();
                    customDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addItem.this,e.getMessage(),Toast.LENGTH_LONG).show();
                customDialog.dismiss();
            }
        });

    }



    public Bitmap decodeSampledBitmapFromUri(Uri uri,int reqWidth, int reqHeight){
        Bitmap bm = null;
        try{
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(addItem.this.getContentResolver().openInputStream(uri),null ,options);
            options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
            options.inJustDecodeBounds = false;

            bm = BitmapFactory.decodeStream(addItem.this.getContentResolver().openInputStream(uri)
            ,null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                super.onBackPressed();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
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
}
