package com.example.dayout.ui.dialogs;

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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.example.dayout.R;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.dialogs.notify.ErrorDialog;
import com.example.dayout.ui.dialogs.notify.LoadingDialog;
import com.example.dayout.viewModels.UserViewModel;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ReportDialog extends Dialog {

    Context context;
    String customerName;
    int customerId;
    LoadingDialog loadingDialog;

    @BindView(R.id.report_dialog_user_name)
    TextView customerName_TV;

    @BindView(R.id.report_dialog_purpose)
    EditText purpose;

    @BindView(R.id.report_dialog_cancel_button)
    Button cancelButton;

    @BindView(R.id.report_dialog_report_button)
    Button reportButton;

    public ReportDialog(@NonNull Context context, int customerId, String customerName) {
        super(context);
        this.context = context;
        this.customerName = customerName;
        this.customerId = customerId;
        setContentView(R.layout.report_dialog);
        setCancelable(false);
        ButterKnife.bind(this);
        initViews();
    }

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

    private void initViews() {
        loadingDialog = new LoadingDialog(getContext());
        customerName_TV.setText(customerName);
        cancelButton.setOnClickListener(onCancelClicked);
        reportButton.setOnClickListener(onReportClicked);
    }

    private JsonObject getReportData() {
        JsonObject object = new JsonObject();
        object.addProperty("target_id", customerId);
        object.addProperty("report", purpose.getText().toString());
        return object;
    }

    private void reportUser() {
        loadingDialog.show();
        UserViewModel.getINSTANCE().reportUser(getReportData());
        UserViewModel.getINSTANCE().reportMutableLiveData.observe((MainActivity) context, reportObserver);
    }

    private final Observer<Pair<Boolean, String>> reportObserver = new Observer<Pair<Boolean, String>>() {
        @Override
        public void onChanged(Pair<Boolean, String> booleanStringPair) {
            loadingDialog.dismiss();
            if (booleanStringPair != null) {
                if (booleanStringPair.first != null) {
                    NoteMessage.showSnackBar((MainActivity)context, getContext().getResources().getString(R.string.reported) + " " + customerName);
                    dismiss();
                } else
                    new ErrorDialog(getContext(), booleanStringPair.second).show();
            } else
                new ErrorDialog(getContext(), getContext().getResources().getString(R.string.error_connection)).show();
        }
    };

    private final View.OnClickListener onCancelClicked = v -> dismiss();

    private final View.OnClickListener onReportClicked = v -> reportUser();
}