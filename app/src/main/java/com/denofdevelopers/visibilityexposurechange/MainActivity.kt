package com.denofdevelopers.visibilityexposurechange

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onScrollTouchListener()
    }

    private fun onScrollTouchListener() {
        scrollView.setOnTouchListener(View.OnTouchListener { _, event ->
            val action = event.action
            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                Handler().postDelayed({
                    val childCount: Int = scrollViewRootChild.childCount

                    //Scroll view location on screen
                    val scrollViewLocation = intArrayOf(0, 0)
                    scrollView.getLocationOnScreen(scrollViewLocation)

                    //Scroll view height
                    val scrollViewHeight: Int = scrollView.height
                    for (i in 0 until childCount) {
                        val child: View = scrollViewRootChild.getChildAt(i)
                        if (child.id == measuredView.id && child.visibility == View.VISIBLE) {
                            val viewLocation = IntArray(2)
                            child.getLocationOnScreen(viewLocation)
                            val viewHeight: Int = measuredView.height
                            getViewVisibilityOnScrollStopped(
                                scrollViewLocation,
                                scrollViewHeight,
                                viewLocation,
                                viewHeight
                            )
                        }
                    }
                }, 150)
            }
            false
        })
    }

    private fun getViewVisibilityOnScrollStopped(
        scrollViewLocation: IntArray, //location of scroll view on screen
        scrollViewHeight: Int, // height of scroll view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewHeight: Int //  height of view
    ) {
        var visiblePercent: Float
        val viewBottom = viewHeight + viewLocation[1] //Get the bottom of view.
        if (viewLocation[1] >= scrollViewLocation[1]) {  //if view's top is inside the scroll view.
            visiblePercent = 100f
            val scrollBottom =
                scrollViewHeight + scrollViewLocation[1] //Get the bottom of scroll view
            if (viewBottom >= scrollBottom) {   //If view's bottom is outside from scroll view
                val visiblePart =
                    scrollBottom - viewLocation[1] //Find the visible part of view by subtracting view's top from scrollview's bottom
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
            }
            Toast.makeText(
                this,
                "Visibility of the view: " + visiblePercent.toInt() + "%",
                Toast.LENGTH_SHORT
            ).show()
        } else {      //if view's top is outside the scroll view.
            if (viewBottom > scrollViewLocation[1]) { //if view's bottom is outside the scroll view
                val visiblePart =
                    viewBottom - scrollViewLocation[1] //Find the visible part of view by subtracting scroll view's top from view's bottom
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
                Toast.makeText(
                    this,
                    "Visibility of the view: " + visiblePercent.toInt() + "%",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
