package fitnesse.responders.refactoring.moving;

import fitnesse.responders.refactoring.RenamePageResponder;
import fitnesse.wiki.FileSystemPage;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class RenamePageAsMoveResponder extends RenamePageResponder {
  @Override
  protected void movePage(WikiPage movedPage, WikiPage targetPage) throws Exception {
    MoveCmSystem cmSystem = new MoveCmSystem(movedPage.getData());
    cmSystem.move(movedPage, targetPage);
  }
}
