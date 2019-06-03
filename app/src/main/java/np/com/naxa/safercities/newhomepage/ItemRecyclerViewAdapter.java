package np.com.naxa.safercities.newhomepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.activity.NotifyOthersActivity;
import np.com.naxa.safercities.activity.MyCircleProfileActivity;
import np.com.naxa.safercities.inventory.InventoryActivity;
import np.com.naxa.safercities.publications.PublicationsSubCatListActivity;
import np.com.naxa.safercities.report.ReportActivity;
import np.com.naxa.safercities.disasterinfo.HazardInfoActivity;
import np.com.naxa.safercities.drr_dictionary.data_glossary.GlossaryListActivity;
import np.com.naxa.safercities.emergencyContacts.EmergencyContactsActivity;
import np.com.naxa.safercities.mapboxmap.OpenSpaceMapActivity;
import np.com.naxa.safercities.publications.PublicationsListActivity;
import np.com.naxa.safercities.quiz.QuizHomeActivity;
import np.com.naxa.safercities.report.ReportCtegoryActivity;
import np.com.naxa.safercities.utils.DialogFactory;

/**
 * Created by samir on 01/12/18.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel;
        private ImageView itemImage;
        private CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.item_label);
            itemImage = (ImageView) itemView.findViewById(R.id.item_imageView);
            cardView = (CardView) itemView.findViewById(R.id.item_card);
        }
    }

    private Context context;
    private ArrayList<String> arrayList;
    private ArrayList<Drawable> arrayListIcon;

    public ItemRecyclerViewAdapter(Context context, ArrayList<String> arrayList, ArrayList<Drawable> arrayListIcon) {
        this.context = context;
        this.arrayList = arrayList;
        this.arrayListIcon = arrayListIcon;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.itemLabel.setText(arrayList.get(position));
        if(arrayListIcon.get(position) != null){
            holder.itemImage.setBackground(arrayListIcon.get(position));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, arrayList.get(position) + " Clicked", Toast.LENGTH_SHORT).show();
                startNewActivity(arrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private void startNewActivity(String gridItemTitle) {
        switch (gridItemTitle) {
            case "FIND OPEN SPACE":
                context.startActivity(new Intent(context, OpenSpaceMapActivity.class));

                break;

            case "Report an incident":
//                DialogFactory.createCustomDialog(context,
//                        "If you want to submit a detailed report, fill the following form.",
//                        new DialogFactory.CustomDialogListener() {
//                            @Override
//                            public void onClick() {
                                context.startActivity(new Intent(context, ReportCtegoryActivity.class));
//                            }
//                        }).show();
                break;
            case "Report":
                context.startActivity(new Intent(context, ReportActivity.class));
                break;

            case "NOTIFY OTHERS":
                context.startActivity(new Intent(context, NotifyOthersActivity.class));
                break;

            case "My CIRCLE":
                context.startActivity(new Intent(context, MyCircleProfileActivity.class));
                break;

            case "EMERGENCY NUMBERS":
                context.startActivity(new Intent(context, EmergencyContactsActivity.class));
                break;

            case "Hazard Info":
                context.startActivity(new Intent(context, HazardInfoActivity.class));
                break;

            case "Play Quiz":
                context.startActivity(new Intent(context, QuizHomeActivity.class));
                break;

            case "Terminologies":
                context.startActivity(new Intent(context, GlossaryListActivity.class));
                break;

            case "Emergency materials":
                context.startActivity(new Intent(context, InventoryActivity.class));
                break;

            case "Multimedia":
                Intent intent = new Intent(context, PublicationsListActivity.class);
                context.startActivity(intent);
                break;

            case "Audio":
                Intent intent2 = new Intent(context, PublicationsSubCatListActivity.class);
                intent2.putExtra("title", "Audio");
                context.startActivity(intent2);
                break;

            case "Video":
                Intent intent3 = new Intent(context, PublicationsSubCatListActivity.class);
                intent3.putExtra("title", "Video");
                context.startActivity(intent3);
                break;

            case "Documents":
                Intent intent4 = new Intent(context, PublicationsSubCatListActivity.class);
                intent4.putExtra("title", "Documents");
                context.startActivity(intent4);
                break;

            case "Brochure":
                Intent intent5 = new Intent(context, PublicationsSubCatListActivity.class);
                intent5.putExtra("title", "Brochure");
                context.startActivity(intent5);
                break;

            case "MAP":
                DialogFactory.createBaseLayerDialog(context, new DialogFactory.CustomBaseLayerDialogListner() {
                    @Override
                    public void onStreetClick() {

                    }

                    @Override
                    public void onSatelliteClick() {

                    }

                    @Override
                    public void onOpenStreetClick() {

                    }

                    @Override
                    public void onMetropolitanClick() {

                    }

                    @Override
                    public void onWardClick() {

                    }
                }).show();
//                context.startActivity(new Intent(context, HomeActivity.class));
                break;
        }

    }


}
