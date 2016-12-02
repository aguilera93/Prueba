package code.aguilera.prueba;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class copyMainActivity extends ActionBarActivity {

    ListView lvPayments;
    TextView tvTotal, tvDate;
    EditText etAmount, etDescription;
    CheckBox cbState;
    Button btnAdd;
    BBDD bd;
    ArrayList<Payment> paymentsList;
    PaymentListAdapter PLA;

    //temporal
    Float temp;
    DecimalFormat precision;
    Cursor c;

    private class HiloTotal extends AsyncTask<String, String, Long> {
        @Override
        protected Long doInBackground(String... arg0) {

            c = bd.getMonthPayments();

            paymentsList=new ArrayList<Payment>();

            temp=Float.valueOf(0);

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

                //temporal
                if(pay.getState()) {
                    temp += Float.valueOf(pay.getAmount());
                }
                else {
                    temp -= Float.valueOf(pay.getAmount());
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

            tvTotal.setText(String.valueOf(precision.format(temp)));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd=new BBDD(getBaseContext());

        precision = new DecimalFormat("00.00");

        lvPayments =(ListView) findViewById(R.id.lvPayments);
        tvTotal=(TextView) findViewById(R.id.tvTotal);
        tvDate=(TextView) findViewById(R.id.tvDate);
        etAmount=(EditText) findViewById(R.id.etAmount);
        etDescription=(EditText) findViewById(R.id.etDescription);
        cbState =(CheckBox) findViewById(R.id.cbState);

        getTotal();

        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(etAmount.getText().length()!=0)
            {
                if(etDescription.getText().length()!=0)
                {
                    addPayment();
                }
            }

            }
        });

        lvPayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editPayment(paymentsList.get(position), position);

            }
        });

        lvPayments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deletePayment(paymentsList.get(position));

                return false;
            }
        });
    }

    public void addPayment() {

        //Creamos el registro a insertar como objeto ContentValues
        Payment pay = new Payment();
        pay.setAmount(Float.valueOf(etAmount.getText().toString()));
        pay.setDescription(etDescription.getText().toString());
        pay.setState(cbState.isChecked());

        //Insertamos el registro en la base de datos
        bd.addPayment(pay);

        getTotal();

    }

    public void editPayment(final Payment pay, final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText ieAmount = new EditText(this);
        ieAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ieAmount.setText(precision.format(pay.getAmount()));

        final EditText ieDescription = new EditText(this);
        ieDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        ieDescription.setText(pay.getDescription()+"");

        final CheckBox icState = new CheckBox(this);
        icState.setChecked(pay.getState());
        icState.setText("Estado");

        LinearLayout ll=new LinearLayout(getBaseContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ieAmount);
        ll.addView(ieDescription);
        ll.addView(icState);

        builder.setView(ll);

        builder.setTitle("Editar Cobro");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //quitar € y formatear decimales
                String qe=ieAmount.getText()+"";
                //qe=qe.substring(0,qe.indexOf("€"));
                qe=qe.replace(",", ".");

                pay.setAmount(Float.valueOf(qe));

                pay.setDescription(ieDescription.getText()+"");

                pay.setState(icState.isChecked());

                bd.updatePayment(pay);

                paymentsList.remove(pos);
                paymentsList.add(pos, pay);

                getTotal();

                Toast.makeText(getBaseContext(), "Cobro modificado correctamente.", Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();

    }

    public void deletePayment(final Payment pay) {

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cobro")
                .setMessage("Eliminar: ' "+pay.getDescription()+" - "+precision.format(pay.getAmount())+"€ '")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        bd.deletePayment(pay.getID());

                        getTotal();

                        Toast.makeText(getBaseContext(), "Cobro eliminado correctamente.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    public void getTotal() {

        etDescription.setText("");
        etAmount.setText("");

        new HiloTotal().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("Old Payments");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(copyMainActivity.this, OldPayments.class));
                return true;
        }

        return false;
    }


}
