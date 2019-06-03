package np.com.naxa.safercities.disasterinfo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.safercities.R;

public class HazardListAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements Filterable {

    private List<String> wordListFiltered;


    public HazardListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        LinearLayout linearLayout = helper.getView(R.id.hazard_list_item_row_layout);

        helper.setText(R.id.tv_hazard_list_title,item);


        if(((helper.getLayoutPosition()+1) %2) == 0){
            Log.d(TAG, "convert: "+helper.getLayoutPosition());
            helper.setBackgroundColor(R.id.hazard_list_item_row_layout, linearLayout.getContext().getResources().getColor(R.color.listItemSecondBG));
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(linearLayout.getContext(), HazardInfoDetailsActivity.class);
                intent.putExtra("OBJ", item);
                linearLayout.getContext().startActivity(intent);
            }
        });

    }


    public void add(List<String> s, int position) {
        position = position == -1 ? getItemCount() : position;
            wordListFiltered.add(position, s.get(position));
            notifyItemInserted(position);

    }

    public void remove(int position) {
        if (position < getItemCount()) {
                wordListFiltered.remove(position);
                notifyItemRemoved(position);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();
                if (charString.isEmpty()) {
//                    wordListFiltered = wordsWithDetailsList;
                    wordListFiltered = mData;
                } else {
                    List<String> filteredList = new ArrayList<>();
//                    for (WordsWithDetailsModelNew row : wordsWithDetailsList) {
                    for (String row : mData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                        if (row.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    wordListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = wordListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                wordListFiltered = (ArrayList<WordsWithDetailsModelNew>) filterResults.values;
                wordListFiltered = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
