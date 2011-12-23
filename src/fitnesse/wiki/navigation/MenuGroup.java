package fitnesse.wiki.navigation;

import java.util.LinkedList;

import fitnesse.wiki.PageType;

@SuppressWarnings("serial")
public class MenuGroup extends LinkedList<MenuItem>{
  final private String displayName ;
  
  public String getDisplayName() {
    return displayName;
  }

  public MenuGroup(String string) {
    displayName = string;
  }
}
