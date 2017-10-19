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
public class RecordAssignmentFilter {
    
    private List<Integer> _userUnitIdList = new ArrayList<>( ); // user unit Ids (from unittree)
    private boolean _nActiveRecordsOnly ; //active records only (true/false)
    private int _nNumberOfDays ; // filter records by period : NONE, LAST_DAY, LAST_WEEK, LAST_MONTH
    private int _nDirectoryId ; // specify a particular directory to filter records
    private int _nStateId ; // specify a particular state to filter records
    private String _strOrderBy ; // sort records

    /**
     * get User Unit Id list
     * 
     * @return the user Unit Id
     */
    public List<Integer> getUserUnitIdList() {
        return _userUnitIdList;
    }

    /**
     * set UserUnitId list
     * 
     * @param userUnitIdList
     */
    public void setUserUnitIdList( List<Integer> userUnitIdList ) {
        this._userUnitIdList = userUnitIdList;
    }
    
    /**
     * add a UserUnitId 
     * 
     * @param id 
     *          the userUnit Id
     */
    public void addUserUnitId( int id )
    {
        this._userUnitIdList.add( id ) ;
    }

    /**
     * get if is ActiveRecordsOnly
     * 
     * @return 
     */
    public boolean isActiveRecordsOnly( ) 
    {
        return _nActiveRecordsOnly;
    }

    /**
     * set ActiveRecordsOnly
     * 
     * @param activeRecordsOnly 
     */
    public void setActiveRecordsOnly(boolean activeRecordsOnly ) 
    {
        this._nActiveRecordsOnly = activeRecordsOnly;
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
    public void setNumberOfDays(int numberOfDays ) 
    {
        this._nNumberOfDays = numberOfDays;
    }

    /**
     * get DirectoryId
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
    public void setDirectoryId(int directoryId ) 
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
     * @param stateId 
     */
    public void setStateId(int stateId ) 
    {
        this._nStateId = stateId;
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
    public void setOrderBy(String orderBy ) 
    {
        this._strOrderBy = orderBy;
    }
    
    
}
