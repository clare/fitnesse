// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.wikitext.widgets;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;

import com.sun.xml.internal.fastinfoset.sax.Properties;

import fitnesse.wiki.InMemoryPage;
import fitnesse.wiki.PageData;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageProperties;

//created by Clare McLennan

public class TestConditionsWidgetTest extends WidgetTestCase {
  private WikiPage root;
  private WikiPage page;
  private WikiPage pageNoHelp;


  public void testRegularExpression() throws Exception {
    assertTrue(Pattern.matches(TestConditionsWidget.REGEXP, "!tc tc_234"));
    assertTrue(Pattern.matches(TestConditionsWidget.REGEXP, "!tc tc_234,tc_334"));
    assertFalse(Pattern.matches(TestConditionsWidget.REGEXP, "!tc tc_234,334"));
    assertFalse(Pattern.matches(TestConditionsWidget.REGEXP, "!tc 233,334"));
    assertFalse(Pattern.matches(TestConditionsWidget.REGEXP, "!tc 234 334a"));
  }

//  public void testResultsWithHelp() throws Exception {
//    setUp();
//    HelpWidget widget = new HelpWidget(new WidgetRoot(page), "!help");
//    assertEquals("some page is about some text", widget.render());
//
//    HelpWidget editableWidget = new HelpWidget(new WidgetRoot(page), "!help -editable");
//    assertEquals("some page is about some text " +
//    		"<a href=\"SomePage?properties\">(edit)</a>", editableWidget.render());
//  }
//
//  public void testResultsWithoutHelp() throws Exception {
//    setUp();
//    HelpWidget widget = new HelpWidget(new WidgetRoot(pageNoHelp), "!help");
//    assertEquals("", widget.render());
//
//    HelpWidget editableWidget = new HelpWidget(new WidgetRoot(pageNoHelp), "!help -editable");
//    assertEquals(" <a href=\"NoHelp?properties\">(edit help text)</a>", editableWidget.render());
//  }

  
  protected String getRegexp() {
    return HelpWidget.REGEXP;
  }

}
