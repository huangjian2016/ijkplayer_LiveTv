package com.example.huangjian.livetv_ijkplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.huangjian.livetv_ijkplayer.widget.MetroViewBorderImpl;

public class MainActivity extends Activity {
    private MetroViewBorderImpl mMetroViewBorderImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMetroViewBorderImpl = new MetroViewBorderImpl(this);
        mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);
        loadRecyclerViewMenuItem();
    }

    private  void loadRecyclerViewMenuItem(){
        RecyclerView recyclerView =(RecyclerView)findViewById(R.id.ry_menu_item);
        GridLayoutManager layoutManager = new GridLayoutManager(this ,1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        mMetroViewBorderImpl.attachTo(recyclerView);
        createOptionItemData(recyclerView,R.layout.detail_menu_item);
    }

    private void createOptionItemData(RecyclerView recyclerView,int id){
        OptionItemAdapter adapter = new OptionItemAdapter(this,id);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//  @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
