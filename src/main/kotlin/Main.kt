import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: program <file_path>")
        return
    }

    val filePath = args[0]

    try {
        val programInput = File(filePath).readText()

        val tokens = Lexer(programInput).tokenize()
        val ast = Parser(tokens).parseProgram()

        SemanticAnalyzer().analyze(ast)
        Interpreter().interpret(ast)
    } catch (e: RuntimeException) {
        println("Error: ${e.message}")
    } catch (e: java.io.IOException) {
        println("File error: ${e.message}")
    }
}
