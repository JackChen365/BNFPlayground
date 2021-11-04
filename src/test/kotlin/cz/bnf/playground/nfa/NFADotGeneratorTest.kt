package cz.bnf.playground.nfa

import cz.bnf.playground.dot.DotGenerator
import cz.bnf.playground.nfa.dot.NFADotGenerator
import org.junit.jupiter.api.Test
import java.io.File

class NFADotGeneratorTest {

    @Test
    fun testDotGenerator1() {
        val text = """
        <int> ::= "ab" [0-9]+ "c"
        """.trimIndent()
        val output = File("output/dot_expression1.dot")
        val node2 = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node2, title = "dot_expression1", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator2() {
        val text = """
        <int> ::= "ab" [0-9]* "c"
        """.trimIndent()
        val output = File("output/dot_expression2.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression2", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator3() {
        val text = """
        <int> ::= "ab" [0-9]? "c"
        """.trimIndent()
        val output = File("output/dot_expression3.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression3", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator4() {
        val text = """
        <var_name> ::= ([a-z] | [A-Z] | "_" | [0-9])+
        <var_list> ::= <var_name> "," <var_list> | <var_name>
        """.trimIndent()
        val output = File("output/dot_expression4.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression4", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator5() {
        val text = """
        <int> ::= [0-9]+
        <factor> ::= <int> | "(" <expr> ")"
        <term> ::= <factor> (("*" | "/" ) <factor>)*
        <expr> ::= <term> (("+" | "-") <term>)*
        """.trimIndent()
        val output = File("output/dot_expression5.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression5", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator6() {
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
        val output = File("output/dot_expression6.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression6", DotGenerator.RANK_DIR_LR)
    }

    @Test
    fun testDotGenerator7() {
        val text = """
        <var> ::= ([a-z] | [A-Z] | [0-9] | "_" | "-" | ".")+
        <attribute> ::= (<var> ":")? <var> "=" '"' <var> '"'
        <element> ::= "<" <var> <attribute>* "/>" | "<" <var> <attribute>* ">" <var> "</" <var> ">" | "<" <var> <attribute>* ">" <element>+ "</" <var> ">"
        <xml_declaration> ::= "<?" <attribute>+ "?>"
        <doc> ::= <xml_declaration> <element>
        """.trimIndent()
        val output = File("output/dot_expression7.dot")
        val node = NFADotGenerator().generate(text)
        DotGenerator().generate(output, node, title = "dot_expression7", DotGenerator.RANK_DIR_LR)
    }

}