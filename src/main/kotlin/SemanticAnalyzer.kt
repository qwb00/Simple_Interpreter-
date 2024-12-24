import java.util.*
import java.util.ArrayDeque

class SemanticAnalyzer {
    private val scopeStack: Deque<MutableSet<String>> = ArrayDeque()

    fun analyze(program: ProgramNode) {
        scopeStack.push(mutableSetOf())
        analyzeStatements(program.statements)
        scopeStack.pop()
    }

    private fun analyzeStatements(statements: List<ASTNode>) {
        for (stmt in statements) {
            when (stmt) {
                is AssignNode -> analyzeAssign(stmt)
                is ScopeNode -> analyzeScope(stmt)
                else -> {}
            }
        }
    }

    private fun analyzeAssign(node: AssignNode) {
        when (val v = node.value) {
            is IdentifierNode -> {
                if (!isVariableDefined(v.name)) {
                    semanticError("Variable '${v.name}' not defined before usage", v.line, v.column)
                }
            }
            is IntNode -> { /* no check needed for integers */ }
        }
        // Define the target variable in the current scope
        currentScope().add(node.target)
    }

    private fun analyzeScope(node: ScopeNode) {
        scopeStack.push(mutableSetOf())
        analyzeStatements(node.statements)
        scopeStack.pop()
    }

    private fun isVariableDefined(name: String): Boolean {
        for (scope in scopeStack) {
            if (scope.contains(name)) {
                return true
            }
        }
        return false
    }

    private fun semanticError(message: String, line: Int, column: Int): Nothing {
        throw RuntimeException("Semantic error at line $line, column $column: $message")
    }

    private fun currentScope(): MutableSet<String> = scopeStack.peek()
}