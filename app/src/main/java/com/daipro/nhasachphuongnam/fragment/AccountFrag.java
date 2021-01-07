package com.daipro.nhasachphuongnam.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.activity.LoginActivity;
import com.daipro.nhasachphuongnam.adapter.UserAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.User;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.setDataInt;
import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class AccountFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_account;
    }

    private RecyclerView rvUser;
    UserAdapter adapter;
    List<User> list;
    AppDataBase appDataBase;
    AppBarLayout mAppBarLayout;
    Toolbar toolbar;
    AppDataDAO dao;
    AppCompatActivity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        if (userSession.lv == 1) {
            toolbar = view.findViewById(R.id.toolBar);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            rvUser = view.findViewById(R.id.rvBook);
            mAppBarLayout = view.findViewById(R.id.mAppbar);
            appDataBase = AppDataBase.getInstance(getActivity());
            dao = appDataBase.getDao();

            list = dao.getAllUser();
            ////

            adapter = new UserAdapter(getActivity(), list);
            rvUser.setAdapter(adapter);
            rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                        setHasOptionsMenu(true);
                    } else
                        setHasOptionsMenu(false);
                }
            });

            LinearLayout lnAddAccount = view.findViewById(R.id.lnAddAccount);
            lnAddAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogAddUserName();
                }
            });

            LinearLayout lnLogout = view.findViewById(R.id.lnLogout);
            lnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogLogout();
                }
            });
            LinearLayout lnChangePassword = view.findViewById(R.id.lnChangePassWord);
            lnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogChangePassword();
                }
            });
        } else {
            setDataInt(userSession.id);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new EditAccountFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.it_add_account:
                openDialogAddUserName();
                break;
            case R.id.it_change_password:
                openDialogChangePassword();
                break;
            case R.id.it_logout:
                openDialogLogout();
                break;
        }
        return true;
    }

    private void openDialogLogout() {
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

    private void openDialogChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_password, null, false);
        final TextInputLayout tipPassword = view.findViewById(R.id.tipPassword);
        final TextInputLayout tipPassword1 = view.findViewById(R.id.tipPassword1);
        final TextInputLayout tipPassword2 = view.findViewById(R.id.tipPassword2);

        final EditText edtPassword = view.findViewById(R.id.edtPassword);
        final EditText edtPassword2 = view.findViewById(R.id.edtPassword2);
        final EditText edtPassword1 = view.findViewById(R.id.edtPassword1);

        Button btnNe = view.findViewById(R.id.btnNe);
        Button btnPos = view.findViewById(R.id.btnPos);
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
                String password = edtPassword.getText().toString();
                String password1 = edtPassword1.getText().toString();
                String password2 = edtPassword2.getText().toString();

                if (!password.equals(userSession.password)) {
                    tipPassword.setErrorEnabled(true);
                    tipPassword.setError(getString(R.string.toast_password));
                    errorCount++;
                } else {
                    tipPassword.setErrorEnabled(false);
                }

                if (password1.length() == 0) {
                    tipPassword1.setEnabled(true);
                    tipPassword1.setError(getString(R.string.error_is_empty));
                    errorCount++;
                    if (password2.length() == 0) {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_is_empty));
                        errorCount++;
                    }
                } else if (!password1.matches("\\w+")) {
                    tipPassword1.setEnabled(true);
                    tipPassword1.setError(getString(R.string.error_password));
                    if (!password2.matches("\\w+")) {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_password));
                    }
                } else {
                    if (!password1.equals(password2)) {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_password_not_matches));
                    }
                }

                if (errorCount == 0) {
                    userSession.password = password1;
                    dao.updateUser(userSession);
                    dialog.dismiss();
                    Toast.makeText(activity, R.string.changed_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog = alertDialog;
        alertDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_account_frag_menu, menu);
    }

    String userName, name, password, phone, email, address;
    Dialog dialog = null;

    public void openDialogAddUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);

        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(userName);
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editText.getText() + "";
                if (userName.length() < 5) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_empty_5_chararcters));
                    return;
                }
                if (userName.indexOf(" ") > -1) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_username_have_space));
                    return;
                }
                if (!userName.matches("\\w*")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_special_characters));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                dialog.dismiss();
                openDialogAddName();
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
        dialog = alertDialog;
        alertDialog.show();
    }

    private void openDialogAddName() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(name);
        tvLabel.setText(R.string.tvLabel_add_account_name);
        btnNe.setText(R.string.btn_Pre);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText() + "";
                if (name.length() < 10) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_empty_10_chararcters));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                dialog.dismiss();
                openDialogAddPassword();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogAddUserName();
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

    private void openDialogAddPassword() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_password, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final EditText edtPassword1 = view.findViewById(R.id.edtPassword1);
        final EditText edtPassword2 = view.findViewById(R.id.edtPassword2);
        final TextInputLayout tipPassword1 = view.findViewById(R.id.tipPassword1);
        final TextInputLayout tipPassword2 = view.findViewById(R.id.tipPassword2);
        final Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        edtPassword1.setText(password);
        edtPassword2.setText(password);
        btnNe.setText(R.string.btn_cancel);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = edtPassword1.getText() + "", password2 = edtPassword2.getText() + "";
                if (password1.length() == 0) {
                    tipPassword1.setEnabled(true);
                    tipPassword1.setError(getString(R.string.error_is_empty));
                    if (password2.length() == 0) {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_is_empty));
                    }
                } else if (!password1.matches("\\w+")) {
                    tipPassword1.setEnabled(true);
                    tipPassword1.setError(getString(R.string.error_password));
                    if (!password2.matches("\\w+")) {
                        tipPassword2.setEnabled(true);
                        tipPassword2.setError(getString(R.string.error_password));
                    }
                } else {
                    if (password1.equals(password2)) {
                        dialog.dismiss();
                        password = password1;
                        openDialogAddPhoneNumber();
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
                openDialogAddName();
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

    private void openDialogAddPhoneNumber() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(phone);
        tvLabel.setText(R.string.tvLabel_add_account_phone);
        btnNe.setText(R.string.btn_Pre);
        editText.setBackgroundResource(R.drawable.edit_phone);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = editText.getText() + "";
                if (phone.length() != 10) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_under_10_digits_phone));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                dialog.dismiss();
                openDialogAddEmail();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogAddPassword();
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

    private void openDialogAddEmail() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(email);
        tvLabel.setText(R.string.tvLabel_add_account_email);
        btnNe.setText(R.string.btn_Pre);
        editText.setBackgroundResource(R.drawable.edit_email);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText.getText() + "";
                if (!email.matches("\\w+@\\w+(\\.\\w+){1,2}")) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_is_email));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                dialog.dismiss();
                openDialogAddAddress();
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogAddPhoneNumber();
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

    private void openDialogAddAddress() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_account_user_name, null, false);
        final EditText editText = view.findViewById(R.id.editText);
        Button btnPos = view.findViewById(R.id.btnPos);
        Button btnNe = view.findViewById(R.id.btnNe);
        final TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        editText.setText(address);
        tvLabel.setText(R.string.tvLabel_add_account_address);
        btnNe.setText(R.string.btn_Pre);
        btnPos.setText("Lưu");
        editText.setBackgroundResource(R.drawable.edit_address);

        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = editText.getText() + "";
                if (address.length() == 0) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(getString(R.string.error_is_empty));
                    return;
                }
                textInputLayout.setErrorEnabled(false);
                User user = new User(userName, name, password, phone, email, address, 0);
                int nId = (int) dao.addUser(user);
                user.id = nId;
                list.add(user);
                adapter.notifyItemInserted(list.size() - 1);
                dialog.dismiss();
                Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                userName = "";
                password = "";
                name = "";
                phone = "";
                email = "";
                address = "";
            }
        });
        btnNe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogAddEmail();
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
}
