package com.daipro.nhasachphuongnam.sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.model.BillDetail;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

@Database(entities = {User.class, BookType.class, Book.class, Bill.class, BillDetail.class},version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract AppDataDAO getDao();
    public static AppDataBase instance;

    public static AppDataBase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context, AppDataBase.class, "BookManager.db")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
    public static void destroyIntance(){
        instance=null;
    }
}
