package np.com.naxa.safercities.newhomepage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.emergencynumbers.EmergencyNumbersActivity;
import np.com.naxa.safercities.utils.recycleviewutils.RecyclerViewType;

/**
 * Created by samir on 01/12/18..
 */

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel, showAllButton;
        private ImageView  dottedViewHead;
        private RecyclerView itemRecyclerView;
        private RelativeLayout dottedViewLine;

        public SectionViewHolder(View itemView) {
            super(itemView);
            dottedViewLine = (RelativeLayout) itemView.findViewById(R.id.dottedView);
            dottedViewHead = (ImageView) itemView.findViewById(R.id.dottedLineHead);
            sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
            showAllButton = (TextView) itemView.findViewById(R.id.section_show_all_button);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private Context context;
    private RecyclerViewType recyclerViewType;
    private ArrayList<SectionModel> sectionModelArrayList;
    int type_empty=1, type_option=2;

    public SectionRecyclerViewAdapter(Context context, RecyclerViewType recyclerViewType, ArrayList<SectionModel> sectionModelArrayList) {
        this.context = context;
        this.recyclerViewType = recyclerViewType;
        this.sectionModelArrayList = sectionModelArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if(sectionModelArrayList.get(position) == null){
            return type_empty;
        }else {
            return type_option;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == type_option) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
            return new SectionViewHolder(view);
        }else {
            View view =new View(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixel(90, parent.getContext()));
            view.setLayoutParams(layoutParams);
            return new EmptyViewHolder(view);
        }
    }

     private int convertDpToPixel(int dp, Context context){
        return dp * ( context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mholder, int position) {
        if(mholder instanceof SectionViewHolder) {
            SectionViewHolder holder = (SectionViewHolder)mholder;
            final SectionModel sectionModel =
                    sectionModelArrayList.get(position);
            holder.showAllButton.setVisibility(View.GONE);


            if (position == 1) {
                holder.dottedViewLine.setBackground(holder.dottedViewLine.getContext().getResources().getDrawable(R.drawable.dotted_line_orange));
                holder.dottedViewHead.setBackground(holder.dottedViewHead.getContext().getResources().getDrawable(R.drawable.dotted_line_orange_head));
            }
            if (position == 2) {
                holder.dottedViewLine.setBackground(holder.dottedViewLine.getContext().getResources().getDrawable(R.drawable.dotted_line_green));
                holder.dottedViewHead.setBackground(holder.dottedViewHead.getContext().getResources().getDrawable(R.drawable.dotted_line_green_head));
            }

            holder.sectionLabel.setText(sectionModel.getSectionLabel());

            //recycler view for items
            holder.itemRecyclerView.setHasFixedSize(true);
            holder.itemRecyclerView.setNestedScrollingEnabled(false);

            /* set layout manager on basis of recyclerview enum type */
            switch (recyclerViewType) {
                case LINEAR_VERTICAL:
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
                    break;
                case LINEAR_HORIZONTAL:
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.itemRecyclerView.setLayoutManager(linearLayoutManager1);
                    break;
                case GRID:
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    holder.itemRecyclerView.setLayoutManager(gridLayoutManager);
                    break;
            }
            ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(context, sectionModel.getItemArrayList(), sectionModel.getItemDrawableArrayList());

            holder.itemRecyclerView.setAdapter(adapter);


            //show toast on click of show all button
            holder.showAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.showAllButton.getContext().startActivity(new Intent(context, EmergencyNumbersActivity.class));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();
    }


}

class EmptyViewHolder extends RecyclerView.ViewHolder{

    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}