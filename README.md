# Mastermind Game Evaluation

## Problem Statement
In the Mastermind game, one player creates a secret code of colored pins, and the other player attempts to guess it. Each pin can be one of six colors, and the code is four pins long. The challenge is to guess the right colors in the correct order.

## Solution Overview
We've implemented an algorithm in Kotlin to evaluate the codebreaker's guesses against the codemaker's secret code. The algorithm provides feedback on the number of pins that are correctly positioned and the number of pins that are correct in color but misplaced.

### Algorithm Explanation
The evaluation process involves two critical steps:

#### 1. Counting Right Positions
- Compare each pin in the guess with the corresponding pin in the secret code.
- If a pin is in the exact position and color as in the secret, it's counted as a correct position.
- We keep track of these correct positions and adjust the count of each pin's occurrence in the secret code to prevent over-counting in the next step.

#### 2. Counting Misplaced Pins
- Then identify pins in the guess that are the correct color but in the wrong position.
- For each such pin, we ensure it's not already counted as a correct position.
- This is done by comparing the updated counts of pins in the secret code after accounting for correctly positioned pins.
- Each pin that's correct in color but wrongly placed increases the count of misplaced pins.

### Complexity
- **Time Complexity**: O(N), where N is the length of the code. The algorithm checks each pin exactly once.
- **Space Complexity**: O(1). The space used remains constant regardless of the code length due to the fixed range of pin colors.

## Sample Test Cases

To better understand the evaluation process, let's go through some sample test cases, their expected outputs, and the reasoning behind these expectations:

### Test Case 1
- **Secret**: `"ABCD"`
- **Guess**: `"ABCD"`
- **Expected Output**: `rightPosition = 4, wrongPosition = 0`
- **Explanation**: Each pin in the guess matches both the color and position of the corresponding pin in the secret code. Therefore, all four pins are counted as correctly positioned, and there are no misplaced pins.

### Test Case 2
- **Secret**: `"ABCD"`
- **Guess**: `"CDBA"`
- **Expected Output**: `rightPosition = 0, wrongPosition = 4`
- **Explanation**: While all the pins in the guess are of the correct color, none are in the correct position. Each pin from the guess finds a match in color in the secret code but at a different position.

### Test Case 3
- **Secret**: `"AABC"`
- **Guess**: `"ADFA"`
- **Expected Output**: `rightPosition = 1, wrongPosition = 1`
- **Explanation**: The first 'A' in the guess is in the correct position. The second 'A' in the guess is the correct color but in the wrong position. The 'D' and 'F' in the guess don't match any pins in the secret.

### Test Case 4
- **Secret**: `"AABC"`
- **Guess**: `"DEFA"`
- **Expected Output**: `rightPosition = 0, wrongPosition = 1`
- **Explanation**: There are no correctly positioned pins in the guess. The 'A' in the guess is the correct color but in the wrong position. 'D', 'E', and 'F' in the guess don't match any pins in the secret.

### Test Case 5
- **Secret**: `"BACD"`
- **Guess**: `"BBAA"`
- **Expected Output**: `rightPosition = 1, wrongPosition = 1`
- **Explanation**: The first 'B' in the guess is correctly positioned. The second 'B' does not have a corresponding 'B' in the secret. The first 'A' in the guess is misplaced, and the second 'A' doesn't find a match as the only 'A' in the secret is already counted as misplaced.

These test cases demonstrate how the algorithm differentiates between correctly positioned and misplaced pins. The algorithm ensures accurate counting, respecting the rules of the Mastermind game without double-counting pins.
