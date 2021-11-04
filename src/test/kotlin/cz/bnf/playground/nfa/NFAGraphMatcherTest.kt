package cz.bnf.playground.nfa

import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class NFAGraphMatcherTest {
    @Test
    fun testNFASearch() {
        val text = """
        <int> ::= [0-9]+
        <factor> ::= <int> | "(" <expr> ")"
        <term> ::= <factor> (("*" | "/" ) <factor>)*
        <expr> ::= <term> (("+" | "-") <term>)*
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val programNode = nodeVisitor.visitNode()
        val subProgram = programNode.getSubProgram("expr")
        val matcher = NFAEngine(subProgram)
        val str =
            "(((1+660)/8313)-((((5*(4*6))/6)-27/25981/9)+6*5-7)*(((26)/2-5+(5414-267)))/((648))/((4)/46)-71)*0+(925)/(66)"
        val pathList = matcher.search(str)
        for (path in pathList) {
            val subStr = str.substring(path.start, path.end)
            println(subStr + " Matcher:" + path.state.matcher)
        }
        val path = pathList.last()
        Assertions.assertEquals(path.end, str.length)
    }

    @Test
    fun testNFASearch2() {
        val text = """
        <num> ::= [0-9]+
        <factor> ::= "+" | "-" | <num> | "(" <expr> ")" | <var_name>
        <term> ::= <factor> (("+" | "-") <factor>)
        <expr> ::= <term> (("*" | "/" ) <term>)
        <var_name> ::= ([a-z] | [A-Z] | "_")+
        <def_type> ::= "INTEGER" | "REAL"
        <var_list> ::= <var_name> "," <var_list> | <var_name>
        <variable_declaration> ::= <var_list> "::=" <def_type>
        <assignment_statement> ::= <var_name> "::=" <expr>
        <compound_statement> ::= "BEGIN" <statement_list> "END"
        <statement> ::= <assignment_statement> | <compound_statement>
        <statement_list> ::= <statement> ";" <statement_list> | <statement>
        <block> ::= "VAR" (<variable_declaration> ";")+
        <program> ::= "PROGRAM" <var_name> ";" <block>? <compound_statement> "."
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val programNode = nodeVisitor.visitNode()
        val subProgram = programNode.getSubProgram("program")
        val matcher = NFAEngine(subProgram)
        val str = """
            BEGIN
              _::=9/8276*23;
              g::=9-60*28*76;
              Y::=++(1*61)/872+(0);
              i::=-258;
              BEGIN
                w::=(4/-(0)+++432)+3;
                Y::=--(69*(58)*-0/+(9689/4))*(1/(66));
                _i::=4-((5/(75)))
              END;
            END.
        """.trimIndent()
        val pathList = matcher.search(str)
        for (path in pathList) {
            val subStr = str.substring(path.start, path.end)
            println(subStr + " Matcher:" + path.state.matcher)
        }
        val path = pathList.last()
        Assertions.assertEquals(path.end, str.length)
    }

    @Test
    fun testNFASearch3() {
        val text = """
        <var_name> ::= ([a-z] | [A-Z] | "_" | [0-9])+
        <var_list> ::= <var_name> "," <var_list> | <var_name>
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val programNode = nodeVisitor.visitNode()
        val subProgram = programNode.getSubProgram("var_list")
        val matcher = NFAEngine(subProgram)
        val str = """
            var_1,var_2,var_3,var_4
        """.trimIndent()
        val pathList = matcher.search(str)
        for (path in pathList) {
            val subStr = str.substring(path.start, path.end)
            println(subStr + " Matcher:" + path.state.matcher)
        }
        val path = pathList.last()
        Assertions.assertEquals(path.end, str.length)
    }

    @Test
    fun testNFASearch4() {
        val text = """
        <var> ::= ([a-z] | [A-Z] | [0-9] | "_" | "-" | ".")+
        <attribute> ::= (<var> ":")? <var> "=" '"' <var> '"'
        <element> ::= "<" <var> <attribute>* "/>" | "<" <var> <attribute>* ">" <var> "</" <var> ">" | "<" <var> <attribute>* ">" <element>+ "</" <var> ">"
        <xml_declaration> ::= "<?" <attribute>+ "?>"
        <doc> ::= <xml_declaration> <element>
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val programNode = nodeVisitor.visitNode()
        val subProgram = programNode.getSubProgram("doc")
        val matcher = NFAEngine(subProgram)
        val str = File("conf/activity_app_layout.xml").readText()
        val pathList = matcher.search(str)
        for (path in pathList) {
            val subStr = str.substring(path.start, path.end)
            println(subStr + " Matcher:" + path.state.matcher)
        }
        val path = pathList.last()
        Assertions.assertEquals(path.end, str.length)
    }
}