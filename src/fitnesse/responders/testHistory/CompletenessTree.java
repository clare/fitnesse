package fitnesse.responders.testHistory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import fitnesse.wiki.WikiPage;

public class CompletenessTree {

  final public TreeItem treeRoot = new TreeItem("root", "", 0);
  
  
  CompletenessTree(List<String> aPagelist, String anotherone) {
    makeTree(aPagelist);
  }
  
  private void makeTree(List<String> pageList) {
    for (String pageName : pageList) 
    {
      String[] splits = pageName.split("\\.");
      treeRoot.addItem(splits, 0);
    }
  }
  
  public CompletenessTree(List<WikiPage> wikiPagelist) {
    this(convertToPageList(wikiPagelist), null);
  }

  private static List<String> convertToPageList(List<WikiPage> wikiPagelist) {
    List<String> allPages = new LinkedList<String>();
    
    for (WikiPage aPage : wikiPagelist)  {
      try {
        allPages.add(aPage.getPageCrawler().getFullPath(aPage).toString());
      } catch (Exception e) {
        allPages.add("There was also a probem getting the path of one page.");
      }
    }
    return allPages;
  }
  
  public void countResults() {
    RecursiveTreeMethod countResults = new RecursiveTreeMethod() {
      @Override
      public boolean shouldDoItemBeforeBranches() {
        return false;
      }
      
      @Override
      public void doMethod(int level, TreeItem item) {
        item.calculateResults();
      }
    };
    treeRoot.doRecursive(countResults, 0);
  }

  public void findLatestResults(final File historyDirectory) {
    RecursiveTreeMethod findLatestResult = new RecursiveTreeMethod() {
      @Override
      public void doMethod(int level, TreeItem item) {
        if (item.isTest()) {
          File directory = new File(historyDirectory, item.fullName);
          MostRecentPageHistoryReader reader = new MostRecentPageHistoryReader(directory);
          item.result = reader.findMostRecentTestRun();
        }
      }
    };
    treeRoot.doRecursive(findLatestResult, 0);
  }
  
  public String[] printTree() {
    final List<String> alllines = new LinkedList<String>();
    RecursiveTreeMethod printTreeItem = new RecursiveTreeMethod() {
      @Override
      public void doMethod(int level, TreeItem item) {
        String line = "";
        
        for (int counter = 0; counter < level - 1; counter++ ) {
          line += "-";
        }
        line += item.name;
        if (item.isTest()) {
          line += "*";
        }
        else {
          line += "(" + item.testsPassed + "," + item.testsFailed + "," + item.testsUnrun + ")";
        }

        alllines.add(line);
      }
    };
    
    treeRoot.doRecursive(printTreeItem, 0);
    return alllines.toArray(new String[alllines.size()]);
  }
  
  public class TreeItem
  {
    final String name;
    final String fullName;
    int testsPassed = 0;
    int testsUnrun = 0;
    int testsFailed = 0;
    final int folderlevel;

    
    List<TreeItem> branches = new LinkedList<TreeItem>();
    TestResultRecord result = null;
    
    public int getTestsPassed() {
      return testsPassed;
    }
    
    public int getTestsUnrun() {
      return testsUnrun;
    }
    
    public int getTestsFailed() {
      return testsFailed;
    }

    public String getName() {
      return name;
    }
    
    TreeItem(String branchName, String branchFullName, int afolderLevel) {
      name = branchName;
      fullName = branchFullName;
      folderlevel = afolderLevel; 
    }
    
    public List<TreeItem> getBranches() {
      return branches;
    }
    
    public void calculateResults() {
      testsPassed = 0;
      testsUnrun = 0;
      testsFailed = 0;
      
      if (isTest()) {
        if (result == null) {
          testsUnrun++;
        }
        else if ((result.getExceptions() == 0) && (result.getWrong() == 0)) {
          testsPassed++;
        }
        else {
          testsFailed++;
        }
      }
      else {
        for (TreeItem branch : branches) {
          testsUnrun += branch.testsUnrun;
          testsPassed += branch.testsPassed;
          testsFailed += branch.testsFailed;
        }
      }
    }

    @Override
    public String toString() {
      return name;
    }
  
    void addItem(String[] itemPath, int currentIndex) {
      if (currentIndex < itemPath.length) {
        //special case for this tree only, that all the titles should be organised before we start.
        if (nameSameAsLastName(itemPath[currentIndex])) {
          branches.get(branches.size() - 1).addItem(itemPath, ++currentIndex);
        }
        else {
          String branchName = itemPath[currentIndex];
          String branchFullName = fullName;
          branchFullName += (fullName.length() > 0) ? "." + branchName : branchName;
          TreeItem branch = new TreeItem(branchName, branchFullName, folderlevel + 1);
          branches.add(branch);
          branch.addItem(itemPath, ++currentIndex);
        }
      }
    }

    private boolean nameSameAsLastName(String currentName) {
      return !branches.isEmpty() && branches.get(branches.size() - 1).name.equals(currentName);
    }
    
    public boolean isTest() {
      return (branches.size() == 0);
    }
    
    public String getCssClass() {
      if (testsFailed != 0) {
        return "failures";
      }
      else if (testsUnrun != 0) {
        return "unrun";
      }
      else {
        return "done";
      }
    }
    
    void doRecursive(RecursiveTreeMethod method, int level) {
      if (method.shouldDoItemBeforeBranches() && (level != 0)) {
        method.doMethod(level, this);
      }
      
      for (TreeItem branch : branches)  {
        branch.doRecursive(method, level + 1);
      }
      
      if (!method.shouldDoItemBeforeBranches() && (level != 0)) {
        method.doMethod(level, this);
      }
    }
  }
  
  abstract class RecursiveTreeMethod
  {
    public boolean shouldDoItemBeforeBranches()
    {
      return true;
    }
    
    public abstract void doMethod(int level, TreeItem item);
 
  }
}

