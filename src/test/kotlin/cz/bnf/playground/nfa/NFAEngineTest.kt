package cz.bnf.playground.nfa

import cz.bnf.playground.ExpressionParser
import cz.bnf.playground.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Since I have changed how we match the text.
 * Once the engine could find a path it will always return true.
 * Never mind, This is not Regular expression.
 */
class NFAEngineTest {
    @Test
    fun testMatcher1() {
        val text = """
        <int> ::= [0-9]+
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        val nfaEngine = NFAEngine(node)
        Assertions.assertTrue(nfaEngine.match("1234"))
    }

    @Test
    fun testMatcher2() {
        val text = """
        <int> ::= [0-9]*
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        val nfaEngine = NFAEngine(node)
        Assertions.assertTrue(nfaEngine.match("1234"))
    }

    @Test
    fun testMatcher3() {
        val text = """
        <int> ::= [0-9]*
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        val nfaEngine = NFAEngine(node)
        Assertions.assertTrue(nfaEngine.match("1"))
        Assertions.assertTrue(nfaEngine.match(""))
    }

    @Test
    fun testMatcher4() {
        val text = """
        <int> ::= "ab" [0-9]* "c"
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        val nfaEngine = NFAEngine(node)
        Assertions.assertTrue(nfaEngine.match("ab12c"))
    }

    @Test
    fun testMatcher5() {
        val text = """
        <int> ::= ("ab"|"bc")* [0-9]+ "c"
        """.trimIndent()
        //Initial the NFA symbol table.
        val symbolTableNodeVisitor = SymbolTableNodeVisitor(ExpressionParser(Lexer(text)))
        symbolTableNodeVisitor.visitNode()
        val symbolTable = symbolTableNodeVisitor.getSymbolTable()

        val nodeVisitor = NFANodeVisitor(symbolTable, ExpressionParser(Lexer(text)))
        val node = nodeVisitor.visitNode()
        val nfaEngine = NFAEngine(node)
        Assertions.assertTrue(nfaEngine.match("abbcc12c"))
    }
}