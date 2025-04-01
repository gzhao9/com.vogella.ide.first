# JUnit Test Variable Analyzer (JDT-based)

This tool analyzes Java test classes that import JUnit and extracts information about all variables declared in those test methods. It uses Eclipse JDT to parse source code and generates structured JSON output for downstream analysis.

---

## What This Tool Does

- Recursively scans all Java files in the Eclipse workspace.
- Identifies test classes that import JUnit (e.g., `org.junit.Test`).
- Visits each test method and records all declared variables.
- Captures each variable’s name, declared type, instantiation method, and source code context.

---

## Output Format

Each variable is represented as a JSON object with the following fields:

| Field              | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| `filePath`         | Full path to the Java test file where the variable is declared             |
| `methodName`       | The name of the test method in which the variable is declared              |
| `methodAnnotations`| Annotations present on the method (e.g., `@Test`, `@Before`)               |
| `variableName`     | The name of the variable declared inside the method                        |
| `dependency`       | Fully-qualified class name of the variable's resolved type                 |
| `creationType`     | The instantiation pattern or method used (e.g., `Mockito.mock`, `new X`)   |
| `rawStatement`     | The full source line of the variable declaration (flattened to one line)   |

### Example Output

```json
{
  "filePath": "/dubbo-auth/src/test/java/org/apache/dubbo/auth/AccessKeyAuthenticatorTest.java",
  "methodName": "testSignForRequest",
  "methodAnnotations": "Test",
  "variableName": "url",
  "dependency": "org.apache.dubbo.common.URL",
  "creationType": "URL.addParameter",
  "rawStatement": "url=URL.valueOf(\"dubbo://10.10.10.10:2181\").addParameter(Constants.ACCESS_KEY_ID_KEY,\"ak\").addParameter(CommonConstants.APPLICATION_KEY,\"test\").addParameter(Constants.SECRET_ACCESS_KEY_KEY,\"sk\")"
}
```

---

## How to Use

1. Import the project into Eclipse as a Plug-in Project.
2. Launch the plugin using:
   `Run > Run Configurations > Eclipse Application`
3. In the runtime Eclipse window:
   - Open or import Java projects containing test files.
   - Trigger the tool via the menu entry (e.g., `Mock Clone Tools > Run JDT Parser`).
4. A `.json` file containing the extracted data will be generated in the configured output directory.

---

## Dependencies

- Eclipse JDT Core
- GSON (for JSON serialization)
