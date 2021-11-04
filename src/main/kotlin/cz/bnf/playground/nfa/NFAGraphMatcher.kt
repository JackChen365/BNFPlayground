package cz.bnf.playground.nfa

/**
 * This is a NFA engine.
 *
 * @see match Check whether the text is fully match the graph.
 * @see search Search the graph and all the matched paths.
 */
class NFAEngine(private val program: NFANode.ProgramNode) {

    /**
     * Check whether the text is fully match the graph.
     * @return true means the text fully matched.
     */
    fun match(text: String): Boolean {
        val start = program.start
        val end = program.end
        var index = 0
        val charArray = text.toCharArray()
        val pathList = mutableListOf<Path>()
        val stateList = mutableListOf<NFANode.Node>()
        val visited = mutableListOf<NFANode.Node>()
        val consume =
            addTransitionState(visited, stateList, pathList, start, end, charArray, index)
        return consume == text.length
    }

    fun search(text: String): List<Path> {
        val start = program.start
        val end = program.end
        var index = 0
        val charArray = text.toCharArray()
        val pathList = mutableListOf<Path>()
        val stateList = mutableListOf<NFANode.Node>()
        val visited = mutableListOf<NFANode.Node>()
        for (node in start.transitions) {
            if (!visited.contains(node)) {
                visited.add(node)
                val consume =
                    addTransitionState(visited, stateList, pathList, node, end, charArray, index)
                println("Consume:$consume")
            }
        }
        return pathList
    }

    /**
     * Skip some invalid symbol such as space or line-break.
     */
    private fun skipInvalidSymbol(charArray: CharArray, start: Int): Int {
        if (start >= charArray.size) return 0
        var index = start
        var char = charArray[index]
        while (null != char && (char == '\n' || char == ' ' || char == '\t')) {
            index++
            char = charArray[index]
        }
        return index - start
    }

    private fun addTransitionState(
        visited: MutableList<NFANode.Node>, stateList: MutableList<NFANode.Node>,
        pathList: MutableList<Path>, node: NFANode.Node, end: NFANode.Node, charArray: CharArray, index: Int
    ): Int {
        val matcher = node.matcher
        var consume = skipInvalidSymbol(charArray, index)
        if (null != matcher) {
            consume += matcher.match(index, charArray)
            if (0 < consume) {
                visited.clear()
                pathList.add(Path(index, index + consume, node))
            }
        }
        if (node is NFANode.DeclarationNode) {
            consume += addTransitionState(visited, stateList, pathList, node.ref, end, charArray, index + consume)
        }
        node.transitions.forEach { child ->
            if (!visited.contains(child)) {
                visited.add(child)
                consume += addTransitionState(visited, stateList, pathList, child, end, charArray, index + consume)
            }
        }
        return consume
    }

    class Path(
        var start: Int,
        var end: Int,
        var state: NFANode.Node
    ) {
        override fun toString(): String {
            return "$start..$end ${state.matcher}"
        }
    }


}