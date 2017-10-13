/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class AssignmentService
{
     // Services
    @Inject
    private static IUnitService _unitService;
    
    /**
     * Get the active record assignment list of a given adminUser, recursively over the unit tree
     * @param adminUser the AdminUser
     * @return the list of active record assignement list for the given admin user.
     */
    public static List<RecordAssignment> getListActiveRecordAssignment( AdminUser adminUser )
    {        
        List<Integer> listSubUnitIds = findSubUnitsIds( adminUser );

         return   RecordAssignmentHome
                        .getRecursiveListActiveByAssignedUnitId( listSubUnitIds, WorkflowDirectorydemandsPlugin.getPlugin( ) );
    }
    
    /**
     * Finds the assigner unit id from the logged in user with request
     * 
     * @param request
     *            the request containing the user
     * @return the assigner unit id
     */
    public static int findAssignerUnitId( HttpServletRequest request )
    {
        AdminUser adminUser = AdminUserService.getAdminUser( request );

        return findAssignerUnitId( adminUser );
    }
    
    /**
     * Finds the assigner unit id from the logged in user
     * 
     * @param adminUser
     *            the admin user
     * @return the assigner unit id
     */
    public static int findAssignerUnitId( AdminUser adminUser )
    {
        int nIdAssignerUnit = 0;
        if ( adminUser != null )
        {
            List<Unit> listUnits = _unitService.getUnitsByIdUser( adminUser.getUserId( ), false );

            if ( listUnits != null && !listUnits.isEmpty( ) )
            {
                if ( listUnits.size( ) > 1 )
                {
                    AppLogService
                            .error( "TaskAssignUpRecord : Multi affectation is enabled on units. The first unit is used, which can cause weard behaviour." );
                }

                nIdAssignerUnit = listUnits.get( 0 ).getIdUnit( );
            }
        }

        return nIdAssignerUnit;
    }
    
    public static List<Integer> findSubUnitsIds( AdminUser adminUser )
    {
        return _unitService.getSubUnits( findAssignerUnitId( adminUser) , false)
                .stream( )
                .map( unit -> unit.getIdUnit( ) )
                .collect( Collectors.toList( ) );
    }
}
