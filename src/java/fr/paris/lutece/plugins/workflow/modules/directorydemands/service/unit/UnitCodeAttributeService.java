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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service.unit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitAttributeService;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.unit.UnitCode;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.unit.UnitCodeAttribute;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.unit.UnitCodeHome;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.exception.UnitCodeNotFoundException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.IUnitCodeService;

/**
 *
 */
public class UnitCodeAttributeService implements IUnitAttributeService, IUnitCodeService
{
    public static final String BEAN_NAME = "workflowdirectorydemands.unitCodeService";

    // Markers
    public static final String MARK_UNIT_CODE = "unit_code";

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateUnit( Unit unit, HttpServletRequest request )
    {
        // the unit in paramater HAS BEEN populate by the request before this call
        // but the id of the unit is not set in the UnitAttribute because the unit wasn't created at the populate call.
        if ( unit.getAttribute( UnitCodeAttribute.ATTRIBUTE_NAME ) != null )
        {
            UnitCode unitCode = (UnitCode) unit.getAttribute( UnitCodeAttribute.ATTRIBUTE_NAME ).getAttribute( );
            unitCode.setIdUnit( unit.getIdUnit( ) );
            UnitCodeHome.mergeConfiguration( unitCode );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doModifyUnit( Unit unit, HttpServletRequest request )
    {
        // the unit in parameter HAS BEEN populate by the request before this call
        if ( unit.getAttribute( UnitCodeAttribute.ATTRIBUTE_NAME ) != null )
        {
            UnitCode unitCode = (UnitCode) unit.getAttribute( UnitCodeAttribute.ATTRIBUTE_NAME ).getAttribute( );
            UnitCodeHome.mergeConfiguration( unitCode );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveUnit( int nIdUnit, HttpServletRequest request )
    {
        UnitCodeHome.removeForUnit( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate( Unit unit )
    {
        if ( unit == null )
        {
            return;
        }

        UnitCodeAttribute codeUnitAtt = new UnitCodeAttribute( );
        UnitCode unitCode = UnitCodeHome.loadByUnit( unit );
        if ( unitCode == null )
        {
            unitCode = new UnitCode( );
            unitCode.setIdUnit( unit.getIdUnit( ) );
        }
        codeUnitAtt.setAttribute( unitCode );

        unit.addAttribute( codeUnitAtt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate( Unit unit, HttpServletRequest request ) throws UnitErrorException
    {
        // use to control request and populate unit before calling dao
        String strUnitCode = request.getParameter( MARK_UNIT_CODE );

        if ( StringUtils.isNotEmpty( strUnitCode ) )
        {
            UnitCode unitCode = new UnitCode( );
            unitCode.setIdUnit( unit.getIdUnit( ) );
            unitCode.setUnitCode( strUnitCode );

            UnitCodeAttribute unitCodeAttribute = new UnitCodeAttribute( );
            unitCodeAttribute.setAttribute( unitCode );
            unit.addAttribute( unitCodeAttribute );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canCreateSubUnit( int nIdUnit )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveSubTree( Unit unitToMove, Unit newUnitParent )
    {
    }

    @Override
    public Integer getIdUnitFromUnitCode( String strUnitCode ) throws UnitCodeNotFoundException
    {
        return UnitCodeHome.loadByCode( strUnitCode ).getIdUnit( );
    }

    /**
     * Get the unit code of the given unit
     * 
     * @param unit
     *            the unit
     * @return the unit code
     */
    public UnitCode getUnitCode( Unit unit )
    {
        return UnitCodeHome.loadByUnit( unit );
    }

}
