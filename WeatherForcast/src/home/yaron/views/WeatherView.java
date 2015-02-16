package home.yaron.views;

import home.yaron.weather_forcast.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class WeatherView extends View
{
	// Members variable
	private int steps; // 1..x
	private Bitmap pic;
	private int drawableHeight;
	private int drawableWidth;
	private Rect dest;
	private int drawableId;
	private int position; // 0..steps-1
	private boolean setHeightByDrawble;
	private Paint textPaint;
	private int textSize;
	private Rect textBounds;
	private String textLeft;
	private String textDrawable;

	public String getTextDrawable()
	{
		return textDrawable;
	}

	public void setTextDrawable(String textDrawable) 
	{
		this.textDrawable = textDrawable;
	}

	public String getLeftText()
	{
		return textLeft;
	}

	public void setLeftText(String text)
	{
		this.textLeft = text;
	}

	public int getTextSize()
	{
		return textSize;
	}

	public void setTextSize(int textSize)
	{
		this.textSize = textSize;
	}

	public boolean isSetHeightByDrawble()
	{
		return setHeightByDrawble;
	}

	public void setSetHeightByDrawble(boolean setHeightByDrawble)
	{
		this.setHeightByDrawble = setHeightByDrawble;
	}

	public int getSteps()
	{
		return steps;
	}

	public void setSteps(int steps)
	{
		this.steps = steps;
		//		invalidate();
		//		requestLayout();
	}

	public int getPosition() 
	{
		return position;
	}

	public void setPosition(int position)
	{	
		if( this.position != position )
		{
			this.position = position;
			invalidate();
		}
	}

	public int getDrawableId()
	{
		return drawableId;
	}

	public void setDrawableId(int drawableId)
	{
		if( this.drawableId != drawableId )
		{
			this.drawableId = drawableId;
			pic.recycle();
			pic = null;
			init();
			invalidate();
			requestLayout();						
		}
	}

	public WeatherView(Context context, AttributeSet attrs)
	{		
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.WeatherView,
				0, 0);

		try 
		{
			steps = a.getInteger(R.styleable.WeatherView_steps, 5);
			setPosition(a.getInteger(R.styleable.WeatherView_position, 0));
			drawableId = a.getResourceId(R.styleable.WeatherView_drawableId,-1);			
			setSetHeightByDrawble(a.getBoolean(R.styleable.WeatherView_setHeightByDrawble,false));
			textSize = a.getInteger(R.styleable.WeatherView_textSize, 15);
			textLeft = a.getString(R.styleable.WeatherView_textLeft);
			textDrawable = a.getString(R.styleable.WeatherView_textDrawable);
		} 
		finally 
		{
			a.recycle();
		}

		init();
	}

	private void init()
	{
		pic = BitmapFactory.decodeResource(getResources(),getDrawableId());
		drawableHeight = pic.getHeight();
		drawableWidth = pic.getWidth();
		dest = new Rect();

		// Text - Convert the dps to pixels, based on density scale
		final float scaleText = getResources().getDisplayMetrics().density;	
		int textScaleSize = (int)(textSize * scaleText + 0.5f);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.RED);
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textScaleSize);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textBounds = new Rect();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if( !isSetHeightByDrawble() )
			return;

		int width = getMeasuredWidth();		

		setMeasuredDimension(width,drawableHeight);
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{		
		super.onDraw(canvas);		

		final int stepWidth = getWidth() / getSteps();
		final int viewHeight = getHeight();		

		final float scaleFactor = (float)viewHeight / drawableHeight;	
		final int scaledWidth = Math.round(drawableWidth * scaleFactor);		
		final int center = stepWidth / 2;		

		dest.set(0, 0, scaledWidth, viewHeight);
		dest.offset(stepWidth * getPosition() + center, 0);
		canvas.drawBitmap(pic, null, dest, null);

		// set week day text
		final float scaleText = getResources().getDisplayMetrics().density;
		textPaint.getTextBounds(getLeftText(), 0, 2, textBounds);
		final float y = drawableHeight / 2 + Math.abs(textBounds.bottom - textBounds.top)/2;
		canvas.drawText(getLeftText(), scaleText * 12 , y, textPaint);

		// Set text temperate
		final String textDrawable = getTextDrawable();
		textPaint.getTextBounds(textDrawable, 0, textDrawable.length(), textBounds);		
		final float tempY = drawableHeight / 2 + Math.abs(textBounds.centerY());
		final float tempX = dest.left + drawableWidth / 2 - Math.abs(textBounds.centerX());		
		canvas.drawText(textDrawable,tempX , tempY, textPaint);
	}		
}
