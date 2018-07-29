package com.example.mycalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends android.support.v4.app.Fragment {

    private Button logout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        pref = getActivity().getSharedPreferences("LogIn",MODE_PRIVATE);
        editor = pref.edit();

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        logout = (Button)view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test","1111");
                editor.putBoolean("isLogin",false);
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Log.i("test","11111");
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
