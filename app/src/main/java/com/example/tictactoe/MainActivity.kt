package com.example.tictactoe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {

    lateinit var buttons: Array<Array<ImageButton>>
    lateinit var textViewPlayer1: TextView
    lateinit var textViewPlayer2: TextView

    private var player1Turn: Boolean = true
    private var roundCount: Int = 0
    private var player1Points: Int = 0
    private var player2Points: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewPlayer1 = findViewById(R.id.player1TextView)
        textViewPlayer2 = findViewById(R.id.player2TextView)

        buttons = Array(3) { r ->
            Array(3) { c ->
                initButtons(r, c)
            }
        }
        val btnReset: Button = findViewById(R.id.btnReset)
        btnReset.setOnClickListener {
            player1Points = 0
            player2Points = 0
            updateScore()
            clearBoard()
        }
    }


    private fun initButtons(r: Int, c: Int): ImageButton {
        val btn: ImageButton = findViewById(resources.getIdentifier("btn$r$c", "id", packageName))
        btn.setOnClickListener {
            obBtnClick(btn)
        }
        return btn
    }

    private fun obBtnClick(btn: ImageButton) {
        if (btn.drawable != null) return
        if (player1Turn) {
            btn.setImageResource(R.drawable.ic_baseline_close_24)
        } else {
            btn.setImageResource(R.drawable.ic_baseline_panorama_fish_eye_24)
        }
        roundCount++

        if (checkForWin()) {
            if (player1Turn) win(1) else win(2)
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val fields = Array(3) { r ->
            Array(3) { c ->
                getField(buttons[r][c])
            }
        }
        for (i in 0..2) {
            if ((fields[i][0] == fields[i][1]) &&
                (fields[i][0] == fields[i][2]) &&
                (fields[i][0] != null)
            ) return true
        }

        for (i in 0..2) {
            if ((fields[0][i] == fields[1][i]) &&
                (fields[0][i] == fields[2][i]) &&
                (fields[0][i] != null)
            ) return true
        }
        if ((fields[0][0] == fields[1][1]) &&
            (fields[0][0] == fields[2][2]) &&
            (fields[0][0] != null)
        ) return true

        return false
    }

    private fun getField(btn: ImageButton): Char? {
        val drw: Drawable? = btn.drawable
        val drwCross: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_close_24, null)
        val drwHeart: Drawable? = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_panorama_fish_eye_24,
            null
        )

        return when (drw?.constantState) {
            drwCross?.constantState -> 'x'
            drwHeart?.constantState -> 'o'
            else -> null
        }

    }

    private fun win(player: Int) {
        if (player == 1) player1Points++ else player2Points++
        Toast.makeText(applicationContext, "Player $player Won!", Toast.LENGTH_SHORT).show()
        updateScore()
        clearBoard()

    }

    private fun clearBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].setImageResource(0)
            }
        }
        roundCount = 0
        player1Turn = true
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        textViewPlayer1.text = "Player 1: $player1Points"
        textViewPlayer2.text = "Player 2: $player2Points"
    }

    private fun draw() {
        Toast.makeText(applicationContext, "Match Draw!", Toast.LENGTH_SHORT).show()
        clearBoard()
    }
}