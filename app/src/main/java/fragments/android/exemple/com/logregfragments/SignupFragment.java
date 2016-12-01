package fragments.android.exemple.com.logregfragments;


import android.content.Context;
import android.graphics.Typeface;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private EditText fullname, email, password, rPassword;
    private FragmentListener mListener;


    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof LoginFragment.FragmentListener)) throw new AssertionError();
        mListener = (FragmentListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View singupF = inflater.inflate(R.layout.fragment_signup, container, false);

        fullname = (EditText) singupF.findViewById(R.id.signupEditFullname);
        email = (EditText) singupF.findViewById(R.id.signupEditEmail);

        password = (EditText) singupF.findViewById(R.id.signupEditPasword);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        rPassword = (EditText) singupF.findViewById(R.id.signupEditRPassword);
        rPassword.setTypeface(Typeface.DEFAULT);
        rPassword.setTransformationMethod(new PasswordTransformationMethod());

        Button button = (Button) singupF.findViewById(R.id.signupBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupMem();
            }
        });

        return singupF;
    }

    private void signupMem() {
        String pas = password.getText().toString();
        String rPas = rPassword.getText().toString();
        if(pas.equals(rPas)){
            if (mListener == null){
                throw  new AssertionError();
            }
            String fn = fullname.getText().toString();
            String em = email.getText().toString();
            String pw = password.getText().toString();
            mListener.onFragmentSignupFinish(fn, em, pw);
        } else {
            Toast.makeText(getContext(), "Your password and confirmation password do not match", Toast.LENGTH_SHORT).show();
        }

    }

    public interface FragmentListener {
        void  onFragmentSignupFinish(String fn, String em, String pw);
    }

}
