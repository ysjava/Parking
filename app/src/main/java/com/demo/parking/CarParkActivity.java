package com.demo.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.demo.parking.db.Appointment;
import com.demo.parking.db.CarPark;
import com.demo.parking.db.Collection;
import com.demo.parking.db.Comment;
import com.demo.parking.db.Parking;
import com.demo.parking.db.Time;
import com.demo.parking.db.User;
import com.demo.parking.ui.BillActivity;
import com.demo.parking.ui.CommentAdapter;

import org.litepal.LitePal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CarParkActivity extends AppCompatActivity {
    private TextView carParkName;
    private TextView tvParking;
    private TextView address;
    private TextView desc;

    private ImageView pic;

    private EditText editText;

    private Button btn1;
    private Button btn1_1;
    private Button btn2;

    private CarPark carPark;

    //选择的车位
    private Parking parking;
    //我在车场预约的车位
    private Parking parking2;

    private Appointment appointment;
    private ListView listView;
    private CommentAdapter adapter;

    public static boolean TAG = false;

    private List<Comment> dataList;
    private Handler handler = new Handler(new Handler.Callback() {
        @SuppressLint("DefaultLocale")
        @Override
        public boolean handleMessage(Message msg) {
            //更新计时
            btn1.setText(String.format("点击离开(已停车:%d秒)", msg.obj));
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park);
        initView();
        initData();


    }

    private void initView() {
        TextView back = findViewById(R.id.tv_back);
        carParkName = findViewById(R.id.tv_name);
        tvParking = findViewById(R.id.tv_parking);
        address = findViewById(R.id.tv_address);
        desc = findViewById(R.id.tv_desc);
        listView = findViewById(R.id.list_view);
        btn1 = findViewById(R.id.btn_1);
        btn1_1 = findViewById(R.id.btn_1_1);
        btn2 = findViewById(R.id.btn_2);
        Button btn3 = findViewById(R.id.btn_3);
        Button btn4 = findViewById(R.id.btn_4);
        Button btn5 = findViewById(R.id.btn_5);
        Button btn6 = findViewById(R.id.btn_6);

        pic = findViewById(R.id.iv_pic);
        editText = findViewById(R.id.et_comment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 26) {
                    Toast.makeText(CarParkActivity.this, "求你了,换个手机系统版本8.0起步的吧", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(CarParkActivity.this, "没有内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                Comment comment = new Comment();
                comment.setCarPark(carPark);
                comment.setContent(editText.getText().toString());
                comment.setDate(LocalDateTime.now());
                comment.setUser(Account.getUser());
                comment.save();
                dataList.add(comment);
                adapter.notifyDataSetChanged();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏
                Collection collection = new Collection();
                collection.setUser(Account.getUser());
                collection.setCarPark(carPark);
                boolean b = collection.save();

                Toast.makeText(CarParkActivity.this, "收藏" + (b ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng startPoint = new LatLng(30.496070, 104.098625);
                LatLng endPoint = new LatLng(carPark.getLatitude(), carPark.getLongitude());


                RouteParaOption paraOption = new RouteParaOption()
                        .startPoint(startPoint)
                        .startName("我的位置")
                        .endPoint(endPoint)
                        .endName(carPark.getAddress())
                        .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
                //调起百度地图
                try {
                    BaiduMapRoutePlan.openBaiduMapTransitRoute(paraOption, CarParkActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //调起结束时及时调用finish方法以释放相关资源
                BaiduMapRoutePlan.finish(CarParkActivity.this);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn1.getText().equals("已到达")) {
                    btn1.setText("完成");
                    btn1_1.setVisibility(View.GONE);
                } else if (btn1.getText().equals("完成")) {
                    //parking表设置user表示我已停车与此  parking设置appointment表示车位被预约
                    //parking2.setUser(Account.getUser());
                    //创建计时表
                    Time time = new Time(parking2.getId());
                    time.save();

                    btn1.setText("点击离开(已停车:0秒)");
                    countTime(1);
                } else {
                    //完成停车  计算费用
                    int t = getTime();
                    int h;
                    //把秒换算成小时
                    h = t / 3600;
                    if (t % 3600 > 0) {
                        if (h != 0) {
                            //超过x小时并低于x+1小时就按x+1小时算钱
                            h += 1;
                        } else {
                            //h==0就表示总时间没超过一小时
                            h = 1;
                        }
                    }

                    //跳到结账界面
                    Intent intent = new Intent(CarParkActivity.this, BillActivity.class);
                    intent.putExtra("h", h);
                    startActivity(intent);
                }

            }
        });

        btn1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appointment != null) {
                    parking2.setOccupy(false);
                    parking2.setPark(carPark);
                    appointment.setCarPark(null);
                    appointment.setParking(null);

                    parking2.save();
                    appointment.save();
                    Toast.makeText(CarParkActivity.this, "成功取消预约 ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到停车场的所有车位
                List<Parking> parkingList = carPark.getParkings();
                //没有被占用的车位
                List<Parking> surplusParking = new ArrayList<>();
                for (Parking parking : parkingList) {
                    if (!parking.isOccupy())
                        //没被占用
                        surplusParking.add(parking);
                }

                List<String> list = new ArrayList<>();
                for (int i = 0; i < surplusParking.size(); i++) {
                    list.add(surplusParking.get(i).getNumber());
                }

                final String[] items = list.toArray(new String[0]);
                AlertDialog alertDialog3 = new AlertDialog.Builder(CarParkActivity.this)
                        .setTitle("显示空闲车位")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(CarParkActivity.this, "点的是：" + items[i], Toast.LENGTH_SHORT).show();
                                parking2 = parking = LitePal.where("number = ?", items[i]).findFirst(Parking.class, true);
                                btn2.setText(parking.getNumber());
                            }
                        })
                        .create();
                alertDialog3.show();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //预约
                if (parking == null) {
                    Toast.makeText(CarParkActivity.this, "未选择车位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isAppointment()) {
                    Toast.makeText(CarParkActivity.this, "你在本车场已经预约过了", Toast.LENGTH_SHORT).show();
                    return;
                }
                //生产预约表
                Appointment appointment = new Appointment();
                User self = Account.getUser();
                appointment.setUser(self);
                appointment.setParking(parking);
                parking.setOccupy(true);
                appointment.setCarPark(parking.getCarPark());
                parking.save();
                appointment.save();

                finish();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if (!TAG)
            return;

        if (appointment != null) {
            parking2.setOccupy(false);
            parking2.setPark(carPark);
            appointment.setCarPark(null);
            appointment.setParking(null);

            parking2.save();
            appointment.save();
        }
        Time t = LitePal.where("parkingId = ?", String.valueOf(parking2.getId())).findFirst(Time.class);
        t.delete();

        parking2 = null;
        btn1.setVisibility(View.GONE);

        TAG = false;
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        String id = intent.getBundleExtra("id").getString("id");
        carPark = LitePal.where("cid = ?", id).findFirst(CarPark.class, true);
        if (parking2 == null) {
            //判断我预约没有
            if (isAppointment()) {
                //预约了就把这个车位信息赋值给parking2
                parking2 = appointment.getParking();
            }
        }

        //评论
        adapter = new CommentAdapter(this, dataList = carPark.getComments());
        listView.setAdapter(adapter);

        carParkName.setText(String.format("停车场名称 : %s", carPark.getName()));
        tvParking.setText(String.format("剩余车位数 : %s", getParking()));
        address.setText(String.format("地址 : %s", carPark.getAddress()));
        desc.setText(String.format("介绍 : %s", carPark.getDesc()));

        pic.setImageResource(carPark.getPic());

        Time t = null;
        if (parking2 != null)
            t = LitePal.where("parkingId = ?", String.valueOf(parking2.getId())).findFirst(Time.class);

        if (isAppointment()) {
            //t不为空表示该停车位被停了
            if (t != null) {
                final int m = getTime();
                btn1.setText(String.format("点击离开(已停车:%d秒)", m));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        countTime(m);
                    }
                }).start();

                //隐藏取消预约的按钮
                btn1_1.setVisibility(View.GONE);
            } else // 没有停车就显示取消预约的按钮
                btn1_1.setVisibility(View.VISIBLE);

            btn1.setVisibility(View.VISIBLE);
        }

    }

    //得到已停车时间  单位秒
    private int getTime() {
        Time time = LitePal.where("parkingId = ?", String.valueOf(parking2.getId())).findFirst(Time.class);
        return time.countTime(Calendar.getInstance());
    }


    private int time = 0;

    private void countTime(int start) {
        time = start;
        //无限发送更新时间的消息
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 0, 1000);
    }


    class MyTask extends TimerTask {

        @Override
        public void run() {
            Message message = new Message();
            message.obj = time++;
            handler.sendMessageDelayed(message, 1000);
        }
    }

    //该停车场的车位是否有被我预约的
    private boolean isAppointment() {
        boolean temp = false;

        for (Parking p : carPark.getParkings()) {
            Parking cp = LitePal.find(Parking.class, p.getId(), true);
            Appointment appointment = cp.getAppointment();
            if (appointment == null)
                continue;
            Appointment a = LitePal.find(Appointment.class, appointment.getId(), true);
            User user = a.getUser();
            //不为空代表已经被预约了
            if (user != null) {
                //并且预约人是登陆账户本人
                if (user.getName().equals(Account.getUser().getName())) {
                    temp = true;
                    this.appointment = a;
                    break;
                }
            }
        }

        return temp;
    }

    private int getParking() {
        int count = 0;
        for (Parking carParkParking : carPark.getParkings()) {
            if (!carParkParking.isOccupy())
                count++;
        }
        return count;
    }
}
