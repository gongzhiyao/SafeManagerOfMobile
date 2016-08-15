package com.example.gongzhiyao.safemanagerofmobile.SecurityPhone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongzhiyao.safemanagerofmobile.Adapter.BlackContactAdapter;
import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackContactInfo;
import com.example.gongzhiyao.safemanagerofmobile.BlackContact.BlackNumberDBOperation;
import com.example.gongzhiyao.safemanagerofmobile.Log.L;
import com.example.gongzhiyao.safemanagerofmobile.R;

import java.util.ArrayList;
import java.util.List;

public class SecurityPhone extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mHaveBlackNum;
    private FrameLayout mNoBlackNum;
    private BlackNumberDBOperation operation;
    private ListView mListview;
    private int pagenumber = 0;
    private int pagesize = 15;//一页十五个
    private int totalNumbe;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;
    private L log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_security_phone);
        initView();
        fillData();
    }


    /**
     * 用于填充数据，重新刷新界面
     */
    private void fillData() {

        /**
         * fillData中的部分代码应该舍去，与OnResume的冗余度太高
         */


        operation = new BlackNumberDBOperation(this);

        totalNumbe = operation.getTotalNum();
        /***
         * 在刚进入界面时，就要查看黑名单中是都有数据，有就显示，没有就隐藏
         */
        if (totalNumbe == 0) {
            mHaveBlackNum.setVisibility(View.GONE);
            mNoBlackNum.setVisibility(View.VISIBLE);

        } else if (totalNumbe > 0) {
            /**含有黑名单
             * 在数据库中
             */
            mHaveBlackNum.setVisibility(View.VISIBLE);
            mNoBlackNum.setVisibility(View.GONE);
            pagenumber = 0;
            if (pageBlackNumber.size() > 0) {
                pageBlackNumber.clear();
                log.d("清空了一次");
            }
            pageBlackNumber.addAll(operation.getPageBlackNumber(pagenumber, pagesize));
            if (adapter == null) {
                adapter = new BlackContactAdapter(pageBlackNumber, this);
                adapter.setCallBack(new BlackContactAdapter.BlackContactCallBack() {
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });

                mListview.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        log = new L();
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ImageView mleft = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
        mleft.setOnClickListener(this);
        mleft.setImageResource(R.drawable.back);
        mHaveBlackNum = (FrameLayout) findViewById(R.id.fl_haveblacknumber);
        mNoBlackNum = (FrameLayout) findViewById(R.id.fl_noblacknumber);
        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
        mListview = (ListView) findViewById(R.id.lv_blacknumbers);
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    /**没有滑动的状态**/
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //获取最后一个可见条目
                        int lastVisiblePosition = mListview.getLastVisiblePosition();
                        //如果当前条目是最后一个，则查询更多的数据
                        if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                            /**
                             * 这样有一个错误，就是剩余的部分黑名单不够一页，可能不能够显示出来
                             */
                            pagenumber++;
                            if (pagenumber * pagesize >= totalNumbe) {
                                Toast.makeText(getApplicationContext(), "黑名单中只有这些！", Toast.LENGTH_SHORT).show();
                            } else {
                                pageBlackNumber.addAll(operation.getPageBlackNumber(pagenumber, pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_addblacknumber:
                startActivity(new Intent(this, AddBlackNumber.class));
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 在选择完黑名单之后，黑名单数量发生变化，这时需要更新列表
         */
        log.d("onResume执行");
        if (totalNumbe != operation.getTotalNum()) {
            if (operation.getTotalNum() > 0) {
                mHaveBlackNum.setVisibility(View.VISIBLE);
                mNoBlackNum.setVisibility(View.GONE);

            } else {
                mHaveBlackNum.setVisibility(View.GONE);
                mNoBlackNum.setVisibility(View.VISIBLE);
            }
            /**在resume的时候list列表还原得到最顶端**/
            pagenumber = 0;
            pageBlackNumber.clear();
            log.d("清空了一次");
            pageBlackNumber.addAll(operation.getPageBlackNumber(pagenumber, pagesize));
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        }
    }
}

