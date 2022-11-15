package ch.heigvd.labo2

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import java.text.ParseException
import java.util.*
import java.util.Calendar

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
            occupationRadioGroup.clearCheck()
        }

        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE)

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


        val spinnerAdapterNationnality = object :
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsNationnality) {

            override fun isEnabled(position: Int): Boolean { // if is not the hint
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

        var nationnalitySelected: String
        nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == itemsNationnality[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    nationnalitySelected = value
                }
            }

        }

        // set sector spinner
        val sectorItems = resources.getStringArray(R.array.sectors)


        val spinnerAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectorItems) {

                override fun isEnabled(position: Int): Boolean { // if is not the hint
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


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sectorSpinner.adapter = spinnerAdapter

        var sectorSelected: String
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

                val cal = Calendar.getInstance()
                cal.set(year, monthOfYear, dayOfMonth)

                val displayedDate = simpleDateFormat.format(cal.time)
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
            val lastname = lastnameField.text.toString()
            if (TextUtils.isEmpty(lastname)) {
                Toast
                    .makeText(this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val firstname = firstnameField.text.toString()
            if (TextUtils.isEmpty(firstname)) {
                Toast
                    .makeText(this, "Veuillez entrer votre prénom", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // birthday
            try {
                calendar.time = simpleDateFormat.parse(birthdateField.text.toString())
            } catch (e: ParseException) {
                Toast
                    .makeText(this, "Veuillez entrer votre date de naissance", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (nationalitySpinner.selectedItemId == 0L) {
                Toast
                    .makeText(this, "Veuillez séléctionner votre nationalité", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val nationality = nationalitySpinner.selectedItem.toString()

            val email = emailField.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast
                    .makeText(this, "Veuillez entrer votre adresse email", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            // TODO: regex validation email

            val remark = remarksField.text.toString()
            // La remarque est optionnelle.

            val person: Person

            // Assert specific data are valid
            when (occupationRadioGroup.checkedRadioButtonId) {
                R.id.student -> {
                    val university = schoolField.text.toString()
                    if (TextUtils.isEmpty(university)) {
                        Toast
                            .makeText(
                                this,
                                "Veuillez entrer le nom de votre université",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        return@setOnClickListener
                    }

                    val graduationYear: Int
                    try {
                        val graduationYearStr = graduationYearField.text.toString()
                        if (TextUtils.isEmpty(graduationYearStr)) {
                            Toast.makeText(
                                this,
                                "Veuillez entrer l'année du diplôme",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        graduationYear = graduationYearField.text.toString().toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(this, "L'année du diplôme est invalide", Toast.LENGTH_SHORT)
                            .show()
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
                        Toast
                            .makeText(this, "Veuillez entrer votre entreprise", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }

                    if (sectorSpinner.selectedItemId == 0L) {
                        Toast
                            .makeText(
                                this,
                                "Veuillez séléctionner votre secteur",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        return@setOnClickListener
                    }
                    val sector = sectorSpinner.selectedItem.toString()

                    val experienceYear: Int
                    try {
                        val experienceYearStr = experienceField.text.toString()
                        if (TextUtils.isEmpty(experienceYearStr)) {
                            Toast.makeText(
                                this,
                                "Veuillez entrer votre nombre d'années d'expérience",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        experienceYear = experienceYearStr.toInt()
                    } catch (e: java.lang.NumberFormatException) {
                        Toast.makeText(
                            this,
                            "Le nombre d'années d'expérience est invalide",
                            Toast.LENGTH_SHORT
                        ).show()
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
                    Toast
                        .makeText(
                            this,
                            "Veuillez séléctionner votre occupation",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    return@setOnClickListener
                }
            }
            // On log la personne
            Log.println(Log.INFO, null, person.toString())

            // Message pour l'utilisateur
            Toast
                .makeText(
                    this,
                    "Les données ont été ajoutées à la base de données !",
                    Toast.LENGTH_SHORT
                )
                .show()
        }

        cancelButton.setOnClickListener {
            // Clear ALL input fields
            clear_inputs()
        }

    }
}