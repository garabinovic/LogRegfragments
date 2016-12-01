package fragments.android.exemple.com.logregfragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ChangepassFragment extends Fragment {
    private FragmentListener mListener;
    EditText code, password, rPassword;


    public ChangepassFragment() {
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
        View changpassF = inflater.inflate(R.layout.fragment_changepass, container, false);
        code = (EditText) changpassF.findViewById(R.id.chpassEditCode);
        password = (EditText) changpassF.findViewById(R.id.chpassEditPasword);
        rPassword = (EditText) changpassF.findViewById(R.id.chpassEditRPassword);

        Button button = (Button) changpassF.findViewById(R.id.chpassBTN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });
        
        return changpassF;
    }

    private void changePass() {
        String cd = code.getText().toString();
        String ps = password.getText().toString();
        String rps = rPassword.getText().toString();

        if(!cd.equals("")){
            if(!ps.equals("")){
                if (ps.equals(rps)){
                    if (mListener == null){
                        throw  new AssertionError();
                    }
                    mListener.onFragmentChangepassFinish(cd,ps,rps);
                } else {
                    Toast.makeText(getContext(), "Your password and confirmation password do not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Enter your password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Enter receiver code", Toast.LENGTH_SHORT).show();
        }
    }

    public interface FragmentListener {
        void  onFragmentChangepassFinish(String cd, String ps, String rps);
    }

}
