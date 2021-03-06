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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.resource.ResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class is an abstract task related to a {@link Record}
 */
public abstract class AbstractTaskRecordRelated extends SimpleTask
{
    // Constants
    private static final String NO_INFORMATION = "(no information)";

    // Services
    private static final IResourceHistoryService _resourceHistoryService = SpringContextService.getBean( ResourceHistoryService.BEAN_SERVICE );

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Record record = findRecordByIdHistory( nIdResourceHistory );

        processTask( record, request, locale );
        createTaskInformation( nIdResourceHistory );
    }

    /**
     * Finds a record by using the specified resource history identifier
     * 
     * @param nIdResourceHistory
     *            The resource history identifier
     * @return the found record or {@code null} if not found
     */
    private Record findRecordByIdHistory( int nIdResourceHistory )
    {
        Record record = null;
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

        if ( resourceHistory != null && Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource( ), WorkflowDirectorydemandsPlugin.getPlugin( ) );
        }

        return record;
    }

    /**
     * Creates a {@code TaskInformation} object
     * 
     * @param nIdResourceHistory
     *            The resource history identifier associated to the created {@code TaskInformation}
     */
    private void createTaskInformation( int nIdResourceHistory )
    {
        TaskInformation taskInformation = new TaskInformation( nIdResourceHistory, getId( ) );
        fillTaskInformation( taskInformation );
        manageNoInformation( taskInformation );
        TaskInformationHome.create( taskInformation );
    }

    /**
     * Manages the case there is no information in the specified {@code TaskInformation}
     * 
     * @param taskInformation
     *            The {@code TaskInformation}
     */
    private void manageNoInformation( TaskInformation taskInformation )
    {
        if ( taskInformation.getKeys( ).isEmpty( ) )
        {
            taskInformation.add( NO_INFORMATION, NO_INFORMATION );
        }
    }

    /**
     * Process the task
     * 
     * @param record
     *            The record on which the task is processed
     * @param request
     *            The request used to process the task
     * @param locale
     *            The locale used to process the task
     */
    protected abstract void processTask( Record record, HttpServletRequest request, Locale locale );

    /**
     * Fills the specified {@code TaskInformation}
     * 
     * @param taskInformation
     *            The {@code TaskInformation} to fill
     */
    protected abstract void fillTaskInformation( TaskInformation taskInformation );
}
