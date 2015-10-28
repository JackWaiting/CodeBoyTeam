/*
 * Copyright (c) 2014. CodeBoyTeam
 */

package com.astuetz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import com.codeboy.app.oschina.R;


/**
 * 类名 PagerSlidingTabStrip.java</br>
 * 创建日期 2014年4月28日</br>
 * @author astuetz (https://github.com/astuetz/PagerSlidingTabStrip)</br>
 * Email andreas.stuetz@gmail.com</br>
 * 更新时间 2014年4月28日 下午1:17:26</br>
 * 最后更新者 LeonLee</br>
 * 
 * 说明 ViewPage的指示界面
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {
	
	/** 自定义View的tab提供器*/
	public interface ViewTabProvider {
		public View getPageTabView(int position);
	}

	/** 带icon的 tab提供器*/
	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}

	// @formatter:off
	private static final int[] ATTRS = new int[] {
		android.R.attr.textSize,
		android.R.attr.textColor
    };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private LinearLayout tabsContainer;
	private ViewPager pager;

	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFF666666;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = false;
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	private int indicatorHeight = 8;
	private int underlineHeight = 2;
	private int dividerPadding = 12;
	private int tabPadding = 24;
	private int dividerWidth = 1;

	private int tabTextSize = 12;
	private int tabTextColor = 0xFF666666;
	private int tabTextColorStateListId = -1;
	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.BOLD;

	private int lastScrollX = 0;

	private int tabBackgroundResId = R.drawable.background_tab;

	private Locale locale;

	public PagerSlidingTabStrip(Context context) {
		this(context, null);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

		indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
		underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
		dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
		indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
		dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
		tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
		shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
		scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
		textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
		tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
		
		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}
	
	/**
	 * 设置ViewPage,默认加入监听器
	 * */
	public void setViewPager(ViewPager pager) {
		setViewPager(pager, true);
	}

	/**
	 * 设置ViewPager
	 * @param pager
	 * @param setupListener 是否设置OnPageChangeListener
	 * */
	public void setViewPager(ViewPager pager, boolean setupListener) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		if(setupListener) {
			pager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					PagerSlidingTabStrip.this.onPageSelected(position);
				}
				
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					PagerSlidingTabStrip.this.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}
				
				@Override
				public void onPageScrollStateChanged(int state) {
					PagerSlidingTabStrip.this.onPageScrollStateChanged(state);
				}
			});
		}
		notifyDataSetChanged();
	}

	/**
	 * Tab数目发生变化，通知更新
	 * */
	public void notifyDataSetChanged() {
		tabsContainer.removeAllViews();
		
		PagerAdapter adapter = pager.getAdapter();
		tabCount = adapter.getCount();

		if(adapter instanceof ViewTabProvider) {
			ViewTabProvider provider = (ViewTabProvider) adapter;
			for (int i = 0; i < tabCount; i++) {
				addTab(i, provider.getPageTabView(i));
			}
		} else if (adapter instanceof IconTabProvider) {
			IconTabProvider provider = (IconTabProvider) adapter;
			for (int i = 0; i < tabCount; i++) {
				addIconTab(i, provider.getPageIconResId(i));
			}
		} else {
			for (int i = 0; i < tabCount; i++) {
				addTextTab(i, adapter.getPageTitle(i).toString());
			}
		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}

				currentPosition = pager.getCurrentItem();
				scrollToChild(currentPosition, 0);
			}
		});

	}

	/**  添加一个文本的Tab*/
	private void addTextTab(final int position, String title) {
		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();

		addTab(position, tab);
	}

	/** 添加一个带Icon的tab*/
	private void addIconTab(final int position, int resId) {
		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);

		addTab(position, tab);
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position);
			}
		});

		tab.setPadding(tabPadding, 0, tabPadding, 0);
		tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
	}

	private void updateTabStyles() {
		for (int i = 0; i < tabCount; i++) {
			View v = tabsContainer.getChildAt(i);
			v.setBackgroundResource(tabBackgroundResId);

			if (v instanceof TextView) {

				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				if(tabTextColorStateListId != -1) {
					ColorStateList cs = getResources().getColorStateList(tabTextColorStateListId);
					tab.setTextColor(cs);
				} else{
					tab.setTextColor(tabTextColor);
				}

				// setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
				// pre-ICS-build
				if (textAllCaps) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						tab.setAllCaps(true);
					} else {
						tab.setText(tab.getText().toString().toUpperCase(locale));
					}
				}
			}
		}

	}

	private void scrollToChild(int position, int offset) {
		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		final int height = getHeight();

		// draw indicator line

		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
		}

		canvas.drawRect(lineLeft, height - indicatorHeight - underlineHeight, lineRight, height - underlineHeight, rectPaint);

		// draw underline

		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
		}
	}

	/** 
	 * ViewPage 滚动事件中的onPageScrolled
	 * {@link android.support.v4.view.ViewPager.OnPageChangeListener}
	 * */
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		currentPosition = position;
		currentPositionOffset = positionOffset;

		scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

		invalidate();
	}

	/** 
	 * ViewPage 滚动事件中的onPageScrollStateChanged
	 * {@link android.support.v4.view.ViewPager.OnPageChangeListener}
	 * */
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			scrollToChild(pager.getCurrentItem(), 0);
		}
	}

	/** 
	 * ViewPage 滚动事件中的onPageSelected
	 * {@link android.support.v4.view.ViewPager.OnPageChangeListener}
	 * */
	public void onPageSelected(int position) {
		
	}

	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	/**
	 * 获取指示器的颜色值的资源
	 * @param resId R.color.xx
	 * */
	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * 获取指示器的颜色值
	 * */
	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	/**
	 * 设置指示器的高度
	 * @param indicatorLineHeightPx px高度值
	 * */
	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	/**
	 * 获取指示器高度
	 * */
	public int getIndicatorHeight() {
		return indicatorHeight;
	}

	/**
	 * 设置底部分界线的颜色值
	 * @param underlineColor
	 * */
	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	/**
	 * 设置底部分界线的颜色值资源
	 * @param resId
	 * */
	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * 获取底部分界线的颜色值
	 * */
	public int getUnderlineColor() {
		return underlineColor;
	}
	
	/**
	 * 设置底部分界线的高度 
	 * @param underlineHeightPx px值高度
	 * */
	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	/**
	 * 获取底部分界线的高度
	 * */
	public int getUnderlineHeight() {
		return underlineHeight;
	}

	/**
	 * 设置左右tab之间的分隔竖线颜色值
	 * @param dividerColor
	 * */
	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	/**
	 * 设置左右tab之间的分隔竖线颜色值资源
	 * @param resId
	 * */
	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * 获取分隔线的颜色值
	 * */
	public int getDividerColor() {
		return dividerColor;
	}

	/**
	 * 设置分隔线的大小
	 * */
	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	/**
	 * 获取分隔线的大小
	 * */
	public int getDividerPadding() {
		return dividerPadding;
	}

	/**
	 * 设置滚动时的偏移值
	 * @param scrollOffsetPx
	 * */
	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	/**
	 * 获取滚动时的偏移值
	 * */
	public int getScrollOffset() {
		return scrollOffset;
	}

	/**
	 * 设置是否挤满界面
	 * @param shouldExpand
	 * */
	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	/**
	 * 获取是否挤满界面
	 * */
	public boolean getShouldExpand() {
		return shouldExpand;
	}

	/**
	 * 获取是否全部使用大写字母来呈现文本
	 * */
	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	/** 
	 * 设置是否全部使用大写字母来呈现文本
	 * @param textAllCaps
	 * */
	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	/**
	 * 设置tab文本的大小
	 * @param textSizePx
	 * */
	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	/**
	 * 获取tab文本的大小
	 * */
	public int getTextSize() {
		return tabTextSize;
	}

	/**
	 * 设置文本的颜色值
	 * @param textColor
	 * */
	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		this.tabTextColorStateListId = -1;
		updateTabStyles();
	}

	/**
	 * 设置文本的颜色值资源
	 * @param resId
	 * */
	public void setTextColorResource(int resId) {
		setTextColor(getResources().getColor(resId));
	}
	
	public void setTextColorStateList(int resId) {
		this.tabTextColorStateListId = resId;
		updateTabStyles();
	}

	/** 获取文本的颜色值*/
	public int getTextColor() {
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	/**
	 * 设置tab背景的点击的资源值
	 * @param resId
	 * */
	public void setTabBackground(int resId) {
		this.tabBackgroundResId = resId;
	}

	/**
	 * 获取tab的背景资源
	 * */
	public int getTabBackground() {
		return tabBackgroundResId;
	}
	
	/**
	 * 设置左右的padding
	 * */
	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	/**
	 * 左右padding大小
	 * */
	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}