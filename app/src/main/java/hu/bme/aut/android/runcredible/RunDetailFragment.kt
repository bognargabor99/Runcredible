package hu.bme.aut.android.runcredible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.runcredible.databinding.FragmentRunDetailBinding

class RunDetailFragment : Fragment() {
    private lateinit var binding: FragmentRunDetailBinding

    private val args: RunDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunDetailBinding.inflate(layoutInflater, container, false)
        binding.tvLandingText.text = args.runId.toString()
        return binding.root
    }
}