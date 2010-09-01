// TestConditionWidget plugin to fitnesse
// Copyright (C) 2010 by Clare McLennan, Clarus Consultants.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.wikitext.widgets;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitnesse.html.HtmlTag;
import fitnesse.wiki.PageData;
import fitnesse.wiki.WikiPagePath;
import fitnesse.wikitext.WikiWidget;

//created by Clare McLennan

public class TestConditionsWidget extends CollapsableWidget {
  public static final String REGEXP = "^!tc[ \\t]+(tc_\\d+(?:,tc_\\d+)*)";
  
  private static final String PREFIX = "tc_";

  public TestConditionsWidget(ParentWidget parent, String text) throws Exception {
    super(parent, makeCollapsableWidgetMarkup(text));
  }
  
  private static String makeCollapsableWidgetMarkup(String text) {

    String title = "";
    Matcher match = Pattern.compile(REGEXP).matcher(text);
    if (match.find()) {
      String[] tcs = makeTcsList(match.group(1));
      title = makeTitle(tcs);
    }
    
    return "!*> " + title + "\nTest conditions will be viewable soon\n*!";
  }

  private static String makeTitle(String[] tcs) {
    String title = "Test Conditions: ";
    
    for (String tc : tcs) {
      title += tc;
      title += ", ";
    }
    
    title = title.substring(0, title.length() - 2);
    return title;
  }

  private static String[] makeTcsList(String group) {
    String[] tcs = group.split(",");
    for (int index = 0; index < tcs.length; index++) {
      if (tcs[index].toLowerCase().startsWith(PREFIX)) {
        tcs[index] = tcs[index].substring(PREFIX.length());
      }
    }
    return tcs;
  }
  
  @Override
  public String render() throws Exception {
    String html = super.render();
    html = html.replace("javascript:toggleCollapsable(", "javascript:toggleCollapsibleRetreivable('http://localhost:11112/files/223.html', ");
    return html;
  }
}
