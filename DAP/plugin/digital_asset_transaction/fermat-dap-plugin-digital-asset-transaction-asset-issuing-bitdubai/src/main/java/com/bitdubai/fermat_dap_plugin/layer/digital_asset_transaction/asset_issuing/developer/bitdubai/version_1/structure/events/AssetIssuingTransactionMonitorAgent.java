package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_api.layer.dmp_world.Agent;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantStartAgentException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing.intra_actor.exceptions.OutgoingIntraActorCantGetCryptoStatusException;
import com.bitdubai.fermat_ccp_api.layer.transaction.outgoing.intra_actor.interfaces.OutgoingIntraActorManager;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantSetObjectException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.interfaces.AssetIssuingTransactionNotificationAgent;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.AssetIssuingTransactionPluginRoot;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.AssetIssuingTransactionMonitorAgentMaxIterationsReachedException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantCheckAssetIssuingProgressException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantInitializeAssetIssuingMonitorAgentException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.exceptions.UnexpectedResultReturnedFromDatabaseException;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure.database.AssetIssuingTransactionDao;
import com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.structure.database.AssetIssuingTransactionDatabaseFactory;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 10/09/15.
 */
public class AssetIssuingTransactionMonitorAgent implements Agent,DealsWithLogger,DealsWithEvents,DealsWithErrors, DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {

    Database database;
    String userPublicKey;
    MonitorAgent monitorAgent;
    Thread agentThread;
    LogManager logManager;
    EventManager eventManager;
    ErrorManager errorManager;
    PluginDatabaseSystem pluginDatabaseSystem;
    UUID pluginId;
    OutgoingIntraActorManager outgoingIntraActorManager;

    public AssetIssuingTransactionMonitorAgent(EventManager eventManager,
                                               PluginDatabaseSystem pluginDatabaseSystem,
                                               ErrorManager errorManager,
                                               UUID pluginId,
                                               String userPublicKey,
                                               OutgoingIntraActorManager outgoingIntraActorManager) throws CantSetObjectException {
        this.eventManager = eventManager;
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.errorManager = errorManager;
        this.pluginId = pluginId;
        this.userPublicKey = userPublicKey;
        setOutgoingIntraActorManager(outgoingIntraActorManager);
    }

    private void setOutgoingIntraActorManager(OutgoingIntraActorManager outgoingIntraActorManager)throws CantSetObjectException{
        if(outgoingIntraActorManager==null){
            throw new CantSetObjectException("outgoingIntraActorManager is null");
        }
        this.outgoingIntraActorManager=outgoingIntraActorManager;
    }

    @Override
    public void start() throws CantStartAgentException {

        monitorAgent = new MonitorAgent();

        ((DealsWithPluginDatabaseSystem) this.monitorAgent).setPluginDatabaseSystem(this.pluginDatabaseSystem);
        ((DealsWithErrors) this.monitorAgent).setErrorManager(this.errorManager);

        try {
            ((MonitorAgent) this.monitorAgent).Initialize();
        } catch (CantInitializeAssetIssuingMonitorAgentException exception) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
        }

        this.agentThread = new Thread(monitorAgent);
        this.agentThread.start();

    }

    @Override
    public void stop() {
        this.agentThread.interrupt();
    }

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager=errorManager;
    }

    @Override
    public void setEventManager(EventManager eventManager) {
        this.eventManager=eventManager;
    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager=logManager;
    }

    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem=pluginDatabaseSystem;
    }

    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId=pluginId;
    }

    /**
     * Private class which implements runnable and is started by the Agent
     * Based on MonitorAgent created by Rodrigo Acosta
     */
    private class MonitorAgent implements AssetIssuingTransactionNotificationAgent, DealsWithPluginDatabaseSystem, DealsWithErrors, Runnable{

        ErrorManager errorManager;
        PluginDatabaseSystem pluginDatabaseSystem;
        public final int SLEEP_TIME = AssetIssuingTransactionNotificationAgent.AGENT_SLEEP_TIME;
        int iterationNumber = 0;
        AssetIssuingTransactionDao assetIssuingTransactionDao;
        @Override
        public void setErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
            this.pluginDatabaseSystem = pluginDatabaseSystem;
        }
//Todo: implement this methods
        @Override
        public void run() {

            logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Asset Issuing Transaction Protocol Notification Agent: running...", null, null);
            while(true){
                /**
                 * Increase the iteration counter
                 */
                iterationNumber++;
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    return;
                }

                /**
                 * now I will check if there are pending transactions to raise the event
                 */
                try {

                    logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Iteration number " + iterationNumber, null, null);
                    doTheMainTask();
                } catch (CantCheckAssetIssuingProgressException | AssetIssuingTransactionMonitorAgentMaxIterationsReachedException | CantExecuteQueryException e) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }

            }

        }
        public void Initialize() throws CantInitializeAssetIssuingMonitorAgentException {
            try {

                database = this.pluginDatabaseSystem.openDatabase(pluginId, userPublicKey);
            }
            catch (DatabaseNotFoundException databaseNotFoundException) {
                AssetIssuingTransactionDatabaseFactory assetIssuingTransactionDatabaseFactory=new AssetIssuingTransactionDatabaseFactory(this.pluginDatabaseSystem);
                try {
                    database = assetIssuingTransactionDatabaseFactory.createDatabase(pluginId, userPublicKey);
                } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                    errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN,cantCreateDatabaseException);
                    throw new CantInitializeAssetIssuingMonitorAgentException(cantCreateDatabaseException,"Initialize Monitor Agent - trying to create the plugin database","Please, check the cause");
                }
            } catch (CantOpenDatabaseException exception) {
                errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_ASSET_ISSUING_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
                throw new CantInitializeAssetIssuingMonitorAgentException(exception,"Initialize Monitor Agent - trying to open the plugin database","Please, check the cause");
            }
        }

        private void doTheMainTask() throws CantCheckAssetIssuingProgressException, CantExecuteQueryException, AssetIssuingTransactionMonitorAgentMaxIterationsReachedException {

            boolean found = false;
            try {
                assetIssuingTransactionDao=new AssetIssuingTransactionDao(pluginDatabaseSystem,pluginId);
/**
 * If I found transactions on Crypto_Statuts  ON_CryptoNetwork and Protocol_Status PENDING_NOTIFIED, lanzo el evento
 */

                List<String> transactionHashList;
                CryptoStatus transactionCryptoStatus;
                if (isTransactionToBeNotified(CryptoStatus.ON_CRYPTO_NETWORK)){
                    found = true;
                    //FermatEvent event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK);
                    //event.setSource(EventSource.CRYPTO_VAULT);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Found transactions pending to be notified in ON_CRYPTO_NETWORK Status! Raising INCOMING_CRYPTO_ON_CRYPTO_NETWORK event.", null, null);
                    //eventManager.raiseEvent(event);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "No other plugin is consuming Asset Issuing transactions.", "Asset Issuing Transaction monitor Agent: iteration number " + this.iterationNumber+ " without other plugins consuming transaction.",null);
                    transactionHashList=assetIssuingTransactionDao.getTransactionsHashByCryptoStatus(CryptoStatus.ON_CRYPTO_NETWORK);
                    for(String transactionHash: transactionHashList){
                        transactionCryptoStatus=getCryptoStatusFromOutgoingIntraActorPlugin(transactionHash);
                        assetIssuingTransactionDao.updateDigitalAssetCryptoStatusByTransactionHash(transactionHash, transactionCryptoStatus);
                    }
                    if (ITERATIONS_THRESHOLD < this.iterationNumber){
                        throw new AssetIssuingTransactionMonitorAgentMaxIterationsReachedException("The max limit configured for the Transaction Protocol Agent has been reached. Iteration Limit: " + ITERATIONS_THRESHOLD + "Please, notify developer.");
                    }
                }

                if (isTransactionToBeNotified(CryptoStatus.ON_BLOCKCHAIN)){
                    found = true;
                    //FermatEvent event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_BLOCKCHAIN);
                    //event.setSource(EventSource.CRYPTO_VAULT);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Found transactions pending to be notified in ON_BLOCKCHAIN Status! Raising INCOMING_CRYPTO_ON_BLOCKCHAIN event.", null, null);
                    //eventManager.raiseEvent(event);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "No other plugin is consuming Asset Issuing transactions.", "Asset Issuing Transaction monitor Agent: iteration number " + this.iterationNumber+ " without other plugins consuming transaction.",null);
                    transactionHashList=assetIssuingTransactionDao.getTransactionsHashByCryptoStatus(CryptoStatus.ON_BLOCKCHAIN);
                    for(String transactionHash: transactionHashList){
                        transactionCryptoStatus=getCryptoStatusFromOutgoingIntraActorPlugin(transactionHash);
                        assetIssuingTransactionDao.updateDigitalAssetCryptoStatusByTransactionHash(transactionHash, transactionCryptoStatus);
                    }
                    if (ITERATIONS_THRESHOLD < this.iterationNumber){
                        throw new AssetIssuingTransactionMonitorAgentMaxIterationsReachedException("The max limit configured for the Transaction Protocol Agent has been reached. Iteration Limit: " + ITERATIONS_THRESHOLD + "Please, notify developer.");
                    }
                }

                if (isTransactionToBeNotified(CryptoStatus.REVERSED_ON_CRYPTO_NETWORK)){
                    found = true;
                    //FermatEvent event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK);
                    //event.setSource(EventSource.CRYPTO_VAULT);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Found transactions pending to be notified in REVERSED_ON_CRYPTO_NETWORK Status! Raising INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK event.", null, null);
                    //eventManager.raiseEvent(event);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "No other plugin is consuming Asset Issuing transactions.", "Asset Issuing Transaction monitor Agent: iteration number " + this.iterationNumber+ " without other plugins consuming transaction.",null);
                    transactionHashList=assetIssuingTransactionDao.getTransactionsHashByCryptoStatus(CryptoStatus.REVERSED_ON_CRYPTO_NETWORK);
                    for(String transactionHash: transactionHashList){
                        transactionCryptoStatus=getCryptoStatusFromOutgoingIntraActorPlugin(transactionHash);
                        assetIssuingTransactionDao.updateDigitalAssetCryptoStatusByTransactionHash(transactionHash, transactionCryptoStatus);
                    }
                    if (ITERATIONS_THRESHOLD < this.iterationNumber){
                        throw new AssetIssuingTransactionMonitorAgentMaxIterationsReachedException("The max limit configured for the Transaction Protocol Agent has been reached. Iteration Limit: " + ITERATIONS_THRESHOLD + "Please, notify developer.");
                    }
                }

                if (isTransactionToBeNotified(CryptoStatus.REVERSED_ON_BLOCKCHAIN)){
                    found = true;
                    //FermatEvent event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN);
                    //event.setSource(EventSource.CRYPTO_VAULT);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "Found transactions pending to be notified in REVERSED_ON_BLOCKCHAIN Status! Raising INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN event.", null, null);
                    //eventManager.raiseEvent(event);


                    //logManager.log(AssetIssuingTransactionPluginRoot.getLogLevelByClass(this.getClass().getName()), "No other plugin is consuming Asset Issuing transactions.", "Asset Issuing Transaction monitor Agent: iteration number " + this.iterationNumber+ " without other plugins consuming transaction.",null);
                    transactionHashList=assetIssuingTransactionDao.getTransactionsHashByCryptoStatus(CryptoStatus.REVERSED_ON_BLOCKCHAIN);
                    for(String transactionHash: transactionHashList){
                        transactionCryptoStatus=getCryptoStatusFromOutgoingIntraActorPlugin(transactionHash);
                        assetIssuingTransactionDao.updateDigitalAssetCryptoStatusByTransactionHash(transactionHash, transactionCryptoStatus);
                    }
                    if (ITERATIONS_THRESHOLD < this.iterationNumber){
                        throw new AssetIssuingTransactionMonitorAgentMaxIterationsReachedException("The max limit configured for the Transaction Protocol Agent has been reached. Iteration Limit: " + ITERATIONS_THRESHOLD + "Please, notify developer.");
                    }
                    //TODO: case IRREVERSIBLE
                }

                if (!found){
                    assetIssuingTransactionDao.updateTransactionProtocolStatus(false);
                    this.iterationNumber = 0;
                } else {
                    this.iterationNumber = assetIssuingTransactionDao.updateTransactionProtocolStatus(true);
                }


            } catch (CantExecuteDatabaseOperationException exception) {
                throw new CantExecuteQueryException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE, exception, "Exception in asset Issuing monitor agent","Cannot execute database operation");
            } catch (OutgoingIntraActorCantGetCryptoStatusException exception) {
                throw new CantCheckAssetIssuingProgressException(exception,"Exception in asset Issuing monitor agent","Exception in OutgoingIntraActor plugin");
            } catch (UnexpectedResultReturnedFromDatabaseException exception) {
                throw new CantCheckAssetIssuingProgressException(exception,"Exception in asset Issuing monitor agent","Unexpected result in database query");
            }

        }

        private boolean isTransactionToBeNotified(CryptoStatus cryptoStatus) throws CantExecuteQueryException {
            boolean isPending =assetIssuingTransactionDao.isPendingTransactions(cryptoStatus);
            return isPending;
        }

        private CryptoStatus getCryptoStatusFromOutgoingIntraActorPlugin(String transactionHash) throws OutgoingIntraActorCantGetCryptoStatusException {
            return outgoingIntraActorManager.getTransactionStatus(transactionHash);
        }

    }
}