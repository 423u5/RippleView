package com.duolier.rippleview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * @author 423u5 || Gopal Chaudhary
 * */
class RippleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var _validate = false
    private var _paint = Paint()
    private var _a1 = 0f
    private val _center = PointF()
    private var _maxRadius = 0f
    private var _start = 0f
    private var _count = 3
    private var _duration = 1800
    private var _color = Color.RED
    private lateinit var _animator : ValueAnimator


    init {

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.RippleView, defStyleAttr, 0)

        _start = typedArray.getDimension(R.styleable.RippleView_start, _start) / 2f
        _count = typedArray.getInteger(R.styleable.RippleView_count, _count)
        _duration = typedArray.getInteger(R.styleable.RippleView_duration, _duration)
        _color = typedArray.getColor(R.styleable.RippleView_color, _color)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!_validate) validate()

        val a3: Float = (_maxRadius - _start) / _count

        for (a4 in _count downTo 0) {

            var a5 = a4 * a3 + _start

            a5 += _a1 * (_maxRadius - _start)
            if (a5 > _maxRadius) a5 -= _maxRadius - _start

            val a6: Float = 180 - a5 / _maxRadius * 180

            _paint.alpha = a6.toInt()
            canvas?.drawCircle(_center.x, _center.y, a5, _paint)
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (MeasureSpec.getSize(widthMeasureSpec) * 0.75f).toInt())
    }


    private fun validate() {

        _validate = true

        _paint.apply {
            style = Paint.Style.FILL
            color = _color
        }


        _center.x = width/ 2f
        _center.y = height / 2f

        _maxRadius = width.coerceAtMost(height) / 2f

        _animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = _duration.toLong()
            interpolator = LinearInterpolator()
            repeatCount = -1
            addUpdateListener {
                _a1 = it.animatedValue as Float
                postInvalidate()
            }
            start()
        }
    }

}