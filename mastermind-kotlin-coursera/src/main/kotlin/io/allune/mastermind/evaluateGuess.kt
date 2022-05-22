package io.allune.mastermind

import kotlin.math.min


data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    var rightPosition = 0
    var wrongPosition = 0

    // Store the original secret and guess so we can remove the characters processed on each string
    var processedGuess = guess
    var processedSecret = secret

    for ((index, ch) in secret.withIndex()) {
        when (ch) {
            guess[index] -> {
                // If the characters in the same position match, increase rightPosition
                rightPosition++

                // Remove the character in processedGuess as being in the right position
                processedGuess = processedGuess.replaceFirst(ch, '*')

                // Mark the first character in processedStart that matches the current character as in the right position
                if (secret[index] == processedSecret[index]) {
                    processedSecret = processedSecret.replaceFirst(ch, '*')
                } else {
                    // the character was incorrectly counted as being in the wrong position, decrease wrongPosition
                    wrongPosition--
                }
            }
            else -> {
                // Count the number the character appears in the remaining characters of processedGuess and processedSecret
                // and increase wrongPosition by the minimum between the two counts. We may be counting a character in the
                // right position in the wrong position. Needs handling in the character match branch
                wrongPosition += min(processedSecret.count { it == ch }, processedGuess.count { it == ch })
                // Remove the character from processedSecret so that we don't count the same character again as in wrong position
                processedSecret = processedSecret.replace(ch, '*')
            }
        }
    }

    // return the results
    return Evaluation(rightPosition, wrongPosition)
}
