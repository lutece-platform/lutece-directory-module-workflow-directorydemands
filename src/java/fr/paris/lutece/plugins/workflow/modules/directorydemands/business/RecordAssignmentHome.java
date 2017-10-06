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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for RecordAssignment objects
 */

public final class RecordAssignmentHome
{

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
     * @param plugin
     *            the Plugin
     * @return The instance of recordAssignment which has been created with its primary key.
     */

    public static RecordAssignment create( RecordAssignment recordAssignment, Plugin plugin )
    {
        _dao.insert( recordAssignment, plugin );

        return recordAssignment;
    }

    /**
     * Update of the recordAssignment which is specified in parameter
     * 
     * @param recordAssignment
     *            The instance of the RecordAssignment which contains the data to store
     * @param plugin
     *            the Plugin
     * @return The instance of the recordAssignment which has been updated
     */

    public static RecordAssignment update( RecordAssignment recordAssignment, Plugin plugin )
    {
        _dao.store( recordAssignment, plugin );

        return recordAssignment;
    }

    /**
     * Remove the recordAssignment whose identifier is specified in parameter
     * 
     * @param nRecordAssignmentId
     *            The recordAssignment Id
     * @param plugin
     *            the Plugin
     */

    public static void remove( int nRecordAssignmentId, Plugin plugin )
    {
        _dao.delete( nRecordAssignmentId, plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a recordAssignment whose identifier is specified in parameter
     * 
     * @param nKey
     *            The recordAssignment primary key
     * @param plugin
     *            the Plugin
     * @return an instance of RecordAssignment
     */

    public static RecordAssignment findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Load the data of all the recordAssignment objects and returns them in form of a list
     * 
     * @param plugin
     *            the Plugin
     * @return the list which contains the data of all the recordAssignment objects
     */

    public static List<RecordAssignment> getRecordAssignmentsList( Plugin plugin )
    {
        return _dao.selectRecordAssignmentsList( plugin );
    }

}
