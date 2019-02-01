package id.gits.gitstextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import id.gits.gitstextview.R;

public class GitsTextView extends AppCompatTextView {

    private int mFontFaceRes = 0;
    private int mFontWeight = 0;
    private int mFontCurrency = 0;

    public static final int LATO = 1;
    public static final int ROBOTO = 2;
    public static final int MONTSERRAT = 3;
    public static final int AVENIR = 4;

    public static final int REGULAR = 0;
    public static final int THIN = 1;
    public static final int LIGHT = 2;
    public static final int ITALIC = 3;
    public static final int BOLD = 4;
    public static final int HEAVY = 5;
    public static final int BOLD_ITALIC = 6;
    public static final int HEAVY_ITALIC = 7;
    public static final int THIN_ITALIC = 8;
    public static final int LIGHT_ITALIC = 9;

    public static final String DEFAULT_FONT_DIR = "fonts/";

    public GitsTextView(Context context) {
        super(context, null);
        inflateView(null);
    }

    public GitsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(attrs);
    }

    public GitsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void inflateView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.GitsTextView);
            mFontFaceRes = typedArray.getInteger(R.styleable.GitsTextView_fontFace, 0);
            mFontWeight = typedArray.getInteger(R.styleable.GitsTextView_fontTextStyle, 0);
            mFontCurrency = typedArray.getInteger(R.styleable.GitsTextView_currencyType, 0);
            changeFontType();
            typedArray.recycle();
        }
    }

    private void changeFontType() {
        String fontName = getContext().getString(R.string.roboto);

        switch (mFontFaceRes) {
            case LATO:
                fontName = getContext().getString(R.string.lato);
                break;
            case ROBOTO:
                fontName = getContext().getString(R.string.roboto);
                break;
            case MONTSERRAT:
                fontName = getContext().getString(R.string.montserrat);
                break;
            case AVENIR:
                fontName = getContext().getString(R.string.avenir);
                break;
        }

        String dirName = fontName;

        switch (mFontWeight) {
            case THIN:
                fontName += getContext().getString(R.string.thin_extra);
                break;
            case LIGHT:
                fontName += getContext().getString(R.string.light_extra);
                break;
            case ITALIC:
                fontName += getContext().getString(R.string.italic_extra);
                break;
            case BOLD:
                fontName += getContext().getString(R.string.bold_extra);
                break;
            case HEAVY:
                fontName += getContext().getString(R.string.heavy_extra);
                break;
            case BOLD_ITALIC:
                fontName += getContext().getString(R.string.bold_italic_extra);
                break;
            case HEAVY_ITALIC:
                fontName += getContext().getString(R.string.heavy_italic_extra);
                break;
            case THIN_ITALIC:
                fontName += getContext().getString(R.string.thin_italic_extra);
                break;
            case LIGHT_ITALIC:
                fontName += getContext().getString(R.string.light_italic_extra);
                break;

        }

        fontName = getContext().getString(R.string.ttf_format, fontName);
        try {
            Typeface mTypeFace = Typeface.createFromAsset(getContext().getAssets(),
                    DEFAULT_FONT_DIR + dirName + "/" + fontName);

            setTypeface(mTypeFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getFontWeight() {
        return mFontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.mFontWeight = fontWeight;
        changeFontType();
    }

    public int getTypeFace() {
        return mFontFaceRes;
    }

    public void setTypeFace(int typeFace) {
        mFontFaceRes = typeFace;
        changeFontType();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.NORMAL);
    }

}
