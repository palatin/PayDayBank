package com.payday.paydaybank.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payday.paydaybank.R
import com.payday.paydaybank.model.Transaction
import com.payday.paydaybank.model.category.Category
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DashboardViewModel @ViewModelInject constructor(
    private val dashboardUseCase: DashboardUseCase
) : ViewModel() {

    private val _stateLd = MutableLiveData<State>()
    val stateLd: LiveData<State>
    get() = _stateLd

    private var state = State()
    set(value) {
        field = value
        _stateLd.value = value
    }

    init {
        fetchData()
    }

    fun postAction(action: Action) {
        when(action) {
            Action.RefreshData -> fetchData(true, state.dateRange)
            is Action.SetDateRange -> fetchData(false, action.range)
            Action.ResetDateRange -> fetchData(false, null)
            is Action.SelectCategory -> state = state.copy(selectedCategory = action.category)
        }
    }

    private fun fetchData(forceUpdate: Boolean = false, range: LongRange? = null) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)
                val data = if(range == null) dashboardUseCase.getDashboardData(forceUpdate) else dashboardUseCase.getDashboardData(forceUpdate, range)
                state = state.copy(isLoading = false, data = data.data, income = data.totalIncome, expensive = data.totalExpensive, dateRange = range)
            } catch (ex: Exception) {
                state = state.copy(isLoading = false)
            }
        }
    }

    data class State(val isLoading: Boolean = false, val data: Map<Category, List<Transaction>>? = null, val income: BigDecimal = BigDecimal.ZERO,
                     val expensive: BigDecimal = BigDecimal.ZERO, val dateRange: LongRange? = null,
                     val selectedCategory: Category? = null)

    sealed class Action {
        object RefreshData : Action()
        data class SetDateRange(val range: LongRange) : Action()
        data class SelectCategory(val category: Category) : Action()
        object ResetDateRange : Action()
    }
}