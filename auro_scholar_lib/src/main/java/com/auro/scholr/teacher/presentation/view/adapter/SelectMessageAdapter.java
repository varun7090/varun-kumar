package com.auro.scholr.teacher.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.auro.scholr.R;
import com.auro.scholr.core.common.AppConstant;
import com.auro.scholr.databinding.SendMessageItemLayoutBinding;
import com.auro.scholr.databinding.TeacherDocumentItemLayoutBinding;
import com.auro.scholr.home.data.model.SelectResponseModel;
import com.auro.scholr.teacher.presentation.viewmodel.SelectMessageViewHolder;
import com.auro.scholr.teacher.presentation.viewmodel.TeacherDoumentViewHolder;

import java.util.List;

public class SelectMessageAdapter extends RecyclerView.Adapter {


    List<SelectResponseModel> list;
    public SelectMessageAdapter(List<SelectResponseModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AppConstant.FriendsLeaderBoard.SELECTMESSAGEADAPTER:
                SendMessageItemLayoutBinding sendMessageItemLayoutBindingBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.send_message_item_layout, viewGroup, false);
                return new SelectMessageViewHolder(sendMessageItemLayoutBindingBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = list.get(position).getViewType();

        switch (viewType) {
            case AppConstant.FriendsLeaderBoard.SELECTMESSAGEADAPTER:
                ((SelectMessageViewHolder) holder).bindUser(list.get(position), position);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {

        switch (list.get(position).getViewType()) {
            case AppConstant.FriendsLeaderBoard.SELECTMESSAGEADAPTER:
                return AppConstant.FriendsLeaderBoard.SELECTMESSAGEADAPTER;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}