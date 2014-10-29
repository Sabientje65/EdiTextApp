package dcictwebs102859.nl.editext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Blob_View_Activity extends ActionBarActivity {
    static String blobText;
    static String returnText = "-3";
    TextView text;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blob__view_);

        save = (Button)findViewById(R.id.saveButton);
        text = (TextView)findViewById(R.id.blobText);

        save.setOnClickListener(saveListener);

        if(blobText.length() > 8) {
            System.out.println(blobText.length());
            blobText = blobText.substring(6, blobText.length() - 3);
            text.setText(blobText);
        }

    }

    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialog progress = new ProgressDialog(Blob_View_Activity.this);
            progress.setTitle("Saving...");
            progress.setMessage("Saving blob!");
            progress.setCancelable(false);
            progress.show();

            blobText = text.getText().toString();

            blobText = blobText.replaceAll("'", "");
            blobText = blobText.replaceAll("\\\\", "");

            new Thread(new Server_Connection_Class.SaveBlob()).start();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(Login_Activity.done){
                        Login_Activity.done = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(blobText);
                            }
                        });

                        progress.cancel();
                        this.cancel();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                returnText = returnText.replaceAll("\r", "");
                                returnText = returnText.replaceAll("\n", "");
                                returnText = returnText.replaceAll(" ", "");
                                int returnInt = Integer.parseInt(returnText);

                                AlertDialog.Builder build = new AlertDialog.Builder(Blob_View_Activity.this);
                                build.setNeutralButton("Ok", null);
                                build.setTitle("Save failed");

                                switch (returnInt){
                                    case 0:
                                        build.setMessage("The blob you tried to save has been deleted.");
                                        break;
                                    case -1:
                                        build.setMessage("A server side error occurred.");
                                        break;
                                    default:
                                        build.setMessage("Blob saved successfully!");
                                }

                                returnText = "-3";

                                build.create();
                                build.show();
                            }
                        });
                    }
                }
            },0, 100);
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Blob_View_Activity.this, Blob_Selection_Activity.class));
    }
}