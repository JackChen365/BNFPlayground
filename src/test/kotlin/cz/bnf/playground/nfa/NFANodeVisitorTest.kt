package cz.bnf.playground.nfa

import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @see NFANodeVisitor
 */
class NFANodeVisitorTest {
    @Test
    fun testNFANodeVisitor() {
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
        val visitNode = nodeVisitor.visitNode()
        Assertions.assertNotNull(visitNode)
    }

    @Test
    fun testNFAQuantifier1() {
        val text = """
        <int> ::= [0-9]+
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val visitNode = nodeVisitor.visitNode()
        Assertions.assertNotNull(visitNode)
    }

    @Test
    fun testNFAQuantifier2() {
        val text = """
        <int> ::= [0-9]*
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val visitNode = nodeVisitor.visitNode()
        Assertions.assertNotNull(visitNode)
    }

    @Test
    fun testNFAQuantifier3() {
        val text = """
        <int> ::= [0-9]?
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val visitNode = nodeVisitor.visitNode()
        Assertions.assertNotNull(visitNode)
    }


}