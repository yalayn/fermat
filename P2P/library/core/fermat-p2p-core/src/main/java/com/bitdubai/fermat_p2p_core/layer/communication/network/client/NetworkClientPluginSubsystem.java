package com.bitdubai.fermat_p2p_core.layer.communication.network.client;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;
import org.iop.client.DeveloperBitDubai;

/**
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 05/11/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NetworkClientPluginSubsystem extends AbstractPluginSubsystem {

    public NetworkClientPluginSubsystem() {
        super(new PluginReference(Plugins.NETWORK_CLIENT));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
