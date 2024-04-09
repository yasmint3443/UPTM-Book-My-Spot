package com.example.uptmbookmyspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uptmbookmyspot.Model.Model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterPage : AppCompatActivity() {

    // declare to connect with database
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    // initial all component
    private lateinit var submit : Button
    private lateinit var reset : Button
    private lateinit var name : EditText
    private lateinit var phone : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var idNum : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        auth = FirebaseAuth.getInstance()

        // declare all components
        submit = findViewById<Button>(R.id.btnRegister)
        reset = findViewById<Button>(R.id.btnReset)
        name = findViewById<EditText>(R.id.eTName)
        phone = findViewById<EditText>(R.id.eTPhone)
        idNum = findViewById<EditText>(R.id.eTIdNum)
        email = findViewById<EditText>(R.id.eTEmail)
        password = findViewById<EditText>(R.id.eTPassword)

        // pop-up message when click button and record
        Toast.makeText(this,"Submit", Toast.LENGTH_LONG).show()

        // when user click submit button
        submit.setOnClickListener {
            val emailStr = email.text.toString()
            val idNumStr = idNum.text.toString()
            val nameStr = name.text.toString()
            val passwordStr = password.text.toString()
            val phoneStr = phone.text.toString()

            if (emailStr.isNotEmpty() && idNumStr.isNotEmpty() && nameStr.isNotEmpty() &&
                passwordStr.isNotEmpty() && phoneStr.isNotEmpty()) {
                // call function to register user
                registerUser(emailStr, passwordStr)
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            }
        }

        // reset button
        reset.setOnClickListener {
            name.setText(" ")
            phone.setText(" ")
            email.setText(" ")
            password.setText(" ")
            idNum.setText(" ")
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Registration successful", Toast.LENGTH_SHORT).show()
                    // proceed to save user data to the database
                    saveUserData()
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // create the function saveData
    // this function send data to Firebase
    // n = name
    // p = password
    // e = email
    // t = phone
    // i = id

    // create the function saveUserData
    // this function sends user data to Firebase Realtime Database
    private fun saveUserData() {
        val userId = auth.currentUser?.uid ?: return
        val emailStr = email.text.toString()
        val idNumStr = idNum.text.toString()
        val nameStr = name.text.toString()
        val passwordStr = password.text.toString()
        val phoneStr = phone.text.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        val userModel = Model(emailStr, userId, idNumStr, nameStr, passwordStr, phoneStr)

        dbRef.child(userId).setValue(userModel)
            .addOnCompleteListener {
                Toast.makeText(this, "User data saved successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish() // Finish this activity so user can't go back to it by pressing back button
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_LONG).show()
            }
    }
}