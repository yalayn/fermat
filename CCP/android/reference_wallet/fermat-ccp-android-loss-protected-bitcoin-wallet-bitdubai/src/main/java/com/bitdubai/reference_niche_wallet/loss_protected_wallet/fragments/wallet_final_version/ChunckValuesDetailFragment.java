package com.bitdubai.reference_niche_wallet.loss_protected_wallet.fragments.wallet_final_version;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.util.FermatAnimationsUtils;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces.BitcoinLossProtectedWalletSpend;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantListCryptoWalletIntraUserIdentityException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.LossProtectedWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetCryptoLossProtectedWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantListLossProtectedTransactionsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletTransaction;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.LossProtectedWalletConstants;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.adapters.ChunckValuesDetailAdapter;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.animation.AnimationManager;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.onRefreshList;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.SessionConstant;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.widget.Toast.makeText;


/**
 * Created by Gian Barboza on 12/04/16.
 */
public class ChunckValuesDetailFragment extends FermatWalletListFragment<BitcoinLossProtectedWalletSpend,ReferenceAppFermatSession<LossProtectedWallet>,ResourceProviderManager>
        implements FermatListItemListeners<BitcoinLossProtectedWalletSpend>, onRefreshList {

    /**
     * Session
     * */
    private ReferenceAppFermatSession<LossProtectedWallet> lossProtectedWalletSession;
    /**
     * Manager
     * */
    private LossProtectedWallet lossProtectedWalletManager;
    /**
     * DATA
     * */
    private List<BitcoinLossProtectedWalletSpend> listBitcoinLossProtectedWalletSpend;
    private BitcoinLossProtectedWalletSpend bitcoinLossProtectedWalletSpend;
    LossProtectedWalletSettings lossProtectedWalletSettings;
    private LossProtectedWalletTransaction transaction;

    private String chunckAmount = "";
    private double chunckExchangeRate = 0;
    private double chunckAmountSpent = 0;
    private int chunckPercentageSpent =0;

    private ErrorManager errorManager;

    private View rootView;
    private LinearLayout empty;
    private AnimationManager animationManager;
    private LinearLayout header;
    private LinearLayout headerSpending;
    private TextView txt_chunck_detail_balance;
    private TextView txt_chunck_detail_balance_type;
    private TextView txt_chunck_detail_exchangeRate;
    private TextView txt_chunck_detail_amountSpent;
    private TextView txt_percent_spent;
    private TextView info_into_progress;
    private ProgressBar progressBar_percent;
    private int offset = 0;

    private int MAX_PERCENTAGE = 100;
    private ShowMoneyType typeAmountSelected = ShowMoneyType.BITCOIN;



    BlockchainNetworkType blockchainNetworkType;


    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */

    public static ChunckValuesDetailFragment newInstance(){
        return new ChunckValuesDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        lossProtectedWalletSession = appSession;

        if(appSession.getData(SessionConstant.TYPE_AMOUNT_SELECTED) != null)
            typeAmountSelected = (ShowMoneyType)appSession.getData(SessionConstant.TYPE_AMOUNT_SELECTED);
        else
            appSession.setData(SessionConstant.TYPE_AMOUNT_SELECTED, typeAmountSelected);

        listBitcoinLossProtectedWalletSpend = new ArrayList<>();
        try {
            lossProtectedWalletManager = lossProtectedWalletSession.getModuleManager();

            getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Drawable drawable = getResources().getDrawable(R.drawable.background_gradient, null);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getPaintActivtyFeactures().setActivityBackgroundColor(drawable);
                                } catch (OutOfMemoryError o) {
                                    o.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });


            if(appSession.getData(SessionConstant.BLOCKCHANIN_TYPE) != null)
                blockchainNetworkType = (BlockchainNetworkType)appSession.getData(SessionConstant.BLOCKCHANIN_TYPE);
            else
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();

            onRefresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error: onCreate", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            rootView =  super.onCreateView(inflater, container, savedInstanceState);
            empty = (LinearLayout) rootView.findViewById(R.id.empty);
            setUp(inflater);

        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error: onCreateView", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void setUp(LayoutInflater inflater){
        try {
            setUpHeader(inflater);
            //setUpDonut(inflater);

        }catch (Exception e){
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
                    UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, e);
        }
    }


    private void setUpHeader(LayoutInflater inflater) {

        try {

            ActiveActorIdentityInformation intraUserLoginIdentity = null;
            intraUserLoginIdentity = lossProtectedWalletManager.getSelectedActorIdentity();
            String intraUserPk = null;
            if (intraUserLoginIdentity != null) {
                intraUserPk = intraUserLoginIdentity.getPublicKey();
            }

            //Get transaction data
            transaction = lossProtectedWalletManager.getTransaction(
                    (UUID)lossProtectedWalletSession.getData(SessionConstant.TRANSACTION_DETAIL_ID),
                    lossProtectedWalletSession.getAppPublicKey(),
                    intraUserPk);

            //Component UI
            txt_chunck_detail_balance = (TextView) rootView.findViewById(R.id.txt_amount_chunck_detail);
            txt_chunck_detail_balance_type = (TextView) rootView.findViewById(R.id.txt_amount_chunck_detail_type);
            txt_chunck_detail_exchangeRate = (TextView) rootView.findViewById(R.id.txt_exchange_rate_chunck_detail);
            txt_chunck_detail_amountSpent = (TextView) rootView.findViewById(R.id.txt_amount_spent);
            txt_percent_spent = (TextView) rootView.findViewById(R.id.txt_percentage_spent);
            progressBar_percent = (ProgressBar) rootView.findViewById(R.id.progressBarLine);
            info_into_progress = (TextView) rootView.findViewById(R.id.info_into_progress);


            chunckAmount          = WalletUtils.formatBalanceString(transaction.getAmount(), typeAmountSelected.getCode());
            chunckExchangeRate    = transaction.getExchangeRate();
            chunckAmountSpent     = getTotalSpent();
            chunckPercentageSpent = Integer.parseInt(WalletUtils.formatAmountStringNotDecimal(getSpendingPercentage(transaction)));

            //stylize the progress bar
            info_into_progress.setText(chunckPercentageSpent + "%");
            progressBar_percent.setProgress(chunckPercentageSpent);
            //set data in header
            txt_chunck_detail_balance.setText(chunckAmount);
            txt_chunck_detail_exchangeRate.setText("(1 BTC = USD " + chunckExchangeRate + ")");
            txt_chunck_detail_amountSpent.setText("BTC Spent: " + chunckAmountSpent + " BTC");
            txt_percent_spent.setText("(" + chunckPercentageSpent + "%)");


        } catch (CantListLossProtectedTransactionsException e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Error Exception : CantListLossProtectedTransactionsException",
                    Toast.LENGTH_SHORT).show();
         } catch (CantGetSelectedActorIdentityException e) {
            makeText(getActivity(), "Error Exception : CantGetSelectedActorIdentityException",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ActorIdentityNotSelectedException e) {
            makeText(getActivity(), "Error Exception : ActorIdentityNotSelectedException",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            listBitcoinLossProtectedWalletSpend = new ArrayList<>();
            animationManager = new AnimationManager(rootView,empty);
            getPaintActivtyFeactures().addCollapseAnimation(animationManager);
        } catch (Exception e){
            makeText(getActivity(), "recovering from system error: onActivityCreated", Toast.LENGTH_SHORT).show();
            lossProtectedWalletSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
    }


    @Override
    public void onItemClickListener(BitcoinLossProtectedWalletSpend data, int position) {
        bitcoinLossProtectedWalletSpend = data;
        onRefresh();
    }

    @Override
    public void onLongItemClickListener(BitcoinLossProtectedWalletSpend data, int position) {

    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.chunck_detail_list_main;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.transactions_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                listBitcoinLossProtectedWalletSpend = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(listBitcoinLossProtectedWalletSpend);
                if(listBitcoinLossProtectedWalletSpend.isEmpty()) FermatAnimationsUtils.showEmpty(getActivity(), true, empty);
                else FermatAnimationsUtils.showEmpty(getActivity(),false,empty);

            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null){
            adapter = new ChunckValuesDetailAdapter(
                    getActivity(),
                    listBitcoinLossProtectedWalletSpend,
                    lossProtectedWalletManager,
                    lossProtectedWalletSession,
                    this);
            adapter.setFermatListEventListener(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }



    public void setReferenceWalletSession(ReferenceAppFermatSession<LossProtectedWallet> lossProtectedWalletSession) {
        this.lossProtectedWalletSession = lossProtectedWalletSession;
    }


    //tool bar menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.add(1, LossProtectedWalletConstants.IC_ACTION_HELP_PRESENTATION, 1, "help").setIcon(R.drawable.loos_help_icon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }



    @Override
    public List<BitcoinLossProtectedWalletSpend> getMoreDataAsync(FermatRefreshTypes refreshType,int pos) {
            long spendingAmount = 0;
        try {
            //when refresh offset set 0
            if (refreshType.equals(FermatRefreshTypes.NEW))
                offset = 0;

            listBitcoinLossProtectedWalletSpend = lossProtectedWalletManager.listSpendingBlocksValue(
                    lossProtectedWalletSession.getAppPublicKey(),
                    (UUID)lossProtectedWalletSession.getData(SessionConstant.TRANSACTION_DETAIL_ID));
        } catch (Exception e) {
            lossProtectedWalletSession.getErrorManager().reportUnexpectedSubAppException(SubApps.CWP_WALLET_STORE,
                    UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            e.printStackTrace();
        }

        return listBitcoinLossProtectedWalletSpend;
    }

    private double getTotalSpent(){

        double spendingAmount = 0;
        try {

            for (BitcoinLossProtectedWalletSpend spendingData : listBitcoinLossProtectedWalletSpend) {
                if (spendingData.getAmount() != 0){
                    spendingAmount += Double.parseDouble(WalletUtils.formatBalanceString(spendingData.getAmount(),ShowMoneyType.BITCOIN.getCode()));
                }
            }
            return spendingAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }

       return 0;
    }

    private int getSpendingPercentage(LossProtectedWalletTransaction transaction){


        double spendingAmount = 0,totalAmount = 0;
        int totalSpendingPercentage = 0;
        try {
            //call spending list
            spendingAmount = getTotalSpent();

            totalAmount = Double.parseDouble(WalletUtils.formatBalanceString(transaction.getAmount(), ShowMoneyType.BITCOIN.getCode()));

            totalSpendingPercentage = (int) ((spendingAmount * 100)/totalAmount);

            return totalSpendingPercentage;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}




