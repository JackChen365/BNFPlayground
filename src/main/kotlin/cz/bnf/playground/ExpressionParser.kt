package cz.bnf.playground

import cz.bnf.playground.nfa.exception.UnexpectedException
import java.lang.Exception

/**
 * Here is the syntax list for the BNF
 * <p>
 *  <text> ::= ([a-z] | [0-9] | "*" | "/" | "+" | "-" | "(" | ")")+
 *  <quantifier> ::= "?" | "+" | "*"
 *  <exp_name> ::= "<" <text> ">"
 *  <var> ::= "\"" <text> "\""
 *  <var_group> ::= "[" <text> "-" <text> "]"
 *  <factor> ::= <var> | <exp_name> | <var_group> | "(" <exp_list> ")"
 *  <exp> ::= <factor> <quantifier>?
 *  <exp_list> ::= <exp> "|"? <exp_list> | <exp>
 *  <declaration> ::= <exp_name> "::=" <exp_list>
 *  <program> ::= <declaration> <program> | <declaration>
 * </p>
 *
 * This class works with [Lexer] The lexer helps us split the text into token.
 * We use parser to generate the AST (Abstract syntax tree)
 * The parse method always follow the syntax list.
 * For example
 * <exp_name> ::= "<" <text> ">" corresponding to the function: [expressionName]
 * <quantifier> ::= "?" | "+" | "*" corresponding to the function: [quantifier]
 */
class ExpressionParser(private var lexer: Lexer) {
    private var token = lexer.nextToken()

    private fun unexpectedError(): Exception {
        throw UnexpectedException("Unexpected token. at:${token}")
    }

    private fun consume(type: String) {
        if (type != token.type) {
            throw unexpectedError()
        }
        token = lexer.nextToken()
    }

    //<quantifier> ::= "?" | "+" | "*"
    private fun quantifier(): ASTNode.QuantifierNode {
        if (token.type == ZERO_OR_ONE ||
            token.type == ZERO_OR_MORE ||
            token.type == ONE_OR_MORE
        ) {
            val quantifier = token.value<Char>()
            consume(token.type)
            return ASTNode.QuantifierNode(quantifier)
        }
        throw unexpectedError()
    }

    //<exp_name> ::= "<" <text> ">"
    private fun expressionName(): ASTNode.Node {
        if (token.type == VAR_SYMBOL) {
            val text = token.value<CharSequence>()
            consume(VAR_SYMBOL)
            return ASTNode.ExpressionNameNode(text.toString())
        }
        throw unexpectedError()
    }

    //<var> ::= "\"" <text> "\""
    private fun variable(): ASTNode.Node {
        if (token.type == TEXT_SYMBOL) {
            val text = token.value<CharSequence>()
            consume(TEXT_SYMBOL)
            return ASTNode.CharSequenceNode(text.toString())
        }
        throw unexpectedError()
    }

    //<var_group> ::= "[" <text> "-" <text> "]"
    private fun variableGroup(): ASTNode.Node {
        if (token.type == GROUP_SYMBOL) {
            val value = token.value<CharSequence>()
            consume(GROUP_SYMBOL)
            return ASTNode.CharacterRangeNode(value.toString())
        }
        throw unexpectedError()
    }

    //<factor> ::= <var> | <exp_name> | <var_group> | "(" <exp_list> ")"
    private fun factor(): ASTNode.Node {
        when (token.type) {
            L_PAREN -> {
                //"(" <exp_list> ")"
                consume(L_PAREN)
                val expressionList = expressionList()
                consume(R_PAREN)
                return expressionList
            }
            TEXT_SYMBOL -> {
                //<var> ::= "\"" <text> "\""
                return variable()
            }
            GROUP_SYMBOL -> {
                //<var_group> ::= "[" <text> "-" <text> "]"
                return variableGroup()
            }
            VAR_SYMBOL -> {
                //<exp_name> ::= "<" <text> ">"
                return expressionName()
            }
            else -> throw unexpectedError()
        }
    }

    //<exp> ::= <factor> <quantifier>?
    private fun expression(): ASTNode.Node {
        val factor = factor()
        var quantifier: ASTNode.Node? = null
        if (token.type == ZERO_OR_ONE ||
            token.type == ZERO_OR_MORE ||
            token.type == ONE_OR_MORE
        ) {
            quantifier = quantifier()
        }
        return ASTNode.ExpressionNode(factor, quantifier)
    }

    //<exp_list> ::= <exp> "|"? <exp_list> | <exp>
    private fun expressionList(): ASTNode.Node {
        var expressionNodeList = mutableListOf<ASTNode.Node>()
        var expressionList = mutableListOf<ASTNode.Node>()
        expressionList.add(expression())
        while (token.type == OR || token.type == TEXT_SYMBOL || token.type == L_PAREN || token.type == GROUP_SYMBOL || token.type == VAR_SYMBOL) {
            if (token.type == OR) {
                consume(OR)
                expressionNodeList.add(ASTNode.ExpressionListNode(expressionList))
                expressionList = mutableListOf()
            }
            expressionList.add(expression())
        }
        if (expressionList.isNotEmpty()) {
            expressionNodeList.add(ASTNode.ExpressionListNode(expressionList))
        }
        return ASTNode.OptionalExpressionListNode(expressionNodeList)
    }

    //<declaration> ::= <exp_name> "::=" <exp_list> (\n|eof)
    private fun declaration(): ASTNode.Node {
        val expressionName = expressionName()
        consume(ASSIGN_SYMBOL)
        val expressionList = expressionList()
        if (EOF != token.type || BREAK_LINE == token.type) {
            consume(token.type)
        }
        return ASTNode.DeclarationNode(expressionName, expressionList)
    }

    //<program> ::= <declaration> <program> | <declaration>
    fun program(): ASTNode.Node {
        val declarationList = mutableListOf<ASTNode.Node>()
        declarationList.add(declaration())
        while (token.type == VAR_SYMBOL) {
            declarationList.add(declaration())
        }
        return ASTNode.ProgramNode(declarationList)
    }
}
