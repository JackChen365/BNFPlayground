package cz.bnf.playground.nfa.exception

/**
 * Unexpected exception.
 * Some unexpected error happened will throw this exception.
 * For instance: We expect next token should be text but get num.
 */
class UnexpectedException(message: String?) : Exception(message)