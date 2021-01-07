package com.daipro.nhasachphuongnam.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.adapter.BookAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class BookFrag extends BaseFragment {
    private ImageView imgSearch;
    private ImageView imgAdd;
    private ImageView imgEdit;

    @Override
    public int setLayout() {
        return R.layout.fragment_book;
    }

    AppCompatActivity activity;
    List<Book> list = new ArrayList<>();
    BookAdapter adapter;
    RecyclerView rvBook;
    Toolbar toolbar;
    AppBarLayout mAppBarLayout;
    AppDataDAO dao;
    BookType bookType;
    SearchView searchView;
    Button btnCancel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = AppDataBase.getInstance(getActivity()).getDao();
        initView(view);
        if (userSession.lv == 0) {
            imgAdd.setVisibility(View.GONE);
            imgEdit.setVisibility(View.GONE);
        }
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0){
                    setHasOptionsMenu(true);
                    Log.e("TAG", "onOffsetChanged: "+"Hide" );
                } else {
                    setHasOptionsMenu(false);
                    Log.e("TAG", "onOffsetChanged: "+"Show" );}
            }
        });
        bookType = dao.getBookTypeById(FuncFrag.getDataInt());
        list = dao.getBookByBookType(FuncFrag.getDataInt());


        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(bookType.name);

        adapter = new BookAdapter(activity, list);
        rvBook.setAdapter(adapter);
        rvBook.setLayoutManager(new GridLayoutManager(activity, 2));

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setEnabled(true);
                searchView.setVisibility(View.VISIBLE);
                searchView.requestFocusFromTouch();
                btnCancel.setVisibility(View.VISIBLE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setEnabled(false);
                searchView.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list = dao.getBookByKw("%" + FuncStr.getKW(newText) + "%", bookType.id);
                adapter = new BookAdapter(getActivity(), list);
                rvBook.setAdapter(adapter);
                return true;
            }
        });
    }

    private TextInputLayout tipName;
    private EditText editName;
    private TextInputLayout tipCount;
    private EditText edtCount;
    private TextInputLayout tipAuthor;
    private EditText edtAuthor;
    private TextInputLayout tipPrice;
    private EditText edtPrice;
    private Button btnPos;
    private Button btnNe;


    Dialog dialog;
    String name, author;
    int count;
    double price;

    private void openAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_book, null, false);
        tipName = view.findViewById(R.id.tipName);
        editName = view.findViewById(R.id.editName);
        tipCount = view.findViewById(R.id.tipCount);
        edtCount = view.findViewById(R.id.edtCount);
        tipAuthor = view.findViewById(R.id.tipAuthor);
        edtAuthor = view.findViewById(R.id.edtAuthor);
        tipPrice = view.findViewById(R.id.tipPrice);
        edtPrice = view.findViewById(R.id.edtPrice);
        btnPos = view.findViewById(R.id.btnPos);
        btnNe = view.findViewById(R.id.btnNe);

        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errorCount = 0;
                name = editName.getText().toString();
                author = edtAuthor.getText() + "";
                try {
                    price = Double.parseDouble(edtPrice.getText().toString());
                    count = Integer.parseInt(edtCount.getText().toString());
                } catch (Exception e) {
                    price = 0;
                    count = 0;
                }
                if (name.length() == 0) {
                    tipName.setErrorEnabled(true);
                    tipName.setError(activity.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipName.setErrorEnabled(false);
                }
                if (author.length() == 0) {
                    tipAuthor.setErrorEnabled(true);
                    tipAuthor.setError(activity.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipAuthor.setErrorEnabled(false);
                }
                if ((edtCount.getText() + "").length() == 0) {
                    tipCount.setErrorEnabled(true);
                    tipCount.setError(activity.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipCount.setErrorEnabled(false);
                }
                if ((edtPrice.getText() + "").length() == 0) {
                    tipPrice.setErrorEnabled(true);
                    tipPrice.setError(activity.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipPrice.setErrorEnabled(false);
                }
                if (errorCount == 0) {
                    Book book = new Book(name, count, price, author, FuncFrag.getDataInt());
                    book.id = (int) dao.addBook(book);
                    list.add(book);
                    adapter.notifyItemInserted(list.size() - 1);
                    dialog.dismiss();
                    bookType.count += count;
                    dao.upDateBookType(bookType);
                    Toast.makeText(activity, R.string.add_complete, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void initView(View view) {
        mAppBarLayout = view.findViewById(R.id.mAppbar);
        rvBook = view.findViewById(R.id.rvBillDetail);
        activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        imgSearch = view.findViewById(R.id.imgSearch);
        imgAdd = view.findViewById(R.id.imgAdd);
        imgEdit = view.findViewById(R.id.imgEdit);
        searchView = view.findViewById(R.id.searchView);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                openAddDialog();
                break;
            case R.id.item_search:
                searchView.setEnabled(true);
                searchView.setVisibility(View.VISIBLE);
                searchView.requestFocusFromTouch();
                btnCancel.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (userSession.lv == 1)
            inflater.inflate(R.menu.toolbar_library_booktype_menu, menu);
        else
            inflater.inflate(R.menu.menu_just_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
