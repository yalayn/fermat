package com.bitdubai.fermat_ccp_plugin.layer.wallet_module.fermat_wallet.developer.bitdubai.version_1;


import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractModule;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededIndirectPluginReferences;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededPluginReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.moduleManagerInterfacea;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.LocationManager;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_bch_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;


import com.bitdubai.fermat_bch_api.layer.crypto_network.manager.BlockchainManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.currency_vault.CryptoVaultManager;

import com.bitdubai.fermat_ccp_api.layer.actor.extra_user.interfaces.ExtraUserManager;
import com.bitdubai.fermat_ccp_api.layer.actor.intra_user.interfaces.IntraWalletUserActorManager;

import com.bitdubai.fermat_ccp_api.layer.basic_wallet.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_extra_user.OutgoingExtraUserManager;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.outgoing_intra_actor.interfaces.OutgoingIntraActorManager;
import com.bitdubai.fermat_ccp_api.layer.crypto_transaction.transfer_intra_wallet_users.interfaces.TransferIntraWalletUsersManager;
import com.bitdubai.fermat_ccp_api.layer.identity.intra_user.interfaces.IntraWalletUserIdentityManager;
import com.bitdubai.fermat_ccp_api.layer.middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.interfaces.CryptoAddressesManager;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_transmission.interfaces.CryptoTransmissionNetworkServiceManager;
import com.bitdubai.fermat_ccp_api.layer.request.crypto_payment.interfaces.CryptoPaymentManager;


import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantGetCryptoWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.FermatWalletSettings;

import com.bitdubai.fermat_ccp_plugin.layer.wallet_module.fermat_wallet.developer.bitdubai.version_1.structure.FermatWalletWalletModuleManager;
import com.bitdubai.fermat_cer_api.layer.search.interfaces.CurrencyExchangeProviderFilterManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;


import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.WalletManagerManager;


import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by loui on 27/05/15.a
 */
@NeededIndirectPluginReferences(indirectReferences = {
        @NeededPluginReference(platform = Platforms.BLOCKCHAINS             , layer = Layers.MIDDLEWARE , plugin = Plugins.CRYPTO_ADDRESSES   ),
        @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION, plugin = Plugins.INCOMING_EXTRA_USER),
        @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION, plugin = Plugins.INCOMING_INTRA_USER)
})

@PluginInfo(createdBy = "Leon Acosta", maintainerMail = "nattyco@gmail.com", platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.WALLET_MODULE, plugin = Plugins.CRYPTO_FERMAT_WALLET)

public class FermatWalletFermatModulePluginRoot extends AbstractModule<FermatWalletSettings, ActiveActorIdentityInformation> implements
        LogManagerForDevelopers {



    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.BASIC_WALLET    , plugin = Plugins.BITCOIN_WALLET)
    private CryptoWalletManager cryptoWalletManager;


   //@NeededPluginReference(platform = Platforms.BLOCKCHAINS             , layer = Layers.CRYPTO_VAULT    , plugin = Plugins.FERMAT_VAULT)
    private CryptoVaultManager cryptoVaultManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API     , layer = Layers.SYSTEM          , addon = Addons.LOG_MANAGER)
    private LogManager logManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.ACTOR           , plugin = Plugins.EXTRA_WALLET_USER)
    private ExtraUserManager extraUserManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.REQUEST         , plugin = Plugins.CRYPTO_PAYMENT_REQUEST)
    private CryptoPaymentManager cryptoPaymentManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION     , plugin = Plugins.OUTGOING_INTRA_ACTOR)
    private OutgoingIntraActorManager outgoingIntraActorManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.NETWORK_SERVICE , plugin = Plugins.CRYPTO_ADDRESSES)
    private CryptoAddressesManager cryptoAddressesNSManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.ACTOR           , plugin = Plugins.INTRA_WALLET_USER)
    private IntraWalletUserActorManager intraWalletUserActorManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.IDENTITY        , plugin = Plugins.INTRA_WALLET_USER)
    private IntraWalletUserIdentityManager intraWalletUserIdentityManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION     , plugin = Plugins.OUTGOING_EXTRA_USER)
    private OutgoingExtraUserManager outgoingExtraUserManager;

    @NeededPluginReference(platform = Platforms.BLOCKCHAINS             , layer = Layers.CRYPTO_MODULE   , plugin = Plugins.CRYPTO_ADDRESS_BOOK)
    private CryptoAddressBookManager cryptoAddressBookManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.MIDDLEWARE      , plugin = Plugins.WALLET_CONTACTS)
    private WalletContactsManager walletContactsManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.NETWORK_SERVICE , plugin = Plugins.CRYPTO_TRANSMISSION)
    private CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.MIDDLEWARE, plugin = Plugins.WALLET_MANAGER)
    WalletManagerManager walletManagerManager;

    @NeededPluginReference(platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.TRANSACTION    , plugin = Plugins.TRANSFER_INTRA_WALLET)
    private TransferIntraWalletUsersManager transferIntraWalletUsersManager;

    @NeededPluginReference(platform = Platforms.CURRENCY_EXCHANGE_RATE_PLATFORM, layer = Layers.SEARCH, plugin = Plugins.FILTER)
    private CurrencyExchangeProviderFilterManager exchangeProviderFilterManagerproviderFilter;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.DEVICE_LOCATION)
    private LocationManager locationManager;

    private String appPublicKey;

//    @NeededPluginReference(platform = Platforms.BLOCKCHAINS         , layer = Layers.CRYPTO_NETWORK  , plugin = Plugins.FERMAT_NETWORK       )
    private BlockchainManager<ECKey, Transaction> fermatNetworkManager;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_BROADCASTER_SYSTEM)
    private Broadcaster broadcaster;



    private static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    public FermatWalletFermatModulePluginRoot() {
        super(new PluginVersionReference(new Version()));

    }

    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<>();
        returnedClasses.add("FermatWalletCryptoModulePluginRoot");
        returnedClasses.add("FermatWalletWalletModuleManager");

        /**
         * I return the values.
         */
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /**
         * I will check the current values and update the LogLevel in those which is different
         */

        for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
            /**
             * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
             */
            if (FermatWalletFermatModulePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                FermatWalletFermatModulePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                FermatWalletFermatModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            } else {
                FermatWalletFermatModulePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
            }
        }
    }

    /**
     * Static method to get the logging level from any class under root.
     * @param className
     * @return
     */
    public static LogLevel getLogLevelByClass(String className){
        try{
            /**
             * sometimes the classname may be passed dinamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split(Pattern.quote("$"));
            return FermatWalletFermatModulePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e){
            /**
             * If I couldn't get the correct loggin level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }


    //TODO: ordenar esto please
    FermatWalletWalletModuleManager walletModuleCryptoWallet = null;

    @Override
    @moduleManagerInterfacea(moduleManager = FermatWalletWalletModuleManager.class)
    public ModuleManager<FermatWalletSettings, ActiveActorIdentityInformation> getModuleManager() throws CantGetModuleManagerException{
        try {

           // logManager.log(FermatWalletFermatModulePluginRoot.getLogLevelByClass(this.getClass().getName()), "FermatWallet instantiation started...", null, null);

            if(walletModuleCryptoWallet == null) {
                walletModuleCryptoWallet = new FermatWalletWalletModuleManager(
                        cryptoWalletManager,
                        cryptoAddressBookManager,
                        cryptoAddressesNSManager,
                        cryptoPaymentManager,
                        cryptoVaultManager,
                        getErrorManager(),
                        extraUserManager,
                        intraWalletUserActorManager,
                        intraWalletUserIdentityManager,
                        outgoingExtraUserManager,
                        outgoingIntraActorManager,
                        walletContactsManager,
                        pluginId,
                        pluginFileSystem,
                        eventManager,
                        fermatNetworkManager,
                        broadcaster,
                        exchangeProviderFilterManagerproviderFilter,
                        walletManagerManager,
                        transferIntraWalletUsersManager,
                        locationManager);

                walletModuleCryptoWallet.initialize();

            }

            logManager.log(FermatWalletFermatModulePluginRoot.getLogLevelByClass(this.getClass().getName()), "FermatWallet instantiation finished successfully.", null, null);

            return walletModuleCryptoWallet;
        } catch (final Exception e) {
            throw new CantGetModuleManagerException(CantGetCryptoWalletException.DEFAULT_MESSAGE, FermatException.wrapException(e).toString());
        }
    }
}
