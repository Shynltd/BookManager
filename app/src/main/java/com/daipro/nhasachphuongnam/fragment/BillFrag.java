package com.daipro.nhasachphuongnam.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.adapter.BillAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class BillFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_bill;
    }

    AppCompatActivity activity;
    BillAdapter adapter;
    List<Bill> list = new ArrayList<>();
    RecyclerView rvBill;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    AppDataDAO dao;
    ImageView imgAdd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = AppDataBase.getInstance(getActivity()).getDao();
        activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        imgAdd = view.findViewById(R.id.imgAdd);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.title_bill_frag);
        appBarLayout = view.findViewById(R.id.mAppbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                    setHasOptionsMenu(true);
                } else {
                    setHasOptionsMenu(false);
                }
            }
        });
        if (userSession.lv == 1)
            list = dao.getAllBills();
        else
            list = dao.getBillByIdStaff(userSession.id);

        rvBill = view.findViewById(R.id.rvBill);
        adapter = new BillAdapter(activity, list);
        rvBill.setAdapter(adapter);
        rvBill.setLayoutManager(new LinearLayoutManager(activity));


        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddBill();
            }
        });
    }

    private Dialog dialog;
    private EditText edtTime, edtCustomer;
    private Button btnPos, btnNe;
    private TextInputLayout tipCustomer;
    private TextInputLayout tipTime;
    String customer;
    private TextView tvUser;

    private void openDialogAddBill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_bill, null, false);
        edtTime = view.findViewById(R.id.edtPrice);
        edtCustomer = view.findViewById(R.id.edtCount);
        tipCustomer = view.findViewById(R.id.tipCount);
        tipTime = view.findViewById(R.id.tipPrice);
        btnNe = view.findViewById(R.id.btnNe);
        btnPos = view.findViewById(R.id.btnPos);
        tvUser = view.findViewById(R.id.tvUser);

        tvUser.setText(String.format("%s (%s)", userSession.userName, userSession.name));
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
                customer = edtCustomer.getText() + "";
                if (customer.length() == 0) {
                    tipCustomer.setErrorEnabled(true);
                    tipCustomer.setError(getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipCustomer.setErrorEnabled(false);
                }

                if (errorCount == 0) {
                    Bill bill = new Bill(customer, 0, userSession.id, FuncStr.timeToMilies(edtTime.getText() + ""));
                    bill.id = (int) dao.addBill(bill);
                    list.add(bill);
                    dialog.dismiss();
                    adapter.notifyItemInserted(list.size());
                    Toast.makeText(activity, R.string.add_complete, Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Calendar calendar = Calendar.getInstance();
        edtTime.setText(FuncStr.toDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        if (userSession.lv == 1)
            edtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtTime.setText(FuncStr.toDateString(year, month + 1, dayOfMonth));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                openDialogAddBill();
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
