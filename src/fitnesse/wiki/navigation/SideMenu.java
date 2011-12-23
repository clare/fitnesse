package fitnesse.wiki.navigation;

import java.util.ArrayList;
import java.util.List;

import fitnesse.html.HtmlComment;
import fitnesse.html.HtmlTag;
import fitnesse.html.TagGroup;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PageType;
import fitnesse.wiki.WikiPageAction;

public class SideMenu {
  

    private static final String HTML_ID = "actionMenu";

    private static MenuGroup[] createStandardMenu() {
      MenuGroup runGroup = new MenuGroup("Run");
      runGroup.add(new MenuItem("Run Test", PageType.TEST).addAccessKey("t"));
      runGroup.add(new MenuItem("Debug", PageType.TEST, "?responder=test&remoteDebug=true"));
      runGroup.add(new MenuItem("Test History", PageType.TEST, "?testHistory"));
      
      runGroup.add(new MenuItem("Run Suite", PageType.SUITE));
      runGroup.add(new MenuItem("Debug", PageType.SUITE, "?responder=suite&remoteDebug=true"));
      runGroup.add(new MenuItem("Filter Run", PageType.SUITE, "?responder=suite&remoteDebug=true"));
      runGroup.add(new MenuItem("View previous runs", PageType.SUITE, "?testHistory"));
      
      MenuGroup editGroup = new MenuGroup("Edit");
      editGroup.add(new MenuItem("Edit Page", PageData.PropertyEDIT).addAccessKey("e"));
      editGroup.add(new MenuItem("Properties", PageData.PropertyPROPERTIES).addAccessKey("p"));
      editGroup.add(new MenuItem("Reorganise", PageData.PropertyREFACTOR).addAccessKey("r"));
      editGroup.add(new MenuItem("Add Child").addJavaScriptLink("popup('addChildPopup')"));
  
      
      MenuGroup searchGroup = new MenuGroup("Wiki");
      searchGroup.add(new MenuItem("Search", PageData.PropertySEARCH, "?searchForm").addAccessKey("s"));
      searchGroup.add(new MenuItem("Where Used", PageData.PropertyWHERE_USED, "?whereUsed").addAccessKey("w"));
      searchGroup.add(new MenuItem("Files", PageData.PropertyFILES, "/files").addAccessKey("f"));
      searchGroup.add(new MenuItem("Recent Changes", PageData.PropertyFILES, "/RecentChanges"));
      searchGroup.add(new MenuItem("Versions", PageData.PropertyVERSIONS).addAccessKey("v"));
      
      MenuGroup helpGroup = new MenuGroup("Help");
      helpGroup.add(new MenuItem("User Guide", "", ".FitNesse.UserGuide"));
      
      return new MenuGroup[] {runGroup, editGroup, searchGroup, helpGroup};
  }
  
  public static HtmlTag makeMenu(String pageName, boolean useNewWindow, PageData pagedata) {
    return makeMenu(createStandardMenu(), pageName, useNewWindow, pagedata);
  }

  public static HtmlTag makeMenu(MenuGroup[] layout, String pageName, boolean useNewWindow, PageData pagedata) {
    HtmlTag container = new HtmlTag("div");
    HtmlTag listTag = new HtmlTag("div");
    
    for (MenuGroup group : layout) {
      AddMenuGroup(group, pageName, useNewWindow, pagedata, listTag);
    }
    
    container.add(listTag);
    container.add(makeScript());
    
    return container;
  }
  
  
  private static HtmlTag makeScript() {
       HtmlTag scriptTag = new HtmlTag("script", "jQuery(document).ready(function(){ $('.accordionTitle').onclick(function() {" +
    "$(this).next().slideToggle('slow');    return false; }).next().hide(); });");
       scriptTag.addAttribute("type", "text/javascript");
    return scriptTag;
  }

  private static HtmlTag AddMenuGroup(MenuGroup group, String pageName, boolean useNewWindow,
      PageData pagedata, HtmlTag menuTag) {
    HtmlTag groupTag = new HtmlTag("div");
    int items = 0;
        
    for (MenuItem menuItem : group) {
      if ((menuItem.getAttributeToCheck() == null) || pagedata.hasAttribute(menuItem.getAttributeToCheck())) {
        groupTag.add(makeMenuItemTag(pageName, useNewWindow, menuItem));
        items++;
      }
    }
    
    if (items > 0) {
      HtmlTag liTag = new HtmlTag("div");
      HtmlTag header = new HtmlTag("h3", group.getDisplayName());
      header.addAttribute("class", "accordionTitle");
      liTag.add(header);
      liTag.add(groupTag);
      menuTag.add(liTag);
    }

    return groupTag;
  }

  private static HtmlTag makeMenuItemTag(String pageName, boolean useNewWindow, MenuItem menuItem) {
    HtmlTag linkTag = new HtmlTag("a", menuItem.getDisplayName());
    if (menuItem.isJavascriptLink()) {
      linkTag.addAttribute("onclick", menuItem.getOnClickEvent());
    }
    else {
      linkTag.addAttribute("href", menuItem.getUrlPath(pageName));
      if (useNewWindow) {
        linkTag.addAttribute("target", "newWindow");
      }
    }
    
    if (menuItem.hasAccessKey()) {
      linkTag.addAttribute("accesskey", menuItem.getAccessKey());
    }
    
    return linkTag;
//  linkTag.addAttribute("accesskey", action.getShortcutKey());
  }
}
