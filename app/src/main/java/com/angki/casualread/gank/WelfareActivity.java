package com.angki.casualread.gank;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.angki.casualread.R;
import com.angki.casualread.util.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class WelfareActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {

    private int code;//第几张图片
    private List<String> urlList;//图片地址集合
    private WelfareViewPagerAdapter adapter;
    private TextView imagecount;
    private TextView imagedownload;
    private ViewPager viewPager;
    private int page;//记录当前是第几张图片


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_layout);

        //数据接收
        Bundle bundle = getIntent().getExtras();
        code = bundle.getInt("code");
        urlList = bundle.getStringArrayList("urlList");

        LoadMoudle();

    }
    /**
     * Glide 获得图片缓存路径
     */
    private String getImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide.with(WelfareActivity.this)
                .load(imgUrl)
                .downloadOnly(500, 500);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("------", "getImagePath:" + path);
        return path;
    }
    /**
     * 保存图片至相册
     */
    private static void saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "瞎Read相册");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.getAbsoluteFile())));
    }

    /**
     * 加载组件
     */
    private void LoadMoudle() {

        imagecount = (TextView) findViewById(R.id.activity_welfare_layout_text);
        imagedownload = (TextView) findViewById(R.id.activity_welfare_layout_save);
        viewPager = (ViewPager) findViewById(R.id.activity_welfare_layout_viewpager);

        adapter = new WelfareViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(code);
        page = code;
        viewPager.setEnabled(false);
        //ViewPager的滑动监听
        viewPager.addOnPageChangeListener(this);
        // 设定当前的页数和总页数
        if (urlList.size() > 2) {
            imagecount.setText((code + 1) + "/" + urlList.size());
        }
        //设置保存的事件
        imagedownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToastUtil.showToast(WelfareActivity.this, "开始下载");
                final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 子线程获得图片路径
                        final String imagePath = getImagePath(urlList.get(page));
                        //主线程更新
                        WelfareActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (imagePath != null) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                                    if (bitmap != null) {
                                        saveImageToGallery(WelfareActivity.this, bitmap);
                                        ToastUtil.showToast(WelfareActivity.this, "以保存至" +
                                                Environment.getExternalStorageDirectory()
                                                        .getAbsolutePath()+"/瞎Read相册");
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * viewpager的adapter
     */
    class WelfareViewPagerAdapter extends PagerAdapter {

        private Context mcontext;

        public WelfareViewPagerAdapter(Context context) {

            mcontext = context;
        }

        /**
         * 加载每个item
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(mcontext)
                    .inflate(R.layout.viewpager_very_image, container, false);
            final PhotoView photoView = (PhotoView)
                    view.findViewById(R.id.viewpager_very_image_photoview);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loading);
            //保存网络图片的路径
            String url = (String) getItem(position);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setClickable(false);

            Glide.with(mcontext).load(url)
                    .crossFade(700)//淡入淡出效果
                    //图片的监听
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Toast.makeText(mcontext, "图片加载异常", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        //监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(photoView);

            photoView.setOnPhotoTapListener(WelfareActivity.this);//设置点击事件
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            if (urlList == null || urlList.size() == 0) {
                return 0;
            }
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        Object getItem(int position) {
            return urlList.get(position);
        }
    }

    /**
     * 下面是对Viewpager的监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 监听viewpager滑动的时候的操作
     */
    @Override
    public void onPageSelected(int position) {
        Log.d("-----", "position: " + position);
        imagecount.setText((position + 1) + "/" + urlList.size());
        page = position;
    }

    /**
     * 当用户敲击在照片上时回调，如果在空白区域不会回调
     * @param view
     * @param x
     * @param y
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        finish();
    }

    /**
     * 在图片外部的空白区域敲击回调
     */
    @Override
    public void onOutsidePhotoTap() {
        finish();
    }
}
