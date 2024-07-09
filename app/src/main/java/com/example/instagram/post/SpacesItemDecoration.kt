package com.example.instagram.post
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as StaggeredGridLayoutManager).spanCount
        val column = position % spanCount

        outRect.left = space / 2
        outRect.right = space / 2
        outRect.top = space

        // Add extra space at the bottom of the last row
        if (position >= parent.adapter!!.itemCount - spanCount) {
            outRect.bottom = space
        }
    }
}
