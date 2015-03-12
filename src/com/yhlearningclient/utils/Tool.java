package com.yhlearningclient.utils;

import java.io.File;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.Toast;

public class Tool {
	
	public static void ShowMessage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 获得视频缩略图
	 * 
	 * @param cr
	 * @param uri
	 * @return
	 */
	public static Bitmap getVideoThumbnail(ContentResolver cr, Uri uri) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Video.Media._ID }, null, null, null);
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		String videoId = cursor.getString(cursor
				.getColumnIndex(MediaStore.Video.Media._ID)); // image id in
																// image table.s

		if (videoId == null) {
			return null;
		}
		cursor.close();
		long videoIdLong = Long.parseLong(videoId);
		bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong,
				Images.Thumbnails.MICRO_KIND, options);
		return bitmap;
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return -1;// 错误
		}
	}

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 发送多媒体MediaPlayer消息用于更新sdCard中的多媒体数据v
	 * 
	 * @param context
	 */
	public static void scanSdCard(Context context,String path) {
		/*IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		ScanSdReceiver scanSdReceiver = new ScanSdReceiver();
		context.registerReceiver(scanSdReceiver, intentfilter);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));*/
		MediaScanner scan =new MediaScanner(context, path);
	}

}
