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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.gra.exception.UnitCodeNotFoundException;
import fr.paris.lutece.plugins.unittree.modules.gra.service.IUnitCodeService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.config.UnitSelectionFromIdentityStoreConfig;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.unittree.service.task.selection.impl.AbstractEmptyConfigurationHandler;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class is a unit selection using IdentityStore
 *
 */
public class UnitSelectionFromIdentityStore implements IUnitSelection
{
    private static final String ID = "UnitSelectionFromIdS";

    // Services
    @Inject
    @Named( "workflow-directorydemands.identitystore.service" )
    private IdentityService _identityService;
    @Inject
    private IUnitCodeService _unitCodeService;
    @Inject
    private IUnitService _unitService;

    private final IConfigurationHandler _configurationHandler = new ConfigurationHandler( );
    private final ITaskFormHandler _taskFormHandler = new TaskFormHandler( );
    private final UnitSelectionFromIdentityStoreConfig _config = new UnitSelectionFromIdentityStoreConfig( );
    private final Plugin _plugin = WorkflowDirectorydemandsPlugin.getPlugin( );

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId( )
    {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutomatic( )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConfigurationHandler getConfigurationHandler( )
    {
        return _configurationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITaskFormHandler getTaskFormHandler( )
    {
        return _taskFormHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int select( int nIdResource, String strResourceType, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException
    {
        Record record = RecordHome.findByPrimaryKey( nIdResource, _plugin );
        String strConnectionId = findFieldValue( _config.getDirectoryEntryTitle( ), record );

        if ( StringUtils.isEmpty( strConnectionId ) )
        {
            throw new AssignmentNotPossibleException( "No value in the directory entry " + _config.getDirectoryEntryTitle( ) );
        }

        IdentityDto identity = _identityService.getIdentity( strConnectionId, null, _config.getApplicationCode( ) );
        AttributeDto attributeDto = identity.getAttributes( ).get( _config.getIdentityStoreAttributeKey( ) );

        if ( attributeDto == null )
        {
            throw new AssignmentNotPossibleException( "The identity attribute " + _config.getIdentityStoreAttributeKey( ) + " does not exist for the identity "
                    + identity.getConnectionId( ) );
        }

        String strUnitCode = attributeDto.getValue( );

        if ( StringUtils.isEmpty( strUnitCode ) )
        {
            throw new AssignmentNotPossibleException( "No value in the identity attribute " + _config.getIdentityStoreAttributeKey( ) );
        }

        int nIdUnit = 0;

        try
        {
            nIdUnit = _unitCodeService.getIdUnitFromUnitCode( strUnitCode );
        }
        catch( UnitCodeNotFoundException e )
        {
            throw new AssignmentNotPossibleException( "The identity attribute " + _config.getIdentityStoreAttributeKey( ) + " does not contain a valid unit", e );
        }

        return nIdUnit;
    }

    /**
     * Finds the field value of the record entry with the specified title
     * 
     * @param strEntryTitle
     *            the entry title
     * @param record
     *            the record
     * @return the field value
     * @throws AssignmentNotPossibleException
     *             if no entry with specified title exists
     */
    private String findFieldValue( String strEntryTitle, Record record ) throws AssignmentNotPossibleException
    {
        String strRecordFieldValue = StringUtils.EMPTY;

        if ( !StringUtils.isEmpty( strEntryTitle ) )
        {
            // RecordField
            EntryFilter entryFilter = new EntryFilter( );
            entryFilter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );

            List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, _plugin );
            boolean bEntryFound = false;

            for ( IEntry entry : listEntries )
            {
                if ( strEntryTitle.equals( entry.getTitle( ) ) )
                {
                    RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
                    recordFieldFilter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );
                    recordFieldFilter.setIdEntry( entry.getIdEntry( ) );
                    recordFieldFilter.setIdRecord( record.getIdRecord( ) );

                    List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilter, _plugin );

                    if ( ( listRecordFields != null ) && !listRecordFields.isEmpty( ) && ( listRecordFields.get( 0 ) != null ) )
                    {
                        RecordField recordFieldId = listRecordFields.get( 0 );
                        strRecordFieldValue = recordFieldId.getValue( );

                        if ( recordFieldId.getField( ) != null )
                        {
                            strRecordFieldValue = recordFieldId.getField( ).getTitle( );
                        }
                    }

                    bEntryFound = true;
                    break;
                }
            }

            if ( !bEntryFound )
            {
                throw new AssignmentNotPossibleException( "The directory entry " + strEntryTitle + " does not exist" );
            }
        }
        else
        {
            throw new AssignmentNotPossibleException( "The directory entry title is empty" );
        }

        return strRecordFieldValue;
    }

    /**
     * This class is a configuration handler for the {@link UnitSelectionFromIdentityStore} class
     *
     */
    private static class ConfigurationHandler extends AbstractEmptyConfigurationHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.directorydemands.unit_selection.from_ids.config.title";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }

    }

    /**
     * This class is a form handler for the {@link UnitSelectionFromIdentityStore} class
     *
     */
    private class TaskFormHandler implements ITaskFormHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.directorydemands.unit_selection.from_ids.form.title";

        // Markers
        private static final String MARK_ASSIGNED_UNIT = "assigned_unit";

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle( Locale locale )
        {
            return I18nService.getLocalizedString( MESSAGE_TITLE, locale );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayedForm( int nIdResource, String strResourceType, Locale locale, ITask task ) throws AssignmentNotPossibleException
        {
            Unit unitAssigned = _unitService.getUnit( select( nIdResource, strResourceType, null, task ), false );

            Map<String, Object> model = new HashMap<String, Object>( );

            model.put( MARK_ASSIGNED_UNIT, unitAssigned );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_AUTOMATIC_TASK_FORM, locale, model );

            return template.getHtml( );
        }

    }

}
