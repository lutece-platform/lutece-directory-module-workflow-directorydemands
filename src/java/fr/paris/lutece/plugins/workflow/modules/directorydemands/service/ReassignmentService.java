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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service;

import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.unittree.modules.gra.exception.UnitCodeNotFoundException;
import fr.paris.lutece.plugins.unittree.modules.gra.service.IUnitCodeService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.ReassignmentResourceHistory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.ReassignmentResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.RecordUserAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignmentType;
import fr.paris.lutece.plugins.workflow.modules.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

/**
 * This class provides methods to reassignate resource to units
 */
public class ReassignmentService
{
    @Inject
    @Named( "workflow-directorydemands.identitystore.service" )
    private IdentityService _identityService;
    @Inject
    private IUnitCodeService _unitCodeService;

    // Properties
    private static final String PROPERTY_ENTRY_TITLE_CONNECTION_ID = AppPropertiesService.getProperty(
            "workflow-directorydemands.unit.selection.from.identitystore.directory.entry.title", StringUtils.EMPTY );
    private static final String PROPERTY_IDS_APPLICATION_CODE = AppPropertiesService.getProperty(
            "workflow-directorydemands.unit.selection.from.identitystore.applicationCode", StringUtils.EMPTY );
    private static final String PROPERTY_IDS_ASSIGNMENT_ATTRIBUTE = AppPropertiesService.getProperty(
            "workflow-directorydemands.unit.selection.from.identitystore.identitystore.attribute.key", StringUtils.EMPTY );

    /**
     * Reassign all the records created by given strConnectionId to new units; based on identity service attribute value
     * 
     * @param strConnectionId
     *            the connection id
     * @throws AssignmentNotPossibleException
     *             the exception is throws if the attribute value cannot be found in the units code
     */
    public void reassignRecordsByConnectionId( String strConnectionId ) throws AssignmentNotPossibleException
    {

        // First get all the record ids created by the given user connection id
        EntryFilter entryFilter = new EntryFilter( );
        entryFilter.setIsComment( EntryFilter.FILTER_FALSE );
        List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, WorkflowDirectorydemandsPlugin.getPlugin( ) );
        listEntries = listEntries.stream( ).filter( entry -> PROPERTY_ENTRY_TITLE_CONNECTION_ID.equals( entry.getTitle( ) ) ).collect( Collectors.toList( ) );

        Set<Integer> listRecordIdOfUser = new HashSet<>( );
        for ( IEntry entry : listEntries )
        {
            RecordFieldFilter filter = new RecordFieldFilter( );
            filter.setIdEntry( entry.getIdEntry( ) );
            List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( filter, WorkflowDirectorydemandsPlugin.getPlugin( ) );
            listRecordFields = listRecordFields.stream( ).filter( recordField -> recordField.getValue( ).equals( strConnectionId ) )
                    .collect( Collectors.toList( ) );

            listRecordIdOfUser
                    .addAll( listRecordFields.stream( ).map( recordField -> recordField.getRecord( ).getIdRecord( ) ).collect( Collectors.toList( ) ) );
        }

        // Get the new user unit code
        IdentityDto identity = _identityService.getIdentity( strConnectionId, null, PROPERTY_IDS_APPLICATION_CODE );
        AttributeDto attributeDto = null;

        // Get the next assigned unit for the user
        Integer nIdUnit = null;
        try
        {
            attributeDto = identity.getAttributes( ).get( PROPERTY_IDS_ASSIGNMENT_ATTRIBUTE );
            if ( attributeDto == null )
            {
                AppLogService.error( "Identity with connection id " + strConnectionId + " has no assignment attribute filled" );
                throw new AssignmentNotPossibleException( );
            }

            nIdUnit = _unitCodeService.getIdUnitFromUnitCode( attributeDto.getValue( ) );
            Unit newAssignedUnit = UnitHome.findByPrimaryKey( nIdUnit );

            // Process the reassignment of each record
            for ( int nIdRecord : listRecordIdOfUser )
            {
                // Deactivate the record assignments for the current record
                UnitAssignmentHome.deactivateByResource( nIdRecord, Record.WORKFLOW_RESOURCE_TYPE );

                // Remove the personnal assignment of the resource
                Record record = new Record( );
                record.setIdRecord( nIdRecord );
                RecordUserAssignmentHome.remove( record );

                // Create the user assignment
                UnitAssignment unitAssignment = new UnitAssignment( );
                unitAssignment.setActive( true );
                unitAssignment.setAssignedUnit( newAssignedUnit );
                unitAssignment.setAssignmentType( UnitAssignmentType.CREATION );
                unitAssignment.setAssignmentDate( new Timestamp( System.currentTimeMillis( ) ) );
                unitAssignment.setResourceType( Record.WORKFLOW_RESOURCE_TYPE );
                unitAssignment.setIdResource( nIdRecord );
                UnitAssignmentHome.create( unitAssignment );

                // Insert reassignment history
                ReassignmentResourceHistory resourceHistory = new ReassignmentResourceHistory( );
                resourceHistory.setContent( newAssignedUnit.getLabel( ) );
                resourceHistory.setCreationDate( new Timestamp( System.currentTimeMillis( ) ) );
                resourceHistory.setIdResource( record.getIdRecord( ) );
                resourceHistory.setResourceType( Record.WORKFLOW_RESOURCE_TYPE );
                ReassignmentResourceHistoryHome.create( resourceHistory );
            }

        }
        catch( UnitCodeNotFoundException e )
        {
            AppLogService.error( "Unable to find unit for the unit code " + attributeDto.getValue( ), e );
            throw new AssignmentNotPossibleException( );
        }
    }
}
