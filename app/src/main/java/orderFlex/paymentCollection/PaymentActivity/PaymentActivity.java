package orderFlex.paymentCollection.PaymentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import orderFlex.paymentCollection.MainActivity.MainActivity;
import orderFlex.paymentCollection.Model.APICallings.FileUploader;
import orderFlex.paymentCollection.Model.APICallings.ImageFileUploader;
import orderFlex.paymentCollection.Model.APICallings.PullPaymentMethods;
import orderFlex.paymentCollection.Model.APICallings.PushBills;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.SharedPrefManager;
import orderFlex.paymentCollection.login.UserLogin;

import static androidx.core.content.FileProvider.getUriForFile;

public class PaymentActivity extends AppCompatActivity implements PullPaymentMethods.PaymentMethodsListener,
        AdapterView.OnItemSelectedListener, PushBills.PushBillListener {
    private Button paySubmit;
    private Spinner spinnerMethod, spinnerBank;
    private SharedPrefManager prefManager;
    private Helper helper;
    private PullPaymentMethods pullPaymentMethods;
    private List<PaymentMothodsResponse.BankList> bankListData =new ArrayList<>();
    private List<PaymentMothodsResponse.PaymentMethode> methodListData =new ArrayList<>();
    private String TAG="PaymentActivity";
    private View containerView;
    private BillPaymentRequestBody requestBody;
    private EditText referenceNo,payAmount;
    private TextView payDate;
    private String orderCode;
    private PushBills pushBills;
    private String imageName;
    private ImageView addImg,referenceImg;
   // private ImageFileUploader imageFileUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_method_form);
        setContentView(R.layout.payment_form_test);

        prefManager=new SharedPrefManager(this);
        helper=new Helper(this);
        containerView=findViewById(R.id.paymentMethodeActivity);
        referenceNo=findViewById(R.id.referenceNo);
        payDate=findViewById(R.id.payDate);
        payAmount=findViewById(R.id.payAmount);
        payDate.setText(helper.getDate());
        requestBody=new BillPaymentRequestBody();
        addImg=findViewById(R.id.addImg);
        referenceImg=findViewById(R.id.referenceImg);
        pushBills=new PushBills(this);

        Intent intent=getIntent();
        orderCode=intent.getStringExtra("order_code");
        Log.i(TAG,"Order Code: "+orderCode);
        //Log.i(TAG,orderCode);

        pullPaymentMethods=new PullPaymentMethods(this);
//        if (helper.isInternetAvailable())
        {
            pullPaymentMethods.paymentMethodsCall(prefManager.getUsername(),prefManager.getUserPassword());
        }
//        else {
//            helper.showSnakBar(containerView,"Please check your internet connection!");
//        }
        spinnerMethod =findViewById(R.id.paymentMethod);
        spinnerMethod.setOnItemSelectedListener(this);
        spinnerBank =findViewById(R.id.bankList);
        spinnerBank.setOnItemSelectedListener(this);
        paySubmit=findViewById(R.id.paySubmit);
        paySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refNo=referenceNo.getText().toString();
                String payed=payAmount.getText().toString();
                requestBody.setOrderCode(orderCode);
                requestBody.setPaymentDateTime(payDate.getText().toString());
                requestBody.setReferenceNo(refNo);
                requestBody.setTrxid(helper.makeUniqueID());
                requestBody.setAmount(payed);
                if (refNo.isEmpty()||payed.isEmpty()){
                    helper.showSnakBar(containerView,"Some fields are empty!");
                }else {
                    if (helper.isInternetAvailable())
                    {
                        pushBills.pushBillCall(prefManager.getUsername(),prefManager.getUserPassword(),requestBody);
                    }
                    else {
                        helper.showSnakBar(containerView,"Please check your internet connection!");
                    }
                }
                //requestBody.set
            }
        });
        checkCameraPermission();
        checkStorageWritePermission();
        checkStorageReadPermission();
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public void onPreResponse(PaymentMothodsResponse response, int code) {
        bankListData =response.getBankList();
        methodListData =response.getPaymentMethode();
        List<String> bankList=new ArrayList<>();
        List<String>methodList=new ArrayList<>();

        if (response!=null && code==202){
            for (PaymentMothodsResponse.BankList bank:response.getBankList()) {
                bankList.add(bank.getBankName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,bankList);
            spinnerBank.setAdapter(adapter);
            spinnerBank.setSelection(bankList.indexOf(0));

            for (PaymentMothodsResponse.PaymentMethode methode:response.getPaymentMethode()) {
                methodList.add(methode.getMethodeName());
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,methodList);
            spinnerMethod.setAdapter(adapter2);
            spinnerMethod.setSelection(bankList.indexOf(0));
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.bankList){
            Log.i(TAG,"Bank");
            requestBody.setFinancial_institution_id(bankListData.get(position).getId());
        }
        if (parent.getId()==R.id.paymentMethod){
            Log.i(TAG,"Method");
            requestBody.setPaymentModeId(methodListData.get(position).getId());
        }
        Log.i(TAG,bankListData.get(position).getBankName());
        Log.i(TAG,"Selected: "+position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//
    }
    @Override
    public void onResponse(BillPaymentResponse response, int code) {
        if (code==202){
            helper.showSnakBar(containerView,"Thank you for your payment!");
            new ImageFileUploader(this, prefManager.getClientId(),imageName,"", "2",".jpg",orderCode,response.getInserted_code()).execute();
//            new FileUploader(this,prefManager.getClientId(),
//                    imageName,"","2",".jpg",
//                    orderCode,response.getInserted_code()).execute();
            Intent intent=new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            helper.showSnakBar(containerView,"Failed to submit!");
        }
    }
    ///camera operation//////////////////////////////////////////////
    static final int REQUEST_TAKE_PHOTO = 111;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.i(TAG,"Created Image: "+photoFile.getAbsolutePath());
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
    }
    ///////////////////////////////////////////////////////
    String currentPhotoPath;
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
        }
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = referenceImg.getWidth();
        int targetH = referenceImg.getHeight();

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
        referenceImg.setImageBitmap(bitmap);
    }

    ///////////////////////permission block

    public boolean checkCameraPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},11);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            checkStorageWritePermission();
        }
        if(requestCode == 12 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            checkStorageReadPermission();
        }
        if(requestCode == 13 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            checkCameraPermission();
        }
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
}
