package hu.bme.aut.android.runcredible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import hu.bme.aut.android.runcredible.database.LocationModel
import hu.bme.aut.android.runcredible.databinding.FragmentRunDetailBinding
import hu.bme.aut.android.runcredible.repository.RunningRepository
import kotlin.math.roundToInt

class RunDetailFragment : Fragment() {
    private lateinit var binding: FragmentRunDetailBinding

    private val args: RunDetailFragmentArgs by navArgs()
    private lateinit var locations: List<LocationModel>
    private val repository = RunningRepository(RunningApplication.runningDatabase.runningDao())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locations = repository.getRunning(args.runId)

        binding = FragmentRunDetailBinding.inflate(layoutInflater, container, false)

        childFragmentManager.beginTransaction()
            .add(R.id.mapContainer, MapsFragment.newInstance(ArrayList(locations.map { LatLng(it.latitude, it.longitude) })))
            .commit()

        val fullDistanceMeters = calculateDistance(locations.map { LatLng(it.latitude, it.longitude) })
        val durationSeconds = ((locations.last().time - locations.first().time) / 1000.0)
        val speedMS = fullDistanceMeters / durationSeconds
        binding.tvLength.text = getString(R.string.stats_run_length, fullDistanceMeters.roundToInt() )
        binding.tvDuration.text = getString(R.string.stats_run_duration, durationSeconds.toInt() / 60, durationSeconds.toInt() % 60)
        binding.tvSpeed.text = getString(R.string.stats_run_speed, speedMS, speedMS * 3.6)
        binding.tvTrackedPoints.text = locations.size.toString()

        return binding.root
    }

    private fun calculateDistance(latLngs: List<LatLng>): Double {
        var distance = 0.0
        for (i in 1 until latLngs.size)
            distance += SphericalUtil.computeDistanceBetween(latLngs[i - 1], latLngs[i])
        return distance
    }
}