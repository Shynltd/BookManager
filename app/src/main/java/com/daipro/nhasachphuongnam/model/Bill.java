package com.daipro.nhasachphuongnam.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daipro.nhasachphuongnam.morefunc.FuncStr;

@Entity
public class Bill {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String customerName;
    @ColumnInfo
    public double price;
    @ColumnInfo
    public int idStaff;
    @ColumnInfo
    public long time;
    @ColumnInfo
    public String kw;

    public Bill(String customerName, double price, int idStaff, long time) {
        this.customerName = customerName;
        this.price = price;
        this.time = time;
        this.idStaff=idStaff;
        this.kw= FuncStr.getKW(customerName);
    }
}
