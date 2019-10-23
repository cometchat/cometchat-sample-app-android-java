package com.inscripts.cometchatpulse.demo.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Adapter.ContactListAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.SelectUserActivityContract;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Presenters.SelectUserActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;

import java.util.HashMap;
import java.util.Set;

public class SelectUserActivity extends AppCompatActivity implements SelectUserActivityContract.SelectUserActivityView {

    private Toolbar toolbar;

    private RecyclerView rvUserList;

    private LinearLayoutManager linearLayoutManager;

    private ContactListAdapter contactListAdapter;

    private SelectUserActivityContract.SelectUserActivityPresenter selectUserActivityPresenter;

    private String scope;

    private String guid;

    private HashMap<String,View> selectedUserView=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        selectUserActivityPresenter=new SelectUserActivityPresenter();
        selectUserActivityPresenter.attach(this);
        initView();
    }

    private void initView() {

        selectUserActivityPresenter.getIntent(getIntent());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Members");

        rvUserList=findViewById(R.id.rvUserList);
        linearLayoutManager= new LinearLayoutManager(this);
        rvUserList.setLayoutManager(linearLayoutManager);

        selectUserActivityPresenter.getUserList(30);


        rvUserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    selectUserActivityPresenter.getUserList(30);
                }

            }
        });

        rvUserList.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvUserList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int position) {
                User user= (User) var1.getTag(R.string.user);
                if (selectedUserView.containsKey(user.getUid())){
                    selectedUserView.get(user.getUid()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    selectedUserView.remove(user.getUid());
                }
                else {
                    selectedUserView.put(user.getUid(),var1);
                    selectedUserView.get(user.getUid()).setBackgroundColor(getResources().getColor(R.color.selection));
                }
            }

            @Override
            public void onLongClick(View var1, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_member,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.check:

                if (!selectedUserView.isEmpty()){
                 Set<String> keySet= selectedUserView.keySet();
                 selectUserActivityPresenter.addMemberToGroup(guid,SelectUserActivity.this,keySet);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setScope(String scope) {
           this.scope=scope;
    }

    @Override
    public void setGUID(String guid) {
          this.guid=guid;
    }

    @Override
    public void setContactAdapter(HashMap<String, User> userHashMap) {

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(userHashMap, this, R.layout.contact_list_item,true);
            rvUserList.setAdapter(contactListAdapter);
        } else {
            if (userHashMap != null && userHashMap.size() == 0) {
                contactListAdapter.refreshData(userHashMap);
            }
        }

    }
}