package com.example.kevin.fifastatistics.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.data.Trie;
import com.example.kevin.fifastatistics.databinding.ItemTextBinding;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MatchEventFootballerAdapter extends ArrayAdapter<Footballer> implements Filterable {

    private static final int ITEM_LAYOUT_ID = R.layout.item_text;

    private Trie<Footballer> mFootballers;
    private List<Footballer> mSuggestions;
    private Context mContext;
    private Filter mFilter;

    public MatchEventFootballerAdapter(Context context, Trie<Footballer> footballers) {
        super(context, ITEM_LAYOUT_ID);
        mContext = context;
        setFootballers(footballers);
    }

    public void setFootballers(Trie<Footballer> footballers) {
        mFootballers = footballers;
        mSuggestions = footballers != null ? footballers.allPrefixes() : Collections.emptyList();
    }

    @Override
    public int getCount() {
        return mSuggestions.size();
    }

    @Override
    public Footballer getItem(int position) {
        return mSuggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ItemTextBinding binding = DataBindingUtil.inflate(inflater, ITEM_LAYOUT_ID, parent, false);
            holder = new ViewHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(mSuggestions.get(position));
        return convertView;
    }

    static class ViewHolder {
        ItemTextBinding binding;
        Footballer footballer;

        ViewHolder(ItemTextBinding binding) {
            this.binding = binding;
        }

        void bind(Footballer footballer) {
            this.footballer = footballer;
            binding.setText(footballer.getName());
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new FootballerFilter();
        }
        return mFilter;
    }

    private class FootballerFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (!TextUtils.isEmpty(constraint)) {
                String prefix = constraint.toString().toLowerCase(Locale.getDefault());
                List<Footballer> suggestions = mFootballers.autoComplete(prefix);
                results.values = suggestions;
                results.count = suggestions.size();
            } else {
                results.values = Collections.emptyList();
                results.count = 0;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSuggestions = (List<Footballer>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
