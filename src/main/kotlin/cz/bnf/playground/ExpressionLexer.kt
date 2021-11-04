package cz.bnf.playground

import cz.bnf.playground.nfa.exception.UnexpectedException

const val VAR_SYMBOL = "VAR"
const val TEXT_SYMBOL = "TEXT"
const val ASSIGN_SYMBOL = "::="
const val GROUP_SYMBOL = "[]"
const val BREAK_LINE = "\n"
const val OR = "|"
const val L_PAREN = "("
const val R_PAREN = ")"
const val ZERO_OR_MORE = "*"
const val ZERO_OR_ONE = "?"
const val ONE_OR_MORE = "+"
const val EOF = "EOF"

/**
 * The BNF expression token.
 */
open class Token(val type: String, private val value: Any) {
    fun <T> value(): T {
        return value as T
    }

    override fun toString(): String {
        return "Token: <$type>"
    }
}

/**
 * The BNF lexer or tokenizer.
 * It helps us to split the text into the token.
 */
class Lexer(private val text: CharSequence) {
    private var pos: Int = 0
    private fun skipWhiteSpace() {
        if (isEndOfFile()) return
        var char = getChar()
        while (null != char && (char == ' ' || char == '\t')) {
            advance()
            char = getChar()
        }
    }

    private fun isEndOfFile(): Boolean {
        return pos >= text.length
    }

    private fun advance(offset: Int = 1) {
        if (isEndOfFile()) return
        pos += offset
    }

    private fun getChar(): Char? {
        if (isEndOfFile()) return null
        return text[pos]
    }

    private fun peekChar(offset: Int = 1): Char {
        return text[pos + offset]
    }

    private fun getText(exclude: Char): CharSequence {
        val start = pos
        var char = getChar()
        while (null != char && exclude != char) {
            advance()
            char = getChar()
        }
        return text.subSequence(start, pos)

    }

    fun nextToken(): Token {
        skipWhiteSpace()
        if (isEndOfFile()) return Token(EOF, EOF)
        val char = getChar()
        while (null != char) {
            if (char == '|') {
                advance()
                return Token(OR, OR)
            }
            if (char == '(') {
                advance()
                return Token(L_PAREN, L_PAREN)
            }
            if (char == ')') {
                advance()
                return Token(R_PAREN, R_PAREN)
            }
            if (char == '<') {
                advance()
                val text = getText('>')
                val token = Token(VAR_SYMBOL, text)
                advance()
                return token
            }
            if (char == '\n') {
                advance()
                return Token(BREAK_LINE, BREAK_LINE)
            }
            if (char == '\"') {
                advance()
                val text = getText('\"')
                val token = Token(TEXT_SYMBOL, text)
                advance()
                return token
            }
            if (char == '\'') {
                advance()
                val text = getText('\'')
                val token = Token(TEXT_SYMBOL, text)
                advance()
                return token
            }
            if (char == '[') {
                advance()
                val text = getText(']')
                val token = Token(GROUP_SYMBOL, text)
                advance()
                return token
            }
            if (char == '?' || char == '*' || char == '+') {
                advance()
                if (char == '?')
                    return Token(ZERO_OR_ONE, char)
                if (char == '*')
                    return Token(ZERO_OR_MORE, char)
                if (char == '+')
                    return Token(ONE_OR_MORE, char)
            }
            if (char == ':' && peekChar(1) == ':' && peekChar(2) == '=') {
                advance(ASSIGN_SYMBOL.length)
                return Token(ASSIGN_SYMBOL, ASSIGN_SYMBOL)
            }
            break
        }
        throw UnexpectedException("Unexpected token.")
    }
}