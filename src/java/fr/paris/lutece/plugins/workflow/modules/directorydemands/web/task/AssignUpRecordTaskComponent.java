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
package fr.paris.lutece.plugins.workflow.modules.directorydemands.web.task;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfig;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.web.task.SimpleTaskComponent;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class represents a component for the {@link fr.paris.lutece.plugins.workflow.modules.directorydemands.service.task.TaskAssignUpRecord
 * TaskAssignUpRecord} workflow task
 *
 */
public class AssignUpRecordTaskComponent extends SimpleTaskComponent
{
    // Parameters
    public static final String PARAMETER_UNIT_ID = "task_assign_up_record_unit_id";

    // Messages
    private static final String MESSAGE_UNCORRECT_UNIT = "module.workflow.directorydemands.task_assign_up_record.validation.uncorrect.unit";
    private static final String MESSAGE_UNKNOWN_UNIT = "module.workflow.directorydemands.task_assign_up_record.validation.unknown.unit";

    // Templates
    private static final String TEMPLATE_TASK_ASSIGN_UP_RECORD_FORM = "admin/plugins/workflow/modules/directorydemands/task_assign_up_record_form.html";

    // Markers
    private static final String MARK_UNIT_LIST = "unit_list";

    // Other constants
    private static final int UNSET_ID = -1;

    // Services
    @Inject
    private IUnitService _unitService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateConfig( ITaskConfig config, HttpServletRequest request )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        int nIdUnit = NumberUtils.toInt( request.getParameter( PARAMETER_UNIT_ID ), UNSET_ID );

        if ( nIdUnit < 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNCORRECT_UNIT, AdminMessage.TYPE_STOP );
        }

        Unit unit = _unitService.getUnit( nIdUnit, false );

        if ( unit == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNKNOWN_UNIT, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale, ITask task )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        ReferenceList listUnits = buildUnitlist( );

        model.put( MARK_UNIT_LIST, listUnits );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_ASSIGN_UP_RECORD_FORM, locale, model );

        return template.getHtml( );
    }

    /**
     * Builds the unit list as a reference list
     * 
     * @return the reference list containing the units
     */
    private ReferenceList buildUnitlist( )
    {
        List<Unit> listUnits = _unitService.getAllUnits( false );

        return ReferenceList.convert( listUnits, "idUnit", "label", true );
    }

}
