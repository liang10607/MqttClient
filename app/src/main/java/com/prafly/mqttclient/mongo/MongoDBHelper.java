package com.prafly.mqttclient.mongo;

import android.util.Log;

import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class MongoDBHelper {
    
    private final static String TAG="MongoDBHelper";

    MongoClient mongoClient = null;
//    DB db=null;
    MongoDatabase mongoDatabase=null;
    public MongoDBHelper() throws UnknownHostException {

//        String sURI = String.format("mongodb://%s:%s@%s:%d/%s", "test", "1qazxsw2".toCharArray(), "115.29.39.102", 27817, "test");
//        MongoClientURI uri = new MongoClientURI("mongodb://test:1qazxsw2@115.29.39.102:27817/test");
//          mongoClient = new MongoClient(uri);

//         db= mongoClient.getDB("test");
        try {
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress("115.29.39.102",27817);
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("test", "test", "1qazxsw2".toCharArray());
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            credentials.add(credential);

          //  通过连接认证获取MongoDB连接
            mongoClient = new MongoClient(addrs,credentials);
////            mongoClient = new MongoClient("192.168.0.12",27017);

            //连接到数据库
              mongoDatabase = mongoClient.getDatabase("test");
            Log.d(TAG, "MongoDBHelper: Connect to database successfully");
            System.out.println("Connect to database successfully");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public List<String> getAllTables(){
//        DBCollection coll = db.getCollection("test");
//       DBCursor dbCursor= coll.find();
        List<String> tableSets=new ArrayList<String>();
//        while (dbCursor.iterator().hasNext()){
//            DBObject dbObject=dbCursor.next();
//            tableSets.add(dbObject.toString());
//        }

        MongoCollection<Document> collection = mongoDatabase.getCollection("devices");
        Log.d(TAG, "getAllTables: 集合 test 选择成功");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){
            System.out.println(mongoCursor.next());
            tableSets.add(mongoCursor.next().toJson());
            Log.d(TAG, "getAllTables:  "+mongoCursor.next());
        }
        return tableSets;
    }
}
