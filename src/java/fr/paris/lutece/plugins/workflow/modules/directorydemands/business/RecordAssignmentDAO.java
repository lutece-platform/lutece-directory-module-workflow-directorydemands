/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.workflow.modules.directorydemands.business;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * This class provides Data Access methods for RecordAssignment objects
 */
public class RecordAssignmentDAO implements IRecordAssignmentDAO
{

    // Constants
    private static final String SQL_QUERY_SELECTALL = "SELECT id, unittree_unit_assignment.id_resource, id_assignor_unit, id_assigned_unit, assignment_date, assignment_type, is_active,"
            + " u_assignor.id_parent as id_parent_assignor_unit, u_assignor.label as label_assignor_unit, u_assignor.description as description_assignor_unit, "
            + " u_assigned.id_parent as id_parent_assigned_unit, u_assigned.label as label_assigned_unit, u_assigned.description as description_assigned_unit, "
            + " directory_record.date_creation "
            + " FROM unittree_unit_assignment "
            + " LEFT JOIN  unittree_unit u_assignor on u_assignor.id_unit = unittree_unit_assignment.id_assignor_unit "
            + " LEFT JOIN  unittree_unit u_assigned on u_assigned.id_unit = unittree_unit_assignment.id_assigned_unit  ";
    private static final String SQL_SUBQUERY_SELECT_ID_RESOURCE = " SELECT id_resource  FROM unittree_unit_assignment "
            + "LEFT JOIN directory_record  on directory_record.id_record = unittree_unit_assignment.id_resource "
            + "LEFT JOIN directory_directory  on directory_directory.id_directory = directory_record.id_directory  ";

    // SQL parts to filter recordAssignments
    private static final String SQL_WHERE_BASE = "WHERE  1 ";

    private static final String SQL_ADD_CLAUSE = " AND ( ";
    private static final String SQL_ADD_RECORD_FIELD_CLAUSE = " AND id_record IN ( ";
    private static final String SQL_END_ADD_CLAUSE = " ) ";
    private static final String SQL_BEGIN_ADD_CLAUSE = " ( ";

    private static final String SQL_USER_UNIT_WHERE_PART1 = " id_assigned_unit in (?";
    private static final String SQL_USER_UNIT_WHERE_PART2 = ") ";

    private static final String SQL_FILTER_BY_RESOURCE_TYPE = " unittree_unit_assignment.resource_type = '" + Record.WORKFLOW_RESOURCE_TYPE + "' ";
    private static final String SQL_FILTER_BY_RESOURCE_ID = " unittree_unit_assignment.id_resource in ( ";
    private static final String SQL_FILTER_BY_RESOURCE_ID_END = " ) ";

    private static final String SQL_ACTIVE_ASSIGNMENTS_ONLY_WHERE_PART = " unittree_unit_assignment.is_active = 1 ";
    private static final String SQL_LAST_ACTIVE_ASSIGNMENTS_ONLY_WHERE_PART = " unittree_unit_assignment.assignment_date = (select max(assignment_date) from unittree_unit_assignment uua where uua.id_resource = unittree_unit_assignment.id_resource and uua.is_active=1 )";

    private static final String SQL_FILTER_PERIOD_WHERE_PART = " directory_record.date_creation  >= date_add( current_timestamp , INTERVAL -? DAY) ";

    private static final String SQL_DIRECTORY_RECORD_FROM_PART = "  LEFT JOIN directory_record  on directory_record.id_record = unittree_unit_assignment.id_resource ";
    private static final String SQL_DIRECTORY_RECORD_WHERE_PART = " directory_record.id_directory = ? ";

    private static final String SQL_DIRECTORY_FROM_PART = "  LEFT JOIN directory_directory  on directory_directory.id_directory = directory_record.id_directory ";
    private static final String SQL_DIRECTORY_WHERE_PART = " directory_directory.is_enabled = ? ";

    private static final String SQL_STATE_FROM_PART = " LEFT JOIN workflow_resource_workflow on directory_record.id_record = workflow_resource_workflow.id_resource ";
    private static final String SQL_STATE_WHERE_PART = "  workflow_resource_workflow.id_state = ? ";

    private static final String SQL_ASSIGNED_UNIT_WHERE_PART = " u_assigned.id_unit = ? ";
    
    private static final String SQL_RECORD_FIELD_GLOBAL_WHERE_PART = " id_record IN ";
    private static final String SQL_RECORD_FIELD_WHERE_PART = " SELECT DISTINCT id_record FROM directory_record_field WHERE record_field_value = ? ";

    private static final String SQL_DEFAULT_ORDER_BY = " order by assignment_date ";
    private static final String SQL_ORDER_BY_CREATED = " order by directory_record.date_creation ";
    private static final String SQL_ORDER_BY_ASSIGNED = " order by u_assigned.label ";
    private static final String SQL_DESC = " DESC ";
    private static final String SQL_ASC = " ASC ";

    private static final String PARAMETER_SORT_BY_CREATED = "created";
    private static final String PARAMETER_SORT_BY_ASSIGNED = "assigned";
    
    private static final String DEFAULT_RECORD_FIELD_VALUE = "-1";

    /**
     * {@inheritDoc }
     */
    @Override
    public List<RecordAssignment> selectRecordAssignmentsFiltredList( RecordAssignmentFilter filterParameters, Plugin plugin )
    {
        List<RecordAssignment> listRecordAssignments = new ArrayList<>( );

        StringBuilder sql = new StringBuilder( SQL_QUERY_SELECTALL );
        StringBuilder sql_subquerySelectIdResource = new StringBuilder( SQL_SUBQUERY_SELECT_ID_RESOURCE );

        StringBuilder whereClause = new StringBuilder( SQL_WHERE_BASE );

        boolean DirectoryRecordJoinAdded = false;

        // filter by resource type
        whereClause.append( SQL_ADD_CLAUSE ).append( SQL_FILTER_BY_RESOURCE_TYPE ).append( SQL_END_ADD_CLAUSE );

        // filter by resource Id ...
        sql_subquerySelectIdResource.append( SQL_WHERE_BASE );

        // filter resource Id by... User unit
        if ( !filterParameters.getUserUnitIdList( ).isEmpty( ) )
        {
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            String strUnitWhereClause = SQL_USER_UNIT_WHERE_PART1;

            StringBuilder additionnalParameters = new StringBuilder( );
            if ( filterParameters.getUserUnitIdList( ).size( ) > 1 )
            {
                for ( int i = 0; i < filterParameters.getUserUnitIdList( ).size( ) - 1; i++ )
                {
                    additionnalParameters.append( ", ?" );
                }
            }
            strUnitWhereClause += additionnalParameters + SQL_USER_UNIT_WHERE_PART2;
            sql_subquerySelectIdResource.append( strUnitWhereClause );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }

        // filter resource Id by... ACTIVE_RECORDS_ONLY
        if ( filterParameters.isActiveAssignmentRecordsOnly( ) )
        {
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            sql_subquerySelectIdResource.append( SQL_ACTIVE_ASSIGNMENTS_ONLY_WHERE_PART );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }

        // filter resource Id by... period
        if ( filterParameters.getNumberOfDays( ) > 0 )
        {
            sql.append( SQL_DIRECTORY_RECORD_FROM_PART );
            DirectoryRecordJoinAdded = true;
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            sql_subquerySelectIdResource.append( SQL_FILTER_PERIOD_WHERE_PART );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }

        // filter resource Id by... active directory
        if ( filterParameters.isActiveDirectory( ) )
        {
            if ( !DirectoryRecordJoinAdded )
            {
                sql.append( SQL_DIRECTORY_RECORD_FROM_PART );
                DirectoryRecordJoinAdded = true;
            }
            sql.append( SQL_DIRECTORY_FROM_PART );
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            sql_subquerySelectIdResource.append( SQL_DIRECTORY_WHERE_PART );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }

        // filter resource Id by... directory ( + state )
        if ( filterParameters.getDirectoryId( ) > 0 )
        {

            if ( !DirectoryRecordJoinAdded )
            {
                sql.append( SQL_DIRECTORY_RECORD_FROM_PART );
                DirectoryRecordJoinAdded = true;
            }
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            sql_subquerySelectIdResource.append( SQL_DIRECTORY_RECORD_WHERE_PART );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );

            // state
            if ( filterParameters.getStateId( ) > 0 )
            {
                sql.append( SQL_STATE_FROM_PART );
                sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
                sql_subquerySelectIdResource.append( SQL_STATE_WHERE_PART );
                sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
            }
        }

        // filter resource Id by Assigned Unit id
        if ( filterParameters.getAssignedUnitId( ) > 0 )
        {
            sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
            sql_subquerySelectIdResource.append( SQL_ASSIGNED_UNIT_WHERE_PART );
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }
        
        // Filter on the record field values inside the RecordFieldItem
        List<RecordFieldItem> listRecordFieldItem = filterParameters.getListRecordFieldItem( );
        boolean bRecordFieldRequestClose = Boolean.FALSE;
        for ( RecordFieldItem recordFieldItem : listRecordFieldItem )
        {
            String strRecordFieldItemValue = recordFieldItem.getRecordFieldValue( );
            if ( StringUtils.isNotBlank( strRecordFieldItemValue ) && !DEFAULT_RECORD_FIELD_VALUE.equals( strRecordFieldItemValue ) )
            {
                if ( !DirectoryRecordJoinAdded )
                {
                    sql.append( SQL_DIRECTORY_RECORD_FROM_PART );
                    DirectoryRecordJoinAdded = true;
                }
                
                if ( !bRecordFieldRequestClose )
                {
                    sql_subquerySelectIdResource.append( SQL_ADD_CLAUSE );
                    sql_subquerySelectIdResource.append( SQL_RECORD_FIELD_GLOBAL_WHERE_PART );
                    sql_subquerySelectIdResource.append( SQL_BEGIN_ADD_CLAUSE );
                    sql_subquerySelectIdResource.append( SQL_RECORD_FIELD_WHERE_PART );
                    sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
                    bRecordFieldRequestClose = true;
                }
                else
                {
                    sql_subquerySelectIdResource.append( SQL_ADD_RECORD_FIELD_CLAUSE );
                    sql_subquerySelectIdResource.append( SQL_RECORD_FIELD_WHERE_PART );
                    sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
                }
            }
        }
        
        if ( bRecordFieldRequestClose )
        {
            sql_subquerySelectIdResource.append( SQL_END_ADD_CLAUSE );
        }

        // add subquery to the where clause
        whereClause.append( SQL_ADD_CLAUSE ).append( SQL_FILTER_BY_RESOURCE_ID ).append( sql_subquerySelectIdResource ).append( SQL_FILTER_BY_RESOURCE_ID_END )
                .append( SQL_END_ADD_CLAUSE );

        // filter resource Id by... LAST_ACTIVE_RECORDS_ONLY
        if ( filterParameters.isLastActiveAssignmentRecordsOnly( ) )
        {
            whereClause.append( SQL_ADD_CLAUSE ).append( SQL_LAST_ACTIVE_ASSIGNMENTS_ONLY_WHERE_PART ).append( SQL_END_ADD_CLAUSE );
        }

        String strOrderBy = SQL_DEFAULT_ORDER_BY + SQL_DESC;
        if ( !StringUtils.isBlank( filterParameters.getOrderBy( ) ) )
        {
            String strParam = filterParameters.getOrderBy( );
            if ( PARAMETER_SORT_BY_CREATED.equals( strParam ) )
            {
                strOrderBy = SQL_ORDER_BY_CREATED + ( filterParameters.isAsc( ) ? SQL_ASC : SQL_DESC );
            }
            else
                if ( PARAMETER_SORT_BY_ASSIGNED.equals( strParam ) )
                {
                    strOrderBy = SQL_ORDER_BY_ASSIGNED + ( filterParameters.isAsc( ) ? SQL_ASC : SQL_DESC );
                }

        }

        // prepare & execute query
        DAOUtil daoUtil = new DAOUtil( sql.toString( ) + whereClause.toString( ) + strOrderBy, plugin );

        int i = 1;
        if ( !filterParameters.getUserUnitIdList( ).isEmpty( ) )
        {
            for ( Integer unitId : filterParameters.getUserUnitIdList( ) )
            {
                daoUtil.setInt( i++, unitId );
            }
        }

        if ( filterParameters.getNumberOfDays( ) > 0 )
            daoUtil.setInt( i++, filterParameters.getNumberOfDays( ) );

        if ( filterParameters.isActiveDirectory( ) )
            daoUtil.setBoolean( i++, filterParameters.isActiveDirectory( ) );

        if ( filterParameters.getDirectoryId( ) > 0 )
        {
            daoUtil.setInt( i++, filterParameters.getDirectoryId( ) );

            if ( filterParameters.getStateId( ) > 0 )
                daoUtil.setInt( i++, filterParameters.getStateId( ) );
        }

        if ( filterParameters.getAssignedUnitId( ) > 0 )
        {
            daoUtil.setInt( i++, filterParameters.getAssignedUnitId( ) );
        }

        for ( RecordFieldItem recordFieldItem : listRecordFieldItem )
        {
            String strRecordFieldItemValue = recordFieldItem.getRecordFieldValue( );
            if ( StringUtils.isNotBlank( strRecordFieldItemValue ) && !DEFAULT_RECORD_FIELD_VALUE.equals( strRecordFieldItemValue ) )
            {
                daoUtil.setString( i++, strRecordFieldItemValue );
            }
        }

        // execute query
        daoUtil.executeQuery( );

        // fetch results
        while ( daoUtil.next( ) )
        {
            listRecordAssignments.add( dataToRecordAssignment( daoUtil ) );
        }

        daoUtil.free( );

        return listRecordAssignments;
    }

    /**
     * Creates a {@code RecordAssignment} object from the data of the specified {@code DAOUtil}
     * 
     * @param daoUtil
     *            the {@code DAOUtil} containing the data
     * @return a new {@code RecordAssignment} object
     */
    private RecordAssignment dataToRecordAssignment( DAOUtil daoUtil )
    {
        RecordAssignment recordAssignment = new RecordAssignment( );
        recordAssignment.setId( daoUtil.getInt( "id" ) );
        recordAssignment.setIdRecord( daoUtil.getInt( "id_resource" ) );
        recordAssignment.setAssignmentDate( daoUtil.getTimestamp( "assignment_date" ) );

        Unit unitAssignor = recordAssignment.getAssignorUnit( );
        unitAssignor.setIdUnit( daoUtil.getInt( "id_assignor_unit" ) );
        unitAssignor.setIdParent( daoUtil.getInt( "id_parent_assignor_unit" ) );
        unitAssignor.setLabel( daoUtil.getString( "label_assignor_unit" ) );
        unitAssignor.setDescription( daoUtil.getString( "description_assignor_unit" ) );

        Unit unitAssigned = recordAssignment.getAssignedUnit( );
        unitAssigned.setIdUnit( daoUtil.getInt( "id_assigned_unit" ) );
        unitAssigned.setIdParent( daoUtil.getInt( "id_parent_assigned_unit" ) );
        unitAssigned.setLabel( daoUtil.getString( "label_assigned_unit" ) );
        unitAssigned.setDescription( daoUtil.getString( "description_assigned_unit" ) );

        return recordAssignment;
    }

}
