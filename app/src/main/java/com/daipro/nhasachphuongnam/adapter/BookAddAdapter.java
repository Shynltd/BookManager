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
import com.daipro.nhasachphuongnam.model.BookType;

import java.util.List;

class BookAddHolder extends RecyclerView.ViewHolder {

    TextView tvName;

    public BookAddHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);

    }
}

public class BookAddAdapter extends RecyclerView.Adapter<BookAddHolder> {
    private Context context;
    private List<BookType> list;
    private AppCompatActivity activity;

    public BookAddAdapter(Context context, List<BookType> list) {
        this.context = context;
        this.list = list;
        activity= (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public BookAddHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookAddHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookAddHolder holder, int position) {
        final BookType bookType = list.get(position);
        holder.tvName.setText(bookType.name);
        id=bookType.id;
    }
    private int id;

    @Override
    public int getItemCount() {
        return list.size();
    }
}
