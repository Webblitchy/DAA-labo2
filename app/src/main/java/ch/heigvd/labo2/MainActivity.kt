package ch.heigvd.labo2

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch all interactable views, starting with common fields
        val lastnameField = findViewById<EditText>(R.id.main_base_name_input)
        val firstnameField = findViewById<EditText>(R.id.main_base_firstname_input)
        val birthdateField = findViewById<EditText>(R.id.main_base_birthdate_input)
        val cakeButton = findViewById<ImageButton>(R.id.cake_button)
        val nationalitySpinner = findViewById<Spinner>(R.id.main_base_nationality_input)
        val occupationRadioGroup = findViewById<RadioGroup>(R.id.occupation)
        val studentGroup = findViewById<Group>(R.id.student_data)
        val workerGroup = findViewById<Group>(R.id.worker_data)

        // Student-specific fields
        val schoolField = findViewById<EditText>(R.id.main_specific_school_input)
        val graduationYearField = findViewById<EditText>(R.id.main_specific_graduationyear_input)

        // Worker-specific fields
        val companyField = findViewById<EditText>(R.id.main_specific_compagny_input)
        val sectorSpinner = findViewById<Spinner>(R.id.main_specific_sector_input)
        val experienceField = findViewById<EditText>(R.id.main_specific_experience_input)

        // And finally additional fields
        val emailField = findViewById<EditText>(R.id.additional_email_input)
        val remarksField = findViewById<EditText>(R.id.additional_remarks_input)
        val okButton = findViewById<Button>(R.id.ok_button)
        val cancelButton = findViewById<Button>(R.id.cancel_button)

        val inputFields = arrayOf(
            lastnameField,
            firstnameField,
            birthdateField,
            schoolField,
            graduationYearField,
            companyField,
            experienceField,
            emailField,
            remarksField,
        )

        val inputSpinners = arrayOf(
            nationalitySpinner,
            sectorSpinner,
        )

        fun clear_inputs() {
            for (elem in inputFields) {
                elem.text = null
            }
            for (elem in inputSpinners) {
                elem.setSelection(0)
            }
        }

        // Hide part of the UI depending on the user type
        occupationRadioGroup.setOnCheckedChangeListener { _, id ->
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

        nationalitySpinner.adapter = spinnerAdapterNationnality

        var nationnalitySelected : String
        nationalitySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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

        sectorSpinner.adapter = spinnerAdapter

        var sectorSelected : String
        sectorSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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

        // create the dialog

        val calendar = Calendar.getInstance()
        val birthdatePickerDialog = DatePickerDialog(
            this,
            R.style.MySpinnerDatePickerStyle, // use the defined style (for spinner mode)
            { view, year, monthOfYear, dayOfMonth ->
                val displayedDate = SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE).format(calendar.time)
                birthdateField.setText(displayedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // ajout du dialog sur les listener
        cakeButton.setOnClickListener {
            birthdatePickerDialog.show()
        }

        birthdateField.setOnClickListener {
            birthdatePickerDialog.show()
        }

        okButton.setOnClickListener {
            // Assert core data are valid
            if (
                TextUtils.isEmpty(lastnameField.text.toString()) ||
                TextUtils.isEmpty(firstnameField.text.toString()) ||
                TextUtils.isEmpty(birthdateField.text.toString()) ||
                nationalitySpinner.selectedItem == null ||
                occupationRadioGroup.checkedRadioButtonId == -1 ||
                TextUtils.isEmpty(emailField.text.toString())
            ) {
                Toast
                    .makeText(this, "Missing core information", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Assert specific data are valid
            when (occupationRadioGroup.checkedRadioButtonId) {
                // TODO: Check for valid student data
                R.id.student -> {
                    if (
                        TextUtils.isEmpty(schoolField.text.toString()) ||
                        TextUtils.isEmpty(graduationYearField.text.toString())
                    ) {
                        Toast
                            .makeText(this, "Missing student information", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }

                // TODO: Check for valid worker data
                R.id.employee -> {
                    if (
                        TextUtils.isEmpty(companyField.text.toString()) ||
                        TextUtils.isEmpty(experienceField.text.toString()) ||
                        sectorSpinner.selectedItem == null
                    ) {
                        Toast
                            .makeText(this, "Missing worker information", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }

                // No need to check for empty selction, case is covered beforehand
            }

            // TODO: Create a new Person instance from the data and log it
        }

        cancelButton.setOnClickListener {
            // TODO: Should clear ALL input fields
        }

    }
}