# Simple Interpreter

This is a simple interpreter for a custom programming language that supports the following operations:

- Variable assignment (e.g., `x = 10`)
- Scoped operations using `{}` (e.g., `scope { ... }`)
- Printing variable values (e.g., `print x`)

## Features

- Supports nested scopes
- Prints `null` if a variable is not defined
- Variables declared inside a scope are removed when the scope ends

## How to Run

1. **Build the project**:
   Ensure that Gradle is properly set up and run:
   ```bash
   ./gradlew build
   
2. **Execute the interpreter with your code file**:
   ```bash
   ./gradlew runWithFile -Pfile=<file_with_your_code>
   
### **Example:**
```bash
./gradlew runWithFile -Pfile=src/test/kotlin/test  
