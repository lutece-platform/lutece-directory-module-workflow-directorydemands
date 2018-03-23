/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.web.rs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.rs.ConnectionIdsNotProvidedException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.rs.ReassignmentDto;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.rs.ReassignmentResponseDto;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.service.ReassignmentService;
import fr.paris.lutece.plugins.workflow.modules.unittree.exception.AssignmentNotPossibleException;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.utils.Constants;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service for rassignment of resources
 *
 */
@Path( RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.ASSIGNMENT_PATH )
public class AssignmentRest
{
    @Inject
    private ReassignmentService _reassignmentService;

    /**
     * Process the record reassignment based on a json body with several connection id
     * 
     * @param strJson
     *            the Json object representing ReassignmentDto obj.
     * @return the list of channels
     */
    @POST
    @Path( Constants.REASSIGN_PATH )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response doReassign( String strJson )
    {
        ReassignmentResponseDto response = new ReassignmentResponseDto( );
        List<String> listErrorConnectionId = new ArrayList<>( );

        // Format from JSON
        ObjectMapper mapper = new ObjectMapper( );
        mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, true );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        try
        {
            ReassignmentDto reassignmentDto = mapper.readValue( strJson, ReassignmentDto.class );

            if ( reassignmentDto.getListConnectionId( ).isEmpty( ) )
            {
                throw new ConnectionIdsNotProvidedException( "No connection id provided" );
            }

            for ( String strConnectionId : reassignmentDto.getListConnectionId( ) )
            {
                try
                {
                    _reassignmentService.reassignRecordsByConnectionId( strConnectionId );
                }
                catch( AssignmentNotPossibleException e )
                {
                    listErrorConnectionId.add( strConnectionId );
                    AppLogService.error( "Unable to reassign records for connection id " + strConnectionId, e );
                }

            }
            if ( listErrorConnectionId.isEmpty( ) )
            {
                response.setMessage( "Successfully reassigned resources for given connection ids" );
            }
            else
            {
                response.setMessage( "Some resources couldn't be reassigned for following connection ids" );
            }
        }
        catch( IOException e )
        {
            AppLogService.error( "Unable to map given JSON body " + strJson + " to ReassignmentDto", e );
            response.setMessage( "Json format error" );
        }
        catch( ConnectionIdsNotProvidedException e )
        {
            AppLogService.error( e.getMessage( ), e );
            response.setMessage( e.getMessage( ) );
        }

        response.setListErrorConnectionId( listErrorConnectionId );

        try
        {
            String strJsonResponse = mapper.writeValueAsString( response );
            return Response.ok( ).entity( strJsonResponse ).build( );
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( "Unable to construct json response", e );
            return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).build( );
        }
    }
}
