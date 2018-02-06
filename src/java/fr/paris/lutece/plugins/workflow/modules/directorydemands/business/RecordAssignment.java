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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.business;

import java.sql.Timestamp;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.portal.business.user.AdminUser;

/**
 * This class represents the assignment of a {@link fr.paris.lutece.plugins.directory.business.Record Record} to a
 * {@link fr.paris.lutece.plugins.unittree.business.unit.Unit Unit}
 */
public class RecordAssignment
{
    private static final int UNIT_DEFAULT_ID = -1;

    private int _nId;
    private int _nIdRecord;
    private Unit _assignedUnit;
    private Unit _assignorUnit;
    private Timestamp _dateAssignmentDate;
    private AdminUser _assignedUser;

    /**
     * Constructor
     */
    public RecordAssignment( )
    {
        this._assignedUnit = new Unit( );
        this._assignorUnit = new Unit( );
        this._assignedUser = new AdminUser( );

        _assignedUnit.setIdUnit( UNIT_DEFAULT_ID );
        _assignorUnit.setIdUnit( UNIT_DEFAULT_ID );
    }

    /**
     * Gives the id
     * 
     * @return The id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the id
     * 
     * @param nId
     *            The id to set
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gives the record id
     * 
     * @return The record id
     */
    public int getIdRecord( )
    {
        return _nIdRecord;
    }

    /**
     * Sets the record id
     * 
     * @param nIdRecord
     *            The record to set
     */
    public void setIdRecord( int nIdRecord )
    {
        _nIdRecord = nIdRecord;
    }

    /**
     * Gives the assigned unit
     * 
     * @return The assigned unit
     */
    public Unit getAssignedUnit( )
    {
        return _assignedUnit;
    }

    /**
     * Sets the assigned unit
     * 
     * @param assignedUnit
     *            The assigned unit to set
     */
    public void setAssignedUnit( Unit assignedUnit )
    {
        _assignedUnit = assignedUnit;
    }

    /**
     * Gives the assignor unit
     * 
     * @return The assignor unit
     */
    public Unit getAssignorUnit( )
    {
        return _assignorUnit;
    }

    /**
     * Sets the assignor unit
     * 
     * @param assignorUnit
     *            The assignor unit to set
     */
    public void setAssignorUnit( Unit assignorUnit )
    {
        _assignorUnit = assignorUnit;
    }

    /**
     * Gives the assignment date
     * 
     * @return The assignment date
     */
    public Timestamp getAssignmentDate( )
    {
        return _dateAssignmentDate;
    }

    /**
     * Sets the assignment date
     * 
     * @param dateAssignmentDate
     *            The assignment date to set
     */
    public void setAssignmentDate( Timestamp dateAssignmentDate )
    {
        _dateAssignmentDate = dateAssignmentDate;
    }

    /**
     * Gives the assigned unit id
     * 
     * @return The assigned unit id
     */
    public int getIdAssignedUnit( )
    {
        return _assignedUnit.getIdUnit( );
    }

    /**
     * Sets the assigned unit id
     * 
     * @param nIdAssignedUnit
     *            The assigned unit id to set
     */
    public void setIdAssignedUnit( int nIdAssignedUnit )
    {
        _assignedUnit.setIdUnit( nIdAssignedUnit );
    }

    /**
     * Gives the assignor unit id
     * 
     * @return The assignor unit id
     */
    public int getIdAssignorUnit( )
    {
        return _assignorUnit.getIdUnit( );
    }

    /**
     * Sets the assignor unit id
     * 
     * @param nIdAssignorUnit
     *            The assignor unit id to set
     */
    public void setIdAssignorUnit( int nIdAssignorUnit )
    {
        _assignorUnit.setIdUnit( nIdAssignorUnit );
    }

    /**
     * Get the assigned user
     * @return the adminUser who is assigned to record
     */
    public AdminUser getAssignedUser() {
        return _assignedUser;
    }

    /**
     * Set the assigned admin user
     * @param _assignedUser the admin user who is assigned to record.
     */
    public void setAssignedUser(AdminUser _assignedUser) {
        this._assignedUser = _assignedUser;
    }
    
    

}
