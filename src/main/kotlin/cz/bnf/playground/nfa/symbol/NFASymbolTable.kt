package cz.bnf.playground.nfa.symbol

import cz.bnf.playground.nfa.NFANode
import cz.bnf.playground.nfa.SymbolTableNodeVisitor
import cz.bnf.playground.nfa.exception.UndefinedException
import java.lang.NullPointerException

/**
 * The NFA symbol table
 * Before we have turn the text into an AST tree. We need to know whether the variable has defined.
 * <p>
 *     <factor> ::= <int> | "(" <expr> ")"
 *     <int> ::= [0-9]+
 *     <term> ::= <factor> (("*" | "/" ) <factor>)*
 *     <expr> ::= <term> (("+" | "-") <term>)*
 * </p>
 * For example in the above syntax list. Before we start use the variable: <int> we should know if the variable is exist.
 *
 * @see [SymbolTableNodeVisitor]
 */
class NFASymbolTable {
    private val symbolTable = mutableMapOf<String, Symbol>()

    init {
        initialBuildInSymbol()
    }

    private fun initialBuildInSymbol() {
        insert(Symbol("END", NFANode.SimpleNode("END")))
    }

    fun insert(symbol: Symbol) {
        symbolTable[symbol.name] = symbol
    }

    fun requireSymbol(name: String): Symbol {
        return lookup(name) ?: throw UndefinedException("The symbol is null.")
    }

    fun lookup(name: String): Symbol? {
        val symbol = symbolTable[name]
        if (null != symbol) {
            return symbol
        }
        return null
    }
}