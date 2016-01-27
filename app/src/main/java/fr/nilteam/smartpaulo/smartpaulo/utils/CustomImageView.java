package fr.nilteam.smartpaulo.smartpaulo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by julien on 27/01/16.
 */
public class CustomImageView extends ImageView implements View.OnTouchListener {

    private float xInit;
    private float yInit;

    private float xFinal;
    private float yFinal;

    private float xCourant;
    private float yCourant;

    private Bitmap bitmap;

    public CustomImageView(Context context) {
        super(context);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CustomImageView(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            xInit = motionEvent.getX();
            yInit = motionEvent.getY();
        } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
            xFinal = motionEvent.getX();
            yFinal = motionEvent.getY();
        } else {
            xCourant = motionEvent.getX();
            yCourant = motionEvent.getY();
        }
        this.postInvalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(bitmap, new Rect(0, 0, 400, 400), new Rect(0,0,240,135) , null);
        canvas.drawLine(xInit, yInit, xCourant, yCourant, paint);
        super.onDraw(canvas);
        //you need to call postInvalidate so that the system knows that it  should redraw your custom ImageView
        this.postInvalidate();
    }
}
