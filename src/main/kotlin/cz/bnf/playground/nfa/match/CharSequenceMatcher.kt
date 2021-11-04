package cz.bnf.playground.nfa.match

/**
 * The char sequence matcher is for us to match a specific text.
 */
class CharSequenceMatcher(private val text: CharSequence) : Matcher {
    override fun match(pos: Int, array: CharArray): Int {
        var start = pos
        var offset = pos
        var index = 0
        while (offset < array.size && index < text.length && text[index] == array[offset]) {
            offset++
            index++
        }
        if (offset - start == text.length) {
            return offset - start
        }
        return 0
    }

    override fun toString(): String {
        return text.toString()
    }
}