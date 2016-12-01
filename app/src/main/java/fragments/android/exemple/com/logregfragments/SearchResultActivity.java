package fragments.android.exemple.com.logregfragments;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    ArrayList<String> sviParagrafi, pretrazeniParagrafi;
    String tRec = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())){
            handleSearch(getIntent().getStringExtra(SearchManager.QUERY));
        }

        createSearchList(pretrazeniParagrafi.size());
    }


    public void createSearchList(int i) {
        LinearLayout ly = (LinearLayout) findViewById(R.id.linearSearch);
        if(i==0){
            TextView newTextView = (TextView) getLayoutInflater().inflate(R.layout.add_search_text, null);
            String poruka = "<i>Nisu pronađeni rezultati pretrage za traženu reč: </i><br/><br/><b><font color='#EE0000'>" + tRec + "</font></b>";
            newTextView.setText(Html.fromHtml(poruka));
            newTextView.setTextSize(20);
            ly.addView(newTextView);
            newTextView.setGravity(Gravity.CENTER);
        }
        else{
            TextView newTextView = (TextView) getLayoutInflater().inflate(R.layout.add_search_text, null);
            String poruka = "<i>Rezultati pretrage za traženu reč: </i><b><font color='#EE0000'>" + tRec + "</font></b><br/>";
            newTextView.setText(Html.fromHtml(poruka));
            newTextView.setTextSize(20);
            ly.addView(newTextView);
            newTextView.setGravity(Gravity.CENTER);

            for (int j = 1; j <= i; j++) {
                newTextView = (TextView) getLayoutInflater().inflate(R.layout.add_search_text, null);
                newTextView.setText(Html.fromHtml(pretrazeniParagrafi.get(j-1)));
                newTextView.setPadding(12,12,12,12);
                if (j%2==0){newTextView.setBackgroundColor(Color.parseColor("#E1E6E9"));} else {newTextView.setBackgroundColor(Color.parseColor("#F7CA76"));}
                ly.addView(newTextView);
            }
        }
    }



    private void popuniSviParagrafiLisu() {
        sviParagrafi = new ArrayList<>();
        try {
            JSONArray jArr = new JSONArray(ZobsActivity.zakon);
            for (int i=1; i<jArr.length(); i++){
                JSONObject jObj = jArr.getJSONObject(i);
                JSONArray jArr1 = jObj.getJSONArray("poglavlje");
                sviParagrafi.add(jArr1.getString(0));

                for (int k=1; k<jArr1.length(); k++){
                    JSONObject jObj2 = jArr1.getJSONObject(k);
                    JSONArray jArr2 = jObj2.getJSONArray("podpoglavlje");
                    if (!jArr2.getString(0).equals("")){
                        sviParagrafi.add(jArr1.getString(0)+"<br/>"+jArr2.getString(0));
                    }

                    for (int j=1; j<jArr2.length(); j++){
                        JSONObject jObj3 = jArr2.getJSONObject(j);
                        sviParagrafi.add(jObj3.getString("clan"));
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void handleSearch(String searchQuery) {
        tRec = searchQuery;
        pretrazeniParagrafi = new ArrayList<>();
        popuniSviParagrafiLisu();
        String[] upitVar = {tRec, tRec.toLowerCase(), tRec.toUpperCase(), tRec.toUpperCase().substring(0,1)+tRec.substring(1, searchQuery.length())};
        for (int i=0; i<sviParagrafi.size(); i++){
            if (sviParagrafi.get(i).contains(tRec) || sviParagrafi.get(i).contains(tRec.toLowerCase()) || sviParagrafi.get(i).contains(tRec.toUpperCase()) || sviParagrafi.get(i).contains(tRec.toUpperCase().substring(0,1)+tRec.substring(1, searchQuery.length())) ){
                String chl = sviParagrafi.get(i);
                for (String upit: upitVar){
                    String chl2 = chl.replaceAll(upit, "<b><font color='#EE0000'>" + upit + "</font></b>");
                    chl = chl2;
                }
                pretrazeniParagrafi.add(chl);
            }


//            if (searchQuery.charAt(0) == searchQuery.toUpperCase().charAt(0)){
//                if(sviParagrafi.get(i).contains(searchQuery)){
//                    chl = sviParagrafi.get(i).replaceAll(searchQuery, "<b><font color='#EE0000'>"+searchQuery+"</font></b>");
//                    pretrazeniParagrafi.add(chl);
//                } else if (sviParagrafi.get(i).contains(searchQuery.toLowerCase())){
//                    chl = sviParagrafi.get(i).replaceAll(searchQuery.toLowerCase(), "<b><font color='#EE0000'>"+searchQuery.toLowerCase()+"</font></b>");
//                    pretrazeniParagrafi.add(chl);
//                }
//            } else {
//                String x = searchQuery.toUpperCase().substring(0,1)+searchQuery.substring(1, searchQuery.length());
//                if(sviParagrafi.get(i).contains(searchQuery)){
//                    chl = sviParagrafi.get(i).replaceAll(searchQuery, "<b><font color='#EE0000'>"+searchQuery+"</font></b>");
//                    pretrazeniParagrafi.add(chl);
//                } else if(sviParagrafi.get(i).contains(x)){
//                    chl = sviParagrafi.get(i).replaceAll(x, "<b><font color='#EE0000'>"+x+"</font></b>");
//                    pretrazeniParagrafi.add(chl);
//                }
//            }
        }
    }
}
