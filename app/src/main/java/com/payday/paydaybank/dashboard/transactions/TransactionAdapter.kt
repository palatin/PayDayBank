package com.payday.paydaybank.dashboard.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.payday.paydaybank.R
import com.payday.paydaybank.dashboard.category.CategoryAdapter
import com.payday.paydaybank.model.Transaction
import com.payday.paydaybank.model.category.Category
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.layout_category.view.*
import kotlinx.android.synthetic.main.layout_transaction.view.*
import javax.inject.Inject
import kotlin.random.Random

val dummiesLogos = arrayOf(
    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/235px-Google_%22G%22_Logo.svg.png",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/YouTube_full-color_icon_%282017%29.svg/320px-YouTube_full-color_icon_%282017%29.svg.png"
)

@FragmentScoped
class TransactionAdapter @Inject constructor() : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions: List<Transaction> = listOf()

    var sortType: TransactionAdapter.DateSorting = TransactionAdapter.DateSorting.DESCENDING
        set(value) {
            if(value != field) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val position = if(sortType == DateSorting.DESCENDING) position else itemCount - 1 - position
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(transaction: Transaction) {

            Glide.with(view).load(dummiesLogos.first()).circleCrop().into(view.iv_logo)
            view.tv_amount.text = transaction.amount.toString() + " $"
            view.tv_name.text = transaction.vendor
            view.tv_date.text = transaction.date.replace('T', ' ').replace('Z', ' ')
        }
    }

    enum class DateSorting {
        ASCENDING,
        DESCENDING
    }
}