package hu.bme.aut.android.runcredible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import hu.bme.aut.android.runcredible.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(LayoutInflater.from(requireContext()))

        binding.btnPrevRuns.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mainFragment_to_listFragment2)
        }

        return binding.root
    }
}