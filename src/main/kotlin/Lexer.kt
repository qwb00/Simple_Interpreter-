class Lexer(private val input: String) {
    private var pos = 0
    private val length = input.length
    private var line = 1
    private var column = 1

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()
        while (pos < length) {
            val ch = input[pos]
            when {
                ch == '\n' -> {
                    pos++
                    line++
                    column = 1
                }
                ch.isWhitespace() -> {
                    pos++
                    column++
                }
                ch.isDigit() -> tokens.add(integerLiteral())
                ch.isLetter() -> tokens.add(identifierOrKeyword())
                ch == '{' -> {
                    tokens.add(Token.LBrace(line, column))
                    pos++
                    column++
                }
                ch == '}' -> {
                    tokens.add(Token.RBrace(line, column))
                    pos++
                    column++
                }
                ch == '=' -> {
                    tokens.add(Token.Equal(line, column))
                    pos++
                    column++
                }
                else -> error("Unexpected character: '$ch' at line $line, column $column")
            }
        }
        tokens.add(Token.EOF(line, column))
        return tokens
    }

    private fun integerLiteral(): Token.IntegerLiteral {
        val startPos = pos
        val startCol = column
        while (pos < length && input[pos].isDigit()) {
            pos++
            column++
        }
        val value = input.substring(startPos, pos).toInt()
        return Token.IntegerLiteral(value, line, startCol)
    }

    private fun identifierOrKeyword(): Token {
        val startPos = pos
        val startCol = column
        while (pos < length && (input[pos].isLetterOrDigit() || input[pos] == '_')) {
            pos++
            column++
        }
        val name = input.substring(startPos, pos)
        return when (name) {
            "scope" -> Token.ScopeKeyword(line, startCol)
            "print" -> Token.PrintKeyword(line, startCol)
            else -> Token.Identifier(name, line, startCol)
        }
    }

    private fun error(message: String): Nothing {
        throw RuntimeException(message)
    }
}
