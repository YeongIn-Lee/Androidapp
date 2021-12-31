package com.example.mysns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<BlogData> localDataSet;
    private static View.OnClickListener onClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TextView_title;
        public TextView TextView_content;
        public View rootView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            TextView_title = (TextView)view.findViewById(R.id.TextView_title);
            TextView_content = (TextView)view.findViewById(R.id.TextView_content);
            rootView = view;

            view.setClickable(true);
            view.setEnabled(true);
            view.setOnClickListener(onClickListener);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param myDataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MyAdapter(List<BlogData> myDataSet, View.OnClickListener onClick) {

        localDataSet = myDataSet;
        onClickListener = onClick;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LinearLayout view = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_blogs, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        BlogData blog = localDataSet.get(position);
        viewHolder.TextView_title.setText(blog.getTitle());

        if(blog.getContent() != null && blog.getContent().length() > 0) {
            viewHolder.TextView_content.setText(blog.getContent());
        }
        else{
            viewHolder.TextView_content.setText("-");
        }

        //tag - label
        viewHolder.rootView.setTag(position);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet == null ? 0 : localDataSet.size();
    }

    public BlogData getBlog(int position){
        return localDataSet != null ? localDataSet.get(position) : null;
    }
}


