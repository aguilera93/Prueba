package code.aguilera.prueba;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OldPayments extends ActionBarActivity {

    ListView lvPayments;
    BBDD bd;
    ArrayList<Payment> paymentsList;
    PaymentListAdapter PLA;

    DecimalFormat precision;
    Cursor c;

    private class HiloTotal extends AsyncTask<String, String, Long> {
        @Override
        protected Long doInBackground(String... arg0) {

            c = bd.getPayments();

            paymentsList=new ArrayList<Payment>();

            for (int s=0;s<c.getCount();s++){
                Payment pay=new Payment();

                pay.setID(c.getInt(0));
                pay.setAmount(c.getFloat(1));
                pay.setDate(c.getString(2));
                pay.setDescription(c.getString(3));

                if(c.getString(4).equals("true"))
                {
                    pay.setState(true);
                }
                else
                {
                    pay.setState(false);
                }

                paymentsList.add(0, pay);

                c.moveToNext();
            }
            c.close();



            return null;
        }
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

            PLA = new PaymentListAdapter(getBaseContext(), R.layout.payment, paymentsList);
            lvPayments.setAdapter(PLA);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_payments);

        bd=new BBDD(getBaseContext());

        precision = new DecimalFormat("00.00");

        lvPayments =(ListView) findViewById(R.id.lvPayments);

        getTotal();

    }

    public void getTotal() {

        new HiloTotal().execute();

    }

}
