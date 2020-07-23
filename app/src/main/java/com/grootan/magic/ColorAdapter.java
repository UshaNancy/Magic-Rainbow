package com.grootan.magic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {
    private String colorName[];
    private String color[];
    private Context context;
    private int count_start = 0;
    private int count_end = 0;
    private int[] gravities = new int[2];

    public ColorAdapter(String[] colorName, String[] color, Context context) {
        this.colorName = colorName;
        this.color = color;
        this.context = context;
    }

    @NonNull
    @Override
    public ColorAdapter.ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v1 = inflater.inflate(R.layout.listitems, parent, false);
        ColorAdapter.ColorHolder viewHolder = new ColorHolder(v1);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ColorHolder holder, int position) {

        holder.colorName.setText(colorName[position]);
        holder.linearLayout.setBackgroundColor(Color.parseColor(color[position]));

        if (BuildConfig.FLAVOR.equals("beta")) {
            if (position % 2 == 0) {
                holder.linearLayout.setGravity(Gravity.END);
            } else {
                holder.linearLayout.setGravity(Gravity.START);
            }
        } else if (BuildConfig.FLAVOR.equals("prod")) {
            gravities[0] = Gravity.START;
            gravities[1] = Gravity.END;
            Random random = new Random();

            //from 0 to gravities.length get a random number
            int randomIndex = random.nextInt(gravities.length);
            //which will be some random gravity constant
            int randomGravity = gravities[randomIndex];
            holder.linearLayout.setGravity(randomGravity);
        }


        if (holder.linearLayout.getGravity() == Gravity.START + Gravity.TOP) {

            count_start++;
        } else {
            count_end++;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if (BuildConfig.FLAVOR.equals("prod")) {
                    if (holder.linearLayout.getGravity() == Gravity.END + Gravity.TOP) {
                        holder.linearLayout.setGravity(Gravity.START);
                        count_start++;
                        count_end--;

                        if (count_start == 7) {
                            Intent intent = new Intent(context.getApplicationContext(), QRCodeGeneratorActivity.class);
                            intent.putExtra("message", "Success- Aligned Left");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            Toast.makeText(context, "Success- Aligned Left", Toast.LENGTH_SHORT).show();
                        }
                    } else if (holder.linearLayout.getGravity() == Gravity.START + Gravity.TOP) {
                        holder.linearLayout.setGravity(Gravity.END);
                        count_end++;
                        count_start--;
                        if (count_end == 7) {
                            Intent intent = new Intent(context.getApplicationContext(), QRCodeGeneratorActivity.class);
                            intent.putExtra("message", "Success- Aligned Right");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            Toast.makeText(context, "Success- Aligned Right", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return colorName.length;
    }

    public class ColorHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;
        private TextView colorName;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linear);
            colorName = itemView.findViewById(R.id.colorName);
        }
    }
}
