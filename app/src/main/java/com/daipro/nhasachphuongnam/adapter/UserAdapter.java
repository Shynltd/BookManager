package com.daipro.nhasachphuongnam.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.fragment.EditAccountFragment;
import com.daipro.nhasachphuongnam.fragment.ViewAccountFrag;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;

import java.util.List;

class UserHolder extends RecyclerView.ViewHolder {

    TextView tvAvt;
    TextView tvName;
    TextView tvMail;
    TextView tvPhone;
    ImageView imgLv;
    ImageView imgMore;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        tvAvt = itemView.findViewById(R.id.tvAvt);
        tvName = itemView.findViewById(R.id.tvName);
        tvMail = itemView.findViewById(R.id.tvMail);
        tvPhone = itemView.findViewById(R.id.tvEmail);
        imgLv = itemView.findViewById(R.id.imgLv);
        imgMore = itemView.findViewById(R.id.imgMore);
    }
}

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    private Context context;
    private List<User> list;
    AppCompatActivity activity;
    AppDataDAO dao;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        activity = (AppCompatActivity) context;
        dao = AppDataBase.getInstance(context).getDao();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    Dialog dialog = null;

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        final User user = list.get(position);
        Log.e("id", user.id + "");
        holder.tvName.setText(user.getName());
        holder.tvMail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhone());
        holder.tvAvt.setText(FuncStr.getCharFirstName(user.name));
        if (user.lv==1) {
            holder.tvName.setTextColor(Color.RED);
            holder.imgLv.setImageResource(R.drawable.admin_ic);
        }
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuncFrag.setDataInt(user.id);
                PopupMenu pm = new PopupMenu(context, holder.imgMore);
                pm.inflate(R.menu.account_popup_menu);
                pm.show();
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_edit:
                                activity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.Frag_Content, new EditAccountFragment()).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
                                break;
                            case R.id.item_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(R.string.confirm_delete).setNegativeButton(R.string.btn_cancel, null)
                                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                list.remove(position);
                                                dao.deleteUSer(user);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, R.string.deleted, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
                                alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                                dialog = alertDialog;
                                alertDialog.show();
                                break;
                            case R.id.item_view:
                                FuncFrag.setDataInt(user.id);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new ViewAccountFrag()).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commit();
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.Frag_Content, new ViewAccountFrag()).commit();
                FuncFrag.setDataInt(user.id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
