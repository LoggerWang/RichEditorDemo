package com.example.legend.richeditordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


import java.util.Map;


/**
 * desc:    富文本编辑页
 * author: legend
 * date: 2017/7/17.
 */

public class RichEditActivity extends Activity implements View.OnLayoutChangeListener {

    //content控件
    private RichEditor edit_addcontent;

    private RelativeLayout edit_bottom_linear;
    private HorizontalScrollView edit_bottom_scroll;
    //    private TextView edit_bottom_top;
    private View activityRootView;
    private ImageButton action_bold;
    private ImageButton action_italic;
    private ImageButton action_underline;
    private ImageButton action_align_left;
    private ImageButton action_align_center;
    private ImageButton action_align_right;
    private ImageButton action_ol;
    private ImageButton action_li;

    //编辑器是否获得了焦点
    private boolean isFocus = false;

    //富文本编辑返回的内容
    private String editContentHTML;
    //键盘
    private InputMethodManager inputMethodManager;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private ImageButton action_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_edit);
        initDatas();
        initViews();

    }

    void initDatas() {
        //键盘对象
        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    void initViews() {
        //content控件
        edit_addcontent = this.findViewById(R.id.edit_addcontent);
        edit_bottom_linear = this.findViewById(R.id.edit_bottom_linear);
        edit_bottom_scroll = this.findViewById(R.id.edit_bottom_scroll);
        activityRootView = findViewById(R.id.root_layout);

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        //初始化编辑器
        initRichEditor();

    }


    /**
     * 初始化富文本编辑器
     */
    private void initRichEditor() {
        edit_addcontent.setEditorFontColor(Color.BLACK);
        edit_addcontent.setPadding(10, 10, 10, 10);
        edit_addcontent.setPlaceholder("请输入正文");
        //添加script回调接口
        edit_addcontent.addJavascriptInterface(new JsInterface(), "NativeInterface");
        Log.i("TAG", ":设置回调接口");
        WebSettings settings = edit_addcontent.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        edit_addcontent.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {

            @Override
            public void onStateChangeListener(String text, Map<String, String> types) {
                isFocus = true;
                setStyleUI(types);
            }
        });

        //内容文本改变监听
        edit_addcontent.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                editContentHTML = text;
                Log.i("TAG", text);
            }
        });

        //有序列表
        action_ol = (ImageButton) findViewById(R.id.action_ol);
        action_ol.clearFocus();
        action_ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setOl();
                if (action_ol.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_ol_click).getConstantState())) {
                    action_ol.setImageResource(R.mipmap.edit_ol);
                } else {
                    action_ol.setImageResource(R.mipmap.edit_ol_click);
                    action_li.setImageResource(R.mipmap.edit_li);
                }
            }
        });

        //无序列表
        action_li = findViewById(R.id.action_li);
        action_li.clearFocus();
        action_li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setLi();
                if (action_li.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_li_click).getConstantState())) {
                    action_li.setImageResource(R.mipmap.edit_li);
                } else {
                    action_li.setImageResource(R.mipmap.edit_li_click);
                    action_ol.setImageResource(R.mipmap.edit_ol);
                }
            }
        });

        //粗体
        action_bold = findViewById(R.id.action_bold);
        action_bold.clearFocus();
        action_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setBold();
                if (action_bold.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_b_click).getConstantState())) {
                    action_bold.setImageResource(R.mipmap.edit_b);
                } else {
                    action_bold.setImageResource(R.mipmap.edit_b_click);
                }
            }
        });
        //斜体
        action_italic = findViewById(R.id.action_italic);
        action_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setItalic();
                if (action_italic.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_i_click).getConstantState())) {
                    action_italic.setImageResource(R.mipmap.edit_i);
                } else {
                    action_italic.setImageResource(R.mipmap.edit_i_click);
                }
            }
        });
        //下划线
        action_underline = findViewById(R.id.action_underline);
        action_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setUnderline();
                if (action_underline.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_u_click).getConstantState())) {
                    action_underline.setImageResource(R.mipmap.edit_u);
                } else {
                    action_underline.setImageResource(R.mipmap.edit_u_click);
                }
            }
        });
        //局左
        action_align_left = findViewById(R.id.action_align_left);
        action_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignLeft();
                if (action_align_left.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_left_click).getConstantState())) {
                    action_align_left.setImageResource(R.mipmap.edit_left);
                } else {
                    action_align_left.setImageResource(R.mipmap.edit_left_click);
                    action_align_center.setImageResource(R.mipmap.edit_center);
                    action_align_right.setImageResource(R.mipmap.edit_right);
                }
            }
        });
        //居中
        action_align_center = findViewById(R.id.action_align_center);
        action_align_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignCenter();
                if (action_align_center.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_center_click).getConstantState())) {
                    action_align_center.setImageResource(R.mipmap.edit_center);
                } else {
                    action_align_center.setImageResource(R.mipmap.edit_center_click);
                    action_align_left.setImageResource(R.mipmap.edit_left);
                    action_align_right.setImageResource(R.mipmap.edit_right);
                }
            }
        });
        //居右
        action_align_right = findViewById(R.id.action_align_right);
        action_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_addcontent.setAlignRight();
                if (action_align_right.getDrawable().getConstantState().equals(getResources().getDrawable(R.mipmap.edit_right_click).getConstantState())) {
                    action_align_right.setImageResource(R.mipmap.edit_right);
                } else {
                    action_align_right.setImageResource(R.mipmap.edit_right_click);
                    action_align_left.setImageResource(R.mipmap.edit_left);
                    action_align_center.setImageResource(R.mipmap.edit_center);
                }
            }
        });
        //收起软键盘
        action_close = findViewById(R.id.action_close);
        action_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        });
    }


    public void setStyleUI(Map<String, String> type) {
        //加粗
        if (type.get("BOLD") != null) {
            action_bold.setImageResource(R.mipmap.edit_b_click);
        } else {
            action_bold.setImageResource(R.mipmap.edit_b);
        }

        //斜体
        if (type.get("ITALIC") != null) {
            action_italic.setImageResource(R.mipmap.edit_i_click);
        } else {
            action_italic.setImageResource(R.mipmap.edit_i);
        }

        //下划线
        if (type.get("UNDERLINE") != null) {
            action_underline.setImageResource(R.mipmap.edit_u_click);
        } else {
            action_underline.setImageResource(R.mipmap.edit_u);
        }
        //居中对齐
        if (type.get("JUSTIFYCENTER") != null) {
            action_align_center.setImageResource(R.mipmap.edit_center_click);
        } else {
            action_align_center.setImageResource(R.mipmap.edit_center);
        }
        //两端对齐
        if (type.get("JUSTIFYFULL") != null) {
        } else {

        }
        //左对齐
        if (type.get("JUSTIFYLEFT") != null) {
            action_align_left.setImageResource(R.mipmap.edit_left_click);
        } else {
            action_align_left.setImageResource(R.mipmap.edit_left);
        }
        //右对齐
        if (type.get("JUSTIFYRIGHT") != null) {
            action_align_right.setImageResource(R.mipmap.edit_right_click);
        } else {
            action_align_right.setImageResource(R.mipmap.edit_right);
        }

        //有序列表
        if (type.get("ORDEREDLIST") != null) {
            action_li.setImageResource(R.mipmap.edit_li_click);
        } else {
            action_li.setImageResource(R.mipmap.edit_li);
        }
        //无序列表
        if (type.get("UNORDEREDLIST") != null) {
            action_ol.setImageResource(R.mipmap.edit_ol_click);
        } else {
            action_ol.setImageResource(R.mipmap.edit_ol);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (edit_addcontent != null) {
            edit_addcontent.pauseTimers();
            edit_addcontent.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        if (edit_addcontent != null) {
            edit_addcontent.resumeTimers();
            edit_addcontent.onResume();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (edit_addcontent != null) {
//            edit_addcontent.ond();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (edit_addcontent != null) {
//            edit_addcontent.onNewIntent(intent);
        }
    }


    /**
     * 布局改变回调方法，用来做底部控件的显示和隐藏
     *
     * @param v
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param oldLeft
     * @param oldTop
     * @param oldRight
     * @param oldBottom
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//            scrollview.scrollTo(0, edit_bottom_linear.getTop());// 改变滚动条的位置1
            edit_bottom_linear.setVisibility(View.VISIBLE);
            edit_bottom_scroll.setVisibility(View.VISIBLE);
//            edit_bottom_top.setVisibility(View.INVISIBLE);

//            Toast.makeText(this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "是否显示了1：" + (edit_bottom_linear.getVisibility() == View.VISIBLE));

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            edit_bottom_linear.setVisibility(View.GONE);
            edit_bottom_scroll.setVisibility(View.GONE);
//            edit_bottom_top.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "是否显示了2：" + (edit_bottom_linear.getVisibility() == View.VISIBLE));
        }

    }

    public class JsInterface {
    }
}
