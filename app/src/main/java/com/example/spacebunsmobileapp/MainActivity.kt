package com.example.spacebunsmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.ActivityMainBinding
import com.stripe.android.*
import kotlinx.coroutines.launch

var RC_SIGN_IN = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nav by lazy {
        supportFragmentManager.findFragmentById(R.id.host)!!.findNavController()
    }
        private val pvm: ProductViewModel by viewModels()

//    private var currentUser: FirebaseUser? = null
//    private lateinit var paymentSession: PaymentSession
//    private lateinit var selectedPaymentMethod: PaymentMethod
//
//    private val stripe: Stripe by lazy {
//        Stripe(
//            applicationContext,
//            PaymentConfiguration.getInstance(applicationContext).publishableKey
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        PaymentConfiguration.init(applicationContext,"pk_test_51LWbMdCffsYKlOgZJQu5OlKPoEe978IKUuzKJjwcW0ZDwb46LaVT09D3svdGTvaWoavzzCTqo9fM7DztaOL8dSGu00MhmVJabg")

//        // move from one fragment to another using the bottom nav
        binding.bottomNavBar.setupWithNavController(nav)
//
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
//
//        binding.payButton.setOnClickListener {
//            confirmPayment(selectedPaymentMethod.id!!)
//        }
//
//        binding.paymentmethod.setOnClickListener {
//            // Create the customer session and kick start the payment flow
//            paymentSession.presentPaymentMethodSelection()
//        }
//
//        showUI()

        // TODO(8): Auto login -> auth.loginFromPreferences(...)
        lifecycleScope.launch {
            pvm.loginFromPreferences(this@MainActivity)
        }

    }

//    private fun confirmPayment(paymentMethodId: String) {
//        binding.payButton.isEnabled = false
//
//        val paymentCollection = Firebase.firestore
//            .collection("customers").document(currentUser?.uid ?: "")
//            .collection("payments")
//
//        // Add a new document with a generated ID
//        paymentCollection.add(
//            hashMapOf(
//                "amount" to 2300,
//                "currency" to "myr"
//            )
//        )
//            .addOnSuccessListener { documentReference ->
//                Log.d("payment", "DocumentSnapshot added with ID: ${documentReference.id}")
//                documentReference.addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.w("payment", "Listen failed.", e)
//                        return@addSnapshotListener
//                    }
//
//                    if (snapshot != null && snapshot.exists()) {
//                        Log.d("payment", "Current data: ${snapshot.data}")
//                        val clientSecret = snapshot.data?.get("client_secret")
//                        Log.d("payment", "Create paymentIntent returns $clientSecret")
//                        clientSecret?.let {
//                            stripe.confirmPayment(
//                                this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
//                                    paymentMethodId,
//                                    (it as String)
//                                )
//                            )
//
////                            checkoutSummary.text = "Thank you for your payment"
//                            binding.checkoutSummary.text = "Payment Successful."
//                            Toast.makeText(applicationContext, "Payment Done!!", Toast.LENGTH_LONG)
//                                .show()
//                        }
//                    } else {
//                        Log.e("payment", "Current payment intent : null")
//                        binding.payButton.isEnabled = true
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w("payment", "Error adding document", e)
//                binding.payButton.isEnabled = true
//            }
//    }
//
//    private fun showUI() {
//        currentUser?.let {
//            binding.loginButton.visibility = View.INVISIBLE
//
//            binding.greeting.visibility = View.VISIBLE
//            binding.checkoutSummary.visibility = View.VISIBLE
//            binding.payButton.visibility = View.VISIBLE
//            binding.paymentmethod.visibility = View.VISIBLE
//
//            binding.greeting.text = "Hello ${it.displayName}"
//
//            setupPaymentSession()
//        } ?: run {
//            // User does not login
//            binding.loginButton.visibility = View.VISIBLE
//
//            binding.greeting.visibility = View.INVISIBLE
//            binding.checkoutSummary.visibility = View.INVISIBLE
//            binding.paymentmethod.visibility = View.INVISIBLE
//            binding.payButton.visibility = View.INVISIBLE
//            binding.payButton.isEnabled = false
//
//        }
//    }
//
//    private fun setupPaymentSession() {
//        // Setup Customer Session
//        CustomerSession.initCustomerSession(this, FirebaseEphemeralKeyProvider())
//        // Setup a payment session
//        paymentSession = PaymentSession(
//            this, PaymentSessionConfig.Builder()
//                .setShippingInfoRequired(false)
//                .setShippingMethodsRequired(false)
//                .setBillingAddressFields(BillingAddressFields.None)
//                .setShouldShowGooglePay(true)
//                .build()
//        )
//
//        paymentSession.init(
//            object : PaymentSession.PaymentSessionListener {
//                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
//                    Log.d("PaymentSession", "PaymentSession has changed: $data")
//                    Log.d(
//                        "PaymentSession",
//                        "${data.isPaymentReadyToCharge} <> ${data.paymentMethod}"
//                    )
//
//                    if (data.isPaymentReadyToCharge) {
//                        Log.d("PaymentSession", "Ready to charge");
//                        binding.payButton.isEnabled = true
//
//                        data.paymentMethod?.let {
//                            Log.d("PaymentSession", "PaymentMethod $it selected")
//                            binding.paymentmethod.text =
//                                "${it.card?.brand} card ends with ${it.card?.last4}"
//                            selectedPaymentMethod = it
//                        }
//                    }
//                }
//
//                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
//                    Log.d("PaymentSession", "isCommunicating $isCommunicating")
//                }
//
//                override fun onError(errorCode: Int, errorMessage: String) {
//                    Log.e("PaymentSession", "onError: $errorCode, $errorMessage")
//                }
//            }
//        )
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
//
//            if (resultCode == Activity.RESULT_OK) {
//                currentUser = FirebaseAuth.getInstance().currentUser
//
//                Log.d("Login", "User ${currentUser?.displayName} has signed in.")
//                Toast.makeText(
//                    applicationContext,
//                    "Welcome ${currentUser?.displayName}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                showUI()
//            } else {
//                Log.d("Login", "Signing in failed!")
//                Toast.makeText(
//                    applicationContext,
//                    response?.error?.message ?: "Sign in failed",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } else {
//            paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }
}