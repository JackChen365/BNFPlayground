package cz.bnf.playground

/**
 * All the AST node classes.
 * The class represent different syntax
 * Here is the syntax list for the BNF
 * <p>
 *  <text> ::= ([a-z] | [0-9] | "*" | "/" | "+" | "-" | "(" | ")")+
 *  <quantifier> ::= "?" | "+" | "*"
 *  <exp_name> ::= "<" <text> ">"
 *  <var> ::= "\"" <text> "\""
 *  <var_group> ::= "[" <text> "-" <text> "]"
 *  <factor> ::= <var> | <exp_name> | <var_group> | "(" <exp_list> ")"
 *  <exp> ::= <factor> <quantifier>?
 *  <exp_list> ::= <exp> "|"? <exp_list> | <exp>
 *  <declaration> ::= <exp_name> "::=" <exp_list>
 *  <program> ::= <declaration> <program> | <declaration>
 * </p>
 */
sealed class ASTNode {

    /**
     * The basic node class
     */
    open class Node

    /**
     * Node that represents all the quantifier such as '+' '*' and '?'
     */
    class QuantifierNode(val quantifier: Char) : Node()

    /**
     * This class represents the expression name
     * For example: <int> ::= [0-9]+
     * The expression name will be: int
     *
     * <exp_name> ::= "<" <text> ">"
     */
    class ExpressionNameNode(val name: String) : Node() {
        override fun toString(): String {
            return name
        }
    }

    /**
     * Represents a char sequence.
     * <p>
     *  <statement> ::= "TEL:" <phone>
     * </p>
     * The "TEL:" will be the a char sequence node.
     */
    class CharSequenceNode(val text: String) : Node()

    /**
     * Represents character range.
     * For example: <int> ::= [0-9]+
     * [0-9] will be a character range node.
     *
     * <var_group> ::= "[" <text> "-" <text> "]"
     */
    class CharacterRangeNode(val text: String) : Node()

    /**
     * Represents an expression.
     * <p>
     *  <factor> <quantifier>?
     * </p>
     */
    class ExpressionNode(val factor: Node, val quantifier: Node?) : Node()

    /**
     * Represents expression list.
     * <exp_list> ::= <exp> "|"? <exp_list> | <exp>
     */
    class ExpressionListNode(val expressionList: List<Node>) : Node()

    /**
     * Represents that we have more options.
     * <var> | <exp_name> | <var_group> | "(" <exp_list> ")"
     * Each expression split by '|' will be an option for us.
     */
    class OptionalExpressionListNode(val expressionList: List<Node>) : Node()

    /**
     * <exp_name> "::=" <exp_list>
     */
    class DeclarationNode(val name: Node, val expressionList: Node) : Node()

    /**
     * <declaration> <program> | <declaration>
     */
    class ProgramNode(val declarationList: List<Node>) : Node()
}