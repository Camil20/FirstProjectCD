package com.cdoroteo.firstprojectcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.databinding.DataBindingUtil
import com.cdoroteo.firstprojectcd.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding
    private lateinit var progressBar : ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        progressBar = binding.progressBarRegister
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference = database.reference.child("User")

        createNewAccount()

    }

    private fun createNewAccount(){
        binding.btnRegister.setOnClickListener {
            val name: String = binding.txtName.text.toString()
            val lastName: String = binding.txtLastName.text.toString()
            val phone: String = binding.editTextPhone.text.toString()
            val email: String = binding.editTxtEmail.text.toString()
            validateEmail()
            val password: String = binding.txtPassword.text.toString()
            validatePassword()
            val gender: String = binding.txtGender.text.toString()
            val dateOfBirthday: String = binding.editTxtDate.text.toString()
            val country: String = binding.txtCountry.text.toString()
            val address: String = binding.txtAddress.text.toString()

            if (TextUtils.isEmpty(name)){
                binding.txtName.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(lastName)){
                binding.txtLastName.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(phone)){
                binding.editTextPhone.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(gender)){
                binding.txtGender.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(dateOfBirthday)) {
                binding.editTxtDate.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(country)) {
                binding.txtCountry.error = "Field can not be empty"
            }
            else if(TextUtils.isEmpty(address)){
                binding.txtAddress.error = "Field can not be empty"
            }

            else{
                progressBar.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(
                    email.toString(),
                    password.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val user = auth.currentUser
                            val userDB = dbReference.child((user?.uid!!))
                            verifyEmail(user)

                            userDB.child("Name").setValue(name)
                            userDB.child("LastName").setValue(lastName)
                            userDB.child("Phone").setValue(phone)
                            userDB.child("Email").setValue(email)
                            userDB.child("Gender").setValue(gender)
                            userDB.child("DateOfBirth").setValue(dateOfBirthday)
                            userDB.child("Country").setValue(country)
                            userDB.child("Address").setValue(address)
                            finish()

                        }
                    }
            }
        }
    }

    private fun validateEmail() : Boolean{
        val email = binding.editTxtEmail.editableText.toString()
        return if(email.isEmpty()){
            binding.editTxtEmail.error = "Field can not be empty"
            false
        }else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTxtEmail.error = "Please enter a valid email address"
            false
        }else{
            binding.editTxtEmail.error = null
            true
        }

    }

    private fun validatePassword() : Boolean{
        val password = binding.txtPassword.editableText.toString()
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +  //at least 1 digit
                    "(?=.*[a-z])" +  //at least 1 lower case letter
                    "(?=.*[A-Z])" +  //at least 1 upper case letter
                    ".{6,}" +        //at least 6 characters
                    "$"
        )
        return if(password.isEmpty()){
            binding.txtPassword.error = "Field can not be empty"
            false
        }else if (!passwordRegex.matcher(password).matches()){
            binding.txtPassword.error = "Password is too weak"
            false
        }else{
            binding.txtPassword.error = null
            true
        }

    }


    private fun verifyEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isComplete) {
                    Toast.makeText(this,"Email sent", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"ERROR in sending the email", Toast.LENGTH_LONG).show()
                }

            }
    }

}