package com.cdoroteo.firstprojectcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.cdoroteo.firstprojectcd.databinding.ActivityForgotPsswBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPsswActivity : AppCompatActivity() {

    lateinit var binding : ActivityForgotPsswBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_pssw)
        auth = FirebaseAuth.getInstance()
        progressBar = binding.progressBarForgot


        binding.btnSend.setOnClickListener {
            val email = binding.txtEmail.text.toString()

            if(!TextUtils.isEmpty(email)){
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                            task ->
                        if(task.isSuccessful)
                        {
                            progressBar.visibility = View.VISIBLE
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        else {
                            Toast.makeText(this,"ERROR to send the email", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

}