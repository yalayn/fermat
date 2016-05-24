package com.bitdubai.sub_app.chat_community.app_connection;

import android.app.ProgressDialog;
import android.content.Context;

import com.bitdubai.fermat_android_api.engine.FermatFragmentFactory;
import com.bitdubai.fermat_android_api.engine.FooterViewPainter;
import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.engine.NavigationViewPainter;
import com.bitdubai.fermat_android_api.engine.NotificationPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.abstracts.AbstractFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.AppConnections;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySelectableIdentity;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySubAppModuleManager;
import com.bitdubai.fermat_cht_plugin.layer.sub_app_module.chat_community.developer.bitdubai.version_1.structure.ChatActorCommunitySelectableIdentityImpl;
import com.bitdubai.sub_app.chat_community.fragmentFactory.ChatCommunityFragmentFactory;
import com.bitdubai.sub_app.chat_community.navigation_drawer.ChatCommunityNavigationViewPainter;
import com.bitdubai.sub_app.chat_community.session.ChatUserSubAppSession;

import java.util.ArrayList;

/**
 * ChatCommunityFermatAppConnection
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 13/04/16.
 * @version 1.0
 */
public class ChatCommunityFermatAppConnection extends AppConnections<ChatUserSubAppSession> {

    private ChatUserSubAppSession chatUserSubAppSession;
    private ChatActorCommunitySubAppModuleManager moduleManager;
    private ChatActorCommunitySelectableIdentity activeIdentity;



    public ChatCommunityFermatAppConnection(Context activity) {
        super(activity);
    }

    @Override
    public FermatFragmentFactory getFragmentFactory() {
        return new ChatCommunityFragmentFactory();
    }

    @Override
    public PluginVersionReference getPluginVersionReference() {
        return  new PluginVersionReference(
                Platforms.CHAT_PLATFORM,
                Layers.SUB_APP_MODULE,
                Plugins.CHAT_COMMUNITY_SUP_APP_MODULE,
                Developers.BITDUBAI,
                new Version()
        );
    }

    @Override
    public AbstractFermatSession getSession() {
        return new ChatUserSubAppSession();
    }

    @Override
    public NavigationViewPainter getNavigationViewPainter() {
        //return new ChatCommunityNavigationViewPainter(getContext(),getActiveIdentity(),getFullyLoadedSession());
        getChtActiveIdentity();
        //TODO: el actorIdentityInformation lo podes obtener del module en un hilo en background y hacer un lindo loader mientras tanto
        return new ChatCommunityNavigationViewPainter(getContext(),activeIdentity, null);
    }

    @Override
    public HeaderViewPainter getHeaderViewPainter() {
        return null;
    }

    @Override
    public FooterViewPainter getFooterViewPainter() {
        return null;
    }

    @Override
    public NotificationPainter getNotificationPainter(String code){
        try
        {
            this.chatUserSubAppSession = (ChatUserSubAppSession)this.getSession();
            if(chatUserSubAppSession!=  null)
                moduleManager = chatUserSubAppSession.getModuleManager();
            return ChatCommunityBuildNotification.getNotification(moduleManager,code);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void getChtActiveIdentity() {

        if (activeIdentity == null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            FermatWorker worker = new FermatWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    return getMoreData();
                }
            };
            worker.setContext(getContext());
            worker.setCallBack(new FermatWorkerCallBack() {
                @SuppressWarnings("unchecked")
                @Override
                public void onPostExecute(Object... result) {
                    activeIdentity = null;
                    if (result != null &&
                            result.length > 0) {
                        progressDialog.dismiss();
                        if (getContext() != null) {
                            activeIdentity = (ChatActorCommunitySelectableIdentity) result[0];
                        }
                    } else
                        activeIdentity = null;
                }

                @Override
                public void onErrorOccurred(Exception ex) {
                    progressDialog.dismiss();
                    activeIdentity = null;
                    if (getContext() != null)
                        ex.printStackTrace();
                }
            });
            worker.execute();
        }
    }

    private synchronized ChatActorCommunitySelectableIdentity getMoreData() {
        ChatActorCommunitySelectableIdentity result = null;
        try {
            this.chatUserSubAppSession = (ChatUserSubAppSession)this.getSession();
            if(chatUserSubAppSession!=  null)
                moduleManager = chatUserSubAppSession.getModuleManager();
            result = moduleManager.getSelectedActorIdentity();
        }catch (CantGetSelectedActorIdentityException e){
            //There are no identities in device
            e.printStackTrace();
        }catch (ActorIdentityNotSelectedException e){
            //There are identities in device, but none selected
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
