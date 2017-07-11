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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.adapter.ShoppingListAdapter;
import com.models.ShoppingList;
import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListShoppingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListShoppingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Object MODE_PRIVATE = "MODE_PRIVATE";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView listShoppingListView;
    private Button buttonAddShoppingList;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private List<ShoppingList> shoppingLists = new ArrayList<ShoppingList>();
    private String tokenUser;
    private ShoppingList shopingList;
    public SharedPreferences sharedList;
    private int listCpt=0;
    SharedPreferences.Editor editor;

    public ListShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListShoppingListFragment newInstance(String param1, String param2) {
        ListShoppingListFragment fragment = new ListShoppingListFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_shopping_list, container, false);
        sharedList = getActivity().getSharedPreferences("mySharedPref",0);
        editor = sharedList.edit();

        listShoppingListView = (ListView) view.findViewById(R.id.product_list_view);
        buttonAddShoppingList = (Button) view.findViewById(R.id.button_add_shopping_list);
        tokenUser = sharedList.getString("userToken","");

        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject json) {
                ShoppingListAdapter shoppingListAdapter;
                String name;
                JSONArray listArray;
                JSONObject tmpObj;
                int id;
                Date date;
                String isActive;

                try {
                    listArray = json.getJSONArray("result");
                    for (int i = 0; i < listArray.length(); i++) {
                        listCpt +=1;
                        tmpObj = listArray.getJSONObject(i);
                        id = Integer.parseInt(tmpObj.getString("id"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        date = simpleDateFormat.parse(tmpObj.getString("created_date"));
                        name = tmpObj.getString("name");
                        if(name.contains("*")){
                            name = name.replace("*"," ");
                        }
                        isActive = tmpObj.getString("completed");
                        if(isActive.equals("0")){
                            shopingList = new ShoppingList(id,name,date, false);
                        } else {
                            shopingList = new ShoppingList(id,name,date,true);
                        }
                        shoppingLists.add(shopingList);
                        Log.e("name", tmpObj.getString("name"));
                    }
                    shoppingListAdapter = new ShoppingListAdapter(getActivity(), shoppingLists);
                    listShoppingListView.setAdapter(shoppingListAdapter);
                    editor.putInt("nbListShop",listCpt);
                    editor.commit();
                    listShoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
                            ShoppingList itemselected = (ShoppingList) listShoppingListView.getItemAtPosition(position);
                            editor.putString("listshopName",itemselected.getName());
                            editor.putInt("listshopId",itemselected.getId());
                            editor.putBoolean("list_status",itemselected.isCompleted());
                            editor.commit();
                            Fragment fragment = new ShopDetailsFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.fragment_products, fragment).addToBackStack(null).commit();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                buttonAddShoppingList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new ListFormularFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.fragment_products, fragment).addToBackStack(null).commit();
                    }
                });
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });


        String url = Constant.WS_LIST_SHOPPINGLIST_URL+"?token="+tokenUser;
        asyncTask.execute(url);

        return view;
      // return inflater.inflate(R.layout.fragment_list_shopping_list, container, false);
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
