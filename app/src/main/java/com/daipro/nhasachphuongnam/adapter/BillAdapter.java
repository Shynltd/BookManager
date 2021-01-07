package com.daipro.nhasachphuongnam.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.fragment.BillDetailFrag;
import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

class BillHolder extends RecyclerView.ViewHolder {
    TextView tvCustomer;
    TextView tvPrice;
    TextView tvStaffName;
    TextView tvTime;
    ImageView imgMore;

    public BillHolder(@NonNull View itemView) {
        super(itemView);
        tvCustomer = itemView.findViewById(R.id.tvBook);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvStaffName = itemView.findViewById(R.id.tvStaffName);
        tvTime = itemView.findViewById(R.id.tvTime);
        imgMore = itemView.findViewById(R.id.imgMore);
    }
}

public class BillAdapter extends RecyclerView.Adapter<BillHolder> {
    Context context;
    List<Bill> list;
    AppDataDAO dao;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
        dao = AppDataBase.getInstance(context).getDao();
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillHolder(LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, final int position) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        final Bill bill = list.get(position);
        holder.tvCustomer.setText(bill.customerName);
        holder.tvPrice.setText(FuncStr.reWriteMoney(bill.price));
        User user = dao.getUserById(bill.idStaff);
        if (user != null)
            holder.tvStaffName.setText(user.name + "");
        holder.tvTime.setText(FuncStr.miliesToTime(bill.time));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuncFrag.setDataInt(bill.id);
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.Frag_Content, new BillDetailFrag()).commit();
            }
        });
        final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
        popupMenu.inflate(R.menu.book_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        openDeleteDialog(bill, position);
                        break;
                    case R.id.item_edit:
                        openEditDialog(bill, position);
                        break;
                }
                return true;
            }
        });
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    private Dialog dialog;
    private EditText edtTime, edtCustomer;
    private Button btnPos, btnNe;
    private TextInputLayout tipCustomer;
    TextView tvUser;
    private TextInputLayout tipTime;
    List<User> userList = new ArrayList<>();
    String staff, customer;

    private void openEditDialog(final Bill bill, final int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_bill, null, false);
        edtTime = view.findViewById(R.id.edtPrice);
        tvUser = view.findViewById(R.id.tvUser);
        edtCustomer = view.findViewById(R.id.edtCount);
        tipCustomer = view.findViewById(R.id.tipCount);
        tipTime = view.findViewById(R.id.tipPrice);


        btnNe = view.findViewById(R.id.btnNe);
        btnPos = view.findViewById(R.id.btnPos);

        User user = dao.getUserById(bill.idStaff);
        tvUser.setText(String.format("%s (%s)", user.name, user.userName));
        edtCustomer.setText(bill.customerName);
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
                    tipCustomer.setError(context.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipCustomer.setErrorEnabled(false);
                }

                if (errorCount == 0) {
                    Bill billN = new Bill(customer, bill.price, bill.idStaff, FuncStr.timeToMilies(edtTime.getText() + ""));
                    billN.id = bill.id;
                    list.set(position, billN);
                    dao.updateBill(billN);
                    dialog.dismiss();
                    notifyItemChanged(position);
                    Toast.makeText(context, R.string.saved, Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Calendar calendar = Calendar.getInstance();
        edtTime.setText(FuncStr.miliesToTime(bill.time));
        if (userSession.lv == 1)
            edtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtTime.setText(FuncStr.toDateString(year, month + 1, dayOfMonth));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });
        builder.setView(view);
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDeleteDialog(final Bill bill, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_delete).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.deleteBill(bill);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                dialog.dismiss();
                Toast.makeText(context, R.string.deleted, Toast.LENGTH_SHORT).show();
                dao.deleteBillDetailByIdBill(bill.id);
            }
        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
