package hu.bme.aut.android.runcredible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import hu.bme.aut.android.runcredible.database.RunningDatabase
import hu.bme.aut.android.runcredible.databinding.FragmentListBinding
import hu.bme.aut.android.runcredible.repository.RunningRepository

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var database: RunningDatabase
    private lateinit var repository: RunningRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        database = Room.databaseBuilder(
                requireContext(),
                RunningDatabase::class.java,
                "runningDB")
                .build()

        repository = RunningRepository(database.runningDao())

        return binding.root
    }
}