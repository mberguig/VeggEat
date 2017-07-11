package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String email;
    public static String password;
    public static String firstname;
    public static String lastname;

    private EditText editEmail;
    private EditText editPwd;
    private EditText editfirstName;
    private EditText editlastName;
    public Button registerButton;
    private Fragment frg = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        editEmail = (EditText) view.findViewById(R.id.email);
        editPwd = (EditText) view.findViewById(R.id.password);
        editfirstName = (EditText) view.findViewById(R.id.firstName);
        editlastName = (EditText) view.findViewById(R.id.lastName);

        registerButton = (Button)view.findViewById(R.id.subscribe_button);

        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {

            @Override
            public void onSuccess(JSONObject obj) {
                Log.e("subscribe","successful...");
                frg = new ConnectFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.registerFragment, frg);
                transaction.commit();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                TextView txtView = (TextView)view.findViewById(R.id.errorConnect);
                txtView.setText("Erreur, veuillez réessayer votre inscription !");
                txtView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email = editEmail.getText().toString();
                        password = editPwd.getText().toString();
                        lastname = editlastName.getText().toString();
                        firstname = editfirstName.getText().toString();
                        if(firstname.contains(" ")){
                            firstname = firstname.replace(" ","*");
                        }
                        if(lastname.contains(" ")){
                            lastname = lastname.replace(" ","*");
                        }
                        String url = Constant.WS_SUBSCRIBE_URL+"?email="+email+"&password="+password+"&firstname="+firstname+"&lastname="+lastname;
                        asyncTask.execute(url);
                    }
                }
        );

       // return inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
