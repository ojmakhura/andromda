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

public final class ManagePerson extends DispatchAction
{
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        request.getSession().setAttribute("manageableForm", actionForm);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.PersonForm form = (org.andromda.test.howto16.a.crud.PersonForm)actionForm;

        org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().create(
            (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("birthDateAsString"))) ? null : form.getBirthDate()
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("cars"))) ? null : form.getCars()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward read(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.PersonForm form = (org.andromda.test.howto16.a.crud.PersonForm)actionForm;

        final java.util.List list = org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().read(
            (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("birthDateAsString"))) ? null : form.getBirthDate()
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("cars"))) ? null : form.getCars()
        );
        form.setManageableList(list);

        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final java.util.Map backingLists = org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().readBackingLists();
        form.setCarsBackingList((java.util.List)backingLists.get("cars"));

        return mapping.getInputForward();
    }

    public ActionForward preload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.PersonForm form = (org.andromda.test.howto16.a.crud.PersonForm)actionForm;

        final java.util.List list = org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().readAll();
        form.setManageableList(list);


        if (list.size() >= 250)
        {
            saveMaxResultsWarning(request);
        }

        final java.util.Map backingLists = org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().readBackingLists();
        if (StringUtils.isNotBlank(request.getParameter("ref_Car")))
        {
            final java.lang.Long[] array = new java.lang.Long[1];
            array[0] = new java.lang.Long(request.getParameter("ref_Car"));
            form.setCars(array);
        }
        form.setCarsBackingList((java.util.List)backingLists.get("cars"));

        return mapping.getInputForward();
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return preload(mapping, actionForm, request, response);
    }

    public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.PersonForm form = (org.andromda.test.howto16.a.crud.PersonForm) actionForm;

        org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().update(
            (StringUtils.isBlank(request.getParameter("name"))) ? null : form.getName()
            , (StringUtils.isBlank(request.getParameter("birthDateAsString"))) ? null : form.getBirthDate()
            , (StringUtils.isBlank(request.getParameter("id"))) ? null : form.getId()
            , (StringUtils.isBlank(request.getParameter("cars"))) ? null : form.getCars()
        );

        return preload(mapping, actionForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final org.andromda.test.howto16.a.crud.PersonForm form = (org.andromda.test.howto16.a.crud.PersonForm) actionForm;

        final java.lang.Long[] selectedRows = form.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0)
        {
            org.andromda.test.ManageableServiceLocator.instance().getPersonManageableService().delete(selectedRows);
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
