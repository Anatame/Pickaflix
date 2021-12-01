package com.anatame.pickaflix.presentation.CustomViews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.ThemeableBottomNavBinding
import kotlin.properties.Delegates

class ThemeableBottomNav constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var binding: ThemeableBottomNavBinding = ThemeableBottomNavBinding.inflate(LayoutInflater.from(context), this, false)

    init { // inflate binding and add as view
        addView(binding.root)

        val attributes = context.obtainStyledAttributes(attrs, com.anatame.pickaflix.R.styleable.ThemeableBottomNav)
        try {
            val color = attributes.getColor(R.styleable.ThemeableBottomNav_backgroundColor, 0)
            binding.bottomNavBackground.setBackgroundColor(color)

        } finally {
            attributes.recycle()
        }
    }

    fun setNavBackgroundColor(color: Int){
        binding.bottomNavBackground.setBackgroundColor(color)
    }



}