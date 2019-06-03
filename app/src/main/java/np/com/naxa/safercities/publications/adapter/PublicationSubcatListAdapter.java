package np.com.naxa.safercities.publications.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.publications.PublicationsListActivity;

public class PublicationSubcatListAdapter  extends BaseQuickAdapter<String, BaseViewHolder>{

String fileType ;

    public PublicationSubcatListAdapter(int layoutResId, @Nullable List<String> data, String fileType) {
        super(layoutResId, data);
        this.fileType = fileType;
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
                Intent intent = new Intent(linearLayout.getContext(), PublicationsListActivity.class);
                intent.putExtra("title", item);
                intent.putExtra("type", fileType);
                linearLayout.getContext().startActivity(intent);
            }
        });

    }

}

