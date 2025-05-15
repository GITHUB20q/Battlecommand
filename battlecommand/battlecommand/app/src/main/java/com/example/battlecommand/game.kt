package com.example.battlecommand

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameActivity : AppCompatActivity() {

    private lateinit var gridAdapter: GridRecyclerAdapter
    private lateinit var turnText: TextView
    private val gridSize = 6
    private val shipsHealth = mutableMapOf<Pair<Int, Int>, Int>() // track health per ship cell
    private var shipsRemaining = 3
    private var shipsPlaced = 0
    private var cannonPlaced = false

    private enum class Mode {
        PLACEMENT,
        ATTACK,
        DEFEND
    }
    private var currentMode = Mode.PLACEMENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        turnText = findViewById(R.id.turnText)
        val gameGrid: RecyclerView = findViewById(R.id.gameGrid)

        gridAdapter = GridRecyclerAdapter(gridSize) { row, col ->
            handleCellClick(row, col)
        }

        gameGrid.layoutManager = GridLayoutManager(this, gridSize)
        gameGrid.adapter = gridAdapter

        findViewById<Button>(R.id.btnAttack).setOnClickListener { enterAttackMode() }
        findViewById<Button>(R.id.btnDefend).setOnClickListener { enterDefendMode() }
        findViewById<Button>(R.id.btnReset).setOnClickListener { resetGame() }

        updateTurnText()
    }

    private fun handleCellClick(row: Int, col: Int) {
        when (currentMode) {
            Mode.PLACEMENT -> {
                if (shipsPlaced < 3) {
                    if (!gridAdapter.isOccupied(row, col)) {
                        gridAdapter.setShipAt(row, col, R.drawable.ship3)
                        shipsHealth[row to col] = 1  // set health of ship part
                        shipsPlaced++
                    } else {
                        Toast.makeText(this, "Cell already occupied!", Toast.LENGTH_SHORT).show()
                        return
                    }
                } else if (!cannonPlaced) {
                    if (!gridAdapter.isOccupied(row, col)) {
                        gridAdapter.setShipAt(row, col, R.drawable.cannon)
                        shipsHealth[row to col] = 1
                        cannonPlaced = true
                    } else {
                        Toast.makeText(this, "Cell already occupied!", Toast.LENGTH_SHORT).show()
                        return
                    }
                } else {
                    Toast.makeText(this, "All units placed!", Toast.LENGTH_SHORT).show()
                    return
                }
                updateTurnText()
            }

            Mode.ATTACK -> {
                val cellPos = row to col
                if (gridAdapter.isAlreadyAttacked(row, col)) {
                    Toast.makeText(this, "Already attacked this cell!", Toast.LENGTH_SHORT).show()
                    return
                }
                if (shipsHealth.containsKey(cellPos)) {
                    // Hit
                    shipsHealth.remove(cellPos)
                    shipsRemaining--
                    gridAdapter.setHitAt(row, col)
                    if (shipsRemaining == 0) {
                        turnText.text = "Game Over! You win!"
                        // Optionally disable further clicks here
                    } else {
                        turnText.text = "Hit! Ships remaining: $shipsRemaining"
                    }
                } else {
                    // Miss
                    gridAdapter.setMissAt(row, col)
                    turnText.text = "Miss!"
                }
            }

            Mode.DEFEND -> {
                // Implement defend logic here (optional)
                Toast.makeText(this, "Defend mode: no action on grid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enterAttackMode() {
        if (shipsPlaced < 3 || !cannonPlaced) {
            Toast.makeText(this, "Finish placing ships and cannon first!", Toast.LENGTH_SHORT).show()
            return
        }
        currentMode = Mode.ATTACK
        updateTurnText()
    }

    private fun enterDefendMode() {
        if (shipsPlaced < 3 || !cannonPlaced) {
            Toast.makeText(this, "Finish placing ships and cannon first!", Toast.LENGTH_SHORT).show()
            return
        }
        currentMode = Mode.DEFEND
        updateTurnText()
    }

    private fun updateTurnText() {
        when (currentMode) {
            Mode.PLACEMENT -> {
                when {
                    shipsPlaced < 3 -> turnText.text = "Place Ship ${shipsPlaced + 1} of 3"
                    !cannonPlaced -> turnText.text = "Place Cannon"
                    else -> turnText.text = "All units placed. Ready!"
                }
            }
            Mode.ATTACK -> turnText.text = "Attack Mode - Tap to attack"
            Mode.DEFEND -> turnText.text = "Defend Mode"
        }
    }

    private fun resetGame() {
        shipsPlaced = 0
        cannonPlaced = false
        shipsRemaining = 3
        shipsHealth.clear()
        gridAdapter.clearGrid()
        currentMode = Mode.PLACEMENT
        updateTurnText()
    }
}
