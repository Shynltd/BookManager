package com.daipro.nhasachphuongnam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.adapter.BillDetailAdapter;
import com.daipro.nhasachphuongnam.adapter.SearchBookAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.model.BillDetail;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.List;

import static com.daipro.nhasachphuongnam.adapter.SearchBookAdapter.bookChose;

public class BillDetailFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_bill_detail;
    }

    List<BillDetail> list = new ArrayList<>();
    BillDetailAdapter adapter;
    AppCompatActivity activity;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    RecyclerView rvBillDetail;
    ImageView imgAdd,imgEdit;
    AppDataDAO dao;
    Bill bill;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i)-appBarLayout.getTotalScrollRange()==0){
                    setHasOptionsMenu(true);
                } else {
                    setHasOptionsMenu(false);
                }
            }
        });

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(R.string.title_bill_detail);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddBillDetail();
            }
        });

        list=dao.getBillDetailByIdBill(bill.id);
        adapter=new BillDetailAdapter(activity, list);
        rvBillDetail.setAdapter(adapter);
        rvBillDetail.setLayoutManager(new LinearLayoutManager(activity));
    }

    private Dialog dialog;
    private EditText edtCount,edtPrice;
    private TextInputLayout tipCount;
    private SearchView svBook;
    RecyclerView rvBook;
    SearchBookAdapter bookAdapter;
    Button btnNe,btnPos;
    TextView tvCount;
    int countBuy=0;


    private void openDialogAddBillDetail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_bill_detail, null,false);
        edtCount=view.findViewById(R.id.edtCount);
        edtPrice=view.findViewById(R.id.edtPrice);
        tipCount=view.findViewById(R.id.tipCount);
        tvCount=view.findViewById(R.id.tvCount);
        svBook=view.findViewById(R.id.svBook);
        rvBook=view.findViewById(R.id.rvBook);
        btnPos=view.findViewById(R.id.btnPos);
        btnNe=view.findViewById(R.id.btnNe);
        edtCount.setEnabled(false);
        edtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    countBuy=Integer.parseInt(edtCount.getText()+"");
                } catch (Exception e){}
                if(countBuy>0&&countBuy<= bookChose.count){
                    tipCount.setErrorEnabled(false);
                    edtPrice.setText(FuncStr.reWriteMoney(bookChose.price*countBuy));
                    btnPos.setEnabled(true);
                } else{
                    tipCount.setErrorEnabled(true);
                    tipCount.setError(getString(R.string.bounded_count));
                    edtPrice.setText("");
                    btnPos.setEnabled(false);
                }
                if (edtCount.getText().toString().length()==0){
                    edtPrice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillDetail billDetail = new BillDetail(bookChose.id, countBuy,countBuy*bookChose.price, System.currentTimeMillis(),bill.id);

                BookType bookType =dao.getBookTypeById(bookChose.idBookType);
                bookType.count-=bookChose.count;

                bookChose.count-=countBuy;
                dao.upDateBook(bookChose);
                bookType.count+=bookChose.count;
                dao.upDateBookType(bookType);

                bill.price+=billDetail.price;
                dao.updateBill(bill);

                billDetail.id= (int) dao.addBillDetail(billDetail);
                list.add(billDetail);
                adapter.notifyItemInserted(list.size());
                dialog.dismiss();
                Toast.makeText(activity, R.string.add_complete, Toast.LENGTH_SHORT).show();
            }
        });
        svBook.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tvCount.setVisibility(View.GONE);
                edtCount.setEnabled(false);
                edtCount.setText("");
                tipCount.setErrorEnabled(false);
                if (newText.length()>0) {
                    bookAdapter = new SearchBookAdapter(getActivity(), dao.getBookByKw("%" + FuncStr.getKW(newText) + "%"), svBook, rvBook,tvCount,edtCount);
                    rvBook.setAdapter(bookAdapter);
                    rvBook.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvBook.setVisibility(View.VISIBLE);
                } else {
                    rvBook.setVisibility(View.GONE);
                }
                return true;
            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog=alertDialog;
        alertDialog.show();
    }

    private void initView(View view) {
        dao= AppDataBase.getInstance(getActivity()).getDao();
        rvBillDetail = view.findViewById(R.id.rvBillDetail);
        toolbar = view.findViewById(R.id.toolBar);
        activity = (AppCompatActivity) getActivity();
        appBarLayout = view.findViewById(R.id.mAppbar);
        imgAdd=view.findViewById(R.id.imgAdd);
        imgEdit=view.findViewById(R.id.imgEdit);
        bill = dao.getBillById(FuncFrag.getDataInt());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_add:
                openDialogAddBillDetail();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option_bill, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
