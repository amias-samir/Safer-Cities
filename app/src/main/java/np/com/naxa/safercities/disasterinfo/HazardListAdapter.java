package np.com.naxa.safercities.disasterinfo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.safercities.R;

public class HazardListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

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
}
