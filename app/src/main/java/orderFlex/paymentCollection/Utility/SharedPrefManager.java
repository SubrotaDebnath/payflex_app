package orderFlex.paymentCollection.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {

    private static final String PREF_NAME = "onuPref";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String USERNAME = "user_name";
    private static final String PASSWORD = "user_password";
    private static final String USER_TYPE = "user_type";
    private static final String CLIENT_PAIR_ID = "client_pair_id";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_CODE = "client_code";
    private static final String CLIENT_NAME = "client_name";
    private static final String CLIENT_EMAIL = "client_email";
    private static final String CLIENT_CONTACT_NUMBER = "client_contact_number";
    private static final String CLIENT_ADDRESS = "CLIENT_address";
    private static final String PRESENTER="presenter_name";
    private static final String HANDLER_ID="handler_id";



    public SharedPrefManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
    public void setLoggedInFlag(boolean flag){
        editor.putBoolean(IS_LOGIN,flag);
        editor.commit();
    }
    /////////////////////
    public void setUserNane(String username){
        editor.putString(USERNAME,username);
        editor.commit();
    }
    public String getUsername(){
        return preferences.getString(USERNAME,null);
    }
    /////////////////////////
    public void setUserPassword(String password){
        editor.putString(PASSWORD,password);
        editor.commit();
    }
    public String getUserPassword(){
        return preferences.getString(PASSWORD,null);
    }
    ////////////////////////
    public void setUserType(String id){
        editor.putString(USER_TYPE,id);
        editor.commit();
    }
    public String getUserType(){
        return preferences.getString(USER_TYPE,null);
    }
    ////////////////////////////
    public void setClientPairId(String id){
        editor.putString(CLIENT_PAIR_ID,id);
        editor.commit();
    }
    public String getClientPairId(){
        return preferences.getString(CLIENT_PAIR_ID,null);
    }
    ////////////////////////////
    public void setClientId(String id){
        editor.putString(CLIENT_ID,id);
        editor.commit();
    }
    public String getClientId(){
        return preferences.getString(CLIENT_ID,null);
    }
    ////////////////////////////
    public void setClientName(String id){
        editor.putString(CLIENT_NAME,id);
        editor.commit();
    }
    public String getClientName(){
        return preferences.getString(CLIENT_NAME,"");
    }
    ////////////////////////////
    public void setClientEmail(String id){
        editor.putString(CLIENT_EMAIL,id);
        editor.commit();
    }
    public String getClientEmail(){
        return preferences.getString(CLIENT_EMAIL,"");
    }
    ////////////////////////////
    public void setClientContactNumber(String id){
        editor.putString(CLIENT_CONTACT_NUMBER,id);
        editor.commit();
    }
    public String getClientContactNumber(){
        return preferences.getString(CLIENT_CONTACT_NUMBER,"");
    }

    ////////////////////////////
    public void setClientAddress(String id){
        editor.putString(CLIENT_ADDRESS,id);
        editor.commit();
    }
    public String getClientAddress(){
        return preferences.getString(CLIENT_ADDRESS,"");
    }
    ////////////////////////////
    public void setPresenterName(String id){
        editor.putString(PRESENTER,id);
        editor.commit();
    }
    public String getPresenterName(){
        return preferences.getString(PRESENTER,"");
    }
    ////////////////////////////
    public void setClientCode(String id){
        editor.putString(CLIENT_CODE,id);
        editor.commit();
    }
    public String getClientCode(){
        return preferences.getString(CLIENT_CODE,"");
    }

    ////////////////////////////
    public void setHandlerId(String id){
        editor.putString(HANDLER_ID,id);
        editor.commit();
    }
    public String getHandlerId(){
        return preferences.getString(HANDLER_ID,"");
    }
}
