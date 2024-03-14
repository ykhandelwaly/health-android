package org.simple.clinic

import android.annotation.SuppressLint
import android.app.Activity
import android.os.StrictMode
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.tspoon.traceur.Traceur
import io.github.inflationx.viewpump.ViewPump
import org.simple.clinic.activity.SimpleActivityLifecycleCallbacks
import org.simple.clinic.di.AppComponent
import org.simple.clinic.di.AppModule
import org.simple.clinic.di.DaggerDebugAppComponent
import org.simple.clinic.di.DebugAppComponent
import org.simple.clinic.main.TheActivity
import org.simple.clinic.util.AppSignature
import org.simple.clinic.widgets.ProxySystemKeyboardEnterToImeOption
import timber.log.Timber
import javax.inject.Inject

// Using SuppressLint to ignore the "Registered" warning since this class is for debug purposes.
@SuppressLint("Registered")
class DebugClinicApp : ClinicApp() {

    // NetworkFlipperPlugin is injected to enable network debugging.
    // This instance will be shared with the OkHttp interceptor for network logging.
    @Inject
    lateinit var networkFlipperPlugin: NetworkFlipperPlugin

    // The signature variable will hold app signature information for security debugging purposes.
    private lateinit var signature: AppSignature

    // Companion object to allow static access to the appComponent from other parts of the app.
    companion object {
        fun appComponent(): DebugAppComponent {
            return appComponent as DebugAppComponent
        }
    }

    // The onCreate method initializes debugging tools and the app environment.
    override fun onCreate() {
        // Adding strict mode checks to catch accidental disk or network access on the main thread.
        addStrictModeChecks()

        // Enabling Traceur for better stack trace logging.
        Traceur.enableLogging()
        super.onCreate()

        // Initializing SoLoader for native library loading.
        SoLoader.init(this, false)

        // Inject dependencies into this class.
        appComponent().inject(this)

        // Setup Flipper plugins for debugging.
        setupFlipper()

        // Planting a Timber DebugTree to enable logging.
        Timber.plant(Timber.DebugTree())

        // Showing a debug notification for quick access to debugging features.
        showDebugNotification()

        // Initializing ViewPump for intercepting and modifying views at runtime.
        ViewPump.init(ViewPump.builder()
            .addInterceptor(ProxySystemKeyboardEnterToImeOption())
            .build())

        // Capturing the application's signature for security debugging.
        signature = AppSignature(this)
    }

    // Configures and adds various Flipper plugins for debugging different components of the app.
    private fun setupFlipper() {
        // Obtaining the current context for plugin initialization.
        val context = this

        // Getting an instance of AndroidFlipperClient and adding plugins.
        with(AndroidFlipperClient.getInstance(this)) {
            // Adding the InspectorFlipperPlugin for UI inspection.
            addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))

            // Adding the NetworkFlipperPlugin for network request inspection.
            addPlugin(networkFlipperPlugin)

            // Creating and adding the DatabasesFlipperPlugin for database inspection.
            val databasePlugin = DatabasesFlipperPlugin(context)
            addPlugin(databasePlugin)

            // Adding the SharedPreferencesFlipperPlugin for preferences inspection.
            addPlugin(SharedPreferencesFlipperPlugin(context, "${context.packageName}_preferences"))

            // Starting the Flipper client.
            start()
        }
    }

    // Shows a debug notification with app signature information when TheActivity is started.
    private fun showDebugNotification() {
        // Registering activity lifecycle callbacks to show and hide the debug notification.
        registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks() {
            override fun onActivityStarted(activity: Activity) {
                // Checking if the activity is TheActivity to show the notification.
                if (activity is TheActivity) {
                    DebugNotification.show(activity, signature.appSignatures)
                }
            }

            override fun onActivityStopped(activity: Activity) {
                // Checking if the activity is TheActivity to stop the notification.
                if (activity is TheActivity) {
                    DebugNotification.stop(activity)
                }
            }
        })
    }

    // Builds and returns the Dagger graph for dependency injection.
    override fun buildDaggerGraph(): AppComponent {
        // Using Dagger to build the app component with AppModule.
        return DaggerDebugAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    // Configures StrictMode policies for thread and VM to catch common mistakes.
    private fun addStrictModeChecks() {
        // Configuring thread policy to detect all violations and log them.
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                // This policy will cause an app crash on network access on the main thread.
                .penaltyDeathOnNetwork()
                .build()
        )

        // Configuring VM policy to detect leaked resources and log them.
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .build()
        )
    }
}
