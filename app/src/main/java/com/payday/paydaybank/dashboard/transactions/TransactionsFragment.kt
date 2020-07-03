package com.payday.paydaybank.dashboard.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.payday.paydaybank.R
import com.payday.paydaybank.dashboard.DashboardViewModel
import com.payday.paydaybank.model.category.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_transactions.*
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    @Inject
    lateinit var adapter: TransactionAdapter

    private val vm by activityViewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = MaterialContainerTransform()
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setPathMotion(androidx.transition.ArcMotion())
            setDuration(300)
            startElevation = 0f
            endElevation = 0f
            endViewId = R.id.ll_dashboard_top
            startViewId = R.id.cv_category
            addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    removeListener(this)
                    requireActivity().window.statusBarColor = resources.getColor(vm.stateLd.value?.selectedCategory?.color ?: return)
                }

                override fun onTransitionResume(transition: Transition) {

                }

                override fun onTransitionPause(transition: Transition) {

                }

                override fun onTransitionCancel(transition: Transition) {

                }

                override fun onTransitionStart(transition: Transition) {

                }

            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.stateLd.observe(viewLifecycleOwner, Observer {
            setCategory(it.selectedCategory ?: return@Observer)
            adapter.setTransactions(it.data?.get(it.selectedCategory) ?: return@Observer)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv.adapter = adapter
        btn_sort.setOnClickListener {
            adapter.sortType = if(adapter.sortType == TransactionAdapter.DateSorting.DESCENDING) TransactionAdapter.DateSorting.ASCENDING else TransactionAdapter.DateSorting.DESCENDING
        }
        ll_dashboard_top.doOnPreDraw {
            ll_dashboard_top.layoutParams.height = bottom_sheet.top + 100
        }
    }

    private fun setCategory(category: Category) {
        ll_dashboard_top.transitionName = "Category_${category.name}"
        tv_category_name.text = category.name
        ll_dashboard_top.setBackgroundColor(ContextCompat.getColor(requireContext(), category.color))

        tv_category_money.text = "${category.amount.toString()} $"

    }



}