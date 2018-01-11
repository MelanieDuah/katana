package com.katana.dataaccess.concrete;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.BaseEntity;
import com.katana.infrastructure.exceptions.KatanaDataException;
import com.katana.infrastructure.support.OperationCallBack;
import com.katana.infrastructure.support.OperationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akwasi Owusu on 11/16/17.
 */

public class FirebaseDataAccess implements DataAccess {

    private FirebaseDatabase database;

    public FirebaseDataAccess() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public <T extends BaseEntity> OperationResult addDataItem(T dataItem, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result = OperationResult.FAILED;
        try {
            if (dataItem != null) {
                DatabaseReference databaseReference = database.getReference(dataItem.getClass().getSimpleName()).push();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T savedDataItem = (T)dataSnapshot.getValue(dataItem.getClass());
                        savedDataItem.setId(dataSnapshot.getKey());
                        operationCallBack.onOperationSuccessful(savedDataItem);
                        databaseReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                        databaseReference.removeEventListener(this);
                    }
                });
                databaseReference.setValue(dataItem);
                result = OperationResult.SUCCESSFUL;
            }
        } catch (Exception ex) {
            throw new KatanaDataException(String.format("Failed to save %s", dataItem.toString()), ex);
        }
        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findAllItems(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;

        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<T> items = new ArrayList<>();
                    Iterable<DataSnapshot> results = dataSnapshot.getChildren();

                    for (DataSnapshot data : results) {
                        T dataItem = data.getValue(itemClass);
                        dataItem.setId(data.getKey());
                        items.add(dataItem);
                    }
                    operationCallBack.onCollectionOperationSuccessful(items);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        }catch (Exception ex){
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findItem(Class<T> itemClass, String fieldName, String fieldValue, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;
        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByChild(fieldName).equalTo(fieldValue).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleSingleItemSnapshot(dataSnapshot, operationCallBack, itemClass);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        }catch (Exception ex){
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findItemByKey(Class<T> itemClass, String key, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;
        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByKey().equalTo(key).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleSingleItemSnapshot(dataSnapshot, operationCallBack, itemClass);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        }catch (Exception ex){
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    private <T> void handleSingleItemSnapshot(DataSnapshot dataSnapshot, OperationCallBack<T> operationCallBack, Class<T> itemClass){
        try {
            T dataItem = null;
            if(dataSnapshot.hasChildren()) {
                dataItem = dataSnapshot.getChildren().iterator().next().getValue(itemClass);
            }
            operationCallBack.onOperationSuccessful(dataItem);
        }catch (Exception e) {
            operationCallBack.onOperationFailed(e);
        }
    }
}
