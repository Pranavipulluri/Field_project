package org.meicode.fieldproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;  // Ensure Picasso is imported

import java.util.List;
public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.WorkViewHolder> {

    private List<Work> worksList;

    public WorksAdapter(List<Work> worksList) {
        this.worksList = worksList;
    }

    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_item, parent, false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkViewHolder holder, int position) {
        Work work = worksList.get(position);
        holder.workTitle.setText(work.getTitle());
        holder.workDescription.setText(work.getDescription());
        // Set other fields (image, etc.)
    }

    @Override
    public int getItemCount() {
        return worksList.size();
    }

    public static class WorkViewHolder extends RecyclerView.ViewHolder {
        TextView workTitle, workDescription;

        public WorkViewHolder(View itemView) {
            super(itemView);
            workTitle = itemView.findViewById(R.id.workTitle);
            workDescription = itemView.findViewById(R.id.workDescription);
        }
    }
}
