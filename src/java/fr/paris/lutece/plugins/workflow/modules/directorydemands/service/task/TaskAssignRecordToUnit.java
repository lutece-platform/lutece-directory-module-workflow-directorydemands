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

import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.AssignmentType;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.exception.UnitCodeNotFoundException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.IUnitCodeService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.utils.Constants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class TaskAssignRecordToUnit extends AbstractTaskDirectoryDemands
{
    // SERVICES
    @Inject
    private IdentityService _identityService;
    @Inject
    private IUnitCodeService _unitCodeService;

    // Resources
    private static final String MESSAGE_TASK_ASSIGN_TO_UNIT = "module.workflow.directorydemands.task_assign_record_to_unit.title";

    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_ASSIGN_TO_UNIT, locale );
    }

    /**
     * Perform the assign-to-unit task; based on user ugd_unit_code on identityservice.
     * 
     * @param nIdResourceHistory
     *            the id of resource history
     * @param request
     *            the HttpServletRequest
     * @param locale
     *            the locale
     */
    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        Record record = findRecordByIdHistory( nIdResourceHistory );

        if ( record != null )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            // FIXME : The request is null when the action is automatic
            // Get the identity with his/her guid
            String strGuid = user.getName( );
            IdentityDto identity = _identityService.getIdentity( strGuid, null, Constants.APPLICATION_CODE );
            if ( identity != null )
            {
                Map<String, AttributeDto> mapAttributeDto = identity.getAttributes( );
                AttributeDto unitCode = mapAttributeDto.get( Constants.ATTRIBUTE_UNIT_CODE );
                if ( unitCode != null )
                {
                    try
                    {
                        // Get the unit id from unitCode
                        Integer nIdUnit = _unitCodeService.getIdUnitFromUnitCode( unitCode.getValue( ) );

                        RecordAssignment recordAssignment = new RecordAssignment( );
                        recordAssignment.setIdRecord( nIdResourceHistory );
                        recordAssignment.setIdAssigneeUnit( nIdUnit );
                        recordAssignment.setAssignmentType( AssignmentType.CREATION );
                        RecordAssignmentHome.create( recordAssignment, WorkflowDirectorydemandsPlugin.getPlugin( ) );
                    }
                    catch( UnitCodeNotFoundException e )
                    {
                        AppLogService.error( "Unable to find unit with given unit code", e );
                    }
                }

            }
        }
    }
}
