package com.mikalh.purchaseorderonline;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class registerUser extends android.support.v4.app.Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText email_register, name_register, position_register, username_register,password_register;
    Button registerBtn;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    registerCompanyProfile previous;
    private OnFragmentInteractionListener mListener;
    CustomDialog cd;
    String instanceId;
    CircleImageView imagePerusahaan_add;
    FirebaseStorage storage;
    StorageReference storageReference;
    int PICK_IMAGE_REQUEST = 1;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Uri filePath,imageResult;
    String ImageURL;
    public registerUser() {
    }

    public static registerUser newInstance(String param1, String param2) {
        registerUser fragment = new registerUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    SharedPreferences mPrefs;
    TextInputLayout TIL_emailRegis,TIL_picRegis,TILpicPosRegis,TIL_usernameRegis,TIL_passRegis;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        previous = new registerCompanyProfile();
        instanceId = FirebaseInstanceId.getInstance().getToken();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_user, container, false);
        imagePerusahaan_add = v.findViewById(R.id.imagePerusahaan_add);
        email_register = v.findViewById(R.id.email_register);
        name_register = v.findViewById(R.id.name_register);
        position_register = v.findViewById(R.id.position_register);
        username_register = v.findViewById(R.id.username_register);
        password_register = v.findViewById(R.id.password_register);
        TIL_emailRegis = v.findViewById(R.id.TIL_emailRegis);
        TIL_passRegis = v.findViewById(R.id.TIL_passRegis);
        TIL_picRegis = v.findViewById(R.id.TIL_picRegis);
        TILpicPosRegis = v.findViewById(R.id.TIL_picPosRegis);
        TIL_usernameRegis = v.findViewById(R.id.TIL_usernameRegis);
        registerBtn = v.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        imagePerusahaan_add.setOnClickListener(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view == registerBtn){
            Boolean register = true;
            TIL_passRegis.setError(null);
            TIL_passRegis.setErrorEnabled(false);
            TIL_usernameRegis.setError(null);
            TIL_usernameRegis.setErrorEnabled(false);
            TILpicPosRegis.setError(null);
            TILpicPosRegis.setErrorEnabled(false);
            TIL_picRegis.setError(null);
            TIL_picRegis.setErrorEnabled(false);
            TIL_emailRegis.setError(null);
            TIL_emailRegis.setErrorEnabled(false);
            if (email_register.getText().toString().isEmpty()){
                TIL_passRegis.setErrorEnabled(true);
                TIL_passRegis.setError("Semua Data Harap diisi");
                register = false;
            }if (name_register.getText().toString().isEmpty()){
                TIL_passRegis.setErrorEnabled(true);
                TIL_passRegis.setError("Semua Data Harap diisi");
                register = false;
            }if (position_register.getText().toString().isEmpty()){
                TIL_passRegis.setErrorEnabled(true);
                TIL_passRegis.setError("Semua Data Harap diisi");
                register = false;
            }if (username_register.getText().toString().isEmpty()){
                TIL_passRegis.setErrorEnabled(true);
                TIL_passRegis.setError("Semua Data Harap diisi");
                register = false;
            }if (password_register.getText().toString().isEmpty()){
                TIL_passRegis.setErrorEnabled(true);
                TIL_passRegis.setError("Semua Data Harap diisi");
                register = false;
            }
            if (register) {
                RegisterDo();
            }
        }if (view == imagePerusahaan_add){
            popUpAddFoto();
        }
    }

    void RegisterDo(){
        try {
            cd = new CustomDialog(getActivity());
            cd.show();
            Gson gson = new Gson();
            String json = mPrefs.getString("Company", "");
            Company mcompany = gson.fromJson(json, Company.class);
            //Company mcompany = CachePot.getInstance().pop(Company.class);
            final String CompanyName = mcompany.getNama_perusahaan();
            final String Address = mcompany.getAlamat_perusahaan();
            final String Province = mcompany.getProvinsi();
            final String City = mcompany.getKota();
            final String Telephone = mcompany.getNomorTelphone();
            final String FAX = mcompany.getNo_fax();
            final String Email = email_register.getText().toString();
            final String PICName = name_register.getText().toString();
            final String PICPossition = position_register.getText().toString();
            final String Username = username_register.getText().toString();
            String Password = password_register.getText().toString();

            auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        if (filePath != null) {
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
                                    if (task.isSuccessful()) {
                                        imageResult = task.getResult();
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(PICName).setPhotoUri(filePath).build();
                                        user.updateProfile(profileChangeRequest).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if (e == null) {
                                                    Log.e("Error :", e.getMessage());
                                                }
                                            }
                                        });
                                        User userAdd = new User(Address, CompanyName, Telephone, FAX, City, Province, imageResult.toString(), PICName, Email, user.getUid(), PICPossition, Username, instanceId, "");
                                        firestore.collection("Users").document(user.getUid()).set(userAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(getActivity(),"Successfuly Create User",Toast.LENGTH_LONG).show();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                cd.dismiss();
                                                                popRole(user);
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Crashlytics.logException(e);
                                                            cd.dismiss();
                                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                Crashlytics.logException(e);
                                            }
                                        });
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                        } else { // file path kosong
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(PICName).build();
                            user.updateProfile(profileChangeRequest).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e == null) {
                                        Log.e("Error :", e.getMessage());
                                    }
                                }
                            });
                            User userAdd = new User(Address, CompanyName, Telephone, FAX, City, Province, "", PICName, Email, user.getUid(), PICPossition, Username, instanceId, "");
                            firestore.collection("Users").document(user.getUid()).set(userAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(getActivity(),"Successfuly Create User",Toast.LENGTH_LONG).show();
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    cd.dismiss();
                                                    popRole(user);
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Crashlytics.logException(e);
                                                cd.dismiss();
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    Crashlytics.logException(e);
                                    cd.dismiss();
                                }
                            });
                        }

                    } else {
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        cd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error Create User", e.getMessage());
                    cd.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(),"Terjadi Kesalahan Harap Masukan ulang data anda",Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        }
    }
    public void popUpAddFoto(){
        final Dialog dialog = new Dialog(getActivity());
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
                if (!hasPermissions(getActivity(),permissionsRequired)) {
                    requestPermissions(permissionsRequired,1);
                }else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"DCIM/Camera","IMG_lwlwlwlwlwwl.jpg");
                    filePath = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                    startActivityForResult(i,0);
                    dialog.dismiss();
                }

            }
        });
        filePicture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getActivity(), permissionsRequired)) {

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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                final Bitmap bitmap;
                bitmap = decodeSampledBitmapFromUri(filePath,imagePerusahaan_add.getWidth(),imagePerusahaan_add.getHeight());
                imagePerusahaan_add.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }if (requestCode == 0 && resultCode == RESULT_OK && filePath!=null){
            Matrix mat = new Matrix();
            try {
                ExifInterface exif = new ExifInterface(filePath.getPath());
                String orientstring = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientstring != null ? Integer.parseInt(orientstring) : ExifInterface.ORIENTATION_NORMAL;
                int rotateangle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                    rotateangle = 90;
                if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
                    rotateangle = 180;
                if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
                    rotateangle = 270;
                mat.setRotate(rotateangle, (float) imagePerusahaan_add.getWidth()/ 2, (float) imagePerusahaan_add.getHeight() / 2);

            } catch (IOException e) {
                Log.e("Image Error path",e.getMessage());
            }
            final Bitmap bitmap;
            bitmap = decodeSampledBitmapFromUri(filePath,imagePerusahaan_add.getWidth(),imagePerusahaan_add.getHeight());
            imagePerusahaan_add.setImageBitmap(bitmap);
        }
        // letak if di sini coy

    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri,int reqWidth, int reqHeight){
        Bitmap bm = null;
        try{
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri),null ,options);
            options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
            options.inJustDecodeBounds = false;

            bm = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri)
                    ,null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
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

    public void popRole(final FirebaseUser user){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_role);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final Button buttonSeller = dialog.findViewById(R.id.buttonSeller);
        final Button buttonBuyer = dialog.findViewById(R.id.buttonBuyer);

        buttonBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(user.getUid()).update("roleActive","Pembeli").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(getActivity(),buyerActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"Terjadi Kesalahan harap Login Ulang",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        buttonSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Users").document(user.getUid()).update("roleActive","Penjual").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getActivity(), newUserUI.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                    }
                });

            }
        });
        dialog.show();

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
}
