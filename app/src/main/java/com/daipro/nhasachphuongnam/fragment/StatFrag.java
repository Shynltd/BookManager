package com.daipro.nhasachphuongnam.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.daipro.nhasachphuongnam.R;
import com.daipro.nhasachphuongnam.adapter.StatAdapter;
import com.daipro.nhasachphuongnam.base.BaseFragment;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.morefunc.FuncStr;
import com.daipro.nhasachphuongnam.sqlite.AppDataBase;
import com.daipro.nhasachphuongnam.sqlite.DAO.AppDataDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatFrag extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.fragment_stat;
    }

    AppCompatActivity activity;
    RecyclerView rvStat;
    Toolbar toolbar;
    AppBarLayout mAppBarLayout;
    List<Book> list = new ArrayList<>();
    StatAdapter adapter;
    BottomSheetBehavior behavior;
    AppDataDAO dao;
    boolean isShowSheet=false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity=(AppCompatActivity)getActivity();
        rvStat=view.findViewById(R.id.rvStat);
        toolbar=view.findViewById(R.id.toolBar);
        mAppBarLayout = view.findViewById(R.id.mAppbar);
        dao= AppDataBase.getInstance(getActivity()).getDao();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.top_ten_book_selling);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i)-appBarLayout.getTotalScrollRange()==0){
                    setHasOptionsMenu(true);
                } else {
                    setHasOptionsMenu(false);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        long toDay=FuncStr.timeToMilies(FuncStr.miliesToTime(System.currentTimeMillis()));

        long startMonth=FuncStr.timeToMilies(FuncStr.toDateString(year, month+1, 1));
        long endMonth=FuncStr.timeToMilies(FuncStr.toDateString(year, month+1, 30));

        long startYear=FuncStr.timeToMilies(FuncStr.toDateString(year, 1, 1));
        long endYear=FuncStr.timeToMilies(FuncStr.toDateString(year, 12, 31));

        TextView tvStatContent=view.findViewById(R.id.tvStatContent);

        tvStatContent.setText(String.format("Hôm nay: %s\nThang này: %s\nNam này: %s"
                , FuncStr.reWriteMoney(dao.getTodayMoney(toDay))
                ,FuncStr.reWriteMoney(dao.getMonthMoney(startMonth, endMonth))
                ,FuncStr.reWriteMoney(dao.getYearMoney(startYear, endYear)))
        );

        list=dao.getTop10Book();
        adapter=new StatAdapter(activity, list);
        rvStat.setAdapter(adapter);
        rvStat.setLayoutManager(new LinearLayoutManager(activity));


        final View bottomSheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        final FloatingActionButton fab = view.findViewById(R.id.mFab);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        isShowSheet=false;
                        fab.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isShowSheet=true;
                        fab.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowSheet){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    isShowSheet=true;
                    fab.setExpanded(false);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isShowSheet=false;
                }
            }
        });
    }
}
