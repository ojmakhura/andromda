package org.andromda.modules.xmilink;

import com.togethersoft.openapi.model.elements.Element;
import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Property;
import com.togethersoft.openapi.model.elements.UniqueName;
import com.togethersoft.openapi.model.enum.EntityEnumeration;
import com.togethersoft.openapi.model.enum.PropertyEnumeration;

/**
 * Base class for export strategies.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public abstract class BaseExportStrategy
        implements IExportStrategy
{

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.IExportStrategy#export(com.togethersoft.openapi.model.elements.Entity)
     */
    public final void export(Entity entity)
    {
        // some export strategies may store local state, which must be reset now
        reset();

        // some model elements and their children might need to be supressed
        if (doExportEntity(entity))
        {

            // some model elements might need to be supressed, but not their
            // children
            boolean suppress = suppressEntity(entity);

            if (!suppress)
            {
                exportEntityPrologue(entity);
                exportEntityContents(entity);
                exportEntityPrologueEnd(entity);
            }

            // the body normally contains the child nodes
            exportEntityBody(entity);

            if (!suppress)
            {
                exportEntityEpilogue(entity);
            }
        }
    }

    /**
     * Reset all private fields.
     * 
     * Some strategies store state information (e.g. the
     * {@link org.andromda.modules.xmilink.uml14.UMLClassExportStrategy} which
     * stores information about the stereotype of the class). Since strategies
     * are singletons, their state must be reset each time before they can
     * export a new model entity.
     */
    protected void reset()
    {
        // Intentionally left blank. Subclasses may choose to override this
        // method
        // according to their needs.
    }

    /**
     * Export the prologue of the entity.
     * 
     * @param entity
     *            The current entity.
     */
    protected abstract void exportEntityPrologue(Entity entity);

    /**
     * Export the contents of the entity itself. Usually, the properties will be
     * exported.
     * 
     * @param entity
     *            The current entity.
     */
    protected void exportEntityContents(Entity entity)
    {
        exportID(entity);
        exportProperties(entity);
    }

    /**
     * Exports the element's ID.
     * 
     * @param entity
     *            The current entity.
     */
    private void exportID(Entity entity)
    {
        String id = getID(entity);
        if (id != null)
        {
            ExportContext.getWriter().writeProperty("xmi.id", id);
        }
    }

    protected String getID(Entity entity)
    {
        if (entity != null)
        {
            UniqueName uName = entity.getUniqueName();
            if (uName != null)
            {
                return uName.getLocation();
            }
        }
        return null;
    }

    protected String getID(Element element)
    {
        if (element != null)
        {
            UniqueName uName = element.getUniqueName();
            if (uName != null)
            {
                return uName.getLocation();
            }
        }
        return null;
    }

    /**
     * Export the end of the entity prologue.
     * 
     * @param entity
     *            The current entity.
     */
    protected abstract void exportEntityPrologueEnd(Entity entity);

    /**
     * Exports the body of the entity. This usually means exporting the child
     * nodes.
     * 
     * @param entity
     *            The current entity.
     */
    protected void exportEntityBody(Entity entity)
    {
        if (doExportChildNodes())
        {
            exportChildNodes(entity);
        }
    }

    /**
     * Exports the dependencies of the entity.
     * 
     * @param entity
     *            The current entity.
     */
    private void exportDependencies(Entity entity)
    {
        if (doExportDependencies())
        {
            exportDependencyLinks(entity);
        }
    }

    /**
     * Let the respective strategy decide whether to export the child nodes or
     * not.
     * 
     * @return <code>true</code> if the childnodes should be exported,
     *         <code>false</code> otherwise.
     */
    protected abstract boolean doExportChildNodes();

    /**
     * Let the respective strategy decide whether to export the links or not.
     * 
     * @return <code>true</code> if the links should be exported,
     *         <code>false</code> otherwise.
     */
    protected abstract boolean doExportDependencies();

    /**
     * Decide whether to export the given property or not.
     * 
     * @param name
     *            The name of the property.
     * @param value
     *            The value of the property.
     * @return <code>true</code> if the property should be exported,
     *         <code>false</code> otherwise.
     */
    protected abstract boolean doExportProperty(String name, String value);

    /**
     * This method handles exporting of the properties of the given entity.
     * 
     * @param entity
     *            The current entity.
     */
    private void exportProperties(Entity entity)
    {
        if (entity != null)
        {
            PropertyEnumeration properties = entity.properties();
            if (properties != null)
            {
                while (properties.hasMoreElements())
                {
                    Property property = properties.next();
                    exportProperty(property);
                }
            }
        }
    }

    /**
     * Export a property.
     * 
     * @param property
     *            The property to export.
     */
    protected abstract void exportProperty(Property property);

    private void exportChildNodes(Entity entity)
    {
        if (entity != null)
        {
            EntityEnumeration childNodes = entity.childNodes();
            while (childNodes.hasMoreElements())
            {
                Entity childNode = childNodes.next();
                if (ExportContext.export(childNode) == true)
                {
                    exportDependencies(childNode);
                }
            }
        }
    }

    private void exportDependencyLinks(Entity entity)
    {
        if (entity != null)
        {
            EntityEnumeration links = entity.childLinks();
            while (links.hasMoreElements())
            {
                Entity link = links.next();
                if (doExportDependencyLink(link))
                {
                    ExportContext.export(link);
                }
            }
        }
    }

    protected abstract boolean doExportDependencyLink(Entity link);

    /**
     * Decide whether the current entity and it's child nodes will be exported.
     * 
     * @param entity
     *            The current entity.
     * @return <code>true</code> if the entity is to be exported,
     *         <code>false</code> otherwise.
     */
    protected boolean doExportEntity(Entity entity)
    {
        return true;
    }

    /**
     * Decides whether the current entity will be supressed, but not it's child
     * nodes.
     * 
     * @param entity
     *            The current enity.
     * @return <code>true</code> if the entity is to be exported,
     *         <code>false</code> otherwise.
     */
    protected boolean suppressEntity(Entity entity)
    {
        return false;
    }

    /**
     * Export the entity epilogue.
     * 
     * @param entity
     *            The current entity.
     */
    protected abstract void exportEntityEpilogue(Entity entity);

    protected String getMetaClass(Entity entity)
    {
        if (entity != null)
        {
            Property metaclassprop = entity.getProperty("$metaclass");
            if (metaclassprop != null)
            {
                return metaclassprop.getValue();
            }
        }
        return "";
    }

}
