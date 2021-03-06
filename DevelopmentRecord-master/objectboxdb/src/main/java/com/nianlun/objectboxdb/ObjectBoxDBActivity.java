package com.nianlun.objectboxdb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nianlun.objectboxdb.adapter.UserAdapter;
import com.nianlun.objectboxdb.database.ObjectBox;
import com.nianlun.objectboxdb.entity.User;
import com.nianlun.objectboxdb.utils.NameUtils;
import com.nianlun.objectboxdb.utils.SnowflakeIdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.objectbox.Box;
import io.objectbox.BoxStore;
@Route(path = "/objectboxdb/ObjectBoxDBActivity")
public class ObjectBoxDBActivity extends AppCompatActivity implements View.OnClickListener {

    private BoxStore mBoxStore;

    private LinearLayout llTop;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnUpdate;
    private Button btnQuery;
    private LinearLayout llBottom;
    private RecyclerView rvUser;
    private SnowflakeIdGenerator mIdWorker;
    private List<User> mUserList;
    private UserAdapter mUserAdapter;
    private Box<User> mUserBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_box_db);
        initView();
        initUserBox();
        initUser();
    }

    private void initUserBox() {
        ObjectBox.init(this);

        mBoxStore = ObjectBox.get();

        mUserBox = mBoxStore.boxFor(User.class);
    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        rvUser = (RecyclerView) findViewById(R.id.rv_user);
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        llTop.setOnClickListener(this);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        btnQuery = (Button) findViewById(R.id.btn_query);
        btnQuery.setOnClickListener(this);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        llBottom.setOnClickListener(this);
        rvUser = (RecyclerView) findViewById(R.id.rv_user);
        rvUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            User user = new User();
            user.setUserId(String.valueOf(mIdWorker.nextId()));
            user.setUserName(NameUtils.createRandomZHName(new Random().nextInt(4) + 1));
            user.setAge(18 + new Random().nextInt(10));

            // ???????????????
            mUserBox.put(user);

            queryAllUser();

            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_delete) {
            User user = mUserList.get(mUserList.size() - 1);

            //??????????????????
            mUserBox.remove(user);

            queryAllUser();

            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_update) {
            User user = mUserList.get(mUserList.size() - 1);
            user.setUserName(NameUtils.createRandomZHName(new Random().nextInt(4) + 1));

            //??????????????????
            mUserBox.put(user);

            queryAllUser();

            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_query) {
            mUserAdapter.setNewData(new ArrayList<User>());

            btnQuery.postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryAllUser();
                }
            }, 1000);

            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryAllUser() {
        mUserList = mUserBox.query().build().find();
        mUserAdapter.setNewData(mUserList);
        rvUser.smoothScrollToPosition(mUserList.size() - 1);
    }

    private void initUser() {

        //??????ID?????????
        mIdWorker = new SnowflakeIdGenerator(0, 0);

        mUserBox.removeAll();

        mUserList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserId(String.valueOf(mIdWorker.nextId()));
            // ????????????????????????
            user.setUserName(NameUtils.createRandomZHName(random.nextInt(4) + 1));
            user.setAge(18 + random.nextInt(10));
            mUserList.add(user);
        }

        mUserAdapter = new UserAdapter(mUserList);
        rvUser.setAdapter(mUserAdapter);

        mUserBox.put(mUserList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBoxStore.close();
    }
}
