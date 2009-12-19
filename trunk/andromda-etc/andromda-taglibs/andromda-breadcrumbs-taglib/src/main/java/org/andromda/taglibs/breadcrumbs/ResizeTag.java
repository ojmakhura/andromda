package org.andromda.taglibs.breadcrumbs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class ResizeTag extends TagSupport
{
    private int size = BreadCrumbs.DEFAULT_SIZE;

    public String getSize()
    {
        return String.valueOf(size);
    }

    public void setSize(String size)
    {
        this.size = Integer.parseInt(size);
    }

    public int doStartTag() throws JspException
    {
        Object breadCrumbsObject = this.pageContext.getAttribute(BreadCrumbs.SESSION_KEY, PageContext.SESSION_SCOPE);

        if (breadCrumbsObject == null)
        {
            BreadCrumbs breadCrumbs = new BreadCrumbs(size);
            this.pageContext.setAttribute(BreadCrumbs.SESSION_KEY, breadCrumbs, PageContext.SESSION_SCOPE);
        }
        else if (breadCrumbsObject instanceof BreadCrumbs)
        {
            ((BreadCrumbs) breadCrumbsObject).setMaxSize(size);
        }

        return Tag.SKIP_BODY;
    }
}
