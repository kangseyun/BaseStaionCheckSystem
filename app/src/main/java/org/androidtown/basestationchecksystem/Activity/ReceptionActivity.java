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
import android.widget.Button;
import android.widget.EditText;

import com.melnykov.fab.FloatingActionButton;

import org.androidtown.basestationchecksystem.Adapter.ReceptionAdapter;
import org.androidtown.basestationchecksystem.ContactParser;
import org.androidtown.basestationchecksystem.Model.DispatchData;
import org.androidtown.basestationchecksystem.Model.ReceptionData;
import org.androidtown.basestationchecksystem.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ReceptionActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ReceptionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ReceptionData> myDataset;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Realm realm;
    final Context context = this;
    private ContactParser contactPaser;
    private Button sync, delete;
    private List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reception_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_reception);
        fab = (FloatingActionButton) findViewById(R.id.fab_reception) ;
        fab.attachToRecyclerView(mRecyclerView);
        sync = (Button) findViewById(R.id.reception_sync);
        sync.setOnClickListener(this);


        setRecyclerView();
        setAddPhoneNumber();
        setToolbar();
        realm = Realm.getDefaultInstance();
        getData();
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
        RealmQuery<ReceptionData> query = realm.where(ReceptionData.class);

        RealmResults<ReceptionData> result = query.findAll();

        for (ReceptionData data:
             result) {
            myDataset.add(data);
        }

        mAdapter.notifyDataSetChanged();
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
        mAdapter = new ReceptionAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ReceptionAdapter.ClickListener() {
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
                                        final ReceptionData result = realm.where(ReceptionData.class)
                                                .equalTo("text", myDataset.get(position).getText()).findAllAsync().first();
                                        realm.beginTransaction();
                                        result.deleteFromRealm();
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

    private void setAddPhoneNumber()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ReceptionActivity.this);

                alert.setTitle("전화번호 입력");
                alert.setMessage("(ㅡ는 빼고 입력해 주세요)");

                final EditText name = new EditText(ReceptionActivity.this);
                alert.setView(name);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String username = name.getText().toString(); // 유효성 검사
                        if(isStringDouble(username))
                        {
                            myDataset.add(new ReceptionData(username));
                            realm.beginTransaction();
                            ReceptionData data = realm.createObject(ReceptionData.class); // 새 객체 만들기
                            data.setText(username);
                            realm.commitTransaction();
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            new AlertDialog.Builder(ReceptionActivity.this)
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.reception_sync:
                contactPaser = new ContactParser(0);
                data = contactPaser.load();

                realm.beginTransaction();

                for (String obj: data) {
                    ReceptionData realmObj = realm.createObject(ReceptionData.class); // 새 객체 만들기
                    realmObj.setText(obj);
                }

                realm.commitTransaction();

                for (String result:
                        data) {
                    myDataset.add(new ReceptionData(result));
                }

                mAdapter.notifyDataSetChanged();
                break;
            case R.id.reception_delete:
                final RealmResults<ReceptionData> results = realm.where(ReceptionData.class).findAll();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });

                myDataset.clear();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}


