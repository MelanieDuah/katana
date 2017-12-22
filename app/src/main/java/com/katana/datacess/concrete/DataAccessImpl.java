package com.katana.datacess.concrete;

import com.katana.datacess.DataAccess;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class DataAccessImpl implements DataAccess {

    MongoDatabase mongodb;

    public DataAccessImpl() {

        String connectionString = "mongodb://akwasiowusu28:28.mysteries@cluster0-shard-00-00-kxsgs.mongodb.net:27017,cluster0-shard-00-01-kxsgs.mongodb.net:27017,cluster0-shard-00-02-kxsgs.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";
        MongoClient mongoClient = new MongoClient(new MongoClientURI(connectionString));
        mongodb = mongoClient.getDatabase("katanadb");
    }

    @Override
    public <T> OperationResult addDataItem(T dataItem) throws KatanaDataException {
        try {
            mongodb.getCollection(dataItem.getClass().getName()).insertOne(new Document());
            return OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException(String.format("Failed to save {0}", dataItem.toString()), ex);
        }
    }
}
