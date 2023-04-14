package com.example.directory_20110501002;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.directory_20110501002.nonse.MyReciver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String[] name= {"解湛舰", "仲淞金", "柳洋恋", "黎励杏", "沈革纪",
                    "姚北鉴", "伍泓勃", "郦琪珑", "翟想振", "滕昀昀",
                    "卫哲雁", "常良雯", "缪鹭瑛", "奚纯霁", "顾习沙",
                    "赵泓静", "崔泉童", "洪骏胡", "汤娥昕", "杜婧娣",
                    "罗言宁", "徐焰忱", "尚傲印", "蒙总黎", "翟韵虹",
                    "武谦子", "钟战竹", "胡意泓", "左舰麟", "丁淑玮",
                    "晏登彭", "尹旦彭", "黄雯晴", "怀桃冰", "贺钧歆",
                    "郁嫣滢", "曹励椒", "凌同弛", "王洁剑", "张峥力",
                    "翁唯芊", "方秋笛", "滕娜曼", "柳轶彦", "韦余枫",
                    "张琴知", "包微俭", "昌瑶华", "倪俐凝", "冯轲跃","阿宝",
                    "Thomasina","Constant","Song-Thrush","Oscar",
            "Gardener","Lilly","Industrious","Una","Warrior","Dudley","Eilly"};
    String[] phone = {"13369741607" ,"17632290765","15930970391","18656438104" ,"13727222573" ,"19929207794" ,"15946094837" ,"18514852161" ,"15869354812" ,"16570228658" ,"13022052164" ,"15710106759" ,"14521637689" ,"15329012895" ,"18570184926" ,"15089267935" ,"15779662839" ,"15846507673" ,"13032179176" ,"17592803669" ,"18828984518" ,"18295591165" ,"17567591647" ,"15332474695" ,"13317489462" ,"17548153842" ,"13652676170" ,"18693325304" ,"13595706680" ,"18991609428" ,"13534021475" ,"17845564720" ,"17882294698" ,"15574649638" ,"15842292431" ,"18821497903" ,"13636477313" ,"18752727372" ,"13174248456" ,"19985667133" ,"13690906771" ,"17344139343" ,"15321281090" ,"18632719577" ,"17623089752" ,"13595013471" ,"18450548561" ,"15734868602" ,"14568881128" ,"15028577535" ,"13901178805" ,"18714228545" ,"17733589699" ,"13220709520" ,"16513783015" ,"19926459530" ,"15735481688" ,"13641508567" ,"18724435703" ,"19925933870" ,"13574709110" ,"16561351238"};
    int[] image={R.drawable.basketball,R.drawable.pants,R.drawable.midhead,R.drawable.ji,R.drawable.dachang,R.drawable.littlefat,R.drawable.daoshi};

    String[] address={"暂未填写","翻斗花园","沈阳大街","花果山","住山 王里"};
    String[] sex={"男","女","不详"};
    ListView listView;
    Button add;
    SortAdapter sortAdapter;
    private SideBar sideBar;
    private Context context;

    public Context getContext() {
        return context;
    }

    List<Contact> mlist =new ArrayList<>();
    List<Contact> mlist2 =new ArrayList<>();
//    private MyHander myHander=new MyHander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_recyclerview);

        listView= findViewById(R.id.recycler_view);
        sideBar = findViewById(R.id.side_bar);
        add=findViewById(R.id.add);
        // 构造一些数据
        initData();
        add.setOnClickListener(view -> {
            Intent intent =new Intent(MainActivity.this,Add_contact.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact= mlist2.get(i);

                Intent intent =new Intent(MainActivity.this,Contact_message.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("contact", (Serializable) contact);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mlist2.size(); i++) {
//                    if (mlist.get(i).getName() == "新的朋友" || mlist.get(i).getName() == "群聊" ||
//                            mlist.get(i).getName() == "标签" || mlist.get(i).getName() == "公众号"  )
//                        continue;
                    if (selectStr.equalsIgnoreCase(mlist2.get(i).getFirstLetter())) {
//                        listView.setSelection(i);// 选择到首字母出现的位置
                        int finalI = i;
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.smoothScrollToPosition(finalI);
                            }
                        });
                        return;
                    }
                }
            }
        });

    }
    @SuppressLint("Range")
    private void initData() {
        PublicVariable variable = new PublicVariable();
        //名字要提取出来在添加到list中，因为要进行字母排序

        MySQLiteHelper mySQLiteHelper= new MySQLiteHelper(this);
        SQLiteDatabase db=mySQLiteHelper.getReadableDatabase();
        Random random = new Random();
//        for (int i = 0; i < name.length; i++) {
//            mlist.add(new Contact(null,name[i],phone[i],image[random.nextInt(7)],address[random.nextInt(5)],sex[random.nextInt(3)]));
//            ContentValues values;
//            values = new ContentValues();
//            values.put("name",mlist.get(i).getName());
//            values.put("image",mlist.get(i).getImage());
//            values.put("phone",mlist.get(i).getPhone());
//            values.put("sex",mlist.get(i).getSex());
//            values.put("address",mlist.get(i).getAddress());
//            db.insert("contacts",null,values);
//        }
        Cursor cursor = db.query("contacts",null,null,null,null,null,null);
        while (cursor.moveToNext()) {
            Integer id;
            String phone;
            String name;
            String address;
            Integer image;
            String sex;
            id=cursor.getInt(cursor.getColumnIndex("_id"));

            image=cursor.getInt(cursor.getColumnIndex("image"));
            name=cursor.getString(cursor.getColumnIndex("name"));
            phone = cursor.getString(cursor.getColumnIndex("phone"));
            address=cursor.getString(cursor.getColumnIndex("address"));
            sex=cursor.getString(cursor.getColumnIndex("sex"));
            Contact contact =new Contact(id,name,phone,image,address,sex);
            contact.setId(id);
            mlist2.add(contact);
        }

        db.close();
        Collections.sort(mlist2); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法

        sortAdapter = new SortAdapter(MainActivity.this,mlist2);
        listView.setAdapter(sortAdapter);
    }


    @SuppressLint("Range")
    @Override
    protected void onResume() {
        super.onResume();
        
        if (PublicVariable.deleteID!=null){
            int m = -1;
            for(int i=0;i<mlist2.size();i++){
                if(Objects.equals(mlist2.get(i).getId(), PublicVariable.deleteID)){
                    m=i;
                    break;
                }
            }

            if(m!=-1)  mlist2.remove(m);
            sortAdapter.notifyDataSetChanged();
            PublicVariable.deleteID=null;
        }

        if(PublicVariable.add_sign==0){


            String up_name = PublicVariable.up_name;
            String up_phone = PublicVariable.up_phone;
            String up_address = PublicVariable.up_address;
            Integer up_image = PublicVariable.up_image;
            String up_sex = PublicVariable.up_sex;
            Contact contact =new Contact(null,up_name,up_phone,up_image,up_address,up_sex);
            MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
            SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("image",R.drawable.btn1);
            values.put("name", String.valueOf(contact.getName()));
            values.put("phone", String.valueOf(contact.getPhone()));
            values.put("address", String.valueOf(contact.getAddress()));
            values.put("sex", String.valueOf(contact.getSex()));


            db.insert("contacts", null, values);
            Cursor cursor = db.query("contacts", null, "name=? and phone=? and address=? and sex=?",
                    new String[]{String.valueOf(contact.getName()), String.valueOf(contact.getPhone()),
                            String.valueOf(contact.getAddress()), String.valueOf(contact.getSex())},
                    null, null, null);

            cursor.moveToNext();
            int id = cursor.getColumnIndex("_id");
            Integer id1 =cursor.getInt(id);
            contact.setId(id1);

            cursor.close();
            db.close();
            mlist2.add(contact);
            Collections.sort(mlist2);
            sortAdapter.notifyDataSetChanged();

            PublicVariable.add_sign=-1;
            PublicVariable.up_image= null;
            PublicVariable.up_name=null;
            PublicVariable.up_phone=null;
            PublicVariable.up_address=null;
            PublicVariable.up_sex=null;
        }

        if(PublicVariable.up_id!=null){
            MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
            SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

//            values.put("id",PublicVariable.up_id);
            values.put("image",PublicVariable.up_image);
            values.put("name", String.valueOf(PublicVariable.up_name));
            values.put("phone", String.valueOf(PublicVariable.up_phone));
            values.put("address", String.valueOf(PublicVariable.up_address));
            values.put("sex", String.valueOf(PublicVariable.up_sex));


            db.update("contacts", values, "_id=?", new String[]{String.valueOf(PublicVariable.up_id)});
            db.close();

            Contact contact =new Contact(
                    PublicVariable.up_id,
                    PublicVariable.up_name,PublicVariable.up_phone,
                    PublicVariable.up_image,PublicVariable.up_address,
                    PublicVariable.up_sex);

//            contact.setId(PublicVariable.up_id);

            for(int i=0;i<mlist2.size();i++){
                if (Objects.equals(mlist2.get(i).getId(), PublicVariable.up_id)) {
                    mlist2.set(i,contact);
                    break;
                }
            }
            Collections.sort(mlist2);
            sortAdapter.notifyDataSetChanged();

            Toast.makeText(this, "联系人信息已更新", Toast.LENGTH_SHORT).show();

            PublicVariable.up_id=null;
            PublicVariable.up_image= null;
            PublicVariable.up_name=null;
            PublicVariable.up_phone=null;
            PublicVariable.up_address=null;
            PublicVariable.up_sex=null;

        }

        
    }
}