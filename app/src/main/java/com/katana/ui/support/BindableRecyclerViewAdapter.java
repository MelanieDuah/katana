package com.katana.ui.support;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katana.infrastructure.support.Action;
import com.katana.infrastructure.support.ActionFunc;
import com.katana.infrastructure.support.ActionFuncWithOneParam;
import com.katana.ui.BR;
import com.katana.ui.R;
import com.katana.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

/**
 * Created by Akwasi Owusu on 1/21/18
 */

public class BindableRecyclerViewAdapter<T extends BaseViewModel> extends
        RecyclerView.Adapter<BindableRecyclerViewAdapter.KatanaRecyclerViewHolder> implements RefreshListChangeListener.DataSetChangeNotificationReceiver {

    private int layoutId;
    private ObservableArrayList<T> items;
    private ActionFunc adapterObserverAction;
    private int selectedIndex = NO_POSITION;
    private List<Action> selectionChangedActions;
    private int swipeDir = LEFT | RIGHT;


    BindableRecyclerViewAdapter(ObservableArrayList<T> items, int layoutId) {
        this.layoutId = layoutId;
        this.items = items;
        items.addOnListChangedCallback(new RefreshListChangeListener(this));
        this.registerAdapterDataObserver(adapterDataObserver);
        selectionChangedActions = new ArrayList<>();
    }

    void setAdapterObserverAction(ActionFunc adapterObserverAction) {
        this.adapterObserverAction = adapterObserverAction;
        if (adapterObserverAction != null)
            adapterObserverAction.invoke();
    }

    int getSwipeDirs() {
        return swipeDir;
    }

    void setSwipeDir(int swipeDir) {
        this.swipeDir = swipeDir;
    }

    int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    void addSelectionChangedAction(Action selectionChangedAction) {
        this.selectionChangedActions.add(selectionChangedAction);
    }

    @Override
    public KatanaRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new KatanaRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindableRecyclerViewAdapter.KatanaRecyclerViewHolder holder, int position) {
        T itemViewModel = items.get(position);
        holder.bind(itemViewModel);
        holder.itemView.setSelected(selectedIndex == position);
    }

    @Override
    public int getItemViewType(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class KatanaRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewDataBinding binding;
        private View backgroundView, foregroundView;
        private BaseViewModel viewModel;


        KatanaRecyclerViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getBackgroundView() {
            return backgroundView;
        }

        View getForegroundView() {
            return foregroundView;
        }

        public BaseViewModel getViewModel() {
            return viewModel;
        }

        public void bind(BaseViewModel viewModel) {
            this.viewModel = viewModel;
            binding.setVariable(BR.viewmodel, viewModel);
            binding.executePendingBindings();

            backgroundView = itemView.findViewById(R.id.bacgroundView);
            foregroundView = itemView.findViewById(R.id.foregroundView);

            if (foregroundView != null)
                foregroundView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            notifyItemChanged(selectedIndex);
            selectedIndex = getLayoutPosition();
            notifyItemChanged(selectedIndex);
            for (Action action : selectionChangedActions) {
                if (action instanceof ActionFunc)
                    ((ActionFunc) action).invoke();
                else if (action instanceof ActionFuncWithOneParam)
                    ((ActionFuncWithOneParam) action).invoke(selectedIndex);
            }

        }
    }

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {

        private void invokeAdapterObserverAction() {
            if (adapterObserverAction != null)
                adapterObserverAction.invoke();
        }

        @Override
        public void onChanged() {
            invokeAdapterObserverAction();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            invokeAdapterObserverAction();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            invokeAdapterObserverAction();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            invokeAdapterObserverAction();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            invokeAdapterObserverAction();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            invokeAdapterObserverAction();
        }
    };
}
