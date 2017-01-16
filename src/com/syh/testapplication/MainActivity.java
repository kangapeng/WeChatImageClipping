package com.syh.testapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import com.syh.wechatImageClipping.R;

/**
 * 这个demo有个问题，就是右侧的那个角的下边框展示不出来，
 * 解决方案是让UI提供两个点九图：bitmap_bg_1和 bitmap_bg_2
 * bitmap_bg_1尖角以上区域的高度比bitmap_bg_2尖角以上区域的高度小10px
 *
 */
public class MainActivity extends Activity {

	private ImageView iv_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		showImage();
	}
	
	private void showImage(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap_bg_1 = BitmapFactory.decodeResource(getResources(), R.drawable.chat_adapter_to_bg);
				Bitmap bitmap_bg_2 = BitmapFactory.decodeResource(getResources(), R.drawable.chat_adapter_to_bg);
				Bitmap bitmap_in = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoliying);
				final Bitmap bp = getRoundCornerImage(bitmap_bg_1, bitmap_in);
				final Bitmap bp2 = getShardImage(bitmap_bg_2, bp);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						iv_image.setImageBitmap(bp2);
					}
				});
			}
		}).start();
	}
	
	public static Bitmap getRoundCornerImage(Bitmap bitmap_bg,Bitmap bitmap_in)
	{
		//创建一个宽高都是500的bitMap
		Bitmap roundConcerImage = Bitmap.createBitmap(790,790, Config.ARGB_8888);
		//用canvas绘制bitMap
		Canvas canvas = new Canvas(roundConcerImage);
		//创建一个画笔
		Paint paint = new Paint();
		//创建一个矩形区域
		Rect rect = new Rect(0,0, 790,790);
		//获取上层图片的矩形区域
		Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
		paint.setAntiAlias(true);
		//获取点九图
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		//将点九图绘制在canvas上
		patch.draw(canvas, rect);
		//将上册图片绘制在canvas上
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap_in, rectF, rect, paint);
		return roundConcerImage;
	}
	public static Bitmap getShardImage(Bitmap bitmap_bg,Bitmap bitmap_in)
	{
		Bitmap roundConcerImage = Bitmap.createBitmap(800,800, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0,0,800,800);
		paint.setAntiAlias(true);
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		patch.draw(canvas, rect);
		//注意：如果设置的宽高大于原图片的宽高，如：800 > 790超出790部分将以其他颜色绘制，如：透明色
		Rect rect2 = new Rect(10,10,800,800);
		canvas.drawBitmap(bitmap_in, rect, rect2, paint);
		return roundConcerImage;
	}
}
