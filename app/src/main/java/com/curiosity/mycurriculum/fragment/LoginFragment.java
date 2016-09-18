package com.curiosity.mycurriculum.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.curiosity.mycurriculum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Curiosity on 2016-9-17.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.code)
    ImageView im;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.title)
    EditText et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_frag, container, false);
        ButterKnife.bind(view);
        Button send = (Button) view.findViewById(R.id.snack);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(
                        view.findViewById(R.id.login_root), "this is a snackbar", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
