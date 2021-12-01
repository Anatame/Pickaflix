package com.anatame.pickaflix.presentation.CustomViews

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Color
import android.graphics.ColorFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.ThemeableBottomNavBinding
import kotlin.properties.Delegates


class ThemeableBottomNav constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var binding: ThemeableBottomNavBinding = ThemeableBottomNavBinding.inflate(LayoutInflater.from(context), this, false)

    var selectedColor = Color.BLUE
    var unselectedColor = Color.GRAY

    init { // inflate binding and add as view
        addView(binding.root)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ThemeableBottomNav)
        try {
            val color = attributes.getColor(R.styleable.ThemeableBottomNav_backgroundColor, 0)
            binding.bottomNavBackground.setBackgroundColor(color)
            selectionBehaviour()

        } finally {
            attributes.recycle()
        }
    }

    private fun selectionBehaviour(){
        val count = binding.bottomNavBackground.childCount
        val viewList = ArrayList<View?>()
        for(i in 0..count){
            viewList.add(binding.bottomNavBackground.getChildAt(i))
        }

        viewList.forEachIndexed { index, view ->
            view?.let {
                view.setOnClickListener{
                     Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()

                   val v =  viewList[index] as ImageButton?
                    v?.setColorFilter(selectedColor)

                    viewList.forEachIndexed { mindex, view ->
                        view?.let {
                            if(mindex != index ){
                                val uView = view as ImageButton
                                uView.setColorFilter(unselectedColor)
                            }
                        }
                    }
                }
            }
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