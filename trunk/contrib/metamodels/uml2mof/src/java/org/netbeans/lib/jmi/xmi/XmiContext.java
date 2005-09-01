/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2001 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.lib.jmi.xmi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.jmi.model.AliasType;
import javax.jmi.model.Attribute;
import javax.jmi.model.Classifier;
import javax.jmi.model.CollectionType;
import javax.jmi.model.DataTypeClass;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.EnumerationTypeClass;
import javax.jmi.model.Feature;
import javax.jmi.model.GeneralizableElement;
import javax.jmi.model.Import;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MofPackageClass;
import javax.jmi.model.MultiplicityType;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.model.NameNotResolvedException;
import javax.jmi.model.Namespace;
import javax.jmi.model.PrimitiveType;
import javax.jmi.model.PrimitiveTypeClass;
import javax.jmi.model.Reference;
import javax.jmi.model.ScopeKindEnum;
import javax.jmi.model.StructuralFeature;
import javax.jmi.model.StructureField;
import javax.jmi.model.StructureFieldClass;
import javax.jmi.model.StructureType;
import javax.jmi.model.StructureTypeClass;
import javax.jmi.model.Tag;
import javax.jmi.model.VisibilityKindEnum;
import javax.jmi.reflect.InvalidCallException;
import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefEnum;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.xml.parsers.ParserConfigurationException;

import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReferenceProvider;
import org.netbeans.api.xmi.XMIReferenceResolver;
import org.netbeans.lib.jmi.util.DebugException;
import org.netbeans.lib.jmi.util.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *  This class needs to be on the classpath before the regular MDR
 *  XmiContext, because it has a modified call in the {@link #resolve(XMIReferenceResolver.Client, RefPackage, String, XMIInputConfig, Collection)}
 *  operation.  Basically it sets the first element in the {@link #extents} array to the value of the 
 *  passed in extent (which is not done in MDR's implementation for some reason).  Maybe we should ask Martin Matula
 *  why this is?
 */
public class XmiContext implements XMIReferenceResolver {

    private static final String ANNOTATION = ""; //"Created by XMI SAX Reader to substitute Corba types.";
            
    private static final String [] PRIMITIVE_TYPES = {
        XmiConstants.BOOLEAN_TYPE, XmiConstants.DOUBLE_TYPE,
        XmiConstants.FLOAT_TYPE, XmiConstants.INTEGER_TYPE,
        XmiConstants.LONG_TYPE, XmiConstants.STRING_TYPE
    };
    
    // shared XmiElements .......................................................
    
    XmiElement.PrimitiveValue PRIMITIVE_VALUE = new XmiElement.PrimitiveValue (this);
    XmiElement.EnumerationValue ENUMERATION_VALUE = new XmiElement.EnumerationValue (this);
    XmiElement.ReferenceValue REFERENCE_VALUE = new XmiElement.ReferenceValue (this);
    
    // variables ................................................................

    String XMI_HREF; // name of attributes storing an href in XMI
    String XMI_IDREF; // name of attributes storing an idref in XMI
    String XMI_ID; // name of attributes storing an id in XMI
    
    boolean isXmi20; // if true, XMI version is 2.0
    String xmiNsPrefix; // prefix of XMI namespace in case of XMI 2.0 (followed by ':' if the prefix is non-empty)
    String xmlNsPrefix; // prefix of XML namespace in case of XMI 2.0 (followed by ':' if the prefix is non-empty)
    private HashMap nsURIToPrefix = new HashMap (); // XMI 2.0 only - namespase URI to namespace prefix mapping
    
    // storage of all outermost packages
    private HashMap outermostPackages = new HashMap ();
    // storage of all available namespaces
    private HashMap namespaces = new HashMap ();

    // extents passed as a parameter
    private RefPackage [] extents;
    // stores ModelPackage if it is one of the input packages
    private ModelPackage modelPackage = null;
    // primitive types package
    private MofPackage primitiveTypesPackage = null;
    // stores primitive types contained in @link #primitiveTypesPackage
    private HashMap primitiveTypes = null;
    // stores all Alias instances created instead of Corba primitive types
    private List corbaTypesReferencingPrimitiveTypes = new LinkedList ();

    // cache storing instance level attributes
    private HashMap instanceAttributes_cache = new HashMap ();    
    // cache storing references
    private HashMap instanceReferences_cache = new HashMap ();
    // cache storing instance level attributes and references by names
    private HashMap instanceElementsByName_cache = new HashMap ();
    // cache storing class level attributes
    private HashMap classAttributes_cache = new HashMap ();
    // cache storing class level attributes by names
    private HashMap classElementsByName_cache = new HashMap ();
    
    // cache storing structures' fields
    private HashMap structureFields_cache = new HashMap ();
    // cache storing labels' prefixes
    private HashMap labelPrefix_cache = new HashMap ();

    // list of all currently created outermost composite objects
    private Collection outermostInstances = new LinkedList ();
    // cache storing all already resolved names
    private HashMap resolvedNames_cache = new HashMap ();
    
    // all currently known references given by xmi.id, each element is a hashtable
    // containing references related to one XMI document
    private HashMap allXmiReferences = new HashMap ();
    /*
    // xmi references resolved for the currently read document
    private HashMap xmiReferences;
     */
    // internal unresolved reference, stores systemId -> ids_map, where ids_map is a
    // xmiId -> List of UnresolvedReferences mapping
    private HashMap unresolvedRefs = new HashMap ();
    // external unresolved references for the currently read document
    private HashMap unresolvedExternalRefs = new HashMap ();
    //
    private List listOfUnresolvedHRefs = new LinkedList ();
    //
    protected Set allUnresolvedHRefs = new HashSet ();
    //
    private HashMap hrefClients = new HashMap ();
    // true if the main document (not some external) is being processed
    boolean isMain = true;
    // set storing systemId's of all already read or currently being read documents
    private Set readDocuments = new HashSet ();
    // flag indicating whether an unknown elements should be ignored (otherwise a DebugException is thrown)
    private boolean ignoreUnknownElements = false;
    // unknown elements listener that is being notified about names of ignored elements
    private UnknownElementsListener elemsListener = null;
    
    // systemId of the currently read document
    private String thisSystemId = null;
    // mapping systemId (possibly relative) -> absolute systemId
    private HashMap systemIds = new HashMap ();
    // URL of the read document
    private URL docURL = null;
    // global cache used by @link #findOutermostPackages method
    private HashMap trackedPackages;
    // cache for resolved proxies enabling Enum and Struct creation and Association proxies
    private HashMap proxies_cache = new HashMap ();
    // configuration
    private XMIInputConfig config;

    // counter of created instances
    public int instancesCounter = 0;
    
    // xmi reference resolver
    private XMIReferenceResolver resolver = this;
    // xmi header consumer
    private XMIHeaderConsumer headerConsumer = null;
    
    // init .....................................................................
        
    public XmiContext (RefPackage [] extents, URL docURL, XMIInputConfig config) {
        this (extents, config);
        this.docURL = docURL;
        if (docURL == null)
            thisSystemId = ""; // NOI18N
        else
            thisSystemId = docURL.toString ();
        readDocuments.add (thisSystemId);
    }
    
    public XmiContext (RefPackage [] extents, XMIInputConfig config) {      
        this.extents = extents;
        this.config = config;
        resolver = config.getReferenceResolver ();
        if (resolver == null)
            resolver = this;
        if (config instanceof InputConfig) {
            headerConsumer = ((InputConfig) config).getHeaderConsumer ();
            ignoreUnknownElements = ((InputConfig) config).isUnknownElementsIgnored();
            elemsListener = ((InputConfig) config).getUnknownElementsListener();
        }        

        // check if ModelPackage is present among input packages (and store it)
        for (int i = 0; i < extents.length; i++)
            if (extents [i] instanceof ModelPackage) {
                modelPackage = (ModelPackage) extents [i];
                break;
            }
    
        isMain = true;
        unresolvedRefs = new HashMap ();
        allUnresolvedHRefs = new HashSet ();        
        allXmiReferences = new HashMap ();
    }

    // methods ..................................................................
    
    public void setVersion (Attributes attrs) {
        xmiNsPrefix = null;        
        String version = attrs.getValue (XmiConstants.XMI_VersionAtt);
        if (version != null) {
            if (!(version.equals ("1.0") || version.equals ("1.1") || version.equals ("1.2"))) {
                throw new DebugException ("Malformed version parameter or unsupported verion of XMI: " 
                    + XmiConstants.XMI20_VERSION + " = " + version);
            }
            isXmi20 = false;                
        } else {
            isXmi20 = true;
            xmiNsPrefix = "";
            for (int x = 0; x < attrs.getLength (); x++) {
                String attrValue = attrs.getValue (x);
                if (attrValue.equals (XmiConstants.XMI_NAMESPACE_URI)) {
                    String attrName = attrs.getQName (x);
                    if (attrName.startsWith ("xmlns:")) {
                        xmiNsPrefix = attrName.substring (6, attrName.length ()) + ":";
                        break;
                    }
                } // if            
            } // for
            version = attrs.getValue (xmiNsPrefix + XmiConstants.XMI20_VERSION);
            if (version == null) {
                throw new DebugException ("XMI version attribute is missing.");
            }
            if (!version.equals ("2.0")) {
                throw new DebugException ("Malformed version parameter or unsupported verion of XMI: " 
                    + xmiNsPrefix + XmiConstants.XMI20_VERSION + " = " + version);
            }
        } // else                
    
        if (isXmi20) {
            XMI_HREF = xmiNsPrefix + XmiConstants.XMI_HREF;
            XMI_ID = xmiNsPrefix + XmiConstants.XMI20_ID;
            XMI_IDREF = xmiNsPrefix + XmiConstants.XMI20_IDREF;
            
            // obtain namespace declarations
            for (int x = 0; x < attrs.getLength (); x++) {
                String attrName = attrs.getQName (x);
                String attrValue = attrs.getValue (x);
                if (attrName.equals ("xmlns")) {
                    if (attrValue.equals (XmiConstants.XML_SCHEMA_NAMESPACE_URI))
                        xmlNsPrefix = "";
                    else
                        nsURIToPrefix.put (attrValue, null);
                } else if (attrName.startsWith ("xmlns:")) {
                    String name = attrName.substring (6, attrName.length ());
                    if (attrValue.equals (XmiConstants.XML_SCHEMA_NAMESPACE_URI))
                        xmlNsPrefix = name + ":";
                    else {
                        nsURIToPrefix.put (attrValue, name);
                    }
                }
            } // for
        } else {
            XMI_HREF = XmiConstants.XMI_HREF;
            XMI_ID = XmiConstants.XMI_ID;
            XMI_IDREF = XmiConstants.XMI_IDREF;
        }
        
        trackedPackages = new HashMap (); // used as global cache for findOutermostPackages calls
        for (int i = 0; i < extents.length; i++)
        {
            findOutermostPackages (extents [i].refOutermostPackage());
        }
    }
    
    /**
     * Detects all outermost packages related to the input extents and stores them.
     * Stores all related namespaces as well.
     */
    private void findOutermostPackages (RefPackage pkg) {
        if (trackedPackages.get (pkg) != null)
            return;
        String name, uri;
        MofPackage metaObj = (MofPackage) pkg.refMetaObject ();
        if (metaObj.getContainer () == null) { 
            Iterator iter = metaObj.getQualifiedName ().iterator ();
            String fqName = (String) iter.next ();
            while (iter.hasNext ())
                fqName = fqName.concat (XmiConstants.DOT_SEPARATOR).concat ((String) iter.next ());
            outermostPackages.put (fqName, pkg);
        }
        
        if (isXmi20) {
            uri = getTagValue (metaObj, XmiConstants.TAG_NS_URI);
            if (uri == null) {
                throw new DebugException ("No tag specifying a namespace uri is attached to MofPackage " + metaObj.getName ());
            }
            name = (String) nsURIToPrefix.get (uri);
            if (name == null) {
                throw new DebugException ("XMI document does not contain namespace declaration for " + uri + ", MofPackage " + metaObj.getName ());
            }
        } else {            
            name = getTagValue (metaObj, XmiConstants.TAGID_XMI_NAMESPACE);            
        }
        if (name != null) {
            List list = (List) namespaces.get (name);
            if (list == null) {
                list = new LinkedList ();
                namespaces.put (name, list);
            }
            list.add (pkg);
        }
        
        trackedPackages.put (pkg, pkg);
        Iterator iter =  pkg.refAllPackages ().iterator ();
        while (iter.hasNext ()) {
            findOutermostPackages ((RefPackage) iter.next ());
        }
    }

    public static String getTagValue (ModelElement element, String tagId) {
        Collection tags = ((ModelPackage) element.refImmediatePackage()).
            getAttachesTo().getTag (element);
        Tag tag = null;
        for (Iterator it = tags.iterator(); it.hasNext();) {
            Object obj = it.next ();
            if (!(obj instanceof Tag))
                continue;
            Tag temp = (Tag) obj;
            if (tagId.equals (temp.getTagId ())) {
                tag = temp;
                break;
            }
        }
        if (tag == null)
            return null;
        Collection values = tag.getValues ();
        if (values.size () == 0)
            return null;
        return (String) values.iterator ().next ();
    }
    
    /**
     * This method is called to applay already read differences on a given document.
     *
     * @param href link to an external document
     * @param diffs list of differences (see @link #XmiElement.Difference.Diff) to be applyed
     */
    public void resolveDifferences (String href, HashMap diffs) {
        if (href == null)
            throw new DebugException ("External document to applay differences on is not specified.");
        try {
            XmiSAXReader reader = new XmiSAXReader (config);
            URL doc;
            if (docURL != null) {
                String path = docURL.getPath ();
                int copyTo = path.lastIndexOf ("/");
                if (copyTo > -1) {
                    href = path.substring (0, copyTo) + "/" + href;
                }
                doc = new URL (docURL.getProtocol (), this.docURL.getHost (), href);
            } else {
                doc = new URL (href);
            }
            Collection res = reader.read (doc, extents, null, diffs);
            outermostInstances.addAll (res);
        } catch (Exception e) {
            throw new DebugException (e.getMessage ());
        }
    }
    
    /**
     * This private method is called to obtain and cache all class and instance 
     * scoped attributes and references.
     * Related caches are: 
     * @link #instanceAttributes_cache, @link #instanceReferences_cache, 
     * @link #instanceElementsByName_cache
     */
    private void cacheContainedElements (RefClass proxyClass) {
        List temp = new LinkedList ();
        MofClass metaProxyClass = (MofClass) proxyClass.refMetaObject ();
        List superClasses = metaProxyClass.allSupertypes ();
        Namespace namespace = null;
        Iterator it = superClasses.iterator ();
        while (it.hasNext ()) {
            namespace = (Namespace) it.next ();
            temp.addAll (namespace.getContents ());
        }
        temp.addAll (metaProxyClass.getContents ());
        List instanceAttributes = new LinkedList ();
        List instanceReferences = new LinkedList ();
        List classAttributes = new LinkedList ();
        HashMap instanceElementsByName = new HashMap ();
        HashMap classElementsByName = new HashMap ();
        
        it = temp.iterator ();
        while (it.hasNext ()) {
            RefObject refObject = (RefObject) it.next ();
            if (refObject instanceof Feature) {
                boolean instanceLevel = ((Feature) refObject).getScope ().equals (ScopeKindEnum.INSTANCE_LEVEL);
                if ((refObject instanceof Attribute) && (!((Attribute) refObject).isDerived ())) {
                    if (instanceLevel) {
                        instanceAttributes.add (refObject);
                        instanceElementsByName.put 
                            (((Attribute) refObject).getName (), refObject);
                    } else {
                        classAttributes.add (refObject);
                        classElementsByName.put 
                            (((Attribute) refObject).getName (), refObject);
                    }
                } else if (refObject instanceof Reference) {
                    if (instanceLevel) {
                        instanceReferences.add (refObject);
                        instanceElementsByName.put 
                            (((Reference) refObject).getName (), refObject);
                    }
                } // else
            } // if (refObject instanceof Feature)
        } // while
        instanceAttributes_cache.put (proxyClass, instanceAttributes);
        instanceReferences_cache.put (proxyClass, instanceReferences);        
        instanceElementsByName_cache.put (proxyClass, instanceElementsByName);
        classAttributes_cache.put (proxyClass, classAttributes);
        classElementsByName_cache.put (proxyClass, instanceElementsByName);
    }

    /**
     * For a given class proxy, returns list of all instance-scoped attributes 
     * (references are not included).
     */
    public List instanceAttributes (RefClass refClass) {
        List list = (List) instanceAttributes_cache.get (refClass);
        if (list == null) {
            cacheContainedElements (refClass);
            list = (List) instanceAttributes_cache.get (refClass);
        }
        return list;
    }
    
    /**
     * For a given class proxy, returns list of all (instance-scoped) references. 
     */
    public List instanceReferences (RefClass refClass) {
        List list = (List) instanceReferences_cache.get (refClass);
        if (list == null) {
            cacheContainedElements (refClass);
            list = (List) instanceReferences_cache.get (refClass);
        }
        return list;
    }

    /**
     * Returns an attribute, resp. a reference according to given class proxy and 
     * attribute, resp. reference name.
     * Used when attribute values stored as XLM element attributes are being resolved.
     */
    public StructuralFeature instanceElementByName (RefClass refClass, String name) {
        HashMap map = (HashMap) instanceElementsByName_cache.get (refClass);
        if (map == null) {
            cacheContainedElements (refClass);
            map = (HashMap) instanceElementsByName_cache.get (refClass);
        }
        StructuralFeature feature = (StructuralFeature) map.get (name);
        if (feature == null) {
            // [PENDING]
            // throw new DebugException ("Attribute name cannot be resolved: " + name);
        }
        return feature;
    }

    public List staticAttributes (RefClass refClass) {
        List list = (List) classAttributes_cache.get (refClass);
        if (list == null) {
            cacheContainedElements (refClass);
            list = (List) classAttributes_cache.get (refClass);
        }
        return list;
    }
    
    public Attribute staticAttributeByName (RefClass refClass, String name) {
        HashMap map = (HashMap) classElementsByName_cache.get (refClass);
        if (map == null) {
            cacheContainedElements (refClass);
            map = (HashMap) classElementsByName_cache.get (refClass);
        }
        return (Attribute) map.get (name);
    }
    
    /**
     * Returns list of all fields belonging to the given StructureType.
     */
    public List structureFields (StructureType type) {
        List fields = (List) structureFields_cache.get (type);
        if (fields != null)
            return fields;
        // find fields and cache them
        fields = new LinkedList ();
        Iterator content = type.getContents ().iterator ();
        while (content.hasNext ()) {
            Object element = content.next ();
            if (element instanceof StructureField)
                fields.add (element);
        } // while
        structureFields_cache.put (type, fields);
        return fields;
    }
    
    /**
     * Returns labels prefix given by "unprefix" tag attached to EnumerationType or
     * the empty String if no such tag is present.
     */
    public String labelPrefix (EnumerationType type) {
        if (isXmi20)
            return "";
        String prefix = (String) labelPrefix_cache.get (type);
        if (prefix != null)
            return prefix;
        prefix = getTagValue (type, XmiConstants.TAGID_XMI_ENUMERATION_UNPREFIX);
        if (prefix == null)
            prefix = "";
        labelPrefix_cache.put (type, prefix);
        return prefix;
    }
    
    /**
     * Resolves given fully qualified or namespace prefixed element name.
     * If the name corresponds to MofClass, related proxy class is returned instead of it.
     */
    public Object resolveElementName (String fqName) {
        Object result = resolvedNames_cache.get (fqName);
        if (result != null)
            return result;

        int pos = fqName.indexOf (XmiConstants.NS_SEPARATOR);
        RefPackage outermostPackage = null;
        if ((pos < 0) && !isXmi20) {
            // namespace not presented in name, fully qualified name is given
            int index = fqName.indexOf (XmiConstants.DOT_SEPARATOR);
            String packageName = fqName;
            while (index > -1) {
                packageName = packageName.substring (0, index);                
                outermostPackage = (RefPackage) outermostPackages.get (packageName);
                if (outermostPackage != null)
                    break;
                index = packageName.indexOf (XmiConstants.DOT_SEPARATOR);
            }            
            if (outermostPackage == null) {
                if (ignoreUnknownElements) {
                    return null;
                }
                    throw new DebugException ("Element name cannot be resolved, unknown package: " + fqName);
               
            }
            StringTokenizer tokenizer = new StringTokenizer (fqName, ".");
            LinkedList nameParts = new LinkedList ();
            tokenizer.nextToken (); // skip the outermost package name
            while (tokenizer.hasMoreTokens ())
                nameParts.add (tokenizer.nextToken ());
            Namespace namespace = (Namespace) outermostPackage.refMetaObject ();
            try {
                RefObject obj = namespace.resolveQualifiedName (nameParts);
                if (obj instanceof MofClass) {
                    RefPackage refPkg = (RefPackage) findProxy ((MofClass)obj);
                    if (refPkg != null)                        
                        result = refPkg.refClass (obj);                    
                } else
                    result = obj;
            } catch (NameNotResolvedException e) {
            }
        } else {
            // name contains namespace prefix, it is of the form NAMESPACE:NAME1(.NAME2)?
            String nsPrefixName;
            if (pos < 0) {
                nsPrefixName = ""; // XMI 2.0 & default namespace
            } else {
                nsPrefixName = fqName.substring (0, pos);
            }
            List packages = (List) namespaces.get (nsPrefixName);
            if (packages == null) {
                if (ignoreUnknownElements) {
                    return null;
                }
                    throw new DebugException ("Namespace cannot be resolved: " + nsPrefixName);
            }
            
            int pos2 = fqName.indexOf (XmiConstants.DOT_SEPARATOR);
            Iterator iter = packages.iterator ();
            while (iter.hasNext ()) {
                outermostPackage = (RefPackage) iter.next ();                
                try {
                    if (pos2 == -1) {
                        // 1) a "class level" element
                        RefObject metaClass = ((GeneralizableElement) outermostPackage.refMetaObject ()).
                            lookupElementExtended (fqName.substring (pos + 1, fqName.length ()));
                        if (metaClass instanceof MofClass)
                            result = outermostPackage.refClass (metaClass);
                        else
                           result = metaClass;
                    } else {
                        // 2) an "attribute level" element
                        Classifier element = (Classifier) ((GeneralizableElement) outermostPackage.refMetaObject ()).
                            lookupElementExtended (fqName.substring (pos + 1, pos2));
                        if (element == null)
                            continue;
                        result = element.lookupElementExtended (fqName.substring (pos2 + 1, fqName.length ()));
                    } // else
                } catch (NameNotFoundException e) {
                }
                if (result != null)
                    break;
            } // while
                
        } // else

        if (result == null) {
            if (ignoreUnknownElements) {
                return null;
            }
                throw new DebugException("Name cannot be resolved: " + fqName);
        }
        resolvedNames_cache.put (fqName, result);
        return result;
    }

    /**
     * Called by XMIElement.Header to process the read XMI reader.
     */
    public void receiveHeader (String header) {        
        if (headerConsumer != null) {
            byte [] bytes = new byte [header.length()];
            for (int x = 0; x < bytes.length; x++) {
                bytes [x] = (byte) header.charAt (x);
            }
            headerConsumer.consumeHeader (new ByteArrayInputStream (bytes));
        } // if
    }
        
    /**
     * Called when the whole XMI document has been read.
     * Handles setting of Imports related to Aliases created instead of Corba primitive types.
     */
    public void finish () {
        if (!isMain)
            return;
        
        HashMap checkedPackages = new HashMap ();
        Iterator iter = corbaTypesReferencingPrimitiveTypes.iterator ();
        while (iter.hasNext ()) {
            ModelElement element = (ModelElement) iter.next ();
            Namespace container = element.getContainer ();
            Namespace owner = container.getContainer ();
            while (owner != null) {
                container = owner;
                owner = owner.getContainer ();
            }
            while (container.getContainer () != null)
                container = container.getContainer ();
            if ((container instanceof MofPackage) && (checkedPackages.get (container) == null)) {
                MofPackage pkg = (MofPackage) container;
                Iterator content = pkg.getContents ().iterator ();
                boolean found = false;
                while (content.hasNext ()) {
                    ModelElement el = (ModelElement) content.next ();
                    if (el instanceof Import) {
                        if (primitiveTypesPackage.equals (((Import) el).getImportedNamespace ())) {
                            found = true;
                            break;
                        } // if
                    } // if
                } // while
                if (!found) {
                    Import imp = modelPackage.getImport ().createImport (
                        XmiConstants.PRIMITIVE_TYPES_PACKAGE, ANNOTATION,
                        VisibilityKindEnum.PUBLIC_VIS, false
                    );
                    imp.setImportedNamespace (primitiveTypesPackage);
                    imp.setContainer (pkg);
                } // if
                checkedPackages.put (pkg, pkg);
            } // if
        } // while
        checkedPackages = null;
        
        Logger.getDefault().log ("Number of created instances: " + instancesCounter);
        
    }
    
    public XmiElement resolveInstanceOrReference (XmiElement parent,
        String qName, Attributes attrs) {
        String idRef = attrs.getValue (XMI_IDREF);
        if (idRef != null) {
            REFERENCE_VALUE.init (parent, idRef);
            return REFERENCE_VALUE;
        }
        idRef = attrs.getValue (XMI_HREF);
        if (idRef != null) {
            REFERENCE_VALUE.initExternal (parent, idRef);
            return REFERENCE_VALUE;
        }        
        Object ref = resolveElementName (qName);
        if (ref == null && ignoreUnknownElements) {
            return new XmiElement.Dummy(parent, this, qName);
        }
        if (ref instanceof DataTypeClass) {
            // ===================================
            // MOF 1.3 compatibility
            // ===================================
            return new XmiElement.DataTypeElement (parent, this, qName, attrs);
        }
        return new XmiElement.Instance (parent, this, qName, (RefClass) ref, attrs);
    }
    
    /**
     * Adds created outermost object to the collection returned by
     * @link #XmiSAXReader.read method as a result.
     */
    public void addOutermostObject (RefObject obj) {
        outermostInstances.add (obj);        
    }
    
    /**
     * When the reading of XMI is finished, returns collection of all created 
     * outermost objects.
     */
    public Collection getOutermostObjects () {
        return outermostInstances;
    }
    
    // XMIReferenceResolver implementation ......................................
    
    public void register(String systemId, String xmiId, RefObject object) {
        String href = systemId + '#' + xmiId;
        List list = (List) hrefClients.remove (href);
        if (list != null) {
            Iterator iter = list.iterator ();
            while (iter.hasNext ()) {
                ((Client) iter.next ()).resolvedReference (href, object, true);
            } // while
            
        } // if
    }
        
    public void resolve(XMIReferenceResolver.Client client, RefPackage extent, String systemId, XMIInputConfig configuration, Collection hrefs) throws MalformedXMIException, IOException {
        // - set the extent back (this solves the issue of the first extent being null and therefore causing a null
        //   pointer - this is not present in the MDR version)
        extents[0] = extent;
        String href;
        listOfUnresolvedHRefs = new LinkedList ();
        unresolvedExternalRefs = new HashMap ();
        
        Iterator iter = hrefs.iterator ();
        while (iter.hasNext ()) {
            href = (String) iter.next ();
            List list = (List) hrefClients.get (href);
            if (list == null) {
                list = new LinkedList ();
                hrefClients.put (href, list);
            }
            list.add (client);
        } // while
                
        iter = hrefs.iterator ();
        while (iter.hasNext ()) {
            href = (String) iter.next ();
            XMIReferenceProvider.XMIReference ref = toXMIReference (href);
            String systId = ref.getSystemId ();
            String xmiId = ref.getXmiId ();            
            RefObject obj = getReference (systId, xmiId);
            if (obj != null) {
                register (systId, xmiId, obj);
            } else {
                if (!readDocuments.contains (systId)) {
                    URL url = toURL (systId);
                    if ((url == null) || (!readDocuments.contains (url.toString ()))) {
                        readExternalDocument (systId);
                        obj = getReference (systId, xmiId);
                        if (obj != null) {
                            register (systId, xmiId, obj);
                        }
                    }
                } // if
            }
        } // while
    }

    // ..........................................................................

    /**
     * If an instance identified by a given xmiId has been created, it is returned,
     * otherwise <code>null</code> is returned.
     */
    public RefObject getReference (String xmiId) {
        return getReference (thisSystemId, xmiId);
    }

    public RefObject getReference (String docId, String xmiId) {
        HashMap map = (HashMap) allXmiReferences.get (absoluteSystemId (docId));
        return (map != null) ? (RefObject) map.get (xmiId) : null;
    }

    /**
     * Stores a created instance having xmiId as a pair (xmiId, obj).
     */
    public void putReference (String systemId, String xmiId, RefObject obj) {
        systemId = absoluteSystemId (systemId);
        Map map = (Map) allXmiReferences.get (systemId);
        if (map == null) {
            map = new HashMap ();
            allXmiReferences.put (systemId, map);
        }
        if (map.put (xmiId, obj) != null)
            throw new DebugException ("The same value of xmi.idref used second time: " + xmiId + ", " + systemId);

        map = (Map) unresolvedRefs.get (systemId);
        if (map != null) {
            List list = (List) map.remove (xmiId);
            if (list != null) {
                Iterator iter = list.iterator ();
                while (iter.hasNext ()) {
                    ((XmiElement.UnresolvedReference) iter.next ()).referenceResolved (obj);
                } // while
                if (map.size () == 0)
                    unresolvedRefs.remove (systemId);
            } // if
        } // if
        resolver.register (systemId, xmiId, obj);
    }

    /**
     * Registers UnresolvedReference, i.e. stores it in a HashMap using xmiId as a key.
     */
    public void registerUnresolvedRef (String xmiId, XmiElement.UnresolvedReference element) {
        Map map = (Map) unresolvedRefs.get (thisSystemId);
        if (map == null) {
            map = new HashMap ();
            unresolvedRefs.put (thisSystemId, map);
        }
        List list = (List) map.get (xmiId);
        if (list == null) {
            list = new LinkedList ();
            map.put (xmiId, list);
        }
        list.add (element);
    }

    /**
     * Registers external unresolved reference in context of given document.
     */
    public void registerUnresolvedExternalRef (String docId, String xmiId, XmiElement.UnresolvedReference element) {
        String href = absoluteSystemId (docId) + '#' + xmiId;
        List list = (List) unresolvedExternalRefs.get (href);        
        if (list == null) {
            list = new LinkedList ();            
            unresolvedExternalRefs.put (href, list);
            listOfUnresolvedHRefs.add (href);
            allUnresolvedHRefs.add (href);
        }
        list.add (element);
    }

    public void resolveExternalReferences () {        
        try {
            if (listOfUnresolvedHRefs.size () > 0) {    
                resolver.resolve (
                    new Client (unresolvedExternalRefs),
                    extents [0], // [PENDING]
                    thisSystemId,
                    config,
                    listOfUnresolvedHRefs
                );
                listOfUnresolvedHRefs = new LinkedList ();
                unresolvedExternalRefs = new HashMap ();
            } // if
        } catch (MalformedXMIException e) {
            throw new DebugException (e.getMessage ());
        } catch (IOException e) {
            throw new DebugException (e.getMessage ());
        }
    }

    public XMIReferenceProvider.XMIReference toXMIReference (String href) {
        int index = href.lastIndexOf ('#');
        if (index < 0)
            index = href.lastIndexOf ('|');
        if (index < 0)
            throw new DebugException ("Bad href, # delimiter character missing: " + href);
        String docId = href.substring (0, index);
        String xmiId = href.substring (index + 1, href.length ());
        if (xmiId.length () == 0)
            throw new DebugException ("Invalid href format: " + href);
        return new XMIReferenceProvider.XMIReference (docId, xmiId);
    }

    public URL toURL (String systemId) {        
        URL doc = null;
        try {
            doc = new URL (systemId);
        } catch (MalformedURLException e) {
            if (docURL != null) {
                String path = docURL.getPath ();
                String href = systemId;
                int copyTo = path.lastIndexOf ("/");
                if (copyTo > -1) {
                    href = path.substring (0, copyTo) + "/" + href;
                }
                try {
                    doc = new URL (docURL.getProtocol (), this.docURL.getHost (), href);                
                    try {
                        (doc.openStream ()).close ();
                    } catch (IOException ioe) {
                    }
                } catch (MalformedURLException mue) {
                }
            } // if
        }        
        return doc;
    }

    public String absoluteSystemId (String id) {
        String result = (String) systemIds.get (id);
        if (result == null) {
            URL url = toURL (id);
            if (url == null)
                result = id;                
            else
                result = url.toString ();
            systemIds.put (id, result);
        }
        return result;
    }

    public void readExternalDocument (String systemId) {        
        try {
            boolean temp_isMain = isMain;
            String temp_thisSystemId = thisSystemId;
            
            isMain = false;
            thisSystemId = absoluteSystemId (systemId);
            
            URL doc = toURL (systemId);
            if (doc == null)
                throw new DebugException ("Cannot create URL: " + systemId);
            
            XmiSAXReader reader = new XmiSAXReader (this, config);
            readDocuments.add (thisSystemId);
            reader.read (doc, extents, null);            
            
            isMain = temp_isMain;
            thisSystemId = temp_thisSystemId;
            
        } catch (MalformedURLException e) {
            throw new DebugException (e.getMessage ());
        } catch (IOException e) {
            throw new DebugException (e.getMessage ());
        } catch (SAXException e) {
            throw new DebugException (e.getMessage ());
        } catch (ParserConfigurationException e) {
            throw new DebugException (e.getMessage ());
        }
    }

    public String getCurrentDocId () {
        return thisSystemId;
    }            

    /**
     * True iff all registered unresolved references has been already resolved.
     */
    public boolean allReferencesResolved () {
        return (unresolvedRefs.size () == 0) && (allUnresolvedHRefs.size () == 0);
    }

    /**
     * Returns one of the unresolved references. (Used to report an error 
     * when the reading is finished and there are still some unresolved references.)
     */
    public String getUnresolvedRefId () {
        if (unresolvedRefs.size () > 0) {
            Map map = (Map) ((Map.Entry) unresolvedRefs.entrySet ().iterator ().next ()).getValue ();
            return (String) map.keySet ().iterator ().next ();
        } else if (allUnresolvedHRefs.size () > 0) {
            return (String) allUnresolvedHRefs.iterator ().next ();
        }
        return null;
    }

    /**
     * @param type type that should be resolved
     * @param asText text representation of a value
     *
     * @return primitive type value
     */
    public static Object resolvePrimitiveValue (PrimitiveType type, String asText) {
        String typeName = type.getName ();
        if (XmiConstants.BOOLEAN_TYPE.equals (typeName))
            return Boolean.valueOf(asText.toString ());
        if (XmiConstants.DOUBLE_TYPE.equals (typeName))
            return new Double (asText.toString ());
        if (XmiConstants.FLOAT_TYPE.equals (typeName))
            return new Float (asText.toString ());
        if (XmiConstants.INTEGER_TYPE.equals (typeName))
            return new Integer (asText.toString ());
        if (XmiConstants.LONG_TYPE.equals (typeName))
            return new Long (asText.toString ());
        if (XmiConstants.STRING_TYPE.equals (typeName))
            return (asText == null) ? "" : asText.toString ();
        throw new DebugException ("unrecognized type name: " + typeName);
    }

    public RefEnum resolveEnumerationValue (EnumerationType type, String asText) {        
        RefBaseObject proxy = findProxy (type);
        if (proxy == null) {
            throw new DebugException ("Proxy not found: " + type.getName ());
        }
                
        // see @link #labelPrefix method for clarification ...
        asText = labelPrefix (type) + asText;
        
        try {
            if (proxy instanceof RefClass)
            {
                return ((RefClass) proxy).refGetEnum (type, asText);
            }
                return ((RefPackage) proxy).refGetEnum (type, asText);
        } catch (InvalidCallException e) {
            throw new DebugException("Error creating literal \'" + asText + "\'. Probably invalid literal name for enumeration " + type.getName());
        }
    }

    /**
     * Finds proxy object related to the container of a given meta model element.
     *
     * @param element meta model element
     * @return related proxy object or <code>null</code> if proxy cannot be found
     */
    public RefBaseObject findProxy (ModelElement element) {
        RefBaseObject proxy = (RefBaseObject) proxies_cache.get (element);
        if (proxy != null)
            return proxy;
        LinkedList path = new LinkedList ();
        ModelElement container = element.getContainer ();
        while (container != null) {
            path.add (container);
            container = container.getContainer ();
        }
        MofPackage mofPackage = (MofPackage) path.removeLast ();
        RefPackage refPackage = (RefPackage) outermostPackages.get (mofPackage.getName ());
        if (refPackage == null) {
            // try to find an outermost package such that its meta package is a subclass of the requsted mofPackage
            Iterator iter = outermostPackages.entrySet ().iterator ();
            while (iter.hasNext ()) {
                RefPackage ref = (RefPackage) ((Map.Entry) iter.next ()).getValue ();
                MofPackage meta = (MofPackage) ref.refMetaObject ();
                if (meta.allSupertypes().contains(mofPackage)) {
                    refPackage = ref;
                    break;
                }
            } // while
        } // if

        if (refPackage == null)
            return null;        
        if (path.size () == 0)
            proxy = refPackage;        
        while (path.size () > 0) {
            ModelElement elem = (ModelElement) path.removeLast ();
            if (elem instanceof MofPackage) {
                refPackage = refPackage.refPackage (elem);
                if (path.size () == 0)
                    proxy = refPackage;
            } else {
                if ((elem instanceof MofClass) && (path.size () == 0)) {
                    RefClass refClass = refPackage.refClass (elem);                    
                    proxy = refClass;
                } else
                    break;
            } // else
        } // while
        if (proxy != null)
            proxies_cache.put (element, proxy);
        return proxy;
    }

    /**
     * Returns true if given StructuralFeature is multivalued.
     */
    public static boolean isMultivalued (StructuralFeature attr) {
        MultiplicityType multType = attr.getMultiplicity ();
        int upper = multType.getUpper ();
        return upper != 1;
    }

    /**
     * Returns fully qualified name of an element as a String.
     */
    public static String getQualifiedName (Classifier type) {
        List list = type.getQualifiedName ();
        Iterator iter = list.iterator ();
        String name = (String) iter.next ();
        while (iter.hasNext ())
            name = name + '.' + (String) iter.next ();
        return name;
    }

    public XmiElement resolveValue (XmiElement parent, Classifier type, Attributes attrs) {
        while (type instanceof AliasType)
            type = ((AliasType) type).getType ();
        if (type instanceof PrimitiveType) {
            PRIMITIVE_VALUE.init (parent, (PrimitiveType) type, attrs);
            return PRIMITIVE_VALUE;
        }
        if (type instanceof EnumerationType) {
            ENUMERATION_VALUE.init (parent, (EnumerationType) type, attrs);
            return ENUMERATION_VALUE;
        }
        if (type instanceof MofClass) {
            boolean isNull = false;
            if (isXmi20) {
                String value = attrs.getValue (xmlNsPrefix + XmiConstants.XMI20_NIL);
                isNull = ((value != null) && value.equals ("true"));
            } // if
            return new XmiElement.ObjectValues 
                (parent, this, (XmiElement.ReferencesCounter) parent, isNull);
        }
        if (type instanceof StructureType) {
            return new XmiElement.StructureValues
                (parent, this, (StructureType) type);
        }
        if (type instanceof CollectionType) {
            return new XmiElement.CollectionValues
                (parent, this, (CollectionType) type);
        }
        throw new DebugException ("Type not resolved: " + type.getName ());
    }

    public boolean ignoreUnknownElements() {
        return ignoreUnknownElements;
    }
    
    public void unknownElementFound(String name) {
        if (elemsListener != null) {
            elemsListener.elementFound(name);
        }
    }
    
    /**
     * Returns default value for a given attribute.
     */
    public static Object defaultValue (Attribute attr) {
        Classifier type = attr.getType ();
        if (!(type instanceof PrimitiveType))
            return null;
        MultiplicityType multType = attr.getMultiplicity ();
        boolean isMultivalued = multType.getUpper () != 1;
        int lower = multType.getLower ();
        if (lower == 0) {
            if (isMultivalued)
            {
                return new LinkedList ();
            }
                return null;
        }
        return defaultValue ((PrimitiveType) type, isMultivalued, lower);
    }

    /**
     * Returns default (single-) value of a given type.
     */
    public static Object defaultValue (Classifier type) {
        if (type instanceof PrimitiveType)
            return defaultValue ((PrimitiveType) type, false, 1);
        return null;
    }

    /**
     * Returns default value.
     *
     * @param type type of the value
     * @multiValued if true, multi-value is returned
     * @card cardinality of multi-value, it is ignored if multiValued is <code>false</code>
     *
     * @return default value
     */
    private static Object defaultValue (PrimitiveType type, boolean multiValued, int card) {
        Object baseValue = null;
        String typeName = type.getName ();
        if (XmiConstants.BOOLEAN_TYPE.equals (typeName))
            baseValue = Boolean.FALSE;
        else if (XmiConstants.DOUBLE_TYPE.equals (typeName))
            baseValue = new Double (0);
        else if (XmiConstants.FLOAT_TYPE.equals (typeName))
            baseValue = new Float (0);
        else if (XmiConstants.INTEGER_TYPE.equals (typeName))
            baseValue = new Integer (0);
        else if (XmiConstants.LONG_TYPE.equals (typeName))
            baseValue = new Long (0);
        else if (XmiConstants.STRING_TYPE.equals (typeName))
            baseValue = "";
        if (baseValue == null)
            return null;
        if (!multiValued)
            return baseValue;
        List multiValue = new LinkedList ();
        for (int x = 0; x < card; x++)
            multiValue.add (baseValue);
        return multiValue;
    }
    
    // ==========================================================================
    // Corba types resolving
    // ==========================================================================
    
    public Classifier resolveCorbaType (XmiElement.DataTypeElement.Node node, boolean nestedCall) {
        node = node.firstSubNode ();
        String id = node.name;
        if (id.equals (XmiConstants.XMICorbaTcShort)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcLong)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcUShort)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcULong)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);        
        if (id.equals (XmiConstants.XMICorbaTcFloat))             
            return resolveCorbaPrimitive (node, XmiConstants.FLOAT_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcDouble)) 
            return resolveCorbaPrimitive (node, XmiConstants.DOUBLE_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcBoolean)) 
            return resolveCorbaPrimitive (node, XmiConstants.BOOLEAN_TYPE, nestedCall);        
        if (id.equals (XmiConstants.XMICorbaTcChar))             
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcWChar)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcOctet)) 
            return resolveCorbaPrimitive (node, XmiConstants.INTEGER_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcString)) 
            return resolveCorbaPrimitive (node, XmiConstants.STRING_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcWString)) 
            return resolveCorbaPrimitive (node, XmiConstants.STRING_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcLongLong)) 
            return resolveCorbaPrimitive (node, XmiConstants.LONG_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcULongLong)) 
            return resolveCorbaPrimitive (node, XmiConstants.LONG_TYPE, nestedCall);
        if (id.equals (XmiConstants.XMICorbaTcLongDouble)) 
            return resolveCorbaPrimitive (node, XmiConstants.DOUBLE_TYPE, nestedCall);
        
        if (id.equals (XmiConstants.XMICorbaTcStruct))
            return resolveCorbaStruct (node);
        if (id.equals (XmiConstants.XMICorbaTcEnum)) 
            return resolveCorbaEnum (node);
        if (id.equals (XmiConstants.XMICorbaTcAlias))
            return resolveCorbaAlias (node);        
        if (id.equals (XmiConstants.XMICorbaTcAny)) 
            return resolveCorbaPrimitive (node, XmiConstants.STRING_TYPE, nestedCall);
        
        throw new DebugException ("Unsupported Corba type: " + id);
    }
        
    private Classifier resolveCorbaStruct (XmiElement.DataTypeElement.Node node) {
        StructureField field;
        Classifier fieldType;
        boolean hasPrimType = false;
        String name = node.tcName;
        StructureFieldClass fieldProxy = modelPackage.getStructureField ();
        List fields = new LinkedList ();
        Iterator iter = node.subnodes.iterator ();
        while (iter.hasNext ()) {
            XmiElement.DataTypeElement.Node fieldNode = 
                (XmiElement.DataTypeElement.Node) iter.next ();
            String fieldName = fieldNode.tcName;
            fieldType = resolveCorbaType (fieldNode.firstSubNode (), true);
            field = fieldProxy.createStructureField (
                fieldName, ANNOTATION
            );
            field.setType (fieldType);
            fields.add (field);
        }
        StructureTypeClass proxyClass = modelPackage.getStructureType();
        StructureType struct = proxyClass.createStructureType (
            name, ANNOTATION, false, false, false, VisibilityKindEnum.PUBLIC_VIS
        );
        iter = fields.iterator ();
        while (iter.hasNext ()) {
            field = (StructureField) iter.next ();
            field.setContainer (struct);
            fieldType = field.getType ();
            if (fieldType instanceof PrimitiveType) {
                hasPrimType = true;
            } else {
                fieldType.setContainer(struct);
            }
        }
        if (hasPrimType) {
            corbaTypesReferencingPrimitiveTypes.add (struct);
        }
        return struct;
    }

    private Classifier resolveCorbaEnum (XmiElement.DataTypeElement.Node node) {
        String name = node.tcName;
        List labels = new LinkedList ();
        Iterator iter = node.subnodes.iterator ();
        while (iter.hasNext ()) {
            XmiElement.DataTypeElement.Node labelNode = 
                (XmiElement.DataTypeElement.Node) iter.next ();
            labels.add (labelNode.tcName);
        }
        EnumerationTypeClass proxyClass = modelPackage.getEnumerationType();
        return proxyClass.createEnumerationType (
            name, ANNOTATION, false, false, false, VisibilityKindEnum.PUBLIC_VIS, labels
        );
    }

    private Classifier resolveCorbaAlias (XmiElement.DataTypeElement.Node node) {        
        String name = node.tcName;
        Classifier type = resolveCorbaType (node.firstSubNode (), true);
        AliasType alias = modelPackage.getAliasType ().createAliasType (
            name, ANNOTATION, 
            false, false, false, VisibilityKindEnum.PUBLIC_VIS
        );
        alias.setType (type);
        if (type instanceof PrimitiveType) {
            corbaTypesReferencingPrimitiveTypes.add (alias);
        } else {
            type.setContainer (alias);
        }
        return alias;
    }

    /**
     * Resolves Corba primitive type as an Alias pointing to an equivalent
     * of the primitive type.
     */
    private Classifier resolveCorbaPrimitive (XmiElement.DataTypeElement.Node node, String name, boolean nestedCall) {
        PrimitiveType primitive = substCorbaPrimitive (node, name);
        if (nestedCall) {
            return primitive;
        }
            AliasType alias = modelPackage.getAliasType ().createAliasType (
                name, ANNOTATION, 
                false, false, false, VisibilityKindEnum.PUBLIC_VIS
            );
            alias.setType (primitive);
            // Store created Alias instance to be able set all needed Imports later when 
            // all elements of XMI document are deserialized.
            corbaTypesReferencingPrimitiveTypes.add (alias);
            return alias;

    }
    
    private PrimitiveType substCorbaPrimitive (XmiElement.DataTypeElement.Node node, String name) {
        if (primitiveTypes == null) {
            // locate PrimitiveTypes package, if it does not exist, create it
            if (modelPackage == null)
                throw new DebugException ("Unable to create PrimitiveType for " + name);
            MofPackageClass proxy = modelPackage.getMofPackage ();
            Iterator iter = proxy.refAllOfType ().iterator ();
            while (iter.hasNext ()) {
                MofPackage pckg = ((MofPackage) iter.next ());
                if (XmiConstants.PRIMITIVE_TYPES_PACKAGE.equals (pckg.getName ())) {
                    primitiveTypesPackage = pckg;
                    break;
                }
            } // while
            primitiveTypes = new HashMap ();
            if (primitiveTypesPackage == null) {
                /* <Model.Package name = 'CorbaIdlTypes' annotation = '' isRoot = 'false' 
                 isLeaf = 'false' isAbstract = 'false' visibility = 'public_vis'> */
                primitiveTypesPackage = proxy.createMofPackage (
                    XmiConstants.PRIMITIVE_TYPES_PACKAGE, ANNOTATION,
                    false, false, false, VisibilityKindEnum.PUBLIC_VIS
                );
                Tag tag = modelPackage.getTag ().createTag ();
                tag.setTagId ("javax.jmi.packagePrefix");
                tag.getValues ().add ("javax.jmi");
                tag.getElements ().add (primitiveTypesPackage);                
                PrimitiveType type;
                PrimitiveTypeClass typeProxy = modelPackage.getPrimitiveType ();
                for (int x = 0; x < PRIMITIVE_TYPES.length; x++) {
                    type = typeProxy.createPrimitiveType (
                        PRIMITIVE_TYPES [x], ANNOTATION, false, false, false, 
                        VisibilityKindEnum.PUBLIC_VIS
                    );
                    type.setContainer (primitiveTypesPackage);
                    primitiveTypes.put (PRIMITIVE_TYPES [x], type);
                } // for
            } else {
                for (Iterator contents = primitiveTypesPackage.getContents().iterator(); 
                    contents.hasNext();) {
                    ModelElement type = (ModelElement) contents.next();
                    if (type instanceof PrimitiveType)
                        primitiveTypes.put (type.getName (), type);
                } // for
            } // else
        } // if    
        
        PrimitiveType type = (PrimitiveType) primitiveTypes.get (name);
        if (type == null) {
            type = modelPackage.getPrimitiveType ().createPrimitiveType (
                name, ANNOTATION, false, false, false, VisibilityKindEnum.PUBLIC_VIS
            );
            type.setContainer (primitiveTypesPackage);
            primitiveTypes.put (name, type);
        }
        return type;
    }
    
    public void countInstance () {
        instancesCounter++;
    }

    // XMIReferenceResolver.Client implementation ...............................
    
    public class Client implements XMIReferenceResolver.Client {
    
        private HashMap refs;
        
        public Client (HashMap refs) {
            this.refs = refs;
        }
        
        public void resolvedReference (String href, RefObject obj) {
            resolvedReference (href, obj, false);
        }
        
        public void resolvedReference (String href, RefObject obj, boolean internalCall) {
            if (!internalCall) {
                XMIReferenceProvider.XMIReference xmiRef = toXMIReference (href);            
                putReference (xmiRef.getSystemId (), xmiRef.getXmiId (), obj);
            }
            allUnresolvedHRefs.remove (href);
            
            List list = (List) refs.remove (href);
            if (list != null) {                
                Iterator iter = list.iterator ();
                while (iter.hasNext ()) {
                    ((XmiElement.UnresolvedReference) iter.next ()).referenceResolved (obj);
                } // while
            } // if
        }
    
    } // Client
    
}
