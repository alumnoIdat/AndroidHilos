package com.example.chango.androidhilos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSinHilos;
    private Button btnHilo;
    private Button btnAsyncTask;
    private Button btnCancelar;
    private Button btnAsyncDialog;
    private ProgressBar pbarProgreso;
    private ProgressDialog pDialog;
    private MiTareaAsincrona tarea1;
//    private MiTareaAsincronaDialog tarea2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSinHilos = (Button) findViewById(R.id.btnSinHilos);
        btnHilo = (Button) findViewById(R.id.btnHilo);
        btnAsyncTask = (Button) findViewById(R.id.btnAsyncTask);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnAsyncDialog = (Button) findViewById(R.id.btnAsyncDialog);
        pbarProgreso = (ProgressBar) findViewById(R.id.pbarProgreso);

        btnSinHilos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarProgreso.setMax(100); //valor máximo de ProgressBar
                pbarProgreso.setProgress(0); // Empieza vacía ProgressBar
                for (int i = 1; i <= 10; i++) {
                    tareaLarga();
                    pbarProgreso.incrementProgressBy(10); //incremento
                }
                Toast.makeText(getApplicationContext(),
                        "Tarea finalizada!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void conHilos(View view) {
        new Thread(new Runnable() {
            public void run() {
                pbarProgreso.post(new Runnable() {
                    public void run() {
                        pbarProgreso.setProgress(0);
                    }
                });
                for (int i = 1; i <= 10; i++) {
                    tareaLarga();
                    pbarProgreso.post(new Runnable() {
                        public void run() {
                            pbarProgreso.incrementProgressBy(10);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Tarea finalizada!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public void conAsyncTask(View view) {
        this.tarea1 = new MiTareaAsincrona();
    }


    public void cancelar(View view) {
        this.tarea1.onCancelled();
    }


    private void tareaLarga() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i = 1; i <= 10; i++) {
                tareaLarga();
                publishProgress(i * 10);
                if (isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pbarProgreso.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pbarProgreso.setMax(100);
            pbarProgreso.setProgress(0);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getApplicationContext(),
                        "Tarea finalizada!",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(),
                    "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}