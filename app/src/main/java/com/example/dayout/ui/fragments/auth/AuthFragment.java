package com.example.dayout.ui.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.fragments.drawer.PrivacyPolicyFragment;
import com.example.dayout.ui.fragments.drawer.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dayout.config.AppConstants.AUTH_FRC;
import static com.example.dayout.config.AppConstants.MAIN_FRC;


public class AuthFragment extends Fragment {


    View view;

    @BindView(R.id.login_btn)
    Button loginButton;
    @BindView(R.id.sign_up_btn)
    Button signUpButton;
    @BindView(R.id.privacy_policy_txt)
    TextView privacyPolicyTxt;
    @BindView(R.id.guest_btn)
    Button guestButton;
    @BindView(R.id.setting_btn)
    Button settingBtutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_auth, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        loginButton.setOnClickListener(onLoginClicked);
        signUpButton.setOnClickListener(onSignUpClicked);
        privacyPolicyTxt.setOnClickListener(onPrivacyPolicyClicked);
        settingBtutton.setOnClickListener(onSettingClicked);
        guestButton.setOnClickListener(onGuestClicked);
    }

    private final View.OnClickListener onSettingClicked = v -> FN.addFixedNameFadeFragment(AUTH_FRC, requireActivity(), new SettingsFragment(1));


    private final View.OnClickListener onGuestClicked = v -> openMainActivity();

    private final View.OnClickListener onLoginClicked = v -> FN.addFixedNameFadeFragment(AUTH_FRC, requireActivity(), new LoginFragment());

    private final View.OnClickListener onSignUpClicked = v -> FN.addFixedNameFadeFragment(AUTH_FRC, requireActivity(), new SignUpFragment());

    private final View.OnClickListener onPrivacyPolicyClicked = v -> FN.addFixedNameFadeFragment(AUTH_FRC, requireActivity(), new PrivacyPolicyFragment(1));

    private void openMainActivity() {
        requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

}