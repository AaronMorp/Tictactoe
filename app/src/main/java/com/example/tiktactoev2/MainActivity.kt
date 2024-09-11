package com.example.tiktactoev2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.example.tiktactoev2.ui.theme.TikTacToeV2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TikTacToeV2Theme {
                TicTacToeGame()
            }
        }
    }
}



@Composable
fun TicTacToeGame() {
    var boardState by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameResult by remember { mutableStateOf<String?>(null) }
    var showBoard by remember { mutableStateOf(true) }
    var playerXWins by remember { mutableStateOf(0) }
    var playerOWins by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showBoard) {
            if (gameResult == null) {
                Text(
                    text = "Tic Tac Toe",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(bottom = 16.dp),
                    onTextLayout = { result: TextLayoutResult ->
                        // Do something with the TextLayoutResult
                    }
                )

                Text(
                    text = "Player ${currentPlayer}'s Turn",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    modifier = Modifier.padding(bottom = 16.dp),
                    onTextLayout = { result: TextLayoutResult ->
                        // Do something with the TextLayoutResult
                    }
                )
            }

            if (gameResult != null) {
                Text(
                    text = gameResult!!,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    onTextLayout = { result: TextLayoutResult ->
                        // Do something with the TextLayoutResult
                    }
                )
            }

            Board(boardState) { row, col ->
                if (boardState[row][col].isEmpty() && gameResult == null) {
                    boardState = boardState.copyOf().also {
                        it[row][col] = currentPlayer
                    }
                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                    gameResult = checkGameResult(boardState)
                }
            }
        } else {
            // Display high scores
            Text(
                text = "High Scores",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                onTextLayout = { result: TextLayoutResult ->
                    // Do something with the TextLayoutResult
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Player X: $playerXWins",             onTextLayout = { result: TextLayoutResult ->
                // Do something with the TextLayoutResult
            })
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Player O: $playerOWins",             onTextLayout = { result: TextLayoutResult ->
                // Do something with the TextLayoutResult
            })
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Draws: $draws",             onTextLayout = { result: TextLayoutResult ->
                // Do something with the TextLayoutResult
            })
            Spacer(modifier = Modifier.height(16.dp))

            // Restart Button
            Button(
                onClick = {
                    showBoard = true
                    boardState = Array(3) { Array(3) { "" } }
                    currentPlayer = "X"
                    gameResult = null
                },
                interactionSource = remember { MutableInteractionSource() }
            ) {
                Text(
                    text = "Restart Game",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    onTextLayout = { result: TextLayoutResult ->
                        // Do something with the TextLayoutResult
                    }
                )
            }
        }

        // LaunchedEffect for delay and high score update
        LaunchedEffect(key1 = gameResult) {
            if (gameResult != null) {
                if (gameResult!!.contains("X wins")) {
                    playerXWins++
                } else if (gameResult!!.contains("O wins")) {
                    playerOWins++
                } else if (gameResult!!.contains("draw")) {
                    draws++
                }
                delay(5000)
                showBoard = false
            }
        }
    }
}


@Composable
fun Board(boardState: Array<Array<String>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Cell(boardState[i][j], onCellClick, i, j)
                }
            }
        }
    }
}


@Composable
fun Cell(value: String, onCellClick: (Int, Int) -> Unit, row: Int, col: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(
                color = Color.White, // Set background color to white
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onCellClick(row, col) }
            .padding(8.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = if (value == "X") Color.Red else Color.Blue,
            onTextLayout = { result: TextLayoutResult ->
                // Do something with the TextLayoutResult
            }
        )
    }
}

fun checkGameResult(boardState: Array<Array<String>>): String? {
    // Check rows
    for (i in 0..2) {
        if (boardState[i][0].isNotEmpty() && boardState[i][0] == boardState[i][1] && boardState[i][0] == boardState[i][2]) {
            return "Player ${boardState[i][0]} wins!"
        }
    }

    // Check columns
    for (j in 0..2) {
        if (boardState[0][j].isNotEmpty() && boardState[0][j] == boardState[1][j] && boardState[0][j] == boardState[2][j]) {
            return "Player ${boardState[0][j]} wins!"
        }
    }

    // Check diagonals
    if (boardState[0][0].isNotEmpty() && boardState[0][0] == boardState[1][1] && boardState[0][0] == boardState[2][2]) {
        return "Player ${boardState[0][0]} wins!"
    }
    if (boardState[0][2].isNotEmpty() && boardState[0][2] == boardState[1][1] && boardState[0][2] == boardState[2][0]) {
        return "Player ${boardState[0][2]} wins!"
    }

    // Check for draw
    if (boardState.all { row -> row.all { it.isNotEmpty() } }) {
        return "It's a draw!"
    }

    return null // Game is still ongoing
}


