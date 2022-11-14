package ch.heigvd.labo2

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Fetch all interactable views, starting with common fields
        val lastname = findViewById<EditText>(R.id.main_base_name_input)
        val firstname = findViewById<EditText>(R.id.main_base_firstname_input)
        val birthday = findViewById<EditText>(R.id.main_base_birthdate_input)
        val nationality = findViewById<Spinner>(R.id.main_base_nationality_input)
        val radioGroup = findViewById<RadioGroup>(R.id.occupation)
        val studentGroup = findViewById<Group>(R.id.student_data)
        val workerGroup = findViewById<Group>(R.id.worker_data)

        // Student-specific fields
        val school = findViewById<EditText>(R.id.main_specific_school_input)
        val graduationYear = findViewById<EditText>(R.id.main_specific_graduationyear_input)

        // Worker-specific fields
        val company = findViewById<EditText>(R.id.main_specific_compagny_input)
        val sector = findViewById<Spinner>(R.id.main_specific_sector_input)
        val experience = findViewById<EditText>(R.id.main_specific_experience_input)

        // And finally the additional fields
        val email = findViewById<EditText>(R.id.additional_email_input)
        val remarks = findViewById<EditText>(R.id.additional_remarks_input)
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

        nationality.adapter = spinnerAdapterNationnality

        var nationnalitySelected : String
        nationality.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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

        sector.adapter = spinnerAdapter

        var sectorSelected : String
        sector.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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


        // Date picker
        val pickDateBtn = findViewById<ImageButton>(R.id.cake_button)
        val selectedDate = findViewById<EditText>(R.id.main_base_birthdate_input)

        // create the dialog
        val birthdatePickerDialog = DatePickerDialog(
            this,
            R.style.MySpinnerDatePickerStyle, // use the defined style (for spinner mode)
            { view, year, monthOfYear, dayOfMonth ->
                val displayedDate = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
                selectedDate.setText(displayedDate)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        // ajout du dialog sur les listener
        pickDateBtn.setOnClickListener {
            birthdatePickerDialog.show()
        }

        selectedDate.setOnClickListener {
            birthdatePickerDialog.show()
        }


        okButton.setOnClickListener {
            // TODO: Should create a new instance of Student/Worker & print it in logs
        }

        cancelButton.setOnClickListener {
            // TODO: Should clear ALL input fields
        }

    }
}