package com.bitdubai.sub_app.intra_user_community.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserInformation;
import com.bitdubai.sub_app.intra_user_community.R;
import com.bitdubai.sub_app.intra_user_community.holders.AvailableIntraUserViewHolder;
import com.bitdubai.sub_app.intra_user_community.holders.LoadingMoreViewHolder;

import java.util.List;

/**
 * @author Jose Manuel De Sousa
 */
public class AppFriendsListAdapter extends FermatAdapter<IntraUserInformation, FermatViewHolder> {

    public static final int DATA_ITEM = 1;
    public static final int LOADING_ITEM = 2;
    private boolean loadingData = true;

    public AppFriendsListAdapter(Context context, List<IntraUserInformation> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected FermatViewHolder createHolder(View itemView, int type) {
        if (type == DATA_ITEM)
            return new AvailableIntraUserViewHolder(itemView, type);
        if (type == LOADING_ITEM)
            return new LoadingMoreViewHolder(itemView, type);
        return null;
    }

    @Override
    public FermatViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        return createHolder(LayoutInflater.from(context).inflate(getCardViewResource(type), viewGroup, false), type);
    }

    protected int getCardViewResource(int type) {
        if (type == DATA_ITEM)
            return R.layout.ccp_connection_tab_list_item;
        if (type == LOADING_ITEM)
            return R.layout.ccp_loading_more_list_item;
        return 0;
    }

    @Override
    protected int getCardViewResource() {
        return 0;
    }

    @Override
    public void onBindViewHolder(FermatViewHolder holder, int position) {
        if (holder instanceof AvailableIntraUserViewHolder )
            super.onBindViewHolder(holder, position);

        else if (holder instanceof LoadingMoreViewHolder) {
            final LoadingMoreViewHolder loadingMoreViewHolder = (LoadingMoreViewHolder) holder;
            loadingMoreViewHolder.progressBar.setVisibility(loadingData ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void bindHolder(FermatViewHolder holder, IntraUserInformation data, int position) {
        final AvailableIntraUserViewHolder availableIntraUserViewHolder = (AvailableIntraUserViewHolder) holder;
        availableIntraUserViewHolder.bind(data);
    }

    @Override
    public int getItemViewType(int position) {
        return position == super.getItemCount() ? LOADING_ITEM : DATA_ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public boolean isLoadingData() {
        return loadingData;
    }

    public void setLoadingData(boolean loadingData) {
        this.loadingData = loadingData;
    }
}
