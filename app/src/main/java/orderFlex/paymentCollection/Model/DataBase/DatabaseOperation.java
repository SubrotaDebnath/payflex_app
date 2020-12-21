package orderFlex.paymentCollection.Model.DataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import orderFlex.paymentCollection.Model.APILog.APILogData;
import orderFlex.paymentCollection.Model.PaymentAndBillData.PaymentQueueRequestData;

public class DatabaseOperation {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    //private Helper helper;
    private Context context;
    public static final String TAG="DatabaseOperation";
    public DatabaseOperation(Context context) {
        databaseHelper = new DatabaseHelper(context);
       // helper = new Helper(context);
    }
    public void open(){

        db = databaseHelper.getWritableDatabase();
    }
    public void close(){
        db.close();
    }

    //////////////////insert qr/////////////
    public boolean insertIMGQueue(PaymentQueueRequestData data){
        Log.i(TAG,"Name: "+data.getFilename());
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.IMG_FILENAME, data.getFilename());
        values.put(DatabaseHelper.IMG_CLIENT_ID, data.getClientID());
        values.put(DatabaseHelper.IMG_FILE_TYPE, data.getFileType());
        values.put(DatabaseHelper.IMG_FILE_DETAILS,data.getFileDetail());
        values.put(DatabaseHelper.IMG_EXTENSION,data.getExtension());
        values.put(DatabaseHelper.IMG_ORDER_CODE, data.getOrder_code());
        values.put(DatabaseHelper.IMG_PAYMENT_ID,data.getPayment_id());
        values.put(DatabaseHelper.IMG_SOURCE_PATH,data.getSourcePath());
        values.put(DatabaseHelper.IMG_IS_SYNC,data.getIsSync());

        // Inserting Row
        long code=db.insert(DatabaseHelper.TBL_IMAGE_QUEUE, null, values);
        Log.i(TAG,"User info Insert code: "+code);
        this.close(); // Closing Database connection
        return true;
    }
    ////////////////////////////////////////////////

    public void deleteIMGQueue(){
        this.open();
        db.execSQL("delete from " + DatabaseHelper.TBL_IMAGE_QUEUE);
        this.close();
    }

    public void deleteIMGQueueByPaymentID(String fileName){
        this.open();
        db.execSQL("delete from " + DatabaseHelper.TBL_IMAGE_QUEUE+" where "+DatabaseHelper.IMG_FILENAME+" = '"+fileName+"';");
        this.close();
    }

    public void updateImgQueue(String fileName, String status)
    {
        Log.d(TAG,"Call from CALLS_QUEUE - updateCalllogForMissed");
        this.open();
        db.execSQL("UPDATE " + DatabaseHelper.TBL_IMAGE_QUEUE +" SET "+ DatabaseHelper.IMG_IS_SYNC +" ='"+ status+"' WHERE "+DatabaseHelper.IMG_FILENAME+" = '"+fileName+"'");
        this.close();
    }

    public List<PaymentQueueRequestData>getAllImgQueueData(){
        List<PaymentQueueRequestData> qrList=new ArrayList<>();
        this.open();

        Cursor c=db.rawQuery("SELECT * FROM "+DatabaseHelper.TBL_IMAGE_QUEUE,null);
        if (c!=null && c.getCount()>0){
            c.moveToFirst();
            do {
                int id =c.getInt(c.getColumnIndex(DatabaseHelper.IMG_ID));
                String fileName = c.getString(c.getColumnIndex(DatabaseHelper.IMG_FILENAME));
                String clientID = c.getString(c.getColumnIndex(DatabaseHelper.IMG_CLIENT_ID));
                String fileType = c.getString(c.getColumnIndex(DatabaseHelper.IMG_FILE_TYPE));
                String fileDetail = c.getString(c.getColumnIndex(DatabaseHelper.IMG_FILE_DETAILS));
                String extension = c.getString(c.getColumnIndex(DatabaseHelper.IMG_EXTENSION));
                String order_code = c.getString(c.getColumnIndex(DatabaseHelper.IMG_ORDER_CODE));
                String payment_id = c.getString(c.getColumnIndex(DatabaseHelper.IMG_PAYMENT_ID));
                String sourcePath = c.getString(c.getColumnIndex(DatabaseHelper.IMG_SOURCE_PATH));
                String isSync = c.getString(c.getColumnIndex(DatabaseHelper.IMG_IS_SYNC));

                PaymentQueueRequestData data  = new PaymentQueueRequestData(id,fileName,fileType,clientID,fileDetail,extension,order_code,payment_id,sourcePath,isSync);
                qrList.add(data);
            }while (c.moveToNext());
        }
        c.close();

        this.close();
        return qrList;
    }

    ////////////////////////////API LOG OPERATION////////////////
    public boolean insertAPILog(APILogData data){
        Log.i(TAG,"URL: "+data.getCallURL());
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.API_CALL_NAME, data.getCallName());
        values.put(DatabaseHelper.API_CALL_URL, data.getCallURL());
        values.put(DatabaseHelper.API_CALL_TIME, data.getCallTime());
        values.put(DatabaseHelper.API_REQUEST_BODY,data.getRequestBody());
        values.put(DatabaseHelper.API_RESPONSE_CODE,data.getResponseCode());
        values.put(DatabaseHelper.API_RESPONSE_BODY, data.getResponseBody());
        values.put(DatabaseHelper.API_EXCEPTION,data.getException());
        values.put(DatabaseHelper.API_RESPONSE_TIME,data.getResponseTime());

        // Inserting Row
        long code=db.insert(DatabaseHelper.TBL_API_LOG, null, values);
        Log.i(TAG,"User info Insert code: "+code);
        this.close(); // Closing Database connection
        return true;
    }

    public List<APILogData>getAPILogData(){
        List<APILogData> apiLogs=new ArrayList<>();
        this.open();

        Cursor c=db.rawQuery("SELECT * FROM "+DatabaseHelper.TBL_API_LOG,null);
        if (c!=null && c.getCount()>0){
            c.moveToFirst();
            do {
                int id =c.getInt(c.getColumnIndex(DatabaseHelper.API_LOG_ID));
                String callNAme = c.getString(c.getColumnIndex(DatabaseHelper.API_CALL_NAME));
                String callURl = c.getString(c.getColumnIndex(DatabaseHelper.API_CALL_URL));
                String callTime = c.getString(c.getColumnIndex(DatabaseHelper.API_CALL_TIME));
                String requestBody = c.getString(c.getColumnIndex(DatabaseHelper.API_REQUEST_BODY));
                String responseCode = c.getString(c.getColumnIndex(DatabaseHelper.API_RESPONSE_CODE));
                String responseBody = c.getString(c.getColumnIndex(DatabaseHelper.API_RESPONSE_BODY));
                String exception = c.getString(c.getColumnIndex(DatabaseHelper.API_EXCEPTION));
                String responseTime = c.getString(c.getColumnIndex(DatabaseHelper.API_RESPONSE_TIME));
                APILogData data  = new APILogData(id,callNAme,callURl,callTime,requestBody,responseCode,responseBody,exception,responseTime);
                apiLogs.add(data);
            }while (c.moveToNext());
        }
        c.close();
        this.close();
        return apiLogs;
    }

    public void deleteAPILogs(){
        this.open();
        db.execSQL("delete from " + DatabaseHelper.TBL_API_LOG);
        this.close();
    }
}
