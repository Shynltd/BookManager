package com.daipro.nhasachphuongnam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daipro.nhasachphuongnam.morefunc.FuncStr;

@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public int count;
    @ColumnInfo
    public double price;
    @ColumnInfo
    public String author;
    @ColumnInfo
    public int idBookType;
    @ColumnInfo
    public String kw;

    public Book(String name, int count, double price, String author, int idBookType) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.author = author;
        this.idBookType = idBookType;
        this.kw=FuncStr.getKW(name+author);
    }
}
