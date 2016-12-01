package fragments.android.exemple.com.logregfragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotFragment extends Fragment {

    private FragmentListener mListener;
    private EditText email;


    public ForgotFragment() {
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
        // Inflate the layout for this fragment


        View forgotF = inflater.inflate(R.layout.fragment_forgot, container, false);
        SharedPreferences preferences = forgotF.getContext().getSharedPreferences("login",MODE_PRIVATE);

        email = (EditText) forgotF.findViewById(R.id.forgotEditEmail);
        email.setText(preferences.getString("username",""), TextView.BufferType.EDITABLE);

        Button button = (Button) forgotF.findViewById(R.id.forgotBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPas();
            }
        });

        return forgotF;
    }

    private void forgotPas() {
        String em = email.getText().toString();
        if(!em.equals("")){
            if (mListener == null){
                throw  new AssertionError();
            }
            mListener.onFragmentForgotFinish(em);
        } else {
            Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
        }
    }

    public interface FragmentListener {
        void  onFragmentForgotFinish(String em);
    }

}
