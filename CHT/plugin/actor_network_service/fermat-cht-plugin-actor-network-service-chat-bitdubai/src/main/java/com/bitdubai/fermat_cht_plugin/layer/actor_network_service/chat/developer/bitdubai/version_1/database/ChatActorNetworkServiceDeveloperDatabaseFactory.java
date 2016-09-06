package com.bitdubai.fermat_cht_plugin.layer.actor_network_service.chat.developer.bitdubai.version_1.database;

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
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantInitializeDatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by José D. Vilchez A. (josvilchezalmera@gmail.com) on 07/04/16.
 * Edited by Miguel Rincon on 19/04/2016
 */
public class ChatActorNetworkServiceDeveloperDatabaseFactory {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID pluginId;

    private Database database;

    /**
     * Constructor
     *
     * @param pluginDatabaseSystem
     * @param pluginId
     */
    public ChatActorNetworkServiceDeveloperDatabaseFactory(final PluginDatabaseSystem pluginDatabaseSystem,
                                                           final UUID pluginId) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;
    }

    /**
     * This method open or creates the database i'll be working with
     *
     * @throws CantInitializeDatabaseException
     */
    public void initializeDatabase(final String tableId) throws CantInitializeDatabaseException {

        switch (tableId) {

            case ChatActorNetworkServiceDatabaseConstants.CHAT_ACTOR_NETWORK_SERVICE_DATABASE_NAME:

                try {

                    database = this.pluginDatabaseSystem.openDatabase(pluginId, tableId);

                } catch (final CantOpenDatabaseException e) {

                    throw new CantInitializeDatabaseException(e, "tableId: " + tableId, "Error trying to open the database.");

                } catch (final DatabaseNotFoundException e) {

                    final ChatActorNetworkServiceDatabaseFactory cryptoBrokerActorNetworkServiceDatabaseFactory = new ChatActorNetworkServiceDatabaseFactory(pluginDatabaseSystem);

                    try {

                        database = cryptoBrokerActorNetworkServiceDatabaseFactory.createDatabase(pluginId, tableId);

                    } catch (final CantCreateDatabaseException z) {

                        throw new CantInitializeDatabaseException(z, "tableId: " + tableId, "Error trying to create the database.");
                    }
                }
                break;

        }
    }

    public List<DeveloperDatabase> getDatabaseList(final DeveloperObjectFactory developerObjectFactory) {

        List<DeveloperDatabase> databases = new ArrayList<>();

        databases.add(developerObjectFactory.getNewDeveloperDatabase(
                "Actor Network Service",
                ChatActorNetworkServiceDatabaseConstants.CHAT_ACTOR_NETWORK_SERVICE_DATABASE_NAME
        ));

        return databases;
    }

    public List<DeveloperDatabaseTable> getDatabaseTableList(final DeveloperObjectFactory developerObjectFactory,
                                                             final DeveloperDatabase developerDatabase) {

        List<DeveloperDatabaseTable> tables = new ArrayList<>();

        switch (developerDatabase.getId()) {

            case ChatActorNetworkServiceDatabaseConstants.CHAT_ACTOR_NETWORK_SERVICE_DATABASE_NAME:

                /**
                 * Table Connection News columns.
                 */
                List<String> connectionNewsColumns = new ArrayList<>();

                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ID_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_PUBLIC_KEY_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ACTOR_TYPE_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENDER_ALIAS_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_DESTINATION_PUBLIC_KEY_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_TYPE_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_STATE_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_REQUEST_ACTION_COLUMN_NAME);
                connectionNewsColumns.add(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_SENT_TIME_COLUMN_NAME);
                /**
                 * Table Connection News addition.
                 */
                DeveloperDatabaseTable connectionNewsTable = developerObjectFactory.getNewDeveloperDatabaseTable(ChatActorNetworkServiceDatabaseConstants.CONNECTION_NEWS_TABLE_NAME, connectionNewsColumns);
                tables.add(connectionNewsTable);

                break;
        }

        return tables;
    }

    public final List<DeveloperDatabaseTableRecord> getDatabaseTableContent(final DeveloperObjectFactory developerObjectFactory,
                                                                            final DeveloperDatabase developerDatabase,
                                                                            final DeveloperDatabaseTable developerDatabaseTable) {

        try {

            initializeDatabase(developerDatabase.getId());

            final List<DeveloperDatabaseTableRecord> returnedRecords = new ArrayList<>();

            final DatabaseTable selectedTable = database.getTable(developerDatabaseTable.getName());

            try {

                selectedTable.loadToMemory();
            } catch (CantLoadTableToMemoryException cantLoadTableToMemory) {

                return returnedRecords;
            }

            final List<DatabaseTableRecord> records = selectedTable.getRecords();

            List<String> developerRow;

            for (DatabaseTableRecord row : records) {

                developerRow = new ArrayList<>();

                for (DatabaseRecord field : row.getValues())
                    developerRow.add(field.getValue());

                returnedRecords.add(developerObjectFactory.getNewDeveloperDatabaseTableRecord(developerRow));
            }
            return returnedRecords;

        } catch (Exception e) {

            System.err.println(e.toString());
            return new ArrayList<>();
        }
    }
}
