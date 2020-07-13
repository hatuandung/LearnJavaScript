package com.htd.learnjavascript;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.htd.learnjavascript.databinding.ItemChapterBinding;

import java.util.ArrayList;

import static com.htd.learnjavascript.BR.item;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterHolder> implements Filterable {
    private LayoutInflater inflater;
    private ArrayList<Chapter> data;
    private ArrayList<Chapter> dataAll;
    private ChapterItemClickListener listener;
    private ChapterFilter filter = new ChapterFilter();

    public void setListener(ChapterItemClickListener listener) {
        this.listener = listener;
    }

    public ChapterAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<Chapter> data) {
        this.data = data;
        this.dataAll = data;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterBinding binding = ItemChapterBinding.inflate(inflater,parent, false);
        return new ChapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
        holder.binding.setItem(data.get(position));
        if (listener!= null){
            holder.binding.setListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ChapterHolder extends RecyclerView.ViewHolder {
        ItemChapterBinding binding;

        public ChapterHolder(@NonNull ItemChapterBinding binding) {

            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ChapterItemClickListener{
        void onItemClickListener(Chapter item);
    }

    public class ChapterFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence key) {
            ArrayList<Chapter> result = new ArrayList<>();
            for (Chapter chapter: dataAll) {
                if (chapter.getTitle().toLowerCase().contains(key.toString().toLowerCase())){
                    result.add(chapter);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.count = result.size();
            filterResults.values = result;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<Chapter>) results.values;
            notifyDataSetChanged();
        }
    }
}
