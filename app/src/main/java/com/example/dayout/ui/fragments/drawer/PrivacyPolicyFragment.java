package com.example.dayout.ui.fragments.drawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.ui.activities.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrivacyPolicyFragment extends Fragment {


    @BindView(R.id.privacy_policy_txt)
    TextView privacyPolicyTxt;
    View view;
    @BindView(R.id.back_arrow)
    ImageButton backArrow;

    InputStream inputStream;
    BufferedReader reader;

    int type;
    public PrivacyPolicyFragment(int type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        ButterKnife.bind(this, view);
        intiView();

        return view;
    }


    @Override
    public void onStart() {
if (type == 2)
        ((MainActivity)requireActivity()).hideBottomBar();
        super.onStart();
    }

    private void intiView() {
        backArrow.setOnClickListener(v-> FN.popTopStack(requireActivity()));
        privacyPolicyTxt.setText(readAndGetPrivacyPolicy().toString());
    }

    private StringBuffer readAndGetPrivacyPolicy() {
        try {
            inputStream = requireActivity().getResources().openRawResource(R.raw.privacy_police_text);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            StringBuffer stringBuffer = new StringBuffer();
            while (line != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
                line = reader.readLine();
            }

            return stringBuffer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new StringBuffer(getString(R.string.no_privacy_policy));
    }
}