package com.anatame.pickaflix.presentation.CustomViews

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
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

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ThemeableBottomNav)
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

    fun setNavHeight(height: Int){
        val params: ViewGroup.LayoutParams = binding.bottomNavBackground.layoutParams
// Changes the height and width to the specified *pixels*
// Changes the height and width to the specified *pixels*
// Changes int to px via the extension function
        params.height = height.px
        binding.bottomNavBackground.layoutParams = params
    }



}

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()