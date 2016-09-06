package com.bitdubai.fermat_wpd_core.layer.middleware;

import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractLayer;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartLayerException;
import com.bitdubai.fermat_wpd_core.layer.middleware.wallet_manager.WalletManagerPluginSubsystem;

/**
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 12/11/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class MiddlewareLayer extends AbstractLayer {

    public MiddlewareLayer() {
        super(Layers.MIDDLEWARE);
    }

    public void start() throws CantStartLayerException {

        try {

//            registerPlugin(new WalletFactoryPluginSubsystem());
//            registerPlugin(new WalletPublisherPluginSubsystem());
//            registerPlugin(new WalletSettingsPluginSubsystem());
//            registerPlugin(new WalletStorePluginSubsystem());
            registerPlugin(new WalletManagerPluginSubsystem());
        } catch (Exception e) {

            throw new CantStartLayerException(
                    e,
                    "Middleware Layer of WPD Platform.",
                    "Problem trying to register a plugin."
            );
        }
    }

}
