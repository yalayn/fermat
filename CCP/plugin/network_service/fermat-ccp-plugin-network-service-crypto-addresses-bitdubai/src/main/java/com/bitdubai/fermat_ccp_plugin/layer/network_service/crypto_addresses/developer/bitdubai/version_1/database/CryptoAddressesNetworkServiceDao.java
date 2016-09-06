package com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.database;

/**
 * The class <code>com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.database.CryptoAddressesNetworkServiceDao</code>
 * contains all the methods that interact with the database.
 * Manages the addresses exchange requests by storing them on a Database Table.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 24/09/2015.
 *
 * @version 1.0
 */
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantDeleteRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.enums.CryptoAddressDealers;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.enums.ProtocolState;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.enums.RequestAction;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.enums.RequestType;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.CantAcceptAddressExchangeRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.CantConfirmAddressExchangeRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.CantDenyAddressExchangeRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.CantGetPendingAddressExchangeRequestException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.CantListPendingCryptoAddressRequestsException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.exceptions.PendingRequestNotFoundException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_addresses.interfaces.CryptoAddressRequest;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.exceptions.CantChangeProtocolStateException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.exceptions.CantCreateRequestException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.exceptions.CantInitializeCryptoAddressesNetworkServiceDatabaseException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.structure.AddressesConstants;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_addresses.developer.bitdubai.version_1.structure.CryptoAddressesNetworkServiceCryptoAddressRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CryptoAddressesNetworkServiceDao {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID                 pluginId            ;

    private Database database;

    public CryptoAddressesNetworkServiceDao(final PluginDatabaseSystem pluginDatabaseSystem,
                                            final UUID                 pluginId            ) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId             = pluginId            ;
    }

    public void initialize() throws CantInitializeCryptoAddressesNetworkServiceDatabaseException {
        try {

            database = this.pluginDatabaseSystem.openDatabase(
                    this.pluginId,
                    this.pluginId.toString()
            );

        } catch (DatabaseNotFoundException e) {

            try {

                CryptoAddressesNetworkServiceDatabaseFactory databaseFactory = new CryptoAddressesNetworkServiceDatabaseFactory(pluginDatabaseSystem);
                database = databaseFactory.createDatabase(
                        pluginId,
                        pluginId.toString()
                );

            } catch (CantCreateDatabaseException f) {

                throw new CantInitializeCryptoAddressesNetworkServiceDatabaseException(CantCreateDatabaseException.DEFAULT_MESSAGE, f, "", "There is a problem and i cannot create the database.");
            } catch (Exception z) {

                throw new CantInitializeCryptoAddressesNetworkServiceDatabaseException(CantInitializeCryptoAddressesNetworkServiceDatabaseException.DEFAULT_MESSAGE, z, "", "Generic Exception.");
            }

        } catch (CantOpenDatabaseException e) {

            throw new CantInitializeCryptoAddressesNetworkServiceDatabaseException(CantOpenDatabaseException.DEFAULT_MESSAGE, e, "", "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (Exception e) {

            throw new CantInitializeCryptoAddressesNetworkServiceDatabaseException(CantInitializeCryptoAddressesNetworkServiceDatabaseException.DEFAULT_MESSAGE, e, "", "Generic Exception.");
        }
    }


    public void createAddressExchangeRequest(UUID                  id                         ,
                                             String                walletPublicKey            ,
                                             CryptoCurrency        cryptoCurrency             ,
                                             Actors                identityTypeRequesting     ,
                                             Actors                identityTypeAccepting      ,
                                             String                identityPublicKeyRequesting,
                                             String                identityPublicKeyAccepting ,
                                             ProtocolState         protocolState              ,
                                             RequestType           requestType                ,
                                             RequestAction         requestAction              ,
                                             CryptoAddressDealers  dealer                     ,
                                             BlockchainNetworkType blockchainNetworkType      ,
                                             int                    sentNumber,
                                             long sentDate,
                                             String messageType,
                                             boolean readMark) throws CantCreateRequestException {

        try {

            if(!existPendingRequest(id)) {
                CryptoAddressesNetworkServiceCryptoAddressRequest addressExchangeRequest = new CryptoAddressesNetworkServiceCryptoAddressRequest(
                        id                      ,
                        walletPublicKey            ,
                        identityTypeRequesting     ,
                        identityTypeAccepting      ,
                        identityPublicKeyRequesting,
                        identityPublicKeyAccepting ,
                        cryptoCurrency             ,
                        null                       ,
                        protocolState              ,
                        requestType                ,
                        requestAction              ,
                        dealer                     ,
                        blockchainNetworkType,
                        sentNumber,
                        sentDate,
                        messageType,
                        readMark
                );

                DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

                DatabaseTableRecord entityRecord = addressExchangeRequestTable.getEmptyRecord();

                entityRecord = buildDatabaseRecord(entityRecord, addressExchangeRequest);

                addressExchangeRequestTable.insertRecord(entityRecord);
            }


        } catch (CantInsertRecordException e) {

            throw new CantCreateRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot insert the record.");

       } catch (CantGetPendingAddressExchangeRequestException e) {
           throw new CantCreateRequestException(e, "", "Exception not handled by the plugin, there is a problem in database");

        }
    }

    /**
     * we'll return to the actor all the pending requests pending a local action.
     * State : PENDING_ACTION.
     *
     * @throws CantListPendingCryptoAddressRequestsException      if something goes wrong.
     */
    public List<CryptoAddressRequest> listAllPendingRequests() throws CantListPendingCryptoAddressRequestsException {


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME,Boolean.FALSE.toString(), DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }

    /**
     * we'll return to the actor all the request in a specific protocol state
     *
     * @param protocolState  that we need.
     *
     * @throws CantListPendingCryptoAddressRequestsException      if something goes wrong.
     */
    public List<CryptoAddressRequest> listPendingRequestsByProtocolState(ProtocolState protocolState) throws CantListPendingCryptoAddressRequestsException {

        if (protocolState == null)
            throw new CantListPendingCryptoAddressRequestsException(null, "", "actorType, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, protocolState.getCode(), DatabaseFilterType.EQUAL);


            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }


    public List<CryptoAddressRequest> listUncompletedRequest(String remoteIdentityKey) throws CantListPendingCryptoAddressRequestsException {


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, ProtocolState.DONE.getCode(), DatabaseFilterType.NOT_EQUALS);
            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_TYPE_REQUESTING_COLUMN_NAME, remoteIdentityKey, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }

    public List<CryptoAddressRequest> listUncompletedRequest() throws CantListPendingCryptoAddressRequestsException {


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, ProtocolState.DONE.getCode(), DatabaseFilterType.NOT_EQUALS);
            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_MESSAGE_TYPE_COLUMN_NAME, AddressesConstants.OUTGOING_MESSAGE, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }


    public List<CryptoAddressRequest> listRequestsByActorPublicKey(String identityPublicKey) throws CantListPendingCryptoAddressRequestsException {


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_REQUESTING_COLUMN_NAME, identityPublicKey, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }


    public boolean isPendingRequestByProtocolState(ProtocolState protocolState) throws CantListPendingCryptoAddressRequestsException {

        if (protocolState == null)
            throw new CantListPendingCryptoAddressRequestsException(null, "", "actorType, can not be null");

        try {

            DatabaseTable table = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, protocolState.getCode(), DatabaseFilterType.EQUAL);

            table.setFilterTop("1");

            table.loadToMemory();

            return (!table.getRecords().isEmpty());

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    public boolean isPendingRequestByProtocolStateAndNotReadAndReceived(ProtocolState protocolState) throws CantListPendingCryptoAddressRequestsException {

        if (protocolState == null)
            throw new CantListPendingCryptoAddressRequestsException(null, "", "actorType, can not be null");

        try {

            DatabaseTable table = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, protocolState.getCode(), DatabaseFilterType.EQUAL);

            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME, Boolean.FALSE.toString(), DatabaseFilterType.EQUAL);
            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME, AddressesConstants.INCOMING_MESSAGE, DatabaseFilterType.EQUAL);

            table.setFilterTop("1");

            table.loadToMemory();

            return (!table.getRecords().isEmpty());

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    /**
     * we'll return to the actor all the request in a specific protocol state
     *
     * @param protocolState  that we need.
     *
     * @throws CantListPendingCryptoAddressRequestsException      if something goes wrong.
     */
    public List<CryptoAddressRequest> listPendingRequestsByProtocolStateAndAction(final ProtocolState protocolState      ,
                                                                                  final RequestAction requestAction) throws CantListPendingCryptoAddressRequestsException {

        if (protocolState == null)
            throw new CantListPendingCryptoAddressRequestsException(null, "", "actorType, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, protocolState.getCode(), DatabaseFilterType.EQUAL);
            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME, requestAction.getCode(), DatabaseFilterType.EQUAL);

            //addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME, RequestType.RECEIVED.getCode(), DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            List<CryptoAddressRequest> cryptoAddressRequests = new ArrayList<>();

            for (DatabaseTableRecord record : records) {
                cryptoAddressRequests.add(buildAddressExchangeRequestRecord(record));
            }

            return cryptoAddressRequests;

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Check the cause."                                                                                );
        }
    }

    public CryptoAddressRequest getPendingRequest(UUID requestId) throws Exception {

        if (requestId == null)
            throw new CantGetPendingAddressExchangeRequestException(null, "", "requestId, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();


            if (!records.isEmpty())
                return buildAddressExchangeRequestRecord(records.get(0));
            else
                throw new Exception("AddressID: "+requestId, new Throwable("Can not find an address exchange request with the given request id."));


        } catch (CantLoadTableToMemoryException exception) {

            throw new CantGetPendingAddressExchangeRequestException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException exception) {

            throw new CantGetPendingAddressExchangeRequestException(exception, "", "Check the cause."                                                                                );
        }
    }


    public Actors getActorTypeFromRequest(String identityPublicKeySender) throws CantGetPendingAddressExchangeRequestException {
        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_REQUESTING_COLUMN_NAME, identityPublicKeySender, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            if (!records.isEmpty())
                return buildAddressExchangeRequestRecord(records.get(0)).getIdentityTypeRequesting();
            else{

                addressExchangeRequestTable.clearAllFilters();
                addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_RESPONDING_COLUMN_NAME, identityPublicKeySender, DatabaseFilterType.EQUAL);
                addressExchangeRequestTable.loadToMemory();
                List<DatabaseTableRecord> records1 = addressExchangeRequestTable.getRecords();
                if (!records1.isEmpty())
                    return buildAddressExchangeRequestRecord(records1.get(0)).getIdentityTypeResponding();
                else
                    throw new CantGetPendingAddressExchangeRequestException(new Exception(), "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");


            }






        } catch (CantLoadTableToMemoryException exception) {

            throw new CantGetPendingAddressExchangeRequestException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException e) {
            throw new CantGetPendingAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");

        } catch (Exception e) {
            throw new CantGetPendingAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");

        }
    }

    public Actors getActorTypeToRequest(String identityPublicKeyDestination) throws CantGetPendingAddressExchangeRequestException {
        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_RESPONDING_COLUMN_NAME, identityPublicKeyDestination, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.loadToMemory();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();

            if (!records.isEmpty())
                return buildAddressExchangeRequestRecord(records.get(0)).getIdentityTypeResponding();
            else{

                addressExchangeRequestTable.clearAllFilters();
                addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_REQUESTING_COLUMN_NAME, identityPublicKeyDestination, DatabaseFilterType.EQUAL);
                addressExchangeRequestTable.loadToMemory();
                List<DatabaseTableRecord> records1 = addressExchangeRequestTable.getRecords();
                if (!records1.isEmpty())
                    return buildAddressExchangeRequestRecord(records1.get(0)).getIdentityTypeRequesting();
                else
                    throw new CantGetPendingAddressExchangeRequestException(new Exception(), "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");


            }
        } catch (CantLoadTableToMemoryException exception) {

            throw new CantGetPendingAddressExchangeRequestException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (InvalidParameterException e) {
            throw new CantGetPendingAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");

        } catch (Exception e) {
            throw new CantGetPendingAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");

        }
    }

    public boolean existPendingRequest(UUID requestId) throws CantGetPendingAddressExchangeRequestException {


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.numRecords();

            List<DatabaseTableRecord> records = addressExchangeRequestTable.getRecords();


            if (addressExchangeRequestTable.numRecords() > 0)
                return true;
            else
                return false;

        } catch (Exception exception) {

            throw new CantGetPendingAddressExchangeRequestException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    /**
     * when i accept a request, first, i update the request with the address that i'm returning.
     * then i indicate the ns agent to do the next action:
     * -State : @state
     * -Action: ACCEPT.
     *
     * @param requestId id of the address exchange request we want to confirm.
     *
     * @throws CantAcceptAddressExchangeRequestException      if something goes wrong.
     * @throws PendingRequestNotFoundException                if i can't find the record.
     */
    public void acceptAddressExchangeRequest(final UUID          requestId    ,
                                             final CryptoAddress cryptoAddress,
                                             final ProtocolState state        ) throws CantAcceptAddressExchangeRequestException,
                                                                                       PendingRequestNotFoundException          {

        System.out.println("************ Crypto Addresses -> i'm processing dao acceptance.");

        if (requestId == null)
            throw new CantAcceptAddressExchangeRequestException(null, "", "The requestId is required, can not be null");

        if (cryptoAddress == null)
            throw new CantAcceptAddressExchangeRequestException(null, "", "The cryptoAddress is required, can not be null");

        if (state == null)
            throw new CantAcceptAddressExchangeRequestException(null, "", "The state is required, can not be null");

        try {

            System.out.println("************ Crypto Addresses -> dao validation ok.");

            RequestAction action = RequestAction.ACCEPT;

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);


            System.out.println("************ Crypto Addresses -> load to memory ok.");

           DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();


                System.out.println("************ Crypto Addresses -> i will update the record.");

                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_CRYPTO_ADDRESS_COLUMN_NAME, cryptoAddress.getAddress());
                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME         , state        .getCode()   );
                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME        , action       .getCode()   );

                addressExchangeRequestTable.updateRecord(record);

                System.out.println("************ Crypto Addresses -> updating ok.");

        } catch (CantUpdateRecordException e) {

            throw new CantAcceptAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    /**
     * when i deny a request i indicate the ns agent to do the next action:
     * State : @state.
     * Action: DENY.
     *
     * @param requestId id of the address exchange request we want to confirm.
     *
     * @throws CantDenyAddressExchangeRequestException      if something goes wrong.
     * @throws PendingRequestNotFoundException              if i can't find the record.
     */
    public void denyAddressExchangeRequest(final UUID          requestId,
                                           final ProtocolState state    ) throws CantDenyAddressExchangeRequestException,
                                                                        PendingRequestNotFoundException        {

        if (requestId == null)
            throw new CantDenyAddressExchangeRequestException(null, "", "The requestId is required, can not be null");

        if (state == null)
            throw new CantDenyAddressExchangeRequestException(null, "", "The state is required, can not be null");

        try {

            RequestAction action = RequestAction.DENY           ;

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

         DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();


                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME , state .getCode());
                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME, action.getCode());

                addressExchangeRequestTable.updateRecord(record);

        } catch (CantUpdateRecordException e) {

            throw new CantDenyAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    /**
     * when i confirm a request i put it in the final state, indicating:
     * State : DONE.
     * Action: NONE.
     *
     * @param requestId id of the address exchange request we want to confirm.
     *
     * @throws CantConfirmAddressExchangeRequestException   if something goes wrong.
     * @throws PendingRequestNotFoundException              if i can't find the record.
     */
    public void confirmAddressExchangeRequest(UUID requestId) throws CantConfirmAddressExchangeRequestException,
                                                                     PendingRequestNotFoundException           {

        if (requestId == null) {
            throw new CantConfirmAddressExchangeRequestException(null, "", "The requestId is required, can not be null");
        }

        try {

            ProtocolState state  = ProtocolState.DONE;
            RequestAction action = RequestAction.NONE;

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);


            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);
            addressExchangeRequestTable.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_MESSAGE_TYPE_COLUMN_NAME, AddressesConstants.OUTGOING_MESSAGE, DatabaseFilterType.EQUAL);


            DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME , state .getCode());

                addressExchangeRequestTable.updateRecord(record);


        } catch (CantUpdateRecordException e) {

            throw new CantConfirmAddressExchangeRequestException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    /**
     * change the protocol state
     *
     * @param requestId id of the address exchange request we want to confirm.
     * @param state     protocol state to change
     *
     * @throws CantChangeProtocolStateException      if something goes wrong.
     * @throws PendingRequestNotFoundException       if i can't find the record.
     */
    public void changeProtocolState(final UUID          requestId,
                                    final ProtocolState state    ) throws CantChangeProtocolStateException,
                                                                         PendingRequestNotFoundException  {

        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");

        if (state == null)
            throw new CantChangeProtocolStateException(null, "", "The state is required, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

           DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

           record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME , state .getCode());

            addressExchangeRequestTable.updateRecord(record);


        } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    public boolean isPendingRequestNotReaded(RequestType received) throws CantListPendingCryptoAddressRequestsException {

        if (received == null || received == null) throw new CantListPendingCryptoAddressRequestsException(null, "", "received, can not be null");


        try {

            DatabaseTable table = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME, received.getCode(), DatabaseFilterType.EQUAL);
            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME, Boolean.FALSE.toString(), DatabaseFilterType.EQUAL);

            table.setFilterTop("1");

            table.loadToMemory();

            return (!table.getRecords().isEmpty());

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    public boolean isPendingRequestByProtocolStateAndMessageReceive(ProtocolState protocolState, RequestType received) throws CantListPendingCryptoAddressRequestsException {

        if (protocolState == null || received == null) throw new CantListPendingCryptoAddressRequestsException(null, "", "actorType, can not be null");


        try {

            DatabaseTable table = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, protocolState.getCode(), DatabaseFilterType.EQUAL);
            table.addStringFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME, received.getCode(), DatabaseFilterType.EQUAL);

            table.setFilterTop("1");

            table.loadToMemory();

            return (!table.getRecords().isEmpty());

        } catch (CantLoadTableToMemoryException exception) {

            throw new CantListPendingCryptoAddressRequestsException(exception, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    public void changeRequestType(UUID requestId, RequestType type) throws CantChangeProtocolStateException, PendingRequestNotFoundException {
        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");

        if (type == null)
            throw new CantChangeProtocolStateException(null, "", "The type is required, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

            DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

           record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME, type.getCode());

            addressExchangeRequestTable.updateRecord(record);


        } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    public void markRead(UUID requestId) throws CantChangeProtocolStateException, PendingRequestNotFoundException {
        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");
        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

            DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();



                record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME, Boolean.TRUE.toString());

                addressExchangeRequestTable.updateRecord(record);


        } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    public void markReadAndDone(UUID requestId) throws CantChangeProtocolStateException, PendingRequestNotFoundException {
        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");
        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);


            DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

           record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME, Boolean.TRUE.toString());
            record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME, ProtocolState.DONE.getCode());

                addressExchangeRequestTable.updateRecord(record);


        } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    /**
     * change the protocol state
     *
     * @param requestId id of the address exchange request we want to confirm.
     * @param action     Request Action  to change
     *
     * @throws CantChangeProtocolStateException      if something goes wrong.
     * @throws PendingRequestNotFoundException       if i can't find the record.
     */
    public void changeActionState(final UUID          requestId,
                                    final  RequestAction action    ) throws CantChangeProtocolStateException,
            PendingRequestNotFoundException  {

        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");

        if (action == null)
            throw new CantChangeProtocolStateException(null, "", "The state is required, can not be null");

        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

           DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

           record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME, action.getCode());

                addressExchangeRequestTable.updateRecord(record);

             } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }


    public void changeSentNumber(final UUID          requestId,
                                    final int  sentNumber    ) throws CantChangeProtocolStateException,
            PendingRequestNotFoundException  {

        if (requestId == null)
            throw new CantChangeProtocolStateException(null, "", "The requestId is required, can not be null");


        try {

            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, requestId, DatabaseFilterType.EQUAL);

           DatabaseTableRecord record = addressExchangeRequestTable.getEmptyRecord();

           record.setIntegerValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_SENT_NUMBER_COLUMN_NAME, sentNumber);

            addressExchangeRequestTable.updateRecord(record);

        } catch (CantUpdateRecordException e) {

            throw new CantChangeProtocolStateException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot update the record.");
        }
    }

    public void delete(UUID notificationId) throws CantDeleteRecordException {

        try {
            DatabaseTable addressExchangeRequestTable = database.getTable(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TABLE_NAME);

            addressExchangeRequestTable.addUUIDFilter(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, notificationId, DatabaseFilterType.EQUAL);

            addressExchangeRequestTable.deleteRecord();

        } catch (CantDeleteRecordException e) {

            throw new CantDeleteRecordException(CantDeleteRecordException.DEFAULT_MESSAGE,e, "Exception not handled by the plugin, there is a problem in database and i cannot load the table.","");
        }

    }


    private DatabaseTableRecord buildDatabaseRecord(final DatabaseTableRecord                                 record                ,
                                                    final CryptoAddressesNetworkServiceCryptoAddressRequest addressExchangeRequest) {

        record.setUUIDValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME, addressExchangeRequest.getRequestId());

        if (addressExchangeRequest.getWalletPublicKey() != null)
            record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_WALLET_PUBLIC_KEY_COLUMN_NAME             , addressExchangeRequest.getWalletPublicKey()                  );

        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_TYPE_REQUESTING_COLUMN_NAME      , addressExchangeRequest.getIdentityTypeRequesting()     .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_TYPE_RESPONDING_COLUMN_NAME      , addressExchangeRequest.getIdentityTypeResponding()     .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_REQUESTING_COLUMN_NAME, addressExchangeRequest.getIdentityPublicKeyRequesting()          );
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_RESPONDING_COLUMN_NAME, addressExchangeRequest.getIdentityPublicKeyResponding()          );
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_CRYPTO_CURRENCY_COLUMN_NAME               , addressExchangeRequest.getCryptoCurrency()             .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME                         , addressExchangeRequest.getState()                      .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME                          , addressExchangeRequest.getType()                       .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME                        , addressExchangeRequest.getAction()                     .getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_DEALER_COLUMN_NAME, addressExchangeRequest.getCryptoAddressDealer().getCode());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_BLOCKCHAIN_NETWORK_TYPE_COLUMN_NAME, addressExchangeRequest.getBlockchainNetworkType().getCode());
        record.setIntegerValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_SENT_NUMBER_COLUMN_NAME, addressExchangeRequest.getSentNumber());
        record.setIntegerValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_SENT_NUMBER_COLUMN_NAME, addressExchangeRequest.getSentNumber());
        record.setIntegerValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_SENT_NUMBER_COLUMN_NAME, addressExchangeRequest.getSentNumber());
        record.setLongValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TIMESTAMP_COLUMN_NAME, addressExchangeRequest.getSentDate());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_MESSAGE_TYPE_COLUMN_NAME, addressExchangeRequest.getMessageType());
        record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME,String.valueOf(addressExchangeRequest.isReadMark()));


        if (addressExchangeRequest.getCryptoAddress() != null)
            record.setStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_CRYPTO_ADDRESS_COLUMN_NAME, addressExchangeRequest.getCryptoAddress().getAddress());

        return record;
    }

    private CryptoAddressRequest buildAddressExchangeRequestRecord(DatabaseTableRecord record) throws InvalidParameterException {

        UUID   requestId                    = record.getUUIDValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ID_COLUMN_NAME);
        String walletPublicKey              = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_WALLET_PUBLIC_KEY_COLUMN_NAME);
        String identityTypeRequestingString = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_TYPE_REQUESTING_COLUMN_NAME      );
        String identityTypeRespondingString = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_TYPE_RESPONDING_COLUMN_NAME      );
        String identityPublicKeyRequesting  = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_REQUESTING_COLUMN_NAME);
        String identityPublicKeyResponding  = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_IDENTITY_PUBLIC_KEY_RESPONDING_COLUMN_NAME);
        String cryptoCurrencyString         = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_CRYPTO_CURRENCY_COLUMN_NAME               );
        String cryptoAddressString          = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_CRYPTO_ADDRESS_COLUMN_NAME                );
        String stateString                  = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_STATE_COLUMN_NAME                         );
        String typeString                   = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TYPE_COLUMN_NAME                          );
        String actionString                 = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_ACTION_COLUMN_NAME                        );
        String dealerString                 = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_DEALER_COLUMN_NAME                        );
        String blockchainNetworkTypeString  = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_BLOCKCHAIN_NETWORK_TYPE_COLUMN_NAME);

        int sentNumber                      = record.getIntegerValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_SENT_NUMBER_COLUMN_NAME);
        long sentDate                     = record.getLongValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_TIMESTAMP_COLUMN_NAME);
        String messageType                 = record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_MESSAGE_TYPE_COLUMN_NAME);
        boolean readMark                 = Boolean.valueOf(record.getStringValue(CryptoAddressesNetworkServiceDatabaseConstants.ADDRESS_EXCHANGE_REQUEST_READ_MARK_COLUMN_NAME));


        Actors                identityTypeRequesting = Actors               .getByCode(identityTypeRequestingString);
        Actors                identityTypeAccepting  = Actors               .getByCode(identityTypeRespondingString);
        BlockchainNetworkType blockchainNetworkType  = BlockchainNetworkType.getByCode(blockchainNetworkTypeString);
        ProtocolState         state                  = ProtocolState        .getByCode(stateString);
        RequestType           type                   = RequestType          .getByCode(typeString);
        RequestAction         action                 = RequestAction        .getByCode(actionString);
        CryptoCurrency        cryptoCurrency         = CryptoCurrency       .getByCode(cryptoCurrencyString);
        CryptoAddressDealers  dealer                 = CryptoAddressDealers .getByCode(dealerString);

        CryptoAddress         cryptoAddress          = new CryptoAddress(cryptoAddressString , cryptoCurrency);

        return new CryptoAddressesNetworkServiceCryptoAddressRequest(
                requestId                  ,
                walletPublicKey            ,
                identityTypeRequesting     ,
                identityTypeAccepting      ,
                identityPublicKeyRequesting,
                identityPublicKeyResponding,
                cryptoCurrency             ,
                cryptoAddress              ,
                state                      ,
                type                       ,
                action                     ,
                dealer                     ,
                blockchainNetworkType,
                sentNumber,
                sentDate,
                messageType,
                readMark
        );
    }



}