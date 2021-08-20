package orderFlex.paymentCollection.Activityes.PaymentActivity;

import androidx.annotation.NonNull;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import orderFlex.paymentCollection.Model.APICallings.ReplaceBills;
import orderFlex.paymentCollection.Model.DataBase.DatabaseOperation;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillReplaceRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentQueueRequestData;
import orderFlex.paymentCollection.Activityes.OrderDetailsActivity.OrderDetailsActivity;
import orderFlex.paymentCollection.Model.APICallings.PaymentImageFileUploader;
import orderFlex.paymentCollection.Model.APICallings.PullPaymentMethods;
import orderFlex.paymentCollection.Model.APICallings.PushBills;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentRequestBody;
import orderFlex.paymentCollection.Model.PaymentAndBillData.BillPaymentResponse;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentMothodsResponse;
import orderFlex.paymentCollection.R;
import orderFlex.paymentCollection.Utility.Helper;
import orderFlex.paymentCollection.Utility.LanguagePackage.BaseActivity;
import orderFlex.paymentCollection.Utility.LanguagePackage.LocaleManager;
import orderFlex.paymentCollection.Utility.SharedPrefManager;

public class PaymentActivity
        extends BaseActivity
        implements PullPaymentMethods.PaymentMethodsListener,
        AdapterView.OnItemSelectedListener,
        PushBills.PushBillListener,
        ReplaceBills.ReplaceBillListener {
    private TextView paySubmit;
    private Spinner spinnerMethod, spinnerBank;
    private SharedPrefManager prefManager;
    private Helper helper;
    private PullPaymentMethods pullPaymentMethods;
    private List<PaymentMothodsResponse.BankList> bankListData = new ArrayList<>();
    private List<PaymentMothodsResponse.PaymentMethode> methodListData = new ArrayList<>();
    private String TAG = "PaymentActivity";
    private View containerView;
    private BillPaymentRequestBody requestBody;
    private EditText referenceNo, payAmount;
    private TextView payDate, virtualAccount;
    private String orderCode;
    private PushBills pushBills;
    //    private UpdateBill updateBill;
    private ReplaceBills replaceBills;
    private String imageName;
    private ImageView addImg, referenceImg;
    private LinearLayout payDatePick;
    private String bankName = "", methodeName = "";
    private boolean updateFlag = false;
    private String paymentId = "";
    private String imageOrginalPath;
    private boolean is_attachment_active = true;
    private DatabaseOperation dbOperation;
    private boolean isGalleryImg = false;
    private String preAmount, preMethod, preReference;
    // private ImageFileUploader imageFileUploader;
    private BillReplaceRequestBody replaceRequestBody = new BillReplaceRequestBody();
    private BillReplaceRequestBody.NewData newData = new BillReplaceRequestBody.NewData();
    private BillReplaceRequestBody.OldData oldData = new BillReplaceRequestBody.OldData();

    ///below line codded by subrota
    private LinearLayout amountBlockLL, referenceBlockLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_method_form);
        setContentView(R.layout.payment_form);

        prefManager = new SharedPrefManager(this);
        helper = new Helper(this);
        dbOperation = new DatabaseOperation(this);

        containerView = findViewById(R.id.paymentMethodeActivity);
        referenceNo = findViewById(R.id.referenceNo);
        payDate = findViewById(R.id.payDate);
        payAmount = findViewById(R.id.payAmount);
        payDate.setText(helper.getDateInEnglish());
        requestBody = new BillPaymentRequestBody();
        addImg = findViewById(R.id.addImg);
        referenceImg = findViewById(R.id.referenceImg);
        virtualAccount = findViewById(R.id.virtualAccount);
        virtualAccount.setText(prefManager.getClientVirtualAccountNumber());
        payDatePick = findViewById(R.id.payDatePick);
        spinnerMethod = findViewById(R.id.paymentMethod);
        spinnerMethod.setOnItemSelectedListener(this);
        spinnerBank = findViewById(R.id.bankList);
        spinnerBank.setOnItemSelectedListener(this);
        paySubmit = findViewById(R.id.paySubmit);

        ////codded by subrota

        referenceBlockLL = findViewById(R.id.referenceBlockLL);
        amountBlockLL = findViewById(R.id.amountBlockLL);
        /////end subrota////

        pushBills = new PushBills(this);
//        updateBill=new UpdateBill(this);
        replaceBills = new ReplaceBills(this);

        try {
            Intent intent = getIntent();
            if (intent.getStringExtra("order_code") != null) {
                orderCode = intent.getStringExtra("order_code");
                Log.i(TAG, "Order Code From Main: " + orderCode);
                updateFlag = false;
            } else {
                requestBody = (BillPaymentRequestBody) intent.getSerializableExtra("payment_data");
                Log.i(TAG, "Order Code From Payment List: " + requestBody.getOrderCode());
                preAmount = requestBody.getAmount();
                preReference = requestBody.getReferenceNo();
                referenceNo.setText(preReference);
                payAmount.setText(preAmount);
                paySubmit.setText("Update");
                bankName = intent.getStringExtra("bank_name");
                methodeName = intent.getStringExtra("method_name");
                preMethod = methodeName;
                String imgUrl = intent.getStringExtra("img_url");
                orderCode = requestBody.getOrderCode();
                paymentId = intent.getStringExtra("payment_id");
                updateFlag = true;
                oldData.setTrxid(requestBody.getTrxid());
                oldData.setAmount(requestBody.getAmount());
                oldData.setOrderCode(requestBody.getOrderCode());
                oldData.setReferenceNo(requestBody.getReferenceNo());
                if (imgUrl != null) {
                    Picasso.get()
                            .load(imgUrl)
                            .placeholder(R.drawable.loading_wh)
//                            .resize(100, 100)
                            .priority(Picasso.Priority.HIGH)
                            .into(referenceImg);
                    Log.i(TAG, "Image URL: " + imgUrl);
                } else {
                    Log.i(TAG, "No Image found!");
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception in catch Intent: " + e.toString());
        }

        pullPaymentMethods = new PullPaymentMethods(this);
        if (helper.isInternetAvailable()) {
            pullPaymentMethods.paymentMethodsCall(prefManager.getUsername(), prefManager.getUserPassword());
        } else {
            helper.showSnakBar(containerView, "Please check your internet connection!");
        }


        payDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.getCalenderDateInEnglish(payDate);
            }
        });

        paySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refNo = referenceNo.getText().toString();
                String payed = payAmount.getText().toString();
                requestBody.setOrderCode(orderCode);
                String paymentDate = payDate.getText().toString();
                requestBody.setPaymentDateTime(paymentDate);
                requestBody.setReferenceNo(refNo);
                ////////codded by subrota
//                if (methodListData.equals("2")
//                        ||methodListData.equals("3")
//                        ||methodListData.equals("4")
//                        ||methodListData.equals("12")) {
//                    requestBody.setReferenceNo("");
//                }else {
//                    if (refNo == null || refNo.equals("")){
//                        referenceNo.setError("This Field could not be empty");
//                    }else {
//                        requestBody.setReferenceNo(refNo);
//                    }
//                }
                ///end

                if (requestBody.getTrxid() == null) {
                    requestBody.setTrxid(helper.makeUniqueID());
                }
                requestBody.setAmount(payed);
                requestBody.setIsEditable(0);
                requestBody.setSubmittedDateTime(helper.getDateTimeInEnglish());
                if (payed.isEmpty() || paymentDate.isEmpty()) {
                    helper.showSnakBar(containerView, "Some fields are empty!");
                    return;
                }
//////////////////////////////codded by subrota as replacement bellow commented previous code
                if (requestBody.getPaymentModeId().equals("2")
                        || requestBody.getPaymentModeId().equals("3")
                        || requestBody.getPaymentModeId().equals("4")
                        || requestBody.getPaymentModeId().equals("12")) {
                    //in case of condition no required any reference number, so we can set empty string as reference number
                    requestBody.setReferenceNo("");

                    ///now bew condition checking other empty field which can't be empty
                    if (payed.isEmpty() || paymentDate.isEmpty()) {
                        helper.showSnakBar(containerView, "Some fields are empty!");
                        return;
                    } else {
                        Log.i(TAG, "Mode ID at else in save point: " + requestBody.getPaymentModeId());
                    }

                } else {
                    //in this section reference number can't be empty
                    //this payment methods are need reference number or id
                    if (refNo.isEmpty() || payed.isEmpty() || paymentDate.isEmpty()) {
                        helper.showSnakBar(containerView, "Some fields are empty!");
                        return;
                    } else {
                        Log.i(TAG, "Mode ID at else in save point: " + requestBody.getPaymentModeId());
                    }
                }
///////////////////////////commented section is previous code
                /*if (!requestBody.getPaymentModeId().equals("2")
                        && !requestBody.getPaymentModeId().equals("3")
                        && !requestBody.getPaymentModeId().equals("4")
                        && !requestBody.getPaymentModeId().equals("7")
                        && !requestBody.getPaymentModeId().equals("13")) {
                    Log.i(TAG, "Mode ID at save point: " + requestBody.getPaymentModeId());

                    if (refNo.isEmpty() || payed.isEmpty() || paymentDate.isEmpty()) {
                        helper.showSnakBar(containerView, "Some fields are empty!");
                        return;
                    }
                } else {
                    Log.i(TAG, "Mode ID at else in save point: " + requestBody.getPaymentModeId());
                }*/

                if (helper.isInternetAvailable()) {
                    if (updateFlag) {
                        newData.setTrxid(helper.makeUniqueID());
                        newData.setPaymentModeId(requestBody.getPaymentModeId());
                        newData.setFinancialInstitutionId(requestBody.getFinancial_institution_id());
                        newData.setAmount(requestBody.getAmount());
                        newData.setReferenceNo(requestBody.getReferenceNo());
                        newData.setOrderCode(requestBody.getOrderCode());
                        newData.setPaymentDateTime(requestBody.getPaymentDateTime());
                        newData.setSubmittedDate(requestBody.getSubmittedDateTime());
                        newData.setIsEditable("0");
                        newData.setReplaceTag(preReference + "/" + preMethod + "/" + preAmount);
                        replaceRequestBody.setNewData(newData);
                        replaceRequestBody.setOldData(oldData);
                        replaceBills.replaceBillCall(prefManager.getUsername(), prefManager.getUserPassword(), replaceRequestBody);
                    } else {
                        pushBills.pushBillCall(prefManager.getUsername(), prefManager.getUserPassword(), requestBody);
                    }
                } else {
                    helper.showSnakBar(containerView, "Please check your internet connection!");
                }
                //requestBody.set
            }
        });
        checkCameraPermission();
//        checkStorageWritePermission();
//        checkStorageReadPermission();
        final Context context = this;
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_attachment_active) {
                    if (checkCameraPermission()) {
                        if (checkStorageReadPermission()) {
                            if (checkStorageWritePermission()) {
                                final AlertDialog alertDialog;
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = LayoutInflater.from(context);
                                ConstraintLayout customRoot = (ConstraintLayout) inflater.inflate(R.layout.image_pick_select_view, null);

                                CardView cameraTake = customRoot.findViewById(R.id.cameraTake);
                                CardView galleryTake = customRoot.findViewById(R.id.galleryTake);
                                builder.setView(customRoot);
                                builder.setCancelable(true);
                                alertDialog = builder.create();
                                alertDialog.show();
                                cameraTake.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dispatchTakePictureIntent(1);
                                        alertDialog.dismiss();
                                        isGalleryImg = false;
                                    }
                                });
                                galleryTake.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dispatchTakePictureIntent(2);
                                        isGalleryImg = true;
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                } else {
                    helper.showSnakBar(containerView, "Sorry! You can't submits attachment in ACCOUNT BALANCE");
                }
            }
        });
    }

    @Override
    public void onPreResponse(PaymentMothodsResponse response, int code) {
        bankListData = response.getBankList();
        methodListData = response.getPaymentMethode();
        List<String> bankList = new ArrayList<>();
        List<String> methodList = new ArrayList<>();

        if (response != null && code == 202) {
            int bankID = 0, bankCount = 0;
            for (PaymentMothodsResponse.BankList bank : response.getBankList()) {
                if (!bank.getBankName().isEmpty()) {
                    bankList.add(bank.getBankName());
                } else {
                    bankList.add("<Select Bank>");
                }

                if (bank.getBankName().equals(bankName)) {
                    bankID = bankCount;
                }
                bankCount++;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, bankList);
            spinnerBank.setAdapter(adapter);
            if (requestBody.getFinancial_institution_id() != null) {
                spinnerBank.setSelection(bankID);
                Log.i(TAG, "Bank ID: " + bankID);
            } else {
                spinnerBank.setSelection(0);
            }

            int methodeID = 0, methodeCounter = 0;
            for (PaymentMothodsResponse.PaymentMethode methode : response.getPaymentMethode()) {
                methodList.add(methode.getMethodeName());
                if (methode.getMethodeName().equals(methodeName)) {
                    methodeID = methodeCounter;
                }
                methodeCounter++;
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, methodList);
            spinnerMethod.setAdapter(adapter2);
            if (requestBody.getPaymentModeId() != null) {
                spinnerMethod.setSelection(methodeID);
                Log.i(TAG, "Methode ID: " + methodeID);
            } else {
                spinnerMethod.setSelection(0);
            }

        } else {
            if (code == 401) {
                helper.showSnakBar(containerView, "Unauthorized Username or Password!");
            } else {
                helper.showSnakBar(containerView, "Server not Responding! Please check your internet connection.");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.bankList) {
            Log.i(TAG, "Bank");
            requestBody.setFinancial_institution_id(bankListData.get(position).getId());
        }
        if (parent.getId() == R.id.paymentMethod) {
            Log.i(TAG, "Method");
            requestBody.setPaymentModeId(methodListData.get(position).getId());
            if (methodListData.get(position).getId().equals("2")
                    || methodListData.get(position).getId().equals("3")
                    || methodListData.get(position).getId().equals("4")
                    || methodListData.get(position).getId().equals("7")
                    | methodListData.get(position).getId().equals("12")
                    | methodListData.get(position).getId().equals("13")) {
                Log.i(TAG, "Selected");
                if (methodListData.get(position).getId().equals("12")) {
                    is_attachment_active = false;
                } else {
                    is_attachment_active = true;
                    referenceNo.setText("");
                }
                //add referenceNo
//                if (methodListData.get(position).getId().equals("12")){
//                    referenceNo.setText("111");
//                    referenceNo.setEnabled(false);
//                }
            } else {
                referenceNo.setEnabled(true);
                Log.i(TAG, "Not advance Selected");
//                if (is_attachment_active){
//                    referenceNo.setText("");
//                }
                is_attachment_active = true;
            }
        }
        // codded by Subrota
        if (methodListData.get(position).getId().equals("2")
                || methodListData.get(position).getId().equals("3")
                || methodListData.get(position).getId().equals("4")
                || methodListData.get(position).getId().equals("12")) {

            referenceBlockLL.setVisibility(View.GONE);
            amountBlockLL.setBackgroundColor(getResources().getColor(R.color.barColor));

        } else {
            referenceBlockLL.setVisibility(View.VISIBLE);
            amountBlockLL.setBackgroundColor(getResources().getColor(R.color.white));
        }

        ////end subrota


        Log.i(TAG, bankListData.get(position).getBankName());
        Log.i(TAG, "Selected: " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//
    }

    @Override
    public void onResponse(BillPaymentResponse response, int code) {
        if (code == 202 && response != null) {
            helper.showSnakBar(containerView, "Thank you for your payment!");
            paymentId = response.getInserted_code();
            if (is_attachment_active && response.isSuccessfull()) {
                PaymentQueueRequestData requestData = new PaymentQueueRequestData(helper.makeUniqueID(), "2"
                        , prefManager.getClientId(), "", ".jpg", orderCode, paymentId, imageOrginalPath, "0");
                dbOperation.insertIMGQueue(requestData);
                new PaymentImageFileUploader(this, requestData, isGalleryImg).execute();
            }
            if (response.isSuccessfull()) {
                Intent intent = new Intent(PaymentActivity.this, OrderDetailsActivity.class);
                intent.putExtra("payment_massege", response.getMessage());
                intent.putExtra("booked_code", orderCode);
                startActivity(intent);
                finish();
            } else {
                helper.showSnakBar(containerView, response.getMessage());
            }
        } else {
            if (code == 401) {
                helper.showSnakBar(containerView, "Unauthorized Username or Password!");
            } else {
                helper.showSnakBar(containerView, "Server not Responding! Please check your internet connection.");
            }
        }
    }

    /////////////////////////#######################################333//////////////////
    ///camera operation//////////////////////////////////////////////
    private String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 111;
    static final int REQUEST_PICK_PHOTO = 222;

    private void dispatchTakePictureIntent(int x) {
        if (x == 1) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    imageOrginalPath = photoFile.getAbsolutePath();
                    Log.i(TAG, "Created Image: " + imageOrginalPath);
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
        } else if (x == 2) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_PHOTO);
        }

    }
    ///////////////////////////////////////////////////////

    private File createImageFile() throws IOException {
        String timeStamp = helper.makeUniqueID();
        imageName = "pay" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
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
        } else if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.i(TAG, "Image picked");
            Log.i(TAG, "Image URi: " + selectedImage);

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.i(TAG, "Image Path: " + picturePath);
            currentPhotoPath = picturePath;
            imageOrginalPath = picturePath;
            setPic();
        } else {
            Log.i(TAG, "Result not ok!");
            Log.i(TAG, "Request Code: " + requestCode);
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
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        referenceImg.setImageBitmap(bitmap);
    }

    ///////////////////////permission block

    public boolean checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
            return false;
        }
        return true;
    }

    public boolean checkStorageWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            return false;
        }
        return true;
    }

    public boolean checkStorageReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 13);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkStorageWritePermission();
        }
        if (requestCode == 12 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkStorageReadPermission();
        }
        if (requestCode == 13 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkCameraPermission();
        }
    }
    //////////////////////////////////////////////////////


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

    @Override
    public void onReplaceResponse(BillPaymentResponse response, int code) {
        if (code == 202 && response != null) {
            helper.showSnakBar(containerView, "Thank you for your payment!");
            paymentId = response.getInserted_code();
            if (imageOrginalPath != null && is_attachment_active && response.isSuccessfull()) {
                PaymentQueueRequestData requestData = new PaymentQueueRequestData(helper.makeUniqueID(), "2"
                        , prefManager.getClientId(), "", ".jpg", orderCode, paymentId, imageOrginalPath, "0");
                dbOperation.insertIMGQueue(requestData);
                new PaymentImageFileUploader(this, requestData, isGalleryImg).execute();
            }
            if (response.isSuccessfull()) {
                Intent intent = new Intent(PaymentActivity.this, OrderDetailsActivity.class);
                intent.putExtra("payment_massege", response.getMessage());
                intent.putExtra("booked_code", orderCode);
                startActivity(intent);
                finish();
            } else {
                helper.showSnakBar(containerView, response.getMessage());
            }
        } else {
            if (code == 401) {
                helper.showSnakBar(containerView, "Unauthorized Username or Password!");
            } else {
                helper.showSnakBar(containerView, "Server not Responding! Please check your internet connection.");
            }
        }
    }
}
