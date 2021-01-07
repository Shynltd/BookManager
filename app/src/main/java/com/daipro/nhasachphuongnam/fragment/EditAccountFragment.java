package com.daipro.nhasachphuongnam.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.activity.LoginActivity;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.morefunc.FuncFrag;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class EditAccountFragment extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_edit_account;
    }

    AppCompatActivity activity;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        init(view);
    }

    private TextView tvAvt;
    private TextView tvName;
    private ImageView imgLv, imgLogout;
    private TextView tvEmail;

    private Button btnEditUserName;
    private Button btnEditPassword;
    private Button btnEditName;
    private Button btnEditPhone;
    private Button btnEditEmail;
    private Button btnEditAddress;
    AppDataDAO dao;
    AppDataBase dataBase;
    User user;


    private void init(View view) {
        dataBase = AppDataBase.getInstance(getActivity());
        dao = dataBase.getDao();
        user = dao.getUserById(FuncFrag.getDataInt());
        tvAvt = view.findViewById(R.id.tvAvt);
        tvName = view.findViewById(R.id.tvName);
        imgLv = view.findViewById(R.id.imgLv);
        tvEmail = view.findViewById(R.id.tvEmail);
        imgLogout = view.findViewById(R.id.imgLogout);
        if (userSession.lv == 0) {
            imgLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogLogOut();
                }
            });
            imgLogout.setVisibility(View.VISIBLE);
        } else
            imgLogout.setVisibility(View.GONE);

        tvAvt.setText(FuncStr.getCharFirstName(user.name));
        tvName.setText(user.name);
        tvEmail.setText(user.email);

        btnEditUserName = view.findViewById(R.id.btnEditUserName);
        btnEditPassword = view.findViewById(R.id.btnEditPassword);
        btnEditName = view.findViewById(R.id.btnEditName);
        btnEditPhone = view.findViewById(R.id.btnEditPhone);
        btnEditEmail = view.findViewById(R.id.btnEditEmail);
        btnEditAddress = view.findViewById(R.id.btnEditAddress);
        if (user.lv == 1) {
            imgLv.setImageResource(R.drawable.admin_ic);
            tvName.setTextColor(Color.RED);
        }
        setTextToBtnEdit();

        btnEditUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditUserName();
            }
        });
        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditAddress();
            }
        });
        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditEmail();
            }
        });
        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditName();
            }
        });
        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditPhoneNumber();
            }
        });
        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditPassword();
            }
        });

    }

    private void openDialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(getString(R.string.comfirm_logout));
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }
        }).setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
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

    private void setTextToBtnEdit() {
        btnEditUserName.setText(user.getUserName());
        btnEditName.setText(user.name);
        btnEditPassword.setText(user.password);
        btnEditPhone.setText(user.phone);
        btnEditEmail.setText(user.email);
        btnEditAddress.setText(user.address);
        tvName.setText(user.name);
        tvEmail.setText(user.email);
        tvAvt.setText(FuncStr.getCharFirstName(user.name));
    }

    Dialog dialog;

    public void openDialogEditUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);

        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        tvLabel.setText(R.string.edit_user_name);
        editText.setText(user.userName);
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editText.getText().toString()).length() < 5) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_empty_5_chararcters));
                    return;
                }
                if ((editText.getText().toString()).indexOf(" ") > -1) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_username_have_space));
                    return;
                }
                if (!(editText.getText().toString()).matches("\\w*")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_special_characters));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                user.setUserName(editText.getText() + "");
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                dao.updateUser(user);
                setTextToBtnEdit();
                dialog.dismiss();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogEditName() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(user.name);
        tvLabel.setText(R.string.tvLabel_add_account_name);
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editText.getText().toString()).length() < 10) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_empty_10_chararcters));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                user.setName(editText.getText() + "");
                dao.updateUser(user);
                setTextToBtnEdit();
                dialog.dismiss();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogEditPassword() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_password, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final EditText edtPassword1 = view.findViewById(R.id.edtPassword1);
        final EditText edtPassword2 = view.findViewById(R.id.edtPassword2);
        final TextInputLayout tipPassword1 = view.findViewById(R.id.tipPassword1);
        final TextInputLayout tipPassword2 = view.findViewById(R.id.tipPassword2);
        final Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        edtPassword1.setText(user.password);
        edtPassword2.setText("");
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = edtPassword1.getText() + "", password2 = edtPassword2.getText() + "";
                if (password1.length() == 0) {
                    tipPassword1.setErrorEnabled(true);
                    tipPassword1.setError(getString(R.string.error_is_empty));
                    if (password2.length() == 0) {
                        tipPassword2.setErrorEnabled(true);
                        tipPassword2.setError(getString(R.string.error_is_empty));
                    }
                } else if (!password1.matches("\\w+")) {
                    tipPassword1.setErrorEnabled(true);
                    tipPassword1.setError(getString(R.string.error_password));
                    if (!password2.matches("\\w+")) {
                        tipPassword2.setErrorEnabled(true);
                        tipPassword2.setError(getString(R.string.error_password));
                    }
                } else {
                    if (password1.equals(password2)) {
                        user.setPassword(edtPassword1.getText() + "");
                        dao.updateUser(user);
                        Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        setTextToBtnEdit();
                    } else {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_password_not_matches));
                    }
                }
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogEditPhoneNumber() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(user.phone);
        tvLabel.setText(R.string.tvLabel_add_account_phone);
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);
        editText.setBackgroundResource(R.drawable.edit_phone);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editText.getText().toString()).length() != 10) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_under_10_digits_phone));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                user.setPhone(editText.getText() + "");
                dao.updateUser(user);
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                setTextToBtnEdit();
                dialog.dismiss();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogEditEmail() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(user.email);
        tvLabel.setText(R.string.tvLabel_add_account_email);
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);
        editText.setBackgroundResource(R.drawable.edit_email);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editText.getText().toString()).matches("\\w+@\\w+(\\.\\w+){1,2}")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_is_email));
                    return;
                }
                user.setEmail(editText.getText() + "");
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                setTextToBtnEdit();
                dao.updateUser(user);
                textInputLayout.setErrorEnabled(false);
                dialog.dismiss();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogEditAddress() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(user.address);
        tvLabel.setText(R.string.tvLabel_add_account_address);
        btnNe.setText(R.string.btn_cancel);
        btnPos.setText(R.string.save);
        btnPos.setText("Lưu");
        editText.setBackgroundResource(R.drawable.edit_address);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = editText.getText() + "";
                if (address.length() == 0) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_is_empty));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                user.setAddress(address);
                dao.updateUser(user);
                Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show();
                setTextToBtnEdit();
                dialog.dismiss();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }

    @Override
    public boolean onBackPressed() {
        if (userSession.lv == 1)
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new ViewAccountFrag()).commit();
        return super.onBackPressed();
    }
}
