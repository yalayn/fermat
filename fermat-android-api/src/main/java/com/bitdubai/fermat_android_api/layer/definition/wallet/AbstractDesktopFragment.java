package com.bitdubai.fermat_android_api.layer.definition.wallet;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.DesktopAppSelector;
import com.bitdubai.fermat_api.layer.dmp_module.InstalledApp;
import com.bitdubai.fermat_api.layer.dmp_module.sub_app_manager.InstalledSubApp;
import com.bitdubai.fermat_api.layer.dmp_module.wallet_manager.InstalledWallet;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;

/**
 * Created by Matias Furszyfer on 2016.03.09..
 */
public class AbstractDesktopFragment<S extends FermatSession,R extends ResourceProviderManager> extends AbstractFermatFragment<S,R> {



    protected void selectSubApp(InstalledSubApp installedSubApp){
        destroy();
        getDesktopAppSelector().selectSubApp(installedSubApp);
    }

    protected void selectApp(InstalledApp installedSubApp){
        destroy();
        getDesktopAppSelector().selectApp(installedSubApp);
    }

    protected void selectWallet(InstalledWallet installedWallet){
        destroy();
        getDesktopAppSelector().selectWallet(installedWallet);
    }

    private DesktopAppSelector getDesktopAppSelector(){
        return ((DesktopAppSelector)getActivity());
    }


}
