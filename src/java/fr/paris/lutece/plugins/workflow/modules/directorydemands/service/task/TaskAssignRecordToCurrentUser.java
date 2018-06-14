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

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformation;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is a task to assign a record to the current user
 */
public class TaskAssignRecordToCurrentUser extends AbstractTaskRecordRelated
{
    // Constants
    private static final String TASK_INFORMATION_ASSIGNED_USER = "ASSIGNED_USER";
    private static final String USER_NAME_SEPARATOR = " ";

    // Messages
    private static final String MESSAGE_ASSIGN_RECORD_TO_CURRENT_USER = "module.workflow.directorydemands.task_assign_record_to_current_user.title";

    // Variables
    private AdminUser _user;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processTask( Record record, HttpServletRequest request, Locale locale )
    {
        _user = AdminUserService.getAdminUser( request );

        RecordUserAssignmentHome.remove( record );
        RecordUserAssignmentHome.create( record, _user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillTaskInformation( TaskInformation taskInformation )
    {
        String sbUserName = new StringBuilder( _user.getFirstName( ) ).append( USER_NAME_SEPARATOR ).append( _user.getLastName( ) ).toString( );

        taskInformation.add( TASK_INFORMATION_ASSIGNED_USER, sbUserName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_ASSIGN_RECORD_TO_CURRENT_USER, locale );
    }
}
