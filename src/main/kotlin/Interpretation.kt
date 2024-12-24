import java.util.*
import java.util.ArrayDeque

class Interpreter {
    private val scopeStack: Deque<MutableMap<String, Int>> = ArrayDeque()

    init {
        // Global scope
        scopeStack.push(mutableMapOf())
    }

    fun interpret(program: ProgramNode) {
        executeStatements(program.statements)
    }

    private fun executeStatements(statements: List<ASTNode>) {
        for (stmt in statements) {
            when (stmt) {
                is AssignNode -> executeAssign(stmt)
                is PrintNode -> executePrint(stmt)
                is ScopeNode -> executeScope(stmt)
                else -> runtimeError("Unknown statement type: $stmt", stmt.line, stmt.column)
            }
        }
    }

    private fun executeAssign(node: AssignNode) {
        val value = when (val v = node.value) {
            is IdentifierNode -> resolveVariable(v.name)
            is IntNode -> v.value
        }
        currentScope()[node.target] = value!!
    }

    private fun executePrint(node: PrintNode) {
        val value = resolveVariable(node.variableName)
        println(value?.toString() ?: "null")
    }

    private fun executeScope(node: ScopeNode) {
        // Create new scope
        scopeStack.push(mutableMapOf())
        executeStatements(node.statements)
        // Pop scope
        scopeStack.pop()
    }

    private fun resolveVariable(name: String): Int? {
        for (scope in scopeStack) {
            if (scope.containsKey(name)) {
                return scope[name]
            }
        }
        return null
    }

    private fun currentScope(): MutableMap<String, Int> = scopeStack.peek()

    private fun runtimeError(message: String, line: Int, column: Int): Nothing {
        throw RuntimeException("Runtime error at line $line, column $column: $message")
    }
}