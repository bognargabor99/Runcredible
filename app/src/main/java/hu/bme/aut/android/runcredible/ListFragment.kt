package hu.bme.aut.android.runcredible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.android.runcredible.adapter.RunAdapter
import hu.bme.aut.android.runcredible.database.RunEntity
import hu.bme.aut.android.runcredible.database.RunningDatabase
import hu.bme.aut.android.runcredible.databinding.FragmentListBinding
import hu.bme.aut.android.runcredible.repository.RunningRepository
import hu.bme.aut.android.runcredible.viewmodel.RunningViewModel

class ListFragment : Fragment(), RunAdapter.RunItemClickListener {
    private lateinit var binding: FragmentListBinding
    private lateinit var runningViewModel: RunningViewModel
    private lateinit var runAdapter: RunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runningViewModel = ViewModelProvider(this).get(RunningViewModel::class.java)
        runningViewModel.allRunnings.observe(this) {
            runAdapter.submitList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        runAdapter = RunAdapter()
        runAdapter.itemClickListener = this
        binding.runList.adapter = runAdapter
    }

    override fun onItemClick(run: RunEntity) {

    }

    override fun onDelete(run: RunEntity) {
        runningViewModel.delete(run)
    }
}