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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.plugins.directory.business.MockRecord;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentDAOTest;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.MockAdminUser;
import fr.paris.lutece.test.LuteceTestCase;

public class TaskRemoveRecordAssignmentTest extends LuteceTestCase
{
    private static final Locale _locale = Locale.getDefault( );

    private AdminUser _user;
    private TaskRemoveRecordAssignment _task;
    private MockHttpServletRequest _request;
    private RecordUserAssignmentDAOTest _daoTest;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _user = MockAdminUser.insertUserInDatabase( );

        _task = new TaskRemoveRecordAssignment( );
        _request = new MockHttpServletRequest( );
        _daoTest = new RecordUserAssignmentDAOTest( );
    }

    public void testRemoveAssignmentWhenRecordIsNotAssigned( )
    {
        Record record = MockRecord.create( );

        _task.processTask( record, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 0 ) );
    }

    public void testRemoveAssignmentWhenRecordIsAssigned( )
    {
        Record record = MockRecord.create( );
        RecordUserAssignmentHome.create( record, _user );

        _task.processTask( record, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 0 ) );
    }

    public void testRemoveAssignmentPreserveOtherRecords( )
    {
        Record record1 = MockRecord.create( );
        Record record2 = MockRecord.create( );
        RecordUserAssignmentHome.create( record1, _user );
        RecordUserAssignmentHome.create( record2, _user );

        _task.processTask( record1, _request, _locale );

        List<Integer> listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record1 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 0 ) );
        listUserIdsAssignedToRecord = _daoTest.findUserIdsAssignedTo( record2 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = _daoTest.findRecordIdsAssignedTo( _user );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );
    }

    public void testGetTitle( )
    {
        assertThat( _task.getTitle( _locale ), is( notNullValue( ) ) );
    }
}
