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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.service.workflow;

import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.ReassignmentResourceHistory;
import fr.paris.lutece.plugins.workflow.modules.directorydemands.business.ReassignmentResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.service.WorkflowProvider;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.plugins.workflowcore.web.task.ITaskComponentManager;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class DirectorydemandsWorkflowProvider extends WorkflowProvider
{
    // TEMPLATES
    private static final String TEMPLATE_RESOURCE_HISTORY = "admin/plugins/workflow/resource_history.html";
    private static final String TEMPLATE_REASSIGNMENT_INFO = "admin/plugins/workflow/modules/directorydemands/reassignment_information.html";

    // MARKS
    private static final String MARK_RESOURCE_HISTORY = "resource_history";
    private static final String MARK_TASK_INFORMATION_LIST = "task_information_list";
    private static final String MARK_ADMIN_USER_HISTORY = "admin_user_history";
    private static final String MARK_HISTORY_INFORMATION_LIST = "history_information_list";
    private static final String MARK_ADMIN_AVATAR = "adminAvatar";
    private static final String MARK_REASSIGNMENT_INFO = "reassignment_information";

    // MESSAGES
    private static final String MESSAGE_ACTION_REASSIGNMENT_NAME = "module.workflow.directorydemands.reassignment.action.name";
    private static final String MESSAGE_ACTION_REASSIGNMENT_DESCRIPTION = "module.workflow.directorydemands.reassignment.action.description";

    // SERVICES
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    private ITaskService _taskService;
    @Inject
    private ITaskComponentManager _taskComponentManager;

    @Override
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale )
    {
        return getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, null, TEMPLATE_RESOURCE_HISTORY );
    }

    @Override
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,
            String strTemplate )
    {
        Map<String, Object> defaultModel = getDefaultModelDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale );

        HtmlTemplate templateList = AppTemplateService.getTemplate( strTemplate, locale, defaultModel );

        return templateList.getHtml( );
    }

    @Override
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,
            Map<String, Object> model, String strTemplate )
    {
        Map<String, Object> defaultModel = getDefaultModelDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale );

        if ( model != null )
        {
            defaultModel.putAll( model );
        }

        HtmlTemplate templateList = AppTemplateService.getTemplate( strTemplate, locale, defaultModel );

        return templateList.getHtml( );
    }

    private Map<String, Object> getDefaultModelDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request,
            Locale locale )
    {
        List<ResourceHistory> listResourceHistory = _resourceHistoryService.getAllHistoryByResource( nIdResource, strResourceType, nIdWorkflow );
        List<ITask> listActionTasks;
        List<String> listTaskInformation;
        Map<String, Object> model = new HashMap<String, Object>( );
        Map<String, Object> resourceHistoryTaskInformation;
        List<Map<String, Object>> listResourceHistoryTaskInformation = new ArrayList<Map<String, Object>>( );
        String strTaskinformation;

        for ( ResourceHistory resourceHistory : listResourceHistory )
        {
            resourceHistoryTaskInformation = new HashMap<String, Object>( );
            resourceHistoryTaskInformation.put( MARK_RESOURCE_HISTORY, resourceHistory );

            if ( resourceHistory.getUserAccessCode( ) != null )
            {
                resourceHistoryTaskInformation.put( MARK_ADMIN_USER_HISTORY, AdminUserHome.findUserByLogin( resourceHistory.getUserAccessCode( ) ) );
            }

            listTaskInformation = new ArrayList<String>( );
            listActionTasks = _taskService.getListTaskByIdAction( resourceHistory.getAction( ).getId( ), locale );

            for ( ITask task : listActionTasks )
            {
                strTaskinformation = _taskComponentManager.getDisplayTaskInformation( resourceHistory.getId( ), request, locale, task );

                if ( strTaskinformation != null )
                {
                    listTaskInformation.add( strTaskinformation );
                }
            }

            resourceHistoryTaskInformation.put( MARK_TASK_INFORMATION_LIST, listTaskInformation );

            listResourceHistoryTaskInformation.add( resourceHistoryTaskInformation );
        }

        // Add custom reassignment resource history informations
        for ( ReassignmentResourceHistory reassignmentResourceHistory : ReassignmentResourceHistoryHome.getReassignmentResourceHistorysListByResource(
                nIdResource, strResourceType ) )
        {
            ResourceHistory resourceHistory = new ResourceHistory( );
            resourceHistory.setAction( null );
            resourceHistory.setCreationDate( reassignmentResourceHistory.getCreationDate( ) );
            resourceHistory.setIdResource( reassignmentResourceHistory.getIdResource( ) );
            resourceHistory.setResourceType( reassignmentResourceHistory.getResourceType( ) );
            resourceHistory.setUserAccessCode( null );
            resourceHistory.setWorkFlow( null );

            Action action = new Action( );
            action.setName( I18nService.getLocalizedString( MESSAGE_ACTION_REASSIGNMENT_NAME, locale ) );
            action.setDescription( I18nService.getLocalizedString( MESSAGE_ACTION_REASSIGNMENT_DESCRIPTION, locale ) );
            resourceHistory.setAction( action );

            resourceHistoryTaskInformation = new HashMap<>( );
            resourceHistoryTaskInformation.put( MARK_RESOURCE_HISTORY, resourceHistory );
            listTaskInformation = new ArrayList<>( );
            Map<String, Object> modelTaskInfo = new HashMap<String, Object>( );
            modelTaskInfo.put( MARK_REASSIGNMENT_INFO, reassignmentResourceHistory.getContent( ) );
            listTaskInformation.add( AppTemplateService.getTemplate( TEMPLATE_REASSIGNMENT_INFO, locale, modelTaskInfo ).getHtml( ) );
            resourceHistoryTaskInformation.put( MARK_TASK_INFORMATION_LIST, listTaskInformation );

            listResourceHistoryTaskInformation.add( resourceHistoryTaskInformation );
        }

        listResourceHistoryTaskInformation.sort( new ResourceHistoryComparator( ) );
        model.put( MARK_HISTORY_INFORMATION_LIST, listResourceHistoryTaskInformation );
        model.put( MARK_ADMIN_AVATAR, PluginService.isPluginEnable( "adminavatar" ) );

        return model;
    }

    private class ResourceHistoryComparator implements Comparator<Map<String, Object>>
    {
        @Override
        public int compare( Map<String, Object> resource1, Map<String, Object> resource2 )
        {
            ResourceHistory resourceHistory1 = (ResourceHistory) resource1.get( MARK_RESOURCE_HISTORY );
            ResourceHistory resourceHistory2 = (ResourceHistory) resource2.get( MARK_RESOURCE_HISTORY );
            return -resourceHistory1.getCreationDate( ).compareTo( resourceHistory2.getCreationDate( ) );
        }
    }

}
