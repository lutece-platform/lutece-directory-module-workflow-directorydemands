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

package fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for {@link TaskInformation} objects
 */

public final class TaskInformationHome
{

    // Static variable pointed at the DAO instance
    private static ITaskInformationDAO _dao = SpringContextService.getBean( ITaskInformationDAO.BEAN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private TaskInformationHome( )
    {
    }

    /**
     * Create an instance of the {@code TaskInformation} class
     * 
     * @param taskInformation
     *            The instance of the {@code TaskInformation} which contains the informations to store
     * @return The instance of {@code taskInformation} which has been created with its primary key.
     */
    public static TaskInformation create( TaskInformation taskInformation )
    {
        _dao.insert( taskInformation, WorkflowDirectorydemandsPlugin.getPlugin( ) );

        return taskInformation;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a {@code TaskInformation} for the specified history id and task id
     * 
     * @param nIdHistory
     *            the history id
     * @param nIdTask
     *            the task id
     * @return an instance of {@code TaskInformation}
     */
    public static TaskInformation find( int nIdHistory, int nIdTask )
    {
        TaskInformation taskInformation = _dao.load( nIdHistory, nIdTask, WorkflowDirectorydemandsPlugin.getPlugin( ) );

        if ( taskInformation == null )
        {
            taskInformation = new TaskInformation( nIdHistory, nIdTask );
        }

        return taskInformation;
    }

}
