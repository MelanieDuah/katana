package com.katana.dataaccess.concrete;

import android.support.constraint.solver.widgets.Snapshot;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katana.dataaccess.DataAccess;
import com.katana.entities.BaseEntity;
import com.katana.entities.Category;
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
        try {
            OperationResult result = OperationResult.FAILED;
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
            }
            return result;
        } catch (Exception ex) {
            throw new KatanaDataException(String.format("Failed to save {0}", dataItem.toString()), ex);
        }
    }

    @Override
    public <T extends BaseEntity> OperationResult findAllItems(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result = OperationResult.FAILED;

        DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                databaseReference.removeEventListener(this);
            }
        });
        return result;
    }
}
