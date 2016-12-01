package fragments.android.exemple.com.logregfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetZobsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    TextView text;
    String poglavlje;
    String podpoglavlje;
    int extraI1;
    String rev, zakon;
    ArrayList<String> podLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_zobs);



        text = (TextView) findViewById(R.id.detZobsNaslov);

        Intent i = getIntent();
        if (i != null) {
            extraI1=i.getIntExtra("broj",0);
            rev = i.getStringExtra("revizija");
        }

        populateList();
        text.setText(poglavlje);
        klikItem();

    }

    private void populateList() {
        podLista = new ArrayList<>();

        try {

            preferences = getSharedPreferences(ZobsActivity.ZAKON, MODE_PRIVATE);
            zakon = preferences.getString(rev,ZobsActivity.NEMA);

            JSONArray jArr = new JSONArray(zakon);
            JSONObject jObj = jArr.getJSONObject(extraI1);
            JSONArray jArr1 = jObj.getJSONArray("poglavlje");
            poglavlje = jArr1.getString(0);


            for (int i=1; i<jArr1.length(); i++) {
                JSONObject jObj2 = jArr1.getJSONObject(i);
                JSONArray jArr2 = jObj2.getJSONArray("podpoglavlje");
                podpoglavlje = jArr2.getString(0);
                podLista.add(podpoglavlje.toUpperCase());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter2 = new MyAdapter();
        ListView listView = (ListView) findViewById(R.id.detZobsList);
        listView.setAdapter(adapter2);

    }

    private void klikItem() {
        final ListView list = (ListView) findViewById(R.id.detZobsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = new Intent(DetZobsActivity.this, ClanoviActivity.class);
                intent.putExtra("broj", extraI1);
                intent.putExtra("podbroj", position+1);
                intent.putExtra("revizija",rev);
                startActivity(intent);

            }
        });
    }

    private class MyAdapter extends ArrayAdapter {
        public MyAdapter() {
            super(DetZobsActivity.this, R.layout.poglavlje, podLista);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.poglavlje, parent, false);
            }

            String trentnoPoglavlje = podLista.get(position);

            TextView numeracija = (TextView) itemView.findViewById(R.id.poglavljeNumeracija);
            numeracija.setText(trentnoPoglavlje.substring(0,3));
            TextView poglavlje = (TextView) itemView.findViewById(R.id.poglavljeNaziv);
            poglavlje.setText(trentnoPoglavlje.substring(4,trentnoPoglavlje.length()));

            return itemView;
        }
    }

}
