package ch.heigvd.labo2

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import java.text.DateFormat
import java.text.ParseException
import java.util.*

/*
 * Authors : Eliott Chytil, Maxim Golay & Lucien Perregaux
 *
 * Controls the View and the Model.
 * - Validates the inputs
 * - Shows DatePickers on click
 * - Clear the inputs
 * - Creates a Person
 * - Manages the visibles groups (areas) depending on the selected radio button
 */
class MainActivity : AppCompatActivity() {
    val SPINNER_SELECT = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch all interactive views, starting with common fields
        val lastnameField = findViewById<EditText>(R.id.main_base_name_input)
        val firstnameField = findViewById<EditText>(R.id.main_base_firstname_input)
        val birthdateField = findViewById<EditText>(R.id.main_base_birthdate_input)
        val cakeButton = findViewById<ImageButton>(R.id.cake_button)
        val nationalitySpinner = findViewById<Spinner>(R.id.main_base_nationality_input)
        val occupationRadioGroup = findViewById<RadioGroup>(R.id.occupation)
        val studentRadioButton = findViewById<RadioButton>(R.id.student)
        val employeeRadioButton = findViewById<RadioButton>(R.id.employee)
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

        // All fields
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

        // All spinners
        val inputSpinners = arrayOf(
            nationalitySpinner,
            sectorSpinner,
        )

        // Clear all inputs
        fun clearInputs() {
            for (elem in inputFields) {
                elem.text = null
            }
            for (elem in inputSpinners) {
                elem.setSelection(SPINNER_SELECT.toInt())
            }
            occupationRadioGroup.clearCheck()
        }

        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

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

        // Set nationality spinner (using this answer : https://stackoverflow.com/a/64739753/10010580)
        val itemsNationnality = resources.getStringArray(R.array.nationalities)

        val spinnerAdapterNationnality = object :
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsNationnality) {

            // disable click on the hint
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(Color.GRAY) // gray color for the hint
                }
                return view
            }
        }

        spinnerAdapterNationnality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        nationalitySpinner.adapter = spinnerAdapterNationnality

        nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // On screen rotation, this callback might be called with view set to null ???
                // Whatever, we'll just return and dont try anything...
                if (view == null) return

                val value = parent!!.getItemAtPosition(position).toString()
                if (value == itemsNationnality[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }

        }

        // set sector spinner
        val sectorItems = resources.getStringArray(R.array.sectors)


        val spinnerAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectorItems) {

                // disable click on the hint
                override fun isEnabled(position: Int): Boolean {
                    return position != SPINNER_SELECT.toInt()
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView
                    if (position == SPINNER_SELECT.toInt()) {
                        view.setTextColor(Color.GRAY) // gray color for the hint
                    }
                    return view
                }
            }


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sectorSpinner.adapter = spinnerAdapter

        sectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == sectorItems[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }

        // create the dialog

        val calendar = Calendar.getInstance()
        val birthdatePickerDialog = DatePickerDialog(
            this,
            R.style.MySpinnerDatePickerStyle, // use the defined style (for spinner mode)
            { _, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()
                cal.set(year, monthOfYear, dayOfMonth)

                val displayedDate = dateFormat.format(cal.time)
                birthdateField.setText(displayedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Add dialog on the listener
        cakeButton.setOnClickListener {
            birthdatePickerDialog.show()
        }

        birthdateField.setOnClickListener {
            birthdatePickerDialog.show()
        }

        okButton.setOnClickListener {
            val lastname = lastnameField.text.toString()
            if (TextUtils.isEmpty(lastname)) {
                showToast(getString(R.string.input_lastname))
                return@setOnClickListener
            }

            val firstname = firstnameField.text.toString()
            if (TextUtils.isEmpty(firstname)) {
                showToast(getString(R.string.input_firstname))
                return@setOnClickListener
            }

            // birthday
            try {
                calendar.time = dateFormat.parse(birthdateField.text.toString()) as Date
            } catch (e: ParseException) {
                showToast(getString(R.string.input_birthdate))
                return@setOnClickListener
            }

            if (nationalitySpinner.selectedItemId == SPINNER_SELECT) {
                showToast(getString(R.string.input_nationnality))
                return@setOnClickListener
            }
            val nationality = nationalitySpinner.selectedItem.toString()

            val email = emailField.text.toString()
            if (TextUtils.isEmpty(email)) {
                showToast(getString(R.string.input_email))
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                showToast(getString(R.string.bad_email))
                return@setOnClickListener
            }

            val remark = remarksField.text.toString()
            // The remark is optionnal

            val person: Person

            // Assert specific data are valid
            when (occupationRadioGroup.checkedRadioButtonId) {
                R.id.student -> {
                    val university = schoolField.text.toString()
                    if (TextUtils.isEmpty(university)) {
                        showToast(getString(R.string.input_university))
                        return@setOnClickListener
                    }

                    val graduationYear: Int
                    try {
                        val graduationYearStr = graduationYearField.text.toString()
                        if (TextUtils.isEmpty(graduationYearStr)) {
                            showToast(getString(R.string.input_graduation_year))
                            return@setOnClickListener
                        }
                        graduationYear = graduationYearField.text.toString().toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        showToast(getString(R.string.bad_graduation_year))
                        return@setOnClickListener
                    }

                    person = Student(
                        lastname,
                        firstname,
                        calendar,
                        nationality,
                        university,
                        graduationYear,
                        email,
                        remark
                    )
                }

                R.id.employee -> {
                    val compagny = companyField.text.toString()
                    if (TextUtils.isEmpty(compagny)) {
                        showToast(getString(R.string.input_company))
                        return@setOnClickListener
                    }

                    if (sectorSpinner.selectedItemId == SPINNER_SELECT) {
                        showToast(getString(R.string.input_sector))
                        return@setOnClickListener
                    }
                    val sector = sectorSpinner.selectedItem.toString()

                    val experienceYear: Int
                    try {
                        val experienceYearStr = experienceField.text.toString()
                        if (TextUtils.isEmpty(experienceYearStr)) {
                            showToast(getString(R.string.input_experience))
                            return@setOnClickListener
                        }
                        experienceYear = experienceYearStr.toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        showToast(getString(R.string.bad_experience))
                        return@setOnClickListener
                    }

                    person = Worker(
                        lastname,
                        firstname,
                        calendar,
                        nationality,
                        compagny,
                        sector,
                        experienceYear,
                        email,
                        remark
                    )
                }

                // No need to check for empty selction, case is covered beforehand
                else -> {
                    showToast(getString(R.string.input_occupation))
                    return@setOnClickListener
                }
            }
            // We log the person
            Log.i("Person Object", person.toString())

            // Notifies the user
            showToast(getString(R.string.data_added))
        }

        cancelButton.setOnClickListener {
            // Clear ALL input fields
            clearInputs()
        }


        fun fillPerson(person: Person) {
            lastnameField.setText(person.name)
            firstnameField.setText(person.firstName)
            birthdateField.setText(dateFormat.format(person.birthDay.time))
            nationalitySpinner.setSelection(getSpinnerIndex(nationalitySpinner, person.nationality))
            emailField.setText(Person.exampleWorker.email)
            remarksField.setText(Person.exampleWorker.remark)

            when (person.javaClass.simpleName.toString()) {
                "Worker" -> {
                    val worker : Worker = person as Worker
                    employeeRadioButton.isChecked = true
                    companyField.setText(worker.company)
                    sectorSpinner.setSelection(getSpinnerIndex(sectorSpinner, Person.exampleWorker.sector))
                    experienceField.setText(Person.exampleWorker.experienceYear.toString())
                }
                "Student" -> {
                    val student : Student = person as Student
                    studentRadioButton.isChecked = true
                    schoolField.setText(student.university)
                    graduationYearField.setText(student.graduationYear.toString())
                }
            }
        }


        // CHANGE HERE to fill with an example person
        fillPerson(Person.exampleWorker)
        //fillPerson(Person.exampleStudent)
    }

    private fun showToast(s: String) {
        Toast
            .makeText(
                this,
                s,
                Toast.LENGTH_SHORT
            )
            .show()
    }

    private fun getSpinnerIndex(spinner: Spinner, myString: String?): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                return i
            }
        }
        return 0
    }

}