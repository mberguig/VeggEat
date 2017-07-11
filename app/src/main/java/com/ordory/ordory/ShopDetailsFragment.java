package com.ordory.ordory;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ProductAdapter;
import com.holder.ProductViewHolder;
import com.models.Product;
import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listProductsView;
    private Button addProductButton;
    private List<Product> products = new ArrayList<Product>();
    private TextView totalPrice;
    private Button editProductButton;
    private Button deleteProductButton;
    private int productCpt=0;
    private boolean is_completed;


    public ShopDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopDetailsFragment newInstance(String param1, String param2) {
        ShopDetailsFragment fragment = new ShopDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_details, container, false);

        listProductsView = (ListView) view.findViewById(R.id.product_list_view);
        addProductButton = (Button) view.findViewById(R.id.button_add_product);
        editProductButton = (Button) view.findViewById(R.id.button_edit_product);
        deleteProductButton = (Button) view.findViewById(R.id.button_delete_product);
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        TextView title = (TextView)view.findViewById(R.id.titleListProduct);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySharedPref",0);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        boolean statusConnect = sharedPreferences.getBoolean("is_connected",false);
        is_completed = sharedPreferences.getBoolean("list_status",false);
        String tokenUser = sharedPreferences.getString("userToken","");
        final int listshopId = sharedPreferences.getInt("listshopId",0);
        title.setText(sharedPreferences.getString("listshopName",""));
        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {

            @Override
            public void onSuccess(JSONObject obj) {
                String name;
                JSONObject tmpObj;
                int id, qty;
                double price;
                JSONArray listProducts;

                try {
                    listProducts = obj.getJSONArray("result");
                    System.out.println("Data Product : " + listProducts);
                    for (int i = 0; i < listProducts.length(); i++) {
                        productCpt += 1;
                        tmpObj = listProducts.getJSONObject(i);
                        id = Integer.parseInt(tmpObj.getString("id"));
                        price = Double.parseDouble(tmpObj.getString("price"));
                        qty = Integer.parseInt(tmpObj.getString("quantity"));
                        name = tmpObj.getString("name");
                        if(name.contains("*")){
                            name = name.replace("*"," ");
                        }
                        products.add(new Product(id, name, qty, price));
                    }
                    ProductAdapter productAdapter = new ProductAdapter(getActivity(), products);
                    listProductsView.setAdapter(productAdapter);
                    if (products.size() > 0){
                        totalPrice.setText("Prix total : " + countTotalPrice()+ " €");
                    }
                    edit.putInt("nbProduct",productCpt);
                    edit.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_completed){
                    Toast.makeText(getActivity(), "Cette liste est déjà complet !", Toast.LENGTH_LONG).show();
                }else{
                    Fragment fragment = new ProductFormFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.fragment_shoppingList, fragment).addToBackStack(null).commit();
                }
            }
        });

        String url = Constant.WS_LIST_PRODUCT_URL+"?token="+tokenUser+"&shopping_list_id="+listshopId;
        asyncTask.execute(url);

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

    private double countTotalPrice(){
        double total = 0;
        for(Product product : products){
            total = total + (product.getPrice()*product.getQuantity());
        }

        return total;
    }
}
