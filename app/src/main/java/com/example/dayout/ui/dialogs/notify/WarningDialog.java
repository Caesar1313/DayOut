package com.example.dayout.ui.dialogs.notify;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.example.dayout.R;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.viewModels.TripViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class WarningDialog extends Dialog {

    Context context;

    LoadingDialog loadingDialog;

    @BindView(R.id.warning_dialog_message)
    TextView warningDialogMessage;

    @BindView(R.id.warning_dialog_yes)
    Button warningDialogYes;

    @BindView(R.id.warning_dialog_no)
    Button warningDialogNo;

    //for canceling booking
    boolean isCancelingBooking;
    int tripId;

    public WarningDialog(@NonNull Context context, String message, boolean isCancelingBooking, int tripId) {
        super(context);
        this.context = context;
        this.isCancelingBooking = isCancelingBooking;
        this.tripId = tripId;
        setContentView(R.layout.warning_dialog);
        setCancelable(false);
        ButterKnife.bind(this);
        initViews(message);
    }

    private void initViews(String message) {
        loadingDialog = new LoadingDialog(getContext());
        warningDialogMessage.setText(message);
        warningDialogNo.setOnClickListener(onNoButtonClicked);
        warningDialogYes.setOnClickListener(onYesButtonClicked);
    }

    private void cancelBooking(int tripId){
        loadingDialog.show();
        TripViewModel.getINSTANCE().cancelBooking(tripId);
        TripViewModel.getINSTANCE().cancelBookingMutableLiveData.observe((MainActivity) context, cancelBookingObserver);
    }

    private final Observer<Pair<Boolean, String>> cancelBookingObserver = new Observer<Pair<Boolean, String>>() {
        @Override
        public void onChanged(Pair<Boolean, String> booleanStringPair) {
            loadingDialog.dismiss();
            if(booleanStringPair != null){
                if(booleanStringPair.first != null){
                    NoteMessage.showSnackBar((MainActivity)context, context.getResources().getString(R.string.booking_canceled));
                    FN.popStack((MainActivity) context);
                } else
                    new ErrorDialog(context, booleanStringPair.second).show();
            } else
                new ErrorDialog(context, context.getResources().getString(R.string.error_connection)).show();
        }
    };

    private final View.OnClickListener onYesButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isCancelingBooking){
                cancelBooking(tripId);
                dismiss();
            }
        }
    };

    private final View.OnClickListener onNoButtonClicked = view -> dismiss();

    @Override
    public void show() {
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        getWindow().setAttributes(wlp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // match width dialog
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.show();
    }
}