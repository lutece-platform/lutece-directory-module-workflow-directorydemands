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

/**
 * This is the business class for the object RecordAssignment
 */
public class RecordAssignment
{
    // Variables declarations
    private int _nId;
    private int _nIdRecord;
    private int _nIdAssignedUnit;
    private int _nIdAssignorUnit;
    private Timestamp _dateAssignmentDate;
    private AssignmentType _assignmentType;
    private boolean _bIsActive;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the IdRecord
     * 
     * @return The IdRecord
     */
    public int getIdRecord( )
    {
        return _nIdRecord;
    }

    /**
     * Sets the IdRecord
     * 
     * @param nIdRecord
     *            The IdRecord
     */
    public void setIdRecord( int nIdRecord )
    {
        _nIdRecord = nIdRecord;
    }

    /**
     * Returns the IdAssignedUnit
     * 
     * @return The IdAssignedUnit
     */
    public int getIdAssignedUnit( )
    {
        return _nIdAssignedUnit;
    }

    /**
     * Sets the IdAssigneeUnit
     * 
     * @param nIdAssignedUnit
     *            The IdAssigneeUnit
     */
    public void setIdAssignedUnit( int nIdAssignedUnit )
    {
        _nIdAssignedUnit = nIdAssignedUnit;
    }

    /**
     * Returns the IdAssignorUnit
     * 
     * @return The IdAssignorUnit
     */
    public int getIdAssignorUnit( )
    {
        return _nIdAssignorUnit;
    }

    /**
     * Sets the IdAssignerUnit
     * 
     * @param nIdAssignorUnit
     *            The IdAssignerUnit
     */
    public void setIdAssignorUnit( int nIdAssignorUnit )
    {
        _nIdAssignorUnit = nIdAssignorUnit;
    }

    /**
     * Returns the AssignmentDate
     * 
     * @return The AssignmentDate
     */
    public Timestamp getAssignmentDate( )
    {
        return _dateAssignmentDate;
    }

    /**
     * Sets the AssignmentDate
     * 
     * @param dateAssignmentDate
     *            The AssignmentDate
     */
    public void setAssignmentDate( Timestamp dateAssignmentDate )
    {
        _dateAssignmentDate = dateAssignmentDate;
    }

    /**
     * Get the assignment type
     * 
     * @return the assignment type instance of enum
     */
    public AssignmentType getAssignmentType( )
    {
        return _assignmentType;
    }

    /**
     * Set the assignment type
     * 
     * @param _assignmentType
     *            the assignment type instance of enum
     */
    public void setAssignmentType( AssignmentType _assignmentType )
    {
        this._assignmentType = _assignmentType;
    }

    /**
     * Return the is active boolean
     * @return true if active, false otherwise
     */
    public boolean isActive()
    {
        return _bIsActive;
    }

    /**
     * Set the active boolean
     * @param isActive the active boolean
     */
    public void setActive( boolean isActive )
    {
        _bIsActive = isActive;
    }
    
    

}
