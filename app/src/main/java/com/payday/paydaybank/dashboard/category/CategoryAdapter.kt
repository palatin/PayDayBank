package com.payday.paydaybank.dashboard.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.payday.paydaybank.R
import com.payday.paydaybank.model.category.Category
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.layout_category.view.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FragmentScoped
class CategoryAdapter @Inject constructor(private val lifecycleCoroutineScope: LifecycleCoroutineScope) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories: Collection<Category> = listOf()
    private val click = BroadcastChannel<View>(1)
    val clickFlow = click.asFlow().mapLatest { Pair(it,
        categories.elementAt((it.parent as RecyclerView).getChildAdapterPosition(it))) }.catch {  }



    lateinit var fragment: Fragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_category, parent, false)
                .apply {
                    setOnClickListener {
                        lifecycleCoroutineScope.launch {
                            click.send(it)
                        }
                    }
                })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories.elementAt(position))
        fragment.startPostponedEnterTransition()
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    fun setCategories(categories: Collection<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(category: Category) {
            view.transitionName = "Category_${category.name}"
            view.cv_category.setCardBackgroundColor(view.resources.getColor(category.color))
            Glide.with(view.iv_category).load(category.icon).centerCrop().into(view.iv_category)
            view.tv_category_name.text = category.name
            view.tv_category_money.text = category.amount.toString() + " $"
        }
    }


}