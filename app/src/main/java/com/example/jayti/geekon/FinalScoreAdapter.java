package com.example.jayti.geekon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jayti on 5/9/2017.
 */
public class FinalScoreAdapter extends ArrayAdapter<FinalScore>{
    private ArrayList<FinalScore> dataSet;
    Context mContext;
    String winner;

    // View lookup cache
    private static class ViewHolder {
        TextView nickname;
        TextView opp;
        TextView score1;
        TextView score2;
        TextView winner_name;
    }

    public FinalScoreAdapter(ArrayList<FinalScore> data, Context context) {
        super(context, R.layout.scorelist, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FinalScore dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.scorelist, null);


            viewHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            viewHolder.opp = (TextView) convertView.findViewById(R.id.opp);
            viewHolder.score1 = (TextView) convertView.findViewById(R.id.score1);
            viewHolder.score2 = (TextView) convertView.findViewById(R.id.score2);
            viewHolder.winner_name=(TextView)convertView.findViewById(R.id.winner_name);


            if(Integer.parseInt(dataModel.getScore_opp())>Integer.parseInt(dataModel.getScore_user()))
                viewHolder.winner_name.setText(dataModel.getOpponent());
            else
                viewHolder.winner_name.setText(dataModel.getUser());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //result=convertView;
        }

        viewHolder.nickname.setText(dataModel.getUser());
        viewHolder.opp.setText(dataModel.getOpponent());
        viewHolder.score1.setText(dataModel.getScore_user());
        viewHolder.score2.setText(dataModel.getScore_opp());
        return convertView;
    }
}

