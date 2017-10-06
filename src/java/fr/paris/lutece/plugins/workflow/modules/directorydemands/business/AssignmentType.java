/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

public enum AssignmentType
{
    CREATION( "create" ), ASSIGN_UP( "assign_up" ), ASSIGN_DOWN( "assign_down" ), TRANSFER( "transfer" );

    private String _strAssignmentTypeCode;

    AssignmentType( String strAssignmentTypeCode )
    {
        _strAssignmentTypeCode = strAssignmentTypeCode;
    }

    /**
     * Get the assignment type code
     * 
     * @return the assignment type code
     */
    public String getAssignmentTypeCode( )
    {
        return _strAssignmentTypeCode;
    }

    /**
     * Set the assignment type code
     * 
     * @param strAssignmentTypeCode
     *            the assignment type code
     */
    public void setAssignmentTypeCode( String strAssignmentTypeCode )
    {
        _strAssignmentTypeCode = strAssignmentTypeCode;
    }

    /**
     * Get assignment type by assignment code
     * 
     * @param strAssignmentType
     * @return the assignment type
     * @throws IllegalArgumentException
     */
    public static AssignmentType getFromCode( String strAssignmentType ) throws IllegalArgumentException
    {
        for ( AssignmentType assignmentType : values( ) )
        {
            if ( assignmentType.getAssignmentTypeCode( ).equals( strAssignmentType ) )
            {
                return assignmentType;
            }
        }
        return null;
    }
}
