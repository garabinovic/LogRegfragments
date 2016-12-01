package fragments.android.exemple.com.logregfragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LogRegActivity extends AppCompatActivity implements LoginFragment.FragmentListener, SignupFragment.FragmentListener, ForgotFragment.FragmentListener, ChangepassFragment.FragmentListener {

    public static final String FRAGMENT_TAG = "fragment_tag";
    private String userEmail, userPassword, msg, regFullname, regEmail, regPassword, forgotEmail, changeCode, chandePassword;

    public static final String VEC_REG = "User name is already taken";
    public static final String INV_REG = "Invalid request argument";
    public static final String LOG_ERR = "Wrong username/password";
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment==null){
            LoginFragment lf = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction().addToBackStack(null)
                    .add(R.id.fragment_container, lf, FRAGMENT_TAG)
                    .commit();
        }
    }


    public void signupHere(View view) {
        SignupFragment sf = new SignupFragment();
        getSupportFragmentManager()
                .beginTransaction().addToBackStack(null)
                .replace(R.id.fragment_container, sf, FRAGMENT_TAG).commit();


    }

    public void forgotPass(View view) {
        ForgotFragment ff = new ForgotFragment();
        getSupportFragmentManager()
                .beginTransaction().addToBackStack(null)
                .replace(R.id.fragment_container, ff, FRAGMENT_TAG).commit();
    }

    @Override
    public void onFragmentLoginFinish(String un, String pw) {
        userEmail = un;
        userPassword = pw;
        ComThread ct = new ComThread();
        ct.execute();
    }

    @Override
    public void onFragmentSignupFinish(String fn, String em, String pw) {
        regFullname = fn;
        regEmail = em;
        regPassword = pw;
        ComThread2 ct2 = new ComThread2();
        ct2.execute();

    }

    @Override
    public void onFragmentForgotFinish(String em) {
        forgotEmail = em;
        ComThread3 ct3 = new ComThread3();
        ct3.execute();
    }

    @Override
    public void onFragmentChangepassFinish(String cd, String ps, String rps) {
        changeCode = cd;
        chandePassword = ps;
        ComThread4 ct4 = new ComThread4();
        ct4.execute();
    }


    private class ComThread extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... voids) {
            uloguj(userEmail,userPassword);
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            msg=s;
            Toast.makeText(LogRegActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class ComThread2 extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... voids) {
            signup(regFullname,regEmail,regPassword);
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            msg=s;
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private class ComThread3 extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... voids) {
            forgot(forgotEmail);
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            msg=s;
//            if(msg.equalsIgnoreCase("ok")){
//                ChangepassFragment pif = new ChangepassFragment();
//                getSupportFragmentManager()
//                        .beginTransaction().addToBackStack(null)
//                        .replace(R.id.fragment_container, pif, FRAGMENT_TAG).commit();
//            } else {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }

            ChangepassFragment pif = new ChangepassFragment();
            getSupportFragmentManager()
                        .beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container, pif, FRAGMENT_TAG).commit();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class ComThread4 extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... voids) {
            changePassword(changeCode, chandePassword);
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            msg=s;
//            if(msg.equalsIgnoreCase("password changed")){
//                LoginFragment lf = new LoginFragment();
//                getSupportFragmentManager()
//                        .beginTransaction().addToBackStack(null)
//                        .replace(R.id.fragment_container, lf, FRAGMENT_TAG).commit();
//            } else {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }

            LoginFragment lf = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_container, lf, FRAGMENT_TAG).commit();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void uloguj(String un, String pw) {

        String logAdr = "http://developer.digitalcube.rs:8765/" +
                "user/login?username=" +
                un +
                "&password=" +
                pw;
        String postDataBody = null;

        FetchingJsonData loginFJD = new FetchingJsonData(logAdr);
        String odziv = loginFJD.postRequest(postDataBody);
        JSONObject job1 = null;

        try {
            job1 = new JSONObject(odziv);
            boolean daLiToken = job1.has("token");
            if (daLiToken){
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username",un);
                editor.putString("password",pw);
                editor.commit();
                Intent i = new Intent(LogRegActivity.this, ZobsActivity.class);
                i.putExtra("token", job1.getString("token"));
                startActivity(i);
                finish();
                return;
            }
            boolean daLiMessage = job1.has("message");
            if (daLiMessage){
                odziv = job1.getString("message");
                if(odziv!=null) {
                    msg = odziv;
                    return;
                }
                else {
                    msg = "AAAAAAAA";
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signup(String fn, String em, String pw) {
        String regAdr = "http://developer.digitalcube.rs:8765/" +
                "user/register?username=" +
                em;


        String postDataBody = "&password=" + pw + "&data={\"fullname\":\"" +fn+"\"}";

        FetchingJsonData loginFJD = new FetchingJsonData(regAdr);
        String answer = loginFJD.postRequest(postDataBody);
        JSONObject job1 = null;

        try {
            job1 = new JSONObject(answer);

            boolean daLiToken = job1.has("token");
            if (daLiToken){
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username",em);
                editor.putString("password",pw);
                editor.commit();
                Intent i = new Intent(LogRegActivity.this, ZobsActivity.class);
                i.putExtra("token", job1.getString("token"));
                startActivity(i);
                finish();
                return;
            }
            boolean daLiMessage = job1.has("message");
            if (daLiMessage){
                answer = job1.getString("message");
                if(answer!=null) {
                    msg = answer;
                    return;
                }
                else {
                    msg = "";
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;

    }

    private void forgot(String em) {
        String answer = "{}";

        String regFor = "http://developer.digitalcube.rs:8765/" +
                "user/password/forgot?username=" +
                em;


        String postDataBody = null;

        FetchingJsonData loginFJD = new FetchingJsonData(regFor);
        answer = loginFJD.putRequest(postDataBody);
        JSONObject job1 = null;

        try {
            job1 = new JSONObject(answer);

            boolean daLiMessage = job1.has("message");
            if (daLiMessage){
                answer = job1.getString("message");
                if(answer!=null) {
                    msg = answer;
                    return;
                }
                else {
                    msg = "AAAAAAAA";
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    private void changePassword(String cc, String cp) {
        String answer = "{}";
//        String hash = "h00000p4qkZN0diMYoHkNoK9VLGW6u17m6S3sbneK8D7B5rCzJUh6wPM0aZiRf2W";
//        String regFor = "http://developer.digitalcube.rs:8765" +
//                "/user/password/change?hash="
//                 +hash+
//                "&newpassword="
//                + cp;

        String regFor = "http://developer.digitalcube.rs:8765/" +
                "/user/password/change?hash="+
                cc +
                "&newpassword="
                + cp;



        String postDataBody = null;

        FetchingJsonData loginFJD = new FetchingJsonData(regFor);
        answer = loginFJD.postRequest(postDataBody);
        JSONObject job1 = null;

        try {
            job1 = new JSONObject(answer);

            boolean daLiMessage = job1.has("message");
            if (daLiMessage){
                answer = job1.getString("message");
                if(answer!=null) {
                    msg = answer;
                    return;
                }
                else {
                    msg = "AAAAAAAA";
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            msg = "AAAAAAAA";
        }
        return;
    }


        @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment instanceof LoginFragment){
            if (exit) {
                finish();}
            else{
                Toast.makeText(this, "Za izlazak iz aplikacije potvrdi 'Back'", Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        } else {
            super.onBackPressed();
        }


    }
}
