package com.vogella.ide.first.parser;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class TestObjectHandler extends AbstractHandler {

  ArrayList<TestObject> testObjects = new ArrayList<>();
  private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    testObjects.clear();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    // Get all projects in the workspace
    IProject[] projects = root.getProjects();
    // Loop over all projects
    for (IProject project : projects) {

        try {
			if (project.isNatureEnabled(JDT_NATURE)) {
			  analyseMethods(project);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("skipped "+project.getName());
		}

    }
    String outPutPath =  		
    	"path\\floder\\to\\output\\"
        + projects[0].getName() + "-Result.json";
    printResults(outPutPath);

	Shell shell = HandlerUtil.getActiveShell(event);
	MessageDialog.openInformation(shell, "Handler Triggered", "Output results to: " + new File(outPutPath).getAbsolutePath() + ", Object count: " + testObjects.size());
    return null;
  }

  private boolean isTestClass(ICompilationUnit unit) throws CoreException {
    if (unit.getImports().length <= 0) {
      return false;
    }
    for (IImportDeclaration importDeclaration : unit.getImports()) {
      if (importDeclaration.getElementName().contains("junit")) {
        return true;
      }
    }
    return false;
  }

  private void analyseMethods(IProject project) throws CoreException {
    IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
    // parse(JavaCore.create(project));
    for (IPackageFragment mypackage : packages) {
      if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
        createAST(mypackage);
      }

    }
  }

  private void createAST(IPackageFragment mypackage) throws CoreException {
    for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
      // now create the AST for the ICompilationUnits
      if (!isTestClass(unit)) {
        continue;
      }
      try {
      CompilationUnit parse = parse(unit);
      ObjectVisitor visitor = new ObjectVisitor(unit.getPath().toString());
      parse.accept(visitor);

      String packageName=unit.getPackageDeclarations()[0].getElementName().toString();
      for (TestObject singleObject : visitor.getObjects()) {
//    	  singleObject.filePath = unit.getPath().toString();
        testObjects.add(singleObject);
      }
     }
      catch (CoreException e) {
          e.printStackTrace();
        }
    }
  }



  private void printResults(String path) {
	    if (!testObjects.isEmpty()) {
	        Gson gson = new GsonBuilder()
	                .setPrettyPrinting()
	                .disableHtmlEscaping() // 禁用 HTML 转义
	                .create();
	                
	        String jsonOutput = gson.toJson(testObjects);  // 将列表转换为 JSON 字符串

	        try (FileOutputStream fos = new FileOutputStream(path)) {
	            fos.write(jsonOutput.getBytes());
	            fos.flush();
	            System.out.println("Output written to: " + new File(path).getAbsolutePath() + ", count: " + testObjects.size());
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}


  private static CompilationUnit parse(ICompilationUnit unit) {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(unit);
    parser.setResolveBindings(true);
    return (CompilationUnit) parser.createAST(null);
  }
}