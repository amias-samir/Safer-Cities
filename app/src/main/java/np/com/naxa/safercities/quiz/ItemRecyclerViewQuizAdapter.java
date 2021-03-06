package np.com.naxa.safercities.quiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.quiz.entity.QuizCategory;
import np.com.naxa.safercities.quiz.quiznew.McqQuizTestActivity;
import np.com.naxa.safercities.utils.colorutils.ColorList;

/**
 * Created by samir on 01/12/18.
 */

public class ItemRecyclerViewQuizAdapter extends RecyclerView.Adapter<ItemRecyclerViewQuizAdapter.ItemViewHolder> {

    String TAG = "EmergencyNoAdapter";

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemLabel, quiz_percentage;
        ImageButton item_bg ;
        private CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemLabel = (TextView) itemView.findViewById(R.id.item_label);
            item_bg = (ImageButton) itemView.findViewById(R.id.item_bg);
            quiz_percentage = (TextView) itemView.findViewById(R.id.quiz_percentage);
            cardView = (CardView) itemView.findViewById(R.id.item_card);
        }
    }

    private Context context;
    private List<QuizCategory> arrayList;

    public ItemRecyclerViewQuizAdapter(Context context, List<QuizCategory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_quiz_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        holder.itemLabel.setText(arrayList.get(position).getName());
        holder.itemLabel.setTextColor(ColorList.COLORFUL_COLORS[position]);

        DrawableCompat.setTint(holder.item_bg.getBackground().mutate(), ColorList.COLORFUL_COLORS[position]);

        Drawable mDrawable1 = context.getResources().getDrawable(R.drawable.button_rounded_purple).mutate().getCurrent();
        mDrawable1.setColorFilter(new
                PorterDuffColorFilter(ColorList.COLORFUL_COLORS[position],PorterDuff.Mode.SRC));
       holder.quiz_percentage.setBackground(mDrawable1);


        holder.item_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, arrayList.get(position) + " Clicked", Toast.LENGTH_SHORT).show();
//                startNewActivity(arrayList.get(position));
//                EventBus.getDefault().post(new EmergenctContactCallEvent.ContactItemClick(arrayList.get(position)));

                Intent intent = new Intent(holder.item_bg.getContext(), McqQuizTestActivity.class);
                intent.putExtra("OBJ", arrayList.get(position));
                holder.item_bg.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
