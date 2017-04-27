package com.angki.casualread.gank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class WelfareActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {

    private int code;//第几张图片
    private List<String> urlList;//图片地址集合
    private List<String> idList;//图片地址集合
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
        idList = bundle.getStringArrayList("idList");

        LoadMoudle();

    }
    /**
     * Glide 获得图片缓存路径
     */
    private String getImagePath(String imgUrl) {
        String path = null;
        //下载图片
        FutureTarget<File> future = Glide.with(WelfareActivity.this)
                .load(imgUrl)
                .downloadOnly(500, 500);
        try {
            File cacheFile = future.get();
            //获取绝对路径
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
    private static boolean saveImageToGallery(Context context, Bitmap bitmap, String id) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "瞎Read相册");
        //判断这个文件是否存在，不存在就创建该目录
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        //文件命名使用下载时间+.jpg
        String fileName = id + ".jpg";
        File file = new File(appDir, fileName);
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                //图片压缩，其中100表示不压缩
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
            return true;
        }
        return false;
    }

    /**
     * 加载组件
     */
    private void LoadMoudle() {

        imagecount = (TextView) findViewById(R.id.activity_welfare_layout_text);
        imagedownload = (TextView) findViewById(R.id.activity_welfare_layout_save);
        viewPager = (ViewPager) findViewById(R.id.activity_welfare_layout_viewpager);

        adapter = new WelfareViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(code);//跳转第几张相片
        page = code;
        viewPager.setEnabled(true);
        //ViewPager的滑动监听
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end();
            }
        });
        // 设定当前的页数和总页数
        if (urlList.size() > 2) {
            imagecount.setText((code + 1) + "/" + urlList.size());
        }
        //设置保存的事件
        imagedownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //权限申请
                if (ContextCompat.checkSelfPermission(WelfareActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(WelfareActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    saveImage();
                }
            }
        });
    }

    /**
     * 保存相片的方法
     */
    private void saveImage() {

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
                                //判断图片是否已经在相册存在
                                if (saveImageToGallery(WelfareActivity.this, bitmap, idList.get(page))) {
                                    ToastUtil.showToast(WelfareActivity.this, "以保存至" +
                                            Environment.getExternalStorageDirectory()
                                                    .getAbsolutePath()+"/瞎Read相册");
                                }else {
                                    ToastUtil.showToast(WelfareActivity.this, "图片已存在~");
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 权限申请的回调结果
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    saveImage();
                }else {
                    ToastUtil.showToast(WelfareActivity.this, "没有权限，保存图片失败..");
                }
        }
    }

    /**
     * viewpager的adapter
     */
    class WelfareViewPagerAdapter extends PagerAdapter {

        private PhotoView photoView;
        /**
         * 加载每个item
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(final ViewGroup container, int position) {

            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.viewpager_very_image, container, false);
            photoView = (PhotoView)
                    view.findViewById(R.id.viewpager_very_image_photoview);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.loading);
            //保存网络图片的路径
            String url = (String) getItem(position);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setClickable(false);

            Glide.with(WelfareActivity.this).load(url)
                    .crossFade(700)//淡入淡出效果
                    //图片的监听
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            ToastUtil.showToast(WelfareActivity.this, "图片无缓存！ 0.0");
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        //监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).error(R.drawable.ic_meizi)
                    .into(photoView);

            photoView.setOnPhotoTapListener(WelfareActivity.this);//设置单点点击事件
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
        //释放内存，当传入空时，释放内存
        public void notifyDataSetChanged(ArrayList<String> List) {
            if (List == null) {
                Glide.clear(photoView);
                Glide.get(WelfareActivity.this).clearMemory();
                photoView = null;
            }
            notifyDataSetChanged();
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
        end();
    }

    /**
     * 在图片外部的空白区域敲击回调
     */
    @Override
    public void onOutsidePhotoTap() {
        end();
    }

    /**
     * 销毁所占内存
     */
    private void end() {
        //将各个组件置null，清除内存
        urlList.clear();
        urlList = null;
        idList.clear();
        idList = null;
        adapter.notifyDataSetChanged(null);//传入null，到Adapter中去释放内存
        imagecount = null;
        imagedownload = null;
        viewPager = null;
        onBackPressed();
    }
}
