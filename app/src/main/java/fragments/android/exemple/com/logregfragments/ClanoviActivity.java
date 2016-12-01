package fragments.android.exemple.com.logregfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ClanoviActivity extends AppCompatActivity {


    SharedPreferences preferences;
    List<String> clanoviLista;
    int extraI1, extraI2;
    String rev, zakon, pog, podPog;
    JSONArray jArr = null;
    ExpandableTextView etv;
    TextView naslov, podnaslov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clanovi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        if (i!=null){
            extraI1 = i.getIntExtra("broj",0);
            extraI2 = i.getIntExtra("podbroj",1000);
            rev = i.getStringExtra("revizija");
        }

        populateList();

        naslov = (TextView) findViewById(R.id.clanoviNaslov);
        naslov.setText(pog);
        podnaslov = (TextView) findViewById(R.id.clanoviPodnaslov);
        if (!podPog.equals(""))
        podnaslov.setText(podPog);


    }

    private void populateList() {
        clanoviLista = new ArrayList<>();
        String cl;
        preferences = getSharedPreferences(ZobsActivity.ZAKON, MODE_PRIVATE);
        zakon = preferences.getString(rev,ZobsActivity.NEMA);

        if (extraI2!=1000){
            try {
                jArr = new JSONArray(zakon);
                JSONObject jObj = jArr.getJSONObject(extraI1);
                JSONArray jArr1 = jObj.getJSONArray("poglavlje");
                pog = jArr1.getString(0);
                JSONObject jObj1 = jArr1.getJSONObject(extraI2);
                JSONArray jArr2 = jObj1.getJSONArray("podpoglavlje");
                podPog = jArr2.getString(0);
                for (int i=1; i<jArr2.length(); i++){
                    JSONObject jObj2 = jArr2.getJSONObject(i);
                    cl = Html.fromHtml(jObj2.getString("clan")).toString();
                    clanoviLista.add(cl);

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else {
            try {
                jArr = new JSONArray(zakon);
                JSONObject jObj = jArr.getJSONObject(extraI1);
                JSONArray jArr1 = jObj.getJSONArray("poglavlje");
                pog = jArr1.getString(0);
                JSONObject jObj1 = jArr1.getJSONObject(1);
                JSONArray jArr2 = jObj1.getJSONArray("podpoglavlje");
                podPog = jArr2.getString(0);
                for (int i=1; i<jArr2.length(); i++){
                    JSONObject jObj2 = jArr2.getJSONObject(i);
                    cl = Html.fromHtml(jObj2.getString("clan")).toString();
                    clanoviLista.add(cl);

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        ArrayAdapter<String> adapter3 = new MyListAdapter();
        ListView listV = (ListView) findViewById(R.id.clanoviLista);
        listV.setAdapter(adapter3);
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
                super(ClanoviActivity.this, R.layout.clanovi, clanoviLista);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView = convertView;
                if(itemView==null){
                    itemView = getLayoutInflater().inflate(R.layout.clanovi,parent,false);
                }
                String trenutni = clanoviLista.get(position);

                etv = (ExpandableTextView) itemView.findViewById(R.id.lorem_ipsum);
                etv.setText(trenutni);

                return itemView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
