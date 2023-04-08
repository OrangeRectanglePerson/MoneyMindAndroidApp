package com.example.moneymind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.moneymind.ViewModels.GraphScreenViewModel
import com.example.moneymind.ViewModels.TransactionCategoryViewModel
import com.example.moneymind.ViewModels.TransactionLogViewModel
import com.example.moneymind.data.GraphScreenState
import com.example.moneymind.data.TransactionCategoriesState
import com.example.moneymind.data.TransactionLogsState
import com.example.moneymind.ui.Extras.getSerializable
import com.example.moneymind.ui.theme.CSProjectTheme

class NavHostMainActivity : ComponentActivity() {

    companion object{
        val CHANNEL_ID = "MONEY_MIND_CHANNEL_ID"
        var notification_ID = 0;
    }

    var TLVM : TransactionLogViewModel = TransactionLogViewModel()
    var TCVM : TransactionCategoryViewModel = TransactionCategoryViewModel()
    var GSVM : GraphScreenViewModel = GraphScreenViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {

        TCVM.changeTransactionCategoriesState(
            this.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE)
                .getSerializable("categoriesJSON", TransactionCategoriesState::class.java)
        )

        TLVM.changeTransactionLogsState(
            this.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE)
                .getSerializable("transactionsJSON", TransactionLogsState::class.java)
        )

        GSVM.setGraphScreenState(
            this.getSharedPreferences("MoneyMindApp", Context.MODE_PRIVATE)
                .getSerializable("gssJSON", GraphScreenState::class.java)
        )

        super.onCreate(savedInstanceState)
        setContent {
            CSProjectTheme {
                MainApp(
                    transactionLogsViewModel = TLVM,
                    transactionCategoriesViewModel = TCVM,
                    graphScreenViewModel = GSVM
                )
            }
        }

        //notification stuff
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Money Mind Notification Channel"
            val descriptionText = "Notification Channel for Money Mind App"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}