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

    var layoutView: View? = null
        private set

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

        emptyView = inflater.inflate(R.layout.custom_view_empty, null)
        emptyText = emptyView.findViewById(R.id.emptyText)

        errorView = inflater.inflate(R.layout.custom_view_error, null)

        loadingView = inflater.inflate(R.layout.custom_view_loading, null)

        this.addView(errorView)
        this.addView(loadingView)
        this.addView(emptyView)
        this.addView(internetView)
    }

    fun setLayout(layoutView: View) {
        this.layoutView = layoutView
        setVisible(CustomViews.LOADING)
    }

    fun setEmptyText(text: String) {
        emptyText.text = text
    }

    fun setEmptyText(textId: Int) {
        emptyText.text = context.getString(textId)
    }

    fun retry(action: () -> Unit) {
        internetView.setOnClickListener {
            action.invoke()
        }
    }

    fun setVisible(customViews: CustomViews) {
        if (layoutView != null) {
            val views =
                arrayOf(layoutView!!, emptyView, internetView, errorView, loadingView)
            for (x in views.indices) {
                if (customViews.value == x) {
                    views[customViews.value].visibility = View.VISIBLE
                } else {
                    views[x].visibility = View.GONE
                }
            }
        } else {
            throw Throwable("no layout view provided")
        }
    }

    fun hideAll() {
        if (layoutView != null) {

            val views =
                arrayOf(layoutView!!, emptyView, internetView, errorView, loadingView)
            for (x in views.indices) {
                views[x].visibility = View.GONE
            }
        } else {
            throw Throwable("no layout view provided")
        }
    }
}
