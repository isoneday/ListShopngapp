package com.teamproject.plastikproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamproject.plastikproject.R;

/**
 * Created by rage on 08.02.15. Create by task: 004
 */
public class UserPrefferenceFragment extends BaseFragment {
    private static final String TAG = UserPrefferenceFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_prefference, container, false);
        return view;
    }

}