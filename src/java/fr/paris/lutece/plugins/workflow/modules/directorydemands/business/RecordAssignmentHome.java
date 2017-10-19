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

import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for RecordAssignment objects
 */

public final class RecordAssignmentHome
{
    private static final Plugin _plugin = WorkflowDirectorydemandsPlugin.getPlugin( );

    // Static variable pointed at the DAO instance

    private static IRecordAssignmentDAO _dao = (IRecordAssignmentDAO) SpringContextService.getBean( "workflow-directorydemands.recordAssignmentDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */

    private RecordAssignmentHome( )
    {
    }

    /**
     * Create an instance of the recordAssignment class
     * 
     * @param recordAssignment
     *            The instance of the RecordAssignment which contains the informations to store
     * @return The instance of recordAssignment which has been created with its primary key.
     */

    public static RecordAssignment create( RecordAssignment recordAssignment )
    {
        _dao.insert( recordAssignment, _plugin );

        return recordAssignment;
    }

    /**
     * Update of the recordAssignment which is specified in parameter
     * 
     * @param recordAssignment
     *            The instance of the RecordAssignment which contains the data to store
     * @return The instance of the recordAssignment which has been updated
     */

    public static RecordAssignment update( RecordAssignment recordAssignment )
    {
        _dao.store( recordAssignment, _plugin );

        return recordAssignment;
    }

    /**
     * Remove the recordAssignment whose identifier is specified in parameter
     * 
     * @param nRecordAssignmentId
     *            The recordAssignment Id
     */

    public static void remove( int nRecordAssignmentId )
    {
        _dao.delete( nRecordAssignmentId, _plugin );
    }

    /**
     * Remove the recordAssignment whose identifier is specified in parameter
     * 
     * @param recordAssignment
     *            The recordAssignment Id
     */
    // TODO : rename this method
    public static void desactivate( RecordAssignment recordAssignment )
    {
        _dao.desactivate( recordAssignment, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a recordAssignment whose identifier is specified in parameter
     * 
     * @param nKey
     *            The recordAssignment primary key
     * @return an instance of RecordAssignment
     */

    public static RecordAssignment findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns the last record assignment for the specified couple {id record, assignment type}
     * 
     * @param nIdRecord
     *            The record id
     * @param assignmentType
     *            The assignment type
     * @return the last record assignment
     */
    public static RecordAssignment findLast( int nIdRecord, AssignmentType assignmentType )
    {
        return _dao.loadLast( nIdRecord, assignmentType, _plugin );
    }

    /**
     * Load the data of all the recordAssignment objects and returns them in form of a list
     * 
     * @return the list which contains the data of all the recordAssignment objects
     */

    public static List<RecordAssignment> getRecordAssignmentsList( )
    {
        return _dao.selectRecordAssignmentsList( _plugin );
    }

    /**
     * Load the data of all the recordAssignment objects and returns them in form of a list
     * 
     * @param map
     *            the filtering criterias (see IRecordAssignmentDAO.selectRecordAssignmentsFiltredList() javadoc)
     * @return the list which contains the data of all the recordAssignment objects
     */

    public static List<RecordAssignment> getRecordAssignmentsFiltredList( HashMap<String, Integer> map )
    {
        return _dao.selectRecordAssignmentsFiltredList( map, _plugin );
    }

    /**
     * Loads the record assignment associated to the specified record if
     * 
     * @param nIdRecord
     *            the record id
     * @return the list of record assignments
     */
    public static List<RecordAssignment> findRecordAssignmentsByRecordId( int nIdRecord )
    {
        return _dao.selectRecordAssignmentsByRecordId( nIdRecord, _plugin );
    }
}
