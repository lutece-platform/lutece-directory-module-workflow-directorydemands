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

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.List;

/**
 *
 */
public interface IUnitCodeDAO
{
    final String BEAN_NAME = "workflow-directorydemands.unitCodeDAO";

    /**
     * retrieve UnitCode configuration for a given unit
     * 
     * @param nIdUnit
     *            id of the unit
     * @param plugin
     *            the plugin
     * @return {@link UnitCode} the UnitCode configuration
     */
    UnitCode loadByIdUnit( int nIdUnit, Plugin plugin );

    /**
     * remove UnitCode configuration for a given unit
     * 
     * @param nIdUnit
     *            id of the unit
     * @param plugin
     *            the plugin
     */
    void removeForUnit( int nIdUnit, Plugin plugin );

    /**
     * update or create a UnitCode configuration
     * 
     * @param technicalCode
     *            the UnitCode confifuration
     * @param plugin
     *            the plugin
     */
    void mergeConfiguration( UnitCode technicalCode, Plugin plugin );

    /**
     * Load unitCode from code
     * 
     * @param strCode
     *            the code
     * 
     * @param plugin
     *            the plugin
     * 
     * @return the unitCode
     */
    UnitCode loadByCode( String strCode, Plugin plugin );
}
