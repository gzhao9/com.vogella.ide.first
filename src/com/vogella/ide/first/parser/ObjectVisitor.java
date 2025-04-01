package com.vogella.ide.first.parser;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


public class ObjectVisitor extends ASTVisitor {

	public String filePath;
	
	public ObjectVisitor(String filePath) {
		// TODO Auto-generated constructor stub
		this.filePath = filePath;
	}

	ArrayList<TestObject> testObjects = new ArrayList<>();



    @Override
    public boolean visit(FieldDeclaration node) {
        String annotations = extractAnnotations(node);
        String dependency = node.getType().resolveBinding() != null ? node.getType().resolveBinding().getQualifiedName() : "";

        for (Object fragment : node.fragments()) {
            if (fragment instanceof VariableDeclarationFragment) {
                VariableDeclarationFragment varFragment = (VariableDeclarationFragment) fragment;
                TestObject testObject = new TestObject(
                        filePath,
                        "[FieldDeclaration]",
                        annotations,
                        varFragment.getName().toString(),
                        dependency,
                        node.getType().toString(),
                        node.toString().replace("\n", "")
                );
                testObjects.add(testObject);
            }
        }
        return super.visit(node);
    }
    
    @Override
    public boolean visit(MethodDeclaration node) {
        String methodName = node.getName().getIdentifier();
        String methodAnnotations = extractAnnotations(node);

        node.accept(new ASTVisitor() {
            @Override
            public boolean visit(VariableDeclarationFragment fragment) {
                String creationType = "";
                String dependency = "";

                // 获取类型绑定信息（dependency）
                if (fragment.resolveBinding() != null && fragment.resolveBinding().getType() != null) {
                    dependency = fragment.resolveBinding().getType().getQualifiedName();
                }

                // 检查初始化表达式是否是类实例创建
                if (fragment.getInitializer() instanceof ClassInstanceCreation) {
                    ClassInstanceCreation initializer = (ClassInstanceCreation) fragment.getInitializer();
                    ITypeBinding typeBinding = initializer.resolveTypeBinding();
                    if (typeBinding != null) {
                        creationType = typeBinding.getQualifiedName();
                    } else {
                        creationType = initializer.getType().toString();
                    }
                }
                // 检查初始化表达式是否是方法调用
                else if (fragment.getInitializer() instanceof MethodInvocation) {
                    MethodInvocation initializer = (MethodInvocation) fragment.getInitializer();
                    IMethodBinding methodBinding = initializer.resolveMethodBinding();

                    if (methodBinding != null) {
                        ITypeBinding declaringClassBinding = methodBinding.getDeclaringClass();
                        ITypeBinding returnTypeBinding = methodBinding.getReturnType();
                        if (declaringClassBinding != null) {
                        	dependency = returnTypeBinding.getQualifiedName();
//                            creationType = methodBinding.getName();
                        	creationType =  declaringClassBinding.getName() + "." + methodBinding.getName();
                        	
                        }
                    } else {
                    	Expression expr = initializer.getExpression();
                    	if (expr != null) {
                    	    creationType = expr.toString() + "." + initializer.getName().getIdentifier();
                    	} else {
                    	    creationType = initializer.getName().getIdentifier();
                    	}
                    }
                }

                TestObject testObject = new TestObject(
                        filePath,
                        methodName,
                        methodAnnotations,
                        fragment.getName().toString(),
                        dependency,
                        creationType,
                        fragment.toString().replace("\n", "")
                );
                testObjects.add(testObject);
                return super.visit(fragment);
            }
        });
        return super.visit(node);
    }


    private String extractAnnotations(BodyDeclaration node) {
        List<String> annotationsList = new ArrayList<>();
        for (Object modifier : node.modifiers()) {
            if (modifier instanceof MarkerAnnotation) {
                annotationsList.add(((MarkerAnnotation) modifier).getTypeName().getFullyQualifiedName());
            }
        }
        return String.join(", ", annotationsList);
    }


	ArrayList<TestObject> getObjects(){
		return this.testObjects;
	}

}
