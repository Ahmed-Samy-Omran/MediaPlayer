package com.example.mediaplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class WaveformSeekBar extends View {

    private Paint waveformPaint;
    private Paint progressPaint;
    private List<Float> waveformPoints;
    private float progress = 0f;
    private OnProgressChangedListener listener;

    public WaveformSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        waveformPaint = new Paint();
        waveformPaint.setColor(Color.LTGRAY); // Background color of the waveform
        waveformPaint.setStrokeWidth(2f);
        waveformPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.RED); // Color of the progress waveform
        progressPaint.setStrokeWidth(2f);
        progressPaint.setAntiAlias(true);
    }

    public void setWaveformData(List<Float> points) {
        waveformPoints = points;
        invalidate(); // Redraw the view
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (waveformPoints == null || waveformPoints.isEmpty()) {
            return;
        }

        // Draw waveform background
        for (int i = 0; i < waveformPoints.size(); i++) {
            float startX = getWidth() * i / (float) waveformPoints.size();
            float startY = getHeight() / 2f - waveformPoints.get(i) * getHeight() / 2f;
            float stopY = getHeight() / 2f + waveformPoints.get(i) * getHeight() / 2f;

            canvas.drawLine(startX, startY, startX, stopY, waveformPaint);
        }

        // Draw waveform progress
        for (int i = 0; i < (int) (waveformPoints.size() * progress); i++) {
            float startX = getWidth() * i / (float) waveformPoints.size();
            float startY = getHeight() / 2f - waveformPoints.get(i) * getHeight() / 2f;
            float stopY = getHeight() / 2f + waveformPoints.get(i) * getHeight() / 2f;

            canvas.drawLine(startX, startY, startX, stopY, progressPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            progress = event.getX() / getWidth();
            if (listener != null) {
                listener.onProgressChanged(progress);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void setOnProgressChangedListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(float progress);
    }
}
