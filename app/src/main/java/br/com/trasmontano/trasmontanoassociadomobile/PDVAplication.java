package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;
import se.emilsjolander.sprinkles.annotations.Column;

/**
 * Created by rbarbosa on 08/07/2016.
 */
public class PDVAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());

        sprinkles.addMigration(new Migration() {
            @Override
            protected void onPreMigrate() {
                // do nothing
            }

            @Override
            protected void doMigration(SQLiteDatabase db) {
                db.execSQL(
                        "CREATE TABLE associado (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "usuario TEXT, " +
                                "dataNascimento TEXT, " +
                                "cpf TEXT, " +
                                "nome TEXT, " +
                                "caminhoImagem TEXT " +
                         ")"
                );

                db.execSQL(

                "CREATE TABLE alarme (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "segunda INTEGER, " +
                                "terca INTEGER, " +
                                "quarta INTEGER, " +
                                "quinta INTEGER, " +
                                "sexta INTEGER, " +
                                "sabado INTEGER, " +
                                "domingo INTEGER, " +
                                "todos INTEGER, " +
                                "ativo INTEGER, " +
                                 "nomeAlarme TEXT, " +
                                "hora TEXT " +
                                ")"
                );

            }

            @Override
            protected void onPostMigrate() {
                // do nothing
            }
        });
    }

}
