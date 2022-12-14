package com.example.spacebunsmobileapp.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.RC_SIGN_IN
import com.example.spacebunsmobileapp.databinding.FragmentAccountBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AccountFragment : Fragment() {
    //private lateinit var binding: FragmentAccountBinding
    private var _binding: FragmentAccountBinding? = null
    private val nav by lazy { findNavController() }
    lateinit var auth: FirebaseAuth
    private lateinit var imgUri: Uri

    private val binding get() = _binding!!


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgUser.setImageURI(it.data?.data)  // result is path
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.loginButton.setOnClickListener {
            // login to firebase
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
//            nav.navigateUp()
        }

        //whether user login or not
        if (user != null) {
            //binding.edtName.setText(user.displayName)
            binding.edtEmail.setText(user.email)

            //whether email is verified
            if (user.isEmailVerified) {
                binding.iconVerify.visibility = View.VISIBLE
                binding.iconNotVerify.visibility = View.GONE
            } else {
                binding.iconVerify.visibility = View.GONE
                binding.iconNotVerify.visibility = View.VISIBLE
            }
        }

        binding.btnProfile.setOnClickListener {
            btnProfile()
        }

        binding.imgUser.setOnClickListener {
            select()
        }



        binding.btnLogout.setOnClickListener {
            btnLogout()
        }

        binding.btnVerify.setOnClickListener {
            emailVerification()
        }
        //change password
        binding.btnChangePass.setOnClickListener {
            changePass()
        }

        binding.btnChangeEmail.setOnClickListener {
            changeEmail()
        }

//        binding.btnScanQR.setOnClickListener {
//            scanQRCode()
//        }
    }

    private fun btnProfile() {
        nav.navigate(R.id.profileFragment)
    }

//    private fun scanQRCode() {
//        val intent = Intent(context, CodeScannerActivity::class.java)
//        startActivity(intent)
//    }

private fun changeEmail() {
    val intent = Intent(context, ChangeEmailActivity::class.java)
    startActivity(intent)
}

    private fun changePass() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.cvCurrentPass.visibility = View.VISIBLE

        binding.btnCancel.setOnClickListener {
            binding.cvCurrentPass.visibility = View.GONE
        }

        binding.btnConfirm.setOnClickListener btnConfirm@{

            val pass = binding.edtCurrentPassword.text.toString()

            if (pass.isEmpty()) {
                binding.edtCurrentPassword.error = "Password cannot be empty!"
                binding.edtCurrentPassword.requestFocus()
                return@btnConfirm
            }

            user.let {
                val userCredential = EmailAuthProvider.getCredential(it?.email!!, pass)
                it.reauthenticate(userCredential).addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            binding.cvCurrentPass.visibility = View.GONE
                            binding.cvUpdatePass.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            binding.edtCurrentPassword.error = "Wrong Password"
                            binding.edtCurrentPassword.requestFocus()
                        }
                        else -> {
                            Toast.makeText(
                                activity,
                                "${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            binding.btnNewCancel.setOnClickListener {
                binding.cvCurrentPass.visibility = View.GONE
                binding.cvUpdatePass.visibility = View.GONE
            }

            binding.btnNewChange.setOnClickListener newChangePassword@{

                val newPass = binding.edtNewPass.text.toString()
                val passConfirm = binding.edtConfirmPass.text.toString()

                if (newPass.isEmpty()) {
                    binding.edtCurrentPassword.error = "Password cannot be empty!"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (passConfirm.isEmpty()) {
                    binding.edtCurrentPassword.error = "Retype new password!"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (newPass.length < 6) {
                    binding.edtCurrentPassword.error = "Password must more than 6 character!"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (passConfirm.length < 6) {
                    binding.edtCurrentPassword.error = "Password must more than 6 character!"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (newPass != passConfirm) {
                    binding.edtCurrentPassword.error = "Password is not the same!"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                user?.let {
                    user.updatePassword(newPass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                activity,
                                "Password is Update Successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            successLogout()
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }
        }
    }

    private fun successLogout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()

        Toast.makeText(activity, "Please login again", Toast.LENGTH_SHORT).show()
    }

    private fun emailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Verification email is send", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun goToCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
//            activity?.packageManager?.let {
//                intent?.resolveActivity(it).also {
//                    startActivityForResult(intent, REQ_CAM)
//                }
//            }
//        }
//    }

    private fun select() {
        // TODO: Select file
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQ_CAM && resultCode == Activity.RESULT_OK) {
//            val imgBitmap = data?.extras?.get("data") as Bitmap
//            uploadImgToFirebase(imgBitmap)
//        }
//    }

    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref =
            FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener { Task ->
                        Task.result.let { Uri ->
                            imgUri = Uri
                            binding.imgUser.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }

    private fun btnLogout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        const val REQ_CAM = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val applicationContext = requireActivity().application  // get context
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                Log.d("Login", "User ${user?.displayName} has signed in.")
                Toast.makeText(
                    applicationContext,
                    "Welcome ${user?.displayName}",
                    Toast.LENGTH_SHORT
                ).show()
                nav.navigateUp()
//                showUI()
            } else {
                Log.d("Login", "Signing in failed!")
                Toast.makeText(
                    applicationContext,
                    response?.error?.message ?: "Sign in failed",
                    Toast.LENGTH_LONG
                ).show()
            }
//        } else {
//            paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
        }
    }

    private fun showUI() {
        TODO("Here should show the UI if the user has logged in")

    }
}