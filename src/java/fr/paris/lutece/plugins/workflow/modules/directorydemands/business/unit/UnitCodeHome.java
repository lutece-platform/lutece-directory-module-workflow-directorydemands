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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.business.unit;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.WorkflowDirectorydemandsPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public final class UnitCodeHome
{
    private static Plugin _plugin = PluginService.getPlugin( WorkflowDirectorydemandsPlugin.PLUGIN_NAME );
    private static IUnitCodeDAO _dao = SpringContextService.getBean( IUnitCodeDAO.BEAN_NAME );

    /**
     * Private constructor
     */
    private UnitCodeHome( )
    {
    }

    /**
     * retrieve UnitCode configuration for a given unit
     * 
     * @param unit
     *            the unit
     * @return {@link UnitCode} the UnitCode configuration
     */
    public static UnitCode loadByUnit( Unit unit )
    {
        if ( unit != null )
        {
            return _dao.loadByIdUnit( unit.getIdUnit( ), _plugin );
        }
        else
        {
            return null;
        }
    }

    /**
     * retrieve UnitCode configuration for a given unit code
     * 
     * @param strCode
     *            the unit code
     * @return {@link UnitCode} the UnitCode configuration
     */
    public static UnitCode loadByCode( String strCode )
    {
        return _dao.loadByCode( strCode, _plugin );
    }

    /**
     * remove UnitCode configuration for a given unit
     * 
     * @param nIdUnit
     *            id of the unit
     */
    public static void removeForUnit( int nIdUnit )
    {
        _dao.removeForUnit( nIdUnit, _plugin );
    }

    /**
     * update or create a UnitCode configuration
     * 
     * @param unitCode
     *            the UnitCode confifuration
     */
    public static void mergeConfiguration( UnitCode unitCode )
    {
        if ( unitCode != null )
        {
            _dao.mergeConfiguration( unitCode, _plugin );
        }
    }
}
