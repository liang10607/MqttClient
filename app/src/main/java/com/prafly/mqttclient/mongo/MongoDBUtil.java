package com.prafly.mqttclient.mongo;
//
///**
// * Created by Administrator on 2017/6/19 0019.
// */
//import java.util.List;
//import java.util.Set;
//
//import org.bson.types.ObjectId;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.mongodb.Mongo;
//
///**
// *  Class Name: MongoDBUtil.java
// *  Function:
// *          The Util that MongoDB Operate.
// *     Modifications:
// *
// *  @author Gym Yung.
// *  @DateTime 2014-10-29 下午1:56:49
// *  @version 1.0
// */
public class MongoDBUtil {
//    static Mongo connection = null;
//    static DB db = null;
//
//    private String address="10.0.2.2:27017";
//
//    public MongoDBUtil(String addr,String dbName) throws Exception
//    {
//        this.address=addr;
//        connection = new Mongo(address);
//        db = connection.getDB(dbName);
//    }
//
//    public static Mongo getConnection() {
//        return connection;
//    }
//
//    public Set<String> queryDBTables(){
//        return db.getCollectionNames();
//    }
//
//    public void createCollection(String collName)
//    {
//        DBObject dbs = new BasicDBObject();
//        dbs.put("test", "test");
//        db.createCollection(collName, dbs);
//    }
//
//    /**
//     * Insert dbObject into collection.
//     * @param dbObject
//     * @param collName
//     */
//    public void insert(DBObject dbObject,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        collection.insert(dbObject);
//    }
//
//    /**
//     * Insert dbObject list into collection.
//     * @param dbObjects
//     * @param collName
//     */
//    public void insertBatch(List<DBObject> dbObjects,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        collection.insert(dbObjects);
//    }
//
//    /**
//     * Delete data By Id.
//     * @param id
//     * @param collName
//     * @return
//     */
//    public int deleteById(String id,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        DBObject dbs = new BasicDBObject("_id", new ObjectId(id));
//        int counts = collection.remove(dbs).getN();
//        return counts;
//    }
//
//    /**
//     * Delete data By Condition.
//     * @param dbObject
//     * @param collName
//     * @return
//     */
//    public int deleteByDbs(DBObject dbObject,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        int count = collection.remove(dbObject).getN();
//        return count;
//    }
//
//    /**
//     * Update Data.
//     * @param find
//     * @param update
//     * @param upsert
//     * @param multi
//     * @param collName
//     * @return
//     */
//    public int update(DBObject find,DBObject update,boolean upsert,boolean multi,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        int count = collection.update(find, update, upsert, multi).getN();
//        return count;
//    }
//
//
//    public DBCursor findWithPage(DBObject where,DBObject selection,int start,int limit,String collName)
//    {
//        DBCursor cursor = findNoPage(where, selection, collName);
//        return cursor.limit(limit).skip(start);
//    }
//
//
//    public DBCursor findNoPage(DBObject where,DBObject selection,String collName)
//    {
//        DBCollection collection = db.getCollection(collName);
//        DBCursor cursor = collection.find(where, selection);
//        return cursor;
//    }
}