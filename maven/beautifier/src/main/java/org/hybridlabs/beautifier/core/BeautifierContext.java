package org.hybridlabs.beautifier.core;

/**
 * Copyright 2008 hybrid labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.hunsicker.jalopy.language.antlr.JavaNode;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Context object used during a beatification process.
 *
 * @author Karsten Klein, hybrid labs; Plushnikov Michail
 */
public class BeautifierContext implements ImportBeautifierJalopyConstants
{

    private TypeContext currentTypeContext;

    private Map<JavaNode, Object> processedNodes = new IdentityHashMap<JavaNode, Object>();

    private int packageLine = -1;
    private int packageEnd = -1;

    private int importEndLine = -1;
    private int importEnd = -1;

    private Set<TypeContext> typeContexts = new TreeSet<TypeContext>();

    public BeautifierContext()
    {
        initializeTypeContext(-1);
    }

    public void preProcess(JavaNode javaNode)
    {
        processedNodes.put(javaNode, null);
    }

    public boolean isProcessed(JavaNode javaNode)
    {
        return processedNodes.containsKey(javaNode);
    }

    public void addToCurrentTypeContext(JavaNode javaNode)
    {
        currentTypeContext.addComponent(javaNode.getText());
    }

    public TypeContext getCurrentTypeContext()
    {
        return currentTypeContext;
    }

    public void postProcess(JavaNode javaNode)
    {
    }

    private void initializeTypeContext(int type)
    {
        currentTypeContext = new TypeContext(type, new ArrayList<String>());
    }

    public void terminateCurrentSequence(JavaNode javaNode, int typeOfSequenceScope)
    {
        if (currentTypeContext != null)
        {
            currentTypeContext.reviseType();

            if (currentTypeContext.getType() == PACKAGE_ANNOTATION)
            {
                if (packageEnd == -1)
                {
                    packageEnd = javaNode.getEndColumn();
                    packageLine = javaNode.getEndLine();
                }
            }

            if (currentTypeContext.getType() == IMPORT_STATEMENT ||
                    currentTypeContext.getType() == STATIC_IMPORT_STATEMENT)
            {
                if (javaNode.getEndLine() > importEndLine)
                {
                    importEndLine = javaNode.getEndLine();
                    importEnd = javaNode.getEndColumn();
                }
            }

            if (currentTypeContext.getType() == 19)
            {
                if (currentTypeContext.getLength() == 1)
                {
                    typeContexts.add(currentTypeContext);
                }
            }

            if (currentTypeContext.getType() == 55)
            {
                if (currentTypeContext.getLength() == 1)
                {
                    typeContexts.add(currentTypeContext);
                }
            }

            if (currentTypeContext.getLength() > 1)
            {
                typeContexts.add(currentTypeContext);
            }
        }

        if (typeOfSequenceScope == -1)
        {
            initializeTypeContext(javaNode.getType());
        }
        else
        {
            initializeTypeContext(typeOfSequenceScope);
        }

    }

    public Set<TypeContext> getSequences()
    {
        return typeContexts;
    }

    public int getPackageEnd()
    {
        return packageEnd;
    }

    public int getPackageLine()
    {
        return packageLine;
    }

    public int getImportEndLine()
    {
        return importEndLine;
    }

    public int getImportEndColumn()
    {
        return importEnd;
    }

}
