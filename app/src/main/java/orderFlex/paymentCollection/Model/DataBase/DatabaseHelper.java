package orderFlex.paymentCollection.Model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "onutiative_and_total_payflex_db";
    public static int VERSION = 1;
    private String TAG= "DatabaseHelper";


    ///////////////////////////////////Table qr queue ////////////////////////////

    public static final String TBL_IMAGE_QUEUE = "tbl_image_queue";

    public static final String IMG_ID = "_id";
    public static final String IMG_FILENAME = "filename";
    public static final String IMG_CLIENT_ID = "clientID";
    public static final String IMG_FILE_TYPE = "fileType";
    public static final String IMG_FILE_DETAILS = "fileDetail";
    public static final String IMG_EXTENSION = "extension";
    public static final String IMG_ORDER_CODE = "order_code";
    public static final String IMG_PAYMENT_ID = "payment_id";
    public static final String IMG_SOURCE_PATH = "sourcePath";
    public static final String IMG_IS_SYNC= "is_sync";

//           this.filename=helper.makeUniqueID();
//        this.fileType=fileType;
//        this.clientID=clientID;
//        this.fileDetail=fileDetail;
//        this.extension=extension;
//        this.order_code=order_code;
//        this.payment_id=payment_id;
//        this.sourcePath= sourcePath;


    public static final String CREATE_TABLE_QR_QUEUE =
            "create table "+ TBL_IMAGE_QUEUE +"("+
                    IMG_ID+ " integer primary key autoincrement, "+
                    IMG_FILENAME + " text, "+
                    IMG_CLIENT_ID + " text, "+
                    IMG_FILE_TYPE + " text, "+
                    IMG_FILE_DETAILS + " text, "+
                    IMG_EXTENSION + " text, "+
                    IMG_ORDER_CODE + " text, "+
                    IMG_PAYMENT_ID + " text, "+
                    IMG_SOURCE_PATH + " text," +
                    IMG_IS_SYNC + " text)" ;

    //////////////////////////////Table qr queue////////////////////////

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QR_QUEUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_IMAGE_QUEUE);
    }

}
