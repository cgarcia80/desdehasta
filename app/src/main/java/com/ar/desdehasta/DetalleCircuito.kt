package com.ar.desdehasta

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class DetalleCircuito : AppCompatActivity() {

    companion object {
        private const val serverKey="AIzaSyATmeZ6gpP4X6B1jfCeItUYsMKQopTelMY"

    }


    private var circuito: Circuito? = null
    private var googleMap: GoogleMap? = null
    lateinit var origen:LatLng
    lateinit var destino:LatLng
    private var nombre: TextView? = null
    private var kilometros: TextView? = null
    private var tiempo: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_circuito)

        initViews()
        initValues()
        setUpMapa()
    }

    private fun setUpMapa() {
        origen = LatLng(circuito!!.latitude_ori, circuito!!.longitude_ori)
        destino = LatLng(circuito!!.latitude_des, circuito!!.longitude_des)

        (supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment).getMapAsync { googleMap ->
            this.googleMap = googleMap
        }
       // GoogleDirectionConfiguration.getInstance().isLogEnabled = BuildConfig.DEBUG
        GoogleDirection.withServerKey(serverKey)
            .from(origen)
            .to(destino)
            .transportMode(TransportMode.BICYCLING)
            .execute(
                onDirectionSuccess = { direction -> onDirectionSuccess(direction) },
                onDirectionFailure = { t -> onDirectionFailure(t) }
            )
    }


    private fun initViews() {
        nombre = findViewById<TextView>(R.id.tvNombrecircuito_detalle)
        kilometros = findViewById<TextView>(R.id.tvdistanciaDetalle)
        tiempo = findViewById<TextView>(R.id.tvtiempoDetalle)

    }

    private fun initValues() {
        circuito = intent.extras!!.getSerializable("Detalle Circuito") as Circuito?
        nombre?.setText(circuito!!.nombre)
        kilometros?.setText(circuito!!.kilometros.toString())
        tiempo?.setText(circuito!!.tiempo.toString())
    }

    private fun onDirectionSuccess(direction: Direction?) {
        direction?.let {
            //showSnackbar(getString(R.string.success_with_status, direction.status))
            if (direction.isOK) {
                val route = direction.routeList[0]


                googleMap?.addMarker(MarkerOptions().position(origen))
                googleMap?.addMarker(MarkerOptions().position(destino))

                val directionPositionList = route.legList[0].directionPoint
                googleMap?.addPolyline(
                    DirectionConverter.createPolyline(
                        this,
                        directionPositionList,
                        5,
                        Color.GREEN
                    )
                )
                setCameraWithCoordinationBounds(route)
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
        Log.i("Prueba", "dataroute "+ route.bound.southwestCoordination.coordination.toString())
        Log.i("Prueba", "dataroute "+ route.bound.northeastCoordination.coordination.toString())

    }

    private fun showSnackbar(message: String?) {
        message?.let {
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }
}
