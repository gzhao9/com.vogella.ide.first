# Task: Mock Analysis in Java Test Code

This task is based on our published paper:  
An empirical study on the usage of mocking frameworks in Apache software foundation  
https://link.springer.com/content/pdf/10.1007/s10664-023-10410-y.pdf

## Task Overview

We provide a Java parser built with Eclipse JDT. This tool extracts all variable declarations from Java test classes that import JUnit. The parser is available at the following link:  
[https://github.com/gzhao9/com.vogella.ide.first](https://github.com/gzhao9/com.vogella.ide.first)

Your task is as follows:

1. Select one Apache Java project.
2. Use the provided parser to extract variable data from its test classes.
3. Analyze the output to identify the five most frequently mocked classes in the project.
4. Write a report (no more than two pages) that:
  - Lists the top five most frequently mocked classes in the project.
  - Explains why those classes might be frequently mocked.
5. Submit your analysis scripts and all intermediate data generated during your work.

## Report Requirements

The report should be no longer than two pages and must include:

- The name and description of the selected project
- The top five most frequently mocked classes for the project
- Your explanation or hypothesis about why those classes are mocked often
- A brief description of how you conducted the analysis

## Resources

The following links may be helpful during this task:

- Mockito official documentation    
  https://site.mockito.org/

- Eclipse JDT AST documentation    
  https://help.eclipse.org/latest/index.jsp?topic=/org.eclipse.jdt.doc.isv/guide/jdt_int_model.htm

- Eclipse JDT - Abstract Syntax Tree (AST) and the Java Model      
  https://vogella.com/tutorials/EclipseJDT/article.html  
  
- The original paper    
  https://link.springer.com/content/pdf/10.1007/s10664-023-10410-y.pdf


## Notes

You may use any tools or programming languages to assist your analysis.

You may also use AI tools (such as ChatGPT or GitHub Copilot), but you must clearly indicate where and how you used them.

Please include the following in your submission:

- The two-page report (PDF)
- Your analysis code or scripts
- All intermediate data files

If anything is unclear, feel free to ask for clarification.
