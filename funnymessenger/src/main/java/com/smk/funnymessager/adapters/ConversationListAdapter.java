package com.smk.funnymessager.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.smk.funnymessager.R;
import com.smk.funnymessager.database.DatabaseManager;
import com.smk.funnymessager.database.controller.MessageController;
import com.smk.funnymessager.models.Message;
import com.smk.funnymessager.models.User;
import com.smk.funnymessager.services.ChatService;
import com.smk.funnymessager.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConversationListAdapter extends BaseAdapter {

	private final User mMe;
	private final User mFriend;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Message> listItem;
	private boolean firstSelfVisible = true;
	private boolean firstNotSelfVisible = true;

	public ConversationListAdapter(Context ctx, List<Message> list, User me, User friend){
		mContext = ctx;
		listItem = list;
		mMe = me;
		mFriend = friend;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public Message getItem(int position) {
		// TODO Auto-generated method stub
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if(getItem(position).isSelf()){
           return 1;
        }
	    return 0;
	}

	@Override
	public int getViewTypeCount() {
	    return 2; // Count of different layouts
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
		    // You can move this line into your constructor, the inflater service won't change.
		    mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		    if(getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.list_item_message_left, null);
            }else {
                convertView = mInflater.inflate(R.layout.list_item_message_right, null);
            }
            holder = new ViewHolder();
            holder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
            holder.txt_status = (TextView) convertView.findViewById(R.id.txt_status);
            holder.img_user = (IconicsImageView) convertView.findViewById(R.id.img_user);
            holder.txt_date_status = (RelativeTimeTextView) convertView.findViewById(R.id.txt_date_status);
            holder.img_user = (IconicsImageView) convertView.findViewById(R.id.img_user);
        	convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
        if(getItem(position).isSelf()) {
			Picasso.with(mContext).load(mMe.getImage()).placeholder(getIconicDrawable(GoogleMaterial.Icon.gmd_account_circle.toString(),android.R.color.white,50)).transform(new CircleTransform()).into(holder.img_user);
			if(getItem(position).getSeen()){
				holder.txt_status.setText("Seen:");
			}else{
				holder.txt_status.setText("Delivered:");
			}
			try {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = null;
				d = f.parse(getItem(position).getUpdatedAt());
				Date newDate = DateUtils.addHours(d, 10);
				Date changeDate = DateUtils.addMinutes(newDate, 30);
				holder.txt_date_status.setReferenceTime(changeDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else {
			Picasso.with(mContext).load(mFriend.getImage()).placeholder(getIconicDrawable(GoogleMaterial.Icon.gmd_account_circle.toString(),android.R.color.white,50)).transform(new CircleTransform()).into(holder.img_user);
			try {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = null;
				d = f.parse(getItem(position).getUpdatedAt());
				Date newDate = DateUtils.addHours(d, 10);
				Date changeDate = DateUtils.addMinutes(newDate, 30);
				holder.txt_date_status.setReferenceTime(changeDate.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

            if(!getItem(position).getSeen()){
                Message message = getItem(position);
                message.setSeen(true);
                message.setType("seen");
				DatabaseManager<Message> databaseManager = new MessageController(mContext.getApplicationContext());
				databaseManager.update(message);
                ChatService.client.send(new Gson().toJson(message));
            }
		}
        holder.txt_message.setText(getItem(position).getMessage());

		return convertView;
	}

	public IconicsDrawable getIconicDrawable(String icon, int color, int size){
		return new IconicsDrawable(mContext)
				.icon(icon)
				.color(mContext.getResources().getColor(color))
				.sizeDp(size);
	}

	private String changeDateFormat(String date){
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yy hh:mm");

		String reformattedStr = null;
		try {

			reformattedStr = myFormat.format(fromUser.parse(date));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reformattedStr;
	}
	
	public static class ViewHolder {
		TextView txt_message;
		TextView txt_status;
		RelativeTimeTextView txt_date_status;
		IconicsImageView img_user;
		
	}
	
	

}
