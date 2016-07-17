package id.prasetiyo.a3dimageretrieval.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.math.vector.Vector3;

import id.prasetiyo.a3dimageretrieval.R;

/**
 * Created by aoktox on 26/06/16.
 */
public class LoadModelFragment extends MainFragment {

    @Override
    public MainRenderer createRenderer() {
        return new LoadModelRenderer(getActivity());
    }

    private final class LoadModelRenderer extends MainRenderer {
        final Bundle bundle = getArguments();

        private DirectionalLight mLight;
        private Object3D mObjectGroup;
        private Animation3D mCameraAnim, mLightAnim;

        public LoadModelRenderer(Context context) {
            super(context);
        }

        @Override
        protected void initScene() {
            if (bundle == null || !bundle.containsKey(BUNDLE_OBJ_NAME)) {
                throw new IllegalArgumentException(getClass().getSimpleName()
                        + " requires " + BUNDLE_OBJ_NAME
                        + " argument at runtime!");
            }
            mLight = new DirectionalLight();
            mLight.setPosition(1.0, 0.0, 0.0);
            mLight.setPower(1.5f);
            mLight.setLookAt(Vector3.ZERO);
            mLight.enableLookAt();

            getCurrentScene().setBackgroundColor(0.7f, 0.7f, 0.7f, 1.0f);

            getCurrentScene().addLight(mLight);
            getCurrentCamera().setZ(16);

            //LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.multiobjects_obj);
            //LoaderOBJ objParser = new LoaderOBJ(new LoadModelRenderer(getActivity()),f);
            //LoaderOBJ objParser = new LoaderOBJ(this,"Download/3D/multiobjects_obj");
            try {
                LoaderOBJ objParser = new LoaderOBJ(this,"3D/"+bundle.getString(BUNDLE_OBJ_NAME));
                objParser.parse();
                mObjectGroup = objParser.getParsedObject();
                getCurrentScene().addChild(mObjectGroup);

                mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
                mCameraAnim.setDurationMilliseconds(8000);
                mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
                mCameraAnim.setTransformable3D(mObjectGroup);
            } catch (ParsingException e) {
                e.printStackTrace();
                return;
            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
                    new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
                    360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

            mLightAnim.setDurationMilliseconds(3000);
            mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
            mLightAnim.setTransformable3D(mLight);

            getCurrentScene().registerAnimation(mCameraAnim);
            getCurrentScene().registerAnimation(mLightAnim);

            mCameraAnim.play();
            mLightAnim.play();
            ArcballCamera arcball = new ArcballCamera(mContext, ((Activity)mContext).findViewById(R.id.layout_hasil));
            arcball.setPosition(4, 4, 4);
            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);
        }
    }
}
