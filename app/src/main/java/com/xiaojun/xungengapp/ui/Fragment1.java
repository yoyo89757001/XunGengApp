package com.xiaojun.xungengapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.adapter.F1Adapter;
import com.xiaojun.xungengapp.intface.ClickIntface;
import com.xiaojun.xungengapp.views.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment implements ClickIntface {

    private LRecyclerView lRecyclerView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<String> stringList=new ArrayList<>();
    private F1Adapter adapter;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        lRecyclerView = (LRecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new F1Adapter(stringList);
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        final DividerDecoration divider = new DividerDecoration.Builder(getContext())
                .setHeight(2.0f)
                .setPadding(2.0f)
                .setColorResource(R.color.transparent)
                .build();

        lRecyclerView.addItemDecoration(divider);
        //设置头部加载颜色
        lRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.blake ,R.color.write);
        lRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        lRecyclerView.setFooterViewColor(R.color.huise, R.color.blake ,R.color.write);
        //设置底部加载文字提示
        lRecyclerView.setFooterViewHint("拼命加载中","--------我是有底线的--------","网络不给力...");
        lRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                startActivity(new Intent(getContext(),DaKaActivity.class));

            }
        });


        lRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                //   Log.d("Fragment144444", "下拉刷新");

            }
        });

        lRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void BackId(int id) {



    }
}
