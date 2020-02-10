package com.iu.share.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iu.share.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    public MessageFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "MessageFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ljr", "onCreateView: "+TAG);
        return inflater.inflate(R.layout.fragment_message, container, false);
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
