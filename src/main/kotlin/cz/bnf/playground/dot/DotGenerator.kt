package cz.bnf.playground.dot

import cz.bnf.playground.nfa.dot.NFADotGenerator
import java.io.File

/**
 * Output the graph into a .dot file in order to visualize the graph.
 * We use the the dot engine to visualize all the graph.
 * Visit this website to see how your dot file looks like.
 * https://dreampuf.github.io/GraphvizOnline/
 *
 * This class usually cooperate with the class [NFADotGenerator]
 * The [NFADotGenerator] convert the AST tree to NFA tree, then we use this class to generate the dot file.
 *
 */
class DotGenerator {
    companion object {
        /**
         * Display horizontally
         */
        const val RANK_DIR_LR = "LR"

        /**
         * Display vertically
         */
        const val RANK_DIR_TB = "TB"
    }

    /**
     * @param output The output dot file.
     * @param node The root node for the graph or tree
     * @param title The title of the graph or tree
     * @param rankdir This param determine the graph displayed horizontally or vertically.
     * [RANK_DIR_TB] means top to bottom
     * [RANK_DIR_LR] means left to right
     */
    fun generate(dest: File, node: Node, title: String? = null, rankdir: String = RANK_DIR_TB) {
        val output = StringBuilder(
            """
            digraph astgraph {
              node [fontsize=36, height=1];rankdir = "$rankdir";
            """.trimIndent()
        )
        if (null != title) {
            output.append("labelloc = \"t\";label = \"$title\";")
        }
        output.append("\n")
        traversalNode(output, node)
        output.append("}")
        dest.writeText(output.toString())
    }

    /**
     * node1 [label="expr"]
     * node2 [label="term"]
     */
    private fun traversalNode(output: StringBuilder, node: Node?) {
        if (null == node) {
            return
        }
        val visited = mutableListOf<String>()
        val deque = mutableListOf<Node?>()
        deque.add(node)
        while (deque.isNotEmpty()) {
            val size = deque.size
            for (i in 0 until size) {
                var curr = deque.removeFirst()
                if (null != curr) {
                    if (curr.name == "text<\">") {
                        output.append("\tnode${curr?.id} [label = \"text<'>\"]\n")
                    } else {
                        output.append("\tnode${curr?.id} [label = \"${curr.name}\"]\n")
                    }
                    curr.children.forEach { child ->
                        val connectString = "\tnode${curr.id} -> node${child.id}\n"
                        if (!visited.contains(connectString)) {
                            deque.add(child)
                            visited.add(connectString)
                            output.append(connectString)
                        }
                    }
                }
            }
        }
    }
}