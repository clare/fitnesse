package fitnesse.responders.testHistory;

import java.util.LinkedList;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompletenessTreeTest {

  
  @SuppressWarnings("deprecation")
  @Test  
  public void testTestsInSameDirectoryAppearOnSameLevel() {
    LinkedList<String> pageNames = new LinkedList<String>();
    pageNames.add("FitNesse.MyTests.Test1");
    pageNames.add("FitNesse.MyTests.Test2");
    pageNames.add("FitNesse.MyTests.Test3");
    
    CompletenessTree tree = new CompletenessTree(pageNames, null);
    
  }

  @SuppressWarnings("deprecation")
  @Test  
  public void testTestsInDiffDirectoryAreSplit() {
    LinkedList<String> pageNames = new LinkedList<String>();
    pageNames.add("FitNesse.MyTestsA.Test1");
    pageNames.add("FitNesse.MyTestsA.Test2");
    pageNames.add("FitNesse.MyTestsB.Test3");
    
    CompletenessTree tree = new CompletenessTree(pageNames, null);
    
  }
}
