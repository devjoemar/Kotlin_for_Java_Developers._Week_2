package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

/**
 * Evaluates a guess in the Mastermind game against a secret code.
 *
 * This function implements a linear approach to count the number of characters in the guess that match
 * the secret code exactly (rightPosition) and those that are correct in value but placed in the wrong
 * position (wrongPosition).
 *
 * Algorithm Explanation:
 * 1. [countRightPositions] iterates over the strings to count characters that are correct in both value and position.
 *    It also decrements the frequency of these characters in the secret's frequency map to prevent double-counting.
 * 2. [countWrongPositions] counts characters that are correct in value but misplaced, considering the frequency
 *    of each character after accounting for correctly positioned characters. This ensures each character is only
 *    counted once, either as a right position or a wrong position, but not both.
 *
 * Double Counting Avoidance:
 * - Double counting in this context means counting a character more than once towards either right or wrong positions.
 * - This is avoided by decrementing the frequency in the secret's frequency map once a character is counted in the right position.
 * - The subsequent count for wrong positions respects this updated frequency, ensuring accurate counting.
 *
 * Time Complexity:
 * - O(N), where N is the length of the strings. The strings are iterated over linearly to count right and wrong positions.
 *
 * Space Complexity:
 * - O(1), as the space used does not scale with the input size. The frequency maps for characters are limited
 *   to the fixed range (A-F), making the space requirement constant.
 *
 * Example:
 * - Secret: "ABCD", Guess: "ABCF" yields rightPosition = 3, wrongPosition = 0.
 * - Secret: "AABC", Guess: "ADFE" yields rightPosition = 1, wrongPosition = 0.
 *
 * @param secret The secret code to be guessed, composed of characters.
 * @param guess The player's guess, to be evaluated against the secret.
 * @return Evaluation result indicating the number of right and wrong positioned characters.
 */
fun evaluateGuess(secret: String, guess: String): Evaluation {
    val secretFrequency = secret.groupingBy { it }.eachCount().toMutableMap()
    val guessedFrequency = mutableMapOf<Char, Int>()

    val rightPositions = countRightPositions(secret, guess, secretFrequency)
    val wrongPositions = countWrongPositions(secret, guess, secretFrequency, guessedFrequency)

    return Evaluation(rightPositions, wrongPositions)
}

/**
 * Counts the number of characters in the guess that are correctly positioned in the Mastermind game.
 *
 * This method is responsible for identifying the characters in the guess that are exactly in the same position
 * as in the secret code. For each character in the guess, it compares the character at the same index in the
 * secret code. If they match, it signifies that the character is not only correct but also in the correct position.
 *
 * Upon finding a correctly positioned character, two actions are performed:
 * 1. Increment the `rightPositionCount` which keeps track of the number of correctly positioned characters.
 * 2. Decrement the frequency of this character in the `secretFrequency` map.
 *
 * The decrement of the frequency is crucial for the following reasons:
 * - It prevents the double counting of characters. For instance, if the secret code has a character 'A' only once,
 *   but the guess has 'A' multiple times in the correct position, we should count 'A' only once as a right position.
 * - The `?: 0` part is a safety check. It ensures that if, for any reason, the character's frequency becomes null
 *   (which theoretically should not happen in this context), it defaults to 0. This avoids potential `NullPointerException`.
 * - Reducing the frequency after counting a right position ensures accuracy when counting wrong positions later.
 *   It reflects that this instance of the character has already been accounted for in the right positions and
 *   should not be considered again for the wrong positions.
 *
 * @param secret The secret code string.
 * @param guess The guess string.
 * @param secretFrequency A mutable map tracking the frequency of each character in the secret.
 * @return The number of characters in the guess that are correctly positioned.
 */
fun countRightPositions(secret: String, guess: String, secretFrequency: MutableMap<Char, Int>): Int {
    var rightPositionCount = 0
    for (i in secret.indices) {
        if (secret[i] == guess[i]) {
            rightPositionCount++
            secretFrequency[secret[i]] = secretFrequency[secret[i]]?.minus(1) ?: 0
        }
    }
    return rightPositionCount
}

/**
 * Counts the number of characters in the guess that are correctly valued but wrongly positioned.
 *
 * This method is essential for identifying characters in the guess that are present in the secret code
 * but not in the correct position. It complements the [countRightPositions] method by focusing on the
 * characters that are correct in value but misplaced.
 *
 * The logic proceeds as follows:
 * 1. Iterate over each character in the guess and compare it with the corresponding character in the secret.
 * 2. If a character at a given position does not match the character at the same position in the secret
 *    (i.e., it is misplaced), its frequency in the guessed string is updated in `guessedFrequency`.
 * 3. The crucial condition `guessedFrequency[guessedChar]!! <= secretFrequency.getOrDefault(guessedChar, 0)`
 *    checks whether the current frequency of the misplaced character in the guess is less than or equal to
 *    its frequency in the secret (after accounting for right positions). This ensures that each character is
 *    counted appropriately based on its occurrence in both the guess and the secret.
 *    - The `!!` operator is used to assert that `guessedFrequency[guessedChar]` is non-null. Since we initialize
 *      and update this frequency, it should not be null when accessed.
 *    - The `getOrDefault(guessedChar, 0)` ensures that if a character from the guess is not found in the secret,
 *      it defaults to 0, preventing false counting of wrong positions.
 * 4. For every character in the guess that meets the above condition, `wrongPositionCount` is incremented, signifying
 *    a character that is correct in value but misplaced.
 *
 * This method, together with [countRightPositions], provides a complete evaluation of the guess against the secret code,
 * adhering to the rules of the Mastermind game.
 *
 * @param secret The secret code string.
 * @param guess The guess string.
 * @param secretFrequency A map tracking the frequency of each character in the secret after accounting for right positions.
 * @param guessedFrequency A mutable map tracking the frequency of each guessed character.
 * @return The number of characters in the guess that are correct in value but wrongly positioned.
 */
fun countWrongPositions(secret: String, guess: String, secretFrequency: Map<Char, Int>, guessedFrequency: MutableMap<Char, Int>): Int {
    var wrongPositionCount = 0
    for (i in secret.indices) {
        val guessedChar = guess[i]
        if (secret[i] != guessedChar) {
            guessedFrequency[guessedChar] = guessedFrequency.getOrDefault(guessedChar, 0) + 1
            if (guessedFrequency[guessedChar]!! <= secretFrequency.getOrDefault(guessedChar, 0)) {
                wrongPositionCount++
            }
        }
    }
    return wrongPositionCount
}