package com.cleanslatetech.floc.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by pimpu on 3/5/2017.
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

    private  JSONArray jsonArrayAllChats = new JSONArray();
    private LayoutInflater inflater;
    private Context context;
    private int iUSerId;

    public ChatRecyclerAdapter(Context context, JSONArray jsonArrayAllChats, int iUSerId) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.jsonArrayAllChats = jsonArrayAllChats;
        this.iUSerId = iUSerId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            String msg = jsonArrayAllChats.getJSONObject(position).getString("UserMsg");
            String userName = jsonArrayAllChats.getJSONObject(position).getString("UserName");
            int iChatUserId = jsonArrayAllChats.getJSONObject(position).getInt("UserId");

            int pos;
            if(position == 0) {
                pos = 0;
            }
            else {
                pos = position-1;
            }

            if( position !=0 && jsonArrayAllChats.getJSONObject(pos).getInt("UserId") == iChatUserId ) {
                if( iUSerId == iChatUserId ) {

                    holder.frameLayoutMy.setVisibility(View.GONE);
                    holder.frameLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutMy.setVisibility(View.VISIBLE);

                    holder.tvMyContinueName.setText(userName);
                    holder.tvMyContinueText.setText(msg);
                } else {
                    holder.frameLayoutMy.setVisibility(View.GONE);
                    holder.frameLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutOther.setVisibility(View.VISIBLE);
                    holder.linearLayoutMy.setVisibility(View.GONE);

                    holder.tvOtherContinueName.setText(userName);
                    holder.tvOtherContinueText.setText(msg);
                }

            } else {
                if( iUSerId == iChatUserId ) {

                    holder.frameLayoutMy.setVisibility(View.VISIBLE);
                    holder.frameLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutMy.setVisibility(View.GONE);

                    holder.tvMyName.setText(userName);
                    holder.tvMyText.setText(msg);
                } else {
                    holder.frameLayoutMy.setVisibility(View.GONE);
                    holder.frameLayoutOther.setVisibility(View.VISIBLE);
                    holder.linearLayoutOther.setVisibility(View.GONE);
                    holder.linearLayoutMy.setVisibility(View.GONE);

                    holder.tvOtherName.setText(userName);
                    holder.tvOtherText.setText(msg);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArrayAllChats.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayoutOther, frameLayoutMy;
        LinearLayout linearLayoutOther, linearLayoutMy;

        AppCompatTextView tvOtherName, tvOtherText, tvOtherContinueName, tvOtherContinueText,
                tvMyName, tvMyText, tvMyContinueName, tvMyContinueText;

        public ViewHolder(View itemView) {
            super(itemView);

            frameLayoutOther = (FrameLayout) itemView.findViewById(R.id.chat_other_start);
            linearLayoutOther = (LinearLayout) itemView.findViewById(R.id.chat_other_continue);

            frameLayoutMy = (FrameLayout) itemView.findViewById(R.id.chat_my_start);
            linearLayoutMy = (LinearLayout) itemView.findViewById(R.id.chat_my_continue);

            tvOtherName = (AppCompatTextView) itemView.findViewById(R.id.chat_other_username);
            tvOtherText = (AppCompatTextView) itemView.findViewById(R.id.chat_other_text);

            tvOtherContinueName= (AppCompatTextView) itemView.findViewById(R.id.chat_other_continuation_username);
            tvOtherContinueText = (AppCompatTextView) itemView.findViewById(R.id.chat_other_continuation_text);

            tvMyName = (AppCompatTextView) itemView.findViewById(R.id.chat_my_username);
            tvMyText = (AppCompatTextView) itemView.findViewById(R.id.chat_my_text);

            tvMyContinueName= (AppCompatTextView) itemView.findViewById(R.id.chat_my_continuation_username);
            tvMyContinueText = (AppCompatTextView) itemView.findViewById(R.id.chat_my_continuation_text);
        }
    }

}
