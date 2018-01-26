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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentDAOTest;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.MockTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.information.TaskInformation;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.util.IdGenerator;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.test.LuteceTestCase;

public class TaskAssignRecordToCurrentUserTest extends LuteceTestCase
{
    private static final String TASK_INFORMATION_ASSIGNED_USER = "ASSIGNED_USER";

    private static final Locale _locale = Locale.getDefault( );

    private AdminUser _user1;
    private AdminUser _user2;
    private TaskAssignRecordToCurrentUser _task;
    private MockHttpServletRequest _request;
    private RecordUserAssignmentDAOTest _daoTest;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _user1 = createUser( );
        _user2 = createUser( );

        AdminUserHome.create( _user1 );
        AdminUserHome.create( _user2 );

        _task = new TaskAssignRecordToCurrentUser( );
        _request = new MockHttpServletRequest( );
        _daoTest = new RecordUserAssignmentDAOTest( );
    }

    private AdminUser createUser( )
    {
        int nId = IdGenerator.generateId( );
        String strTestValue = String.valueOf( nId );

        AdminUser user = new AdminUser( );
        user.setUserId( nId );
        user.setStatus( AdminUser.ACTIVE_CODE );
        user.setAccessCode( strTestValue );
        user.setLastName( strTestValue );
        user.setFirstName( strTestValue );
        user.setEmail( strTestValue );

        return user;
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        AdminUserHome.remove( _user1.getUserId( ) );
        AdminUserHome.remove( _user2.getUserId( ) );

        super.tearDown( );
    }

    public void testAssignOneRecordToOneUser( ) throws Exception
    {
        AdminAuthenticationService.getInstance( ).registerUser( _request, _user1 );
        Record record = createRecord( );

        _task.processTask( record, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );

        RecordUserAssignmentHome.remove( record );
    }

    private Record createRecord( )
    {
        Record record = new Record( );
        record.setIdRecord( IdGenerator.generateId( ) );

        return record;
    }

    public void testAssignTwoRecordToOneUser( ) throws Exception
    {
        AdminAuthenticationService.getInstance( ).registerUser( _request, _user1 );
        Record record1 = createRecord( );
        Record record2 = createRecord( );

        _task.processTask( record1, _request, _locale );
        _task.processTask( record2, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record1 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record2 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 2 ) );

        RecordUserAssignmentHome.remove( record1 );
        RecordUserAssignmentHome.remove( record2 );
    }

    public void testAssignOneRecordToTwoUser( ) throws Exception
    {
        AdminAuthenticationService.getInstance( ).registerUser( _request, _user1 );
        Record record = createRecord( );

        _task.processTask( record, _request, _locale );

        AdminAuthenticationService.getInstance( ).registerUser( _request, _user2 );
        _task.processTask( record, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 0 ) );
        listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user2 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );

        RecordUserAssignmentHome.remove( record );
    }

    public void testAssignOneRecordToTwoUserEach( ) throws Exception
    {
        AdminAuthenticationService.getInstance( ).registerUser( _request, _user1 );
        Record record1 = createRecord( );
        Record record2 = createRecord( );

        _task.processTask( record1, _request, _locale );

        AdminAuthenticationService.getInstance( ).registerUser( _request, _user2 );
        _task.processTask( record2, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record1 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record2 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );
        listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user2 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );

        RecordUserAssignmentHome.remove( record1 );
        RecordUserAssignmentHome.remove( record2 );
    }

    public void testTaskInformationAdded( ) throws Exception
    {
        AdminAuthenticationService.getInstance( ).registerUser( _request, _user1 );
        Record record = createRecord( );
        TaskInformation taskInformation = MockTaskInformation.createWithNoPieceOfInformation( );
        _task.processTask( record, _request, _locale );

        _task.fillTaskInformation( taskInformation );

        assertThatTaskInformationContainsTheUserName( taskInformation, _user1 );

        RecordUserAssignmentHome.remove( record );
    }

    public void assertThatTaskInformationContainsTheUserName( TaskInformation taskInformation, AdminUser user )
    {
        String strUserName = user.getFirstName( ) + " " + user.getLastName( );
        String strUserNameFromTaskInformation = taskInformation.getValue( TASK_INFORMATION_ASSIGNED_USER );

        assertThat( strUserNameFromTaskInformation, is( strUserName ) );
    }

    public void testGetTitle( )
    {
        assertThat( _task.getTitle( _locale ), is( notNullValue( ) ) );
    }
}
