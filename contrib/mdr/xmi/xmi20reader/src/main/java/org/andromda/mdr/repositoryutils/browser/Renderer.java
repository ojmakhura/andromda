package org.andromda.mdr.repositoryutils.browser;

import java.awt.Component;

import java.util.Collection;
import java.util.Iterator;

import javax.jmi.model.ModelElement;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.andromda.mdr.repositoryutils.MDRReflectionHelper;


final class Renderer
    extends DefaultTreeCellRenderer
{
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus1)
    {
        DefaultTreeCellRenderer treeCellRendererComponent =
            (DefaultTreeCellRenderer)super.getTreeCellRendererComponent(
                tree,
                value,
                sel,
                expanded,
                leaf,
                row,
                hasFocus1);
        Object userObject = ((DefaultMutableTreeNode)value).getUserObject();

        if (userObject instanceof RefPackage)
        {
            RefPackage rp = (RefPackage)userObject;
            ModelElement me = (ModelElement)rp.refMetaObject();
            treeCellRendererComponent.setText(me.getName());
        }

        if (userObject instanceof RefClass)
        {
            RefClass rc = (RefClass)userObject;
            ModelElement me = (ModelElement)rc.refMetaObject();
            String text2 = me.getName();
            text2 += "(" + rc.refAllOfClass().size() + ")";
            treeCellRendererComponent.setText(text2);
        }

        String type = "";
        if (userObject instanceof RefObject)
        {
            RefObject refObject = (RefObject)userObject;
            type = " - " + ((ModelElement)refObject.refClass().refMetaObject()).getName();

            // treeCellRendererComponent.setText("<html>"+type+userObject.toString()+"</html>");
        }

        String namedName = getNamedName(userObject);
        if (namedName != null)
        {
            // String text2 = userObject.toString();
            String text2 = "";
            text2 = " \"<b>" + namedName + "\"<b>" + text2 + type;
            treeCellRendererComponent.setText("<html>" + text2 + "</html>");
        }

        if (userObject instanceof Value)
        {
            Value v = (Value)userObject;
            String text2 = "";
            text2 += v.getFeature().getName();
            if (isShowType())
            {
                text2 += "(<b>";
                if (v.getFeature() instanceof Reference)
                {
                    text2 += ">";
                }
                text2 += v.getFeature().getType().getName();
                text2 += "</b>)";

                // if(v.feature.getMultiplicity().getUpper()>1)
                // {
                // text2 = "*"+text2;
                // }
            }
            Object realValue = v.getValue();
            text2 = appendValueText(
                    text2,
                    realValue);

            text2 = "<html>" + text2 + type + "</html>";
            treeCellRendererComponent.setText(text2);
        }

        return treeCellRendererComponent;
    }

    private String getNamedName(Object userObject)
    {
        if (userObject instanceof RefObject)
        {
            RefObject ro = (RefObject)userObject;
            RefClass proxyClass = ro.refClass();
            StructuralFeature feature = MDRReflectionHelper.getFeature(
                    "name",
                    proxyClass);
            if (feature != null)
            {
                return (String)ro.refGetValue("name");
            }
        }
        return null;
    }

    private String appendValueText(
        String text2,
        Object realValue)
    {
        String namedName2 = getNamedName(realValue);
        if (namedName2 != null)
        {
            text2 += String.valueOf(realValue);
            text2 += " <b>\"" + namedName2 + "\"</b>";
        }
        else if (realValue instanceof Collection)
        {
            Collection collection = (Collection)realValue;
            text2 += "(" + collection.size() + ")[";
            for (Iterator iter = collection.iterator(); iter.hasNext();)
            {
                Object oo = iter.next();
                text2 += String.valueOf(oo);

                String namedName = getNamedName(oo);
                if (namedName != null)
                {
                    text2 += " <b>\"" + namedName + "\"</b>";
                }

                if (iter.hasNext())
                {
                    text2 += ",";
                }
            }
            text2 += "]";
        }
        else
        {
            text2 += String.valueOf(realValue);
        }
        return text2;
    }

    private boolean isShowType()
    {
        return true;
    }
}