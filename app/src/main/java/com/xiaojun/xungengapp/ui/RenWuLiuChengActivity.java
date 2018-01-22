package com.xiaojun.xungengapp.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.adapter.RenWuLiuChengAdapter;
import com.xiaojun.xungengapp.views.WrapContentLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenWuLiuChengActivity extends Activity {

    private LRecyclerView lRecyclerView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private List<String> stringList=new ArrayList<>();
    private RenWuLiuChengAdapter adapter;

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            // window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_ren_wu_liu_cheng);
        ButterKnife.bind(this);

        lRecyclerView = (LRecyclerView)findViewById(R.id.recyclerView);
        adapter = new RenWuLiuChengAdapter(stringList);
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        stringList.add("ddddd");
        lRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(RenWuLiuChengActivity.this, LinearLayoutManager.VERTICAL, false);
        lRecyclerView.setLayoutManager(linearLayoutManager);
        lRecyclerView.setAdapter(lRecyclerViewAdapter);

        final DividerDecoration divider = new DividerDecoration.Builder(RenWuLiuChengActivity.this)
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
        lRecyclerView.setLoadMoreEnabled(false);
        lRecyclerView.setPullRefreshEnabled(false);
        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

               // startActivity(new Intent(RenWuLiuChengActivity.this,RenWuLiuChengActivity.class));

            }
        });




    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


}
