package ch.heigvd.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.constraintlayout.widget.Group

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set interaction of radio buttons
        val radioGroup = findViewById<RadioGroup>(R.id.occupation)
        val studentGroup = findViewById<Group>(R.id.student_data)
        val workerGroup = findViewById<Group>(R.id.worker_data)

        radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.student -> {
                    studentGroup.visibility = View.VISIBLE
                    workerGroup.visibility = View.GONE
                }
                R.id.employee -> {
                    studentGroup.visibility = View.GONE
                    workerGroup.visibility = View.VISIBLE
                }
            }
        }
    }
}