package com.ordory.ordory;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * {@link EditProductFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProductFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductFormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String productName;
    private String quantity;
    private String price;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sharedPreferences;
    private Button btnUpdateProduct;
    private EditText productName_edit;
    private EditText price_edit;
    private EditText quantity_edit;
    private TextView blocInfo;

    public EditProductFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProductFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProductFormFragment newInstance(String param1, String param2) {
        EditProductFormFragment fragment = new EditProductFormFragment();
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
        View view  = inflater.inflate(R.layout.fragment_edit_product_form, container, false);

        productName_edit = (EditText) view.findViewById(R.id.productName);
        price_edit = (EditText) view.findViewById(R.id.price);
        quantity_edit = (EditText) view.findViewById(R.id.quantity);
        blocInfo = (TextView) view.findViewById(R.id.editProductInfo);

        sharedPreferences = getActivity().getSharedPreferences("mySharedPref", 0);

        String productNameToEdit = sharedPreferences.getString("productNameToEdit", "");
        String productPriceToEdit= sharedPreferences.getString("productPriceToEdit", "");
        int productQuantityToEdit = sharedPreferences.getInt("productQuantityToEdit", 0);

        price_edit.setText(productPriceToEdit);
        productName_edit.setText(productNameToEdit);
        quantity_edit.setText(productQuantityToEdit+"");

        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject obj) {
                Fragment fragment = new ShopDetailsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_edit_product_form, fragment);
                transaction.commit();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                blocInfo.setText("Erreur lors de la mise à jour du produit");
            }
        });

        btnUpdateProduct = (Button) view.findViewById(R.id.btn_edit_product);
        btnUpdateProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                String userToken = sharedPreferences.getString("userToken", "");
                int shopId = sharedPreferences.getInt("listshopId", -1);
                int idProductToEdit = sharedPreferences.getInt("idProductToEdit", -1);

                productName = productName_edit.getText().toString();
                price = price_edit.getText().toString();
                quantity = quantity_edit.getText().toString();
                if(productName.contains(" ")){
                    productName = productName.replace(" ","*");
                }
                String url = Constant.WS_EDIT_PRODUCT_URL+"?token="+userToken+"&id="+idProductToEdit+"&name="+productName+"&quantity="+quantity+"&price="+price;
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
