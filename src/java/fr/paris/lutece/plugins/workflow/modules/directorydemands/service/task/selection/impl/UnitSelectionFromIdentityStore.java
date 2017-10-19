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
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.config.UnitSelectionFromIdentityStoreConfig;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.exception.UnitCodeNotFoundException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.IUnitCodeService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.IConfigurationHandler;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.ITaskFormHandler;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.IUnitSelection;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.utils.Constants;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
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
    @Named( "workflow-directorydemands.unitSelectionFromIdentityStoreConfigService" )
    private ITaskConfigService _taskConfigService;
    @Inject
    @Named( "workflow-directorydemands.identitystore.service" )
    private IdentityService _identityService;
    @Inject
    private IUnitCodeService _unitCodeService;

    private final IConfigurationHandler _configurationHandler = new ConfigurationHandler( );
    private final ITaskFormHandler _taskFormHandler = new TaskFormHandler( );

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
    public int select( int nIdResource, HttpServletRequest request, ITask task ) throws AssignmentNotPossibleException
    {
        UnitSelectionFromIdentityStoreConfig config = findConfig( task );
        Record record = RecordHome.findByPrimaryKey( nIdResource, WorkflowDirectorydemandsPlugin.getPlugin( ) );
        String strConnectionId = findFieldValue( config.getIdDirectoryEntry( ), record );

        if ( StringUtils.isEmpty( strConnectionId ) )
        {
            throw new AssignmentNotPossibleException( "No value in the directory entry number " + config.getIdDirectoryEntry( ) );
        }

        IdentityDto identity = _identityService.getIdentity( strConnectionId, null, Constants.APPLICATION_CODE );
        AttributeDto attributeDto = identity.getAttributes( ).get( config.getIdentityStoreAttributeKey( ) );

        if ( attributeDto == null )
        {
            throw new AssignmentNotPossibleException( "The identity attribute " + config.getIdentityStoreAttributeKey( ) + "does not exist" );
        }

        String strUnitCode = attributeDto.getValue( );

        if ( StringUtils.isEmpty( strUnitCode ) )
        {
            throw new AssignmentNotPossibleException( "No value in the identity attribute " + config.getIdentityStoreAttributeKey( ) );
        }

        int nIdUnit = 0;

        try
        {
            nIdUnit = _unitCodeService.getIdUnitFromUnitCode( strUnitCode );
        }
        catch( UnitCodeNotFoundException e )
        {
            throw new AssignmentNotPossibleException( "The identity attribute " + config.getIdentityStoreAttributeKey( ) + "does not contain a valid unit", e );
        }

        return nIdUnit;
    }

    /**
     * <p>
     * Finds the configuration associated to the specified task.
     * </p>
     * <p>
     * If no configuration exists for the task, creates an empty one.
     * </p>
     * 
     * @param task
     *            the task
     * @return the configuration
     */
    private UnitSelectionFromIdentityStoreConfig findConfig( ITask task )
    {
        UnitSelectionFromIdentityStoreConfig config = _taskConfigService.findByPrimaryKey( task.getId( ) );

        if ( config == null )
        {
            config = new UnitSelectionFromIdentityStoreConfig( );
            config.setIdTask( task.getId( ) );
            _taskConfigService.create( config );
        }

        return config;
    }

    /**
     * Finds the field value of the record entry
     * 
     * @param nIdEntry
     *            the entry id
     * @param record
     *            the record
     * @return the field value
     */
    private String findFieldValue( int nIdEntry, Record record )
    {
        String strRecordFieldValue = StringUtils.EMPTY;

        RecordFieldFilter recordFieldFilter = new RecordFieldFilter( );
        recordFieldFilter.setIdDirectory( record.getDirectory( ).getIdDirectory( ) );
        recordFieldFilter.setIdEntry( nIdEntry );
        recordFieldFilter.setIdRecord( record.getIdRecord( ) );

        List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilter, WorkflowDirectorydemandsPlugin.getPlugin( ) );

        if ( listRecordFields != null && !listRecordFields.isEmpty( ) && listRecordFields.get( 0 ) != null )
        {
            RecordField recordField = listRecordFields.get( 0 );
            strRecordFieldValue = recordField.getValue( );

            if ( recordField.getField( ) != null )
            {
                strRecordFieldValue = recordField.getField( ).getTitle( );
            }
        }

        return strRecordFieldValue;
    }

    /**
     * This class is a configuration handler for the {@link UnitSelectionFromIdentityStore} class
     *
     */
    private class ConfigurationHandler implements IConfigurationHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.directorydemands.unit_selection.from_ids.config.title";

        // Parameters
        private static final String PARAMETER_DIRECTORY_ENTRY_ID = "directory_entry_id";
        private static final String PARAMETER_IDENTITY_STORE_ATTRIBUTE_KEY = "ids_attribute_key";

        // Templates
        private static final String TEMPLATE_CONFIG = "admin/plugins/workflow/modules/directorydemands/unitselection/config/unit_selection_from_identitystore_configuration.html";

        // Markers
        private static final String MARK_DIRECTORY_ENTRY_ID = PARAMETER_DIRECTORY_ENTRY_ID;
        private static final String MARK_IDENTITY_STORE_ATTRIBUTE_KEY = PARAMETER_IDENTITY_STORE_ATTRIBUTE_KEY;

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
        public String getDisplayedForm( Locale locale, ITask task )
        {
            UnitSelectionFromIdentityStoreConfig config = findConfig( task );

            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_DIRECTORY_ENTRY_ID, config.getIdDirectoryEntry( ) );
            model.put( MARK_IDENTITY_STORE_ATTRIBUTE_KEY, config.getIdentityStoreAttributeKey( ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIG, locale, model );

            return template.getHtml( );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String saveConfiguration( HttpServletRequest request, ITask task )
        {
            int nIdDirectoryEntry = NumberUtils.toInt( request.getParameter( PARAMETER_DIRECTORY_ENTRY_ID ) );
            String strIdentityStoreAttributeKey = request.getParameter( PARAMETER_IDENTITY_STORE_ATTRIBUTE_KEY );

            UnitSelectionFromIdentityStoreConfig config = findConfig( task );
            config.setIdDirectoryEntry( nIdDirectoryEntry );
            config.setIdentityStoreAttributeKey( strIdentityStoreAttributeKey );
            _taskConfigService.update( config );

            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeConfiguration( ITask task )
        {
            _taskConfigService.remove( task.getId( ) );
        }

    }

    /**
     * This class is a form handler for the {@link UnitSelectionFromIdentityStore} class
     *
     */
    private static class TaskFormHandler implements ITaskFormHandler
    {
        // Messages
        private static final String MESSAGE_TITLE = "module.workflow.directorydemands.unit_selection.from_ids.form.title";

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
        public String getDisplayedForm( int nIdResource, Locale locale, ITask task )
        {
            return StringUtils.EMPTY;
        }

    }

}
