package com.example.suitcase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private var itemDetails: Item_Model? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve item details from intent
        itemDetails = intent.getSerializableExtra("itemDetails") as? Item_Model

        // Initialize and show the map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_frag) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // This method is triggered when the map is ready to be used.
        // You can add your map-related logic here.
        // For example, you can set markers, move the camera, etc.
        // Enable zoom controls
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Enable zoom gestures
        googleMap.uiSettings.isZoomGesturesEnabled = true

        // Example: Set a marker on a location and handle user interaction
        googleMap.setOnMapClickListener { latLng ->
            // Handle the selected location
            // Combine item details and location for SMS
            val message = "Check out this item: ${itemDetails?.item_name}, Price: ${itemDetails?.item_price}\nLocation: $latLng"

            // Send SMS with the message
            sendSms(message)
            finish()
        }
    }

    private fun sendSms(message: String) {
        // Add your SMS sending logic here using Intent.ACTION_SENDTO
        // Example:
        val smsIntent = Intent(Intent.ACTION_SENDTO)
        smsIntent.data = Uri.parse("smsto:")  // Specify the recipient's number if needed
        smsIntent.putExtra("sms_body", message)

        // Start the SMS application
        startActivity(smsIntent)
    }
}
