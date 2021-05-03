package hu.bme.aut.android.runcredible

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator


class MapsFragment(private val route: List<LatLng>) : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        try {
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.retro_map_style))
            if (!success) {
                Log.e("map", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("map", "Can't find style. Error: ", e)
        }
        googleMap.addPolyline(PolylineOptions()
            .addAll(route)
            .color(Color.argb(255, 0, 128, 0))
            .pattern(listOf(Dot()))
            .endCap(RoundCap())
            .startCap(ButtCap())
            .jointType(JointType.ROUND)
            .width(20f)
        )
        val generator = IconGenerator(context)
        val optionsStart = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon(getString(R.string.start)))).position(route.first()).anchor(generator.anchorU, generator.anchorV)
        val optionsEnd = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon(getString(R.string.end)))).position(route.last()).anchor(generator.anchorU, generator.anchorV)
        googleMap.addMarker(optionsStart)
        googleMap.addMarker(optionsEnd)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.first(), 15f))
        googleMap.setMaxZoomPreference(30f)
        googleMap.setMinZoomPreference(10f)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}