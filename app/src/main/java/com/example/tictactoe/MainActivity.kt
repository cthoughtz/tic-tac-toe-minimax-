package com.example.tictactoe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

// Coding Tutorial From: https://www.youtube.com/watch?v=67bdklHmXA8&list=PLk7v1Z2rk4hjVaZ8DZKe8iT9RIM9OUrwp

class MainActivity : AppCompatActivity() {

    private val boardCells = Array(3) {
        arrayOfNulls<ImageView>(3)
    }

    var board = Board()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadBoard()

        button_restart.setOnClickListener {
            board = Board()
            text_view_result.text =""
            mapBoardToUi()
        }
    }

    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun loadBoard() {
        for (i in boardCells.indices) {
            for(j in boardCells.indices) {

                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply{
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)

                    width = 210
                    height = 190
                    bottomMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                boardCells[i][j]?.setOnClickListener(CellClickListener(i,j))

                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class CellClickListener(val i: Int, val j: Int) : View.OnClickListener {
        override fun onClick(p0: View?) {

            if (!board.isGameOver) {

                val cell = Cell(i, j)

                board.placeMove(cell, Board.PLAYER)

                board.minimax(0, Board.COMPUTER)

                board.computersMove?.let {
                    board.placeMove(it, Board.COMPUTER)
                }

                mapBoardToUi()
            }

            when {
                board.hasComputerWon() -> text_view_result.text = "Computer Won"
                board.hasPlayerWon() -> text_view_result.text = "Player Won"
                board.isGameOver -> text_view_result.text = "Game Tied"
            }
        }

    }
}