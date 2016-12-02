package code.aguilera.prueba;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class FrAddPayment extends Fragment {

    BBDD bd;
    EditText etAmount, etDescription;
    CheckBox cbState;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_add_payment, container, false);

        bd=new BBDD(this.getContext());

        etAmount=(EditText) view.findViewById(R.id.etAmount);
        etDescription=(EditText) view.findViewById(R.id.etDescription);
        cbState =(CheckBox) view.findViewById(R.id.cbState);


        return view;
    }

    public void addPayment() {

        //Creamos el registro a insertar como objeto ContentValues
        Payment pay = new Payment();
        pay.setAmount(Float.valueOf(etAmount.getText().toString()));
        pay.setDescription(etDescription.getText().toString());
        pay.setState(cbState.isChecked());

        //Insertamos el registro en la base de datos
        bd.addPayment(pay);

    }

}
