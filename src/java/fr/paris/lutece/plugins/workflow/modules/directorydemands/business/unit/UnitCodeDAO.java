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
import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 */
public class UnitCodeDAO implements IUnitCodeDAO
{
    private final String SQL_SELECT_BY_UNIT = "SELECT id_unit, unit_code FROM unittree_unit_code WHERE id_unit = ?";
    private final String SQL_SELECT_BY_CODE = "SELECT id_unit, unit_code FROM unittree_unit_code WHERE unit_code = ?";
    private final String SQL_DELETE_BY_UNIT = "DELETE FROM unittree_unit_code WHERE id_unit = ?";
    // WARNING, UPDATE and INSERT HAVE TO have the same parameter order
    private final String SQL_UPDATE_UNIT_CODE = "UPDATE unittree_unit_code SET unit_code = ? WHERE id_unit = ?";
    private final String SQL_INSERT_UNIT_CODE = "INSERT INTO unittree_unit_code ( unit_code, id_unit ) VALUES ( ?, ? )";

    /**
     * {@inheritDoc}
     */
    @Override
    public UnitCode loadByIdUnit( int nIdUnit, Plugin plugin )
    {
        UnitCode unitCode = null;

        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_BY_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;

            unitCode = new UnitCode( );
            unitCode.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unitCode.setUnitCode( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );

        return unitCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeForUnit( int nIdUnit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DELETE_BY_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeConfiguration( UnitCode unitCode, Plugin plugin )
    {
        if ( unitCode != null )
        {
            DAOUtil daoUtil;
            if ( loadByIdUnit( unitCode.getIdUnit( ), plugin ) != null )
            {
                // UPDATE
                daoUtil = new DAOUtil( SQL_UPDATE_UNIT_CODE, plugin );
            }
            else
            {
                // INSERT
                daoUtil = new DAOUtil( SQL_INSERT_UNIT_CODE, plugin );
            }

            int nIndex = 1;
            daoUtil.setString( nIndex++, unitCode.getUnitCode( ) );
            daoUtil.setInt( nIndex++, unitCode.getIdUnit( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnitCode loadByCode( String strCode, Plugin plugin )
    {
        UnitCode unitCode = null;

        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_BY_CODE, plugin );
        daoUtil.setString( 1, strCode );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;

            unitCode = new UnitCode( );
            unitCode.setIdUnit( daoUtil.getInt( nIndex++ ) );
            unitCode.setUnitCode( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );

        return unitCode;
    }

}
