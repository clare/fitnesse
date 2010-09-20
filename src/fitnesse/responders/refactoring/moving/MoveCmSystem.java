package fitnesse.responders.refactoring.moving;

import java.lang.reflect.Method;

import fitnesse.wiki.FileSystemPage;
import fitnesse.wiki.PageData;
import fitnesse.wiki.WikiPage;

public class MoveCmSystem {
  
  private final PageData pageData;
  
  public MoveCmSystem(PageData aPageData) {
    pageData = aPageData;
  }

  public void move(WikiPage sourcePage, WikiPage destinationPage) throws Exception {
    if (sourcePage instanceof FileSystemPage && destinationPage instanceof FileSystemPage)
    {
      String sourcePath = ((FileSystemPage) sourcePage).getFileSystemPath();
      String destinationPath = ((FileSystemPage) destinationPage).getFileSystemPath();
      invokeCmMethod("cmMove", sourcePath, destinationPath);
    }
    else 
    {
      throw new Exception("Could not move page as refactor works only on simple pages");
    }
  }

  private void invokeCmMethod(String method, String param1, String param2) throws Exception {
    if (getCmSystemClassName() != null) {
      try {
        Class<?> cmSystem = Class.forName(getCmSystemClassName());
        Method updateMethod = cmSystem.getMethod(method, String.class, String.class, String.class);
        updateMethod.invoke(null, param1, param2, getCmSystemVariable());
      } catch (Exception e) {
        System.err.println("Could not invoke static " + method + "(path,path) of " + getCmSystemClassName());
        e.printStackTrace();
      }
    }
  }

  private String getCmSystemClassName() throws Exception {
    String cmSystemVariable = getCmSystemVariable();
    if (cmSystemVariable == null)
      return null;
    String cmSystemClassName = cmSystemVariable.split(" ")[0].trim();
    if (cmSystemClassName == null || cmSystemClassName.equals(""))
      return null;

    return cmSystemClassName;
  }

  private String getCmSystemVariable() throws Exception {
    return pageData.getVariable("CM_SYSTEM");
  }
}
