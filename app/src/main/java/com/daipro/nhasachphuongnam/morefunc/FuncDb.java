package com.daipro.nhasachphuongnam.morefunc;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FuncDb {
    final private static String DB_NAME="BookManager.db";
    private static String DB_PATH="";
    private Context context;

    private boolean checkDatabase(){
        File file = new File(DB_PATH+DB_NAME);
        Log.e("have data", file.exists()+"");
        File dir = new File(DB_PATH);
        if (!dir.exists()){
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file.exists();
    }

    public FuncDb(Context context){
        this.context = context;
        if (Build.VERSION.SDK_INT>=17){
            DB_PATH=context.getApplicationInfo().dataDir+"/databases/";
        } else {
            DB_PATH="/data/data/"+context.getOpPackageName()+"/databases/";
        }
    }


    public void createDatabase() {
        if (!checkDatabase()){
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("DataBase", "Can't copy DataBase: "+e);
            }
        }
    }

    private void copyDataBase() throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        OutputStream outputStream = new FileOutputStream(DB_PATH+DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length=inputStream.read(buffer))>0){
            outputStream.write(buffer,0,length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
