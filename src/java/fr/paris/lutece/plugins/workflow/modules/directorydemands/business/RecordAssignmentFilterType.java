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

public enum RecordAssignmentFilterType
{
    USER_UNIT_ID( "USER_UNIT_ID" ), // user unit Id (from unittree)
    RECURSIVE_SEARCH_DEPTH( "RECURSIVE_SEARCH_DEPTH" ), // depth of recursive search of the children units of the user unit (1,2 or 3)
    ACTIVE_RECORDS_ONLY( "ACTIVE_RECORDS_ONLY" ), // active records only (true/false)
    FILTER_PERIOD( "FILTER_PERIOD" ), // filter records by period : NONE, LAST_DAY, LAST_WEEK, LAST_MONTH
    DIRECTORY_ID( "DIRECTORY_ID" ), // specify a particular directory to filter records
    STATE_ID( "STATE_ID" ), // specify a particular state to filter records
    ;

    private String _strRecordAssignmentFilterTypeCode;

    RecordAssignmentFilterType( String strCode )
    {
        _strRecordAssignmentFilterTypeCode = strCode;
    }

    /**
     * Get the assignment filter type code
     * 
     * @return the assignment filter type code
     */
    public String getRecordAssignmentFilterTypeCode( )
    {
        return _strRecordAssignmentFilterTypeCode;
    }

    /**
     * Set the assignment type code
     * 
     * @param strCode
     *            the assignment filter type code
     */
    public void setRecordAssignmentFilterTypeCode( String strCode )
    {
        _strRecordAssignmentFilterTypeCode = strCode;
    }

    /**
     * Get assignment type by assignment code
     * 
     * @param strType
     * @return the assignment filter type
     * @throws IllegalArgumentException
     */
    public static RecordAssignmentFilterType getFromCode( String strType ) throws IllegalArgumentException
    {
        for ( RecordAssignmentFilterType assignmentFilterType : values( ) )
        {
            if ( assignmentFilterType.getRecordAssignmentFilterTypeCode( ).equals( strType ) )
            {
                return assignmentFilterType;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString( )
    {
        return _strRecordAssignmentFilterTypeCode;
    }
}
