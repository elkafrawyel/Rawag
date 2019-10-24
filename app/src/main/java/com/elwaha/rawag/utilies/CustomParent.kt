package com.elwaha.rawag.utilies

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import com.elwaha.rawag.R

enum class CustomViews(val value: Int) {
    LAYOUT(0),
    EMPTY(1),
    INTERNET(2),
    ERROR(3),
    LOADING(4),
}

class CustomParent : FrameLayout {

    private lateinit var internetView: View
    private lateinit var emptyView: View
    private lateinit var errorView: View
    private lateinit var loadingView: View
    private lateinit var emptyText: TextView
    private lateinit var errorText: TextView
    private var layoutView: View? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet)
            : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    @SuppressLint("InflateParams")
    private fun init() {

        val inflater = LayoutInflater.from(context)

        internetView = inflater.inflate(R.layout.custom_view_network, null)
        internetView.visibility = View.GONE

        emptyView = inflater.inflate(R.layout.custom_view_empty, null)
        emptyText = emptyView.findViewById(R.id.emptyText)

        errorView = inflater.inflate(R.layout.custom_view_error, null)
        errorText = errorView.findViewById(R.id.errorText)

        loadingView = inflater.inflate(R.layout.custom_view_loading, null)

        this.addView(errorView)
        this.addView(loadingView)
        this.addView(emptyView)
        this.addView(internetView)

    }

    fun setLayout(layoutView: View) {
        this.layoutView = layoutView
        hideAll()
    }

    fun setEmptyText(text: String) {
        emptyText.text = text
    }

    fun setEmptyText(textId: Int) {
        emptyText.text = context.getString(textId)
    }

    fun setErrorText(text: String) {
        errorText.text = text
    }

    fun setErrorText(textId: Int) {
        errorText.text = context.getString(textId)
    }

    fun retry(action: () -> Unit) {
        internetView.setOnClickListener {
            action.invoke()
        }
    }

    fun setVisible(customViews: CustomViews) {
        if (layoutView != null) {
            val views = arrayOf(layoutView!!, emptyView, internetView, errorView, loadingView)

            views.forEachIndexed { index, view ->
                if (customViews.value == index) {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        } else {
            throw Throwable("No layout view provided")
        }
    }

    fun hideAll() {
        if (layoutView != null) {
            val views = arrayOf(layoutView!!, emptyView, internetView, errorView, loadingView)

            views.forEach {
                it.visibility = View.GONE
            }
        } else {
            throw Throwable("No layout view provided")
        }
    }
}
