package org.andromda.modules.xmilink;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * Base interface for export strategies.
 * 
 * Export strategies visit an
 * {@link com.togethersoft.openapi.model.elements.Entity} and export the entity
 * using the {@link ExportContext}.
 * 
 * They are completely agnostic of the output format. I.e. one and the same
 * export strategy can be used to export an entity to XMI and plain text as
 * well.
 * 
 * @see org.andromda.modules.xmilink.ExportContext
 * @see org.andromda.modules.xmilink.io.Writer
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public interface IExportStrategy
{

    /**
     * Export the given entity and its subentities.
     * 
     * @param entity
     *            The entity to export.
     */
    void export(Entity entity);
}
