package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments.wizard_pages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters.WizardListAdapter;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSessionReferenceApp;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.util.ChtConstants;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lozadaa on 20/01/16.
 */
public class WizardTwoStepBroadcastFragment extends AbstractFermatFragment {

    // Fermat Managers

    private ErrorManager errorManager;
    ChatManager chatManager;
    ListView list;
    // Defines a tag for identifying log entries
    ArrayList<String> contactname = new ArrayList<>();
    ArrayList<Bitmap> contacticon = new ArrayList<>();
    ArrayList<UUID> contactid = new ArrayList<>();
    private ChatSessionReferenceApp chatSession;
    WizardListAdapter adapter;

    public static WizardTwoStepBroadcastFragment newInstance() {
        return new WizardTwoStepBroadcastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            chatSession = ((ChatSessionReferenceApp) appSession);
            chatManager = chatSession.getModuleManager();
            errorManager = appSession.getErrorManager();
            adapter = new WizardListAdapter(getActivity(), contactname, contacticon, contactid, errorManager);

        } catch (Exception e) {
            if (errorManager != null)
                errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }

        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cht_ic_back_buttom));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ShowDialogWelcome();
        View layout = inflater.inflate(R.layout.contact_list_fragment, container, false);

        adapter = new WizardListAdapter(getActivity(), contactname, contacticon, contactid, errorManager);
        list = (ListView) layout.findViewById(R.id.list);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()/*new AdapterView.OnItemClickListener()*/ {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    appSession.setData("whocallme", "contact");
                    //TODO:Cardozo revisar esta logica ya no aplica, esto viene de un metodo nuevo que lo buscara del module del actor connections//chatManager.getChatUserIdentities();
                    appSession.setData(ChatSessionReferenceApp.CONTACT_DATA, null);//chatManager.getContactByContactId(contactid.get(position)));
                    changeActivity(Activities.CHT_CHAT_OPEN_MESSAGE_LIST, appSession.getAppPublicKey());
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
            }
        });


        return layout;
    }

    public void ShowDialogWelcome() {
        PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), (ReferenceAppFermatSession) appSession)
                .setBody(R.string.cht_chat_body_broadcast_step_one)
                .setSubTitle(R.string.cht_chat_subtitle_broadcast_step_one)
                .setTextFooter(R.string.cht_chat_footer_broadcast_step_one)
                .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                .setBannerRes(R.drawable.cht_banner)
                .setIconRes(R.drawable.chat_subapp)
                .build();
        presentationDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, ChtConstants.CHT_BROADCAST_NEXT_STEP, 0, "Create")
                .setTitle("Create")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case ChtConstants.CHT_BROADCAST_NEXT_STEP:
                saveSettingAndGoNextStep();
                break;
            case android.R.id.home:
                changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_ONE_DETAIL, appSession.getAppPublicKey());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSettingAndGoNextStep() {
        //TODO: AÑADIR SAVESETTINGS
        changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST);

    }


}