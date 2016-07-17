package id.prasetiyo.a3dimageretrieval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import id.prasetiyo.a3dimageretrieval.adapter.SimilarAdapter;
import id.prasetiyo.a3dimageretrieval.models.Similar;

public class SimilarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);
        this.setTitle("Metode : "+getIntent().getExtras().getString("metode"));
        ListView lv = (ListView) findViewById(R.id.listView_similar);
        final ArrayList<Similar> similars = (ArrayList<Similar>) getIntent().getSerializableExtra("similar");

        SimilarAdapter adapter = new SimilarAdapter(this, similars);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),LoaderActivity.class);
                intent.putExtra("similar",similars.get(i));
                startActivity(intent);
            }
        });
    }
}
