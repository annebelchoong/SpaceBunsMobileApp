package com.example.spacebunsmobileapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.FragmentAccountBinding
import com.example.spacebunsmobileapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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

        //whether user login or not
        if (user != null){
            //binding.edtName.setText(user.displayName)
            binding.edtEmail.setText(user.email)
            binding.txtProName.setText(user.displayName)
            binding.txtProPhone.setText(user.phoneNumber)
            //binding.txtProAddress.setText(user.a)

            //whether email is verified
            if (user.isEmailVerified){
                binding.iconVerify.visibility = View.VISIBLE
                binding.iconNotVerify.visibility = View.GONE
            } else {
                binding.iconVerify.visibility = View.GONE
                binding.iconNotVerify.visibility = View.VISIBLE
            }
        }

        binding.imgUser.setOnClickListener {
            goToCamera()
        }
    }

    private fun goToCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            activity?.packageManager?.let {
                intent?.resolveActivity(it).also {
                    startActivityForResult(intent, REQ_CAM)
                }
            }
        }
    }

    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.imgUser.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }

    companion object{
        const val REQ_CAM = 100
    }
}