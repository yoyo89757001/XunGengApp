package com.xiaojun.xungengapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.utils.SpringEffect;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WangJiMiMaActivity extends Activity {

    @BindView(R.id.shouji)
    EditText shouji;
    @BindView(R.id.yanzhengma_tv)
    TextView yanzhengmaTv;
    @BindView(R.id.yazhengma_et)
    EditText yazhengmaEt;
    @BindView(R.id.mima1)
    EditText mima1;
    @BindView(R.id.mima2)
    EditText mima2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                  //  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
          //  window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_wang_ji_mi_ma);
        ButterKnife.bind(this);

        SpringEffect.doEffectSticky(findViewById(R.id.denglu), new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(WangJiMiMaActivity.this,MainActivity.class));

            }
        });
        SpringEffect.doEffectSticky(findViewById(R.id.back), new Runnable() {
            @Override
            public void run() {

              finish();

            }
        });
        SpringEffect.doEffectSticky(findViewById(R.id.yanzhengma_tv), new Runnable() {
            @Override
            public void run() {



            }
        });
    }
}
