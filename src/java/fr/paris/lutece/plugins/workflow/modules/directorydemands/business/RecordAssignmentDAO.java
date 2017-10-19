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

    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM directory_record_unit_assignment";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, directory_record_unit_assignment.id_record, id_assignor_unit, id_assigned_unit, assignment_date, assignment_type, is_active, u_assignor.label, u_assignor.description, u_assigned.label, u_assigned.description  FROM directory_record_unit_assignment  LEFT JOIN  unittree_unit u_assignor on u_assignor.id_unit = directory_record_unit_assignment.id_assignor_unit   left JOIN  unittree_unit u_assigned on u_assigned.id_unit = directory_record_unit_assignment.id_assigned_unit  ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id = ?";
    private static final String SQL_QUERY_SELECT_LAST = SQL_QUERY_SELECTALL + " WHERE id_record = ? AND assignment_type = ? ORDER BY assignment_date DESC";
    private static final String SQL_QUERY_SELECT_BY_ID_RECORD = SQL_QUERY_SELECTALL + " WHERE id_record = ?  ORDER BY assignment_date ASC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directory_record_unit_assignment ( id, id_record, id_assigned_unit, id_assignor_unit, assignment_date, assignment_type, is_active ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directory_record_unit_assignment WHERE id = ? ";
    private static final String SQL_QUERY_DESACTIVATE = "UPDATE directory_record_unit_assignment SET is_active = 0 WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directory_record_unit_assignment SET id = ?, id_record = ?, id_assigned_unit = ?, id_assignor_unit = ? , assignment_date = ?, assignment_type = ?, is_active = ? WHERE id = ?";

    // SQL parts to filter recordAssignments
    private static final String SQL_WHERE_BASE = " WHERE 1 " ;
    private static final String SQL_ADD_CLAUSE =  " AND ( " ;
    private static final String SQL_END_ADD_CLAUSE =  " ) " ;
    
    private static final String SQL_USER_UNIT_WHERE_PART1 = " id_assigned_unit in (?" ;
    private static final String SQL_USER_UNIT_WHERE_PART2 = ") " ;
    
    private static final String SQL_ACTIVE_RECORDS_ONLY_WHERE_PART = " directory_record_unit_assignment.is_active = 1 ";
    
    private static final String SQL_FILTER_PERIOD_WHERE_PART = " directory_record.date_creation  >= date_add( current_timestamp , INTERVAL -? DAY) ";

    private static final String SQL_DIRECTORY_FROM_PART = "  LEFT JOIN directory_record  on directory_record.id_record = directory_record_unit_assignment.id_record ";
    private static final String SQL_DIRECTORY_WHERE_PART = " directory_record.id_directory = ? ";

    private static final String SQL_STATE_FROM_PART = " LEFT JOIN workflow_resource_workflow on directory_record.id_record = workflow_resource_workflow.id_resource ";
    private static final String SQL_STATE_WHERE_PART = "  workflow_resource_workflow.id_state = ? ";

    private static final String SQL_DEFAULT_ORDER_BY = " order by assignment_date DESC ";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery( );

        int nKey = ( daoUtil.next( ) ) ? daoUtil.getInt( 1 ) + 1 : 1;
        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( RecordAssignment recordAssignment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        recordAssignment.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, recordAssignment.getId( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getIdRecord( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getAssignedUnit( ).getIdUnit( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getAssignorUnit( ).getIdUnit( ) );
        daoUtil.setTimestamp( nIndex++, recordAssignment.getAssignmentDate( ) );
        daoUtil.setString( nIndex++, recordAssignment.getAssignmentType( ).getAssignmentTypeCode( ) );
        daoUtil.setBoolean( nIndex, recordAssignment.isActive( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public RecordAssignment load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        RecordAssignment recordAssignment = null;

        if ( daoUtil.next( ) )
        {
            recordAssignment = dataToRecordAssignment( daoUtil );
        }

        daoUtil.free( );

        return recordAssignment;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public RecordAssignment loadLast( int nIdRecord, AssignmentType assignmentType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST, plugin );
        daoUtil.setInt( 1, nIdRecord );
        daoUtil.setString( 2, assignmentType.getAssignmentTypeCode( ) );
        daoUtil.executeQuery( );

        RecordAssignment recordAssignment = null;

        if ( daoUtil.next( ) )
        {
            recordAssignment = dataToRecordAssignment( daoUtil );
        }

        daoUtil.free( );

        return recordAssignment;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nRecordAssignmentId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nRecordAssignmentId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void desactivate( RecordAssignment recordAssignment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DESACTIVATE, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex, recordAssignment.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( RecordAssignment recordAssignment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, recordAssignment.getId( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getIdRecord( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getAssignedUnit( ).getIdUnit( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getAssignorUnit( ).getIdUnit( ) );
        daoUtil.setTimestamp( nIndex++, recordAssignment.getAssignmentDate( ) );
        daoUtil.setString( nIndex++, recordAssignment.getAssignmentType( ).getAssignmentTypeCode( ) );
        daoUtil.setInt( nIndex, recordAssignment.getId( ) );
        daoUtil.setBoolean( nIndex, recordAssignment.isActive( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<RecordAssignment> selectRecordAssignmentsList( Plugin plugin )
    {
        List<RecordAssignment> listRecordAssignments = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listRecordAssignments.add( dataToRecordAssignment( daoUtil ) );
        }

        daoUtil.free( );

        return listRecordAssignments;
    }

    /**
     * {@inheritDoc }
     */
    @Override

    public List<RecordAssignment> selectRecordAssignmentsFiltredList( RecordAssignmentFilter filterParameters, Plugin plugin )
    {
        List<RecordAssignment> listRecordAssignments = new ArrayList<>( );
                
        StringBuilder sql = new StringBuilder( SQL_QUERY_SELECTALL );
        StringBuilder whereClause = new StringBuilder( SQL_WHERE_BASE );

        boolean DirectoryRecordJoinAdded = false;
        
        // User unit
        if ( ! filterParameters.getUserUnitIdList( ).isEmpty( ) )
        {
            whereClause.append( SQL_ADD_CLAUSE );
            String strUnitWhereClause = SQL_USER_UNIT_WHERE_PART1 ;
            if (filterParameters.getUserUnitIdList( ).size( ) > 1 ) {
                StringBuilder additionnalParameters = new StringBuilder( );
                for (int i=0; i< filterParameters.getUserUnitIdList( ).size( ) -1; i++) 
                {
                    additionnalParameters.append(  ", ?");
                }
                strUnitWhereClause += additionnalParameters + SQL_USER_UNIT_WHERE_PART2;
            }
            whereClause.append( strUnitWhereClause );
            whereClause.append( SQL_END_ADD_CLAUSE ) ;            
        }

        // ACTIVE_RECORDS_ONLY
        if ( filterParameters.isActiveRecordsOnly( ) )
        {
            whereClause.append ( SQL_ADD_CLAUSE );
            whereClause.append ( SQL_ACTIVE_RECORDS_ONLY_WHERE_PART );
            whereClause.append ( SQL_END_ADD_CLAUSE );
        }

        // period
        if ( filterParameters.getNumberOfDays( ) > 0 )
        {

                sql.append( SQL_DIRECTORY_FROM_PART ) ; 
                DirectoryRecordJoinAdded = true;
                whereClause.append( SQL_ADD_CLAUSE );
                whereClause.append( SQL_FILTER_PERIOD_WHERE_PART );
                whereClause.append( SQL_END_ADD_CLAUSE );
        }

        // directory ( + state )
        if ( filterParameters.getDirectoryId( ) > 0 )
        {

            if (!DirectoryRecordJoinAdded) sql.append( SQL_DIRECTORY_FROM_PART ) ;
            whereClause.append( SQL_ADD_CLAUSE ) ;
            whereClause.append( SQL_DIRECTORY_WHERE_PART ) ;
            whereClause.append( SQL_END_ADD_CLAUSE ) ;          
            
            // state 
            if ( filterParameters.getStateId( ) > 0 )
            {
                sql.append( SQL_STATE_FROM_PART ) ;
                whereClause.append( SQL_ADD_CLAUSE ) ;
                whereClause.append( SQL_STATE_WHERE_PART ) ;
                whereClause.append( SQL_END_ADD_CLAUSE ) ;          
            }            
        }
        
        String strOrderBy = SQL_DEFAULT_ORDER_BY ;
        if ( !StringUtils.isBlank( filterParameters.getOrderBy( ) ) )  strOrderBy = filterParameters.getOrderBy( );
        
        // prepare & execute query
        DAOUtil daoUtil = new DAOUtil( sql.toString( ) + whereClause.toString( ) + strOrderBy , plugin );

        int i=1;
        if ( !filterParameters.getUserUnitIdList( ).isEmpty( ) ) 
        {
            for (Integer unitId : filterParameters.getUserUnitIdList( )) 
            {
                daoUtil.setInt(i++, unitId );
            }
        }
        
        if ( filterParameters.getNumberOfDays( ) > 0 )
            daoUtil.setInt( i++, filterParameters.getNumberOfDays( ) );
        
        if ( filterParameters.getDirectoryId( ) > 0 ) {
            daoUtil.setInt( i++, filterParameters.getDirectoryId( ) );
            if ( filterParameters.getStateId( ) > 0 )
                daoUtil.setInt( i++, filterParameters.getStateId( ) );
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
     * {@inheritDoc}
     */
    @Override
    public List<RecordAssignment> selectRecordAssignmentsByRecordId( int nIdRecord, Plugin plugin )
    {
        List<RecordAssignment> listRecordAssignments = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_RECORD, plugin );
        daoUtil.setInt( 1, nIdRecord );
        daoUtil.executeQuery( );

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
        int nIndex = 1;

        RecordAssignment recordAssignment = new RecordAssignment( );
        recordAssignment.setId( daoUtil.getInt( nIndex++ ) );
        recordAssignment.setIdRecord( daoUtil.getInt( nIndex++ ) );

        recordAssignment.getAssignorUnit( ).setIdUnit( daoUtil.getInt( nIndex++ ) );
        recordAssignment.getAssignedUnit( ).setIdUnit( daoUtil.getInt( nIndex++ ) );
        recordAssignment.setAssignmentDate( daoUtil.getTimestamp( nIndex++ ) );
        recordAssignment.setAssignmentType( AssignmentType.getFromCode( daoUtil.getString( nIndex++ ) ) );
        recordAssignment.setActive( daoUtil.getBoolean( nIndex++ ) );

        recordAssignment.getAssignorUnit( ).setLabel( daoUtil.getString( nIndex++ ) );
        recordAssignment.getAssignorUnit( ).setDescription( daoUtil.getString( nIndex++ ) );
        recordAssignment.getAssignedUnit( ).setLabel( daoUtil.getString( nIndex++ ) );
        recordAssignment.getAssignedUnit( ).setDescription( daoUtil.getString( nIndex++ ) );

        return recordAssignment;
    }

}
