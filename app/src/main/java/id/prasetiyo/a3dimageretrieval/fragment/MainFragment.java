package id.prasetiyo.a3dimageretrieval.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import id.prasetiyo.a3dimageretrieval.R;

/**
 * Created by aoktox on 26/06/16.
 */
public abstract class MainFragment  extends Fragment implements IDisplay {

    public static final String BUNDLE_EXAMPLE_TITLE = "BUNDLE_EXAMPLE_TITLE";
    public static final String BUNDLE_OBJ_NAME = "BUNDLE_OBJ_NAME";

    //protected ProgressBar mProgressBarLoader;
    protected String           mExampleTitle;
    protected FrameLayout mLayout;
    protected ISurface mRajawaliSurface;
    protected ISurfaceRenderer mRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(BUNDLE_EXAMPLE_TITLE)) {
            throw new IllegalArgumentException(getClass().getSimpleName()
                    + " requires " + BUNDLE_EXAMPLE_TITLE
                    + " argument at runtime!");
        }
        mExampleTitle = bundle.getString(BUNDLE_EXAMPLE_TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the view
        mLayout = (FrameLayout) inflater.inflate(getLayoutID(), container, false);

        // Find the TextureView
        mRajawaliSurface = (ISurface) mLayout.findViewById(R.id.rajwali_surface);

        getActivity().setTitle(mExampleTitle);

        // Create the renderer
        mRenderer = createRenderer();
        onBeforeApplyRenderer();
        applyRenderer();
        return mLayout;
    }

    protected void onBeforeApplyRenderer() {

    }

    protected void applyRenderer() {
        mRajawaliSurface.setSurfaceRenderer(mRenderer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLayout != null)
            mLayout.removeView((View) mRajawaliSurface);
    }

    @Override
    public int getLayoutID() {
        return R.layout.rajawali_textureview_fragment;
    }

    protected abstract class MainRenderer extends Renderer {

        public MainRenderer(Context context) {
            super(context);
        }

        @Override
        public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            super.onRenderSurfaceCreated(config, gl, width, height);
        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
        }
    }
}