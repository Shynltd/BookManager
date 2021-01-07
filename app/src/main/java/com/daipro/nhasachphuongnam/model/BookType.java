package com.daipro.nhasachphuongnam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daipro.nhasachphuongnam.morefunc.FuncStr;

@Entity
public class BookType {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String location;
    @ColumnInfo
    public int count;
    @ColumnInfo
    public String kw;

    public BookType(String name, String location, int count) {
        this.name = name;
        this.location = location;
        this.count = count;
        this.kw = FuncStr.getKW(name+location);
    }
}
