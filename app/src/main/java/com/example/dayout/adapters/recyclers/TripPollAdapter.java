package com.example.dayout.adapters.recyclers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayout.R;
import com.example.dayout.config.AppSharedPreferences;
import com.example.dayout.helpers.view.FN;
import com.example.dayout.helpers.view.NoteMessage;
import com.example.dayout.models.poll.PollsData;
import com.example.dayout.models.room.pollsRoom.databases.PollsDatabase;
import com.example.dayout.ui.activities.MainActivity;
import com.example.dayout.ui.fragments.drawer.Posts.tab.PollsChoicesFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.dayout.config.AppConstants.MAIN_FRC;

public class TripPollAdapter extends RecyclerView.Adapter<TripPollAdapter.ViewHolder> {
    List<PollsData> list;
    Context context;


    public TripPollAdapter(List<PollsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void refresh(List<PollsData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_poll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        insertRoomObject(list.get(position));

        holder.pollTitle.setText(list.get(position).title);
        holder.pollDescription.setText(list.get(position).description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insertRoomObject(PollsData poll) {

        // insert object in room database
        PollsDatabase.getINSTANCE(context).iPolls()
                .insertPoll(poll)
                .subscribeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Trip Poll Adapter", "onError: " + e.toString());
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.poll_title)
        TextView pollTitle;

        @BindView(R.id.poll_description)
        TextView pollDescription;

        @BindView(R.id.poll_votes_button)
        Button pollVotesButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            pollVotesButton.setOnClickListener(v -> openVoteFragment());
        }

        private void openVoteFragment() {
            if (AppSharedPreferences.GET_ACC_TOKEN().isEmpty())
                NoteMessage.showSnackBar((MainActivity) context, context.getString(R.string.presmission_deny));
            else
                FN.addFixedNameFadeFragment(MAIN_FRC, (MainActivity) context, new PollsChoicesFragment(list.get(getAdapterPosition())));
        }
    }
}
