// license-header java merge-point
package org.andromda.test.howto16.a.crud;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.andromda.test.ManageableServiceLocator;
import org.andromda.test.howto16.a.CarType;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

public final class ManageCar extends DispatchAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        request.getSession().setAttribute("manageableForm", actionForm);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final CarForm form = (CarForm)actionForm;

        if (StringUtils.isNotBlank(request.getParameter("type")) && !CarType.literals().contains(form.getType()))
        {
            throw new IllegalArgumentException("type must be  one of " + CarType.literals());
        }

        ManageableServiceLocator.instance().getCarManageableService().create(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward read(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final CarForm form = (CarForm)actionForm;

        final List list = ManageableServiceLocator.instance().getCarManageableService().read(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );
        form.setManageableList(list);

        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final Map backingLists = ManageableServiceLocator.instance().getCarManageableService().readBackingLists();
        form.setOwnerBackingList((List)backingLists.get("owner"));

        return mapping.getInputForward();
    }

    public ActionForward preload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final CarForm form = (CarForm)actionForm;

        final List list = ManageableServiceLocator.instance().getCarManageableService().readAll();
        form.setManageableList(list);

        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final Map backingLists = ManageableServiceLocator.instance().getCarManageableService().readBackingLists();
        if (StringUtils.isNotBlank(request.getParameter("ref_Person")))
        {
            form.setOwner(new Long(request.getParameter("ref_Person")));
        }
        form.setOwnerBackingList((List)backingLists.get("owner"));

        return mapping.getInputForward();
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return preload(mapping, actionForm, request, response);
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final CarForm form = (CarForm) actionForm;

        if (StringUtils.isNotBlank(request.getParameter("type")) && !CarType.literals().contains(form.getType()))
        {
            throw new IllegalArgumentException("type must be  one of " + CarType.literals());
        }

        ManageableServiceLocator.instance().getCarManageableService().update(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final CarForm form = (CarForm) actionForm;

        final Long[] selectedRows = form.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0)
        {
            ManageableServiceLocator.instance().getCarManageableService().delete(selectedRows);
        }

        return preload(mapping, actionForm, request, response);
    }

    private void saveMaxResultsWarning(HttpServletRequest request)
    {
        final HttpSession session = request.getSession();

        ActionMessages messages = (ActionMessages)session.getAttribute(Globals.MESSAGE_KEY);
        if (messages == null)
        {
            messages = new ActionMessages();
            session.setAttribute(Globals.MESSAGE_KEY, messages);
        }
        messages.add("org.andromda.bpm4struts.warningmessages", new ActionMessage("maximum.results.fetched.warning", "250"));
    }
}
