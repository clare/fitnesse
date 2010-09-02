package fitnesse.responders.testHistory;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.VelocityFactory;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.html.HtmlPage;
import fitnesse.html.HtmlUtil;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.run.SuiteContentsFinder;
import fitnesse.responders.run.SuiteFilter;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;

public class SuiteCompletionResponder implements Responder {

  public Response makeResponse(FitNesseContext context, Request request) throws Exception {
    WikiPage root = context.root;
    WikiPage page = root.getPageCrawler().getPage(root, PathParser.parse(request.getResource()));
    
    SuiteFilter filter = new SuiteFilter(request, page.getPageCrawler().getFullPath(page).toString());
    SuiteContentsFinder suiteTestFinder = new SuiteContentsFinder(page, filter, root);
    
    List<WikiPage> pagelist = suiteTestFinder.makePageList();
    CompletenessTree treeview = new CompletenessTree(pagelist);
    treeview.findLatestResults(context.getTestHistoryDirectory());
    treeview.countResults();
    String[] treeLines = treeview.printTree();
    
    SimpleResponse response = new SimpleResponse(400);
    HtmlPage html = context.htmlPageFactory.newPage();
    HtmlUtil.addTitles(html, "Suite Completion Responder");
    html.main.add("<ul>");
    for (String line : treeLines) {
      html.main.add("<li>" + line + "</li>");
    }
    
    html.main.add("</ul>");
    response.setContent(html.html());

    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("treeRoot", treeview.treeRoot);
    String velocityTemplate = "pageCompletion.vm";
    Template template = VelocityFactory.getVelocityEngine().getTemplate(velocityTemplate);
    StringWriter writer = new StringWriter();
    template.merge(velocityContext, writer);
    response.setContent(writer.toString());
    return response;

  }
}
