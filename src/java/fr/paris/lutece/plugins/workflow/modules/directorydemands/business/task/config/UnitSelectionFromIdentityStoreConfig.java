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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.business.task.config;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * This class represents the configuration for the unit selection
 * {@link fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.selection.impl.UnitSelectionFromIdentityStore UnitSelectionFromIdentityStore}
 */
public class UnitSelectionFromIdentityStoreConfig
{
    // Properties
    private static final String PROPERTY_APPLICATION_CODE = "workflow-directorydemands.unit.selection.from.identitystore.applicationCode";
    private static final String PROPERTY_DIRECTORY_ENTRY_TITLE = "workflow-directorydemands.unit.selection.from.identitystore.directory.entry.title";
    private static final String PROPERTY_IDENTITYSTORE_ATTRIBUTE_KEY = "workflow-directorydemands.unit.selection.from.identitystore.identitystore.attribute.key";

    // Variables declarations
    private static final String _strApplicationCode = AppPropertiesService.getProperty( PROPERTY_APPLICATION_CODE );
    private static final String _strDirectoryEntryTitle = AppPropertiesService.getProperty( PROPERTY_DIRECTORY_ENTRY_TITLE );
    private static final String _strIdentityStoreAttributeKey = AppPropertiesService.getProperty( PROPERTY_IDENTITYSTORE_ATTRIBUTE_KEY );

    /**
     * Gives the application code
     * 
     * @return the application code
     */
    public String getApplicationCode( )
    {
        return _strApplicationCode;
    }

    /**
     * Gives the title of the directory entry
     * 
     * @return The title of the directory entry
     */
    public String getDirectoryEntryTitle( )
    {
        return _strDirectoryEntryTitle;
    }

    /**
     * Gives the IdentityStore attribute key
     * 
     * @return The IdentityStore attribute key
     */
    public String getIdentityStoreAttributeKey( )
    {
        return _strIdentityStoreAttributeKey;
    }
}
