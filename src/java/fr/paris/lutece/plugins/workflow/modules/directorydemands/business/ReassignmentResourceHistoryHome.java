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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for ReassignmentResourceHistory objects
 */
public final class ReassignmentResourceHistoryHome
{
    // Static variable pointed at the DAO instance
    private static IReassignmentResourceHistoryDAO _dao = SpringContextService.getBean( "workflow-directorydemands.reassignmentResourceHistoryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "workflow-directorydemands" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReassignmentResourceHistoryHome( )
    {
    }

    /**
     * Create an instance of the reassignmentResourceHistory class
     * 
     * @param reassignmentResourceHistory
     *            The instance of the ReassignmentResourceHistory which contains the informations to store
     * @return The instance of reassignmentResourceHistory which has been created with its primary key.
     */
    public static ReassignmentResourceHistory create( ReassignmentResourceHistory reassignmentResourceHistory )
    {
        _dao.insert( reassignmentResourceHistory, _plugin );

        return reassignmentResourceHistory;
    }

    /**
     * Update of the reassignmentResourceHistory which is specified in parameter
     * 
     * @param reassignmentResourceHistory
     *            The instance of the ReassignmentResourceHistory which contains the data to store
     * @return The instance of the reassignmentResourceHistory which has been updated
     */
    public static ReassignmentResourceHistory update( ReassignmentResourceHistory reassignmentResourceHistory )
    {
        _dao.store( reassignmentResourceHistory, _plugin );

        return reassignmentResourceHistory;
    }

    /**
     * Remove the reassignmentResourceHistory whose identifier is specified in parameter
     * 
     * @param nKey
     *            The reassignmentResourceHistory Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a reassignmentResourceHistory whose identifier is specified in parameter
     * 
     * @param nKey
     *            The reassignmentResourceHistory primary key
     * @return an instance of ReassignmentResourceHistory
     */
    public static ReassignmentResourceHistory findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the reassignmentResourceHistory objects and returns them as a list
     * 
     * @return the list which contains the data of all the reassignmentResourceHistory objects
     */
    public static List<ReassignmentResourceHistory> getReassignmentResourceHistorysList( )
    {
        return _dao.selectReassignmentResourceHistorysList( _plugin );
    }

    /**
     * Load the id of all the reassignmentResourceHistory objects and returns them as a list
     * 
     * @return the list which contains the id of all the reassignmentResourceHistory objects
     */
    public static List<Integer> getIdReassignmentResourceHistorysList( )
    {
        return _dao.selectIdReassignmentResourceHistorysList( _plugin );
    }

    /**
     * Load the data of all the reassignmentResourceHistory objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the reassignmentResourceHistory objects
     */
    public static ReferenceList getReassignmentResourceHistorysReferenceList( )
    {
        return _dao.selectReassignmentResourceHistorysReferenceList( _plugin );
    }

    /**
     * Load the data of all the reassignmentResourceHistory objects based on resource id and resource type and return them as a list
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @return the list which contains the data of all the reassignementResourceHistory based on given resource
     */
    public static List<ReassignmentResourceHistory> getReassignmentResourceHistorysListByResource( int nIdResource, String strResourceType )
    {
        return _dao.selectReassignmentResourceHistorysListByResource( nIdResource, strResourceType, _plugin );
    }
}
