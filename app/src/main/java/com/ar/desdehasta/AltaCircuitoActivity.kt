package com.ar.desdehasta

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.googledirection.BuildConfig
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.akexorcist.googledirection.util.execute
import com.ar.desdehasta.pojo.Circuito
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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_alta_circuito.*
import java.util.*


class AltaCircuitoActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        private const val FROM_REQUEST_CODE_AUTOCOMPLETE = 1
        private const val TO_REQUEST_CODE_AUTOCOMPLETE = 2
        private const val TAG = "AltaCircuitoActivity"
    }
    private lateinit var googleMap: GoogleMap
    private var mOrigenLatLng: LatLng? = null
    private var mDestinoLatLng: LatLng? = null
    private var mMarkerOrigen: Marker? = null
    private var mMarkerDestino: Marker? = null
    private var distancia: Double = .0
    private var tiempo: Double = .0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_circuito)

        setUpMap()
        setupPlaces()

        Log.i("Prueba", "oncreate")

    }
    fun agregar(v:View){
        Log.i("Prueba", "AGregar")

        if(validar()){
            Toast.makeText(this,"ingreso ok",Toast.LENGTH_SHORT).show()

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Circuito")
            var uid: String =UUID.randomUUID().toString()
            var nombre: String = ti_nombreCircuito.text.toString()
            val lat_ori: Double = mOrigenLatLng!!.latitude
            val lng_ori: Double = mOrigenLatLng!!.longitude
            val lat_dest: Double = mDestinoLatLng!!.latitude
            val lng_dest: Double = mDestinoLatLng!!.longitude

            Log.i("Prueba", distancia.toString() + tiempo.toString())

            val data: Circuito= Circuito(uid,nombre,distancia,tiempo, lat_ori, lng_ori,lat_dest,lng_dest)
            //myRef.push().setValue(data)
            myRef.child(uid).setValue(data);


            val intent = Intent(v.context, ListadoCircuitosActivity::class.java)
            startActivityForResult(intent, 0)
                //myRef.push().child("origen").setValue(mOrigenLatLng.toString())
            //myRef.push().child("destino").setValue(mDestinoLatLng.toString())
        }
    }
    private fun validar(): Boolean {
        var retorno=true
        if(ti_nombreCircuito.text.toString().isEmpty() ){
            ti_nombreCircuito.error = "Debe indicar un nombre de circuito!"
            retorno=false
        }
        if(tvOrigen.toString().isEmpty() ){
            Log.i("Prueba", "origen")
            tvOrigen.requestFocus()
            tvOrigen.setError("Debe indicar un Origen de circuito!")
            retorno=false
        }
        if(tvDestino.toString().isEmpty() ){
            Log.i("Prueba", "origen")

            tvDestino.requestFocus()
            tvDestino.setError("Debe indicar un Destino de circuito!")
            retorno=false
        }
        return retorno


    }
    private fun setUpMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { googleMap ->
            this.googleMap = googleMap
        }
       /*
       val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        */
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
    private  fun  startAutoComplete(requestCode: Int ){
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS )

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        googleMap.clear()
        if (requestCode == FROM_REQUEST_CODE_AUTOCOMPLETE) {
            processAutoCompleteResult(resultCode,data ){ place ->
                tvOrigen.text= getString(R.string.label_origen, place.address)
                place.latLng?.let{
                    mOrigenLatLng=it
                    setMarkerFrom(it)
                }
            }
            return
        }else if (requestCode== TO_REQUEST_CODE_AUTOCOMPLETE){
            processAutoCompleteResult(resultCode,data){
                    place ->
                tvDestino.text= getString(R.string.label_destino, place.address)
                place.latLng?.let{
                    mDestinoLatLng=it
                    setMarkerTo(it)

                    GoogleDirectionConfiguration.getInstance().isLogEnabled = BuildConfig.DEBUG
                    GoogleDirection.withServerKey(getString(R.string.api_key))
                        .from(mOrigenLatLng!!)
                        .to(mDestinoLatLng!!)
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
        googleMap.getUiSettings().setCompassEnabled(true);


        val ortBelgrano = LatLng(-34.5497773,-58.4541588)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ortBelgrano))
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

                googleMap.addMarker(MarkerOptions().position(mOrigenLatLng!!))
                googleMap.addMarker(MarkerOptions().position(mDestinoLatLng!!))
                val directionPositionList = route.legList[0].directionPoint
                googleMap.addPolyline(
                    DirectionConverter.createPolyline(
                        this,
                        directionPositionList,
                        5,
                        Color.BLUE
                    )
                )

                distancia= (route.totalDistance/ 1000).toDouble()
                tiempo=  (route.totalDuration / 60).toDouble()

                lldistancia.text="Distancia " + distancia.toString() + " Kmts "
                lltiempo.text= "Tiempo: " +tiempo.toString() + " Min"

                //Log.i("Data distancia y tiempo"," ..... :-- ${ route.legList[0].distance}")

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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    private fun showSnackbar(message: String?) {
        message?.let {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }
}