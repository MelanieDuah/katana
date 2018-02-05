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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akwasi Owusu on 11/16/17
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
                        T savedDataItem = (T) dataSnapshot.getValue(dataItem.getClass());
                        if (savedDataItem != null)
                            savedDataItem.setId(dataSnapshot.getKey());
                        if (operationCallBack != null)
                            operationCallBack.onOperationSuccessful(savedDataItem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (operationCallBack != null)
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
    public <T extends BaseEntity> OperationResult addDataItems(OperationCallBack<T> operationCallBack, T[] dataItems) throws KatanaDataException {
        OperationResult operationResult = OperationResult.FAILED;
        Map<String, Object> items = new HashMap<>();

        for (T dataItem : dataItems) {
            items.put(dataItem.getClass().getSimpleName() + '/' + dataItem.getId(), dataItem);
        }

        database.getReference().updateChildren(items, (databaseError, databaseReference) -> {
            if (operationCallBack != null) {
                if (databaseError != null)
                    operationCallBack.onOperationFailed(databaseError.toException());
                else
                    operationCallBack.onCollectionOperationSuccessful(Arrays.asList(dataItems));
            }
        });

        return operationResult;
    }

    @Override
    public <T extends BaseEntity> OperationResult findAllItems(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;

        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleMultipleItemSnapshot(dataSnapshot, itemClass, operationCallBack);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findAllItemsByField(Class<T> itemClass, String fieldName, String fieldValue, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;
        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByChild(fieldName).equalTo(fieldValue).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleMultipleItemSnapshot(dataSnapshot, itemClass, operationCallBack);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
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
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
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
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    private <T extends BaseEntity> void handleSingleItemSnapshot(DataSnapshot dataSnapshot, OperationCallBack<T> operationCallBack, Class<T> itemClass) {
        try {
            T dataItem = null;
            if (dataSnapshot.hasChildren()) {
                DataSnapshot dataItemSnapshot = dataSnapshot.getChildren().iterator().next();
                if (dataItemSnapshot.hasChildren()) {
                    dataItem = dataItemSnapshot.getValue(itemClass);
                    assert dataItem != null;
                    dataItem.setId(dataItemSnapshot.getKey());
                }
            }
            if (operationCallBack != null)
                operationCallBack.onOperationSuccessful(dataItem);
        } catch (Exception e) {
            if (operationCallBack != null)
                operationCallBack.onOperationFailed(e);
        }
    }

    @Override
    public <T extends BaseEntity> String createAndReturnNewIdFor(Class<T> itemClass, T dataItem) throws KatanaDataException {
        return database.getReference(itemClass.getSimpleName()).push().getKey();
    }

    @Override
    public <T extends BaseEntity> OperationResult findLastInsertedItemFor(Class<T> itemClass, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;

        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleSingleItemSnapshot(dataSnapshot, operationCallBack, itemClass);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException("Data operation error", ex);
        }

        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findItemsInRange(Class<T> itemClass, String startPoint, String endPoint, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;
        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByKey().startAt(startPoint).endAt(endPoint).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleMultipleItemSnapshot(dataSnapshot, itemClass, operationCallBack);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException("Data operation error", ex);
        }
        return result;
    }

    @Override
    public <T extends BaseEntity> OperationResult findItemsInRangeOrderedByField(Class<T> itemClass, String fieldName, String startPoint, String endPoint, OperationCallBack<T> operationCallBack) throws KatanaDataException {
        OperationResult result;
        try {
            DatabaseReference databaseReference = database.getReference(itemClass.getSimpleName());
            databaseReference.orderByChild(fieldName).startAt(startPoint).endAt(endPoint).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    handleMultipleItemSnapshot(dataSnapshot, itemClass, operationCallBack);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (operationCallBack != null)
                        operationCallBack.onOperationFailed(new KatanaDataException("Database error", databaseError.toException()));
                }
            });
            result = OperationResult.SUCCESSFUL;
        } catch (Exception ex) {
            throw new KatanaDataException("Data operation error", ex);
        }
        return result;
    }

    private <T extends BaseEntity> void handleMultipleItemSnapshot(DataSnapshot dataSnapshot, Class<T> itemClass, OperationCallBack<T> operationCallBack) {
        List<T> dataItems = new ArrayList<>();
        Iterable<DataSnapshot> results = dataSnapshot.getChildren();
        for (DataSnapshot data : results) {
            T dataItem = null;
            if (data.hasChildren()) {
                dataItem = data.getValue(itemClass);
                assert dataItem != null;
                dataItem.setId(data.getKey());
                dataItems.add(dataItem);
            }
        }
        if (operationCallBack != null)
            operationCallBack.onCollectionOperationSuccessful(dataItems);
    }
}
