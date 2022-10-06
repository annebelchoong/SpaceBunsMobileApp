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
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.RC_SIGN_IN
import com.example.spacebunsmobileapp.databinding.FragmentCheckoutBinding
import com.example.spacebunsmobileapp.util.FirebaseEphemeralKeyProvider
import com.example.spacebunsmobileapp.util.StripeFactory
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields


class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private val nav by lazy { findNavController() }
    private var currentUserT: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var paymentSession: PaymentSession
    private lateinit var selectedPaymentMethod: PaymentMethod
//    private var applicationContext: Context? = null

//    private val vm: CheckoutViewModel by activityViewModels()
//    private val applicationContext = vm.applicationContext

//    private val applicationContext = context

//    private val stripe: Stripe by lazy {
//        Stripe(
//            applicationContext!!,
//            PaymentConfiguration.getInstance(applicationContext).publishableKey
//        )
//    }

    private val stripe: Stripe by lazy {
        StripeFactory(requireContext()).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val applicationContext = requireActivity().application  // get context

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51LWbMdCffsYKlOgZJQu5OlKPoEe978IKUuzKJjwcW0ZDwb46LaVT09D3svdGTvaWoavzzCTqo9fM7DztaOL8dSGu00MhmVJabg"
        )

//        binding.loginButton.setOnClickListener {
//            // login to firebase
//            val providers = arrayListOf(
//                AuthUI.IdpConfig.EmailBuilder().build()
//            )
//
//            startActivityForResult(
//                AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setAvailableProviders(providers)
//                    .build(),
//                RC_SIGN_IN
//            )
//        }

        binding.payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        binding.paymentmethod.setOnClickListener {
            // Create the customer session and kick start the payment flow
            paymentSession.presentPaymentMethodSelection()
        }

        showUI()

        return binding.root
    }

    private fun confirmPayment(paymentMethodId: String) {
        binding.payButton.isEnabled = false
        val applicationContext = requireActivity().application  // get context

        val paymentCollection = Firebase.firestore
            .collection("customers").document(currentUserT?.uid ?: "")
            .collection("payments")

        // Add a new document with a generated ID
        paymentCollection.add(
            hashMapOf(
                "amount" to 2300,
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
                            Toast.makeText(applicationContext, "Payment Done!!", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        binding.payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                binding.payButton.isEnabled = true
            }
    }

    private fun showUI() {
        currentUserT?.let {
            binding.loginButton.visibility = View.INVISIBLE

            binding.greeting.visibility = View.VISIBLE
            binding.checkoutSummary.visibility = View.VISIBLE
            binding.payButton.visibility = View.VISIBLE
            binding.paymentmethod.visibility = View.VISIBLE

            binding.greeting.text = "Hello ${it.displayName}"

            setupPaymentSession()
        } ?: run {
            // User does not login
            binding.loginButton.visibility = View.VISIBLE

            binding.greeting.visibility = View.INVISIBLE
            binding.checkoutSummary.visibility = View.INVISIBLE
            binding.paymentmethod.visibility = View.INVISIBLE
            binding.payButton.visibility = View.INVISIBLE
            binding.payButton.isEnabled = false

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
                        binding.payButton.isEnabled = true

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
                showUI()
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