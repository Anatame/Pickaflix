package com.anatame.pickaflix.common.Extras.CustomViews

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.ThemeableBottomNavBinding


class ThemeableBottomNav constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var binding: ThemeableBottomNavBinding = ThemeableBottomNavBinding.inflate(LayoutInflater.from(context), this, false)

    var selectedColor = Color.BLUE
    var unselectedColor = Color.GRAY
    var currentItem = 0

    init { // inflate binding and add as view
        addView(binding.root)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ThemeableBottomNav)
        try {
            val color = attributes.getColor(R.styleable.ThemeableBottomNav_backgroundColor, 0)
            binding.bottomNavContainer.setBackgroundColor(color)
            selectionBehaviour()

        } finally {
            attributes.recycle()
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    private fun selectionBehaviour(){
        val count = binding.bottomNavContainer.childCount
        val viewList = ArrayList<View?>()
        for(i in 0..count){
            viewList.add(binding.bottomNavContainer.getChildAt(i))
        }


        viewList.forEachIndexed { index, view ->
            view?.let {
                view.setOnClickListener{
                    onItemClickBehaviour(viewList, index)
                        onItemClickListener?.let { it(index) }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private fun onItemClickBehaviour(
        viewList: ArrayList<View?>,
        index: Int
    ) {
        // set selected item color
        val v = viewList[index] as ImageButton?
        v?.setColorFilter(selectedColor)

        // set colors for unselected items
        viewList.forEachIndexed { mindex, view ->
            view?.let {
                if (mindex != index) {
                    val uView = view as ImageButton
                    uView.setColorFilter(unselectedColor)
                }
            }
        }
    }

    fun setNavBackgroundColor(color: Int){
        binding.bottomNavContainer.setBackgroundColor(color)
    }

    fun setCurrentSelectedItem(currentIndex: Int){
        currentItem = currentIndex
        val activeItem = binding.bottomNavContainer.getChildAt(currentIndex) as ImageButton
        activeItem.setColorFilter(selectedColor)
    }

    fun setNavHeight(height: Int){
        val params: ViewGroup.LayoutParams = binding.bottomNavContainer.layoutParams
    // Changes the height and width to the specified *pixels*
    // Changes the height and width to the specified *pixels*
    // Changes int to px via the extension function
        params.height = height.px
        binding.bottomNavContainer.layoutParams = params
    }

}

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()