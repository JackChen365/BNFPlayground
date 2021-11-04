package cz.bnf.playground.nfa.dot

import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.Lexer
import cz.bnf.playground.dot.Node
import cz.bnf.playground.nfa.NFANode
import cz.bnf.playground.nfa.NFANodeVisitor
import cz.bnf.playground.nfa.SymbolTableNodeVisitor
import java.util.*

/**
 * The NFA dot generator
 * This class is responsible for us to convert the [NFANode] to [Node] so that we are able to visualize it.
 * First we will use [NFANodeVisitor] to get the AST tree and then clone the AST tree and return a NFA tree.
 * @see generate generate a NFA tree for us
 *
 * There are some testcase in [NFADotGeneratorTest] class
 */
class NFADotGenerator {
    /**
     * Generate a NFA tree by using the given text
     * @return The NFA root node
     */
    fun generate(text: CharSequence): Node {
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()
        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        return cloneGraph(node.start)
    }

    /**
     * Clone the AST graph to a NFA tree.
     */
    private fun cloneGraph(node: NFANode.Node): Node {
        val startNode = Node(node.name)
        val visited = mutableMapOf(node to startNode)
        val queue = LinkedList<NFANode.Node>()
        queue.add(node)
        while (!queue.isEmpty()) {
            val current = queue.removeFirst()
            val mappingNode = visited[current]
            current.transitions.forEach { transitionNode ->
                var childNode = visited[transitionNode]
                if (childNode == null) {
                    queue.add(transitionNode)
                    childNode = Node(transitionNode.name)
                    visited[transitionNode] = childNode
                }
                mappingNode?.addChild(childNode)
            }
        }
        return startNode
    }

}