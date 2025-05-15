package com.example.battlecommand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerAdapter(
    private val gridSize: Int = 6,
    private val onCellClick: (row: Int, col: Int) -> Unit
) : RecyclerView.Adapter<GridRecyclerAdapter.CellViewHolder>() {

    // Cell state data class
    private data class CellState(
        var hasShip: Boolean = false,
        var drawableRes: Int = R.drawable.ship3,
        var isHit: Boolean = false,
        var isMiss: Boolean = false,
         // default background
    )

    // 2D array of cell states
    private val cellStates = Array(gridSize) { Array(gridSize) { CellState() } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gridcell, parent, false)
        return CellViewHolder(view)
    }

    override fun getItemCount(): Int = gridSize * gridSize

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val row = position / gridSize
        val col = position % gridSize
        val cell = cellStates[row][col]

        // Set image resource based on cell state priority: Hit > Miss > Ship > Default
        when {
            cell.isHit -> holder.cellImage.setImageResource(R.drawable.hit)     // your hit drawable
            cell.isMiss -> holder.cellImage.setImageResource(R.drawable.miss)   // your miss drawable
            cell.hasShip -> holder.cellImage.setImageResource(cell.drawableRes) // ship or cannon drawable
            else -> holder.cellImage.setImageResource(R.drawable.green_cell)   // empty cell drawable
        }

        holder.itemView.setOnClickListener {
            onCellClick(row, col)
        }
    }

    // Place a ship or cannon at (row, col)
    fun setShipAt(row: Int, col: Int, drawableRes: Int) {
        val cell = cellStates[row][col]
        cell.hasShip = true
        cell.drawableRes = drawableRes
        cell.isHit = false
        cell.isMiss = false
        notifyItemChanged(row * gridSize + col)
    }

    // Mark a cell as hit
    fun setHitAt(row: Int, col: Int) {
        val cell = cellStates[row][col]
        cell.isHit = true
        cell.isMiss = false
        notifyItemChanged(row * gridSize + col)
    }

    // Mark a cell as miss
    fun setMissAt(row: Int, col: Int) {
        val cell = cellStates[row][col]
        cell.isMiss = true
        cell.isHit = false
        notifyItemChanged(row * gridSize + col)
    }

    // Check if cell has ship/cannon
    fun isOccupied(row: Int, col: Int): Boolean {
        return cellStates[row][col].hasShip
    }

    // Check if cell was already attacked (hit or miss)
    fun isAlreadyAttacked(row: Int, col: Int): Boolean {
        val cell = cellStates[row][col]
        return cell.isHit || cell.isMiss
    }

    // Reset all cells
    fun clearGrid() {
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                cellStates[row][col] = CellState()
            }
        }
        notifyDataSetChanged()
    }

    class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cellImage: ImageView = itemView.findViewById(R.id.cellImage)

    }
}
