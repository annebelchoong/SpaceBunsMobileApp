package com.example.spacebunsmobileapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.RC_SIGN_IN
import com.example.spacebunsmobileapp.data.DonationEventDetail
import com.example.spacebunsmobileapp.util.errorDialog
import com.example.spacebunsmobileapp.data.DonationEventDetailViewModel
import com.example.spacebunsmobileapp.databinding.FragmentMakeDonationBinding
import com.example.spacebunsmobileapp.util.FirebaseEphemeralKeyProvider
import com.example.spacebunsmobileapp.util.StripeFactory
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import kotlinx.coroutines.launch

class MakeDonationFragment : Fragment() {
    private lateinit var binding: FragmentMakeDonationBinding
    private val nav by lazy { findNavController() }
    private val vm: DonationEventDetailViewModel by activityViewModels()
    private var currentUserT: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var paymentSession: PaymentSession
    private lateinit var selectedPaymentMethod: PaymentMethod
    private var isPaymentCompleted: Boolean = false
    private var amount: Long = 4000

    private val id by lazy { arguments?.getString("donationEventId", "") ?: "" }
    private val stripe: Stripe by lazy {
        StripeFactory(requireContext()).create()
    }

//    // TODO: Get content launcher
//    private val launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                binding.imgDonationEventPhoto.setImageURI(it.data?.data)  // result is path
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeDonationBinding.inflate(inflater, container, false)

        val applicationContext = requireActivity().application  // get context
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51LWbMdCffsYKlOgZJQu5OlKPoEe978IKUuzKJjwcW0ZDwb46LaVT09D3svdGTvaWoavzzCTqo9fM7DztaOL8dSGu00MhmVJabg"
        )

        setupPaymentSession()
        reset()
//        binding.imgDonationEventPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.paymentmethod.setOnClickListener {
            paymentSession.presentPaymentMethodSelection()
        }
        binding.btnDonate.setOnClickListener {
//            nav.navigate(R.id.checkoutFragment)
            lifecycleScope.launch {
                makePayment()
                setDonation()
            }
        }

        return binding.root
    }

    private fun makePayment(): Boolean {
        confirmPayment(selectedPaymentMethod.id!!)
        return isPaymentCompleted
    }

    private fun reset() {
//        binding.edtDonationId.text.clear()
//        binding.edtDonationEventName.text.clear()
        binding.edtDonationAmount.text.clear()
//        binding.imgDonationEventPhoto.setImageDrawable(null)
        binding.edtDonorName.requestFocus()
    }

//    private fun select() {
//        // TODO: Select file
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        launcher.launch(intent)
//    }

    private suspend fun setDonation() {
        val v = DonationEventDetail(
            donationEventId = id,
            donorName = binding.edtDonorName.text.toString().trim(),
//            donationEventName = binding.edtDonationEventName.text.toString().trim(),
            donationAmount = binding.edtDonationAmount.text.toString().toIntOrNull() ?: 2,
//            donationEventPhoto = binding.imgDonationEventPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(v)
        if (err != "") {
            errorDialog(err)
            return
        }

        // TODO: Set (insert)
//        vm.set(v)
        vm.setDonations(v)

//        nav.navigateUp()
    }

    private fun confirmPayment(paymentMethodId: String) {
        binding.btnDonate.isEnabled = false
        val applicationContext = requireActivity().application  // get context

        val paymentCollection = Firebase.firestore
            .collection("customers").document(currentUserT?.uid ?: "")
            .collection("payments")

        // Add a new document with a generated ID
        paymentCollection.add(
            hashMapOf(
                "amount" to amount,
//                "amount" to (binding.edtDonationAmount.toString().toIntOrNull()!! * 100),
                "currency" to "myr"
            )
        )
            .addOnSuccessListener { documentReference ->
                Log.d("payment", "DocumentSnapshot added with ID: ${documentReference.id}")
                documentReference.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("payment", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("payment", "Current data: ${snapshot.data}")
                        val clientSecret = snapshot.data?.get("client_secret")
                        Log.d("payment", "Create paymentIntent returns $clientSecret")
                        clientSecret?.let {
                            stripe.confirmPayment(
                                this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                    paymentMethodId,
                                    (it as String)
                                )
                            )

//                            checkoutSummary.text = "Thank you for your payment"
                            binding.checkoutSummary.text = "Payment Successful."
                            isPaymentCompleted = true
                            Toast.makeText(applicationContext, "Payment Done!!", Toast.LENGTH_LONG)
                                .show()

                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        binding.btnDonate.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                binding.btnDonate.isEnabled = true
            }
    }

    private fun setupPaymentSession() {
        val applicationContext = requireActivity().application  // get context
        // Setup Customer Session
//        CustomerSession.initCustomerSession(this, FirebaseEphemeralKeyProvider())
        CustomerSession.initCustomerSession(applicationContext!!, FirebaseEphemeralKeyProvider())
        // Setup a payment session
        paymentSession = PaymentSession(
            this, PaymentSessionConfig.Builder()
                .setShippingInfoRequired(false)
                .setShippingMethodsRequired(false)
                .setBillingAddressFields(BillingAddressFields.None)
                .setShouldShowGooglePay(true)
                .build()
        )

        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    Log.d("PaymentSession", "PaymentSession has changed: $data")
                    Log.d(
                        "PaymentSession",
                        "${data.isPaymentReadyToCharge} <> ${data.paymentMethod}"
                    )

                    if (data.isPaymentReadyToCharge) {
                        Log.d("PaymentSession", "Ready to charge");
                        binding.btnDonate.isEnabled = true

                        data.paymentMethod?.let {
                            Log.d("PaymentSession", "PaymentMethod $it selected")
                            binding.paymentmethod.text =
                                "${it.card?.brand} card ends with ${it.card?.last4}"
                            selectedPaymentMethod = it
                        }
                    }
                }

                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    Log.d("PaymentSession", "isCommunicating $isCommunicating")
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    Log.e("PaymentSession", "onError: $errorCode, $errorMessage")
                }
            }
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val applicationContext = requireActivity().application  // get context
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                currentUserT = FirebaseAuth.getInstance().currentUser

                Log.d("Login", "User ${currentUserT?.displayName} has signed in.")
                Toast.makeText(
                    applicationContext,
                    "Welcome ${currentUserT?.displayName}",
                    Toast.LENGTH_SHORT
                ).show()
//                showUI()
            } else {
                Log.d("Login", "Signing in failed!")
                Toast.makeText(
                    applicationContext,
                    response?.error?.message ?: "Sign in failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
        }
    }
}

