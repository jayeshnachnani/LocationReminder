package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.udacity.project4.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragment
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.SelectLocationFragmentDirections

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    lateinit var login: TextView
    private val viewModel by viewModels<LoginViewModel>()

    companion object {
        const val TAG = "MainActivity"
        const val SIGN_IN_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        observeAuthenticationState()
        login = findViewById(R.id.auth_button)
        login.text = "FirstLogin"
        login.setOnClickListener {
            launchSignInFlow()
            // TODO call launchSignInFlow when authButton is clicked
        }
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google

//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email or Google account.
        // If users choose to register with their email,
        // they will need to create a password as well.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()

            // This is where you can provide more ways for users to register and
            // sign in.
        )

        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            AuthenticationActivity.SIGN_IN_REQUEST_CODE
        )
    }

    private fun observeAuthenticationState() {
        //val factToDisplay = viewModel.getFactToDisplay(requireContext())


        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            // TODO 1. Use the authenticationState variable you just added
            // in LoginViewModel and change the UI accordingly.
            when (authenticationState) {
                // TODO 2.  If the user is logged in,
                // you can customize the welcome message they see by
                // utilizing the getFactWithPersonalization() function provided
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                   // binding.welcomeText.text = getFactWithPersonalization(factToDisplay)
                    /*login.text = getString(R.string.logout_text)
                    login.setOnClickListener {
                        // TODO implement logging out user in next step
                    }*/
                    val reminderActivityIntent =
                    Intent(applicationContext, RemindersActivity::class.java)
                    startActivity(reminderActivityIntent)

                    //NavigationCommand.To(
                        //AuthenticationActivityDirections.actionAuthenticationActivityToReminderDescriptionActivity())

                    //val reminderActivityIntent =
                        //Intent(applicationContext, RemindersActivity::class.java)

                    //startActivity(reminderActivityIntent)
                    //NavigationCommand.To()

                }
                else -> {
                    // TODO 3. Lastly, if there is no logged-in user,
                    // auth_button should display Login and
                    // launch the sign in screen when clicked.
                    //binding.welcomeText.text = factToDisplay

                    //login.text = getString(R.string.login_button_text)
                    login.setOnClickListener {
                        launchSignInFlow()
                    }
                }
            }
        })
    }
}
