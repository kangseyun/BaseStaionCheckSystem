package org.androidtown.basestationchecksystem.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;

import org.androidtown.basestationchecksystem.Adapter.DispatchAdapter;
import org.androidtown.basestationchecksystem.Model.DispatchData;
import org.androidtown.basestationchecksystem.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DispatchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DispatchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DispatchData> myDataset;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Realm realm;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_dispatch);
        fab = (FloatingActionButton) findViewById(R.id.fab_dispatch) ;
        fab.attachToRecyclerView(mRecyclerView);
        setRecyclerView();
        setAddPhoneNumber();
        setToolbar();
        realm = Realm.getDefaultInstance();
        getData();
    }

    private void setRecyclerView(){
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new DispatchAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DispatchAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("삭제");
                alertDialogBuilder
                        .setMessage("해당 번호를 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        final RealmResults<DispatchData> result = realm.where(DispatchData.class)
                                                .equalTo("text", myDataset.get(position).getText()).findAllAsync();
                                        realm.beginTransaction();
                                        result.deleteFirstFromRealm();
                                        realm.commitTransaction();
                                        myDataset.remove(position);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("기지국");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        RealmQuery<DispatchData> query = realm.where(DispatchData.class);

        RealmResults<DispatchData> result = query.findAll();

        for (DispatchData data:
                result) {
            myDataset.add(data);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void setAddPhoneNumber()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DispatchActivity.this);

                alert.setTitle("전화번호 입력");
                alert.setMessage("(ㅡ는 빼고 입력해주세요)");

                final EditText name = new EditText(DispatchActivity.this);
                alert.setView(name);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String username = name.getText().toString(); // 유효성 검사
                        if(isStringDouble(username))
                        {
                            myDataset.add(new DispatchData(username));
                            realm.beginTransaction();
                            DispatchData data = realm.createObject(DispatchData.class); // 새 객체 만들기
                            data.setText(username);
                            realm.commitTransaction();
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            new AlertDialog.Builder(DispatchActivity.this)
                                    .setTitle("알림")
                                    .setMessage("숫자만 입력해주세요.")
                                    .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int sumthin) {
                                        }
                                    })
                                    .show(); // 팝업창 보여줌
                        }
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });
    }

    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
