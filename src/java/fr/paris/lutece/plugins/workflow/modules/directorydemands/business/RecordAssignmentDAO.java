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

    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM workflow_directorydemands_record_assignment";
    private static final String SQL_QUERY_SELECT = "SELECT id, id_record, id_assignee_unit, id_assignee_user, id_assigner_unit, id_assigner_user, assignment_date, assignment_type FROM workflow_directorydemands_record_assignment WHERE id = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_directorydemands_record_assignment ( id, id_record, id_assignee_unit, id_assignee_user, id_assigner_unit, id_assigner_user, assignment_date, assignment_type ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_directorydemands_record_assignment WHERE id = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_directorydemands_record_assignment SET id = ?, id_record = ?, id_assignee_unit = ?, id_assignee_user = ?, id_assigner_unit = ?, id_assigner_user = ?, assignment_date = ?, assignment_type = ? WHERE id = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, id_record, id_assignee_unit, id_assignee_user, id_assigner_unit, id_assigner_user, assignment_date, assignment_type FROM workflow_directorydemands_record_assignment";

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

        daoUtil.setInt( 1, recordAssignment.getId( ) );
        daoUtil.setInt( 2, recordAssignment.getIdRecord( ) );
        daoUtil.setInt( 3, recordAssignment.getIdAssigneeUnit( ) );
        daoUtil.setInt( 4, recordAssignment.getIdAssigneeUser( ) );
        daoUtil.setInt( 5, recordAssignment.getIdAssignerUnit( ) );
        daoUtil.setInt( 6, recordAssignment.getIdAssignerUser( ) );
        daoUtil.setTimestamp( 7, recordAssignment.getAssignmentDate( ) );
        daoUtil.setString( 8, recordAssignment.getAssignmentType( ).getAssignmentTypeCode( ) );

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
            recordAssignment = new RecordAssignment( );
            int nIndex = 0;
            recordAssignment.setId( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setIdRecord( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setIdAssigneeUnit( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setIdAssigneeUser( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setIdAssignerUnit( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setIdAssignerUser( daoUtil.getInt( ++nIndex ) );
            recordAssignment.setAssignmentDate( daoUtil.getTimestamp( ++nIndex ) );
            recordAssignment.setAssignmentType( AssignmentType.getFromCode( daoUtil.getString( ++nIndex ) ) );
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
    public void store( RecordAssignment recordAssignment, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 0;
        daoUtil.setInt( ++nIndex, recordAssignment.getId( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getIdRecord( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getIdAssigneeUnit( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getIdAssigneeUser( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getIdAssignerUnit( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getIdAssignerUser( ) );
        daoUtil.setTimestamp( ++nIndex, recordAssignment.getAssignmentDate( ) );
        daoUtil.setString( ++nIndex, recordAssignment.getAssignmentType( ).getAssignmentTypeCode( ) );
        daoUtil.setInt( ++nIndex, recordAssignment.getId( ) );

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
            RecordAssignment recordAssignment = new RecordAssignment( );

            recordAssignment.setId( daoUtil.getInt( 1 ) );
            recordAssignment.setIdRecord( daoUtil.getInt( 2 ) );
            recordAssignment.setIdAssigneeUnit( daoUtil.getInt( 3 ) );
            recordAssignment.setIdAssigneeUser( daoUtil.getInt( 4 ) );
            recordAssignment.setIdAssignerUnit( daoUtil.getInt( 5 ) );
            recordAssignment.setIdAssignerUser( daoUtil.getInt( 6 ) );
            recordAssignment.setAssignmentDate( daoUtil.getTimestamp( 7 ) );
            recordAssignment.setAssignmentType( AssignmentType.getFromCode( daoUtil.getString( 8 ) ) );

            listRecordAssignments.add( recordAssignment );
        }

        daoUtil.free( );

        return listRecordAssignments;
    }

}