package cz.bnf.playground

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExpressionParserTest {
    @Test
    fun testExpressionParser1() {
        val text = """
        <int> ::= [0-9]+
        <factor> ::= <int> | "(" <expr> ")"
        <term> ::= <factor> (("*" | "/" ) <factor>)*
        <expr> ::= <term> (("+" | "-") <term>)*
        """.trimIndent()
        val lexer = Lexer(text)
        val expressionParser = ExpressionParser(lexer)
        val program = expressionParser.program()
        Assertions.assertNotNull(program)
    }

    @Test
    fun testExpressionParser2() {
        val text = """
            <num> ::= [0-9]+
            <factor> ::= "+" | "-" | <num> | "(" <expr> ")" | <var_name>
            <term> ::= <factor> (("+" | "-") <factor>)
            <expr> ::= <term> (("*" | "/" ) <term>)
            <var_name> ::= ([a-z] | [A-Z])+
            <def_type> ::= "INTEGER" | "REAL"
            <var_list> ::= <var_name> "," <var_list> | <var_name>
            <variable_declaration> ::= <var_list> ":" <def_type>
            <assignment_statement> ::= <var_name> ":" <expr>
            <compound_statement> ::= "BEGIN" <statement_list> "END"
            <statement> ::= <assignment_statement> | <compound_statement>
            <statement_list> ::= <statement> ";" <statement_list> | <statement>
            <block> ::= "VAR" (<variable_declaration> ";")+
            <program> ::= "PROGRAM" <var_name> ";" <block>? <compound_statement> "."
        """.trimIndent()
        val lexer = Lexer(text)
        val expressionParser = ExpressionParser(lexer)
        val program = expressionParser.program()
        Assertions.assertNotNull(program)
    }
}