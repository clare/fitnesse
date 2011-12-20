package fitnesse.wikitext.parser;

import org.apache.velocity.VelocityContext;

import fitnesse.VelocityFactory;
import fitnesse.html.HtmlTag;
import fitnesse.html.HtmlUtil;
import fitnesse.wikitext.widgets.TOCWidget;
import util.Maybe;

public class Contents extends SymbolType implements Rule, Translation {
    public Contents() {
        super("Contents");
        wikiMatcher(new Matcher().string("!contents"));
        wikiRule(this);
        htmlTranslation(this);
    }

    public Maybe<Symbol> parse(Symbol current, Parser parser) {
        Symbol body = parser.parseToEnd(SymbolType.Newline);
        for (Symbol option: body.getChildren()) {
            if (option.isType(SymbolType.Whitespace)) continue;
            if (!option.getContent().startsWith("-")) return Symbol.nothing;
            current.add(option);
        }

        current.evaluateVariables(
                new String[] {TOCWidget.HELP_TOC, TOCWidget.REGRACE_TOC, TOCWidget.PROPERTY_TOC, TOCWidget.FILTER_TOC, TOCWidget.MORE_SUFFIX_TOC},
                parser.getVariableSource());

        return new Maybe<Symbol>(current);
    }
    public String toTarget(Translator translator, Symbol symbol) {
        ContentsItemBuilder itemBuilder
                = new ContentsItemBuilder(symbol, 1);
        HtmlTag contentsDiv = HtmlUtil.makeDivTag("contents");
        HtmlTag div = HtmlUtil.makeDivTag("toc1");
        String id="contentsXYZ";
        div.addAttribute("id", id);
        contentsDiv.add(HtmlUtil.makeBold("Contents:"));
        contentsDiv.add(itemBuilder.buildLevel(translator.getPage(), div));
        
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("listName", "#" +  id);

        contentsDiv.add(HtmlUtil.makeJavascriptLink("/files/javascript/jquery.js"));
        contentsDiv.add(HtmlUtil.makeJavascriptLink("/files/javascript/jquery.jstree.js"));
        HtmlTag scriptTag = new HtmlTag("script");
        try {
          scriptTag.add(VelocityFactory.translateTemplate(velocityContext, "contentsTree.vm"));
        } catch (Exception e) {
        }

        contentsDiv.add(scriptTag);

        return contentsDiv.html();
    }
}
