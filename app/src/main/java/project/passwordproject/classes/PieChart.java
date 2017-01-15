package project.passwordproject.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Serban on 14/01/2017.
 */

public class PieChart extends View {
    private final List<Site> mySites;
    private int[] usedColors;

    public PieChart(Context context, List<Site> mySites) {
        super(context);
        this.mySites = mySites;
        usedColors = new int[mySites.size()];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int totalAccounts = 0;
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        float graphWidth = (float) 0.8 * canvasWidth;
        float graphSpacing = (float) 0.2 * canvasWidth;

        for (Site site : mySites) {
            totalAccounts += site.getAccountList().size();
        }
//        Paint textPaint = new Paint();
//        textPaint.setColor(Color.BLACK);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setTextSize(75);
//        canvas.drawText("Accounts Graph",canvasWidth/2,60,textPaint);
        int startAngle = 0;
        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);
        for (int i = 0; i < mySites.size(); i++) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            usedColors[i] = color;
            Paint paint = new Paint();
            paint.setColor(color);
            int angle = 360 * mySites.get(i).getAccountList().size() / totalAccounts;
            if (i == mySites.size() - 1) {
                angle = 360 - startAngle;
            }
            canvas.drawArc(graphSpacing, graphSpacing, graphWidth, graphWidth, startAngle, angle, true, paint);
            canvas.drawArc(graphSpacing, graphSpacing, graphWidth, graphWidth, startAngle, angle, true, strokePaint);
            startAngle += angle;
        }

        float h = (float) (canvasHeight - canvasWidth * 0.9) / mySites.size();
        for (int i = 0; i < mySites.size(); i++) {
            Paint paint = new Paint();
            paint.setColor(usedColors[i]);
            canvas.drawRect(graphSpacing, i * h + (float) 0.9 * canvasWidth - 20, graphSpacing + 100, (float) h * (i + 1) + (float) 0.9 * canvasWidth - 20, paint);
            paint.setColor(Color.BLACK);

            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(50);
            canvas.drawText(mySites.get(i).getName(), graphSpacing + 100, i * h + h / 2 + (float) 0.9 * canvasWidth - 20, paint);
        }

    }
}
