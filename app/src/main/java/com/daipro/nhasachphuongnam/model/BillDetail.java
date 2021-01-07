package com.daipro.nhasachphuongnam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class BillDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public int idBook;
    @ColumnInfo
    public int count;
    @ColumnInfo
    public double price;
    @ColumnInfo
    public long time;
    @ColumnInfo
    public int idBill;

    public BillDetail(int idBook, int count, double price, long time, int idBill) {
        this.idBook = idBook;
        this.count = count;
        this.price = price;
        this.time = time;
        this.idBill = idBill;
    }
}
