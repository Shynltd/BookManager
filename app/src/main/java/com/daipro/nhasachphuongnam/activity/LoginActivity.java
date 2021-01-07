package com.daipro.nhasachphuongnam.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.base.BaseActivity;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class LoginActivity extends BaseActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private ToggleButton tgRemeber;
    AppDataDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        dao = AppDataBase.getInstance(this).getDao();
    }

    String userName, password;
    Boolean isRemember;

    public void checkLog(View view) {
        User user = dao.getUserByUserName(edtUserName.getText() + "");
        if (user != null && (user.userName).equalsIgnoreCase(edtUserName.getText().toString()) && (user.password).equals(edtPassword.getText() + "")) {
            userSession = user;
            setLog();
            openActivity(MainActivity.class, true);
            overridePendingTransition(R.anim.up_down_in, R.anim.up_down_out);
        } else {
            Toast.makeText(this, R.string.toast_wrong_password, Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        tgRemeber = findViewById(R.id.tgRemeber);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRe = findViewById(R.id.tvRe);
        getLog();
        if (isRemember) {
            tgRemeber.setChecked(true);
            edtUserName.setText(userName);
            edtPassword.setText(password);
        }

        ObjectAnimator animLeft = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.left_to_right);
        animLeft.setTarget(edtUserName);
        animLeft.start();
        animLeft = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.left_to_right);
        animLeft.setTarget(edtPassword);
        animLeft.start();


        ObjectAnimator animRight = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.right_to_left);
        animRight.setTarget(tgRemeber);
        animRight.start();
        animRight = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.right_to_left);
        animRight.setTarget(btnLogin);
        animRight.start();
        animRight = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.right_to_left);
        animRight.setTarget(tvRe);
        animRight.start();
    }

    private void getLog() {
        SharedPreferences sharedPreferences = getSharedPreferences("infoLog", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "user");
        password = sharedPreferences.getString("password", "0000");
        isRemember = sharedPreferences.getBoolean("isRemember", false);
    }

    private void setLog() {
        SharedPreferences sharedPreferences = getSharedPreferences("infoLog", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (tgRemeber.isChecked()) {
            editor.putBoolean("isRemember", true);
        } else {
            editor.putBoolean("isRemember", false);
        }
        editor.putString("userName", edtUserName.getText() + "");
        editor.putString("password", edtPassword.getText() + "");
        editor.apply();
    }
}
