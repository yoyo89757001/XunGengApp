package com.xiaojun.xungengapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.utils.SpringEffect;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {


    @BindView(R.id.zhanghao)
    TextView zhanghao;
    @BindView(R.id.xingming)
    TextView xingming;
    @BindView(R.id.juese)
    TextView juese;
    @BindView(R.id.shouji)
    TextView shouji;
    @BindView(R.id.youxiang)
    TextView youxiang;
    Unbinder unbinder;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        SpringEffect.doEffectSticky(view.findViewById(R.id.tuichu), new Runnable() {
            @Override
            public void run() {



            }
        });


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
