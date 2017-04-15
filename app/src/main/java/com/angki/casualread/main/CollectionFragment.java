package com.angki.casualread.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angki.casualread.R;

/**
 * Created by tengyu on 2017/4/15.
 */

public class CollectionFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_collection_fragment, container, false);

        TextView textView = (TextView) view.findViewById(R.id.test);
        return view;
    }
}
