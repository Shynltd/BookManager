package com.daipro.nhasachphuongnam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;

import java.util.List;

class StatHolder extends RecyclerView.ViewHolder {
    TextView tvRank;
    TextView tvName;
    TextView tvPrice;
    TextView tvCount;

    public StatHolder(@NonNull View itemView) {
        super(itemView);

        tvRank = itemView.findViewById(R.id.tvRank);
        tvName = itemView.findViewById(R.id.tvName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvCount = itemView.findViewById(R.id.tvStaffName);

    }
}

public class StatAdapter extends RecyclerView.Adapter<StatHolder> {
    Context context;
    List<Book> list;

    public StatAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatHolder(LayoutInflater.from(context).inflate(R.layout.item_stat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatHolder holder, int position) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Book book = list.get(position);
        holder.tvRank.setText((position+1)+"");
        holder.tvName.setText(book.name);
        holder.tvCount.setText(book.count+" Quyển");
        holder.tvPrice.setText(FuncStr.reWriteMoney(book.price));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
