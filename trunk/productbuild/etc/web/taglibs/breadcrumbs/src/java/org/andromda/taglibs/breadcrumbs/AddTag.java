package org.andromda.taglibs.breadcrumbs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class AddTag extends TagSupport
{
    private String value = null;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int doStartTag() throws JspException
    {
        Object breadCrumbsObject = this.pageContext.getAttribute(BreadCrumbs.SESSION_KEY, PageContext.SESSION_SCOPE);

        if (breadCrumbsObject == null)
        {
            BreadCrumbs breadCrumbs = new BreadCrumbs();
            this.pageContext.setAttribute(BreadCrumbs.SESSION_KEY, breadCrumbs, PageContext.SESSION_SCOPE);
            addToBreadCrumbs(breadCrumbs);
        }
        else if (breadCrumbsObject instanceof BreadCrumbs)
        {
            addToBreadCrumbs((BreadCrumbs) breadCrumbsObject);
        }

        return Tag.SKIP_BODY;
    }

    private void addToBreadCrumbs(BreadCrumbs breadCrumbs)
    {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

        BreadCrumb newCrumb = new BreadCrumb(request.getRequestURL().toString(), value);

        if (breadCrumbs.isEmpty() == false)
        {
            BreadCrumb lastCrumb = (BreadCrumb) breadCrumbs.getLast();
            if (lastCrumb.hasSimilarProperties(newCrumb))
            {
                breadCrumbs.set(breadCrumbs.size() - 1, newCrumb);
                return;
            }
        }
        breadCrumbs.add(newCrumb);
    }
}
