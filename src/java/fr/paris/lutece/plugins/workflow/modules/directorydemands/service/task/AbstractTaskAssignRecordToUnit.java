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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.AssignmentType;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.config.TaskAssignRecordToUnitConfig;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * This class is an abstract task to assign a record to a unit
 *
 */
public abstract class AbstractTaskAssignRecordToUnit extends AbstractTaskDirectoryDemands
{
    // Informations
    private static final String TASK_INFORMATION_ASSIGNED_UNIT = "ASSIGNED_UNIT";
    private static final String TASK_INFORMATION_ASSIGNOR_UNIT = "ASSIGNOR_UNIT";

    private static final int UNSET_ID = -1;

    // Services
    @Inject
    private IUnitService _unitService;
    @Inject
    @Named( "workflow-directorydemands.taskAssignRecordToUnitConfigService" )
    private ITaskConfigService _taskConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Record record = findRecordByIdHistory( nIdResourceHistory );

        if ( record != null )
        {
            try
            {
                IUnitSelection unitSelection = fetchUnitSelection( request );

                int nIdUnit = unitSelection.select( nIdResourceHistory, request, this );

                Unit unitAssigned = _unitService.getUnit( nIdUnit, false );

                if ( unitAssigned == null )
                {
                    throw new AppException( "The target unit does not exist" );
                }

                Unit unitAssignor = AssignmentService.findAssignorUnit( request );
                TaskAssignRecordToUnitConfig config = getConfig( );
                AssignmentType assignmentType = AssignmentType.getFromCode( config.getAssignmentType( ) );

                RecordAssignment recordAssignment = new RecordAssignment( );
                recordAssignment.setIdRecord( record.getIdRecord( ) );
                recordAssignment.setIdAssignedUnit( unitAssigned.getIdUnit( ) );

                if ( unitAssignor == null )
                {
                    recordAssignment.setIdAssignorUnit( UNSET_ID );
                }
                else
                {
                    recordAssignment.setIdAssignorUnit( unitAssignor.getIdUnit( ) );
                }

                recordAssignment.setAssignmentType( assignmentType );
                recordAssignment.setActive( true );
                RecordAssignmentHome.create( recordAssignment, WorkflowDirectorydemandsPlugin.getPlugin( ) );

                saveTaskInformation( nIdResourceHistory, unitAssigned, unitAssignor );
            }
            catch( AssignmentNotPossibleException e )
            {
                throw new AppException( e.getMessage( ), e );
            }
        }

    }

    /**
     * Gives the configuration associated to this task
     * 
     * @return the configuration
     */
    protected TaskAssignRecordToUnitConfig getConfig( )
    {
        return _taskConfigService.findByPrimaryKey( getId( ) );
    }

    /**
     * Saves the task information
     * 
     * @param nIdResourceHistory
     *            the resource history id
     * @param unitAssigned
     *            the assigned unit
     * @param unitAssignor
     *            the assignor unit
     */
    private void saveTaskInformation( int nIdResourceHistory, Unit unitAssigned, Unit unitAssignor )
    {
        TaskInformation taskInformation = new TaskInformation( nIdResourceHistory, getId( ) );
        taskInformation.add( TASK_INFORMATION_ASSIGNED_UNIT, unitAssigned.getLabel( ) );

        if ( unitAssignor != null )
        {
            taskInformation.add( TASK_INFORMATION_ASSIGNOR_UNIT, unitAssignor.getLabel( ) );
        }

        TaskInformationHome.create( taskInformation );
    }

    /**
     * Gives the unit selection to use
     * 
     * @param request
     *            the request which can contain information to fetch the unit selection
     * @return the unit selection
     */
    protected abstract IUnitSelection fetchUnitSelection( HttpServletRequest request );

}