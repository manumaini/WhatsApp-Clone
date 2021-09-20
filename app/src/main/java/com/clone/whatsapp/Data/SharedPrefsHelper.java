package com.clone.whatsapp.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    //static references
    private static final String PREFS = "com.clone.whatsapp.PREFS";
    private static final String USER_UID = "user_Uid";
    private static final String USER_NAME="user_name";
    private static final String USER_PHONE = "user_phone";
    private static final String DATABASE_REFERENCE_ID = "reference_id";


    private final Context context;
    private static SharedPreferences sharedPreferences;
    private static SharedPrefsHelper uniqueInstance;


    public SharedPrefsHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
    }

    public static SharedPrefsHelper getInstance() {
        if (uniqueInstance == null)
            throw new IllegalStateException(
                    "SharedPrefsManager is not initialized, call initialize(applicationContext) " +
                            "static method first");
        return uniqueInstance;
    }

    public static void initialize(Context applicationContext) {
        if (applicationContext == null)
            throw new NullPointerException("Provided application context is null");
        else if (uniqueInstance == null) {
            synchronized (SharedPrefsHelper.class) {
                uniqueInstance = new SharedPrefsHelper(applicationContext);
            }

        }
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


    //uuid
    public void setUserUid(String Uuid){
        sharedPreferences.edit().putString(USER_UID, Uuid).commit();
    }

    public String getUserUid(){
        return sharedPreferences.getString(USER_UID,"");
    }

    //name
    public void setUserName(String name){
        sharedPreferences.edit().putString(USER_NAME,name).commit();

    }

    public String getUserName(){
        return sharedPreferences.getString(USER_NAME,"");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(USER_PHONE,"");
    }

    public void setUserPhone(String phone){
        sharedPreferences.edit().putString(USER_PHONE, phone).commit();
    }

    public void setDatabaseReferenceId(String id){
        sharedPreferences.edit().putString(DATABASE_REFERENCE_ID,id).commit();
    }

    public String getDatabaseReferenceId(){
        return sharedPreferences.getString(DATABASE_REFERENCE_ID,"");
    }
}
