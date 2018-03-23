/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for ReassignmentResourceHistory objects
 */
public final class ReassignmentResourceHistoryDAO implements IReassignmentResourceHistoryDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_reassignment_resource_history, id_resource, resource_type, content, creation_date FROM workflow_directorydemands_reassignment_resource_history WHERE id_reassignment_resource_history = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_directorydemands_reassignment_resource_history ( id_resource, resource_type, content, creation_date ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_directorydemands_reassignment_resource_history WHERE id_reassignment_resource_history = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_directorydemands_reassignment_resource_history SET id_reassignment_resource_history = ?, id_resource = ?, resource_type = ?, content = ?, creation_date = ? WHERE id_reassignment_resource_history = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_reassignment_resource_history, id_resource, resource_type, content, creation_date FROM workflow_directorydemands_reassignment_resource_history";
    private static final String SQL_QUERY_SELECTALL_BY_RESOURCE = SQL_QUERY_SELECTALL + " WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_reassignment_resource_history FROM workflow_directorydemands_reassignment_resource_history";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( ReassignmentResourceHistory reassignmentResourceHistory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, reassignmentResourceHistory.getIdResource( ) );
            daoUtil.setString( nIndex++, reassignmentResourceHistory.getResourceType( ) );
            daoUtil.setString( nIndex++, reassignmentResourceHistory.getContent( ) );
            daoUtil.setTimestamp( nIndex++, reassignmentResourceHistory.getCreationDate( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                reassignmentResourceHistory.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReassignmentResourceHistory load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        ReassignmentResourceHistory reassignmentResourceHistory = null;

        if ( daoUtil.next( ) )
        {
            reassignmentResourceHistory = new ReassignmentResourceHistory( );
            int nIndex = 1;

            reassignmentResourceHistory.setId( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setIdResource( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setResourceType( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setContent( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
        }

        daoUtil.free( );
        return reassignmentResourceHistory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( ReassignmentResourceHistory reassignmentResourceHistory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, reassignmentResourceHistory.getId( ) );
        daoUtil.setInt( nIndex++, reassignmentResourceHistory.getIdResource( ) );
        daoUtil.setString( nIndex++, reassignmentResourceHistory.getResourceType( ) );
        daoUtil.setString( nIndex++, reassignmentResourceHistory.getContent( ) );
        daoUtil.setTimestamp( nIndex++, reassignmentResourceHistory.getCreationDate( ) );
        daoUtil.setInt( nIndex, reassignmentResourceHistory.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<ReassignmentResourceHistory> selectReassignmentResourceHistorysList( Plugin plugin )
    {
        List<ReassignmentResourceHistory> reassignmentResourceHistoryList = new ArrayList<ReassignmentResourceHistory>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ReassignmentResourceHistory reassignmentResourceHistory = new ReassignmentResourceHistory( );
            int nIndex = 1;

            reassignmentResourceHistory.setId( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setIdResource( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setResourceType( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setContent( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );

            reassignmentResourceHistoryList.add( reassignmentResourceHistory );
        }

        daoUtil.free( );
        return reassignmentResourceHistoryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdReassignmentResourceHistorysList( Plugin plugin )
    {
        List<Integer> reassignmentResourceHistoryList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            reassignmentResourceHistoryList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return reassignmentResourceHistoryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectReassignmentResourceHistorysReferenceList( Plugin plugin )
    {
        ReferenceList reassignmentResourceHistoryList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            reassignmentResourceHistoryList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return reassignmentResourceHistoryList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReassignmentResourceHistory> selectReassignmentResourceHistorysListByResource( int nIdResource, String strResourceType, Plugin plugin )
    {
        List<ReassignmentResourceHistory> reassignmentResourceHistoryList = new ArrayList<ReassignmentResourceHistory>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_RESOURCE, plugin );
        daoUtil.setInt( 1, nIdResource );
        daoUtil.setString( 2, strResourceType );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ReassignmentResourceHistory reassignmentResourceHistory = new ReassignmentResourceHistory( );
            int nIndex = 1;

            reassignmentResourceHistory.setId( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setIdResource( daoUtil.getInt( nIndex++ ) );
            reassignmentResourceHistory.setResourceType( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setContent( daoUtil.getString( nIndex++ ) );
            reassignmentResourceHistory.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );

            reassignmentResourceHistoryList.add( reassignmentResourceHistory );
        }

        daoUtil.free( );
        return reassignmentResourceHistoryList;
    }
}
