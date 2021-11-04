package cz.bnf.playground.nfa.match

/**
 * The abstract matcher class.
 *
 * There are some subclasses for the matcher.
 * @see CharacterRangeMatcher
 * @see CharSequenceMatcher
 */
interface Matcher {
    /**
     * @param pos current position for this text.
     * @param c the original char array
     * @return how many word we have consumed.
     */
    fun match(pos: Int, c: CharArray): Int
}