package com.vogella.ide.first.parser;

public class TestObject {
    public String filePath;
    public String methodName;
    public String methodAnnotations;
    public String variableName;
    public String dependency;
    public String creationType;
    public String rawStatement;

    public TestObject(String filePath, String methodName, String methodAnnotations, String objectName, String dependency, String creationType, String rawStatement) {
        this.filePath = filePath;
        this.methodName = methodName;
        this.methodAnnotations= methodAnnotations;
        this.variableName = objectName;
        this.dependency = dependency;
        this.creationType = creationType;
        this.rawStatement = rawStatement;
    }
}
