package fragments.android.exemple.com.logregfragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZobsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CheckRev chekRev;

    private static final String REV_ADR = "http://developer.digitalcube.rs:8765/zobs-backend/get_rev";
    private static final String ZAKON_ADR = "http://developer.digitalcube.rs:8765/zobs-backend/get_zobs";

    public static final String ZAKON = "zakon";
    public static final String NEMA = "nema nista";

    private String token;

    List<String> poglavlja = new ArrayList<>();
    List<String> podpoglavlja = new ArrayList<>();
    SharedPreferences preferences;
    static String rev;
    static String zakon;
    TextView naslov, objava;
    String sObjava, sNaslov;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zobs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent i = getIntent();
        if(i!=null){
            token = i.getStringExtra("token");
        }

        ArrayList<String> params = new ArrayList<>();
        params.add(REV_ADR);
        chekRev = new CheckRev(this);
        chekRev.execute();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void populateListView() {
        try {
            preferences = getSharedPreferences(ZAKON, MODE_PRIVATE);
            zakon = preferences.getString(rev,NEMA);

            JSONArray jArr = new JSONArray(zakon);
            JSONObject jObj = jArr.getJSONObject(0);

            sObjava = jObj.getString("objava");
            sNaslov = jObj.getString("naslov");

            for (int i=1; i<jArr.length(); i++){
                JSONObject jObj1 = jArr.getJSONObject(i);
                JSONArray jArr1 = jObj1.getJSONArray("poglavlje");
                poglavlja.add(jArr1.getString(0));

                JSONObject jObj2 = jArr1.getJSONObject(1);
                JSONArray jArr2 = jObj2.getJSONArray("podpoglavlje");
                podpoglavlja.add(jArr2.getString(0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new MyAdapter();
        ListView list = (ListView) findViewById(R.id.zobsList);
        list.setAdapter(adapter);
    }


    private class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter() {
            super(ZobsActivity.this, R.layout.poglavlje, poglavlja);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView==null){
                itemView = getLayoutInflater().inflate(R.layout.poglavlje, parent, false);
            }

            String trentnoPoglavlje = poglavlja.get(position);

            TextView numeracija = (TextView) itemView.findViewById(R.id.poglavljeNumeracija);
            numeracija.setText(trentnoPoglavlje.substring(0,trentnoPoglavlje.indexOf(" ")));
            TextView poglavlje = (TextView) itemView.findViewById(R.id.poglavljeNaziv);
            poglavlje.setText(trentnoPoglavlje.substring(trentnoPoglavlje.indexOf(" "), trentnoPoglavlje.length()).trim());

            return itemView;
        }
    }

    private void klikItem() {
        final ListView list = (ListView) findViewById(R.id.zobsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                if (podpoglavlja.get(position).equals("")){
                    Intent intent = new Intent(ZobsActivity.this,ClanoviActivity.class);
                    intent.putExtra("broj", position+1);
                    intent.putExtra("revizija", rev);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ZobsActivity.this, DetZobsActivity.class);
                    intent.putExtra("broj", position+1);
                    intent.putExtra("revizija", rev);
                    startActivity(intent);
                }
            }
        });
    }


    private class CheckRev extends AsyncTask<Void,Void,String> {

        ProgressDialog pd;
        Context context;

        public CheckRev(Context context) {
            this.context = context;
            pd = new ProgressDialog(context);
            pd.setMessage("update...");
        }

        @Override
        protected void onPreExecute() {
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            JSONObject jRevObj = null;
            String rev="0";
            // Dovlacenje informacije o aktuelnoj verziji
            FetchingJsonData fetchJD1 = new FetchingJsonData(REV_ADR);
            try {
                jRevObj = new JSONObject(fetchJD1.getRequest());
                rev = jRevObj.getString("res");
                Log.d("GRB", "REVIZIJA "+rev);
                //Pozivamo SharedPreferences i proveravamo da li vec imamo aktuelnu verziju
                preferences = getSharedPreferences(ZAKON,MODE_PRIVATE);
                //Ukoliko imamo prosledjujemo njenu vrednost dalje a ukoliko ne skidamo novu i azuriramo vraziju
                if(preferences.getString(rev,NEMA).equals(NEMA)){
                    FetchingJsonData fetchJD2 = new FetchingJsonData(ZAKON_ADR);
                    JSONObject job = new JSONObject(fetchJD2.getRequest());
                    SharedPreferences.Editor editor = preferences.edit();
                    String noviZakon = job.getString("res");
                    editor.putString(rev,noviZakon);
//                    Log.d("GRB", "ZAKON "+noviZakon);
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Prosledjujemo vrednost aktuelne verzije za potrebe kljuca za SharedPreferences
            return rev;
        }

        @Override
        protected void onPostExecute(String s) {

            rev = s;
            populateListView();
            klikItem();
            objava = (TextView) findViewById(R.id.zobsPrednaslov);
            naslov= (TextView) findViewById(R.id.zobsNaslov);
            objava.setText(sObjava);
            naslov.setText(sNaslov);
            pd.dismiss();
        }
    }

    private class ComThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String logoutAdr = "http://developer.digitalcube.rs:8765/" +
                    "user/logout";
            String postDataBody = null;
            FetchingJsonData loginFJD = new FetchingJsonData(logoutAdr);
            loginFJD.postRequest(token);
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                ComThread ct = new ComThread();
                ct.execute();
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Za izlazak iz aplikacije potvrdi 'Back'",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()<3){
                    Toast.makeText(ZobsActivity.this, "Tražena reč mora imati bar 3 karaktera", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>20){
                    Toast.makeText(ZobsActivity.this, "Tražena reč ili fraza ne mogu imati više od 20 karaktera", Toast.LENGTH_SHORT).show();
                    searchView.setQuery(newText.substring(0,20), false);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent i = new Intent(ZobsActivity.this, AboutActivity.class);
            startActivity(i);
//            // Handle the camera action
//            Toast.makeText(this, "ABOUT", Toast.LENGTH_SHORT).show();
//            AboutFragment aboutFragment = new AboutFragment();
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction().addToBackStack(null).replace(R.id.content_zobs, aboutFragment, aboutFragment.getTag()).commit();
        } else if (id == R.id.nav_logout) {
            ComThread ct = new ComThread();
            ct.execute();
            Intent i = new Intent(ZobsActivity.this, LogRegActivity.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
