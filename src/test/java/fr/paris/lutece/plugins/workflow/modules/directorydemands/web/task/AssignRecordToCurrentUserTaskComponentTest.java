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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.web.task;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.MockTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformationDAOTest;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.MockResourceHistory;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.MockTask;
import fr.paris.lutece.plugins.workflowcore.web.task.ITaskComponent;
import fr.paris.lutece.test.LuteceTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AssignRecordToCurrentUserTaskComponentTest extends LuteceTestCase
{
    private static final String TASK_INFORMATION_KEY = "ASSIGNED_USER";
    private static final String TASK_INFORMATION_VALUE = "assigned user";

    private ResourceHistory _resourceHistory;
    private ITask _task;
    private HttpServletRequest _request;
    private Locale _locale;
    private ITaskComponent _taskComponent;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _resourceHistory = MockResourceHistory.create( );
        _task = MockTask.create( );
        _request = new MockHttpServletRequest( );
        _locale = Locale.getDefault( );
        _taskComponent = new AssignRecordToCurrentUserTaskComponent( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
    }

    public void testTaskInformationTemplate( )
    {
        TaskInformation taskInformation = MockTaskInformation.createWithPiecesOfInformation( _resourceHistory, _task );
        taskInformation.add( TASK_INFORMATION_KEY, TASK_INFORMATION_VALUE );
        TaskInformationHome.create( taskInformation );

        String strDisplayedTaskInformation = getDisplayTaskInformation( );

        assertThat( strDisplayedTaskInformation, is( notNullValue( ) ) );

        TaskInformationDAOTest.removeFromDatabase( taskInformation );
    }

    public void testTaskInformationTemplateWithNoPieceOfInformation( )
    {
        String strDisplayedTaskInformation = getDisplayTaskInformation( );

        assertThat( strDisplayedTaskInformation, is( nullValue( ) ) );
    }

    private String getDisplayTaskInformation( )
    {
        return _taskComponent.getDisplayTaskInformation( _resourceHistory.getId( ), _request, _locale, _task );
    }
}
