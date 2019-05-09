package com.example.asus.ele_me;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.asus.ele_me.entity.RestaurantEntity;
import com.example.asus.ele_me.util.InjectView;
import com.example.asus.ele_me.util.Injector;

public class MainActivity extends TabActivity implements LeftMenuAdapter.onItemSelectedListener, ShopCartImp, ShopCartDialog.ShopCartDialogImp {
    private final static String TAG = "MainActivity";
    private RecyclerView leftMenu;//左侧菜单栏
    private RecyclerView rightMenu;//右侧菜单栏
    private TextView headerView;
    private LinearLayout headerLayout;//右侧菜单栏最上面的菜单
    private LinearLayout bottomLayout;
    private DishMenu headMenu;
    private LeftMenuAdapter leftAdapter;
    private RightDishAdapter rightAdapter;
    private ArrayList<DishMenu> dishMenuList;//数据源
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动
    private ShopCart shopCart;
    //    private FakeAddImageView fakeAddImageView;
    private ImageView shoppingCartView;
    private FrameLayout shopingCartLayout;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RelativeLayout mainLayout;
    private TabHost tabHost;

    @InjectView(R.id.iv_head_left)//
    private ImageView head_left;//
    @InjectView(R.id.linear_above_toHome)//
    private LinearLayout above_toHome;//
    //@InjectView(R.id.tv_common_above_head)//
    private TextView above_tittle;//
    private String restaurant_name;
    private List<RestaurantEntity> mlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        tabHost = getTabHost();
        tabHost.setup(getLocalActivityManager());
        above_tittle=(TextView)findViewById(R.id.tv_common_above_head);
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec pages1 = tabHost.newTabSpec("tab1").setIndicator("点餐").setContent(R.id.tab1);
        tabHost.addTab(pages1);
        TabHost.TabSpec pages2 = tabHost.newTabSpec("tab2").setIndicator("评价").setContent(R.id.tab2);
        tabHost.addTab(pages2);
        TabHost.TabSpec pages3 = tabHost.newTabSpec("tab3").setIndicator("更多...").setContent(R.id.tab3);
        tabHost.addTab(pages3);
        Button btn=(Button)findViewById(R.id.btn_sum);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Pay.class);
                i.putExtra("price","￥ " + shopCart.getShoppingTotalPrice());
                startActivity(i);
            }
        });
        initData();
        initView();
        initAdapter();
        Injector.get(this).inject();// init views
        setListener();//
        Intent intent = getIntent();
        restaurant_name = intent.getStringExtra("name");
        above_tittle.setText(restaurant_name);
    }

    private void initView() {
        //above_tittle.setText(restaurant_name);//

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        leftMenu = (RecyclerView) findViewById(R.id.left_menu);
        rightMenu = (RecyclerView) findViewById(R.id.right_menu);
        headerView = (TextView) findViewById(R.id.right_menu_tv);
        headerLayout = (LinearLayout) findViewById(R.id.right_menu_item);
//        fakeAddImageView = (FakeAddImageView)findViewById(R.id.right_dish_fake_add);
        bottomLayout = (LinearLayout) findViewById(R.id.shopping_cart_bottom);
        shoppingCartView = (ImageView) findViewById(R.id.shopping_cart);
        shopingCartLayout = (FrameLayout) findViewById(R.id.shopping_cart_layout);
        totalPriceTextView = (TextView) findViewById(R.id.shopping_cart_total_tv);
        totalPriceNumTextView = (TextView) findViewById(R.id.shopping_cart_total_num);

        leftMenu.setLayoutManager(new LinearLayoutManager(this));
        rightMenu.setLayoutManager(new LinearLayoutManager(this));

        rightMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(1) == false) {//无法下滑
                    showHeadView();
                    return;
                }
                View underView = null;
                if (dy > 0)
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), headerLayout.getMeasuredHeight() + 1);
                else
                    underView = rightMenu.findChildViewUnder(headerLayout.getX(), 0);
                if (underView != null && underView.getContentDescription() != null) {
                    int position = Integer.parseInt(underView.getContentDescription().toString());
                    DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position);

                    if (leftClickType || !menu.getMenuName().equals(headMenu.getMenuName())) {
                        if (dy > 0 && headerLayout.getTranslationY() <= 1 && headerLayout.getTranslationY() >= -1 * headerLayout.getMeasuredHeight() * 4 / 5 && !leftClickType) {// underView.getTop()>9
                            int dealtY = underView.getTop() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else if (dy < 0 && headerLayout.getTranslationY() <= 0 && !leftClickType) {
                            headerView.setText(menu.getMenuName());
                            int dealtY = underView.getBottom() - headerLayout.getMeasuredHeight();
                            headerLayout.setTranslationY(dealtY);
//                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else {
                            headerLayout.setTranslationY(0);
                            headMenu = menu;
                            headerView.setText(headMenu.getMenuName());
                            for (int i = 0; i < dishMenuList.size(); i++) {
                                if (dishMenuList.get(i) == headMenu) {
                                    leftAdapter.setSelectedNum(i);
                                    break;
                                }
                            }
                            if (leftClickType) leftClickType = false;
                            Log.e(TAG, "onScrolled: " + menu.getMenuName());
                        }
                    }
                }
            }
        });

        shopingCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCart(view);
            }
        });
    }

    private void initData() {
        shopCart = new ShopCart();
        dishMenuList = new ArrayList<>();
        ArrayList<Dish> dishs1 = new ArrayList<>();
        dishs1.add(new Dish("干拌面", 1.0, 10));
        dishs1.add(new Dish("汤面", 1.0, 10));
        dishs1.add(new Dish("炒面", 1.0, 10));
        dishs1.add(new Dish("蒸米粉", 1.0, 10));
        dishs1.add(new Dish("烤面", 1.0, 10));
        dishs1.add(new Dish("烩面", 1.0, 10));
        dishs1.add(new Dish("烤冷面", 1.0, 10));
        DishMenu food1 = new DishMenu("粉面", dishs1);

        ArrayList<Dish> dishs2 = new ArrayList<>();
        dishs2.add(new Dish("红烧排骨", 1.0, 10));
        dishs2.add(new Dish("盖浇饭", 1.0, 10));
        dishs2.add(new Dish("农家小炒肉", 1.0, 10));
        dishs2.add(new Dish("炒粿条", 1.0, 10));
        dishs2.add(new Dish("炒牛河", 1.0, 10));
        dishs2.add(new Dish("炒菜", 1.0, 10));
        DishMenu food2 = new DishMenu("盖饭", dishs2);

        ArrayList<Dish> dishs3 = new ArrayList<>();
        dishs3.add(new Dish("芝士", 1.0, 10));
        dishs3.add(new Dish("小虾卷", 1.0, 10));
        dishs3.add(new Dish("紫菜卷", 1.0, 10));
        dishs3.add(new Dish("粤菜", 1.0, 10));
        dishs3.add(new Dish("赣菜", 1.0, 10));
        dishs3.add(new Dish("蟹黄卷", 1.0, 10));
        DishMenu food3 = new DishMenu("寿司", dishs3);

        ArrayList<Dish> dishs4 = new ArrayList<>();
        dishs4.add(new Dish("珠江", 1.0, 10));
        dishs4.add(new Dish("青岛", 1.0, 10));
        dishs4.add(new Dish("百威", 1.0, 10));
        dishs4.add(new Dish("茅台", 1.0, 10));
        dishs4.add(new Dish("葡萄酒", 1.0, 10));
        dishs4.add(new Dish("可乐", 1.0, 10));
        DishMenu food4 = new DishMenu("酒水", dishs4);

        ArrayList<Dish> dishs5 = new ArrayList<>();
        dishs5.add(new Dish("拌粉丝", 1.0, 10));
        dishs5.add(new Dish("大拌菜", 1.0, 10));
        dishs5.add(new Dish("菠菜", 1.0, 10));
        dishs5.add(new Dish("凉拌", 1.0, 10));
        dishs5.add(new Dish("青瓜", 1.0, 10));
        dishs5.add(new Dish("土豆丝", 1.0, 10));
        DishMenu food5 = new DishMenu("凉菜", dishs5);

        ArrayList<Dish> dishs6 = new ArrayList<>();
        dishs6.add(new Dish("小食组", 1.0, 10));
        dishs6.add(new Dish("薯条", 1.0, 10));
        dishs6.add(new Dish("辣条", 1.0, 10));
        dishs6.add(new Dish("面包", 1.0, 10));
        DishMenu food6 = new DishMenu("小吃", dishs6);

        ArrayList<Dish> dishs7 = new ArrayList<>();
        dishs7.add(new Dish("小米粥", 1.0, 10));
        dishs7.add(new Dish("大米粥", 1.0, 10));
        dishs7.add(new Dish("玉米粥", 1.0, 10));
        dishs7.add(new Dish("紫米粥", 1.0, 10));
        DishMenu food7 = new DishMenu("粥", dishs7);

        ArrayList<Dish> dishs8 = new ArrayList<>();
        dishs8.add(new Dish("海带汤", 1.0, 10));
        dishs8.add(new Dish("冬瓜汤", 1.0, 10));
        dishs8.add(new Dish("紫菜汤", 1.0, 10));
        dishs8.add(new Dish("玉米萝卜汤", 1.0, 10));
        dishs8.add(new Dish("鲜鱼汤", 1.0, 10));
        dishs8.add(new Dish("蘑菇汤", 1.0, 10));
        dishs8.add(new Dish("冬菇汤", 1.0, 10));
        dishs8.add(new Dish("蛋汤", 1.0, 10));
        DishMenu food8 = new DishMenu("汤", dishs8);

        dishMenuList.add(food1);
        dishMenuList.add(food2);
        dishMenuList.add(food3);
        dishMenuList.add(food4);
        dishMenuList.add(food5);
        dishMenuList.add(food6);
        dishMenuList.add(food7);
        dishMenuList.add(food8);
    }

    private void initAdapter() {
        leftAdapter = new LeftMenuAdapter(this, dishMenuList);
        rightAdapter = new RightDishAdapter(this, dishMenuList, shopCart);
        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartImp(this);
        initHeadView();
    }

    private void initHeadView() {
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        headerLayout.setContentDescription("0");
        headerView.setText(headMenu.getMenuName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leftAdapter.removeItemSelectedListener(this);
    }

    private void showHeadView() {
        headerLayout.setTranslationY(0);
        View underView = rightMenu.findChildViewUnder(headerView.getX(), 0);
        if (underView != null && underView.getContentDescription() != null) {
            int position = Integer.parseInt(underView.getContentDescription().toString());
            DishMenu menu = rightAdapter.getMenuOfMenuByPosition(position + 1);
            headMenu = menu;
            headerView.setText(headMenu.getMenuName());
            for (int i = 0; i < dishMenuList.size(); i++) {
                if (dishMenuList.get(i) == headMenu) {
                    leftAdapter.setSelectedNum(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onLeftItemSelected(int position, DishMenu menu) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += dishMenuList.get(i).getDishList().size() + 1;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(sum, 0);
        leftClickType = true;
    }

    @Override
    public void add(View view, int position) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCartView.getLocationInWindow(cartLocation);
        rightMenu.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1] - recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1] - recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(this);
        mainLayout.addView(fakeAddImageView);
        fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
        fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fakeAddImageView.setVisibility(View.GONE);
                mainLayout.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCartView, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCartView, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();

        showTotalPrice();
    }

    @Override
    public void remove(View view, int position) {
        showTotalPrice();
    }

    private void showTotalPrice() {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("￥ " + shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText("" + shopCart.getShoppingAccount());

        } else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
        }
    }

    private void showCart(View view) {
        if (shopCart != null && shopCart.getShoppingAccount() > 0) {
            ShopCartDialog dialog = new ShopCartDialog(this, shopCart, R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
    }

    @Override
    public void dialogDismiss() {
        showTotalPrice();
        rightAdapter.notifyDataSetChanged();
    }
    private void setListener() {
        // TODO Auto-generated method stub
        above_toHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });
    }//
}
