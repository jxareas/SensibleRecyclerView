package com.indisparte.sensiblerecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.indisparte.sensiblerecyclerview.databinding.LayoutEmptyBinding
import com.indisparte.sensiblerecyclerview.databinding.LayoutErrorBinding
import com.indisparte.sensiblerecyclerview.databinding.LayoutLoadingBinding
import com.indisparte.sensiblerecyclerview.databinding.LayoutSensibleRecyclerBinding

/**
 * @author Antonio Di Nuzzo (Indisparte)
 */
class SensibleRecyclerView constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private val binding: LayoutSensibleRecyclerBinding =
        LayoutSensibleRecyclerBinding.inflate(LayoutInflater.from(context), this, false)

    private val errorBinding: LayoutErrorBinding
    private val emptyBinding: LayoutEmptyBinding
    private val loadingBinding: LayoutLoadingBinding

    // expose the recycler view
    val recyclerView: RecyclerView
        get() = binding.customRecyclerView

    var errorText: String = ""
        set(value) {
            field = value
            errorBinding.errorMessage.text = value
        }

    var emptyText: String = ""
        set(value) {
            field = value
            emptyBinding.emptyMessage.text = value
        }

    @DrawableRes
    var errorIcon = 0
        set(value) {
            field = value
            errorBinding.errorImage.setImageResource(value)
        }

    @DrawableRes
    var emptyIcon = 0
        set(value) {
            field = value
            emptyBinding.emptyImage.setImageResource(value)
        }

    init {

        // inflate the layout
        errorBinding = binding.customErrorView
        emptyBinding = binding.customEmptyView
        loadingBinding = binding.customLoadingView

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SensibleRecyclerView,
            0,
            0
        ).apply {
            try {
                errorText = (getString(R.styleable.SensibleRecyclerView_errorText) ?: R.string.msg_error) as String
                emptyText =
                    (getString(R.styleable.SensibleRecyclerView_emptyText) ?: R.string.msg_empty_result) as String
                errorIcon = getResourceId(
                    R.styleable.SensibleRecyclerView_errorIcon,
                    R.drawable.ic_error
                )
                emptyIcon =
                    getResourceId(R.styleable.SensibleRecyclerView_emptyIcon, R.drawable.ic_empty)
            } finally {
                recycle()
            }
        }
    }

    fun showEmptyView(msg: String? = null) {
        emptyText = msg ?: emptyText
        loadingBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE

        emptyBinding.root.visibility = View.VISIBLE
    }

    fun showErrorView(msg: String? = null) {
        errorText = msg ?: errorText
        loadingBinding.root.visibility = View.GONE
        emptyBinding.root.visibility = View.GONE

        errorBinding.root.visibility = View.VISIBLE
    }

    fun showLoadingView() {
        emptyBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE

        loadingBinding.root.visibility = View.VISIBLE
    }

    fun hideAllViews() {
        loadingBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE
        emptyBinding.root.visibility = View.GONE
    }

    fun setOnRetryClickListener(callback: () -> Unit) {
        errorBinding.retryButton.setOnClickListener {
            callback()
        }
    }
}