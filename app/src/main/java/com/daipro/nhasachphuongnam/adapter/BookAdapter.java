package com.daipro.nhasachphuongnam.adapter;

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
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

class BookHolder extends RecyclerView.ViewHolder {

    TextView tvType;
    TextView tvCount;
    TextView tvPrice;
    TextView tvName;
    ImageView imgMore;

    public BookHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvType = itemView.findViewById(R.id.tvType);
        tvCount = itemView.findViewById(R.id.tvStaffName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        imgMore = itemView.findViewById(R.id.imgMore);
    }
}

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {
    private Context context;
    private List<Book> list;
    AppDataDAO dao;
    AppCompatActivity activity;

    public BookAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;
        dao= AppDataBase.getInstance(context).getDao();
        activity= (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, final int position) {
        final Book book = list.get(position);
        holder.tvName.setText(book.name);
        holder.tvType.setText(dao.getBookTypeById(book.idBookType).name);
        holder.tvCount.setText(book.count+"");
        holder.tvPrice.setText(FuncStr.reWriteMoney(book.price));
        if (userSession.lv==0){
            holder.imgMore.setVisibility(View.GONE);
        }

        final PopupMenu popupMenu = new PopupMenu(context, holder.imgMore);
        popupMenu.inflate(R.menu.book_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_edit:
                        openDialogEdit(position,book);
                        break;
                    case  R.id.item_delete:
                        openDialogDelete(book,position);
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

    private void openDialogDelete(final Book book, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.confirm_delete).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookType bookType = dao.getBookTypeById(book.idBookType);
                bookType.count-=book.count;

                dao.deleteBook(book);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                dao.upDateBookType(bookType);
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
        dialog=alertDialog;
        alertDialog.show();
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
    private void openDialogEdit(final int position, final Book book) {
        final BookType bookType= dao.getBookTypeById(FuncFrag.getDataInt());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_book, null, false);
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

        editName.setText(book.name);
        edtAuthor.setText(book.author);
        edtCount.setText(book.count+"");
        edtPrice.setText(book.price+"");

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
                } else{
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
                if ((edtPrice.getText()+"").length()==0){
                    tipPrice.setErrorEnabled(true);
                    tipPrice.setError(activity.getString(R.string.error_is_empty));
                    errorCount++;
                } else {
                    tipPrice.setErrorEnabled(false);
                }
                if (errorCount==0){
                    Book bookNew = new Book(name, count, price, author, FuncFrag.getDataInt());
                    bookNew.id=  book.id;
                    dao.upDateBook(bookNew);
                    list.set(position,bookNew);
                    notifyItemChanged(position);
                    dialog.dismiss();
                    bookType.count-=book.count;
                    bookType.count+=count;
                    dao.upDateBookType(bookType);
                    Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        dialog = alertDialog;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
