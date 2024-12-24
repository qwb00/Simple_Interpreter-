import kotlin.reflect.KClass

class Parser(private val tokens: List<Token>) {
    private var pos = 0
    private val current: Token get() = tokens[pos]

    fun parseProgram(): ProgramNode {
        val stmts = mutableListOf<ASTNode>()
        while (!match(Token.EOF::class)) {
            stmts.add(parseStatement())
        }
        return ProgramNode(stmts)
    }

    private fun parseStatement(): ASTNode {
        return when {
            check(Token.PrintKeyword::class) -> parsePrint()
            check(Token.ScopeKeyword::class) -> parseScope()
            checkIdentifierFollowedByEqual() -> parseAssignment()
            else -> parseError("Expected statement at ${current} (line ${current.line}, col ${current.column})")
        }
    }

    private fun parseAssignment(): AssignNode {
        val (identifier, line, col) = consumeIdentifierWithPos()
        consume(Token.Equal::class)
        val value = when (val t = current) {
            is Token.Identifier -> {
                val (id, l, c) = consumeIdentifierWithPos()
                val node = IdentifierNode(id)
                node.line = l; node.column = c
                node
            }
            is Token.IntegerLiteral -> {
                val (intVal, l, c) = consumeIntegerWithPos()
                val node = IntNode(intVal)
                node.line = l; node.column = c
                node
            }
            else -> parseError("Expected identifier or integer after '=', got $t (line ${t.line}, col ${t.column})")
        }
        val node = AssignNode(identifier, value)
        node.line = line; node.column = col
        return node
    }

    private fun parsePrint(): PrintNode {
        val printToken = consume(Token.PrintKeyword::class)
        val (varName, l, c) = consumeIdentifierWithPos()
        val node = PrintNode(varName)
        node.line = l; node.column = c
        return node
    }

    private fun parseScope(): ScopeNode {
        val scopeToken = consume(Token.ScopeKeyword::class)
        consume(Token.LBrace::class)
        val stmts = mutableListOf<ASTNode>()
        while (!check(Token.RBrace::class) && !check(Token.EOF::class)) {
            stmts.add(parseStatement())
        }
        consume(Token.RBrace::class)
        val node = ScopeNode(stmts)
        node.line = scopeToken.line; node.column = scopeToken.column
        return node
    }

    /** Utility methods for parser **/
    private fun consume(expected: KClass<out Token>): Token {
        if (!match(expected)) {
            parseError("Expected ${expected.simpleName} but got ${current} (line ${current.line}, col ${current.column})")
        }
        return tokens[pos - 1]
    }

    private fun consumeIdentifierWithPos(): Triple<String, Int, Int> {
        val t = current
        if (t is Token.Identifier) {
            pos++
            return Triple(t.name, t.line, t.column)
        } else {
            parseError("Expected identifier, got $t (line ${t.line}, col ${t.column})")
        }
    }

    private fun consumeIntegerWithPos(): Triple<Int, Int, Int> {
        val t = current
        if (t is Token.IntegerLiteral) {
            pos++
            return Triple(t.value, t.line, t.column)
        } else {
            parseError("Expected integer literal, got $t (line ${t.line}, col ${t.column})")
        }
    }

    private fun check(tokenClass: KClass<out Token>): Boolean {
        return if (pos < tokens.size) tokenClass.isInstance(current) else false
    }

    private fun checkIdentifierFollowedByEqual(): Boolean {
        // Look ahead: identifier '=' ...
        if (current is Token.Identifier && pos+1 < tokens.size && tokens[pos+1] is Token.Equal) {
            return true
        }
        return false
    }

    private fun match(tokenClass: KClass<out Token>): Boolean {
        if (pos < tokens.size && tokenClass.isInstance(current)) {
            pos++
            return true
        }
        return false
    }

    private fun parseError(message: String): Nothing {
        throw RuntimeException("Parse error: $message")
    }
}
