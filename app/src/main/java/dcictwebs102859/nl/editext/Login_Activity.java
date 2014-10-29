package dcictwebs102859.nl.editext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Login_Activity extends ActionBarActivity {
    static boolean done;
    Button login, register;
    static String usernameText, passwordText;
    TextView username, password;
    static int userId;
    public static Context CurrentContext;
    int timePassed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        CurrentContext = Login_Activity.this;

        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.registerButton);

        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);

        login.setOnClickListener(loginListener);
        register.setOnClickListener(registerListener);
    }

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = conMan.getActiveNetworkInfo();

            if (info != null){
                usernameText = validation(username.getText().toString());
                passwordText = validation(password.getText().toString());

                username.setText(usernameText);
                password.setText(passwordText);

                if (passwordText.length() > 0 && passwordText.length() <= 50) {
                    if (usernameText.length() > 0 && usernameText.length() <= 50) {
                        userId = -1;
                        timePassed = 0;

                        final ProgressDialog LOGIN_PROGRESS = new ProgressDialog(Login_Activity.this);
                        LOGIN_PROGRESS.setTitle("Logging in...");
                        LOGIN_PROGRESS.setMessage("Verifying login credentials...");
                        LOGIN_PROGRESS.setCancelable(false);
                        LOGIN_PROGRESS.show();

                        new Thread(new Server_Connection_Class.Login_Class()).start();

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {

                                System.out.println(userId);

                                timePassed++;

                                if (timePassed >= 75) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            LOGIN_PROGRESS.cancel();

                                            new AlertDialog.Builder(Login_Activity.this)
                                                    .setNeutralButton("OK", null)
                                                    .setCancelable(false)
                                                    .setMessage("Error, failed to connect to server.")
                                                    .create().show();
                                        }
                                    });

                                    this.cancel();
                                }

                                if (userId != -1) {
                                    if (userId == 0) {
                                        LOGIN_PROGRESS.cancel();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new AlertDialog.Builder(Login_Activity.this)
                                                        .setNeutralButton("OK", null)
                                                        .setCancelable(false)
                                                        .setMessage("Error, wrong username/password entered.")
                                                        .create().show();
                                            }
                                        });
                                    } else {
                                        LOGIN_PROGRESS.cancel();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new AlertDialog.Builder(Login_Activity.this)
                                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                startActivity(new Intent(Login_Activity.this, Blob_Selection_Activity.class));
                                                            }
                                                        })
                                                        .setCancelable(false)
                                                        .setMessage("Successfully logged in as: " + usernameText)
                                                        .create().show();
                                            }
                                        });
                                    }
                                    this.cancel();
                                }
                            }
                        }, 0, 100);
                    } else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (usernameText.length() > 50)
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, username is too long.")
                                            .create().show();
                                else
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, no username entered.")
                                            .create().show();
                            }
                        });
                } else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (usernameText.length() > 50)
                                new AlertDialog.Builder(Login_Activity.this)
                                        .setNeutralButton("OK", null)
                                        .setCancelable(false)
                                        .setMessage("Error, password is too long.")
                                        .create().show();
                            else
                                new AlertDialog.Builder(Login_Activity.this)
                                        .setNeutralButton("OK", null)
                                        .setCancelable(false)
                                        .setMessage("Error, no password entered.")
                                        .create().show();
                        }
                    });
            }
            else
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(Login_Activity.this)
                                .setTitle("No internet connection")
                                .setMessage("Error, no internet connection detected. Please connect to the internet before attempting to log in.")
                                .setNeutralButton("Ok", null)
                                .setCancelable(true)
                                .create()
                                .show();
                    }
                });
        }
    };

    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = conMan.getActiveNetworkInfo();

            if(info != null){
                usernameText = validation(username.getText().toString());
                passwordText = validation(password.getText().toString());

                username.setText(usernameText);
                password.setText(passwordText);

                if (passwordText.length() > 0 && passwordText.length() <= 50) {
                    if (usernameText.length() > 0 && usernameText.length() <= 50) {
                        userId = -1;

                        final ProgressDialog progress = new ProgressDialog(Login_Activity.this);
                        progress.setTitle("Creating account");
                        progress.setMessage("Creating account, contacting server...");
                        progress.setCancelable(false);
                        progress.show();

                        new Thread(new Server_Connection_Class.Register()).start();

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(done){
                                    done = false;
                                    progress.cancel();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            password.setText("");

                                            if (userId != -1)
                                                new AlertDialog.Builder(Login_Activity.this)
                                                        .setTitle("Account created")
                                                        .setMessage("Account created successfully!")
                                                        .setNeutralButton("OK", null)
                                                        .setCancelable(true)
                                                        .create()
                                                        .show();
                                            else
                                                new AlertDialog.Builder(Login_Activity.this)
                                                        .setTitle("Username already exists")
                                                        .setMessage("The desired username is already in use, faggot. Or the server connection failed, in which case you're still a faggot.")
                                                        .setNeutralButton("OK", null)
                                                        .setCancelable(false)
                                                        .create()
                                                        .show();
                                        }
                                    });
                                }
                            }
                        },0, 100);

                    }   else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (usernameText.length() > 50)
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, username is too long.")
                                            .create().show();
                                else
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, no username entered.")
                                            .create().show();
                            }
                        });
                    } else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (usernameText.length() > 50)
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, password is too long.")
                                            .create().show();
                                else
                                    new AlertDialog.Builder(Login_Activity.this)
                                            .setNeutralButton("OK", null)
                                            .setCancelable(false)
                                            .setMessage("Error, no password entered.")
                                            .create().show();
                            }
                        });
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(Login_Activity.this)
                                .setTitle("No internet connection")
                                .setMessage("Error, no internet connection detected. Please connect to the internet before attempting to log in.")
                                .setNeutralButton("Ok", null)
                                .setCancelable(true)
                                .create()
                                .show();
                    }
                });
            }
        }
    };

    private String validation(String string){
        String[] illegalStrings = {"'", "\\\\", "@usr", "@pass"};

        while(true) {
            string = string.replaceAll(illegalStrings[0], "");
            string = string.replaceAll(illegalStrings[1], "");
            string = string.replaceAll(illegalStrings[2], "");
            string = string.replaceAll(illegalStrings[3], "");

            boolean again = false;

            for(String i : illegalStrings)
                if(string.contains(i))
                    again = true;

            if(!again)
                break;
        }

        System.out.println(string);

        return string;
    }

    public void onBackPressed(){
        System.exit(0);
    }
}