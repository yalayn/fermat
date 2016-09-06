package com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.exceptions.CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.database.CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory</code>
 * contains the methods that the Developer Database Tools uses to show the information.
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 01/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */

public class CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;


    Database database;

    public CryptoPaymentRequestNetworkServiceDeveloperDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem,
                                                                      final UUID                 pluginId            ) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId             = pluginId            ;
    }

    public void initializeDatabase() throws CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException {

        try {

            database = this.pluginDatabaseSystem.openDatabase(pluginId, pluginId.toString());

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            throw new CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException(cantOpenDatabaseException);

        } catch (DatabaseNotFoundException e) {

            CryptoPaymentRequestNetworkServiceDatabaseFactory cryptoPaymentRequestNetworkServiceDatabaseFactory = new CryptoPaymentRequestNetworkServiceDatabaseFactory(pluginDatabaseSystem);

            try {

                database = cryptoPaymentRequestNetworkServiceDatabaseFactory.createDatabase(
                        pluginId,
                        pluginId.toString()
                );

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {

                throw new CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException(cantCreateDatabaseException);

            }
        }
    }

    public List<DeveloperDatabase> getDatabaseList(final DeveloperObjectFactory developerObjectFactory) {

        List<DeveloperDatabase> databases = new ArrayList<>();

        databases.add(developerObjectFactory.getNewDeveloperDatabase("Crypto Payment Request", this.pluginId.toString()));

        return databases;
    }


    public List<DeveloperDatabaseTable> getDatabaseTableList(final DeveloperObjectFactory developerObjectFactory) {


        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        /**
         * Table Crypto Address Request columns.
         */
        List<String> cryptoAddressRequestColumns = new ArrayList<>();

        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_REQUEST_ID_COLUMN_NAME         );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_IDENTITY_PUBLIC_KEY_COLUMN_NAME);
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_IDENTITY_TYPE_COLUMN_NAME      );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_ACTOR_PUBLIC_KEY_COLUMN_NAME   );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_ACTOR_TYPE_COLUMN_NAME         );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_DESCRIPTION_COLUMN_NAME        );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_CRYPTO_ADDRESS_COLUMN_NAME     );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_CRYPTO_CURRENCY_COLUMN_NAME    );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_AMOUNT_COLUMN_NAME             );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_START_TIME_STAMP_COLUMN_NAME   );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_DIRECTION_COLUMN_NAME          );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_ACTION_COLUMN_NAME             );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_PROTOCOL_STATE_COLUMN_NAME     );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_NETWORK_TYPE_COLUMN_NAME       );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_WALLET_REFERENCE_TYPE_COLUMN_NAME       );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_SENT_COUNT_COLUMN_NAME       );
        cryptoAddressRequestColumns.add(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_MESSAGE_TYPE_COLUMN_NAME       );

        /**
         * Table Crypto Address Request addition.
         */
        DeveloperDatabaseTable cryptoAddressRequestTable = developerObjectFactory.getNewDeveloperDatabaseTable(CryptoPaymentRequestNetworkServiceDatabaseConstants.CRYPTO_PAYMENT_REQUEST_TABLE_NAME, cryptoAddressRequestColumns);
        tables.add(cryptoAddressRequestTable);

        return tables;
    }

    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                      final DeveloperDatabase developerDatabase,
                                                                      final DeveloperDatabaseTable developerDatabaseTable) {


        try {

            initializeDatabase();

            DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());

            selectedTable.loadToMemory();

            List<DatabaseTableRecord> records = selectedTable.getRecords();



            List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<>();

            for (DatabaseTableRecord row : records) {
                List<String> developerRow = new ArrayList<>();

                for (DatabaseRecord field : row.getValues())
                    developerRow.add(field.getValue());

                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }

            return returnedRecords;

        } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

            return new ArrayList<>();
        }catch (CantInitializeCryptoPaymentRequestNetworkServiceDatabaseException cantLoadTableToMemory) {

            return new ArrayList<>();
        }
    }

}