package com.ordory.ordory;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditShoppingListFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditShoppingListFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditShoppingListFormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button updateButton;

    private String shoppingListName;
    private EditText shoppingListNameEdit;

    private TextView shoppingListFormErr;

    private SharedPreferences sharedPreferences;

    private OnFragmentInteractionListener mListener;

    private Switch isActive;

    public EditShoppingListFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditShoppingListFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditShoppingListFormFragment newInstance(String param1, String param2) {
        EditShoppingListFormFragment fragment = new EditShoppingListFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_shopping_list_form, container, false);

        shoppingListNameEdit = (EditText) view.findViewById(R.id.listName);
        updateButton = (Button) view.findViewById(R.id.btn_create_shoplist);
        shoppingListFormErr = (TextView) view.findViewById(R.id.shoppingListFormErr);
        isActive = (Switch) view.findViewById(R.id.isActive);

        sharedPreferences = getActivity().getSharedPreferences("mySharedPref",0);

        String shoppingListNameToEdit = sharedPreferences.getString("shoppingListNameToEdit", "");
        boolean shoppingListStatusToEdit = sharedPreferences.getBoolean("shoppingListStatusToEdit", true);

        isActive.setChecked(shoppingListStatusToEdit);


        shoppingListNameEdit.setText(shoppingListNameToEdit);

        final MyAsynctask asynctask = new MyAsynctask();
        asynctask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject obj) {
                Fragment fragment = new ListShoppingListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_edit_shopping_list_form, fragment);
                transaction.commit();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                shoppingListFormErr.setText("Erreur lors de la mise à jour");
            }
        });

        updateButton = (Button) view.findViewById(R.id.btn_update_shoplist);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tokenUser = sharedPreferences.getString("userToken", "");
                final int idShoppingList = sharedPreferences.getInt("shoppingListIdToEdit", -1);
                String updatedName = shoppingListNameEdit.getText().toString();
                if(updatedName.contains(" ")){
                    updatedName=updatedName.replace(" ","*");
                }

                String isCompleted;
                if(isActive.isChecked()){
                    isCompleted = "1";
                } else {
                    isCompleted = "0";
                }

                String url= Constant.WS_EDIT_SHOPPINGLIST_URL+"?token="+tokenUser+"&id="+idShoppingList+"&name="+updatedName+"&completed="+isCompleted;
                asynctask.execute(url);
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
