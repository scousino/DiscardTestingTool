package edu.pitt.cs1699.discardtestingtool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Messenger mService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void messageTrigger(View v) {
        Intent intent = new Intent();
        intent.setAction("edu.pitt.cs1699.discard.NEW_MESSAGE");
        String triggerData = "";
        try {
            triggerData = new JSONObject()
                    .put("Time", new JSONObject()
                            .put("Posted Date", "2018-04-02")
                            .put("Posted Time", "15:45:00"))
                    .put("Details", new JSONObject()
                            .put("ChatID", "ABC")
                            .put("Message", "Remember to drive safe as you're leaving the game!")
                    ).toString();

        } catch (JSONException j) {
            j.printStackTrace();
        }
        intent.putExtra("data", triggerData);
        sendBroadcast(intent);
    }

    public void locationTrigger(View v) throws RemoteException {
        Intent intent = new Intent("edu.pitt.cs1699.discard.LOCATION");
        intent.setPackage("edu.pitt.cs1699.discard");
        this.bindService(intent, locationConnection, Context.BIND_AUTO_CREATE);

    }

    public void eventAddTrigger(View v) {
        Intent intent = new Intent();
        intent.setAction("edu.pitt.cs1699.discard.EVENT");
        String triggerData = "";
        try {
            triggerData = new JSONObject()
                    .put("Location", new JSONObject()
                            .put("Lat", "0")
                            .put("Long", "0"))
                    .put("Time", new JSONObject()
                            .put("Start Date", "2018-08-14")
                            .put("Start Time", "12:30:00")
                            .put("End Date", "2018-08-14")
                            .put("End Time", "17:35:00"))
                    .put("Details", new JSONObject()
                            .put("ChatID", "NEWTESTID")
                            .put("Name", "Steelers Football Game")
                            .put("Description", "Chat for the Steelers game against the Cleveland Browns"))
                    .toString();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        intent.putExtra("data", triggerData);
        startActivity(intent);
    }

    public void timeTrigger(View v) throws RemoteException {
        Intent intent = new Intent("edu.pitt.cs1699.discard.TIME");
        intent.setPackage("edu.pitt.cs1699.discard");
        this.bindService(intent, timeConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection locationConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Message message = new Message();
            message.what = 1;

            String triggerData = "";

            try {
                triggerData = new JSONObject()
                        .put("Location", new JSONObject()
                                .put("Lat", "40.4406")
                                .put("Long", "-79.9958"))
                        .toString();

            } catch (JSONException j) {
                j.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString("location",triggerData);

            message.obj = bundle;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mService = null;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };


    private ServiceConnection timeConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Message message = new Message();
            message.what = 2;

            String triggerData = "";

            try {
                triggerData = new JSONObject()
                        .put("Time", new JSONObject()
                                .put("Current Date", "2018-04-13")
                                .put("Current Time", "10:00:00"))
                        .toString();

            } catch (JSONException j) {
                j.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString("time",triggerData);

            message.obj = bundle;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            mService = null;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };
}
