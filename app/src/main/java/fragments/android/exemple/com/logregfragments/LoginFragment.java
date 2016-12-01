package fragments.android.exemple.com.logregfragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private FragmentListener mListener;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof FragmentListener)) throw new AssertionError();
        mListener = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View loginF  =  inflater.inflate(R.layout.fragment_login, container, false);

        SharedPreferences preferences = loginF.getContext().getSharedPreferences("login",MODE_PRIVATE);

        email = (EditText) loginF.findViewById(R.id.loginEditEmail);
        email.setText(preferences.getString("username",""), TextView.BufferType.EDITABLE);

        password = (EditText) loginF.findViewById(R.id.loginEditPassword);
        password.setText(preferences.getString("password",""), TextView.BufferType.EDITABLE);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        Button loginMem  = (Button) loginF.findViewById(R.id.loginBTN);
        loginMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginMem();
            }
        });

        return loginF;
    }

    private void loginMem() {

        String em = email.getText().toString();
        String pw = password.getText().toString();
        if(!em.equals("") && !pw.equals("")){
            if (mListener == null){
                throw  new AssertionError();
            }
            mListener.onFragmentLoginFinish(em, pw);
        } else {
            Toast.makeText(getContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
        }

    }

    public interface FragmentListener {
        void  onFragmentLoginFinish(String un, String pw);
    }




}
