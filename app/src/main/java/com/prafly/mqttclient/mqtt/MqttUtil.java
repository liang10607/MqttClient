package com.prafly.mqttclient.mqtt;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.prafly.mqttclient.MainActivity;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MqttUtil {

    private String host = "tcp://192.168.0.158:1883";
    private String userName = "admin";
    private String passWord = "admin";

    private MqttClient client;

    private String myTopic = "test/topic";

    private String[] topics=new String[]{"test/top1","liang/top","yun/user"};

    private int[] qos=new int[]{0,1,2,3,4};

    private MqttConnectOptions options;

    private ScheduledExecutorService scheduler;


    private MqttTopic mqttTopic;
    private MqttMessage mqttMessage;

    public void setMqttCallback(MqttCallback mqttCallback) {
        this.mqttCallback = mqttCallback;
    }

    MqttCallback mqttCallback;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    System.out.println("-----------------------------");
//                    mlist.add("------收到消息"+msg.obj);
                    break;

                case 2:

//                    mlist.add("连接成功");
                    try {
//                            client.subscribe(myTopic, 1);
//                            client.subscribe(topics,qos);
                        client.subscribe(topics);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:

//                    mlist.add("连接失败，系统正在重连");
                    break;

                case 4:
                    try {
//                        mlist.add("connectionLost----------");
                        client.connect(options);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };


    public void sendTopic(View view){

        try {
            if (client == null || !client.isConnected()) {
                connect();
            }
//
//            logger.info("send message to " + clientId + ", message is "
//                    + message);
            // 发布自己的消息
            int clientId=1221;
            client.publish("GMCC/client/" + clientId, "客户端发送的消息".getBytes(),
                    0, false);
        } catch (MqttException e) {
//            ;.error(e.getCause());
            e.printStackTrace();
        }

        MqttDeliveryToken token = null;
        try {
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        System.out.println(token.isComplete()+"========");
    }

    public  void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if(!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    public  void init(String addr) {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

            if (addr!=null) host=addr;
            client = new MqttClient(host, "test",
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(mqttCallback);
//          connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mqttTopic = client.getTopic(myTopic);

                    mqttMessage = new MqttMessage();
                    mqttMessage.setQos(1);
                    mqttMessage.setRetained(true);
                    System.out.println(mqttMessage.isRetained()+"------ratained状态");
                    mqttMessage.setPayload("eeeeeaaaaaawwwwww---".getBytes());
                    client.connect(options);
//                    client.subscribe(topics,qos);

//                    mqttTopic=client.getTopic()
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


}
