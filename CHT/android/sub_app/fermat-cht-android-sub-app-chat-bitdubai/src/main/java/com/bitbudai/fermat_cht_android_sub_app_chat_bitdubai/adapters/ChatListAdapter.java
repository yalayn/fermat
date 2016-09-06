package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.filters.ChatListFilter;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.holders.ChatHolder;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.models.Chat;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.ChatComparator;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.Utils;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.all_definition.util.Validate;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.TypeMessage;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * ChatListAdapter
 * <p/>
 * Created by Leon Acosta (laion.cj91@gmail.com) on 06/09/2016.
 *
 * @author lnacosta
 * @version 1.0
 */
public class ChatListAdapter extends FermatAdapter<Chat, ChatHolder> implements Filterable {

    private String filterString;

    long time, nanos, milliseconds;

    public ChatListAdapter(Context context, ArrayList<Chat> chats) {
        super(context, chats);
    }

    @Override
    protected ChatHolder createHolder(View itemView, int type) {
        return new ChatHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.chat_list_listview;
    }

    @Override
    public void changeDataSet(List<Chat> dataSet) {

        Collections.sort(dataSet, new ChatComparator());

        super.changeDataSet(dataSet);
    }

    @Override
    protected void bindHolder(ChatHolder holder, Chat data, int position) {

        if (data != null) {
            if (data.getImgId() != null)
                holder.image.setImageBitmap(Utils.getRoundedShape(data.getImgId(), 400));
            else
                holder.image.setImageResource(R.drawable.cht_center_profile_icon_center);

            holder.contactname.setText(data.getContactName());

            if (data.getMessage().equals("Typing..")) {
                holder.lastmessage.setTextColor(Color.parseColor("#FF33A900"));
            } else {
                holder.lastmessage.setTextColor(Color.parseColor("#757575"));
            }
            holder.lastmessage.setText(data.getMessage());

            holder.dateofmessage.setText(getFormattedDate(data.getDateMessage()));

            holder.imageTick.setImageResource(0);

            if (data.getTypeMessage() == TypeMessage.OUTGOING) {

                holder.imageTick.setVisibility(View.VISIBLE);

                if (data.getStatus() == MessageStatus.SENT) {
                    holder.imageTick.setImageResource(R.drawable.cht_ticksent);
                } else if (data.getStatus() == MessageStatus.DELIVERED || data.getStatus() == MessageStatus.RECEIVED) {
                    holder.imageTick.setImageResource(R.drawable.cht_tickdelivered);
                } else if (data.getStatus() == MessageStatus.READ) {
                    holder.imageTick.setImageResource(R.drawable.cht_tickread);
                }
            } else
                holder.imageTick.setVisibility(View.GONE);

            if (data.getNoReadMsgs() > 0) {
                String nUnreadMessages = data.getNoReadMsgs().toString();
                if (nUnreadMessages.length() == 1)
                    nUnreadMessages = " " + nUnreadMessages + " ";
                holder.tvnumber.setText(nUnreadMessages);
                holder.tvnumber.setVisibility(View.VISIBLE);
            } else
                holder.tvnumber.setVisibility(View.GONE);
        }
    }

    public View getView() {
        LayoutInflater vi = LayoutInflater.from(context);
        View convertView = vi.inflate(R.layout.chat_list_listview, null);
        return convertView;
    }

    private DateFormat commonFormatter;
    private DateFormat todayFormatter;

    private String getFormattedDate(Timestamp timestamp) {

        if (commonFormatter == null) {

            try {

                commonFormatter = new SimpleDateFormat("MM/dd/yyyy");
                commonFormatter.setTimeZone(TimeZone.getDefault());

                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    todayFormatter = new SimpleDateFormat("HH:mm");
                } else {
                    todayFormatter = new SimpleDateFormat("hh:mm aa");
                }
            } catch (Exception e) {
                todayFormatter = new SimpleDateFormat("HH:mm");
            }
            todayFormatter.setTimeZone(TimeZone.getDefault());
        }

        time = timestamp.getTime();
        nanos = (timestamp.getNanos() / 1000000);
        milliseconds = time + nanos;
        Date dated = new Date(milliseconds);

        String datef = commonFormatter.format(dated);

        if (Validate.isDateToday(dated))
            return todayFormatter.format(dated);
        else {
            Date today = new Date();
            long dias = (today.getTime() - dated.getTime()) / (1000 * 60 * 60 * 24);
            if (dias == 1) {
                datef = "YESTERDAY";
            }
        }

        return datef;
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    @Override
    public Chat getItem(int position) {

        return dataSet != null ? (!dataSet.isEmpty()
                && position < dataSet.size()) ? dataSet.get(position) : null : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return new ChatListFilter(dataSet, this);
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

}
