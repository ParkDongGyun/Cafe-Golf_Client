package com.sbsj.cafgolf.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.sbsj.cafgolf.R;

import java.util.ArrayList;

public class GalleryAdapter extends PagerAdapter {

	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<Drawable> imglist = new ArrayList<>();


	public GalleryAdapter(Context context, LayoutInflater inflater, ArrayList<Drawable> list) {
		this.context = context;
		this.layoutInflater = inflater;
		this.imglist = list;
	}

	@Override
	public int getCount() {
		return imglist.size();
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View view = null;

		view = layoutInflater.inflate(R.layout.vp_gallery,null);
		ImageView img = (ImageView)view.findViewById(R.id.iv_vp_gallery);
		Glide.with(context).load(imglist.get(position)).into(img);
//		img.setImageBitmap(imglist.get(position));
		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View)object);
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}
}
