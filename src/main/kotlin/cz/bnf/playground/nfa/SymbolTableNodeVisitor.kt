package cz.bnf.playground.nfa

import cz.bnf.playground.ASTNode
import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.nfa.symbol.NFASymbolTable
import cz.bnf.playground.nfa.symbol.Symbol

/**
 * The SymbolTableNodeVisitor is for us to do some preprocess work before we start use the NFA tree.
 * The pre-process work include variable checking and responsible for generate the symbol table.
 *
 * @see NFASymbolTable
 */
class SymbolTableNodeVisitor(private val parser: ExpressionParser) {
    private val symbolTable = NFASymbolTable()
    fun visitNode() {
        val node = parser.program()
        visitNode(node)
    }

    private fun visitNode(node: ASTNode.Node) {
        if (node is ASTNode.DeclarationNode) {
            visitDeclarationNode(node)
        }
        if (node is ASTNode.ExpressionListNode) {
            visitExpressionListNode(node)
        }
        if (node is ASTNode.ExpressionNode) {
            visitExpressionNode(node)
        }
        if (node is ASTNode.OptionalExpressionListNode) {
            visitOptionalExpressionListNode(node)
        }
        if (node is ASTNode.ProgramNode) {
            visitProgramNode(node)
        }
    }

    private fun visitDeclarationNode(node: ASTNode.DeclarationNode) {
        val expressionNameNode = node.name as ASTNode.ExpressionNameNode
        val variableNode = NFANode.SimpleNode(expressionNameNode.name)
        symbolTable.insert(Symbol(expressionNameNode.name, variableNode))
    }

    private fun visitExpressionListNode(node: ASTNode.ExpressionListNode) {
        node.expressionList.forEach { childNode ->
            visitNode(childNode)
        }
    }

    private fun visitExpressionNode(node: ASTNode.ExpressionNode) {
        visitNode(node.factor)
    }

    private fun visitOptionalExpressionListNode(node: ASTNode.OptionalExpressionListNode) {
        node.expressionList.forEach { childNode ->
            visitNode(childNode)
        }
    }

    private fun visitProgramNode(node: ASTNode.ProgramNode) {
        node.declarationList.forEach { childNode ->
            visitNode(childNode)
        }
    }

    fun getSymbolTable(): NFASymbolTable {
        return symbolTable
    }
}