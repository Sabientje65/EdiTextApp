package dcictwebs102859.nl.editext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Blob_Selection_Activity extends ActionBarActivity {
    static ArrayList idList = new ArrayList();
    static LinearLayout layout;
    static int selectedBlobId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blob_selection);

        idList.clear();

        layout = (LinearLayout)findViewById(R.id.blobButtonLayout);

        ((TextView) findViewById(R.id.welcome_user)).setText("Welcome " + Login_Activity.usernameText);


        ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = conMan.getActiveNetworkInfo();

        if(info != null) {
            final ProgressDialog BLOB_FETCH_PROGRESS = new ProgressDialog(this);
            final ProgressDialog BUTTON_CREATOR = new ProgressDialog(this);

            BLOB_FETCH_PROGRESS.setTitle("Getting blobs id's...");
            BLOB_FETCH_PROGRESS.setMessage("Retrieving id's from your blobs from server...");
            BLOB_FETCH_PROGRESS.setCancelable(false);

            BUTTON_CREATOR.setTitle("Creating buttons...");
            BUTTON_CREATOR.setMessage("Creating buttons with retrieved id's...");
            BUTTON_CREATOR.setCancelable(false);

            BLOB_FETCH_PROGRESS.show();

            new Thread(new Server_Connection_Class.GetBlobButtonClass()).start();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(Login_Activity.done){
                        Login_Activity.done = false;

                        BLOB_FETCH_PROGRESS.cancel();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BUTTON_CREATOR.show();
                            }
                        });

                        for(int i = 0; i < idList.size(); i++) {
                            if (i == 0) {
                                final Button newBlob = new Button(Blob_Selection_Activity.this);
                                newBlob.setText("Create new blob");
                                newBlob.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final ProgressDialog progress = new ProgressDialog(Blob_Selection_Activity.this);
                                        progress.setMessage("Creating new blob...");
                                        progress.setTitle("Creating blob...");
                                        progress.setCancelable(false);
                                        progress.show();

                                        new Thread(new Server_Connection_Class.CreateBlob()).start();
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                if(Login_Activity.done) {
                                                    progress.cancel();
                                                    Login_Activity.done = false;
                                                    this.cancel();

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            new AlertDialog.Builder(Blob_Selection_Activity.this)
                                                                    .setTitle("Succeeded")
                                                                    .setMessage("Blob creation succeeded!")
                                                                    .setCancelable(false)
                                                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            reload();
                                                                        }
                                                                    })
                                                                    .create()
                                                                    .show();
                                                        }
                                                    });
                                                }
                                            }
                                        },0, 100);
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout.addView(newBlob);
                                    }
                                });
                            }

                            if ((Integer)idList.get(0) != 0) {
                                final int x = (Integer)idList.get(i);
                                final Button buttocks = new Button(Blob_Selection_Activity.this);
                                buttocks.setText("Blob id: " + idList.get(i));
                                buttocks.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new AlertDialog.Builder(Blob_Selection_Activity.this)
                                                .setTitle("Pick something.")
                                                .setMessage("What is your desired action?")
                                                .setNegativeButton("View blob", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        selectedBlobId = x;

                                                        new Thread(new Server_Connection_Class.ViewBlob()).start();

                                                        final ProgressDialog progress = new ProgressDialog(Blob_Selection_Activity.this);
                                                        progress.setTitle("Loading blob...");
                                                        progress.setMessage("Retrieving blob data from server...");
                                                        progress.setCancelable(false);
                                                        progress.show();

                                                        new Timer().schedule(new TimerTask() {
                                                            @Override
                                                            public void run() {
                                                                if(Login_Activity.done){
                                                                    Login_Activity.done = false;
                                                                    progress.cancel();
                                                                    if(Blob_View_Activity.blobText.length() > 8 || Blob_View_Activity.blobText.contains("@text1"))
                                                                        startActivity(new Intent(Blob_Selection_Activity.this, Blob_View_Activity.class));
                                                                    else
                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                new AlertDialog.Builder(Blob_Selection_Activity.this)
                                                                                        .setTitle("Error retrieving blob")
                                                                                        .setMessage("An error occurred while loading the blob. It's either a server side error, or the requested blob has been deleted.")
                                                                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                reload();
                                                                                            }
                                                                                        })
                                                                                        .setCancelable(false)
                                                                                        .create()
                                                                                        .show();
                                                                            }
                                                                        });
                                                                    this.cancel();
                                                                }
                                                            }
                                                        },0, 100);
                                                    }
                                                })
                                                .setPositiveButton("Delete blob", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        new AlertDialog.Builder(Blob_Selection_Activity.this)
                                                                .setTitle("Confirmation")
                                                                .setMessage("Are you sure you want to delete this blob?")
                                                                .setPositiveButton("YES I AM!", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        selectedBlobId = x;

                                                                        final ProgressDialog progress = new ProgressDialog(Blob_Selection_Activity.this);
                                                                        progress.setTitle("Deleting blob...");
                                                                        progress.setMessage("Sending delete request to server...");
                                                                        progress.setCancelable(false);
                                                                        progress.show();

                                                                        new Thread(new Server_Connection_Class.DeleteBlob()).start();

                                                                        new Timer().schedule(new TimerTask() {
                                                                            @Override
                                                                            public void run() {
                                                                                if (Login_Activity.done) {
                                                                                    Login_Activity.done = false;
                                                                                    progress.cancel();

                                                                                    this.cancel();

                                                                                    runOnUiThread(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            new AlertDialog.Builder(Blob_Selection_Activity.this)
                                                                                                    .setTitle("Blob deleted")
                                                                                                    .setMessage("The blob has been successfully deleted, click the \"OK\" button to reload your blobs.")
                                                                                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                            reload();
                                                                                                        }
                                                                                                    })
                                                                                                    .setCancelable(false)
                                                                                                    .create()
                                                                                                    .show();
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        }, 0, 100);
                                                                    }
                                                                })
                                                                .setNeutralButton("NO I AM NOT!", null)
                                                                .create()
                                                                .show();
                                                    }
                                                })
                                                .setNeutralButton("Cancel", null)
                                                .create()
                                                .show();
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout.addView(buttocks);
                                    }
                                });
                            }
                        }

                        BUTTON_CREATOR.cancel();
                        this.cancel();
                    }
                }
            },0, 100);
        }
        else
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(Blob_Selection_Activity.this)
                            .setTitle("No internet connection")
                            .setMessage("Error, no internet connection detected. Please connect to the internet before attempting to load your blob ids.")
                            .setNeutralButton("Ok", null)
                            .setCancelable(false)
                            .create()
                            .show();
                }
            });
    }

    public void reload(){
        if(Build.VERSION.SDK_INT >= 11){
            recreate();
        }
        else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void onBackPressed(){
        startActivity(new Intent(Blob_Selection_Activity.this, Login_Activity.class));
    }
}