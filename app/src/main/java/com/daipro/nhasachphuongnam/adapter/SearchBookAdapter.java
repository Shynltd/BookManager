package com.daipro.nhasachphuongnam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.List;

class SearchBookHolder extends RecyclerView.ViewHolder {
    TextView tvName,tvDetail;
    public SearchBookHolder(@NonNull View itemView) {
        super(itemView);
        tvName=itemView.findViewById(R.id.tvName);
        tvDetail=itemView.findViewById(R.id.tvDetail);
    }
}
public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookHolder> {
    Context context;
    List<Book> list;
    AppDataDAO dao;
    SearchView searchView;
    RecyclerView recyclerView;
    TextView tvCount;
    EditText edtCount;

    public SearchBookAdapter(Context context, List<Book> list, SearchView searchView, RecyclerView recyclerView, TextView tvCount,EditText edtCount) {
        this.context = context;
        this.list = list;
        this.searchView = searchView;
        this.recyclerView = recyclerView;
        this.tvCount=tvCount;
        this.edtCount=edtCount;
        dao= AppDataBase.getInstance(context).getDao();
    }

    @NonNull
    @Override
    public SearchBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchBookHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBookHolder holder, final int position) {
        final Book book = list.get(position);
        holder.tvName.setText(book.name);
        holder.tvDetail.setText(book.author+" ("+book.count+" quyển)");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.count>0) {
                    idBook = position;
                    searchView.setQuery(book.name, false);
                    recyclerView.setVisibility(View.GONE);
                    tvCount.setVisibility(View.VISIBLE);
                    tvCount.setText("Tổng số: " + book.count + " quyển");
                    edtCount.setEnabled(true);
                    bookChose = book;
                    edtCount.requestFocus();
                }
            }
        });
    }
    public static int idBook=-1;
    public static Book bookChose;

    @Override
    public int getItemCount() {
        return list.size();
    }
}
