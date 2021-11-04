package cz.bnf.playground

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExpressionLexerTest {
    @Test
    fun testExpressionLexer() {
        val text = """
        <factor> ::= <int> | "(" <expr> ")"
        <int> ::= [0-9]+
        <term> ::= <factor> (("*" | "/" ) <factor>)*
        <expr> ::= <term> (("+" | "-") <term>)*
        """.trimIndent()
        val lexer = Lexer(text)
        var token = lexer.nextToken()
        while (token.type != EOF) {
            println(token)
            token = lexer.nextToken()
        }
        Assertions.assertEquals(token.type, EOF)
    }
}