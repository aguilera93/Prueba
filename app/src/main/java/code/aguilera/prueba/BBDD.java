package code.aguilera.prueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by s.aguilera on 14/11/2016.
 */

public class BBDD {

    private static final String BASEDATOS = "BD";
    private static final int VERSION = 2;

    private static final String SQLCREAR1 = "CREATE TABLE Payment (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " +
                                            "amount FLOAT, date TEXT, description TEXT, state BOOL)";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase bd;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, BASEDATOS, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(SQLCREAR1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Payment");
            onCreate(db);
        }
    }

    public BBDD(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        importar();
    }

    // ---abrir la base de datos---
    public void abrirW() throws SQLException {
        bd = DBHelper.getWritableDatabase();
    }

    public void abrirR() throws SQLException {
        bd = DBHelper.getReadableDatabase();
    }

    // ---cerrar la base de datos---
    public void cerrar() {
        DBHelper.close();
    }

    // ---recuperar todos los pagos
    public Cursor getPayments() throws SQLException {
        abrirR();
        //Cursor mCursor = bd.query(true, "Payment", new String[] { "ID", "amount", "date", "description", "state"}, null, null, null, null, null, null);
        Cursor mCursor = bd.rawQuery("SELECT * FROM Payment",null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        cerrar();
        return mCursor;
    }
    // ---recuperar todos pagos del mes en curso
    public Cursor getMonthPayments() throws SQLException {
        abrirR();
        //Cursor mCursor = bd.query(true, "Payment", new String[] { "ID", "amount", "date", "description", "state"}, null, null, null, null, null, null);
        Cursor mCursor = bd.rawQuery("SELECT * FROM Payment WHERE strftime('%m',date) = strftime('%m',datetime())",null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        cerrar();
        return mCursor;
    }
    // ---añadir un nuevo pago
    public Cursor addPayment(Payment payment) throws SQLException {
        abrirR();
        Cursor mCursor = bd.rawQuery("INSERT into Payment (amount, date, description, state) values ("+payment.getAmount()+", datetime(), '"+payment.getDescription()+"', '"+payment.getState()+"');", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        cerrar();
        return mCursor;
    }

    // ---eliminar un pago
    public Cursor deletePayment(int ID) throws SQLException {
        abrirR();
        Cursor mCursor = bd.rawQuery("DELETE FROM Payment WHERE ID='"+ID+"';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        cerrar();
        return mCursor;
    }

    // ---eliminar un pago
    public Cursor updatePayment(Payment pay) throws SQLException {
        abrirR();
        Cursor mCursor = bd.rawQuery("UPDATE Payment SET amount='"+pay.getAmount()+"', description='"+pay.getDescription()+"', state='"+pay.getState()+"' WHERE ID='"+pay.getID()+"';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        cerrar();
        return mCursor;
    }

    @SuppressLint("SdCardPath")
    public void importar() {
        Cursor c = getPayments();
        cerrar();
        if (c.moveToFirst())
            return;
        try {
            abrirW();
            cerrar();
            String destPath = "/data/data/" + context.getPackageName()
                    + "/databases/BD";
//			File f = new File(destPath);
//			f.delete();
            // Realizamos la copia de los bytes de la externa a la interna.
            CopyDB(context.getAssets().open("Payments.sqlite"), new FileOutputStream(
                    destPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void CopyDB(InputStream origen, OutputStream destino)
            throws IOException {
        // copiar en bloques de 1K bytes
        byte[] buffer = new byte[1024];
        int length;
        while ((length = origen.read(buffer)) > 0) {
            destino.write(buffer, 0, length);
        }
        origen.close();
        destino.close();
    }

}
