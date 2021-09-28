package com.ar.desdehasta

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.akexorcist.googledirection.util.execute
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        private const val FROM_REQUEST_CODE_AUTOCOMPLETE = 1
        private const val TO_REQUEST_CODE_AUTOCOMPLETE = 2
        private const val TAG = "MainActivity"
    }
    private lateinit var googleMap: GoogleMap
    private lateinit var mOrigenLatLng: LatLng
    private lateinit var mDestinoLatLng: LatLng
    private var mMarkerOrigen: Marker? = null
    private var mMarkerDestino: Marker? = null


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
                    setMarkerFrom(mOrigenLatLng)
                }
            }
            return
        }else if (requestCode== TO_REQUEST_CODE_AUTOCOMPLETE){
            processAutoCompleteResult(resultCode,data){
                    place ->
                tvDestino.text= getString(R.string.label_destino, place.address)
                place.latLng?.let{
                    mDestinoLatLng=it
                  //  setMarkerTo(mDestinoLatLng)


                    GoogleDirectionConfiguration.getInstance().isLogEnabled = BuildConfig.DEBUG
                    GoogleDirection.withServerKey(getString(R.string.api_key))
                        .from(mOrigenLatLng)
                        .to(mDestinoLatLng)
                        .transportMode(TransportMode.BICYCLING)
                        .execute(
                            onDirectionSuccess = { direction -> onDirectionSuccess(direction) },
                            onDirectionFailure = { t -> onDirectionFailure(t) }
                        )

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
        this.googleMap = googleMap
        //zoom
        this.googleMap.setMinZoomPreference(15f)
        this.googleMap.setMaxZoomPreference(20f)

        // Add a marker in Sydney and move the camera
        //val ortBelgrano = LatLng(-34.5497773,-58.4541588)
        //addMarker(ortBelgrano, "ORT Belgrano")
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(ortBelgrano))
    }

    private fun addMarker(latLng: LatLng, title:String): Marker{

            val markerOptions=MarkerOptions()
                .position(latLng)
                .title(title)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        return googleMap.addMarker(markerOptions)

    }
    private fun setMarkerFrom(latLng: LatLng){

        mMarkerOrigen?.remove()
        mMarkerDestino?.remove()
        mMarkerOrigen= addMarker(latLng,getString(R.string.macador_origen))

    }
    private fun setMarkerTo(latLng: LatLng){
        mMarkerDestino?.remove()

        mMarkerDestino= addMarker(latLng,getString(R.string.macador_origen))

    }


    private fun onDirectionSuccess(direction: Direction?) {
        direction?.let {
           // showSnackbar(getString(R.string.success_with_status, direction.status))


            if (direction.isOK) {
                val route = direction.routeList[0]
                mMarkerOrigen?.remove()
                mMarkerDestino?.remove()

                googleMap?.addMarker(MarkerOptions().position(mOrigenLatLng))
                googleMap?.addMarker(MarkerOptions().position(mDestinoLatLng))
                val directionPositionList = route.legList[0].directionPoint
                googleMap?.addPolyline(
                    DirectionConverter.createPolyline(
                        this,
                        directionPositionList,
                        5,
                        Color.BLUE
                    )
                )
                lldistancia.text="Distancia " + (route.totalDistance/ 1000).toString() + " Kmts "
                lltiempo.text= "Tiempo: " + (route.totalDuration / 60).toString() + " Min"

                Log.i(TAG," ......cesarcesarcesar :-- ${ route.legList[0].distance}")

                setCameraWithCoordinationBounds(route)
                //binding.buttonRequestDirection.visibility = View.GONE
            } else {
                showSnackbar(direction.status)
            }
        } ?: run {
            showSnackbar(getString(R.string.success_with_empty))
        }
    }

    private fun onDirectionFailure(t: Throwable) {
        showSnackbar(t.message)
    }
    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    private fun showSnackbar(message: String?) {
        message?.let {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }
}