package hu.bme.aut.android.runcredible

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import hu.bme.aut.android.runcredible.databinding.FragmentMainBinding
import hu.bme.aut.android.runcredible.service.LocationService
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var runningServiceIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        runningServiceIntent = Intent(requireContext(), LocationService::class.java)

        binding.btnPrevRuns.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mainFragment_to_listFragment)
        }

        binding.btnStartRun.isEnabled = !LocationService.isRunning
        binding.btnStopRun.isEnabled = !binding.btnStartRun.isEnabled

        binding.btnStartRun.setOnClickListener {
            startMonitoringServiceWithPermissionCheck()
        }

        binding.btnStopRun.setOnClickListener {
            stopMonitoringService()
        }

        return binding.root
    }

    @NeedsPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun startMonitoringService() {
        binding.btnStartRun.isEnabled = false
        binding.btnStopRun.isEnabled = true
        activity?.startService(runningServiceIntent)
    }

    private fun stopMonitoringService() {
        binding.btnStopRun.isEnabled = false
        binding.btnStartRun.isEnabled = true
        activity?.stopService(runningServiceIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }
}