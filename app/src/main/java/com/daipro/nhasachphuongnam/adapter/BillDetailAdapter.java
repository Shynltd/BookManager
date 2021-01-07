package com.daipro.nhasachphuongnam.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.model.BillDetail;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.List;

import static com.daipro.nhasachphuongnam.adapter.SearchBookAdapter.bookChose;

class BillDetailHolder extends RecyclerView.ViewHolder {
    TextView tvBook;
    TextView tvPrice;
    TextView tvCount;
    TextView tvTime;
    ImageView imgMore;

    public BillDetailHolder(@NonNull View itemView) {
        super(itemView);
        tvBook = itemView.findViewById(R.id.tvBook);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvCount = itemView.findViewById(R.id.tvStaffName);
        tvTime = itemView.findViewById(R.id.tvTime);
        imgMore = itemView.findViewById(R.id.imgMore);

    }
}

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailHolder> {
    Context context;
    List<BillDetail> list;
    AppDataDAO dao;
    Bill bill;

    public BillDetailAdapter(Context context, List<BillDetail> list) {
        this.context = context;
        this.list = list;
        dao = AppDataBase.getInstance(context).getDao();
    }

    @NonNull
    @Override
    public BillDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillDetailHolder(LayoutInflater.from(context).inflate(R.layout.item_bill_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailHolder holder, final int position) {
        AppCompatActivity activity = (AppCompatActivity) context;
        final BillDetail billDetail = list.get(position);
        Book book = dao.getBookById(billDetail.idBook);
        bill = dao.getBillById(billDetail.idBill);
        if (book != null)
            holder.tvBook.setText(book.name);
        holder.tvCount.setText(billDetail.count + " quyển");
        holder.tvPrice.setText(FuncStr.reWriteMoney(billDetail.price));
        holder.tvTime.setText(FuncStr.timeAgo(billDetail.time));

        final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
        popupMenu.inflate(R.menu.book_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_edit:
                        openDialogEdit(billDetail,position);
                        break;
                    case R.id.item_delete:
                        openDialogDelete(billDetail,position);
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
    private EditText edtCount,edtPrice;
    private TextInputLayout tipCount;
    private SearchView svBook;
    RecyclerView rvBook;
    SearchBookAdapter bookAdapter;
    Button btnNe,btnPos;
    TextView tvCount;
    Book bookOld;
    int countBuy=0;
    private void openDialogEdit(final BillDetail billDetail, final int position) {
        countBuy=billDetail.count;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_bill_detail, null,false);
        edtCount=view.findViewById(R.id.edtCount);
        edtPrice=view.findViewById(R.id.edtPrice);
        tipCount=view.findViewById(R.id.tipCount);
        tvCount=view.findViewById(R.id.tvCount);
        svBook=view.findViewById(R.id.svBook);
        rvBook=view.findViewById(R.id.rvBook);
        btnPos=view.findViewById(R.id.btnPos);
        btnNe=view.findViewById(R.id.btnNe);
        edtCount.setEnabled(true);

        btnPos.setEnabled(false);
        bookOld=dao.getBookById(billDetail.idBook);
        bookChose=bookOld;
        svBook.setQuery(bookOld.name, false);
        edtCount.setText((billDetail.count)+"");
        edtPrice.setText(FuncStr.reWriteMoney(billDetail.count*bookOld.price));
        tvCount.setVisibility(View.VISIBLE);
        tvCount.setText("Tổng số: " + bookOld.count + " quyển");

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
                    tipCount.setErrorEnabled(false);
                    tipCount.setError(context.getString(R.string.bounded_count));
                    edtPrice.setText("");
                }
                if (edtCount.getText().toString().length()==0){
                    edtPrice.setText("");
                    btnPos.setEnabled(false);
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
                BillDetail billDetailN = new BillDetail(bookChose.id, countBuy,countBuy*bookChose.price, System.currentTimeMillis(),billDetail.idBill);
                billDetailN.id=billDetail.id;
                dao.updateBillDetail(billDetailN);

                BookType bookType =dao.getBookTypeById(bookChose.idBookType);
                bookType.count-=bookChose.count;

                bookOld.count+=billDetail.count;
                dao.upDateBook(bookOld);
                bookChose.count-=countBuy;
                dao.upDateBook(bookChose);
                bookType.count+=bookChose.count;
                dao.upDateBookType(bookType);

                bill.price-=billDetail.price;
                bill.price+=billDetailN.price;
                dao.updateBill(bill);

                list.set(position,billDetailN);
                notifyItemChanged(position);
                dialog.dismiss();
                Toast.makeText(context, R.string.add_complete, Toast.LENGTH_SHORT).show();
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
                    bookAdapter = new SearchBookAdapter(context, dao.getBookByKw("%" + FuncStr.getKW(newText) + "%"), svBook, rvBook,tvCount,edtCount);
                    rvBook.setAdapter(bookAdapter);
                    rvBook.setLayoutManager(new LinearLayoutManager(context));
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

    private void openDialogDelete(final BillDetail billDetail, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_delete)
        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bill.price-=billDetail.price;
                Book book= dao.getBookById(billDetail.idBook);
                book.count+=billDetail.count;
                dao.upDateBook(book);
                dao.updateBill(bill);
                dao.deleteBillDetail(billDetail);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                Toast.makeText(context, R.string.deleted, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
