package orderFlex.paymentCollection.Activityes.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import orderFlex.paymentCollection.Model.APICallings.ProfileImageFileUpload;
import orderFlex.paymentCollection.Model.UserData.ProfileQueueRequestData;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class ProfileActivity extends BaseActivity {
    private ImageView proImg,addImg;
    private Helper helper;
    private TextView clientCode,name,presenterName,phoneNo,address;
    private RelativeLayout containerView;
    private CardView updateProfile;
    private SharedPrefManager prefManager;
    private boolean is_attachment_active=true,isGalleryImg=false;
    private Context context=ProfileActivity.this;
    private String TAG="ProfileActivity";
    private String imageOrginalPath,imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //basic class object initial
        helper=new Helper(this);
        prefManager=new SharedPrefManager(this);
        //view
        proImg=findViewById(R.id.proImg);
        addImg=findViewById(R.id.addImg);
        containerView=findViewById(R.id.profileActivity);
        updateProfile=findViewById(R.id.updateProfile);
        updateProfile();

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_attachment_active){
                    if (checkCameraPermission()){
                        if (checkStorageReadPermission()){
                            if (checkStorageWritePermission()){
                                final AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = LayoutInflater.from(context);
                                ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.image_pick_select_view,null);

                                CardView cameraTake=customRoot.findViewById(R.id.cameraTake);
                                CardView galleryTake=customRoot.findViewById(R.id.galleryTake);
                                builder.setView(customRoot);
                                builder.setCancelable(true);
                                alertDialog= builder.create();
                                alertDialog.show();
                                cameraTake.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dispatchTakePictureIntent(1);
                                        alertDialog.dismiss();
                                        isGalleryImg=false;
                                    }
                                });
                                galleryTake.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dispatchTakePictureIntent(2);
                                        isGalleryImg=true;
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                }else {
                    helper.showSnakBar(containerView,"Sorry! You can't submits attachment in ACCOUNT BALANCE");
                }
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isInternetAvailable())
                {
                    ProfileQueueRequestData requestData=new ProfileQueueRequestData(helper.makeUniqueID(),"1"
                            ,prefManager.getClientId(), "",".jpg",imageOrginalPath,"0");
                    new ProfileImageFileUpload(context, requestData,isGalleryImg).execute();
                }
                else {
                    helper.showSnakBar(containerView,"Please check your internet connection!");
                }
            }
        });
    }

    private void updateProfile(){
        clientCode=findViewById(R.id.clientCode);
        name=findViewById(R.id.name);
        presenterName=findViewById(R.id.presenterName);
        phoneNo=findViewById(R.id.phoneNo);
        address=findViewById(R.id.address);

        clientCode.setText(prefManager.getClientCode());
        name.setText(prefManager.getClientName());
        presenterName.setText(prefManager.getPresenterName());
        phoneNo.setText(prefManager.getClientContactNumber());
        address.setText(prefManager.getClientAddress());
        updateProfile.setVisibility(View.GONE);
        if (prefManager.getProImgUrl()!=null){
            Picasso.get()
                    .load(prefManager.getProImgUrl())
                    .placeholder(R.drawable.ic_person)
//                            .resize(100, 100)
                    .priority(Picasso.Priority.HIGH)
                    .into(proImg);
            Log.i(TAG,"Image URL: "+prefManager.getProImgUrl());
        }else {
            Log.i(TAG,"No Image found!");
        }
    }

    ///////////////////////permission block

    public boolean checkCameraPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},11);
            return false;
        }
        return true;
    }

    public boolean checkStorageWritePermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},12);
            return false;
        }
        return true;
    }
    public boolean checkStorageReadPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},13);
            return false;
        }
        return true;
    }

    /////////////////////////#######################################333//////////////////
    ///camera operation//////////////////////////////////////////////
    private String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 111;
    static final int REQUEST_PICK_PHOTO = 222;
    private void dispatchTakePictureIntent(int x) {
        if (x==1){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    imageOrginalPath=photoFile.getAbsolutePath();
                    Log.i(TAG,"Created Image: "+imageOrginalPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.payflex_file_provider.FileProvider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }else if(x==2) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_PHOTO);
        }

    }
    ///////////////////////////////////////////////////////

    private File createImageFile() throws IOException{
        String timeStamp = helper.makeUniqueID();
        imageName = "pay"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);
        currentPhotoPath=imageFile.getAbsolutePath();
        return imageFile;
    }

    ////////////////////////////////
    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ret = false;
            }
        }
        return ret;
    }

    ////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }else if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            Log.i(TAG, "Image picked");
            Log.i(TAG,"Image URi: "+ selectedImage);

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i(TAG,"Image Path: "+ picturePath);
            currentPhotoPath=picturePath;
            imageOrginalPath=picturePath;
            setPic();
            updateProfile.setVisibility(View.VISIBLE);
        }else {
            Log.i(TAG, "Result not ok!");
            Log.i(TAG, "Request Code: "+requestCode);
        }
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = proImg.getWidth();
        int targetH = proImg.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        proImg.setImageBitmap(bitmap);
    }
    //For multi-language operation
    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(mContext, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        Locale locale = new Locale(LocaleManager.getLanguagePref(this));
        Locale.setDefault(locale);
        overrideConfiguration.setLocale(locale);
        super.applyOverrideConfiguration(overrideConfiguration);
    }
}