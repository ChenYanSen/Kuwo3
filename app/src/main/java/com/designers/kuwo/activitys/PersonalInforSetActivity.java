package com.designers.kuwo.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.UserBiz;
import com.designers.kuwo.biz.bizimpl.UserBizImpl;
import com.designers.kuwo.eneity.User;
import com.designers.kuwo.utils.CircularImage;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.StatusBarCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class PersonalInforSetActivity extends Activity {
    private TableRow personal_nike, personal_signature, hobby_tr, personal_sex, personal_area;
    private TextView personal_nike_name_edit, user_nickName_tv,account_show_tv;
    private TextView personal_name_signature_edit, signature_tv;
    private TextView personal_hobby_edit;
    private TextView personal_sex_set;
    private TextView personal_area_edit;
    private CircularImage avatar_image;
    private int sexindex = 0;
    private TextView mProvinceTextView, mDistinguishTextView, mCityTextView;
    private ImageView back;
    //头像路径

    private Uri avatarUri;
    private Bitmap bitMap;
    //省市级联
    private ArrayList<String> mProvinceList;
    private ArrayList<String> mCityList;
    private ArrayList<String> mDistinguishList;
    private JSONObject mJsonObject = null;
    private int mProvinceId = 0;// 省id
    private int mCityId = 0;// 市id
    private int mDistinguishId = 0;// 区id
    boolean city = false, distinguish = false;
    boolean firstselct=true;
    //照片选择和拍照
    private static final int NONE = 0;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int PHOTO_RESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private Intent intent;
    private String current_user;
    CustomApplication customApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_infor_set);
        this.mProvinceTextView = (TextView) findViewById(R.id.mProvinceTextView);
        this.account_show_tv=(TextView)findViewById(R.id.account_show_tv);
        this.mDistinguishTextView = (TextView) findViewById(R.id.mDistinguishTextView);
        this.mCityTextView = (TextView) findViewById(R.id.mCityTextView);
        this.avatar_image= (CircularImage) findViewById(R.id.avatar_image);
        this.personal_sex_set=(TextView)findViewById(R.id.personal_sex_edit);
        this.back= (ImageView) findViewById(R.id.back);
        this.personal_nike_name_edit=(TextView)findViewById(R.id.personal_nike_name_edit);
        this.personal_name_signature_edit=(TextView)findViewById(R.id.personal_name_signature_edit);
        this.signature_tv=(TextView)findViewById(R.id.signature_tv);
        this.user_nickName_tv=(TextView)findViewById(R.id.user_nickName_tv);
        this.personal_hobby_edit=(TextView)findViewById(R.id.personal_hobby_edit);
        customApplication =(CustomApplication)getApplication();
        current_user=customApplication.getUserName();
        this.account_show_tv.setText(current_user);
        Log.i("current_user-----------",current_user);
        //圆头像
         bitMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.login_image);
        UserBiz userBizs=new UserBizImpl();
        User users=userBizs.displayPersonalInfro(current_user,PersonalInforSetActivity.this);
        String strArea=users.getArea();
        //从数据库读取地区
        if(strArea!=null&&strArea.length()>3){
            String[] sourceStrArray = strArea.split(",");
                this.mProvinceTextView.setText(sourceStrArray[0]);
                this.mCityTextView.setText(sourceStrArray[1]);
                this.mDistinguishTextView.setText(sourceStrArray[2]);

        }
        //从数据库读取性别
        if(users.getSex()!=null){
            this.personal_sex_set.setText(users.getSex());
        }
        //从数据库读取昵称
        if(users.getNickname()!=null){
            this.personal_nike_name_edit.setText(users.getNickname());
            this.user_nickName_tv.setText(users.getNickname());
        }
        //从数据库读取个性签名
        if(users.getSignature()!=null){
            this.personal_name_signature_edit.setText(users.getSignature());
            this.signature_tv.setText(users.getSignature());
        }
        if(users.getHobby()!=null){
            this.personal_hobby_edit.setText(users.getHobby());
        }
        //从数据库读出头像地址
        String str=users.getAvatarUri();
        //Log.i("数据库获得地址------------",str);
        if(str!=null){
            Uri uri = Uri.parse((String)str);
            avatarUri=uri;
            try {

                Bitmap bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Log.i("bitmap----------", bitmaps.toString());
                this.avatar_image.setImageBitmap(bitmaps);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.avatar_image.setImageBitmap(bitMap);
        }
        if(avatarUri!=null){

            customApplication.setAvatarUri(avatarUri);
        }
       // this.avatar_image.setImageResource(R.drawable.login_image);
        this.personal_name_signature_edit = (TextView) findViewById(R.id.personal_name_signature_edit);
        this.signature_tv = (TextView) findViewById(R.id.signature_tv);
        this.personal_nike_name_edit = (TextView) findViewById(R.id.personal_nike_name_edit);
        this.user_nickName_tv = (TextView) findViewById(R.id.user_nickName_tv);
        this.personal_signature = (TableRow) findViewById(R.id.personal_signature);
        this.personal_nike = (TableRow) findViewById(R.id.personal_nike);
        this.hobby_tr = (TableRow) findViewById(R.id.hobby_tr);
        this.personal_hobby_edit = (TextView) findViewById(R.id.personal_hobby_edit);
        this.personal_sex = (TableRow) findViewById(R.id.personal_sex);
        this.personal_sex_set = (TextView) findViewById(R.id.personal_sex_edit);
        //昵称编辑点击事件
        this.personal_nike.setOnClickListener(x -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(PersonalInforSetActivity.this).inflate(R.layout.activity_personal_nake_set, null);
            final EditText personal_nake_input = (EditText) view.findViewById(R.id.personal_nake_input);
            final TextView personal_title_set = (TextView) view.findViewById(R.id.personal_title_set);
            personal_title_set.setText("昵称");

            builder.setView(view);
            builder.setPositiveButton("确定", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String str = personal_nake_input.getText().toString().trim();
                    personal_nike_name_edit.setText(str);
                    user_nickName_tv.setText(str);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();


        });
        //个性签名点击事件
        this.personal_signature.setOnClickListener(x -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(PersonalInforSetActivity.this).inflate(R.layout.activity_personal_nake_set, null);
            final EditText personal_nake_input = (EditText) view.findViewById(R.id.personal_nake_input);
            final TextView personal_title_set = (TextView) view.findViewById(R.id.personal_title_set);
            personal_title_set.setText("个性签名");
            builder.setView(view);
            builder.setPositiveButton("确定", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String str = personal_nake_input.getText().toString().trim();
                    personal_name_signature_edit.setText(str);
                    signature_tv.setText(str);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        });
        //个人爱好点击事件
        this.hobby_tr.setOnClickListener(x -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("爱好");
            final String[] hobbys = getResources().getStringArray(R.array.hobby);
            final StringBuilder sb = new StringBuilder();
            final boolean[] checkedItem = new boolean[hobbys.length];

            builder.setMultiChoiceItems(R.array.hobby, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedItem[which] = isChecked;
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < hobbys.length; i++) {
                        if (checkedItem[i]) {
                            sb.append(hobbys[i] + " ");
                        }
                    }
                    personal_hobby_edit.setText(sb.toString());
                    dialog.dismiss();
                }

            }).show();
        });
        //性别点击事件
        this.personal_sex.setOnClickListener(x -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("性别");
            final String[] hobbys = getResources().getStringArray(R.array.hobby);
            final StringBuilder sb = new StringBuilder();
            final boolean[] checkedItem = new boolean[hobbys.length];
            final String[] sex = new String[]{"男", "女", "保密"};

            builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sexindex = which;
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    personal_sex_set.setText(sex[sexindex]);
                    dialog.dismiss();
                }
            }).show();
        });
        //选择地区点击事件
        //选择省
        this.mProvinceTextView.setOnClickListener(x -> {
            DialogShow(getProvinceData(), 1, "选择省");

            Log.i("provence------------------", mProvinceTextView.getText().toString());
            //if(!("省").equals(mProvinceTextView.getText().toString())){
                city = true;
                distinguish = true;
            //}
        });
        //选择市
        String strs=mProvinceTextView.getText().toString();
        this.mCityTextView.setOnClickListener(x -> {
            if (city) {
                DialogShow(mCityList, 2, "选择市");
            }
        });
//选择地区
        this.mDistinguishTextView.setOnClickListener(x -> {
            if (distinguish) {
                DialogShow(mDistinguishList, 3, "选择地区");
            }
        });

        this.back.setOnClickListener(x -> {
            //往数据库中添加个人信息
            User user=new User();
            user.setNickname(personal_nike_name_edit.getText().toString().trim());

            UserBiz userBiz=new UserBizImpl();
            //判断是否获得avatarUri存头像
            //if(avatarUri.toString().length()!=0){
            if(null!=avatarUri){

                user.setAvatarUri(avatarUri.toString());
                Log.i("储存头像-------------------",avatarUri.toString());
            }else {
                user.setAvatarUri(Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://"
                                + PersonalInforSetActivity.this.getResources()
                                .getResourcePackageName(
                                        R.drawable.login_image)
                                + "/"
                                + PersonalInforSetActivity.this.getResources()
                                .getResourceTypeName(
                                        R.drawable.login_image)
                                + "/"
                                + PersonalInforSetActivity.this.getResources()
                                .getResourceEntryName(
                                        R.drawable.login_image))
                        .toString());
            }
            //储存地区
            String strear=mProvinceTextView.getText().toString()+","+mCityTextView.getText().toString()+","+mDistinguishTextView.getText().toString();
            user.setArea(strear);
            //添加昵称
            user.setNickname(personal_nike_name_edit.getText().toString());
            //添加性别
            user.setSex(personal_sex_set.getText().toString());
            //添加个性签名
            user.setSignature(personal_name_signature_edit.getText().toString());
            user.setHobby(personal_hobby_edit.getText().toString());
         boolean f= userBiz.AlterPersonalInfor(current_user,user,PersonalInforSetActivity.this);

            customApplication.setAvatarUri(avatarUri);
            //startActivity(new Intent(PersonalInforSetActivity.this, MainActivity.class));
            finish();
        });

        //头像选择点击事件
       /* this.avatar_image.setOnClickListener(x->{
            selectedSDImage();
        });*/
        this.avatar_image.setOnClickListener(new ViewOCL());

        StatusBarCompat.compat(this);
    }
    //数据装配省市
    /* 显示省市区的dialog */
    public void DialogShow(final ArrayList<String> mList, final int type, String str) {
        ActionSheetDialog dialog = new ActionSheetDialog(PersonalInforSetActivity.this)
                .builder().setTitle(str).setCancelable(false)
                .setCanceledOnTouchOutside(false);

        for (int i = 0; i < mList.size(); i++) {
            final String item = mList.get(i);
            final int position = i;
            dialog.addSheetItem(mList.get(i), ActionSheetDialog.SheetItemColor.Blue,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            if (type == 1) {
                                mProvinceTextView.setText(item);
                                Log.i("item========",item);
                                if("省".equals(mProvinceTextView.getText().toString())){

                                }
                                mProvinceId = position;
                                mCityList = getCityData();
                                mCityTextView.setText(mCityList.get(0));
                                mDistinguishList = getDistinguish();
                                mDistinguishTextView.setText(mDistinguishList.get(0));
                                mCityTextView.setEnabled(true);
                                    mDistinguishTextView.setEnabled(true);

                            } else if (type == 2) {
                                mCityTextView.setText(item);
                                mCityId = position;
                                mDistinguishList = getDistinguish();
                                mDistinguishTextView.setText(mDistinguishList
                                        .get(0));
                            } else {
                                mDistinguishId = position;
                                mDistinguishTextView.setText(item);
                            }
                        }
                    });

        }


        dialog.show();

    }
    //---------------
    /* 得到省 */
    private ArrayList<String> getProvinceData() {
        // TODO Auto-generated method stub
        InputStream inputStream = getResources()
                .openRawResource(R.raw.location);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String str = sb.toString();

        try {
            mJsonObject = new JSONObject(str);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ArrayList<String> mList = new ArrayList<String>();
        for (int i = 0; i < mJsonObject.length(); i++) {
            try {
                JSONObject provinceObject = mJsonObject.getJSONObject((i + 1)
                        + "");
                mList.add(provinceObject.getString("province_name"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mList;
    }
    /* 得到市 */
    private ArrayList<String> getCityData() {
        JSONObject cityObject = null;
        try {
            cityObject = mJsonObject.getJSONObject((mProvinceId + 1) + "")
                    .getJSONObject("city");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> mList = new ArrayList<String>();
        for (int i = 0; i < cityObject.length(); i++) {
            try {
                JSONObject cityJSONObject = cityObject.getJSONObject((i + 1)
                        + "");
                mList.add(cityJSONObject.getString("city_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }
    /* 得到区 */
    public ArrayList<String> getDistinguish() {
        JSONObject areaObject = null;
        try {
            areaObject = mJsonObject.getJSONObject((mProvinceId + 1) + "")
                    .getJSONObject("city").getJSONObject((mCityId + 1) + "")
                    .getJSONObject("area");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> mList = new ArrayList<String>();
        for (int i = 0; i < areaObject.length(); i++) {
            try {
                String cityString = areaObject.getString((i + 1) + "");
                mList.add(cityString.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    /**
     * 自定义方法：使用Intent调用系统提供的相册功能，使用startActivityForResult获取用户选择的照片图片
     * */
    private void selectedSDImage() {
        Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        intentAlbum.setType("image/*");
        startActivityForResult(intentAlbum, 200);
    }

    private class ViewOCL implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.avatar_image:
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            PersonalInforSetActivity.this);
                    final String[] types = { "拍照", "从相册中选择" };
                    builder.setItems(types, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                           // Intent intent;
                            switch (position) {
                                case 0:
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                            .fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    "temp.jpg")));
                                    startActivityForResult(intent, PHOTO_GRAPH);
                                    break;
                                case 1:
                                    selectimgge();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.show();
                    break;
                default:
                    break;
            }
        }
    }


private void selectimgge() {
    if (Build.VERSION.SDK_INT < 19){
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1411);
    } else {
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1411);
    }

}
    //---------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode !=RESULT_OK) { // 判断是否接收到正常数据
            return;
        }

        ContentResolver contentResolver =getContentResolver(); // 对象时当前应用程序，当前指令征用
        if (requestCode == 1411) {
            avatarUri= data.getData(); // 获取uri


            try {
                bitMap = MediaStore.Images.Media.getBitmap(contentResolver,
                        avatarUri); // 自动添加try catch
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.PNG, 50, bos);
            byte[] bytes = bos.toByteArray();
            this.avatar_image.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));

        }
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            avatarUri= Uri.fromFile(picture);
            // startPhotoZoom(Uri.fromFile(picture));


            try {
                bitMap = MediaStore.Images.Media.getBitmap(contentResolver,
                        avatarUri); // 自动添加try catch
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.avatar_image.setImageBitmap(bitMap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//------------
}
