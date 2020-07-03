package com.payday.paydaybank.dashboard.category

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.payday.paydaybank.R
import com.payday.paydaybank.dashboard.DashboardViewModel
import com.payday.paydaybank.dashboard.transactions.TransactionsFragment
import com.payday.paydaybank.model.category.Category
import com.payday.paydaybank.util.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.rv
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CategoryFragment : Fragment() {


    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private val vm by activityViewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        categoryAdapter.fragment = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRv()
        btn_calendar.setOnClickListener {
            pickDates(vm.stateLd.value?.dateRange)
        }
        refresh.setOnRefreshListener {
            vm.postAction(DashboardViewModel.Action.RefreshData)
        }
        BottomSheetBehavior.from(bottom_sheet).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                refresh.isEnabled = newState != BottomSheetBehavior.STATE_EXPANDED && newState != BottomSheetBehavior.STATE_DRAGGING

            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().window.statusBarColor = Color.parseColor("#464958")
        vm.stateLd.observe(viewLifecycleOwner, Observer {
            categoryAdapter.setCategories(it.data?.keys ?: return@Observer)
            refresh.isRefreshing = it.isLoading
            tv_income.text = it.income.toString() + " $"
            tv_expenses.text = it.expensive.toString() + " $"
        })
    }

    private fun setUpRv() {
        rv.adapter = categoryAdapter
        val categorySize = requireContext().resources.getDimension(R.dimen.category_size).toInt()
        rv.doOnPreDraw {
            rv.layoutManager = GridLayoutManager(requireContext(),  it.width / categorySize)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoryAdapter.clickFlow.collect {
                moveToCategoryFragment(it.first, it.second)
            }
        }
    }

    private fun pickDates(timestampRange: LongRange?) {
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select transaction dates range").apply {
                if(timestampRange != null)
                    setSelection(Pair.create(timestampRange.first, timestampRange.last))
            }
            .setCalendarConstraints(CalendarConstraints.Builder().build())
            .build().apply {
                addOnPositiveButtonClickListener {
                    vm.postAction(
                        DashboardViewModel.Action.SetDateRange(
                            LongRange(it.first, it.second)
                        )
                    )
                }
                addOnNegativeButtonClickListener {
                    vm.postAction(DashboardViewModel.Action.ResetDateRange)
                }
                show(this@CategoryFragment.childFragmentManager, "datepicker")
            }

    }

    private fun moveToCategoryFragment(view: View, category: Category) {
        vm.postAction(
            DashboardViewModel.Action.SelectCategory(
                category
            )
        )
        findNavController().navigateSafe(R.id.action_categoryFragment_to_transactionsFragment, null, null, FragmentNavigatorExtras(
            view to view.transitionName))

    }

}