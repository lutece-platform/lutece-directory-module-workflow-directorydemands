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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.workflow.modules.directorydemands.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leridons
 */
public class RecordAssignmentFilter
{

    private List<Integer> _userUnitIdList = new ArrayList<>( ); // user unit Ids (from unittree)
    private boolean _nActiveAssignmentRecordsOnly; // active Assignment records only (true/false)
    private boolean _nLastActiveAssignmentRecordsOnly; // last active Assignment records only (true/false)
    private int _nNumberOfDays; // filter records by period : NONE, LAST_DAY, LAST_WEEK, LAST_MONTH
    private int _nDirectoryId; // specify a particular directory to filter records
    private int _nStateId; // specify a particular state to filter records
    private int _nAssignedUnitId; // specify a particular Assigned Unit to filter records
    private String _strOrderBy; // sort records
    private boolean _bAsc; // sort records order (ASC = true)
    private boolean _bIsActiveDirectory; // For getting record from only active directory

    /**
     * get User Unit Id list
     * 
     * @return the user Unit Id
     */
    public List<Integer> getUserUnitIdList( )
    {
        return _userUnitIdList;
    }

    /**
     * set UserUnitId list
     * 
     * @param userUnitIdList
     */
    public void setUserUnitIdList( List<Integer> userUnitIdList )
    {
        this._userUnitIdList = userUnitIdList;
    }

    /**
     * add a UserUnitId
     * 
     * @param id
     *            the userUnit Id
     */
    public void addUserUnitId( int id )
    {
        this._userUnitIdList.add( id );
    }

    /**
     * get if is ActiveRecordsOnly
     * 
     * @return
     */
    public boolean isActiveAssignmentRecordsOnly( )
    {
        return _nActiveAssignmentRecordsOnly;
    }

    /**
     * set ActiveRecordsOnly
     * 
     * @param activeRecordsOnly
     */
    public void setActiveAssignmentRecordsOnly( boolean activeRecordsOnly )
    {
        this._nActiveAssignmentRecordsOnly = activeRecordsOnly;
    }

    /**
     * get if is Last ActiveRecordsOnly
     * 
     * @return
     */
    public boolean isLastActiveAssignmentRecordsOnly( )
    {
        return _nLastActiveAssignmentRecordsOnly;
    }

    /**
     * set LastActiveRecordsOnly
     * 
     * @param lastActiveRecordsOnly
     */
    public void setLastActiveAssignmentRecordsOnly( boolean lastActiveRecordsOnly )
    {
        this._nLastActiveAssignmentRecordsOnly = lastActiveRecordsOnly;
    }

    /**
     * get NumberOfDays
     * 
     * @return
     */
    public int getNumberOfDays( )
    {
        return _nNumberOfDays;
    }

    /**
     * set NumberOfDays
     * 
     * @param numberOfDays
     */
    public void setNumberOfDays( int numberOfDays )
    {
        this._nNumberOfDays = numberOfDays;
    }

    /**
     * get DirectoryId
     * 
     * @return
     */
    public int getDirectoryId( )
    {
        return _nDirectoryId;
    }

    /**
     * set DirectoryId
     * 
     * @param directoryId
     */
    public void setDirectoryId( int directoryId )
    {
        this._nDirectoryId = directoryId;
    }

    /**
     * get StateId
     * 
     * @return
     */
    public int getStateId( )
    {
        return _nStateId;
    }

    /**
     * set StateId
     * 
     * @param stateId
     */
    public void setStateId( int stateId )
    {
        this._nStateId = stateId;
    }

    /**
     * get the Assigned Unit id
     * 
     * @return the Assigned Unit id
     */
    public int getAssignedUnitId( )
    {
        return _nAssignedUnitId;
    }

    /**
     * set the AssignedUnit
     * 
     * @param nAssignedUnit
     */
    public void setAssignedUnitId( int nAssignedUnit )
    {
        this._nAssignedUnitId = nAssignedUnit;
    }

    /**
     * get OrderBy
     * 
     * @return the order by statement
     */
    public String getOrderBy( )
    {
        return _strOrderBy;
    }

    /**
     * set OrderBy statement
     * 
     * @param orderBy
     */
    public void setOrderBy( String orderBy )
    {
        this._strOrderBy = orderBy;
    }

    /**
     * get the Asc order
     * 
     * @return
     */
    public boolean isAsc( )
    {
        return _bAsc;
    }

    /**
     * set the Asc order
     * 
     * @param _bAsc
     */
    public void setAsc( boolean _bAsc )
    {
        this._bAsc = _bAsc;
    }

    /**
     * Get active directory filter state
     * 
     * @return
     */
    public boolean isActiveDirectory( )
    {
        return _bIsActiveDirectory;
    }

    /**
     * Set active directory filter state
     * 
     * @param bIsActiveDirectory
     */
    public void setActiveDirectory( boolean bIsActiveDirectory )
    {
        _bIsActiveDirectory = bIsActiveDirectory;
    }

}
