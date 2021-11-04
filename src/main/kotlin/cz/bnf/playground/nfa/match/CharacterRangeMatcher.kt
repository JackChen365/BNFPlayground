package cz.bnf.playground.nfa.match

/**
 * This class is for us to match the text within a group.
 * For example:
 * the expression:[0-9] will be resolved as a character range that start from the first element and end withs the last element.
 * Therefore, this matcher will be match all the number inside the range.
 */
class CharacterRangeMatcher(group: CharSequence) : Matcher {
    private val first = group.first()
    private val last = group.last()
    override fun match(pos: Int, array: CharArray): Int {
        var start = pos
        var offset = pos
        while (offset < array.size && array[offset] in first..last) {
            offset++
        }
        return offset - start
    }

    override fun toString(): String {
        return "[$first-$last]"
    }
}