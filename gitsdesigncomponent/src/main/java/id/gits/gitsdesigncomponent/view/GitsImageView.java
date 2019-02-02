package id.gits.gitsdesigncomponent.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;
import id.gits.gitsdesigncomponent.R;
import id.gits.gitsdesigncomponent.utils.Instance;

public class GitsImageView extends FrameLayout {

    private ImageView mImageView;
    private ProgressBar mProgressIndicator;
    private String mImageUrl;
    private Integer mImageRes;
    private ImageView.ScaleType mImageScale = ImageView.ScaleType.CENTER_CROP;

    private Integer mPlaceHolder;
    private Integer mError;


    public GitsImageView(Context context) {
        super(context, null);
        inflateImage(null);
    }

    public GitsImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        inflateImage(attrs);
    }

    public GitsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateImage(attrs);
    }

    private void inflateImage(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.gits_imageview_layout, null);
        mImageView = view.findViewById(R.id.imageContent);
        mProgressIndicator = view.findViewById(R.id.loadingIndicator);
        addView(view);


        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.GitsImageView);

            Integer scaleInt = typedArray.getInteger(R.styleable.GitsImageView_scaleType, 0);

            mImageUrl = typedArray.getString(R.styleable.GitsImageView_imageUrl);
            mImageRes = typedArray.getInteger(R.styleable.GitsImageView_src, 0);
            mPlaceHolder = typedArray.getInteger(R.styleable.GitsImageView_src, 0);
            mError = typedArray.getInteger(R.styleable.GitsImageView_src, 0);

            Integer loadingBackground = typedArray.getInteger(R.styleable.GitsImageView_src, 0);
            Integer progressColor = typedArray.getInteger(R.styleable.GitsImageView_src, 0);


            mImageScale = ImageView.ScaleType.CENTER_CROP;


            switch (scaleInt) {
                case 1:
                    mImageScale = ImageView.ScaleType.CENTER_CROP;
                    break;
                case 2:
                    mImageScale = ImageView.ScaleType.CENTER;
                    break;
                case 3:
                    mImageScale = ImageView.ScaleType.FIT_CENTER;
                    break;
                case 4:
                    mImageScale = ImageView.ScaleType.FIT_XY;
                    break;
            }

            mImageView.setScaleType(mImageScale);
            doImageRequest();
            typedArray.recycle();
        }
    }

    private void doImageRequest() {
        Object imageValue = mImageUrl != null ? mImageUrl : (mImageRes != 0 ? mImageRes : null);
        showLoading();
        if (imageValue != null) {
            RequestCreator creator;

            if (imageValue instanceof Integer) {
                creator = Instance.getPicassoInstance(getContext()).load((Integer) imageValue);
            } else {
                creator = Instance.getPicassoInstance(getContext()).load(String.valueOf(imageValue));
            }

            if (mPlaceHolder != 0) {
                creator.placeholder(mPlaceHolder);
            }


            creator.into(mImageView, new Callback() {
                @Override
                public void onSuccess() {
                    dismissLoading();
                }

                @Override
                public void onError(Exception e) {
                    if (mError != 0) {
                        mImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), mError));
                    }
                    dismissLoading();
                }
            });


        }
    }


    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
        if (mImageView != null) {
            doImageRequest();
        }
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void showLoading() {
        mProgressIndicator.setVisibility(View.VISIBLE);
        mProgressIndicator.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500);
    }

    public void dismissLoading() {
        mProgressIndicator.animate()
                .scaleX(0.1f)
                .scaleY(0.1f)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressIndicator.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

}
