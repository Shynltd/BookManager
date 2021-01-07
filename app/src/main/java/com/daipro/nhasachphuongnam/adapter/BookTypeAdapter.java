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
import com.daipro.nhasachphuongnam.fragment.BookFrag;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

class BookTypeHolder extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvLocation;
    TextView tvCount;
    ImageView imgMore;
    TextView tvAvt;

    public BookTypeHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvLocation = itemView.findViewById(R.id.tvLocation);
        tvCount = itemView.findViewById(R.id.tvStaffName);
        imgMore = itemView.findViewById(R.id.imgMore);
        tvAvt = itemView.findViewById(R.id.tvAvt);
    }
}

public class BookTypeAdapter extends RecyclerView.Adapter<BookTypeHolder> {
    private Context context;
    private List<BookType> list;
    AppDataDAO dao;

    public BookTypeAdapter(Context context, List<BookType> list) {
        this.context = context;
        this.list = list;
        dao= AppDataBase.getInstance(context).getDao();
    }

    @NonNull
    @Override
    public BookTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookTypeHolder(LayoutInflater.from(context).inflate(R.layout.item_booktype, parent, false));
    }

    AppCompatActivity activity;
    Dialog dialog;
    String name,location;
    @Override
    public void onBindViewHolder(@NonNull BookTypeHolder holder, int position) {
        final int idList=position;
        activity= (AppCompatActivity) context;
        final BookType bookType = list.get(position);
        holder.tvAvt.setText(bookType.name.charAt(0)+"");
        holder.tvName.setText(bookType.name);
        holder.tvLocation.setText(bookType.location);
        holder.tvCount.setText(bookType.count+" quyển");
        if (userSession.lv==0){
            holder.imgMore.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.Frag_Content, new BookFrag(),"book").commit();
                FuncFrag.setDataInt(bookType.id);
            }
        });
        final PopupMenu popupMenu = new PopupMenu(activity, holder.imgMore);
        popupMenu.inflate(R.menu.booktype_poup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage(R.string.confirm_delete).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.deleteBookType(bookType);
                                list.remove(idList);
                                dao.deleteBookByBookType(bookType.id);
                                notifyItemRemoved(idList);
                                notifyItemRangeChanged(idList, list.size());
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
                        break;
                    case R.id.item_edit:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_book_type, null, false);
                        final EditText editName = view.findViewById(R.id.edtName);
                        editName.setText(bookType.name);
                        final EditText edtLocation = view.findViewById(R.id.edtLocation);
                        edtLocation.setText(bookType.location);
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
                                    tipName.setError(activity.getString(R.string.error_is_empty));
                                }
                                if (location.length() == 0) {
                                    tipLocation.setErrorEnabled(true);
                                    tipLocation.setError(activity.getString(R.string.error_is_empty));
                                    return;
                                }
                                BookType bookTypeNew = new BookType(name, location, bookType.count);
                                bookTypeNew.id = bookType.id;
                                dao.upDateBookType(bookTypeNew);
                                list.set(idList,bookTypeNew);
                                notifyItemChanged(idList);
                                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        builder1.setView(view);
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog1.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                        dialog = alertDialog1;
                        alertDialog1.show();
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

    @Override
    public int getItemCount() {
        return list.size();
    }
}
