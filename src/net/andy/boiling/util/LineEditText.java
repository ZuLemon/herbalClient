package net.andy.boiling.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Guang on 2016/8/18.
 */
public class LineEditText extends EditText {

    private Paint mPaint;
    /**
     * @param context
     * @param attrs
     */
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//      画底线
        canvas.drawLine(0,this.getHeight()-1,  this.getWidth()-1, this.getHeight()-1, mPaint);
    }
}