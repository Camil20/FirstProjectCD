package com.cdoroteo.firstprojectcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cdoroteo.firstprojectcd.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*enum class ProviderType{
    BASIC
}*/

class ProfileActivity : AppCompatActivity() {

    lateinit var binding : ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    var dbReference : DatabaseReference? = null
     var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database?.reference!!.child("User")
        loadProfile()

        /*val bundle = intent.extras
        val name = bundle?.getString("name")
        val lastname = bundle?.getString("lastname")
        val phone = bundle?.getString("phone")
        val email = bundle?.getString("email")
        val gender = bundle?.getString("gender")
        val dateBirth = bundle?.getString("dateBirth")
        val country = bundle?.getString("country")
        val address = bundle?.getString("address")
        setup(name?: "", lastname?: "", phone?: "", email?: "", gender?: "", dateBirth?: "", country?: "", address?: "")*/

    }

    private fun loadProfile(){
        title = "Profile"
        val user = auth.currentUser
       // val dbReference = database.reference.child("User")
        val userref = dbReference?.child(user?.uid!!)

        binding.txtProfileEmail.text = user?.email


        userref?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot){

                binding.txtProfileName.text = snapshot.child("Name").value.toString()
                binding.txtProfileLastName.text = snapshot.child("LastName").value.toString()
                binding.txtProfilePhone.text = snapshot.child("Phone").value.toString()
                binding.txtProfileGender.text = snapshot.child("Gender").value.toString()
                binding.txtProfileDate.text = snapshot.child("DateOfBirth").value.toString()
                binding.txtProfileCountry.text = snapshot.child("Country").value.toString()
                binding.txtProfileAddress.text = snapshot.child("Address").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    /*private fun setup(name:String, lastname:String, phone:String, email:String, gender:String, dateBirth:String, country:String, address:String )
    {
        binding.txtProfileName.text = name
        binding.txtProfileLastName.text = lastname
        binding.txtProfilePhone.text = phone
        binding.txtProfileEmail.text = email
        binding.txtProfileGender.text = gender
        binding.txtProfileDate.text = dateBirth
        binding.txtProfileCountry.text = country
        binding.txtProfileAddress.text = address


    }*/
}

/*class UsersInfo{
    val id: String = ""
    val name: String = ""
    val lastname: String = ""
    val phone: String = ""
    val email: String = ""
    val password: String = ""
    val gender: String = ""
    val dateOfBirth: String = ""
    val country: String = ""
    val address: String = ""
}*/