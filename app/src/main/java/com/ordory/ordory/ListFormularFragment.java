package com.ordory.ordory;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFormularFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFormularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFormularFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText shoppingListNameEdit;
    private String shoppingListName;
    private String tokenUser;
    private Button confirmButton;

    private TextView shoppingListFormErr;

    public ListFormularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFormularFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFormularFragment newInstance(String param1, String param2) {
        ListFormularFragment fragment = new ListFormularFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_formular, container, false);

        shoppingListNameEdit = (EditText) view.findViewById(R.id.listName);

        confirmButton = (Button) view.findViewById(R.id.btn_create_shoplist);

        shoppingListFormErr = (TextView) view.findViewById(R.id.shoppingListFormErr);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySharedPref",0);
        tokenUser = sharedPreferences.getString("userToken","");
        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {

            @Override
            public void onSuccess(JSONObject obj) {
                //Add registration of user in the application
                Fragment frg = new ListShoppingListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_form_shopping_list, frg);
                transaction.commit();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                shoppingListFormErr.setText("Erreur lors de la creation de la liste");
            }

        });

        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                shoppingListName = shoppingListNameEdit.getText().toString();
                if(shoppingListName.contains(" ")){
                    shoppingListName=shoppingListName.replace(" ","*");
                }
                String url = Constant.WS_CREATE_SHOPPINGLIST_URL+"?token="+tokenUser+"&name="+shoppingListName;
                asyncTask.execute(url);
            }
        });

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
