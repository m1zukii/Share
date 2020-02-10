package com.iu.share.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iu.share.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private static final String TAG = "HomePageFragment";

    public HomePageFragment() {
        // Required empty public constructor
    }




    FruitAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ljr", "onCreateView: "+TAG);
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        refreshLayout = view.findViewById(R.id.swipefresh);
        recyclerView = view.findViewById(R.id.zxc);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        initFruits();
        adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        for (Fruit fruit: fruitList) {
                            fruit.setName(fruit.getName()+System.currentTimeMillis());
                        }
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();

    }
    private ArrayList<Fruit> fruitList = new ArrayList<>();
    private void initFruits() {
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit(getRandomLengthName("Apple"), R.drawable.apple_pic);
            fruitList.add(apple);
            Fruit banana = new Fruit(getRandomLengthName("Banana"), R.drawable.banana_pic);
            fruitList.add(banana);
            Fruit orange = new Fruit(getRandomLengthName("Orange"), R.drawable.orange_pic);
            fruitList.add(orange);
            Fruit watermelon = new Fruit(getRandomLengthName("Watermelon"), R.drawable.watermelon_pic);
            fruitList.add(watermelon);
            Fruit pear = new Fruit(getRandomLengthName("Pear"), R.drawable.pear_pic);
            fruitList.add(pear);
            Fruit grape = new Fruit(getRandomLengthName("Grape"), R.drawable.grape_pic);
            fruitList.add(grape);
            Fruit pineapple = new Fruit(getRandomLengthName("Pineapple"), R.drawable.pineapple_pic);
            fruitList.add(pineapple);
            Fruit strawberry = new Fruit(getRandomLengthName("Strawberry"), R.drawable.strawberry_pic);
            fruitList.add(strawberry);
            Fruit cherry = new Fruit(getRandomLengthName("Cherry"), R.drawable.cherry_pic);
            fruitList.add(cherry);
            Fruit mango = new Fruit(getRandomLengthName("Mango"), R.drawable.mango_pic);
            fruitList.add(mango);
        }
    }
    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("ljr", "onAttach: "+TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ljr", "onCreate: "+TAG);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ljr", "onActivityCreated: "+TAG);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("ljr", "onResume: "+TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ljr", "onStart: "+TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("ljr", "onStop: "+TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ljr", "onDestroy: "+TAG);
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ljr", "onDestroyView: "+TAG);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("ljr", "onDetach: "+TAG);
    }
}
