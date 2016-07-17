package id.prasetiyo.a3dimageretrieval;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import id.prasetiyo.a3dimageretrieval.fragment.LoadModelFragment;
import id.prasetiyo.a3dimageretrieval.fragment.MainFragment;
import id.prasetiyo.a3dimageretrieval.models.Similar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //private Button search_btn;
    private String IP;
    private String endpoint;
    private final int MAX = 572;
    private final int RESULT_SETTINGS = 5758;
    private EditText search_txt;
    private FrameLayout frameLayout;
    final private String[] similar={"colorvoxel","fouriervoxel","multifourier"};
    private ArrayList<Similar> similars = new ArrayList<Similar>();
    int obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_btn = (Button) findViewById(R.id.button);
        getIP();
        final Button btn_show_detail = (Button) findViewById(R.id.btn_show_detail);
        search_txt = (EditText) findViewById(R.id.txt_input_search);
        frameLayout = (FrameLayout) findViewById(R.id.layout_hasil);
        frameLayout.setVisibility(View.INVISIBLE);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.INVISIBLE);
                if (search_txt.getText().toString().equals("")||search_txt==null){
                    Toast.makeText(MainActivity.this, "Objek yang tersedia 1 sampai "+MAX, Toast.LENGTH_SHORT).show();
                    return;
                }
                obj = Integer.parseInt(search_txt.getText().toString());
                if(obj<1||obj>MAX){
                    Toast.makeText(MainActivity.this, "Objek yang tersedia 1 sampai "+MAX, Toast.LENGTH_SHORT).show();
                    return;
                }
                frameLayout.setVisibility(View.VISIBLE);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                final Bundle bundle = new Bundle();
                bundle.putString(MainFragment.BUNDLE_EXAMPLE_TITLE, "3D Image Retrieval");
                bundle.putString(MainFragment.BUNDLE_OBJ_NAME, obj+"_obj");

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment obj = new LoadModelFragment();
                obj.setArguments(bundle);
                transaction.replace(R.id.layout_hasil, obj);
                transaction.commit();
                btn_show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (IP.equalsIgnoreCase("NULL")){
                            Toast.makeText(MainActivity.this, "IP Web Service belum diatur.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        show_similar();
                        //startActivity(new Intent(getApplicationContext(),LoaderActivity.class));
                    }
                });
            }
        });
    }
    private void show_similar(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_show_similar, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Pilih metode");
        alertDialog.setItems(similar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSimilar(obj,i);
            }
        });

        alertDialog.show();
    }

    private void getSimilar(int obj, final int method){
        final ProgressDialog progress = ProgressDialog.show(MainActivity.this, "Loading...","Silakan tunggu sebentar", true);
        OkHttpClient client = new OkHttpClient();
        String url = endpoint+similar[method]+"/"+obj;
        Log.d("URL", " "+url);
        final String[] hasil = {null};
        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(MainActivity.this, "An error occured. Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });

                if (!response.isSuccessful()) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "An error occured. Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    try {
                        Similar[] temp = new Gson().fromJson(response.body().string(),Similar[].class);
                        similars = new ArrayList<Similar>(Arrays.asList(temp));
                        Intent i = new Intent(getApplicationContext(),SimilarActivity.class);
                        i.putExtra("similar",similars);
                        i.putExtra("metode",similar[method]);
                        startActivity(i);
                    }catch (final Exception e){
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                Intent i = new Intent(this, APISettingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_SETTINGS){
            getIP();
        }
    }

    private void getIP(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        IP = sharedPrefs.getString("ip_web_service", "NULL");
        endpoint = "http://"+IP+"/api/v1/";
    }
}