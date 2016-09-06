package com.bitdubai.fermat_api.layer.osa_android.database_system;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantDeleteRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantTruncateTableException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseRecordExistException;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;

import java.util.List;
import java.util.UUID;

/**
 * <p>The abstract class <code>DatabaseTable</code> is a interface
 * that define the methods to manage a DatabaseTable object. Set filters and orders, and load records to memory.
 *
 * @author Luis
 * @version 1.0.0
 * @since 01/01/15.
 */
public interface DatabaseTable {

    void loadToMemory() throws CantLoadTableToMemoryException;

    List<DatabaseTableRecord> loadRecords(List<DatabaseTableFilter> tableFilters,List<DatabaseTableFilterGroup> databaseTableFilterGroups,String[] columns) throws CantLoadTableToMemoryException;

    void truncate() throws CantTruncateTableException;

    long getCount() throws CantLoadTableToMemoryException;

    List<DatabaseTableRecord> getRecords();

    void insertRecord(DatabaseTableRecord record) throws CantInsertRecordException;

    void updateRecord(DatabaseTableRecord record) throws CantUpdateRecordException;

    void deleteAllRecord() throws CantDeleteRecordException;

    void deleteRecord() throws CantDeleteRecordException;

    DatabaseTableRecord getEmptyRecord();

    boolean isTableExists();

    List<DatabaseTableRecord> customQuery(String query, boolean customResult) throws CantLoadTableToMemoryException;

    DatabaseTableFilter getEmptyTableFilter();

    DatabaseTableFilter getNewFilter(String column, DatabaseFilterType type, String value);

    DatabaseTableFilterGroup getNewFilterGroup(List<DatabaseTableFilter> tableFilters, List<DatabaseTableFilterGroup> filterGroups, DatabaseFilterOperator filterOperator);

    void setFilterTop(String top);

    void setFilterOffSet(String offset);

    void addStringFilter(String columnName, String value, DatabaseFilterType type);

    void addFermatEnumFilter(String columnName, FermatEnum value, DatabaseFilterType type);

    void addFilterOrder(String columnName, DatabaseFilterOrder direction);

    /**
     * Through this method you can order the results of the query by nearby locations to a defined point.
     * The distance to the point will be returned in the query by the field @distanceField
     * This method was thought having in count that we're managing long coordinates and never degree coordinates.
     *
     * @param latitudeField  field representing the latitude in the table
     * @param longitudeField field representing the longitude in the table
     * @param point          to get the near locations
     * @param direction      of the order (DESC/ASC) by default is ASC
     * @param distanceField  label for the field of the returning distance between the point and the record.
     */
    // TODO implement in android version
    void addNearbyLocationOrder(String latitudeField,
                                String longitudeField,
                                Location point,
                                DatabaseFilterOrder direction,
                                String distanceField);

    void addUUIDFilter(String columnName, UUID value, DatabaseFilterType type);

    void addAggregateFunction(String columnName, DataBaseAggregateFunctionType operator, String alias);

    void setFilterGroup(DatabaseTableFilterGroup filterGroup);

    void setFilterGroup(List<DatabaseTableFilter> tableFilters, List<DatabaseTableFilterGroup> filterGroups, DatabaseFilterOperator filterOperator);

    void clearAllFilters();

    @Deprecated
        // try to not use this when you're updating records. android database needs filters to update records.
    DatabaseTableRecord getRecordFromPk(String pk) throws Exception;

    // todo try to substract this method from here, they don't belong
    String makeFilter();

    String getTableName();

    List<DatabaseAggregateFunction> getTableAggregateFunction();

    void insertRecordIfNotExist(DatabaseTableRecord record,List<DatabaseTableFilter> filters,DatabaseTableFilterGroup databaseTableFilterGroup) throws DatabaseRecordExistException, CantInsertRecordException;

    /**
     * Cantidad de records con esos filtros
     * @return
     */
    long numRecords();

    DatabaseTableFilter buildFilter(String columnName, String value, DatabaseFilterType type);
}
