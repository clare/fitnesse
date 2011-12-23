package fitnesse.wiki.navigation;

import fitnesse.wiki.PageType;

public class MenuItem {
  
  final String displayName;
  
  final String propertyRequired;
  
  final String urlstring;
 
  final String accessKey;
  
  final boolean isJavaScriptLink;
  
  public MenuItem(String displayName) {
    this(displayName, null, "", null, false);
  }
  
  public MenuItem(String displayName, String propertyRequired) {
    this(displayName, propertyRequired, "?" + propertyRequired.toLowerCase());
  }

  public MenuItem(String displayName, PageType pageType) {
    this(displayName, pageType.toString());
  }

  public MenuItem(String displayName, PageType pageType, String urlString) {
    this(displayName, pageType.toString(), urlString);
  }

  public MenuItem(String displayName, String propertyRequired, String urlString) {
    this(displayName, propertyRequired, urlString, null, false);
  }

  private MenuItem(String displayName, String propertyRequired, String urlString, String accessKey, boolean isJavascriptLink) {
    this.displayName = displayName;
    this.propertyRequired = (propertyRequired != null) && (!"".equals(propertyRequired)) ? propertyRequired : null;
    this.urlstring = urlString;
    this.accessKey = accessKey;
    this.isJavaScriptLink = isJavascriptLink;
  }
  
  public MenuItem addAccessKey(String newAccessKey) {
    return new MenuItem(displayName, propertyRequired, urlstring, newAccessKey, isJavaScriptLink);  
  }

  public MenuItem addJavaScriptLink(String jsOnClickLink) {
    return new MenuItem(displayName, propertyRequired, jsOnClickLink, accessKey, true);  
  }
  
  public String getDisplayName() {
    return displayName;
  }

  public String getAttributeToCheck() {
    return propertyRequired;
  }

  public String getUrlPath(String pagePath) {
    if (!isJavaScriptLink) 
    {
      if (urlstring != null && urlstring.startsWith("?")) {
        return pagePath + urlstring;
      }
      return urlstring;
    }
    return null;
  }
  
  public String getAccessKey() {
    return accessKey;
  }
  
  public boolean hasAccessKey() {
    return accessKey != null;
  }

  public boolean isJavascriptLink() {
    return isJavaScriptLink;
  }

  public String getOnClickEvent() {
    if (isJavaScriptLink) 
    {
      return urlstring;
    }
    return null;
  }

  public boolean alwaysShow() {
    return (propertyRequired == null);
  }
}
