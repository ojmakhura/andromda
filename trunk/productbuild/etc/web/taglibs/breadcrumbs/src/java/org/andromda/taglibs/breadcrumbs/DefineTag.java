package org.andromda.taglibs.breadcrumbs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class DefineTag extends TagSupport
{
    private String id = null;
    private String toScope = null;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getToScope()
    {
        return toScope;
    }

    public void setToScope(String toScope)
    {
        this.toScope = toScope;
    }

    public int doStartTag() throws JspException
    {
        Object breadCrumbsObject = this.pageContext.getAttribute(BreadCrumbs.SESSION_KEY, PageContext.SESSION_SCOPE);

        if (breadCrumbsObject == null)
        {
            BreadCrumbs breadCrumbs = new BreadCrumbs();
            this.pageContext.setAttribute(BreadCrumbs.SESSION_KEY, breadCrumbs, PageContext.SESSION_SCOPE);
        }
        else if (breadCrumbsObject instanceof BreadCrumbs)
        {
            int scope = PageContext.PAGE_SCOPE;

            if (toScope != null)
            {
                if ("page".equals(toScope))
                    scope = PageContext.PAGE_SCOPE;
                if ("session".equals(toScope))
                    scope = PageContext.SESSION_SCOPE;
                if ("application".equals(toScope))
                    scope = PageContext.APPLICATION_SCOPE;
                if ("request".equals(toScope))
                    scope = PageContext.REQUEST_SCOPE;
            }

            this.pageContext.setAttribute(id, breadCrumbsObject, scope);
        }

        return Tag.SKIP_BODY;
    }
}
