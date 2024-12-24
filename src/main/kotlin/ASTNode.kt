sealed class ASTNode {
    var line: Int = 0
    var column: Int = 0
}

data class ProgramNode(val statements: List<ASTNode>) : ASTNode()

data class AssignNode(val target: String, val value: ValueNode) : ASTNode()
sealed class ValueNode : ASTNode()
data class IdentifierNode(val name: String) : ValueNode()
data class IntNode(val value: Int) : ValueNode()

data class PrintNode(val variableName: String) : ASTNode()
data class ScopeNode(val statements: List<ASTNode>) : ASTNode()