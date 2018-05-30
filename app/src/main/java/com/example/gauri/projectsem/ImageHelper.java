package com.example.gauri.projectsem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.microsoft.projectoxford.emotion.contract.FaceRectangle;

    /**
     * Created by Laxmi on 10/29/2017.
     */

    public class ImageHelper {
        public static Bitmap drawRectOnBitmap (Bitmap mBitmap, FaceRectangle faceRectangle,String status) {
            Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(1);


            canvas.drawRect(faceRectangle.left, faceRectangle.top, faceRectangle.left + faceRectangle.width, faceRectangle.top + faceRectangle.height, paint);
            int cX = faceRectangle.left + faceRectangle.width;
            int cY = faceRectangle.top + faceRectangle.height;

            drawTextOnBitmap(canvas, 100, cX / 2 + cX / 5, cY + 100, android.R.color.white, status);
            return bitmap;
        }

        private static void drawTextOnBitmap(Canvas canvas,int textSize,int cX, int cY,int color,String status)
        {
            Paint tempTextPaint=new Paint();
            tempTextPaint.setAntiAlias(true);
            tempTextPaint.setStyle(Paint.Style.FILL);
            tempTextPaint.setColor(color);
            tempTextPaint.setTextSize(textSize);
            canvas.drawText(status,cX,cY,tempTextPaint);



        }
    }
