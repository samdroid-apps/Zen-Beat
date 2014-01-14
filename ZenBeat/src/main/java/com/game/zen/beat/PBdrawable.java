/*This file is part of Zen Beat.

    Zen Beat is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Zen Beat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Zen Beat.  If not, see <http://www.gnu.org/licenses/>.*/

package com.game.zen.beat;

import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.util.TypedValue;

public class PBdrawable extends Drawable {

    private double p = 0.5;
    private Paint mPaint = new Paint();
    private static final int ACCCENT_COLOR = 0x33FFFFFF;
    private int ACCCENT_COLOR_PB = 0xFFCC0000;
    private static final int DIM_COLOR = 0x33000000;
    private int BAR_COLOR = 0xFFFF4444;
    private static final int BG_COLOR = 0xFFDDDDDD;
    private static final String BOTTOM_TEXT = "Points to Next Level";
    public int number = 0;
    public int AB_ICONS = 2;

    static final int COLOR_RED = 0;
    static final int COLOR_YELLOW = 1;
    static final int COLOR_BLUE = 2;
    static final int COLOR_PURPLE = 3;

    static final int[] BAR_COLORS = {0xFFFF4444, 0xFFFFBB33, 0xFF33B5E5, 0xFFAA66CC};
    static final int[] ACCENT_COLORS_PB = {0xFFCC0000, 0xFFFF8800, 0xFF0099CC, 0xFF9933CC};

    public void setProgress (int progress) {p = progress/100.0;}

    public void setColor(int colorId) {
        ACCCENT_COLOR_PB = ACCENT_COLORS_PB[colorId];
        BAR_COLOR = BAR_COLORS[colorId];
        invalidateSelf();
    }

    private int getLeftSide(int rightSide, String txt, Rect b) {
        Resources r = Resources.getSystem();
        //float px = (64+(48*(AB_ICONS-1)))*r.getDisplayMetrics().density;
        float px = 56*AB_ICONS*r.getDisplayMetrics().density;
        return (int)(b.right-px-mPaint.measureText(txt));
    };

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();

        mPaint.setColor(BG_COLOR);
        canvas.drawRect(bounds, mPaint);

        mPaint.setColor(BAR_COLOR);
        canvas.drawRect(bounds.left, bounds.top, (int)(bounds.right*p), bounds.bottom, mPaint);
        mPaint.setColor(ACCCENT_COLOR_PB);

        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        int rightSide = (int)(bounds.right*p);
        mPaint.setTextSize((int)((bounds.height()/3)*2));
        int leftSide = getLeftSide(rightSide, Integer.toString(number), bounds);
        canvas.drawText(Integer.toString(number), leftSide, bounds.bottom-(int)(bounds.height()/3)-2, mPaint);

        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        mPaint.setTextSize((int)(bounds.height()/3));
        leftSide = getLeftSide(rightSide, BOTTOM_TEXT, bounds);
        canvas.drawText(BOTTOM_TEXT, leftSide, bounds.bottom-3, mPaint);

        mPaint.setColor(ACCCENT_COLOR);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.top + 1, mPaint);

        mPaint.setColor(DIM_COLOR);
        canvas.drawRect(bounds.left, bounds.bottom - 2, bounds.right, bounds.bottom, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException("PBdrawable doesn't support setAlpha(int)");
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        throw new UnsupportedOperationException("PBdrawable doesn't support setColorFilter(ColorFilter)");
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

}
