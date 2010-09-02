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
  public static final String REGEXP = "^!tc[ \\t]+tc_\\d+(?:,tc_\\d+)*";
  public static final Pattern pattern = Pattern.compile("^!tc[ \\t]+(tc_\\d+(?:,tc_\\d+)*)");
  private static final String WAIT_STRING = "Test conditions will be viewable soon";
  private static final String PREFIX = "tc_";
  private final String numberList;

  public TestConditionsWidget(ParentWidget parent, String text) throws Exception {
    super(parent, makeCollapsableWidgetMarkup(text));
    
    numberList = makeTcNumbersList(text, ",");
  }
  
  private static String makeCollapsableWidgetMarkup(String text) {

    String title = "";
    
    return "!*> Test Conditions: " + makeTcNumbersList(text, ", ") + 
    "\n" + WAIT_STRING + "\n*!";
  }

  private static String makeTcNumbersList(String text, String separator) {
    Matcher match = pattern.matcher(text);
    String list = ""; 
    
    if (match.find()) {
      String[] tcs = makeTcsList(match.group(1));
      for (String tc : tcs) {
        list += tc;
        list += separator;
      }
      list = list.substring(0, list.length() - separator.length());
    }
    return list;
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
    String url = parent.getVariable("TC_URL");
    
    if (url == null) {
      html = html.replace(WAIT_STRING, "You need to set the \"TC_URL\" to display the test conditions.");
    }
    else {
      html = html.replace("javascript:toggleCollapsable(", 
          "javascript:toggleCollapsibleRetreivable('" + url + numberList + "', ");
      
    }
    return html;
  }
}
