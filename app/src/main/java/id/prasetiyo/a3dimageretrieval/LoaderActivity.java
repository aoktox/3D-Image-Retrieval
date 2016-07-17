package id.prasetiyo.a3dimageretrieval;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import id.prasetiyo.a3dimageretrieval.fragment.LoadModelFragment;
import id.prasetiyo.a3dimageretrieval.fragment.MainFragment;
import id.prasetiyo.a3dimageretrieval.models.Similar;

public class LoaderActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        Bundle b = getIntent().getExtras();
        final Similar similar = (Similar) getIntent().getSerializableExtra("similar");
        show_3D(similar);
    }

    private void show_3D(Similar s){
        final Bundle bundle = new Bundle();
        bundle.putString(MainFragment.BUNDLE_EXAMPLE_TITLE, "Objek : "+s.getId()+" | Jarak : "+s.getJarak());
        bundle.putString(MainFragment.BUNDLE_OBJ_NAME, s.getId()+"_obj");

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment obj = new LoadModelFragment();
        obj.setArguments(bundle);
        transaction.replace(R.id.layout_hasil, obj);
        transaction.commit();
    }
}
