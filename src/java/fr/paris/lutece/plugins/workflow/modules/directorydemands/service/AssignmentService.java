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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignment;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.RecordAssignmentHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides methods for unit assignment
 *
 */
public final class AssignmentService
{
    // Services
    private static IUnitService _unitService = SpringContextService.getBean( IUnitService.BEAN_UNIT_SERVICE );

    /**
     * Constructor
     */
    private AssignmentService( )
    {

    }

    /**
     * Get the active record assignment list of a given adminUser, recursively over the unit tree
     * 
     * @param map
     * @return the list of active record assignement list for the given admin user.
     */
    public static List<RecordAssignment> getRecordAssignmentFiltredList( HashMap<String, Integer> map )
    {

        return RecordAssignmentHome.getRecordAssignmentsFiltredList( map );
    }

    /**
     * Finds the assigner unit id from the logged in user with request
     * 
     * @param request
     *            the request containing the user
     * @return the assigner unit id
     */
    public static Unit findAssignorUnit( HttpServletRequest request )
    {
        Unit unit = null;

        if ( request != null )
        {
            AdminUser adminUser = AdminUserService.getAdminUser( request );

            unit = findAssignorUnit( adminUser );
        }

        return unit;
    }

    /**
     * Finds the assigner unit id from the logged in user
     * 
     * @param adminUser
     *            the admin user
     * @return the assigner unit id
     */
    public static Unit findAssignorUnit( AdminUser adminUser )
    {
        Unit unit = null;
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

                unit = listUnits.get( 0 );
            }
        }

        return unit;
    }
    
    public static List<Integer> findSubUnitsIds( AdminUser adminUser )
    {
        return _unitService.getSubUnits( findAssignorUnit( adminUser).getIdUnit( ) , false)
                .stream( )
                .map( unit -> unit.getIdUnit( ) )
                .collect( Collectors.toList( ) );
    }
}
