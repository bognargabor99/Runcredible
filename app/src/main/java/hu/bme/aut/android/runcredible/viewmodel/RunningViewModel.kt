package hu.bme.aut.android.runcredible.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.runcredible.RunningApplication
import hu.bme.aut.android.runcredible.database.RunEntity
import hu.bme.aut.android.runcredible.repository.RunningRepository
import kotlinx.coroutines.launch

class RunningViewModel : ViewModel() {

    private val repository: RunningRepository

    val allRunnings: LiveData<List<RunEntity>>

    init {
        val todoDao = RunningApplication.runningDatabase.runningDao()
        repository = RunningRepository(todoDao)
        allRunnings = repository.getRunEntities()
    }

    fun delete(run: RunEntity)  {
        repository.delete(run)
    }
}