package org.andromda.core.translation.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.andromda.core.translation.analysis.AnalysisAdapter;
import org.andromda.core.translation.analysis.DepthFirstAdapter;
import org.andromda.core.translation.lexer.Lexer;
import org.andromda.core.translation.node.AActualParameterList;
import org.andromda.core.translation.node.ABarFeatureCallParameterOption;
import org.andromda.core.translation.node.AColonFeatureCallParameterOption;
import org.andromda.core.translation.node.ACommaExpression;
import org.andromda.core.translation.node.ACommaFeatureCallParameterOption;
import org.andromda.core.translation.node.AConcreteFeatureCallParameters;
import org.andromda.core.translation.node.AEqualExpression;
import org.andromda.core.translation.node.AFeatureCallParameters;
import org.andromda.core.translation.node.AIterateDeclarator;
import org.andromda.core.translation.node.AIterateFeatureCallParameterOption;
import org.andromda.core.translation.node.APathName;
import org.andromda.core.translation.node.AStandardDeclarator;
import org.andromda.core.translation.node.ATypeDeclaration;
import org.andromda.core.translation.node.AVariableDeclaration;
import org.andromda.core.translation.node.AVariableDeclarationList;
import org.andromda.core.translation.node.AVariableDeclarationListTail;
import org.andromda.core.translation.node.Node;
import org.andromda.core.translation.node.PExpression;
import org.andromda.core.translation.node.PFeatureCallParameterOption;
import org.andromda.core.translation.node.TName;

/** 
 *  This class adapts the Parser class to handle expressions
 *  in which the SableCC parser can't handle.
 */
public class OclParser extends Parser {

	protected AstFix fix = new AstFix();
	protected Node oclNode;

	/**
	 * Constructs an instance of OclParser.
	 * 
	 * @param lexer
	 */
	public OclParser(Lexer lexer) {
		super(lexer);
	}

    /**
     * @see org.andromda.core.translation.parser.parser.Parser#filter()
     */
	protected void filter() {
		oclNode = node;
		oclNode.apply(fix);
		node = oclNode;
	}

	private class AstFix extends AnalysisAdapter {

        /**
         * @see org.andromda.core.translation.parser.analysis.Analysis#caseAConcreteFeatureCallParameters(org.andromda.core.translation.parser.node.AConcreteFeatureCallParameters)
         */
		public void caseAConcreteFeatureCallParameters(AConcreteFeatureCallParameters featureCallParameters) {
			boolean isDeclarator = false;
			boolean isIterateDeclarator = false;

			List tail = featureCallParameters.getFeatureCallParameterOption();
			PFeatureCallParameterOption[] parameterOption = 
				new PFeatureCallParameterOption[tail.size()];
			Iterator iter = tail.iterator();
			
			for (int ctr = 0; iter.hasNext(); ctr++) {
				PFeatureCallParameterOption option = (PFeatureCallParameterOption) iter.next();
				parameterOption[ctr] = option;
				isIterateDeclarator = option instanceof AIterateFeatureCallParameterOption;
				if (!isIterateDeclarator) {
					isDeclarator = option instanceof ABarFeatureCallParameterOption;
				}
			} 

			if (isIterateDeclarator && !isDeclarator) {
				throw new OclParserException(
					"parser error: illegal feature call parameters format in \""
						+ featureCallParameters
						+ "\"; "
						+ "must contain \";\" only if it contains \"|\"");
			}
			AFeatureCallParameters parameters;
			if (isIterateDeclarator) {
				parameters =
					getParametersWithIterateDeclarator(
						featureCallParameters,
						featureCallParameters.getExpression(),
						parameterOption);
			} else if (isDeclarator) {
				parameters =
					getParametersWithStandardDeclarator(
						featureCallParameters,
						parameterOption);
			} else {
				parameters =
					getParametersWithoutDeclarator(
						featureCallParameters,
						featureCallParameters.getExpression(),
						parameterOption);
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
			AConcreteFeatureCallParameters featureCallParameters,
			PExpression expression,
			PFeatureCallParameterOption[] parameterOption) {
			AIterateDeclarator iteratorDeclarator = new AIterateDeclarator();

			AColonFeatureCallParameterOption featureCallParameterOption0 = 
				(AColonFeatureCallParameterOption) parameterOption[0];
			AIterateFeatureCallParameterOption featureCallParameterOption1 = 
				(AIterateFeatureCallParameterOption) parameterOption[1];
			ABarFeatureCallParameterOption featureCallParameterOption2 = 
				(ABarFeatureCallParameterOption) parameterOption[2];
            
            AVariableDeclaration iterator = 
                new AVariableDeclaration(
                    getName(expression), 
                    featureCallParameterOption0.getTypeDeclaration());      
			iteratorDeclarator.setIterator(iterator);
			iteratorDeclarator.setSemicolon(featureCallParameterOption1.getSemicolon());
            
            AVariableDeclaration accumulator = 
            new AVariableDeclaration(
                    featureCallParameterOption1.getName(), 
                    featureCallParameterOption1.getTypeDeclaration());   
            iteratorDeclarator.setAccumulator(accumulator);
            
            AEqualExpression equalExpression = 
                new AEqualExpression(
                    featureCallParameterOption1.getEqual(), 
                    featureCallParameterOption1.getExpression());
			iteratorDeclarator.setEqualExpression(equalExpression);
			iteratorDeclarator.setBar(featureCallParameterOption2.getBar());
				AActualParameterList params =
					new AActualParameterList(
						featureCallParameterOption2.getExpression(),
						new ArrayList());
			return
				new AFeatureCallParameters(
					featureCallParameters.getLParen(),
					iteratorDeclarator,
					params,
					featureCallParameters.getRParen());
		}
		
        /**
         * Gets AFeatureCallParameters from the standard declarator.
         * 
         * @param featureCallParameters
         * @param parameterOption
         * @return AFeatureCallParameters
         */
		protected AFeatureCallParameters getParametersWithStandardDeclarator(
			AConcreteFeatureCallParameters featureCallParameters,
			PFeatureCallParameterOption[] parameterOptions) {
			
			int parameterOptionNum = parameterOptions.length;

			boolean valid = true;
			
			// if there is less than one parameter in the parameterOption array
			if (parameterOptionNum < 1) {
				valid = false;	
			//check and make sure the last parameterOption is an instance of ABarFeatureCallParameterOption
			//and set to false if not.
			} else if (!(parameterOptions[parameterOptionNum - 1] instanceof ABarFeatureCallParameterOption)) {
				valid = false;
			}
			
			//check if the parameterOptions (after the first two) are instances of either 
			//ACommaFeatureCallParameter (so we can retrieve something like ', name') or
			//AColonFeatureCallParameterOption (so we can retrieve something like ': type') 
			//and valid to false if this isn't the case.
			for (int ctr = 0; ctr < parameterOptionNum - 2; ctr++) {
				if (!(parameterOptions[ctr] instanceof ACommaFeatureCallParameterOption ||
				      parameterOptions[ctr] instanceof AColonFeatureCallParameterOption)) {
					throw new OclParserException(
							"parser error: feature call parameters with standard declarator must have the format "
							+ "\"( name (: type)?, ... , name: type | expression )\"");
				}
			}

			if (!valid) {

			}
			
			ABarFeatureCallParameterOption barParameterType =
				(ABarFeatureCallParameterOption) parameterOptions[parameterOptionNum - 1];
			
			AStandardDeclarator standardDeclarator =
				new AStandardDeclarator(
					this.getVariableDeclarationList(featureCallParameters),
					barParameterType.getBar());
				AActualParameterList params =
					new AActualParameterList(
						barParameterType.getExpression(),
						new ArrayList());
			return
				new AFeatureCallParameters(
					featureCallParameters.getLParen(),
					standardDeclarator,
					params,
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
			AConcreteFeatureCallParameters featureCallParameters,
			PExpression expr,
			PFeatureCallParameterOption[] parameterOption) {
			
			List paramList = new ArrayList();
			
			for (int ctr = 0; ctr < parameterOption.length; ctr++) {
				if (!(parameterOption[ctr] instanceof ACommaFeatureCallParameterOption)) {
					throw new OclParserException(
						"parser error: declarator-less feature call paramaters must have the format "
							+ "\"( expr, ..., expr )\"");
				} else {
					ACommaFeatureCallParameterOption commaOption = 
						(ACommaFeatureCallParameterOption) parameterOption[ctr];
					ACommaExpression commaExpression =
						new ACommaExpression(
							commaOption.getComma(),
							commaOption.getExpression());
					paramList.add(commaExpression);
				}
			}

			return 
				new AFeatureCallParameters(
					featureCallParameters.getLParen(),
					null,
					new AActualParameterList(expr, paramList),
					featureCallParameters.getRParen());
		}

		/**
		 * Gets the AVariableDeclarationList instance from the <code>params</code>
		 * by apply the VariableDeclarationListFinder to it.
		 * @param params the params node to parse.
		 * @return the found AVariableDeclarationList instance.
		 */
		protected AVariableDeclarationList getVariableDeclarationList(AConcreteFeatureCallParameters params) {
			VariableDeclarationListFinder finder = new VariableDeclarationListFinder();
			params.apply(finder);
			return finder.getList();
		}

	}

    /** 
     *  A tree traversal class that searchs for a name in a expression.
     */
    private class VariableDeclarationListFinder extends DepthFirstAdapter {

    	/**
    	 * Stores the variable names in an ordered
    	 * fashion so that we can retrieve them from the 
    	 * namesAndTypes map in the order they were stored.
    	 */
        private LinkedList orderedNames = new LinkedList();

        /**
         * Stores the variable names along with its
         * variable type (if there is one).
         */
        private Map namesAndTypes = new HashMap();

        /**
         * @see org.andromda.core.translation.parser.analysis.DepthFirstAdapter#inAPathName(org.andromda.core.translation.parser.node.APathName)
         */
        public void inAPathName(APathName name) {
        	//we only want to add the first name (since the other
        	//names will all be comma seperated and stored within 
        	//the inACommaFeatureCallParameterOption() method)
        	if (this.namesAndTypes.isEmpty()) {
        		TName initialVariableName = name.getName();
        		this.orderedNames.add(initialVariableName);
        		this.namesAndTypes.put(this.orderedNames.getLast(), null);
        	}
        }

        /**
         * @see org.andromda.core.translation.parser.analysis.DepthFirstAdapter#inACommaFeatureCallParameterOption(org.andromda.core.translation.parser.node.ACommaFeatureCallParameterOption)
         */
        public void inACommaFeatureCallParameterOption(ACommaFeatureCallParameterOption commaName) {
        	this.orderedNames.add(commaName);
        	this.namesAndTypes.put(commaName, null);
        }
        
        /**
         * @see org.andromda.core.translation.parser.analysis.DepthFirstAdapter#inATypeDeclaration(org.andromda.core.translation.parser.node.ATypeDeclaration)
         */
	    public void inATypeDeclaration(ATypeDeclaration type) {
	    	if (this.namesAndTypes.containsKey(this.orderedNames.getLast())) {
	    		this.namesAndTypes.put(this.orderedNames.getLast(), type);
	    	}
	    }

        /**
         * Extracts and constructs AVariableDeclarationlist 
         * from a AConcreteFeatureCallParameters instance.
         * 
         * @return AVariableDeclarationList
         */
        public AVariableDeclarationList getList() {

        	TName initialName = (TName)this.orderedNames.getFirst();
        	
        	ATypeDeclaration typeDeclaration = 
        		(ATypeDeclaration)namesAndTypes.get(initialName);
        	
        	List variableDeclarationListTails = new ArrayList();
        	if (!this.orderedNames.isEmpty()) {
        		int orderedNameSize = orderedNames.size();
        		for (int ctr = 1; ctr < orderedNameSize; ctr++) {
        			ACommaFeatureCallParameterOption name =
        				(ACommaFeatureCallParameterOption)this.orderedNames.get(ctr);
        			
        			ATypeDeclaration typeDecl = 
        				(ATypeDeclaration)this.namesAndTypes.get(name);
        			
        			AVariableDeclaration variableDeclaration =
        				new AVariableDeclaration(
        					getName(name.getExpression()),
							typeDecl);

        			variableDeclarationListTails.add(
        				new AVariableDeclarationListTail(
        					name.getComma(), 
							variableDeclaration, 
							null));
        		}
        	}
        	
        	AVariableDeclarationList list = 				
        		new AVariableDeclarationList(
        			new AVariableDeclaration(initialName, typeDeclaration),
					null,
					variableDeclarationListTails);
        	return list;
        }
        
    }
    
    /** 
     *  A tree traversal class that searchs for a name in a expression.
     */
    private class NameFinder extends DepthFirstAdapter {

        private TName foundName;
        
        /**
         * @see org.andromda.core.translation.parser.analysis.Analysis#caseAPathName(org.andromda.core.translation.parser.node.APathName)
         */
        public void caseAPathName(APathName pathName) {
            this.foundName = pathName.getName();
        }

        /**
         * @return String the TName
         */
        public TName getName() {
            return this.foundName;
        }
    }
    
    /**
     * Gets the TName from the <code>expression</code>.
     * @param expression
     * @return TName the name extracted from the <code>expression</code>.
     */
    protected TName getName(PExpression expression) {
    	NameFinder finder = new NameFinder();
    	expression.apply(finder);
    	return finder.getName();
    }
}
