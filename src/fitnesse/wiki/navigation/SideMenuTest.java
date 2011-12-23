package fitnesse.wiki.navigation;


import util.RegexTestCase;
import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PageType;
import fitnesse.wiki.WikiPage;
import junit.framework.TestCase;

public class SideMenuTest extends RegexTestCase {
  
  private WikiPage root;

  public void setUp() throws Exception {
    root = InMemoryPage.makeRoot("root");
  }
 
  public void testJavascriptLinkMenuItem() throws Exception {
    MenuItem item = new MenuItem("sample").addJavaScriptLink("functionToRun()"); 
    
    assertSubString("<a onclick=\"functionToRun()\">sample</a>", getMenuHtml(item));
  }

  public void testMenuGroupWithoutItemsNotShown() throws Exception {
    MenuGroup singleGroup = new MenuGroup("GroupName");
    MenuItem item = new MenuItem("sample", "impossiblematch"); 
    singleGroup.add(item);

    String html = SideMenu.makeMenu(new MenuGroup[] {singleGroup}, "PageName", false, root.getData()).html();
    
    assertNotSubString("GroupName", html);
  }

  public void testHtmlWithQuestionmarkIncludesPageName() throws Exception {
    MenuItem item = new MenuItem("sample", PageData.PropertyEDIT, "?editPossibly"); 
    String html = getMenuHtml(item);
    assertSubString("<a href=\"PageName?editPossibly\">sample</a>", html);
  }
  
  private String getMenuHtml(MenuItem item) throws Exception {
      MenuGroup singleGroup = new MenuGroup("GroupName");
      singleGroup.add(item);
      return SideMenu.makeMenu(new MenuGroup[] {singleGroup}, "PageName", false, root.getData()).html();
  }
  

  public void testMakeDefaultActions() throws Exception {
    String pageName = "SomePage";
    String html = getActionsHtml(pageName);
    assertSubString("SomePage", html);
  }

  public void testMakeActionsWithTestButtonWhenNameStartsWithTest() throws Exception {
    String pageName = "TestSomething";
    String html = getActionsHtml(pageName);
    verifyDefaultLinks(html, pageName);
    assertSubString("<a href=\"" + pageName + "?test\" accesskey=\"t\">Run Test</a>", html);
  }

  public void testMakeActionsWithSuffixButtonWhenNameEndsWithTest() throws Exception {
    String pageName = "SomethingTest";
    String html = getActionsHtml(pageName);
    verifyDefaultLinks(html, pageName);
    assertSubString("<a href=\"" + pageName + "?test\" accesskey=\"t\">Run Test</a>", html);
  }

  public void testMakeActionsWithSuiteButtonWhenNameStartsWithSuite() throws Exception {
    String pageName = "SuiteNothings";
    String html = getActionsHtml(pageName);
    verifyDefaultLinks(html, pageName);
    assertSubString("<a href=\"" + pageName + "?suite\">Run Suite</a>", html);
  }

  public void testMakeActionsWithSuiteButtonWhenNameEndsWithSuite() throws Exception {
    String pageName = "NothingsSuite";
    String html = getActionsHtml(pageName);
    verifyDefaultLinks(html, pageName);
    assertSubString("<a href=\"" + pageName + "?suite\">Run Suite</a>", html);
  }

  private String getActionsHtml(String pageName) throws Exception {
    root.addChildPage(pageName);
    return root.getChildPage(pageName).getMenuHtml();
  }

  private void verifyDefaultLinks(String html, String pageName) {
    assertSubString("<a href=\"" + pageName + "?edit\" accesskey=\"e\">Edit Page</a>", html);
    assertSubString("<a href=\"" + pageName + "?versions\" accesskey=\"v\">Versions</a>", html);
    assertSubString("<a href=\"" + pageName + "?properties\" accesskey=\"p\">Properties</a>", html);
    assertSubString("<a href=\"" + pageName + "?refactor\" accesskey=\"r\">Reorganise</a>", html);
    assertSubString("<a href=\"" + pageName + "?whereUsed\" accesskey=\"w\">Where Used</a>", html);
    assertSubString("<a href=\"/files\" accesskey=\"f\">Files</a>", html);
    assertSubString("<a href=\"" + pageName + "?searchForm\" accesskey=\"s\">Search</a>", html);
    assertSubString("<a href=\".FitNesse.UserGuide\">User Guide</a>", html);
  }
}


