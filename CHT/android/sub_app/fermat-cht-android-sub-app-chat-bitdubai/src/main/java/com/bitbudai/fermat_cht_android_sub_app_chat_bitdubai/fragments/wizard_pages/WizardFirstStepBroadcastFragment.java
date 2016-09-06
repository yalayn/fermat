package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.fragments.wizard_pages;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

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
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatPreferenceSettings;

/**
 * Created by Lozadaa on 20/01/16.
 */
public class WizardFirstStepBroadcastFragment extends AbstractFermatFragment {
    private static final String TAG = "WizardFirstStepBroadcastFragment";


    Toolbar toolbar;
    private ErrorManager errorManager;
    private ChatPreferenceSettings chatSettings;
    private ChatSessionReferenceApp chatSession;
    private ChatManager chatManager;

    public static WizardFirstStepBroadcastFragment newInstance() {
        return new WizardFirstStepBroadcastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatSession = ((ChatSessionReferenceApp) appSession);
        chatManager = chatSession.getModuleManager();

        errorManager = appSession.getErrorManager();
        toolbar = getToolbar();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.cht_ic_back_buttom));
        //Obtain chatSettings  or create new chat settings if first time opening chat platform
        chatSettings = null;
        try {
            chatSettings = chatManager.loadAndGetSettings(appSession.getAppPublicKey());
        } catch (Exception e) {
            chatSettings = null;
        }

        if (chatSettings == null) {
            chatSettings = new ChatPreferenceSettings();
            chatSettings.setIsPresentationHelpEnabled(true);
            try {
                chatManager.persistSettings(appSession.getAppPublicKey(), chatSettings);
            } catch (Exception e) {
                if (errorManager != null)
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.cht_wizard_broadcast_one_step, container, false);
        final RadioButton radioA = (RadioButton) layout.findViewById(R.id.radioButton);
        final RadioButton radioB = (RadioButton) layout.findViewById(R.id.radioButton2);

        ShowDialogWelcome();
        Button cbutton = (Button) layout.findViewById(R.id.btncreate);
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioA.isChecked() || radioB.isChecked()) {
                    if (radioA.isChecked()) {
                        changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_TWO_DETAIL, appSession.getAppPublicKey());
                        Log.i("ChangeActivity", "To the Wizard Two Simple");
                    }
                    if (radioB.isChecked()) {
                        changeActivity(Activities.CHT_CHAT_BROADCAST_WIZARD_TWO_SCHEDULED_DETAIL, appSession.getAppPublicKey());
                        Log.i("ChangeActivity", "To the Wizard Two Scheduled");
                    }
                } else
                    Toast.makeText(getActivity(), "Please select at least one option", Toast.LENGTH_SHORT).show();
            }
        });
        return layout;
    }

    public void ShowDialogWelcome() {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), (ReferenceAppFermatSession) appSession)
                    .setBody(R.string.cht_chat_body_broadcast_step_one)
                    .setSubTitle(R.string.cht_chat_subtitle_broadcast_step_one)
                    .setTextFooter(R.string.cht_chat_footer_broadcast_step_one)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setBannerRes(R.drawable.cht_banner)
                    .setIconRes(R.drawable.chat_subapp)
                    .build();
            presentationDialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        menu.add(0, ChtConstants.CHT_ICON_HELP, 0, "help").setIcon(R.drawable.cht_help_icon)
                .setVisible(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case ChtConstants.CHT_ICON_HELP:
                ShowDialogWelcome();
                break;
            case android.R.id.home:
                changeActivity(Activities.CHT_CHAT_OPEN_CHATLIST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}