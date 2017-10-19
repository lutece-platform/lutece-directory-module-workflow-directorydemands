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
import java.util.HashMap;
import java.util.List;

/**
 * IRecordAssignmentDAO Interface
 */

public interface IRecordAssignmentDAO
{

    /**
     * Insert a new record in the table.
     * 
     * @param recordAssignment
     *            instance of the RecordAssignment object to inssert
     * @param plugin
     *            the Plugin
     */

    void insert( RecordAssignment recordAssignment, Plugin plugin );

    /**
     * Update the record in the table
     * 
     * @param recordAssignment
     *            the reference of the RecordAssignment
     * @param plugin
     *            the Plugin
     */

    void store( RecordAssignment recordAssignment, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nIdRecordAssignment
     *            int identifier of the RecordAssignment to delete
     * @param plugin
     *            the Plugin
     */

    void delete( int nIdRecordAssignment, Plugin plugin );

    /**
     * Desactivate a record from the table
     * 
     * @param recordAssignment
     *            int identifier of the RecordAssignment to desactivate
     * @param plugin
     *            the Plugin
     */
    
    void desactivate( RecordAssignment recordAssignment, Plugin plugin );
    
    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param nKey
     *            The identifier of the recordAssignment
     * @param plugin
     *            the Plugin
     * @return The instance of the recordAssignment
     */

    RecordAssignment load( int nKey, Plugin plugin );

    /**
     * Loads the last record assignment for the specified couple {id record, assignment type}
     * 
     * @param nIdRecord
     *            The record id
     * @param assignmentType
     *            The assignment type
     * @param plugin
     *            the Plugin
     * @return The last record assignment
     */
    RecordAssignment loadLast( int nIdRecord, AssignmentType assignmentType, Plugin plugin );

    /**
     * Load the data of all the recordAssignment objects and returns them as a List
     * 
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the recordAssignment objects
     */
    List<RecordAssignment> selectRecordAssignmentsList( Plugin plugin );

    /**
     * Load the data of a filtred list of the recordAssignment objects and returns them as a List
     * 
     * FILTER MAP DEFINITION KEYS               
            * USER_UNIT_ID = assigned unit id (from unittree)
            * RECURSIVE_SEARCH_DEPTH = depth of recursive search of the children units of the assigned unit 
                    * 1 : no sub units
                    * 2 : sub units
                    * 3 : sub-sub units
            * ACTIVE_RECORDS = active records (1:active, 0:inactive)
            * FILTER_PERIOD = get only records created since N days 
                    * -1 : none
                    * N : records created since N day
            * DIRECTORY_ID = specify a particular directory to filter records
            * STATE_ID = specify a particular state to filter records
     *
     * 
     * @param filterParameters
     *              map of the fitlering parameters
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the recordAssignment objects
     */

    List<RecordAssignment> selectRecordAssignmentsFiltredList( HashMap<String,Integer> filterParameters, Plugin plugin );
    

    /**
     * Loads the record assignment associated to the specified record if
     * 
     * @param nIdRecord
     *            the record id
     * @param plugin
     *            the plugin
     * @return the list of record assignments
     */
    List<RecordAssignment> selectRecordAssignmentsByRecordId( int nIdRecord, Plugin plugin );

}
