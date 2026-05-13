package com.raithavarta.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

object FirestoreUtils {
    fun uploadInitialTips() {
        val db = FirebaseFirestore.getInstance()
        val tips = listOf(
            mapOf(
                "cropType" to "Wheat",
                "title" to "Use Certified Seeds",
                "descriptionKannada" to "ಪ್ರಮಾಣಿತ ಬೀಜಗಳನ್ನು ಬಳಸುವುದರಿಂದ ಬೆಳೆ ಉತ್ತಮವಾಗುತ್ತದೆ.",
                "imageUrl" to "https://images.unsplash.com/photo-1501004318641-b39e6451bec6",
                "timestamp" to 2
            ),
            mapOf(
                "cropType" to "Wheat",
                "title" to "Balanced Fertilization",
                "descriptionKannada" to "ಸರಿಯಾದ ಪ್ರಮಾಣದಲ್ಲಿ ಗೊಬ್ಬರ ಬಳಸಿ ಬೆಳವಣಿಗೆ ಹೆಚ್ಚಿಸಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1586773860418-d37222d8fce3",
                "timestamp" to 3
            ),
            mapOf(
                "cropType" to "Maize",
                "title" to "Good Drainage",
                "descriptionKannada" to "ಮಣ್ಣಿನಲ್ಲಿ ನೀರು ನಿಲ್ಲದಂತೆ ಉತ್ತಮ ನೀರಿನ ಹರಿವು ಇರಲಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1471193945509-9ad0617afabf",
                "timestamp" to 4
            ),
            mapOf(
                "cropType" to "Maize",
                "title" to "Spacing Between Plants",
                "descriptionKannada" to "ಸಸ್ಯಗಳ ನಡುವೆ ಸರಿಯಾದ ಅಂತರ ಕಾಪಾಡಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1464226184884-fa280b87c399",
                "timestamp" to 5
            ),
            mapOf(
                "cropType" to "Maize",
                "title" to "Pest Control",
                "descriptionKannada" to "ಕೀಟಗಳನ್ನು ನಿಯಂತ್ರಿಸಲು ನಿಯಮಿತವಾಗಿ ಪರಿಶೀಲಿಸಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9",
                "timestamp" to 6
            ),
            mapOf(
                "cropType" to "Tomato",
                "title" to "Use Support Sticks",
                "descriptionKannada" to "ಟೊಮ್ಯಾಟೊ ಗಿಡಗಳಿಗೆ ಬೆಂಬಲ ಕಡ್ಡಿಗಳನ್ನು ಬಳಸಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1518977676601-b53f82aba655",
                "timestamp" to 7
            ),
            mapOf(
                "cropType" to "Tomato",
                "title" to "Regular Watering",
                "descriptionKannada" to "ನಿಯಮಿತವಾಗಿ ನೀರು ಹಾಕಿ ಆದರೆ ಹೆಚ್ಚು ನೀರು ಕೊಡಬೇಡಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1492496913980-501348b61469",
                "timestamp" to 8
            ),
            mapOf(
                "cropType" to "Tomato",
                "title" to "Disease Monitoring",
                "descriptionKannada" to "ಎಲೆಗಳಲ್ಲಿ ಬಣ್ಣ ಬದಲಾವಣೆ ಕಂಡುಬಂದರೆ ತಕ್ಷಣ ಕ್ರಮ ತೆಗೆದುಕೊಳ್ಳಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1519681393784-d120267933ba",
                "timestamp" to 9
            ),
            mapOf(
                "cropType" to "Potato",
                "title" to "Soil Preparation",
                "descriptionKannada" to "ಮಣ್ಣನ್ನು ಚೆನ್ನಾಗಿ ಸಿದ್ಧಪಡಿಸಿ ನಂತರ ಬಿತ್ತನೆ ಮಾಡಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1589927986089-35812388d1f4",
                "timestamp" to 10
            ),
            mapOf(
                "cropType" to "Potato",
                "title" to "Avoid Water Logging",
                "descriptionKannada" to "ಹೆಚ್ಚು ನೀರು ನಿಂತರೆ ಕಂದಗಳು ಹಾಳಾಗುತ್ತವೆ.",
                "imageUrl" to "https://images.unsplash.com/photo-1471193945509-9ad0617afabf",
                "timestamp" to 11
            ),
            mapOf(
                "cropType" to "Potato",
                "title" to "Use Healthy Tubers",
                "descriptionKannada" to "ಆರೋಗ್ಯಕರ ಕಂದಗಳನ್ನು ಮಾತ್ರ ಬಳಸಿರಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1582515073490-dc7d1e2a47e5",
                "timestamp" to 12
            ),
            mapOf(
                "cropType" to "Onion",
                "title" to "Proper Irrigation",
                "descriptionKannada" to "ಸರಿಯಾದ ಸಮಯದಲ್ಲಿ ನೀರಾವರಿ ನೀಡುವುದು ಮುಖ್ಯ.",
                "imageUrl" to "https://images.unsplash.com/photo-1501004318641-b39e6451bec6",
                "timestamp" to 13
            ),
            mapOf(
                "cropType" to "Onion",
                "title" to "Weed Control",
                "descriptionKannada" to "ಕಳೆಗಳನ್ನು ತೆಗೆದುಹಾಕಿ ಉತ್ತಮ ಬೆಳವಣಿಗೆಗೆ ಸಹಾಯ ಮಾಡಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef",
                "timestamp" to 14
            ),
            mapOf(
                "cropType" to "Onion",
                "title" to "Harvest on Time",
                "descriptionKannada" to "ಸರಿಯಾದ ಸಮಯದಲ್ಲಿ ಕಟಾವು ಮಾಡುವುದು ಮುಖ್ಯ.",
                "imageUrl" to "https://images.unsplash.com/photo-1492496913980-501348b61469",
                "timestamp" to 15
            ),
            mapOf(
                "cropType" to "Chilli",
                "title" to "Sunlight Requirement",
                "descriptionKannada" to "ಮೆಣಸಿನಕಾಯಿ ಬೆಳೆಗೆ ಸಾಕಷ್ಟು ಸೂರ್ಯಪ್ರಕಾಶ ಅಗತ್ಯ.",
                "imageUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9",
                "timestamp" to 16
            ),
            mapOf(
                "cropType" to "Chilli",
                "title" to "Pest Monitoring",
                "descriptionKannada" to "ಕೀಟ ಹಾನಿ ಕಂಡುಬಂದರೆ ತಕ್ಷಣ ಚಿಕಿತ್ಸೆ ನೀಡಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1519681393784-d120267933ba",
                "timestamp" to 17
            ),
            mapOf(
                "cropType" to "Chilli",
                "title" to "Organic Fertilizers",
                "descriptionKannada" to "ಜೈವಿಕ ಗೊಬ್ಬರಗಳನ್ನು ಬಳಸುವುದು ಉತ್ತಮ.",
                "imageUrl" to "https://images.unsplash.com/photo-1586773860418-d37222d8fce3",
                "timestamp" to 18
            ),
            mapOf(
                "cropType" to "Cotton",
                "title" to "Seed Treatment",
                "descriptionKannada" to "ಬೀಜ ಚಿಕಿತ್ಸೆ ಮಾಡುವುದು ರೋಗ ತಡೆಗಟ್ಟಲು ಸಹಾಯಕ.",
                "imageUrl" to "https://images.unsplash.com/photo-1501004318641-b39e6451bec6",
                "timestamp" to 19
            ),
            mapOf(
                "cropType" to "Cotton",
                "title" to "Proper Spacing",
                "descriptionKannada" to "ಸಸ್ಯಗಳ ನಡುವೆ ಸರಿಯಾದ ಅಂತರ ಇರಲಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1464226184884-fa280b87c399",
                "timestamp" to 20
            ),
            mapOf(
                "cropType" to "Cotton",
                "title" to "Pest Control",
                "descriptionKannada" to "ಬೋಲ್ ವರ್ಮ್ ಕೀಟಗಳನ್ನು ನಿಯಂತ್ರಿಸಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9",
                "timestamp" to 21
            ),
            mapOf(
                "cropType" to "Sugarcane",
                "title" to "Proper Irrigation",
                "descriptionKannada" to "ಸಕ್ಕರೆಕಬ್ಬಿಗೆ ನಿಯಮಿತವಾಗಿ ನೀರು ಅಗತ್ಯ.",
                "imageUrl" to "https://images.unsplash.com/photo-1471193945509-9ad0617afabf",
                "timestamp" to 22
            ),
            mapOf(
                "cropType" to "Sugarcane",
                "title" to "Use Healthy Setts",
                "descriptionKannada" to "ಆರೋಗ್ಯಕರ ಕಬ್ಬಿನ ತುಂಡುಗಳನ್ನು ಬಳಸಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1501004318641-b39e6451bec6",
                "timestamp" to 23
            ),
            mapOf(
                "cropType" to "Sugarcane",
                "title" to "Weed Management",
                "descriptionKannada" to "ಕಳೆಗಳನ್ನು ನಿಯಂತ್ರಿಸಿ ಉತ್ತಮ ಬೆಳವಣಿಗೆ ಪಡೆಯಿರಿ.",
                "imageUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef",
                "timestamp" to 24
            ),
            mapOf(
                "cropType" to "Wheat",
                "title" to "Proper Sowing Time",
                "descriptionKannada" to "ಸರಿಯಾದ ಸಮಯದಲ್ಲಿ ಬಿತ್ತನೆ ಮಾಡಿದರೆ ಉತ್ತಮ ಉತ್ಪಾದನೆ ಸಿಗುತ್ತದೆ.",
                "imageUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef",
                "timestamp" to 25
            ),
        )

        for (tip in tips) {
            db.collection("tips").add(tip)
                .addOnSuccessListener { Log.d("Firestore", "Tip added: ${tip["title"]}") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error adding tip", e) }
        }
    }

    fun uploadSuccessStories() {
        val db = FirebaseFirestore.getInstance()
        val stories = listOf(
            mapOf(
                "farmerName" to "ಶಿವಕುಮಾರ್ (Shivakumar)",
                "quoteKannada" to "ಮಣ್ಣಿನ ಪರೀಕ್ಷೆ ಮಾಡಿದ ನಂತರ ಸರಿಯಾದ ಗೊಬ್ಬರ ಬಳಸಿ ಇಳುವರಿ ಹೆಚ್ಚಾಯಿತು.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1544725176-7c40e5a2c9f9?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Wheat",
                "rating" to 5.0,
                "yieldIncrease" to "25%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಲಕ್ಷ್ಮಣ್ (Lakshman)",
                "quoteKannada" to "ಡ್ರಿಪ್ ನೀರಾವರಿ ಬಳಸಿ ನೀರಿನ ಖರ್ಚು ಕಡಿಮೆ ಮಾಡಿ ಉತ್ತಮ ಫಲಿತಾಂಶ ಕಂಡೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1502767089025-6572583495b0?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1471193945509-9ad0617afabf?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Maize",
                "rating" to 4.5,
                "yieldIncrease" to "20%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಮಂಜುನಾಥ್ (Manjunath)",
                "quoteKannada" to "ಕೀಟ ನಿಯಂತ್ರಣ ಕ್ರಮಗಳನ್ನು ಸರಿಯಾಗಿ ಅನುಸರಿಸಿ ಬೆಳೆ ನಷ್ಟ ತಪ್ಪಿಸಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Maize",
                "rating" to 5.0,
                "yieldIncrease" to "15%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಸುಮಿತ್ರಾ (Sumitra)",
                "quoteKannada" to "ಜೈವಿಕ ಕೃಷಿ ವಿಧಾನಗಳಿಂದ ಆರೋಗ್ಯಕರ ಬೆಳೆ ಪಡೆದಿದ್ದೇನೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1586773860418-d37222d8fce3?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Tomato",
                "rating" to 5.0,
                "yieldIncrease" to "30%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ರಾಜೇಶ್ (Rajesh)",
                "quoteKannada" to "ಬೆಳೆ ಪರಿವರ್ತನೆ ಮೂಲಕ ಮಣ್ಣಿನ ಗುಣಮಟ್ಟ ಸುಧಾರಿಸಿಕೊಂಡೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1464226184884-fa280b87c399?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Cotton",
                "rating" to 4.0,
                "yieldIncrease" to "10%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಗೀತಾ (Geetha)",
                "quoteKannada" to "ಸಮಯಕ್ಕೆ ಸರಿಯಾದ ನೀರಾವರಿ ನೀಡಿದರಿಂದ ಬೆಳೆ ಉತ್ತಮವಾಗಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1492496913980-501348b61469?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Tomato",
                "rating" to 5.0,
                "yieldIncrease" to "20%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಹನುಮಂತ (Hanumanth)",
                "quoteKannada" to "ಉತ್ತಮ ಬೀಜಗಳನ್ನು ಬಳಸಿದರಿಂದ ಉತ್ಪಾದನೆ ಹೆಚ್ಚಾಯಿತು.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Wheat",
                "rating" to 5.0,
                "yieldIncrease" to "35%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಕವಿತಾ (Kavitha)",
                "quoteKannada" to "ಕಳೆ ನಿಯಂತ್ರಣದಿಂದ ಬೆಳೆ ಬೆಳವಣಿಗೆ ವೇಗವಾಯಿತು.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Onion",
                "rating" to 4.5,
                "yieldIncrease" to "15%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಪ್ರಕಾಶ್ (Prakash)",
                "quoteKannada" to "ಹವಾಮಾನ ಮಾಹಿತಿ ಅನುಸರಿಸಿ ಬೆಳೆ ಹಾನಿ ತಪ್ಪಿಸಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1552374196-c4e7ffc6e126?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Sugarcane",
                "rating" to 5.0,
                "yieldIncrease" to "25%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಶೋಭಾ (Shobha)",
                "quoteKannada" to "ಮಲ್ಚಿಂಗ್ ವಿಧಾನದಿಂದ ಮಣ್ಣಿನ ತೇವಾಂಶ ಉಳಿಸಿಕೊಂಡೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1598514982901-9f6cfc9f5c3b?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Potato",
                "rating" to 4.0,
                "yieldIncrease" to "12%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಮಹೇಶ್ (Mahesh)",
                "quoteKannada" to "ಸಮಯಕ್ಕೆ ಸರಿಯಾದ ಕಟಾವು ಮಾಡಿದರಿಂದ ಉತ್ತಮ ಬೆಲೆ ಸಿಕ್ಕಿತು.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1492496913980-501348b61469?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Onion",
                "rating" to 5.0,
                "yieldIncrease" to "18%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ರೇಖಾ (Rekha)",
                "quoteKannada" to "ಜೈವಿಕ ಕೀಟನಾಶಕ ಬಳಸಿ ಬೆಳೆ ರಕ್ಷಿಸಿಕೊಂಡೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1581090700227-1e8e8c0f6c5b?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Chilli",
                "rating" to 4.5,
                "yieldIncrease" to "22%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ದೀಪಕ್ (Deepak)",
                "quoteKannada" to "ಸರಿಯಾದ ಅಂತರದಲ್ಲಿ ಸಸಿ ನೆಟ್ಟುದರಿಂದ ಬೆಳವಣಿಗೆ ಉತ್ತಮವಾಗಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1545996124-0501ebae84d0?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1464226184884-fa280b87c399?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Maize",
                "rating" to 5.0,
                "yieldIncrease" to "20%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಅನಿತಾ (Anitha)",
                "quoteKannada" to "ನೀರಿನ ಉಳಿತಾಯದಿಂದ ವೆಚ್ಚ ಕಡಿಮೆಯಾಯಿತು.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1471193945509-9ad0617afabf?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Rice",
                "rating" to 4.0,
                "yieldIncrease" to "15%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಸುರೇಶ್ (Suresh)",
                "quoteKannada" to "ಹೊಸ ತಂತ್ರಜ್ಞಾನ ಬಳಸಿ ಉತ್ಪಾದನೆ ಹೆಚ್ಚಿಸಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1502767089025-6572583495b0?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Wheat",
                "rating" to 5.0,
                "yieldIncrease" to "40%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಗಣೇಶ್ (Ganesh)",
                "quoteKannada" to "ಮಣ್ಣಿನ ಆರೋಗ್ಯ ಕಾಪಾಡುವುದರಿಂದ ಉತ್ತಮ ಫಲಿತಾಂಶ ಕಂಡೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1589927986089-35812388d1f4?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Potato",
                "rating" to 4.5,
                "yieldIncrease" to "25%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಪ್ರಿಯಾ (Priya)",
                "quoteKannada" to "ಸಸ್ಯಗಳಿಗೆ ಸಾಕಷ್ಟು ಸೂರ್ಯಪ್ರಕಾಶ ನೀಡುವುದರಿಂದ ಬೆಳವಣಿಗೆ ಉತ್ತಮವಾಗಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1502082553048-f009c37129b9?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Tomato",
                "rating" to 5.0,
                "yieldIncrease" to "30%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ವಿಜಯ್ (Vijay)",
                "quoteKannada" to "ಸರಿಯಾದ ಗೊಬ್ಬರ ಬಳಸಿ ಬೆಳೆ ಗುಣಮಟ್ಟ ಹೆಚ್ಚಿಸಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1586773860418-d37222d8fce3?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Wheat",
                "rating" to 4.0,
                "yieldIncrease" to "20%",
                "isVerified" to true
            ),
            mapOf(
                "farmerName" to "ಸುಜಾತಾ (Sujatha)",
                "quoteKannada" to "ನಿಯಮಿತವಾಗಿ ಹೊಲ ಪರಿಶೀಲನೆ ಮಾಡಿದರಿಂದ समस्याಗಳನ್ನು ಬೇಗ ಗುರುತಿಸಿದೆ.",
                "profilePicUrl" to "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?auto=format&fit=crop&q=80&w=150",
                "beforeAfterPicUrl" to "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&q=80&w=150",
                "cropType" to "Wheat",
                "rating" to 5.0,
                "yieldIncrease" to "15%",
                "isVerified" to true
            )
        )

        for (story in stories) {
            db.collection("success_stories").add(story)
                .addOnSuccessListener { Log.d("Firestore", "Story added: ${story["farmerName"]}") }
                .addOnFailureListener { e -> Log.e("Firestore", "Error adding story", e) }
        }
    }
}
