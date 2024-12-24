sealed class Token(val line: Int, val column: Int) {
    data class Identifier(val name: String, val l: Int, val c: Int) : Token(l, c)
    data class IntegerLiteral(val value: Int, val l: Int, val c: Int) : Token(l, c)
    class ScopeKeyword(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "scope"
    }

    class PrintKeyword(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "print"
    }

    class LBrace(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "{"
    }

    class RBrace(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "}"
    }

    class Equal(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "="
    }

    class EOF(l: Int, c: Int) : Token(l, c) {
        override fun toString() = "EOF"
    }
}