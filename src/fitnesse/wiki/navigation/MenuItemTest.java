package fitnesse.wiki.navigation;

import fitnesse.wiki.PageType;
import junit.framework.TestCase;

public class MenuItemTest extends TestCase
{
  
  public void testMenuItemFromPageTypeWorks() {
    MenuItem item = new MenuItem("Fancy Name", PageType.SUITE);
    
    assertEquals("Fancy Name", item.getDisplayName());
    assertEquals("Suite", item.getAttributeToCheck());
    assertEquals("a.pagename?suite", item.getUrlPath("a.pagename"));
  }
  
  public void testMenuItemFromPageTypeWithDifferentUrlWorks() {
    MenuItem item = new MenuItem("Fancy Name", PageType.SUITE, "?queryParam");
    
    assertEquals("Fancy Name", item.getDisplayName());
    assertEquals("Suite", item.getAttributeToCheck());
    assertEquals("a.pagename?queryParam", item.getUrlPath("a.pagename"));
  }
  
  public void testMenuItemFromStringWithDifferentUrlWorks() {
    MenuItem item = new MenuItem("Fancy Name", "fake", "?queryParam");
    
    assertEquals("Fancy Name", item.getDisplayName());
    assertEquals("fake", item.getAttributeToCheck());
    assertEquals("a.pagename?queryParam", item.getUrlPath("a.pagename"));
  }

  public void testMenuItemFromStringWorks() {
    MenuItem item = new MenuItem("Fancy Name", "fake");
    
    assertEquals("Fancy Name", item.getDisplayName());
    assertEquals("fake", item.getAttributeToCheck());
    assertEquals("a.pagename?fake", item.getUrlPath("a.pagename"));
  }

  public void testUrlStringNotStartingWithQuestionMarkDoesNotAppend() {
    MenuItem item = new MenuItem("Fancy Name", "fake", "/fixedPath");
    
    assertEquals("Fancy Name", item.getDisplayName());
    assertEquals("fake", item.getAttributeToCheck());
    assertEquals("/fixedPath", item.getUrlPath("a.pagename"));
  }

  public void testDefaultMenuItemHasNoAccessKey() {
    MenuItem item = new MenuItem("Fancy Name", "fake", "/fixedPath");
    
    assertEquals(false, item.hasAccessKey());
  }

  public void testItemWithAccessKeySetHasAccessKey() {
    MenuItem item = new MenuItem("Fancy Name", "fake", "/fixedPath").addAccessKey("c");
    
    assertEquals(true, item.hasAccessKey());
    assertEquals("c", item.getAccessKey());
  }


  public void testCheckJavascriptLinkNotTrueByDefault() {
    MenuItem item = new MenuItem("Fancy Name", "fake", "/fixedPath");
    
    assertEquals(false, item.isJavascriptLink());
    assertNull(item.getOnClickEvent());
  }


  public void testCheckJavascriptLinkRecordedAsJs() {
    MenuItem item = new MenuItem("Fancy Name", "fake").addJavaScriptLink("doMethod()");
    
    assertEquals(true, item.isJavascriptLink());
    assertEquals("doMethod()", item.getOnClickEvent());
  }
  
  public void testIfNoAttributeToCheckThenIsAlwaysVisible() {
    MenuItem item = new MenuItem("Fancy Name").addJavaScriptLink("doMethod()");
    
    assertEquals(true, item.alwaysShow());
  }

  public void testIfAttributeToCheckAlwaysShowIsFalse() {
    MenuItem item = new MenuItem("Fancy Name", "fake");
    
    assertEquals(false, item.alwaysShow());
  }
}
