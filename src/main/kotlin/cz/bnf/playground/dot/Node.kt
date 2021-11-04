package cz.bnf.playground.dot

/**
 * The generic node for the Dot.
 * If you want to visualize your tree or graph.
 * You can convert your tree's node to this generic node then use [DotGenerator] to generate the dot file.
 */
class Node(val name: String) {

    companion object {
        private var counter = 0
    }

    /**
     * The unique id for this node
     */
    val id: Int = counter++

    /**
     * The child list
     */
    var children = mutableListOf<Node>()

    fun addChild(node: Node) {
        if (!children.contains(node)) {
            children.add(node)
        }
    }

    override fun toString(): String {
        return name
    }
}