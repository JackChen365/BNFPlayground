package cz.bnf.playground.nfa

import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SymbolTableNodeVisitorTest {

    /**
     * Test the symbol table.
     * After visit all the nodes we will generate a symbol table. So that we are able to lookup the defined node from it.
     */
    @Test
    fun testSymbolTableNodeVisitor() {
        val text = """
        <int> ::= [0-9]+
        <factor> ::= <int> | "(" <expr> ")"
        <term> ::= <factor> (("*" | "/" ) <factor>)*
        <expr> ::= <term> (("+" | "-") <term>)*
        """.trimIndent()
        val expressionParser = ExpressionParser(Lexer(text))
        val nodeVisitor = SymbolTableNodeVisitor(expressionParser)
        nodeVisitor.visitNode()
        val symbolTable = nodeVisitor.getSymbolTable()
        //Should be five elements including one build-in type.
        Assertions.assertNotNull(symbolTable)
    }
}