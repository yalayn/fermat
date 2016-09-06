package com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.core.PluginInfo;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_ccp_api.all_definition.enums.EventType;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.enums.RequestAction;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.enums.RequestProtocolState;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.enums.RequestType;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantConfirmRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantGetRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantInformApprovalException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantInformDenialException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantInformReceptionException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantInformRefusalException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantListPendingRequestsException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantReceiveInformationMessageException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantReceiveRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.CantSendRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.exceptions.RequestNotFoundException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.interfaces.CryptoPaymentRequest;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.interfaces.CryptoPaymentRequestManager;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database.CryptoPaymentRequestNetworkServiceDao;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database.CryptoPaymentRequestNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database.CryptoPaymentRequestNetworkServiceDatabaseFactory;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database.CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantChangeRequestProtocolStateException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantCreateCryptoPaymentRequestException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantListRequestsException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantTakeActionException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.messages.InformationMessage;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.messages.NetworkServiceMessage;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.messages.RequestMessage;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.structure.PaymentConstants;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Joaquin Carrasquero on 15/02/16,email: jc.juaco@gmail.com.
 */

@PluginInfo(createdBy = "Joaquin Carrasquero", maintainerMail = "nattyco@gmail.com", platform = Platforms.CRYPTO_CURRENCY_PLATFORM, layer = Layers.NETWORK_SERVICE, plugin = Plugins.CRYPTO_PAYMENT_REQUEST)

public class CryptoPaymentRequestNetworkServicePluginRootNew extends AbstractNetworkService implements
        CryptoPaymentRequestManager,
        DatabaseManagerForDevelopers {

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM          , addon = Addons.PLUGIN_DATABASE_SYSTEM)
    protected PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    /**
     * Represent the dataBase
     */
    private Database dataBase;


    /**
     * Developer DB
     */

    CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory cryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory;

    /**
     * Executor
     */
    ExecutorService executorService;


    /**
     * Represents  CryptoPaymentRequestNetworkServiceDao
     */

    private CryptoPaymentRequestNetworkServiceDao cryptoPaymentRequestNetworkServiceDao;

    private long reprocessTimer = 300000; //five minutes
    private Timer timer = new Timer();


    /**
     * cache identities to register
     */

    private List<PlatformComponentProfile> actorsToRegisterCache;

    /**
     * Constructor
     */
    public CryptoPaymentRequestNetworkServicePluginRootNew() {
        super(
                new PluginVersionReference(new Version()),
                EventSource.NETWORK_SERVICE_CRYPTO_PAYMENT_REQUEST,
                NetworkServiceType.CRYPTO_PAYMENT_REQUEST
        );
    }


    @Override
    protected void onNetworkServiceStart() throws CantStartPluginException {


        /**
         * Initialize DB
         */

        try {
            initializeCommunicationDb();
        } catch (CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException e) {
            e.printStackTrace();
        }


        /**
         * Initialize Developer Database Factory
         */


        cryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory = new CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
        try {
            cryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory.initializeDatabase();
        } catch (CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException e) {
            e.printStackTrace();
        }


        /**
         * Initialize Dao
         */

        cryptoPaymentRequestNetworkServiceDao = new CryptoPaymentRequestNetworkServiceDao(pluginDatabaseSystem, pluginId);

        try {
            cryptoPaymentRequestNetworkServiceDao.initialize();
        } catch (CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException e) {
            e.printStackTrace();
        }


        executorService = Executors.newFixedThreadPool(1);

        // change message state to process again first time
        reprocessPendingMessage();

        //declare a schedule to process waiting request message
        this.startTimer();
    }

    /**
     * This method initialize the database
     *
     * @throws CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException
     */
    private void initializeCommunicationDb() throws CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException {

        try {

            this.dataBase = this.pluginDatabaseSystem.openDatabase(pluginId, CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_TABLE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
            throw new CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException(cantOpenDatabaseException);

        } catch (DatabaseNotFoundException e) {

            CryptoPaymentRequestNetworkServiceDatabaseFactory communicationLayerNetworkServiceDatabaseFactory = new CryptoPaymentRequestNetworkServiceDatabaseFactory(pluginDatabaseSystem);

            try {

                this.dataBase = communicationLayerNetworkServiceDatabaseFactory.createDatabase(pluginId, CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_TABLE_NAME);

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {

                errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, cantCreateDatabaseException);
                throw new CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException(cantCreateDatabaseException);

            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        executorService.shutdownNow();
    }

    @Override
    public void onNewMessageReceived(com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.entities.NetworkServiceMessage newFermatMessageReceive) {
        try {

            final Gson gson = new Gson();

            final String jsonMessage = newFermatMessageReceive.getContent();

            final NetworkServiceMessage networkServiceMessage = gson.fromJson(jsonMessage, NetworkServiceMessage.class);

            switch (networkServiceMessage.getMessageType()) {
                case INFORMATION:
                    // update the request to processing receive state with the given action. Set Message DONE
                    final InformationMessage informationMessage = gson.fromJson(jsonMessage, InformationMessage.class);
                    receiveInformationMessage(informationMessage);

                    System.out.println(" CPR NS - Information Message Received: " + informationMessage.toString());
                    break;

                case REQUEST:
                    // create the request in processing receive state.
                    final RequestMessage requestMessage = gson.fromJson(jsonMessage, RequestMessage.class);
                    receiveCryptoPaymentRequest(requestMessage);
                    System.out.println(" CPR NS - Request Message Received: " + requestMessage.toString());
                    break;
            }


            raiseEvents();


        } catch (Exception e) {
            reportUnexpectedException(e);
        }
    }

    private void reportUnexpectedException(final Exception e) {
        this.errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
    }


    private void receiveInformationMessage(InformationMessage informationMessage) throws CantReceiveInformationMessageException {
        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    informationMessage.getRequestId(),
                    informationMessage.getAction(),
                    RequestProtocolState.PENDING_ACTION
            );


        } catch (CantTakeActionException |
                RequestNotFoundException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantReceiveInformationMessageException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantReceiveInformationMessageException(e, "", "Unhandled Exception.");
        }
    }


    /**
     * I indicate to the Agent the action that it must take:
     * - Protocol State: PROCESSING_RECEIVE.
     * - Action        : REQUEST           .
     */
    private void receiveCryptoPaymentRequest(RequestMessage requestMessage) throws CantReceiveRequestException {
        try {

            RequestProtocolState protocolState = RequestProtocolState.PENDING_ACTION;
            RequestAction action = RequestAction.REQUEST;
            RequestType direction = RequestType.RECEIVED;

            cryptoPaymentRequestNetworkServiceDao.createCryptoPaymentRequest(
                    requestMessage.getRequestId(),
                    requestMessage.getActorPublicKey(), // the actor receiving, is the identity now.
                    requestMessage.getActorType(), // the actor receiving, is the identity now.
                    requestMessage.getIdentityPublicKey(), // the identity who sent the request, is the actor now.
                    requestMessage.getIdentityType(), // the identity who sent the request, is the actor now.
                    requestMessage.getCryptoAddress(),
                    requestMessage.getDescription(),
                    requestMessage.getAmount(),
                    requestMessage.getStartTimeStamp(),
                    direction,
                    action,
                    protocolState,
                    requestMessage.getNetworkType(),
                    requestMessage.getReferenceWallet(),
                    0, PaymentConstants.OUTGOING_MESSAGE,
                    requestMessage.getWalletPublicKey(),
                    requestMessage.getCryptoCurrency()
            );

        } catch (CantCreateCryptoPaymentRequestException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantReceiveRequestException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantReceiveRequestException(e, "", "Unhandled Exception.");
        }
    }


    private void checkFailedDeliveryTime(String destinationPublicKey) {
        try {

            List<CryptoPaymentRequest> cryptoAddressRequestList = cryptoPaymentRequestNetworkServiceDao.listRequestsByActorPublicKey(destinationPublicKey);

            //if I try to send more than 5 times I put it on hold
            for (CryptoPaymentRequest record : cryptoAddressRequestList) {


                if (!record.getProtocolState().getCode().equals(RequestProtocolState.WAITING_RESPONSE.getCode())) {
                    if (record.getSentNumber() > 10) {
                        // if(record.getSentNumber() > 20)
                        //{
                        //reprocess at two hours
                        //  reprocessTimer =  2 * 3600 * 1000;

                        // }

                        //reprocess at five minutes
                        //update state and process again later
                        cryptoPaymentRequestNetworkServiceDao.changeProtocolState(record.getRequestId(), RequestProtocolState.WAITING_RESPONSE);
                        cryptoPaymentRequestNetworkServiceDao.changeSentNumber(record.getRequestId(), 1);

                    } else {
                        cryptoPaymentRequestNetworkServiceDao.changeSentNumber(record.getRequestId(), record.getSentNumber() + 1);
                    }
                } else {
                    //I verify the number of days I'm around trying to send if it exceeds three days I delete record

                    long sentDate = record.getStartTimeStamp();
                    long currentTime = System.currentTimeMillis();
                    long dif = currentTime - sentDate;

                    double dias = Math.floor(dif / (1000 * 60 * 60 * 24));

                    if ((int) dias > 3) {
                        //notify the user does not exist to intra user actor plugin

                        cryptoPaymentRequestNetworkServiceDao.delete(record.getRequestId());
                    }

                }

            }


        } catch (Exception e) {
            System.out.println("REQUEST PAYMENT NS EXCEPCION VERIFICANDO WAIT MESSAGE");
            e.printStackTrace();
        }

    }

    @Override
    public void onSentMessage(UUID messageSentId) {

       /* Gson gson = new Gson();

        String jsonMessage = messageSent.getContent();

        NetworkServiceMessage networkServiceMessage = gson.fromJson(jsonMessage, NetworkServiceMessage.class);

        switch (networkServiceMessage.getMessageType()) {
            case INFORMATION:
                InformationMessage informationMessage = gson.fromJson(jsonMessage, InformationMessage.class);
                try {
                    confirmRequest(informationMessage.getRequestId());
                } catch (CantConfirmRequestException e) {
                    e.printStackTrace();
                } catch (RequestNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST:
                RequestMessage requestMessage = gson.fromJson(jsonMessage, RequestMessage.class);
                break;

            default:
                try {
                    throw new CantHandleNewMessagesException(
                            "message type: " + networkServiceMessage.getMessageType().name(),
                            "Message type not handled."
                    );
                } catch (CantHandleNewMessagesException e1) {
                    e1.printStackTrace();
                }
        }*/
    }


    private void reprocessPendingMessage() {
        try {

            List<CryptoPaymentRequest> cryptoAddressRequestList = cryptoPaymentRequestNetworkServiceDao.listUncompletedRequest();

            for (CryptoPaymentRequest cryptoPaymentRequest : cryptoAddressRequestList) {

                cryptoPaymentRequestNetworkServiceDao.changeProtocolState(cryptoPaymentRequest.getRequestId(), RequestProtocolState.PROCESSING_SEND);

                String json;
                if (cryptoPaymentRequest.getAction().getCode().equals(RequestAction.REQUEST.getCode())) {
                    json = buildJsonRequestMessage(cryptoPaymentRequest);
                } else {
                    json = buildJsonInformationMessage(cryptoPaymentRequest);
                }

                sendMessage(
                        cryptoPaymentRequest.getIdentityType(),
                        cryptoPaymentRequest.getIdentityPublicKey(),
                        cryptoPaymentRequest.getActorType(),
                        cryptoPaymentRequest.getActorPublicKey(),
                        json
                );

            }
        } catch (CantListRequestsException | CantChangeRequestProtocolStateException | RequestNotFoundException e) {
            System.out.println("Payment Request NS EXCEPCION REPROCESANDO WAIT MESSAGE");
            e.printStackTrace();
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Protocol State: PROCESSING_SEND.
     * - Action        : REQUEST.
     * - Type          : SENT.
     */
    @Override
    public void sendCryptoPaymentRequest(UUID requestId, String identityPublicKey, Actors identityType, String actorPublicKey, Actors actorType,
                                         CryptoAddress cryptoAddress, String description,
                                         long amount, long startTimeStamp,
                                         BlockchainNetworkType networkType,
                                         ReferenceWallet referenceWallet, String walletPublicKey,
                                         CryptoCurrency cryptoCurrency) throws CantSendRequestException {

        System.out.println("********** Crypto Payment Request NS -> sending request. PROCESSING_SEND - REQUEST - SENT.");

        try {

            RequestProtocolState protocolState = RequestProtocolState.PROCESSING_SEND;
            RequestAction action = RequestAction.REQUEST;
            RequestType direction = RequestType.SENT;

            cryptoPaymentRequestNetworkServiceDao.createCryptoPaymentRequest(
                    requestId,
                    identityPublicKey,
                    identityType,
                    actorPublicKey,
                    actorType,
                    cryptoAddress,
                    description,
                    amount,
                    startTimeStamp,
                    direction,
                    action,
                    protocolState,
                    networkType,
                    referenceWallet,
                    0, PaymentConstants.OUTGOING_MESSAGE,
                    walletPublicKey,
                    cryptoCurrency
            );


            final CryptoPaymentRequest cryptoPaymentRequest = cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

            sendMessage(
                    cryptoPaymentRequest.getIdentityType(),
                    cryptoPaymentRequest.getIdentityPublicKey(),
                    cryptoPaymentRequest.getActorType(),
                    cryptoPaymentRequest.getActorPublicKey(),
                    buildJsonRequestMessage(cryptoPaymentRequest)
            );

            toWaitingResponse(requestId);

            System.out.println("********** Crypto Payment Request NS -> sending request. PROCESSING_SEND - REQUEST - SENT - OK.");

        } catch (CantCreateCryptoPaymentRequestException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantSendRequestException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantSendRequestException(e, "", "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Action        : INFORM_REFUSAL.
     * - Protocol State: PROCESSING_SEND.
     */
    @Override
    public void informRefusal(UUID requestId) throws RequestNotFoundException, CantInformRefusalException {


        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    requestId,
                    RequestAction.INFORM_REFUSAL,
                    RequestProtocolState.PROCESSING_SEND
            );


            final CryptoPaymentRequest cryptoPaymentRequest = cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

            sendMessage(
                    cryptoPaymentRequest.getIdentityType(),
                    cryptoPaymentRequest.getIdentityPublicKey(),
                    cryptoPaymentRequest.getActorType(),
                    cryptoPaymentRequest.getActorPublicKey(),
                    buildJsonInformationMessage(cryptoPaymentRequest)
            );

        } catch (CantTakeActionException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantInformRefusalException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (RequestNotFoundException e) {

            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantInformRefusalException(e, "", "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Action        : INFORM_DENIAL.
     * - Protocol State: PROCESSING_SEND.
     */
    @Override
    public void informDenial(UUID requestId) throws RequestNotFoundException, CantInformDenialException {

        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    requestId,
                    RequestAction.INFORM_DENIAL,
                    RequestProtocolState.PROCESSING_SEND
            );

            final CryptoPaymentRequest cryptoPaymentRequest = cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

            sendMessage(
                    cryptoPaymentRequest.getIdentityType(),
                    cryptoPaymentRequest.getIdentityPublicKey(),
                    cryptoPaymentRequest.getActorType(),
                    cryptoPaymentRequest.getActorPublicKey(),
                    buildJsonInformationMessage(cryptoPaymentRequest)
            );


        } catch (CantTakeActionException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantInformDenialException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (RequestNotFoundException e) {

            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantInformDenialException(e, "", "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Protocol State: PROCESSING_SEND.
     * - Action        : INFORM_APPROVAL.
     */
    @Override
    public void informApproval(UUID requestId) throws CantInformApprovalException, RequestNotFoundException {


        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    requestId,
                    RequestAction.INFORM_APPROVAL,
                    RequestProtocolState.PROCESSING_SEND
            );

            final CryptoPaymentRequest cryptoPaymentRequest = cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

            sendMessage(
                    cryptoPaymentRequest.getIdentityType(),
                    cryptoPaymentRequest.getIdentityPublicKey(),
                    cryptoPaymentRequest.getActorType(),
                    cryptoPaymentRequest.getActorPublicKey(),
                    buildJsonInformationMessage(cryptoPaymentRequest)
            );

        } catch (CantTakeActionException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantInformApprovalException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (RequestNotFoundException e) {

            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantInformApprovalException(e, "", "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the Agent the action that it must take:
     * - Action        : INFORM_RECEPTION.
     * - Protocol State: PROCESSING_SEND.
     */
    @Override
    public void informReception(UUID requestId) throws CantInformReceptionException, RequestNotFoundException {

        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    requestId,
                    RequestAction.INFORM_RECEPTION,
                    RequestProtocolState.PROCESSING_SEND
            );

            final CryptoPaymentRequest cryptoPaymentRequest = cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

            sendMessage(
                    cryptoPaymentRequest.getIdentityType(),
                    cryptoPaymentRequest.getIdentityPublicKey(),
                    cryptoPaymentRequest.getActorType(),
                    cryptoPaymentRequest.getActorPublicKey(),
                    buildJsonInformationMessage(cryptoPaymentRequest)
            );

        } catch (CantTakeActionException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantInformReceptionException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (RequestNotFoundException e) {

            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantInformReceptionException(e, "", "Unhandled Exception.");
        }
    }

    /**
     * I indicate to the NS that no more action is needed for the given request:
     * - Action        : NONE.
     * - Protocol State: DONE.
     */
    @Override
    public void confirmRequest(UUID requestId) throws CantConfirmRequestException, RequestNotFoundException {
        try {

            cryptoPaymentRequestNetworkServiceDao.takeAction(
                    requestId,
                    RequestAction.NONE,
                    RequestProtocolState.DONE
            );

        } catch (CantTakeActionException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantConfirmRequestException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (RequestNotFoundException e) {

            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantConfirmRequestException(e, "", "Unhandled Exception.");
        }
    }

    @Override
    public CryptoPaymentRequest getRequestById(UUID requestId) throws CantGetRequestException, RequestNotFoundException {
        try {

            return cryptoPaymentRequestNetworkServiceDao.getRequestById(requestId);

        } catch (CantGetRequestException |
                RequestNotFoundException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw e;
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantGetRequestException(e, "", "Unhandled Exception.");
        }
    }

    @Override
    public List<CryptoPaymentRequest> listPendingRequests() throws CantListPendingRequestsException {
        try {

            return cryptoPaymentRequestNetworkServiceDao.listRequestsByProtocolState(
                    RequestProtocolState.PENDING_ACTION
            );

        } catch (CantListRequestsException e) {
            // i inform to error manager the error.
            reportUnexpectedException(e);
            throw new CantListPendingRequestsException(e, "", "Error in Crypto Payment Request NS Dao.");
        } catch (Exception e) {

            reportUnexpectedException(e);
            throw new CantListPendingRequestsException(e, "", "Unhandled Exception.");
        }


    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return new CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId).getDatabaseList(developerObjectFactory);

    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
            return new CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId).getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        try {
            return new CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId).getDatabaseTableContent(developerObjectFactory, developerDatabase, developerDatabaseTable);
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    private String buildJsonInformationMessage(CryptoPaymentRequest cpr) {

        return new InformationMessage(
                cpr.getRequestId(),
                cpr.getAction(),
                cpr.getIdentityPublicKey(),
                cpr.getActorPublicKey()
        ).toJson();
    }

    private String buildJsonRequestMessage(CryptoPaymentRequest cpr) {

        return new RequestMessage(
                cpr.getRequestId(),
                cpr.getIdentityPublicKey(),
                cpr.getIdentityType(),
                cpr.getActorPublicKey(),
                cpr.getActorType(),
                cpr.getDescription(),
                cpr.getCryptoAddress(),
                cpr.getAmount(),
                cpr.getStartTimeStamp(),
                cpr.getAction(),
                cpr.getNetworkType(),
                cpr.getReferenceWallet(),
                cpr.getIdentityPublicKey(),
                cpr.getActorPublicKey(),
                cpr.getWalletPublicKey(),
                cpr.getCryptoCurrency()).toJson();
    }

    private void toWaitingResponse(final UUID requestId) throws CantChangeRequestProtocolStateException,
            RequestNotFoundException {

        cryptoPaymentRequestNetworkServiceDao.changeProtocolState(
                requestId,
                RequestProtocolState.WAITING_RESPONSE
        );
    }

    private void raiseEvents() {
        try {

            // if there are pending actions I will raise a crypto address news event.
            if (cryptoPaymentRequestNetworkServiceDao.isPendingRequestByProtocolState(RequestProtocolState.PENDING_ACTION)) {
                System.out.println("************* Crypto Payment Request -> Pending Action detected!");
                FermatEvent eventToRaise = eventManager.getNewEvent(EventType.CRYPTO_PAYMENT_REQUEST_NEWS);
                eventToRaise.setSource(this.eventSource);
                eventManager.raiseEvent(eventToRaise);
            }

        } catch (CantListRequestsException e) {

            reportUnexpectedException(e);
        }

    }

    private void startTimer() {


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // change message state to process retry later
                reprocessPendingMessage();
            }
        }, 0, reprocessTimer);

    }

    public void sendMessage(final Actors senderActorType,
                            final String senderPublicKey,
                            final Actors receiverActorType,
                            final String receiverPublicKey,
                            final String contentMessage) {

        final ActorProfile sender = new ActorProfile();
        sender.setActorType(senderActorType.getCode());
        sender.setIdentityPublicKey(senderPublicKey);

        final ActorProfile receiver = new ActorProfile();
        receiver.setActorType(receiverActorType.getCode());
        receiver.setIdentityPublicKey(receiverPublicKey);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    sendNewMessage(
                            sender,
                            receiver,
                            contentMessage,true
                    );
                } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.network_services.exceptions.CantSendMessageException e) {
                    reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
                }
            }
        });
    }

}
