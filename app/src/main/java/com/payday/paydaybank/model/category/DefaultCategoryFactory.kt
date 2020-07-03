package com.payday.paydaybank.model.category

import com.payday.paydaybank.R
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


class DefaultCategoryFactory @Inject constructor(): CategoryFactory {

    private val categories = hashMapOf(
        "Salary" to CategoryResource(R.drawable.ic_baseline_attach_money_24, R.color.DodgerBlue),
        "Kids" to CategoryResource(0, R.color.PeachOrange),
        "Garden" to CategoryResource(0, R.color.Emerald),
        "Movies" to CategoryResource(R.drawable.ic_baseline_movie_24, R.color.MidnightBlue),
        "Grocery" to CategoryResource(0, R.color.MidnightBlue),
        "Toys" to CategoryResource(0, R.color.Pomegranate),
        "Health" to CategoryResource(0, R.color.DodgerBlue),
        "Shoes" to CategoryResource(R.drawable.ic_baseline_attach_money_24, R.color.Amethyst),
        "Jewelery" to CategoryResource(R.drawable.ic_baseline_attach_money_24, R.color.Amethyst),
        "Beauty" to CategoryResource(0, R.color.VividTangerine),
        "Electronics" to CategoryResource(0, R.color.DodgerBlue),
        "Games" to CategoryResource(0, R.color.MidnightBlue),
        "Music" to CategoryResource(0, R.color.Pomegranate),
        "Industrial" to CategoryResource(R.drawable.ic_baseline_business_center_24, R.color.MidnightBlue),
        "Sports" to CategoryResource(0, R.color.Amethyst),
        "Home" to CategoryResource(R.drawable.ic_baseline_home_24, R.color.Emerald),
        "Automotive" to CategoryResource(0, R.color.Amethyst),
        "Computers" to CategoryResource(0, R.color.DodgerBlue),
        "Clothing" to CategoryResource(0, R.color.DodgerBlue)
    )

    override fun fromName(name: String): Category {
        val resource = categories.getOrElse(name, { CategoryResource(0, R.color.DodgerBlue) })
        return Category(name, resource.image, resource.color)
    }

    data class CategoryResource(val image: Int, val color: Int)
}