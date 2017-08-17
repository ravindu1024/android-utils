package com.rw.androidutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by ravindu on 23/06/17.
 */


@SuppressWarnings("WeakerAccess")
public abstract class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final long CALLBACK_DELAY = 100;
    private final int ROW_TYPE_MAGIC = 50313;

    private final Context context;
    private boolean enabled = true;
    private PorterDuffColorFilter colorFilter;
    @NonNull
    private View loadMoreDefaultView;
    @Nullable
    private View loadMoreCustomView;
    private OnLoadMoreListener callback;
    private LayoutInflater inflater;

    private boolean loadingMore = false;

    public interface OnLoadMoreListener
    {
        void onRequestLoadMore();
    }

    public abstract int getLmItemViewType(int position);

    public abstract int getLmItemCount();

    public abstract RecyclerView.ViewHolder onLmCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onLmBindViewHolder(RecyclerView.ViewHolder holder, int position);


    public void setLoadMoreComplete()
    {
        loadingMore = false;
    }

    public void setLoadMoreEnabled(boolean enabled)
    {
        this.enabled = enabled;
        Log.d("set loadmore enabled: " + enabled);
        notifyDataSetChanged();
    }

    public void setLoadMoreView(@Nullable View view)
    {
        loadMoreCustomView = view;
    }

    public void setProgressColor(@ColorInt int color)
    {
        colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public void setProgressColorRes(@ColorRes int colorRes)
    {
        int color = ContextCompat.getColor(context, colorRes);
        setProgressColor(color);
    }

    public void setLoadingMoreListener(OnLoadMoreListener listener)
    {
        callback = listener;
    }

    @SuppressLint("InflateParams")
    public LoadMoreAdapter(Context c)
    {
        this.context = c;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == ROW_TYPE_MAGIC)
        {
            loadMoreDefaultView = inflater.inflate(R.layout.commonutils_loadmore_progress, parent, false);
            return new ProgressHolder(loadMoreCustomView != null ? loadMoreCustomView : loadMoreDefaultView);
        }
        else
            return onLmCreateViewHolder(parent, viewType);
    }


    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (position < getLmItemCount())
            onLmBindViewHolder(holder, position);
        else
        {
            ProgressHolder ph = (ProgressHolder) holder;

            if (loadMoreCustomView == null)
            {

                if (colorFilter == null)
                    ph.progress.getIndeterminateDrawable().clearColorFilter();
                else
                    ph.progress.getIndeterminateDrawable().setColorFilter(colorFilter);

            }

            if (ph.progress != null)
                ph.progress.setVisibility(enabled ? View.VISIBLE : View.GONE);
            else
                ph.root.setVisibility(enabled ? View.VISIBLE : View.GONE);

            if (enabled && !loadingMore && callback != null)
            {
                loadingMore = true;
                loadMoreDefaultView.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.onRequestLoadMore();

                    }
                }, CALLBACK_DELAY);
            }

        }
    }

    @Override
    public final int getItemCount()
    {
        return getLmItemCount() + 1;
    }

    private class ProgressHolder extends RecyclerView.ViewHolder
    {
        private ProgressBar progress;
        private View root;

        public ProgressHolder(View itemView)
        {
            super(itemView);

            root = itemView;
            progress = itemView.findViewById(R.id.commonutils_loadmore_progress);
        }
    }

    @Override
    public final int getItemViewType(int position)
    {
        if (position < getLmItemCount())
            return getLmItemViewType(position);
        else
            return ROW_TYPE_MAGIC;
    }
}
