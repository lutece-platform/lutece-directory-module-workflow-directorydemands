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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.AssignmentType;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.AssignmentService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.web.task.AssignUpRecordTaskComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * This class represents a workflow task to reply to an assigned up record
 *
 */
public class TaskAssignDownRecord extends AbstractTaskDirectoryDemands
{
    // Messages
    private static final String MESSAGE_TASK_ASSIGN_DOWN = "module.workflow.directorydemands.task_assign_down_record.title";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_ASSIGN_DOWN, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Record record = findRecordByIdHistory( nIdResourceHistory );
        
        if ( record != null )
        {
            //First Desactivate previous record assignment
            RecordAssignment recordAssignment = RecordAssignmentHome.findLast( record.getIdRecord( ), AssignmentType.ASSIGN_UP,
                    WorkflowDirectorydemandsPlugin.getPlugin( ) );

            RecordAssignmentHome.desactivate( recordAssignment, WorkflowDirectorydemandsPlugin.getPlugin( ) );
            int nIdAssignorUnit = recordAssignment.getIdAssignorUnit( );
            int nIdAssignedUnit = recordAssignment.getIdAssignedUnit( );
            
            //Then add a new recordassignment for the assign_down action.
            recordAssignment = new RecordAssignment( );
            recordAssignment.setIdAssignedUnit( nIdAssignorUnit );
            recordAssignment.setIdAssignorUnit( nIdAssignedUnit );
            recordAssignment.setAssignmentType( AssignmentType.ASSIGN_DOWN );
            recordAssignment.setActive( true );
            RecordAssignmentHome.create( recordAssignment, WorkflowDirectorydemandsPlugin.getPlugin( ) );
            
        }
    }

}
