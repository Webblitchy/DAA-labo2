package ch.heigvd.labo2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Set interaction of radio buttons
        val radioGroup = findViewById<RadioGroup>(R.id.occupation)
        val studentGroup = findViewById<Group>(R.id.student_data)
        val workerGroup = findViewById<Group>(R.id.worker_data)

        val okButton = findViewById<Button>(R.id.ok_button)
        val cancelButton = findViewById<Button>(R.id.cancel_button)

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
                else -> {
                    studentGroup.visibility = View.GONE
                    workerGroup.visibility = View.GONE
                }
            }
        }

        // Set nationnality spinner (using this answer : https://stackoverflow.com/a/64739753/10010580)

        val spinnerNationnality = findViewById<Spinner>(R.id.main_base_nationality_input)
        val itemsNationnality = resources.getStringArray(R.array.nationalities)


        val spinnerAdapterNationnality = object : ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, itemsNationnality) {

            override fun isEnabled(position: Int): Boolean { // if is not the hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(Color.GRAY) // gray color for the hint
                }
                return view
            }
        }

        spinnerAdapterNationnality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerNationnality.adapter = spinnerAdapterNationnality

        var nationnalitySelected : String
        spinnerNationnality.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == itemsNationnality[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    nationnalitySelected = value
                }
            }

        }

        // set sector spinner
        val spinnerSector = findViewById<Spinner>(R.id.main_specific_sector_input)
        val sectorItems = resources.getStringArray(R.array.sectors)


        val spinnerAdapter = object : ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sectorItems) {

            override fun isEnabled(position: Int): Boolean { // if is not the hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(Color.GRAY) // gray color for the hint
                }
                return view
            }
        }


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSector.adapter = spinnerAdapter

        var sectorSelected : String
        spinnerSector.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == sectorItems[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    sectorSelected = value
                }
            }

        }


        okButton.setOnClickListener {
            // TODO: Should create a new instance of Student/Worker & print it in logs
        }

        cancelButton.setOnClickListener {
            // TODO: Should clear ALL input fields
        }

    }
}