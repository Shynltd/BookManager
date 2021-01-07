package com.daipro.nhasachphuongnam.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;

public class ViewAccountFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.view_account_fragment;
    }

    AppCompatActivity activity;
    ImageView imgBackArrow;
    User user;
    AppDataDAO dao;
    AppDataBase dataBase;
    Dialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (AppCompatActivity) getActivity();

        dataBase = AppDataBase.getInstance(getActivity());
        dao = dataBase.getDao();
        user = dao.getUserById(FuncFrag.getDataInt());

        LinearLayout lnEditInfo = view.findViewById(R.id.lnEditInfo);
        lnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new EditAccountFragment()).commit();
            }
        });

        LinearLayout lnDelteAccount = view.findViewById(R.id.lnDeleteAccount);
        lnDelteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.confirm_delete).setNegativeButton(R.string.btn_cancel, null)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(activity, R.string.deleted, Toast.LENGTH_SHORT).show();
                                dao.deleteUSer(user);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new AccountFrag()).commit();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
                alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog = alertDialog;
                alertDialog.show();
            }
        });
        initView(view);
    }

    private TextView tvAvt;
    private TextView tvName;
    private ImageView imgLv;
    private TextView tvEmail;

    private void initView(View view) {
        tvAvt = view.findViewById(R.id.tvAvt);
        tvName = view.findViewById(R.id.tvName);
        imgLv = view.findViewById(R.id.imgLv);
        tvEmail = view.findViewById(R.id.tvEmail);

        tvAvt.setText(FuncStr.getCharFirstName(user.name));
        tvEmail.setText(user.getEmail());
        tvName.setText(user.name);
        if (user.lv==1){
            tvName.setTextColor(Color.RED);
            imgLv.setImageResource(R.drawable.admin_ic);
        }
    }

    @Override
    public boolean onBackPressed() {
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new AccountFrag()).commit();
        return super.onBackPressed();
    }
}
