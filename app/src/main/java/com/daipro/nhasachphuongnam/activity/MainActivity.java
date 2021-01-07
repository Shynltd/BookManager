package com.daipro.nhasachphuongnam.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.fragment.AccountFrag;
import com.daipro.nhasachphuongnam.fragment.BillFrag;
import com.daipro.nhasachphuongnam.fragment.BookTypeFrag;
import com.daipro.nhasachphuongnam.fragment.StatFrag;

import java.util.List;

import static com.daipro.nhasachphuongnam.morefunc.FuncFrag.userSession;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    private void initView() {
        bottomNav = findViewById(R.id.bottomNav);
        if (userSession.lv == 0)
            bottomNav.inflateMenu(R.menu.bottom_nav_menu_staff);
        else
            bottomNav.inflateMenu(R.menu.bottom_nav_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.Frag_Content, new AccountFrag()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        AccountFrag accountFrag = new AccountFrag();
        BookTypeFrag bookTypeFrag = new BookTypeFrag();
        BillFrag billFrag = new BillFrag();
        StatFrag statFrag = new StatFrag();
        switch (menuItem.getItemId()) {
            case R.id.navigation_account:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("h").replace(R.id.Frag_Content, accountFrag).commit();
                break;
            case R.id.navigation_library:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("h").replace(R.id.Frag_Content, bookTypeFrag).commit();
                break;
            case R.id.navigation_bill:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("h").replace(R.id.Frag_Content, billFrag).commit();
                break;
            case R.id.navigation_chart:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).addToBackStack("h").replace(R.id.Frag_Content, statFrag).commit();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        boolean hanlded = false;
        for (Fragment f : fragments) {
            if (f instanceof BaseFragment) {
                hanlded = ((BaseFragment) f).onBackPressed();
                if (hanlded)
                    break;
            }
        }
        if (!hanlded)
            super.onBackPressed();
        Log.e("back", hanlded + "");
    }
}
