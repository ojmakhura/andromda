package org.andromda.translation.ocl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.andromda.translation.ocl.analysis.AnalysisAdapter;
import org.andromda.translation.ocl.analysis.DepthFirstAdapter;
import org.andromda.translation.ocl.lexer.Lexer;
import org.andromda.translation.ocl.node.AActualParameterList;
import org.andromda.translation.ocl.node.ABarFeatureCallParameterOption;
import org.andromda.translation.ocl.node.AColonFeatureCallParameterOption;
import org.andromda.translation.ocl.node.ACommaExpression;
import org.andromda.translation.ocl.node.ACommaFeatureCallParameterOption;
import org.andromda.translation.ocl.node.AConcreteFeatureCallParameters;
import org.andromda.translation.ocl.node.AEqualExpression;
import org.andromda.translation.ocl.node.AFeatureCallParameters;
import org.andromda.translation.ocl.node.AIterateDeclarator;
import org.andromda.translation.ocl.node.AIterateFeatureCallParameterOption;
import org.andromda.translation.ocl.node.APathName;
import org.andromda.translation.ocl.node.AStandardDeclarator;
import org.andromda.translation.ocl.node.ATypeDeclaration;
import org.andromda.translation.ocl.node.AVariableDeclaration;
import org.andromda.translation.ocl.node.AVariableDeclarationList;
import org.andromda.translation.ocl.node.AVariableDeclarationListTail;
import org.andromda.translation.ocl.node.Node;
import org.andromda.translation.ocl.node.PExpression;
import org.andromda.translation.ocl.node.PFeatureCallParameterOption;
import org.andromda.translation.ocl.node.TName;

/**
 * This class adapts the Parser class to handle expressions in which the SableCC parser can't handle.
 */
public class OclParser
        extends Parser
{
    /**
     *
     */
    protected Node oclNode;

    /**
     * Constructs an instance of OclParser.
     *
     * @param lexer
     */
    public OclParser(Lexer lexer)
    {
        super(lexer);
    }

    /**
     * @see org.andromda.translation.ocl.parser.Parser#filter()
     */
    protected void filter()
    {
        oclNode = node;
        oclNode.apply(handler);
        node = oclNode;
    }

    /**
     *
     */
    protected SyntaxHandler handler = new SyntaxHandler();

    /**
     * A private inner class for handling syntax which SableCC can't handle on its own.
     */
    private class SyntaxHandler
            extends AnalysisAdapter
    {

        /**
         * @param featureCallParameters
         * @see #org.andromda.translation.ocl.parser.OclParser.SyntaxHandler.getParametersWithStandardDeclarator(AConcreteFeatureCallParameters featureCallParameters, PFeatureCallParameterOption[] parameterOptions)
         */
        public void caseAConcreteFeatureCallParameters(AConcreteFeatureCallParameters featureCallParameters)
        {
            boolean isDeclarator = false;
            boolean isIterateDeclarator = false;

            List tail = featureCallParameters.getFeatureCallParameterOption();
            PFeatureCallParameterOption[] parameterOption = new PFeatureCallParameterOption[tail.size()];
            Iterator iter = tail.iterator();

            for (int ctr = 0; iter.hasNext(); ctr++)
            {
                PFeatureCallParameterOption option = (PFeatureCallParameterOption) iter.next();
                parameterOption[ctr] = option;
                isIterateDeclarator = option instanceof AIterateFeatureCallParameterOption;
                if (!isIterateDeclarator)
                {
                    isDeclarator = option instanceof ABarFeatureCallParameterOption;
                }
            }

            if (isIterateDeclarator && !isDeclarator)
            {
                throw new OclParserException("Parser Error: Illegal feature call parameters format in \"" +
                        featureCallParameters + "\"; " + "must contain \";\" only if it contains \"|\"");
            }
            AFeatureCallParameters parameters;
            if (isIterateDeclarator)
            {
                parameters = getParametersWithIterateDeclarator(featureCallParameters,
                        featureCallParameters.getExpression(), parameterOption);
            }
            else if (isDeclarator)
            {
                parameters = getParametersWithStandardDeclarator(featureCallParameters, parameterOption);
            }
            else
            {
                parameters = getParametersWithoutDeclarator(featureCallParameters,
                        featureCallParameters.getExpression(), parameterOption);
            }
            oclNode = parameters;
        }

        /**
         * Gets the AFeatureCallParameters with a iterate declarator.
         *
         * @param featureCallParameters
         * @param expression
         * @param parameterOption
         * @return AFeatureCallParameters
         */
        protected AFeatureCallParameters getParametersWithIterateDeclarator(
                AConcreteFeatureCallParameters featureCallParameters, PExpression expression,
                PFeatureCallParameterOption[] parameterOption)
        {
            AIterateDeclarator iteratorDeclarator = new AIterateDeclarator();

            AColonFeatureCallParameterOption featureCallParameterOption0 = (AColonFeatureCallParameterOption) parameterOption[0];
            AIterateFeatureCallParameterOption featureCallParameterOption1 = (AIterateFeatureCallParameterOption) parameterOption[1];
            ABarFeatureCallParameterOption featureCallParameterOption2 = (ABarFeatureCallParameterOption) parameterOption[2];

            AVariableDeclaration iterator = new AVariableDeclaration(getName(expression),
                    featureCallParameterOption0.getTypeDeclaration());
            iteratorDeclarator.setIterator(iterator);
            iteratorDeclarator.setSemicolon(featureCallParameterOption1.getSemicolon());

            AVariableDeclaration accumulator = new AVariableDeclaration(featureCallParameterOption1.getName(),
                    featureCallParameterOption1.getTypeDeclaration());
            iteratorDeclarator.setAccumulator(accumulator);

            AEqualExpression equalExpression = new AEqualExpression(featureCallParameterOption1.getEqual(),
                    featureCallParameterOption1.getExpression());
            iteratorDeclarator.setEqualExpression(equalExpression);
            iteratorDeclarator.setBar(featureCallParameterOption2.getBar());
            AActualParameterList params = new AActualParameterList(featureCallParameterOption2.getExpression(),
                    new ArrayList());
            return new AFeatureCallParameters(featureCallParameters.getLParen(), iteratorDeclarator, params,
                    featureCallParameters.getRParen());
        }

        /**
         * Gets AFeatureCallParameters from the standard declarator.
         *
         * @param featureCallParameters
         * @param parameterOptions
         * @return AFeatureCallParameters
         */
        protected AFeatureCallParameters getParametersWithStandardDeclarator(
                AConcreteFeatureCallParameters featureCallParameters, PFeatureCallParameterOption[] parameterOptions)
        {

            int parameterOptionNum = parameterOptions.length;

            // check if the parameterOptions (after the first two)
            // are instances of either ACommaFeatureCallParameter
            // (so we can retrieve something like ', name') or
            // ColonFeatureCallParameterOption (so we can retrieve
            // something like ': type') and valid to false if this
            // isn't the case.
            for (int ctr = 0; ctr < parameterOptionNum - 2; ctr++)
            {
                if (!(parameterOptions[ctr] instanceof ACommaFeatureCallParameterOption ||
                        parameterOptions[ctr] instanceof AColonFeatureCallParameterOption))
                {
                    throw new OclParserException("OCL Parser Error: Feature call parameters with " +
                            "a standard declarator must have the format " +
                            "\"( name (: type)?, ... , name (: type)? | expression )\"");
                }
            }

            ABarFeatureCallParameterOption barParameterType = (ABarFeatureCallParameterOption) parameterOptions[parameterOptionNum -
                    1];

            AStandardDeclarator standardDeclarator = new AStandardDeclarator(
                    this.getVariableDeclarationList(featureCallParameters), barParameterType.getBar());
            AActualParameterList params = new AActualParameterList(barParameterType.getExpression(), new ArrayList());
            return new AFeatureCallParameters(featureCallParameters.getLParen(), standardDeclarator, params,
                    featureCallParameters.getRParen());
        }

        /**
         * Gets the AFeatureCallParameter instance without the declarator.
         *
         * @param featureCallParameters
         * @param expr
         * @param parameterOption
         * @return AFeatureCallParameters
         */
        protected AFeatureCallParameters getParametersWithoutDeclarator(
                AConcreteFeatureCallParameters featureCallParameters, PExpression expr,
                PFeatureCallParameterOption[] parameterOption)
        {

            List paramList = new ArrayList();

            for (int ctr = 0; ctr < parameterOption.length; ctr++)
            {
                if (!(parameterOption[ctr] instanceof ACommaFeatureCallParameterOption))
                {
                    throw new OclParserException("parser error: declarator-less feature call paramaters must have the format " +
                            "\"( expr, ..., expr )\"");
                }
                ACommaFeatureCallParameterOption commaOption = (ACommaFeatureCallParameterOption) parameterOption[ctr];
                ACommaExpression commaExpression = new ACommaExpression(commaOption.getComma(),
                        commaOption.getExpression());
                paramList.add(commaExpression);
            }

            return new AFeatureCallParameters(featureCallParameters.getLParen(), null,
                    new AActualParameterList(expr, paramList), featureCallParameters.getRParen());
        }

        /**
         * Gets the AVariableDeclarationList instance from the <code>params</code> by apply the
         * VariableDeclarationListFinder to it.
         *
         * @param params the params node to parse.
         * @return the found AVariableDeclarationList instance.
         */
        protected AVariableDeclarationList getVariableDeclarationList(AConcreteFeatureCallParameters params)
        {
            VariableDeclarationListFinder finder = new VariableDeclarationListFinder();
            params.apply(finder);
            return finder.getList();
        }
    }

    /**
     * A tree traversal class that searches for a name in a expression.
     */
    private class VariableDeclarationListFinder
            extends DepthFirstAdapter
    {
        /**
         * Stores the variable names in an ordered fashion so that we can retrieve them from the namesAndTypes map in
         * the order they were stored.
         */
        private List orderedNames = new LinkedList();

        /**
         * Stores the variable names along with its variable type (if there is one).
         */
        private Map namesAndTypes = new HashMap();

        /**
         * @param name
         * @see org.andromda.translation.ocl.parser.OclParser.VariableDeclarationListFinder#orderedNames
         */
        public void inAPathName(APathName name)
        {
            // we only want to add the first name (since the other
            // names will all be comma separated and stored within
            // the inACommaFeatureCallParameterOption() method)
            if (this.namesAndTypes.isEmpty())
            {
                TName initialVariableName = name.getName();
                this.orderedNames.add(initialVariableName);
                this.namesAndTypes.put(((LinkedList)this.orderedNames).getLast(), null);
            }
        }

        /**
         * @param commaName
         * @see org.andromda.translation.ocl.parser.OclParser.VariableDeclarationListFinder#orderedNames
         */
        public void inACommaFeatureCallParameterOption(ACommaFeatureCallParameterOption commaName)
        {
            this.orderedNames.add(commaName);
            this.namesAndTypes.put(commaName, null);
        }

        /**
         * @param type
         * @see org.andromda.translation.ocl.parser.OclParser.VariableDeclarationListFinder#namesAndTypes
         */
        public void inATypeDeclaration(ATypeDeclaration type)
        {
            if (this.namesAndTypes.containsKey(((LinkedList)this.orderedNames).getLast()))
            {
                this.namesAndTypes.put(((LinkedList)this.orderedNames).getLast(), type);
            }
        }

        /**
         * Extracts and constructs AVariableDeclarationlist from a AConcreteFeatureCallParameters instance.
         *
         * @return AVariableDeclarationList
         */
        public AVariableDeclarationList getList()
        {

            TName initialName = (TName) ((LinkedList)this.orderedNames).getFirst();

            ATypeDeclaration typeDeclaration = (ATypeDeclaration) namesAndTypes.get(initialName);

            List variableDeclarationListTails = new ArrayList();
            if (!this.orderedNames.isEmpty())
            {
                int orderedNameSize = orderedNames.size();
                for (int ctr = 1; ctr < orderedNameSize; ctr++)
                {
                    ACommaFeatureCallParameterOption name = (ACommaFeatureCallParameterOption) this.orderedNames.get(
                            ctr);

                    ATypeDeclaration typeDecl = (ATypeDeclaration) this.namesAndTypes.get(name);

                    AVariableDeclaration variableDeclaration = new AVariableDeclaration(getName(name.getExpression()),
                            typeDecl);

                    variableDeclarationListTails.add(new AVariableDeclarationListTail(name.getComma(),
                            variableDeclaration, null));
                }
            }

            AVariableDeclarationList list = new AVariableDeclarationList(
                    new AVariableDeclaration(initialName, typeDeclaration), null, variableDeclarationListTails);
            return list;
        }
    }

    /**
     * A tree traversal class that searches for a name in a expression.
     */
    private class NameFinder
            extends DepthFirstAdapter
    {
        private TName foundName;

        /**
         * @param pathName
         * @see org.andromda.translation.ocl.node.APathName#getName()
         */
        public void caseAPathName(APathName pathName)
        {
            this.foundName = pathName.getName();
        }

        /**
         * @return String the TName
         */
        public TName getName()
        {
            return this.foundName;
        }
    }

    /**
     * Gets the TName from the <code>expression</code>.
     *
     * @param expression
     * @return TName the name extracted from the <code>expression</code>.
     */
    protected TName getName(PExpression expression)
    {
        NameFinder finder = new NameFinder();
        expression.apply(finder);
        return finder.getName();
    }
}