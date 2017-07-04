package com.prafly.mqttclient;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.prafly.mqttclient.mongo.MongoDBHelper;
import com.prafly.mqttclient.mongo.MongoDBUtil;
import com.prafly.mqttclient.mqtt.MqttUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MqttCallback {

    private static final String TAG="MainActivity";

    private String host = "tcp://192.168.0.158:1883";
    private String userName = "admin";
    private String passWord = "admin";

    private Handler handler;


    private String myTopic = "test/topic";

    private String[] topics=new String[]{"test/top1","liang/top","yun/user"};

    private int[] qos=new int[]{0,1,2,3,4};

    private MqttConnectOptions options;

    private ScheduledExecutorService scheduler;

    EditText et_MqttAddr;

    ListView listViewMsg;

    private MqttTopic mqttTopic;
    private MqttMessage mqttMessage;

    private MqttUtil mqttUtil;

    private List<String> mlist;
    private ArrayAdapter<String> madapter;
    MongoDBHelper mongoDBHelper= null;
    private boolean isOpen=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_MqttAddr= (EditText) findViewById(R.id.et_mqttAddr);
        listViewMsg= (ListView) findViewById(R.id.listview_msg);
        mlist = new ArrayList<String>();
        madapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mlist);
        listViewMsg.setAdapter(madapter);

        mqttUtil=new MqttUtil();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Toast.makeText(MainActivity.this, (String) msg.obj,
                                Toast.LENGTH_SHORT).show();
                        System.out.println("-----------------------------");
                        mlist.add("------收到消息"+msg.obj);
                        break;

                    case 2:
                        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                        mlist.add("连接成功");
                        try {
//                            client.subscribe(myTopic, 1);
//                            client.subscribe(topics,qos);
//                            client.subscribe(topics);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                        Toast.makeText(MainActivity.this, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                        mlist.add("连接失败，系统正在重连");
                        break;

                    case 4:
                        List<String> tables= (List<String>) msg.obj;
                        mlist.add("connetMongo: 长度为:"+tables.size());
                         while (tables.iterator().hasNext()){
                            Log.d(TAG, "connetMongo: "+tables.iterator().next());
                             mlist.add("connetMongo: "+tables.iterator().next());
                        }
//                        try {
//                            mlist.add("connectionLost----------");
////                            client.connect(options);
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
                        break;
                }
                madapter.notifyDataSetChanged();
            }
        };



    }

    public void scribeTopic(View view){
//        try {
//            client.subscribe(topics,qos);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
    }

    public void connetMqttServer(View view){
        String addr=et_MqttAddr.getText().toString();
        mqttUtil.init(addr);
        mqttUtil.startReconnect();
    }

    public void sendTopic(View view){
//
//        try {
//            if (client == null || !client.isConnected()) {
////                connect();
//            }
//
//            int clientId=1221;
//            client.publish("GMCC/client/" + clientId, "客户端发送的消息".getBytes(),
//                    0, false);
//        } catch (MqttException e) {
////            ;.error(e.getCause());
//            e.printStackTrace();
//        }

        MqttDeliveryToken token = null;
        try {
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        System.out.println(token.isComplete()+"========");
    }

    public void connetMongo(View view){
//        try {
//            MongoDBUtil mongoDBUtil=new MongoDBUtil(et_MqttAddr.getText().toString(),"test");
//            Set<String> tables=mongoDBUtil.queryDBTables();
//            for (int i = 0; i < tables.size(); i++) {
//                Log.d(TAG, "connetMongo: "+i+" "+tables.toArray()[i]);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        new  Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    mongoDBHelper = new MongoDBHelper();
                } catch (UnknownHostException e) {
                    Log.d(TAG, "run: "+e.getMessage());
                    e.printStackTrace();

                }

            }
        }).start();


    }

    public void getDBList(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> tables=mongoDBHelper.getAllTables();
                Message msg = new Message();
                msg.what = 4;
                msg.obj = tables;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void startReconnect() {
//        scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//                if(!client.isConnected()) {
////                    connect();
//                }
//            }
//        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOpen=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(client != null && keyCode == KeyEvent.KEYCODE_BACK) {
//            try {
//                client.disconnect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            scheduler.shutdown();
//            client.disconnect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public void connectionLost(Throwable cause) {
        //连接丢失后，一般在这里面进行重连
        System.out.println("connectionLost----------");

        handler.sendEmptyMessage(4);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        System.out.println("deliveryComplete---------"
                + token.isComplete());
        Message msg = new Message();
        msg.what = 1;
        msg.obj = "deliveryComplete---------"
                + token.isComplete();
        handler.sendMessage(msg);
    }

    @Override
    public void messageArrived(String topicName, MqttMessage message)
            throws Exception {
        //subscribe后得到的消息会执行到这里面
        System.out.println("messageArrived----------");
        Message msg = new Message();
        msg.what = 1;
        msg.obj = topicName+"---"+message.toString();
        handler.sendMessage(msg);
    }



}
