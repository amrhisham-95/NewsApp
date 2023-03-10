package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSignUpEmailAndPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpEmailAndPasswordFragment : Fragment() {

    private lateinit var binding : FragmentSignUpEmailAndPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_email_and_password,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.signUpBtn.setOnClickListener {
            if(binding.EmailSignUp.text.isEmpty() || binding.PasswordSignUp.text.isEmpty()) {
                Toast.makeText(requireContext(),"Please Enter The Empty Square",Toast.LENGTH_LONG).show()
            }else {
                auth.createUserWithEmailAndPassword(
                    binding.EmailSignUp.text.toString().trim(),
                    binding.PasswordSignUp.text.toString().trim()
                )
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                requireContext(),
                                "signUpWithEmail:success",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            val user = auth.currentUser
                            findNavController().navigate(R.id.action_signUpEmailAndPasswordFragment_to_welcomeFragment)
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                requireContext(),
                                "signUpWithEmail:failure",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            updateUI(null)
                        }
                    }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }
}