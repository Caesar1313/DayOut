package com.example.dayout.adapters.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayout.R;
import com.example.dayout.models.notification.NotificationModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    List<NotificationModel.Data> notifications;
    Context context;

    public NotificationsAdapter(List<NotificationModel.Data> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    public void refreshList(List<NotificationModel.Data> notifications){
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.notificationTitle.setText(notifications.get(position).title);
        holder.notificationDescription.setText(notifications.get(position).body);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @SuppressLint("NonConstantResourceId")
    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.notification_title)
        TextView notificationTitle;

        @BindView(R.id.notification_description)
        TextView notificationDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
