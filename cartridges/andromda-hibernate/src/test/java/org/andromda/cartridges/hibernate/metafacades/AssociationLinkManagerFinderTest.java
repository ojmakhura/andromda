package org.andromda.cartridges.hibernate.metafacades;

import junit.framework.TestCase;

/**
 * JUnit test class for the association link manager finder.
 * 
 * @since 25.10.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class AssociationLinkManagerFinderTest extends TestCase {

    public class TestData {
        private String typeName1;
        private String oneOrMany1;
        private boolean navigable1;
        private boolean aggregation1;
        private boolean composition1;
        private String typeName2;
        private String oneOrMany2;
        private boolean navigable2;
        private boolean aggregation2;
        private boolean composition2;
        private boolean expectedManager1;
        private boolean expectedManager2;

        public TestData(String oneOrMany1,
                boolean navigable1, boolean aggregation1,
                boolean expectedManager1,
                String oneOrMany2,
                boolean navigable2, boolean aggregation2,
                boolean expectedManager2
                ) {
            this.typeName1 = "A";
            this.oneOrMany1 = oneOrMany1;
            this.navigable1 = navigable1;
            this.aggregation1 = aggregation1;
            this.composition1 = false;
            this.typeName2 = "B";
            this.oneOrMany2 = oneOrMany2;
            this.navigable2 = navigable2;
            this.aggregation2 = aggregation2;
            this.composition2 = false;
            this.expectedManager1 = expectedManager1;
            this.expectedManager2 = expectedManager2;
        }
        
        public void runTest(int testIndex) throws Exception {
            MockClassifierFacade cf1 = new MockClassifierFacade(typeName1);
            MockAssociationEndFacade af1 =  new MockAssociationEndFacade (cf1, oneOrMany1, navigable1, aggregation1, composition1);
            MockClassifierFacade cf2 = new MockClassifierFacade(typeName2);
            MockAssociationEndFacade af2 =  new MockAssociationEndFacade (cf2, oneOrMany2, navigable2, aggregation2, composition2);
            af1.setOtherEnd (af2);
            af2.setOtherEnd (af1);
            boolean isManager1 = AssociationLinkManagerFinder.managesRelationalLink(af1);
            assertEquals ("test index " + testIndex + " expm1: ", expectedManager1, isManager1);
            boolean isManager2 = AssociationLinkManagerFinder.managesRelationalLink(af2);
            assertEquals ("test index " + testIndex + " expm2: ", expectedManager2, isManager2);
            assertEquals("test index " + testIndex + " uneql: ", !isManager1, isManager2);
        }
    }
    

    private static final String one  = "one";
    private static final String many = "many";
    
    private TestData testData[] = new TestData[] {
            /* one:one         nav    agg    expm1        nav    agg    expm2  */
            new TestData(one , true , false, true , one , true , false, false),
            new TestData(one , false, false, true , one , true , false, false),
            new TestData(one , true , false, false, one , false, false, true ),

            new TestData(one , true , true , false, one , true , false, true ),
            new TestData(one , false, true , true , one , true , false, false),

            new TestData(one , true , false, true , one , true , true , false),
            new TestData(one , true , false, false, one , false, true , true ),

            /* one:many        nav    agg    expm1        nav    agg    expm2  */
            new TestData(one , true , false, false, many, true , false, true ),
            new TestData(one , false, false, true , many, true , false, false),
            new TestData(one , true , false, false, many, false, false, true ),

            new TestData(one , true , true , false, many, true , false, true ),
            new TestData(one , false, true , true , many, true , false, false),

            /* many:many       nav    agg    expm1        nav    agg    expm2  */
            new TestData(many, true , false, true , many, true , false, false),
            new TestData(many, false, false, true , many, true , false, false),
            new TestData(many, true , false, false, many, false, false, true ),
    };
    
    public void test() throws Exception {
        for (int i = 0;  i < testData.length; i++) {
            // System.out.println("test case " + i);
            testData[i].runTest(i);
        }
    }
}
