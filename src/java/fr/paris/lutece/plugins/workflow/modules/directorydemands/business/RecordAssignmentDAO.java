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

/**
 * This class provides Data Access methods for RecordAssignment objects
 */

public final class RecordAssignmentDAO implements IRecordAssignmentDAO
{

    // Constants

    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM directory_record_unit_assignment";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, id_record, id_assigned_unit, id_assignor_unit, assignment_date, assignment_type, is_active FROM directory_record_unit_assignment";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE id = ?";
    private static final String SQL_QUERY_SELECT_LAST = SQL_QUERY_SELECTALL + " WHERE id_record = ? AND assignment_type = ? ORDER BY assignment_date DESC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO directory_record_unit_assignment ( id, id_record, id_assigned_unit, id_assignor_unit, assignment_date, assignment_type, is_active ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM directory_record_unit_assignment WHERE id = ? ";
    private static final String SQL_QUERY_DESACTIVATE = "UPDATE directory_record_unit_assignment SET is_active = 0 WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE directory_record_unit_assignment SET id = ?, id_record = ?, id_assigned_unit = ?, id_assignor_unit = ? , assignment_date = ?, assignment_type = ?, is_active = ? WHERE id = ?";

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
        daoUtil.setInt( nIndex++, recordAssignment.getIdAssignedUnit( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getIdAssignorUnit( ) );
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
        daoUtil.setInt( nIndex++, recordAssignment.getIdAssignedUnit( ) );
        daoUtil.setInt( nIndex++, recordAssignment.getIdAssignorUnit( ) );
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
        recordAssignment.setIdAssignedUnit( daoUtil.getInt( nIndex++ ) );
        recordAssignment.setIdAssignorUnit( daoUtil.getInt( nIndex++ ) );
        recordAssignment.setAssignmentDate( daoUtil.getTimestamp( nIndex++ ) );
        recordAssignment.setAssignmentType( AssignmentType.getFromCode( daoUtil.getString( nIndex++ ) ) );
        recordAssignment.setActive( daoUtil.getBoolean( nIndex ) );
        return recordAssignment;
    }

}
