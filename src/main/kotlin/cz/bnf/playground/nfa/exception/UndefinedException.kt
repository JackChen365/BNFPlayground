package cz.bnf.playground.nfa.exception

import java.lang.Exception

/**
 * Undefined exception this means some variable undefined.
 */
class UndefinedException(message: String?) : Exception(message)