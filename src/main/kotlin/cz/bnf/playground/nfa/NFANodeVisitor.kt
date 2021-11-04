package cz.bnf.playground.nfa

import cz.bnf.playground.ASTNode
import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.nfa.symbol.NFASymbolTable
import java.rmi.UnexpectedException

/**
 * This class responsible for us to convert the AST tree into NFA tree.
 * The AST tree is generate from the expression.
 * <p>
 *     <factor> ::= <int> | "(" <expr> ")"
 *     <int> ::= [0-9]+
 *     <term> ::= <factor> (("*" | "/" ) <factor>)*
 *     <expr> ::= <term> (("+" | "-") <term>)*
 * </p>
 */
class NFANodeVisitor(
    private val symbolTable: NFASymbolTable,
    private val parser: ExpressionParser
) {
    companion object {
        private var index = 0
    }

    fun visitNode(): NFANode.ProgramNode {
        val node = parser.program()
        val endNode = symbolTable.requireSymbol("END")
        val programNode = NFANode.ProgramNode(NFANode.SimpleNode("program"), endNode.node)
        visitNode(programNode.start, node)
        return programNode
    }

    private fun visitNode(parent: NFANode.Node, node: ASTNode.Node): NFANode.Node {
        if (node is ASTNode.DeclarationNode) {
            return visitDeclarationNode(parent, node)
        }
        if (node is ASTNode.ExpressionNameNode) {
            return visitExpressionName(parent, node)
        }
        if (node is ASTNode.ExpressionNode) {
            return visitExpressionNode(parent, node)
        }
        if (node is ASTNode.CharacterRangeNode) {
            return visitCharacterRangeNode(parent, node)
        }
        if (node is ASTNode.CharSequenceNode) {
            return visitCharSequenceNode(parent, node)
        }
        if (node is ASTNode.ExpressionListNode) {
            return visitExpressionListNode(parent, node)
        }
        if (node is ASTNode.OptionalExpressionListNode) {
            return visitOptionalExpressionListNode(parent, node)
        }
        if (node is ASTNode.ProgramNode) {
            return visitProgramNode(parent, node)
        }
        throw UnexpectedException("Unexpected node.")
    }

    private fun visitCharSequenceNode(parent: NFANode.Node, node: ASTNode.CharSequenceNode): NFANode.Node {
        var newNode: NFANode.Node = NFANode.CharSequenceNode("text<${node.text}>", node.text)
        parent.addTransition(newNode)
        return newNode
    }

    private fun visitExpressionName(parent: NFANode.Node, node: ASTNode.ExpressionNameNode): NFANode.Node {
        val declarationSymbol = symbolTable.requireSymbol(node.name)
        //Make sure it won't be a circle.
        val declarationNode = NFANode.DeclarationNode(node.name, declarationSymbol.node)
        parent.addTransition(declarationNode)
        return declarationNode
    }

    private fun visitCharacterRangeNode(parent: NFANode.Node, node: ASTNode.CharacterRangeNode): NFANode.Node {
        var newNode: NFANode.Node = NFANode.CharacterRangeNode("range<${node.text}>", node.text)
        parent.addTransition(newNode)
        return newNode
    }

    private fun visitDeclarationNode(parent: NFANode.Node, node: ASTNode.DeclarationNode): NFANode.Node {
        val declarationName = node.name.toString()
        val declarationSymbol = symbolTable.requireSymbol(declarationName)
        //Make sure it won't be a circle.
        val declarationNode = declarationSymbol.node
        parent.addTransition(declarationNode)
        val newNode = visitNode(declarationNode, node.expressionList)
        val endSymbol = symbolTable.requireSymbol("END")
        newNode.addTransition(endSymbol.node)
        return newNode
    }

    private fun visitExpressionNode(parent: NFANode.Node, node: ASTNode.ExpressionNode): NFANode.Node {
        val epsilonNode = NFANode.SimpleNode("ε-${index++}")
        parent.addTransition(epsilonNode)
        val newNode = visitNode(epsilonNode, node.factor)
        if (null != node.quantifier && node.quantifier is ASTNode.QuantifierNode) {
            return addQuantifierForNode(parent, epsilonNode, newNode, node.quantifier)
        }
        return newNode
    }

    /**
     * Add quantifier for the node.
     * The quantifiers here are: "+"/"*"/"?"
     */
    private fun addQuantifierForNode(
        parent: NFANode.Node,
        epsilonNode: NFANode.Node,
        node: NFANode.Node,
        quantifierNode: ASTNode.QuantifierNode
    ): NFANode.Node {
        val nextEpsilonNode = NFANode.SimpleNode("ε-${index++}")
        return when (quantifierNode.quantifier) {
            '+' -> {
                node.addTransition(nextEpsilonNode)
                nextEpsilonNode.addTransition(epsilonNode)
                nextEpsilonNode
            }
            '*' -> {
                parent.addTransition(nextEpsilonNode)
                node.addTransition(nextEpsilonNode)
                nextEpsilonNode.addTransition(epsilonNode)
                nextEpsilonNode
            }
            '?' -> {
                parent.addTransition(nextEpsilonNode)
                node.addTransition(nextEpsilonNode)
                nextEpsilonNode
            }
            else -> throw UnexpectedException("Unexpected node.")
        }
    }

    private fun visitExpressionListNode(parent: NFANode.Node, node: ASTNode.ExpressionListNode): NFANode.Node {
        var nextNode = parent
        node.expressionList.forEach { childNode ->
            nextNode = visitNode(nextNode, childNode)
        }
        return nextNode
    }

    private fun visitOptionalExpressionListNode(
        parent: NFANode.Node, node: ASTNode.OptionalExpressionListNode
    ): NFANode.Node {
        val nodeList = mutableListOf<NFANode.Node>()
        node.expressionList.forEach { childNode ->
            val newNode = visitNode(parent, childNode)
            nodeList.add(newNode)
        }
        val epsilonNode = NFANode.SimpleNode("ε-${index++}")
        nodeList.forEach { child ->
            child.addTransition(epsilonNode)
        }
        return epsilonNode
    }

    private fun visitProgramNode(parent: NFANode.Node, node: ASTNode.ProgramNode): NFANode.Node {
        node.declarationList.forEach { childNode ->
            visitNode(parent, childNode)
        }
        return parent
    }
}