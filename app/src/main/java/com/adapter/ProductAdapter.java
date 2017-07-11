package com.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.holder.ProductViewHolder;
import com.models.Product;
import com.ordory.ordory.EditProductFormFragment;
import com.ordory.ordory.IConnectListner;
import com.ordory.ordory.ListShoppingListFragment;
import com.ordory.ordory.MainActivity;
import com.ordory.ordory.R;
import com.ordory.ordory.ShopDetailsFragment;
import com.utils.Constant;
import com.utils.MyAsynctask;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Patri on 17/11/2016.
 */
public class ProductAdapter extends ArrayAdapter<Product>{

    private SharedPreferences sharedPreferences;

    public ProductAdapter(Context context, List<Product> products){
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_row_view, parent, false);
        }

        ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new ProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.price = (TextView) convertView.findViewById(R.id.productPrice);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.productQuantity);
            viewHolder.editButton = (Button) convertView.findViewById(R.id.button_edit_product);
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.button_delete_product);
            //viewHolder.quantity = (TextView) convertView.findViewById(R.id.productQuantity);

            convertView.setTag(viewHolder);
        }

        final Product product = getItem(position);

        ImageView imgView = (ImageView)convertView.findViewById(R.id.product_image);
        imgView.setBackgroundResource(R.drawable.ic_photo);
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText("Prix unitaire : "+product.getPrice()+" €");
        viewHolder.quantity.setText("Quantité : "+ product.getQuantity());

        final MyAsynctask asyncTask = new MyAsynctask();

        asyncTask.setListner(new IConnectListner() {
            @Override
            public void onSuccess(JSONObject obj) {
                Fragment fragment = new ShopDetailsFragment();
                FragmentTransaction fragmentTransaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_shoppingList, fragment).addToBackStack(null).commit();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });


        viewHolder.editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                sharedPreferences = ((Activity) getContext()).getSharedPreferences("mySharedPref", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("idProductToEdit", product.getId());
                editor.putString("productNameToEdit", product.getName());
                editor.putInt("productQuantityToEdit", product.getQuantity());
                editor.putString("productPriceToEdit", product.getPrice()+"");
                editor.commit();

                Fragment fragment = new EditProductFormFragment();
                FragmentTransaction fragmentTransaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_shoppingList, fragment).addToBackStack(null).commit();
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String url = Constant.WS_REMOVE_PRODUCT_URL + "?token=" + Constant.tokenUser + "&id=" + product.getId();
                sharedPreferences = ((Activity) getContext()).getSharedPreferences("mySharedPref", 0);
                String userToken = sharedPreferences.getString("userToken", "");
                int shopId = sharedPreferences.getInt("listshopId", -1);
                if (userToken != null && userToken != "") {
                    String url = Constant.WS_REMOVE_PRODUCT_URL+"?token="+userToken+"&id="+product.getId();
                    asyncTask.execute(url);
                }

            }
        });

        return convertView;
    }
}
