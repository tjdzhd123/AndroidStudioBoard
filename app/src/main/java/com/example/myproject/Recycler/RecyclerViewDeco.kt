package com.example.myproject.Recycler

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDeco (val context: Context): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val index = parent.getChildAdapterPosition(view)
        if (index % 2 == 0) {
            outRect.set(0,5,0, 5)
        }else {
            outRect.set(0,5,0, 0)
        }
        view.setBackgroundColor(Color.WHITE)
        ViewCompat.setElevation(view, 10f)
    }
}