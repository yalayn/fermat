package com.bitdubai.smartwallet.platform.layer._7_network_service.user;

import com.bitdubai.smartwallet.platform.layer._7_network_service.CantStartSubsystemException;
import com.bitdubai.smartwallet.platform.layer._7_network_service.NetworkService;
import com.bitdubai.smartwallet.platform.layer._7_network_service.NetworkSubsystem;
import com.bitdubai.smartwallet.platform.layer._7_network_service.user.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by ciencias on 20.01.15.
 */
public class UserSubsystem implements NetworkSubsystem{

    NetworkService mNetworkService;

    @Override
    public NetworkService getNetworkService() {
        return mNetworkService;
    }

    @Override
    public void start() throws CantStartSubsystemException {
        /**
         * I will choose from the different Developers available which implementation to use. Right now there is only
         * one, so it is not difficult to choose.
         */

        try {
            DeveloperBitDubai developerBitDubai = new DeveloperBitDubai();
            mNetworkService = developerBitDubai.getNetworkService();
        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException();
        }
    }

}
