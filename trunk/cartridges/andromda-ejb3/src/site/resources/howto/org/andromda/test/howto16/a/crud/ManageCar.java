// license-header java merge-point
package org.andromda.test.howto16.a.crud;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public final class ManageCar extends DispatchAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        request.getSession().setAttribute("manageableForm", actionForm);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.CarForm form = (org.andromda.test.howto16.a.crud.CarForm)actionForm;

        if (StringUtils.isNotBlank(request.getParameter("type")) && !org.andromda.test.howto16.a.CarType.literals().contains(form.getType()))
        {
            throw new IllegalArgumentException("type must be  one of " + org.andromda.test.howto16.a.CarType.literals());
        }

        org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().create(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : org.andromda.test.howto16.a.CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward read(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.CarForm form = (org.andromda.test.howto16.a.crud.CarForm)actionForm;

        final java.util.List list = org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().read(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : org.andromda.test.howto16.a.CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );
        form.setManageableList(list);

        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final java.util.Map backingLists = org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().readBackingLists();
        form.setOwnerBackingList((java.util.List)backingLists.get("owner"));

        return mapping.getInputForward();
    }

    public ActionForward preload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.CarForm form = (org.andromda.test.howto16.a.crud.CarForm)actionForm;

        final java.util.List list = org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().readAll();
        form.setManageableList(list);


        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final java.util.Map backingLists = org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().readBackingLists();
        if (StringUtils.isNotBlank(request.getParameter("ref_Person")))
        {
            form.setOwner(new java.lang.Long(request.getParameter("ref_Person")));
        }
        form.setOwnerBackingList((java.util.List)backingLists.get("owner"));

        return mapping.getInputForward();
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return preload(mapping, actionForm, request, response);
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.CarForm form = (org.andromda.test.howto16.a.crud.CarForm) actionForm;

        if (StringUtils.isNotBlank(request.getParameter("type")) && !org.andromda.test.howto16.a.CarType.literals().contains(form.getType()))
        {
            throw new IllegalArgumentException("type must be  one of " + org.andromda.test.howto16.a.CarType.literals());
        }

        org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().update(
            (StringUtils.isBlank(request.getParameter("serial"))) ? null : form.getSerial()
            , (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("type"))) ? null : org.andromda.test.howto16.a.CarType.fromString(form.getType())
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("owner"))) ? null : form.getOwner()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.CarForm form = (org.andromda.test.howto16.a.crud.CarForm) actionForm;

        final java.lang.Long[] selectedRows = form.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0)
        {
            org.andromda.test.ManageableServiceLocator.instance().getCarManageableService().delete(selectedRows);
        }

        return preload(mapping, actionForm, request, response);
    }

    private void saveMaxResultsWarning(HttpServletRequest request)
    {
        final HttpSession session = request.getSession();

        ActionMessages messages = (ActionMessages)session.getAttribute(org.apache.struts.Globals.MESSAGE_KEY);
        if (messages == null)
        {
            messages = new ActionMessages();
            session.setAttribute(org.apache.struts.Globals.MESSAGE_KEY, messages);
        }
        messages.add("org.andromda.bpm4struts.warningmessages", new ActionMessage("maximum.results.fetched.warning", "250"));
    }

}
