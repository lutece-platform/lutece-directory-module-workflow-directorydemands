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

package fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.config;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for {@link UnitSelectionFromIdentityStoreConfig} objects
 */

public final class UnitSelectionFromIdentityStoreConfigDAO implements ITaskConfigDAO<UnitSelectionFromIdentityStoreConfig>
{

    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_task, id_directory_entry, ids_attribute_key FROM workflow_task_directorydemands_unit_selection_from_ids_cf WHERE id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_directorydemands_unit_selection_from_ids_cf ( id_task, id_directory_entry, ids_attribute_key ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_directorydemands_unit_selection_from_ids_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_directorydemands_unit_selection_from_ids_cf SET id_task = ?, id_directory_entry = ?, ids_attribute_key = ? WHERE id_task = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( UnitSelectionFromIdentityStoreConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, WorkflowDirectorydemandsPlugin.getPlugin( ) );

        objectToData( config, daoUtil );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public UnitSelectionFromIdentityStoreConfig load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, WorkflowDirectorydemandsPlugin.getPlugin( ) );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        UnitSelectionFromIdentityStoreConfig config = null;

        if ( daoUtil.next( ) )
        {
            config = new UnitSelectionFromIdentityStoreConfig( );
            int nIndex = 0;
            config.setIdTask( daoUtil.getInt( ++nIndex ) );
            config.setIdDirectoryEntry( daoUtil.getInt( ++nIndex ) );
            config.setIdentityStoreAttributeKey( daoUtil.getString( ++nIndex ) );
        }

        daoUtil.free( );

        return config;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nConfigId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, WorkflowDirectorydemandsPlugin.getPlugin( ) );
        daoUtil.setInt( 1, nConfigId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( UnitSelectionFromIdentityStoreConfig config )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, WorkflowDirectorydemandsPlugin.getPlugin( ) );

        int nIndex = objectToData( config, daoUtil );
        daoUtil.setInt( ++nIndex, config.getIdTask( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Sets the specified {@code UnitSelectionFromIdentityStoreConfig} object into the specified {@code DAOUtil}
     * 
     * @param config
     *            the {@code UnitSelectionFromIdentityStoreConfig} object to set
     * @param daoUtil
     *            the {@code DAOUtil}
     * @return the index of the last data set into the {@code DAOUtil}
     */
    private int objectToData( UnitSelectionFromIdentityStoreConfig config, DAOUtil daoUtil )
    {
        int nIndex = 0;
        daoUtil.setInt( ++nIndex, config.getIdTask( ) );
        daoUtil.setInt( ++nIndex, config.getIdDirectoryEntry( ) );
        daoUtil.setString( ++nIndex, config.getIdentityStoreAttributeKey( ) );

        return nIndex;
    }

}
