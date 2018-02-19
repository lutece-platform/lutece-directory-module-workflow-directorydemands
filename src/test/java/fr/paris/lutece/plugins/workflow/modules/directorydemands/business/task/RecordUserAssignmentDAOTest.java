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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.util.IdGenerator;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RecordUserAssignmentDAOTest extends LuteceTestCase
{
    private static final String SQL_QUERY_SELECT_USERS = "SELECT id_user FROM workflow_directorydemands_record_user_assignment WHERE id_record = ?";
    private static final String SQL_QUERY_SELECT_RECORDS = "SELECT id_record FROM workflow_directorydemands_record_user_assignment WHERE id_user = ?";

    private static final Plugin _plugin = WorkflowDirectorydemandsPlugin.getPlugin( );

    private IRecordUserAssignmentDAO _dao = new RecordUserAssignmentDAO( );

    public void testAssignOneRecordToOneUser( )
    {
        Record record = createRecord( );
        AdminUser user = createUser( );

        createRecordUserAssignement( record, user );

        List<Integer> listUserIdsAssignedToRecord = findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );

        removeRecordUserAssignement( record );
    }

    private Record createRecord( )
    {
        Record record = new Record( );
        record.setIdRecord( IdGenerator.generateId( ) );

        return record;
    }

    private AdminUser createUser( )
    {
        AdminUser user = new AdminUser( );
        user.setUserId( IdGenerator.generateId( ) );

        return user;
    }

    private void createRecordUserAssignement( Record record, AdminUser user )
    {
        _dao.insert( record, user, _plugin );
    }

    public List<Integer> findUserIdsAssignedTo( Record record )
    {
        List<Integer> listUserIds = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS, _plugin );
        daoUtil.setInt( 1, record.getIdRecord( ) );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listUserIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listUserIds;
    }

    public List<Integer> findRecordIdsAssignedTo( AdminUser user )
    {
        List<Integer> listRecordIds = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RECORDS, _plugin );
        daoUtil.setInt( 1, user.getUserId( ) );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listRecordIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listRecordIds;
    }

    private void removeRecordUserAssignement( Record record )
    {
        _dao.delete( record, _plugin );
    }

    public void testAssignTwoRecordsToOneUser( )
    {
        Record record1 = createRecord( );
        Record record2 = createRecordWithIncrementedId( record1 );
        AdminUser user = createUser( );

        createRecordUserAssignement( record1, user );
        createRecordUserAssignement( record2, user );

        List<Integer> listUserIdsAssignedToRecord = findUserIdsAssignedTo( record1 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        listUserIdsAssignedToRecord = findUserIdsAssignedTo( record2 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 2 ) );

        removeRecordUserAssignement( record1 );
        removeRecordUserAssignement( record2 );
    }

    private Record createRecordWithIncrementedId( Record record )
    {
        Record recordNew = new Record( );
        recordNew.setIdRecord( record.getIdRecord( ) + 1 );

        return recordNew;
    }

    public void testAssignOneRecordToTwoUser( )
    {
        Record record = createRecord( );
        AdminUser user1 = createUser( );
        AdminUser user2 = createUserWithIncrementedId( user1 );

        try
        {
            createRecordUserAssignement( record, user1 );
            createRecordUserAssignement( record, user2 );
            fail( "Expected an exception to be thrown" );
        }
        catch( Exception e )
        {
            // Correct behavior
        }

        List<Integer> listUserIdsAssignedToRecord = findUserIdsAssignedTo( record );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );
        listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user2 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 0 ) );

        removeRecordUserAssignement( record );
    }

    private AdminUser createUserWithIncrementedId( AdminUser user )
    {
        AdminUser userNew = new AdminUser( );
        userNew.setUserId( user.getUserId( ) + 1 );

        return userNew;
    }

    public void testAssignOneRecordToTwoUsersEach( )
    {
        Record record1 = createRecord( );
        Record record2 = createRecordWithIncrementedId( record1 );
        AdminUser user1 = createUser( );
        AdminUser user2 = createUserWithIncrementedId( user1 );

        createRecordUserAssignement( record1, user1 );
        createRecordUserAssignement( record2, user2 );

        List<Integer> listUserIdsAssignedToRecord = findUserIdsAssignedTo( record1 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        listUserIdsAssignedToRecord = findUserIdsAssignedTo( record2 );
        assertThat( listUserIdsAssignedToRecord.size( ), is( 1 ) );
        List<Integer> listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user1 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );
        listRecordIdsAssignedToUser = findRecordIdsAssignedTo( user2 );
        assertThat( listRecordIdsAssignedToUser.size( ), is( 1 ) );

        removeRecordUserAssignement( record1 );
        removeRecordUserAssignement( record2 );
    }

    public void testRemoveRecordUserAssignement( )
    {
        Record record = createRecord( );
        AdminUser user = createUser( );

        createRecordUserAssignement( record, user );
        removeRecordUserAssignement( record );

        List<Integer> listUserIdsAssignedToRecord = findUserIdsAssignedTo( record );

        assertThat( listUserIdsAssignedToRecord.size( ), is( 0 ) );
    }

}
