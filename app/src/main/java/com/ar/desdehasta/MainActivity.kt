package com.ar.desdehasta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        private const val FROM_REQUEST_CODE_AUTOCOMPLETE = 1
        private const val TO_REQUEST_CODE_AUTOCOMPLETE = 2
        private const val TAG = "MainActivity"
    }
    private lateinit var mMap: GoogleMap
    private lateinit var mOrigenLatLng: LatLng
    private lateinit var mDestinoLatLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpMap()
        setupPlaces()

    }
    private fun setUpMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupPlaces(){
        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.api_key))

        btnOrigen.setOnClickListener {
            startAutoComplete(FROM_REQUEST_CODE_AUTOCOMPLETE)
        }
        btnDestino.setOnClickListener {
            startAutoComplete(TO_REQUEST_CODE_AUTOCOMPLETE)
        }
    }
    private  fun startAutoComplete(requestCode: Int ){
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FROM_REQUEST_CODE_AUTOCOMPLETE) {
            processAutoCompleteResult(resultCode,data ){ place ->
                tvOrigen.text= getString(R.string.label_origen, place.address)
                place.latLng?.let{
                    mOrigenLatLng=it
                    addMarker(mOrigenLatLng,getString(R.string.macador_origen))
                }
            }
            return
        }else if (requestCode== TO_REQUEST_CODE_AUTOCOMPLETE){
            processAutoCompleteResult(resultCode,data){
                    place ->
                tvDestino.text= getString(R.string.label_destino, place.address)
                place.latLng?.let{
                    mDestinoLatLng=it
                    addMarker(mDestinoLatLng,getString(R.string.marcador_destino))

                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun processAutoCompleteResult(resultCode: Int,data: Intent?,
        callback:(Place) ->Unit){
        Log.i(TAG,"processAutoCompleteResult(resultCode=$resultCode)")
        when (resultCode) {
            Activity.RESULT_OK -> {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.i(TAG," Datos de la API:-- $place")
                   callback(place)

                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                // TODO: Handle the error.
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    status.statusMessage?.let {
                            message -> Log.i(TAG,message)
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val ortBelgrano = LatLng(-34.5497773,-58.4541588)
        addMarker(ortBelgrano, "ORT Belgrano")
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ortBelgrano))
    }

    private fun addMarker(latLng: LatLng, title:String){

            val markerOptions=MarkerOptions()
                .position(latLng)
                .title(title)

        mMap.addMarker(markerOptions)

    }
}