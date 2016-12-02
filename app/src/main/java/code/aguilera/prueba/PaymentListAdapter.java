package code.aguilera.prueba;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by s.aguilera on 14/11/2016.
 */

public class PaymentListAdapter extends ArrayAdapter<Payment>{

    private ArrayList<Payment> payments;
    private Context context;
    private LayoutInflater inflater;
    private int resource, amonth=0, bmonth=0;

    public PaymentListAdapter(Context context, int resource, ArrayList<Payment> payments) {
        super(context, resource, payments);
        this.context = context;
        this.payments =payments;
        this.resource=resource;
        inflater=LayoutInflater.from(context);

    }

    public View getView(final int position, View layoutFila, ViewGroup parent) {

        layoutFila=(RelativeLayout) inflater.inflate(resource, null);

        bmonth=Integer.parseInt(String.valueOf(payments.get(position).getDate()).substring(5,7));

        if(amonth!=bmonth) {
            amonth = bmonth;

            layoutFila.findViewById(R.id.tvMonth).setVisibility(View.VISIBLE);

            TextView textMonth = (TextView) layoutFila.findViewById(R.id.tvMonth);

            switch (amonth) {
                case 1:
                    textMonth.setText("Enero");
                    break;
                case 2:
                    textMonth.setText("Febrero");
                    break;
                case 3:
                    textMonth.setText("Marzo");
                    break;
                case 4:
                    textMonth.setText("Abril");
                    break;
                case 5:
                    textMonth.setText("Mayo");
                    break;
                case 6:
                    textMonth.setText("Junio");
                    break;
                case 7:
                    textMonth.setText("Julio");
                    break;
                case 8:
                    textMonth.setText("Agosto");
                    break;
                case 9:
                    textMonth.setText("Septiembre");
                    break;
                case 10:
                    textMonth.setText("Octubre");
                    break;
                case 11:
                    textMonth.setText("Noviembre");
                    break;
                case 12:
                    textMonth.setText("Diciembre");
                    break;
            }
        }


        TextView textDate=(TextView)layoutFila.findViewById(R.id.tvDate);
        textDate.setText(payments.get(position).getDate()+"");

        TextView textAmount=(TextView)layoutFila.findViewById(R.id.tvAmount);
        DecimalFormat precision = new DecimalFormat("00.00");

        if(payments.get(position).getState()) {
            textAmount.setText("+"+String.valueOf(precision.format(payments.get(position).getAmount())) + "€");
            textAmount.setTextColor(Color.GREEN);
        }
        else {
            textAmount.setText("-" + String.valueOf(precision.format(payments.get(position).getAmount())) + "€");
            textAmount.setTextColor(Color.RED);
        }
        textAmount.setTag(payments.get(position).getID()+"");

        TextView textDescription=(TextView)layoutFila.findViewById(R.id.tvDescription);
        textDescription.setText(payments.get(position).getDescription());

        return(layoutFila);
    }

}
