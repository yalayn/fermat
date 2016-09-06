package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;

import java.util.ArrayList;

/**
 * Contact Adapter
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 19/01/16.
 * @version 1.0
 */

//public class ChatListAdapter extends FermatAdapter<ChatsList, ChatMessageHolder> {//ChatFactory
public class ProfileAdapter extends ArrayAdapter<String> {

    ArrayList<String> contactAlias;
    ArrayList<String> contactName;
    ArrayList<String> contactUUID;
    String action;
    private ErrorManager errorManager;
    Typeface tf;

    public ProfileAdapter(Context context, ArrayList contactName, ArrayList contactAlias, ArrayList contactUUID, String action, ErrorManager errorManager) {
        super(context, R.layout.profile_detail_item, contactName);
        //tf = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue Medium.ttf");
        this.contactAlias = contactAlias;
        this.contactName = contactName;
        this.contactUUID = contactUUID;
        this.action = action;
        this.errorManager = errorManager;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.profile_detail_item, null, true);
        try {
            switch (action) {
                case "detail":
                    item = inflater.inflate(R.layout.profile_detail_item, null, true);

                    TextView name2 = (TextView) item.findViewById(R.id.contact_detail_header);
                    name2.setText(" ");//name2.setText(contactName.get(0));

                    TextView alias2 = (TextView) item.findViewById(R.id.alias);
                    alias2.setText(" ");//(contactName.get(0));
                    //alias2.setTypeface(tf, Typeface.NORMAL);
                    break;
            }
        } catch (Exception e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
        return item;
    }

    public void refreshEvents(ArrayList contactName, ArrayList contactAlias, ArrayList contactUUID, String action) {
        this.contactName = contactName;
        this.contactAlias = contactAlias;
        this.contactUUID = contactUUID;
        this.action = action;
        notifyDataSetChanged();
    }
}