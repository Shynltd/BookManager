package com.daipro.nhasachphuongnam.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.adapter.BookTypeAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class BookTypeFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_library;
    }

    List<BookType> list = new ArrayList<>();
    BookTypeAdapter adapter;
    RecyclerView rvBookType;
    AppCompatActivity activity;
    ImageView imgSearchBookType, imgAddBookType;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    SearchView searchView;
    AppDataDAO dao;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = AppDataBase.getInstance(getActivity()).getDao();
        searchView = view.findViewById(R.id.searchView);
        appBarLayout = view.findViewById(R.id.mAppbar);
        imgAddBookType = view.findViewById(R.id.imgAddBookType);
        imgSearchBookType = view.findViewById(R.id.imgSearchBookType);
        activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        toolbar.setTitleTextColor(Color.WHITE);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        rvBookType = view.findViewById(R.id.rvBookType);
        activity.getSupportActionBar().setTitle(R.string.library_title);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                    setHasOptionsMenu(true);
                } else
                    setHasOptionsMenu(false);
            }
        });

        list = dao.getAllBookType();
        adapter = new BookTypeAdapter(getActivity(), list);
        rvBookType.setAdapter(adapter);
        rvBookType.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (userSession.lv == 0) {
            imgAddBookType.setVisibility(View.GONE);
        }

        imgAddBookType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBookType();
            }
        });
        imgSearchBookType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ImageView imgSearchClose = searchView.findViewById(R.id.search_mag_icon);
                imgSearchClose.setImageResource(R.drawable.ic_search_black_24dp);
                imgSearchClose.setColorFilter(Color.GRAY);
                final EditText edtSearch = searchView.findViewById(R.id.search_src_text);
                if (edtSearch.getText().toString().length() > 0) {
                    imgSearchClose.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                }
                imgSearchClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setVisibility(View.GONE);
                        edtSearch.setText("");
                    }
                });
                list = dao.getBookTypeByKeyWord("%" + FuncStr.getKW(newText) + "%");
                adapter = new BookTypeAdapter(getActivity(), list);
                rvBookType.setAdapter(adapter);
                return true;
            }
        });
    }

    private AlertDialog dialog = null;
    private String name, location;

    private void openAddBookType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_book_type, null, false);
        final EditText editName = view.findViewById(R.id.edtName);
        final EditText edtLocation = view.findViewById(R.id.edtLocation);
        final TextInputLayout tipName = view.findViewById(R.id.tipName);
        final TextInputLayout tipLocation = view.findViewById(R.id.tipLocation);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText() + "";
                location = edtLocation.getText() + "";
                if (name.length() == 0) {
                    tipName.setErrorEnabled(true);
                    tipName.setError(getString(R.string.error_is_empty));
                }
                if (location.length() == 0) {
                    tipLocation.setErrorEnabled(true);
                    tipLocation.setError(getString(R.string.error_is_empty));
                    return;
                }
                BookType bookType = new BookType(name, location, 0);
                bookType.id = (int) dao.addBookType(bookType);
                list.add(bookType);
                adapter.notifyItemInserted(list.size() - 1);
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                openAddBookType();
                break;
            case R.id.item_search:
                searchView.setVisibility(View.VISIBLE);
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
