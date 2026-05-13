package com.raithavarta.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppUtil {
    fun openWhatsAppChat(context: Context, phoneNumber: String, productName: String) {
        try {
            // Remove any non-digit characters except the leading '+'
            val formattedNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
            
            // Pre-fill a message
            val message = "Hello, I am interested in your product: $productName"
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber&text=${Uri.encode(message)}")
            
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.whatsapp")
            
            // Check if WhatsApp is installed before launching
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Fallback to browser if WhatsApp app is not installed
                val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(browserIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}
