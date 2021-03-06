package com.thesunnahrevival.sunnahassistant.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.thesunnahrevival.sunnahassistant.R;
import com.thesunnahrevival.sunnahassistant.data.model.Reminder;
import com.thesunnahrevival.sunnahassistant.databinding.AltReminderCardViewBinding;
import com.thesunnahrevival.sunnahassistant.databinding.ReminderCardViewBinding;
import com.thesunnahrevival.sunnahassistant.views.interfaces.OnDeleteReminderListener;
import com.thesunnahrevival.sunnahassistant.views.interfaces.ReminderItemInteractionListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;


public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
    private final boolean mIsExpandedLayout;
    private List<Reminder> mAllReminders = new ArrayList<>();
    private ReminderItemInteractionListener mListener;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnDeleteReminderListener mDeleteReminderListener;
    private int mFrequency = 0;

    public ReminderListAdapter(Context context, boolean isExpandedLayout) {
        mContext = context;
        mIsExpandedLayout = isExpandedLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mLayoutInflater == null)
            mLayoutInflater = LayoutInflater.from(mContext);

        int layoutId;

        if (mIsExpandedLayout)
            layoutId = R.layout.reminder_card_view;
        else
            layoutId = R.layout.alt_reminder_card_view;

        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                layoutId, viewGroup, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Reminder currentReminder = mAllReminders.get(i);
        viewHolder.bind(currentReminder);
    }

    @Override
    public int getItemCount() {
        return mAllReminders.size();
    }

    public void setData(List<Reminder> data, int frequency) {
        mAllReminders = data;
        mFrequency = frequency;
        notifyDataSetChanged();
    }

    /**
     * @param listener the listener that will handle when a RecyclerView item is clicked
     */
    public void setOnItemClickListener(ReminderItemInteractionListener listener) {
        mListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    public void setDeleteReminderListener(OnDeleteReminderListener deleteReminderListener) {
        mDeleteReminderListener = deleteReminderListener;
    }

    public void deleteReminder(int position) {
        mDeleteReminderListener.deleteReminder(position);
    }

    /**
     * Inner Class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        private void bind(Reminder reminder) {
            binding.setVariable(BR.reminder, reminder);

            if (binding instanceof ReminderCardViewBinding){
                ((ReminderCardViewBinding) binding).toggleButton.setOnCheckedChangeListener((buttonView, isChecked) ->
                        mListener.onToggleButtonClick(buttonView, isChecked, reminder));
                ((ReminderCardViewBinding) binding).cardView.setOnClickListener((view) -> mListener.openBottomSheet(view, reminder));
                ((ReminderCardViewBinding) binding).setFrequency(mFrequency);
            }
            else if (binding instanceof AltReminderCardViewBinding){
                ((AltReminderCardViewBinding) binding).toggleButton.setOnCheckedChangeListener((buttonView, isChecked) ->
                        mListener.onToggleButtonClick(buttonView, isChecked, reminder));
                ((AltReminderCardViewBinding) binding).cardView.setOnClickListener((view) -> mListener.openBottomSheet(view, reminder));
            }


        }
    }
}
