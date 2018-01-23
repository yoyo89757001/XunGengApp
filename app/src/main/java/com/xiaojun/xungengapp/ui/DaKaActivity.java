package com.xiaojun.xungengapp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;
import com.sdsmdg.tastytoast.TastyToast;
import com.xiaojun.xungengapp.R;
import com.xiaojun.xungengapp.beans.DataSynEvent;
import com.xiaojun.xungengapp.beans.MainBean;
import com.xiaojun.xungengapp.intface.ClickIntface2;
import com.xiaojun.xungengapp.utils.FileUtil;
import com.xiaojun.xungengapp.utils.SpringEffect;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static org.greenrobot.eventbus.EventBus.TAG;

public class DaKaActivity extends Activity implements ClickIntface2 {
    @BindView(R.id.yulan)
    Button yulan;
    private ZhaoPianAdapter zhaoPianAdapter = null;
    private List<String> stringList;
    private RecyclerView recyclerView, recyclerView2;
    private File mSavePhotoFile = null;
    private ImageView shiping_im;
    private String video_uri = null;
    private String output_directory = null;
    private String video_screenshot = null;
    private DataSynEvent dataSynEvent=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "创建");


        video_uri = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_URI);
        output_directory = getIntent().getStringExtra(MediaRecorderActivity.OUTPUT_DIRECTORY);
        video_screenshot = getIntent().getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);

        if (video_uri != null || output_directory != null && video_screenshot != null) {

            EventBus.getDefault().post(new DataSynEvent(video_uri, output_directory, video_screenshot));
            finish();
        } else {
            EventBus.getDefault().register(DaKaActivity.this);//订阅
        }

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            // window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_da_ka);
        ButterKnife.bind(this);
        shiping_im = (ImageView) findViewById(R.id.shiping_im);

        stringList = new ArrayList<>();
        stringList.add("pathOne");
        recyclerView = (RecyclerView) findViewById(R.id.recy);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        // recyclerView.addItemDecoration(new MyDecoration(DaKaActivity.this, LinearLayoutManager.VERTICAL,10,R.color.transparent));

        zhaoPianAdapter = new ZhaoPianAdapter(stringList);
        zhaoPianAdapter.setClickIntface(this);
        recyclerView.setAdapter(zhaoPianAdapter);


        SpringEffect.doEffectSticky(findViewById(R.id.shiping_im), new Runnable() {
            @Override
            public void run() {

                // 录制
                MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                        .fullScreen(true)
                        .recordTimeMax(20000)
                        .recordTimeMin(1500)
                        .maxFrameRate(20)
                        .videoBitrate(600000)
                        .captureThumbnailsTime(1)
                        .build();

                MediaRecorderActivity.goSmallVideoRecorder(DaKaActivity.this, DaKaActivity.class.getName(), config);


            }
        });
        SpringEffect.doEffectSticky(findViewById(R.id.yulan), new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(DaKaActivity.this,VideoActivity.class).putExtra("url",dataSynEvent.getVideo_uri()));

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DataSynEvent event) {
        Log.d(TAG, event.toString());
        dataSynEvent=event;
        Glide.with(DaKaActivity.this)
                .load(event.getVideo_screenshot())
                //  .skipMemoryCache(true)
                //  .diskCacheStrategy(DiskCacheStrategy.NONE)
                //  .transform(new GlideCircleTransform(DengJiActivity.this,2, Color.parseColor("#ffffffff")))
                .into(shiping_im);
        shiping_im.setPadding(20, 20, 20, 20);
        yulan.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(DaKaActivity.this)) {
            EventBus.getDefault().unregister(this);//解除订阅
            EventBus.getDefault().post(new MainBean(true));
            Log.d(TAG, "解除订阅");
        }


        super.onDestroy();
    }

    @Override
    public void BackId(View view) {

        SpringEffect.doEffectSticky(view, new Runnable() {
            @Override
            public void run() {

                new AlertDialog.Builder(DaKaActivity.this).setItems(
                        new String[]{"拍摄照片", "从相册选择"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        String fn = System.currentTimeMillis() + "a.jpg";
                                        FileUtil.isExists(FileUtil.PATH, fn);
                                        mSavePhotoFile = new File(FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator + fn);

                                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        Uri photoUri = FileProvider.getUriForFile(
                                                DaKaActivity.this,
                                                getPackageName() + ".fileprovider",
                                                mSavePhotoFile);
                                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                        startActivityForResult(takePhotoIntent, 333);

                                        break;
                                    case 1:
                                        photoFromAlbum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();

            }
        });

    }

    private void photoFromAlbum() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 222);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 222) {

            try {
                handleImageOnKitkat(data);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (resultCode == Activity.RESULT_OK && requestCode == 333) {
            try {

                Luban.with(this)
                        .load(mSavePhotoFile)   // 传人要压缩的图片列表
                        .ignoreBy(100)   // 忽略不压缩图片的大小
                        .setTargetDir(FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator)  // 设置压缩后文件存储位置
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {
                                //Log.d("BaoZhangDengJiActivity", "开始压缩");
                            }

                            @Override
                            public void onSuccess(File file) {
                                Log.d("BaoZhangDengJiActivity", "删除:" + mSavePhotoFile.delete());
                                //  Log.d("BaoZhangDengJiActivity", "file.length():" + file.length()+"  "+file.getAbsolutePath());
                                stringList.add(0, file.getAbsolutePath());
                                zhaoPianAdapter.notifyDataSetChanged();

//                                // 选择本地视频压缩
//                                LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
//                                final LocalMediaConfig config = buidler
//                                        .setVideoPath(path)
//                                        .captureThumbnailsTime(1)
//                                        .doH264Compress(new AutoVBRMode())
//                                        .setFramerate(15)
//                                        .setScale(1.0f)
//                                        .build();
//                                OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
                            }

                            @Override
                            public void onError(Throwable e) {

                                showMSG("压缩图片出现错误", 4);
                            }
                        }).launch();    //启动压缩

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class ZhaoPianAdapter extends RecyclerView.Adapter<ZhaoPianAdapter.ViewHolder> {
        private List<String> datas;
        private ClickIntface2 clickIntface;

        public void setClickIntface(ClickIntface2 clickIntface) {
            this.clickIntface = clickIntface;
        }

        private ZhaoPianAdapter(List<String> datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tupian_item, viewGroup, false);
            return new ViewHolder(view);
        }


        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickIntface.BackId(viewHolder.itemView);
                }
            });
            if (datas.get(position).equals("pathOne")) {
                viewHolder.tupian.setImageResource(R.drawable.add_pic);
                viewHolder.tupian.setPadding(60, 60, 60, 60);
                viewHolder.shanchu.setVisibility(View.GONE);
            } else {
                Glide.with(DaKaActivity.this)
                        .load(datas.get(position))
                        //  .skipMemoryCache(true)
                        //  .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //  .transform(new GlideCircleTransform(DengJiActivity.this,2, Color.parseColor("#ffffffff")))
                        .into(viewHolder.tupian);
                viewHolder.tupian.setPadding(20, 20, 20, 20);
                viewHolder.shanchu.setVisibility(View.VISIBLE);
            }
            viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    stringList.remove(position);
                    zhaoPianAdapter.notifyDataSetChanged();

                }
            });
            // Log.d("ZhaoPianAdapter", "stringList.size():" + stringList.size());
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView tupian, shanchu;


            private ViewHolder(View view) {
                super(view);
                tupian = (ImageView) view.findViewById(R.id.tupian);
                shanchu = (ImageView) view.findViewById(R.id.shanchu);

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri
                    .getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri
                    .getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果不是document类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath); // 根据图片路径显示图片
        // System.err.println(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            try {

                Luban.with(this)
                        .load(imagePath)   // 传人要压缩的图片列表
                        .ignoreBy(100)   // 忽略不压缩图片的大小
                        .setTargetDir(FileUtil.SDPATH + File.separator + FileUtil.PATH + File.separator)  // 设置压缩后文件存储位置
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {
                                //Log.d("BaoZhangDengJiActivity", "相册图片开始压缩");
                            }

                            @Override
                            public void onSuccess(File file) {
                                // Log.d("BaoZhangDengJiActivity", "file.length():" + file.length()+"  "+file.getAbsolutePath());
                                stringList.add(0, file.getAbsolutePath());
                                zhaoPianAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {

                                showMSG("压缩图片出现错误", 4);
                            }
                        }).launch();    //启动压缩

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showMSG("没有得到图片", 4);
        }
    }


    private void showMSG(final String s, final int i) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast tastyToast = TastyToast.makeText(DaKaActivity.this, s, TastyToast.LENGTH_LONG, i);
                tastyToast.setGravity(Gravity.CENTER, 0, 0);
                tastyToast.show();

            }
        });
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
