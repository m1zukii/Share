package com.iu.share.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iu.share.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends BaseFragment {

    private String title;
    private TextView text;
    private SwipeRefreshLayout layout;
    private int time = 0;
    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_content, container, false);
//        text = (TextView) view.findViewById(R.id.textView);
//        layout = (SwipeRefreshLayout)view.findViewById(R.id.swipefresh);
//        return view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        text = (TextView) view.findViewById(R.id.textView);
        layout = (SwipeRefreshLayout) view.findViewById(R.id.swipefresh);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;

    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        layout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initData() {
        time++;
        text.setText(this.title+" "+time);
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    public static ContentFragment newInstance(String title) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
        this.title = bundle.getString("title");
    }
}
