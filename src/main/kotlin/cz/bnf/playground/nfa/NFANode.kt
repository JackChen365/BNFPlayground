package cz.bnf.playground.nfa

import cz.bnf.playground.nfa.match.Matcher
import cz.bnf.playground.nfa.match.CharSequenceMatcher
import cz.bnf.playground.nfa.match.CharacterRangeMatcher
import java.lang.RuntimeException

/**
 * All the NFA nodes.
 */
sealed class NFANode {
    /**
     * This is a special class to represent a program.
     * Usually a syntax list will generate one program.
     * The program include two nodes. the start node and end node.
     */
    class ProgramNode(val start: Node, val end: Node) {
        fun getSubProgram(name: String): ProgramNode {
            val findNode =
                start.transitions.find { it.name == name } ?: throw RuntimeException("Not found the node:$name")
            return ProgramNode(findNode, end)
        }

        fun getDeclarationNode(name: String): Node {
            return start.transitions.find { it.name == name } ?: throw RuntimeException("Not found the node:$name")
        }
    }

    abstract class Node(val name: String) {
        val transitions = mutableListOf<Node>()
        abstract var matcher: Matcher?

        fun addTransition(node: Node) {
            transitions.add(node)
        }
    }

    /**
     * The simple node. This node does not have things to do.
     * Simply represent a node.
     */
    class SimpleNode(name: String) : Node(name) {
        override var matcher: Matcher? = null
    }

    /**
     * The declaration node represent a declaration.
     * <p>
     *     <factor> ::= <int> | "(" <expr> ")"
     *     <int> ::= [0-9]+
     * </p>
     * The <int> and <factor> are called a declaration.
     * We are trying to avoid cycle when we visualize it.
     */
    class DeclarationNode(name: String, val ref: Node) : Node(name) {
        override var matcher: Matcher? = null
    }

    class CharacterRangeNode(name: String, text: CharSequence) : Node(name) {
        override var matcher: Matcher? = CharacterRangeMatcher(text)
    }

    class CharSequenceNode(name: String, text: CharSequence) : Node(name) {
        override var matcher: Matcher? = CharSequenceMatcher(text)
    }
}