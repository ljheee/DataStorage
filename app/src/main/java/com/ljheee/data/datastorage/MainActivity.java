package com.ljheee.data.datastorage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 数据存储
 * 5中方式存储
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "data.dat";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_PASS = "pass";
    private static final String KEY_CHECKED = "checked";
    private static final String KEY_IS_LODIN = "isLogin";
    private static final String KEY_IS_INIT = "isInit";

    TextView textViewShow;

    EditText editTextAccount;
    EditText editTextPass;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewShow = (TextView) findViewById(R.id.textView_show);


        editTextAccount = (EditText) findViewById(R.id.editText_account);
        editTextPass = (EditText) findViewById(R.id.editText_pass);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        boolean isLogin =  PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_IS_LODIN,false);

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_IS_INIT,true)){
            //执行初始化：第一个启动引导界面

            //修改状态
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit().putBoolean(KEY_IS_INIT,false)
                    .commit();
        }

    }

    /**
     * 活动恢复
     * 读取之前的设置
     */
    @Override
    protected void onResume() {
        super.onResume();
        //获得选项
        String account = getPreferences(MODE_PRIVATE).getString(KEY_ACCOUNT,"");
        String pass = getPreferences(MODE_PRIVATE).getString(KEY_PASS,"");
        boolean isCheck = getPreferences(MODE_PRIVATE).getBoolean(KEY_CHECKED, false);

        editTextAccount.setText(account);
        editTextPass.setText(pass);
        checkBox.setChecked(isCheck);
    }

    /**
     * 活动暂停
     * 保存用户操作
     */
    @Override
    protected void onPause() {
        super.onPause();
        //数据存储--采用选项存储

//        SharedPreferences sp = getSharedPreferences("",MODE_PRIVATE);
        SharedPreferences sp;
        sp = getPreferences(MODE_PRIVATE);//方案二：不指定文件名，默认和活动一样

        sp.edit().putString(KEY_ACCOUNT,editTextAccount.getText().toString())
                 .putString(KEY_PASS,editTextPass.getText().toString())
                 .putBoolean(KEY_CHECKED,checkBox.isChecked())
                 .commit();//一定要提交

    }

    /**
     * 存储数据--采用内部存储
     * @param view
     */
    public void save(View view){
        OutputStream out = null;
        ObjectOutputStream oos = null;
        try {
            out = openFileOutput(FILE_NAME,MODE_PRIVATE);//模式：覆盖
            oos = new ObjectOutputStream(out);

            //可以写任意格式：图片、多媒体
            oos.writeUTF("string：内部存储，很灵活");//写字符串用writeUTF

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(oos != null)   oos.close();
                if(out != null)   out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


    public void load(View view ){

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(openFileInput(FILE_NAME));

            String str = in.readUTF();
            textViewShow.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
