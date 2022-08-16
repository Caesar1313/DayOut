package com.example.dayout.ui.fragments.profile.organizer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.ImageViewer;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.profile.ProfileData;
import com.example.dayout.models.profile.ProfileModel;
import com.example.dayout.models.profile.organizer.OrganizerProfileData;
import com.example.dayout.ui.dialogs.ReportDialog;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.ui.dialogs.notify.LoadingDialog;
import com.example.dayout.viewModels.UserViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dayout.api.ApiClient.BASE_URL;
import static com.example.dayout.helpers.view.ImageViewer.IMAGE_BASE_URL;

@SuppressLint("NonConstantResourceId")
public class OrganizerProfileFragment extends Fragment {

    View view;

    @BindView(R.id.organizer_back_arrow_btn)
    ImageButton backArrowButton;

    @BindView(R.id.organizer_profile_image)
    ImageView profileImage;

    @BindView(R.id.organizer_profile_bio)
    TextView profileBio;

    @BindView(R.id.organizer_profile_followers_count)
    TextView profileFollowersCount;

    @BindView(R.id.organizer_profile_trips_count)
    TextView profileTripsCount;

    @BindView(R.id.organizer_profile_gender)
    TextView profileGender;

    @BindView(R.id.organizer_profile_phone_number)
    TextView profilePhoneNumber;

    @BindView(R.id.organizer_profile_email)
    TextView profileEmail;

    @BindView(R.id.organizer_profile_full_name)
    TextView profileFullName;

    @BindView(R.id.organizer_profile_email_TV)
    TextView emailTV;

    @BindView(R.id.organizer_profile_email_icon)
    ImageButton emailIcon;

    @BindView(R.id.organizer_profile_rate)
    TextView profileRate;

    @BindView(R.id.organizer_profile_follow_button)
    Button followButton;

    @BindView(R.id.organizer_profile_report_button)
    Button reportButton;

    ProfileData data;

    public OrganizerProfileFragment(ProfileData data){
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_organizer_profile, container, false);
        ButterKnife.bind(this, view);
        initViews();
        setData();
        return view;
    }

    private void initViews() {
        backArrowButton.setOnClickListener(onBackArrowClicked);
        followButton.setOnClickListener(onFollowClicked);
        reportButton.setOnClickListener(onReportClicked);
    }



    private void setData() {
        setName(data.user.first_name, data.user.last_name);
        if (data.bio != null)
            setBio(data.bio);
        profileTripsCount.setText(String.valueOf(data.trips_count));
        profileFollowersCount.setText(String.valueOf(data.followers_count));
        profileGender.setText(data.user.gender);
        profilePhoneNumber.setText(data.user.phone_number);
        setFollowButtonText();
        profileRate.setText(String.valueOf(roundRating()));
        setEmail(data.user.email);
        downloadUserImage(data.user.photo);
    }

    private float roundRating(){
        return (float) (Math.round(data.rating * 10) / 10.0);
    }

    private void setFollowButtonText(){
        if(data.iFollowHim){
            followButton.setText(R.string.unfollow);
        } else {
            followButton.setText(R.string.follow);
        }
    }

    private void downloadUserImage(String url) {
        ImageViewer.downloadCircleImage(requireContext(), profileImage, R.drawable.profile_place_holder, IMAGE_BASE_URL + url);
    }

    @SuppressLint("SetTextI18n")
    private void setName(String firstName, String lastName) {
        profileFullName.setText(firstName + " " + lastName);
    }

    private void setEmail(String email) {
        if (email == null) {
            profileEmail.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
            emailTV.setVisibility(View.GONE);
        } else
            profileEmail.setText(email);
    }

    private void setBio(String bio) {
        if (bio != null) {
            profileBio.setText(bio);
        } else {
            profileBio.setText(R.string.biography);
        }
    }

    private int increaseFollowers(){
        int followers = Integer.parseInt(profileFollowersCount.getText().toString());
        return followers + 1;
    }

    private int decreaseFollowers(){
        int followers = Integer.parseInt(profileFollowersCount.getText().toString());
        return followers - 1;
    }

    private void followOrganizer(){
        UserViewModel.getINSTANCE().followOrganizer(data.user.id);
        UserViewModel.getINSTANCE().followMutableLiveData.observe(requireActivity(), followObserver);
    }

    private final Observer<Pair<Boolean, String>> followObserver = new Observer<Pair<Boolean, String>>() {
        @Override
        public void onChanged(Pair<Boolean, String> booleanStringPair) {
            if (booleanStringPair != null){
                if (booleanStringPair.first != null){
                    if(followButton.getText().toString().equals(getResources().getString(R.string.follow))){
                        NoteMessage.showSnackBar(requireActivity(), getResources().getString(R.string.you_are_following) + " " + data.user.first_name + " " + data.user.last_name);
                        profileFollowersCount.setText(String.valueOf(increaseFollowers()));
                        followButton.setText(R.string.unfollow);
                    } else if(followButton.getText().toString().equals(getResources().getString(R.string.unfollow))){
                        NoteMessage.showSnackBar(requireActivity(), getResources().getString(R.string.unfollowed));
                        profileFollowersCount.setText(String.valueOf(decreaseFollowers()));
                        followButton.setText(R.string.follow);
                    }
                } else
                    new ErrorDialog(requireContext(), booleanStringPair.second).show();
            } else
                new ErrorDialog(requireContext(), getResources().getString(R.string.error_connection)).show();
        }
    };

    private final View.OnClickListener onBackArrowClicked = view -> FN.popTopStack(requireActivity());

    private final View.OnClickListener onReportClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = data.user.first_name + " " + data.user.last_name;
            new ReportDialog(requireContext(), data.user.id, name).show();
        }
    };

    private final View.OnClickListener onFollowClicked = v -> followOrganizer();
}