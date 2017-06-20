package com.zhizun.zhizunwifi.utils.router;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.zhizun.zhizunwifi.R;
import com.zhizun.zhizunwifi.bean.Vendor;
import com.zhizun.zhizunwifi.utils.SystemUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


final public class ActivityDiscovery extends ActivityNet implements OnItemClickListener {

    private final String TAG = "ActivityDiscovery";
    public final static long VIBRATE = (long) 250;
    public final static int SCAN_PORT_RESULT = 1;
    public static final int MENU_SCAN_SINGLE = 0;
    public static final int MENU_OPTIONS = 1;
    public static final int MENU_HELP = 2;
    private static final int MENU_EXPORT = 3;
    private static LayoutInflater mInflater;
    private int currentNetwork = 0;
    private long network_ip = 0;
    private long network_start = 0;
    private long network_end = 0;
    private List<HostBean> hosts = null;
    private HostsAdapter adapter;
    //private Button btn_discover;
    private AbstractDiscovery mDiscoveryTask = null;
    private ImageView headerLeft;
    private TextView headerTitle;
    private TextView equipment_num;
    private LinearLayout discovery_finsh_lay;
    private LinearLayout discovery_loading_lay;
    private TextView discovery_loading_tv;
    private TextView discovery_finsh_tv;
    private SQLiteDatabase db;

    // private SlidingDrawer mDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.discovery);
        mInflater = LayoutInflater.from(ctxt);

        headerLeft = (ImageView)findViewById(R.id.headerLeft);
        headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        headerTitle = (TextView)findViewById(R.id.headerTitle);
        headerTitle.setText("路由器管理");

        discovery_finsh_lay = (LinearLayout)findViewById(R.id.discovery_finsh_lay);
        discovery_loading_lay = (LinearLayout)findViewById(R.id.discovery_loading_lay);

        discovery_loading_tv = (TextView)findViewById(R.id.discovery_loading_tv);
        discovery_finsh_tv = (TextView)findViewById(R.id.discovery_finsh_tv);

        equipment_num = (TextView)findViewById(R.id.equipment_num);

        // Discover
       /* btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });*/


        // Hosts list
        adapter = new HostsAdapter(ctxt);
        ListView list = (ListView) findViewById(R.id.output);
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        //list.setOnItemClickListener(this);
        list.setEmptyView(findViewById(R.id.list_empty));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },1000);

    }

    private Handler handler =  new Handler(){
        @Override
        public void handleMessage(Message msg) {
            startDiscovering();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }


    protected void setInfo() {
        // Info
        ((TextView) findViewById(R.id.info_ip)).setText(info_ip_str);
        ((TextView) findViewById(R.id.info_in)).setText(info_in_str);
        ((TextView) findViewById(R.id.info_mo)).setText(info_mo_str);

        // Scan button state
        if (mDiscoveryTask != null) {
            /*//setButton(btn_discover, R.drawable.cancel, false);
            //btn_discover.setText(R.string.btn_discover_cancel);
            //btn_discover.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cancelTasks();
                }
            });*/
        }

        if (currentNetwork != net.hashCode()) {
            Log.i(TAG, "Network info has changed");
            currentNetwork = net.hashCode();

            // Cancel running tasks
            cancelTasks();
        } else {
            return;
        }

        // Get ip information
        network_ip = NetInfo.getUnsignedLongFromIp(net.ip);
        if (prefs.getBoolean(Prefs.KEY_IP_CUSTOM, Prefs.DEFAULT_IP_CUSTOM)) {
            // Custom IP
            network_start = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_START,
                    Prefs.DEFAULT_IP_START));
            network_end = NetInfo.getUnsignedLongFromIp(prefs.getString(Prefs.KEY_IP_END,
                    Prefs.DEFAULT_IP_END));
        } else {
            // Custom CIDR
            if (prefs.getBoolean(Prefs.KEY_CIDR_CUSTOM, Prefs.DEFAULT_CIDR_CUSTOM)) {
                net.cidr = Integer.parseInt(prefs.getString(Prefs.KEY_CIDR, Prefs.DEFAULT_CIDR));
            }
            // Detected IP
            int shift = (32 - net.cidr);
            if (net.cidr < 31) {
                network_start = (network_ip >> shift << shift) + 1;
                network_end = (network_start | ((1 << shift) - 1)) - 1;
            } else {
                network_start = (network_ip >> shift << shift);
                network_end = (network_start | ((1 << shift) - 1));
            }
            // Reset ip start-end (is it really convenient ?)
            Editor edit = prefs.edit();
            edit.putString(Prefs.KEY_IP_START, NetInfo.getIpFromLongUnsigned(network_start));
            edit.putString(Prefs.KEY_IP_END, NetInfo.getIpFromLongUnsigned(network_end));
            edit.commit();
        }
    }

    protected void setButtons(boolean disable) {
        if (disable) {
            //setButtonOff(btn_discover, R.drawable.abc_ic_search);
        } else {
           // setButtonOn(btn_discover, R.drawable.abc_ic_search);
        }
    }

    protected void cancelTasks() {
        if (mDiscoveryTask != null) {
            mDiscoveryTask.cancel(true);
            mDiscoveryTask = null;
        }
    }

    // Listen for Activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_PORT_RESULT:
                if (resultCode == RESULT_OK) {
                    // Get scanned ports
                    if (data != null && data.hasExtra(HostBean.EXTRA)) {
                        HostBean host = data.getParcelableExtra(HostBean.EXTRA);
                        if (host != null) {
                            hosts.set(host.position, host);
                        }
                    }
                }
            default:
                break;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final HostBean host = hosts.get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityDiscovery.this);
        dialog.setTitle(R.string.save);
        dialog.setItems(new CharSequence[] { getString(R.string.save),
                getString(R.string.save) }, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                       /* // Start portscan
                        Intent intent = new Intent(ctxt, ActivityPortscan.class);
                        intent.putExtra(EXTRA_WIFI, NetInfo.isConnected(ctxt));
                        intent.putExtra(HostBean.EXTRA, host);
                        startActivityForResult(intent, SCAN_PORT_RESULT);*/
                        break;
                    case 1:
                        // Change name
                        // FIXME: TODO

                        final View v = mInflater.inflate(R.layout.dialog_edittext, null);
                        final EditText txt = (EditText) v.findViewById(R.id.edittext);
                        final Save s = new Save();
                        txt.setText(s.getCustomName(host));

                        final AlertDialog.Builder rename = new AlertDialog.Builder(
                                ActivityDiscovery.this);
                        rename.setView(v);
                        rename.setTitle(R.string.discover_action_rename);
                        rename.setPositiveButton(R.string.btn_ok, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String name = txt.getText().toString();
                                host.hostname = name;
                                s.setCustomName(name, host.hardwareAddress);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(ActivityDiscovery.this,
                                        R.string.discover_action_saved, Toast.LENGTH_SHORT).show();

                            }
                        });
                        rename.setNegativeButton(R.string.btn_remove, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                host.hostname = null;
                                s.removeCustomName(host.hardwareAddress);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(ActivityDiscovery.this,
                                        R.string.discover_action_deleted, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        rename.show();
                        break;
                }
            }
        });
        dialog.setNegativeButton(R.string.btn_discover_cancel, null);
        dialog.show();
    }

    static class ViewHolder {
        TextView host;
        TextView mac;
        //TextView vendor;
        //ImageView logo;
    }

    // Custom ArrayAdapter
    private class HostsAdapter extends ArrayAdapter<Void> {
        public HostsAdapter(Context ctxt) {
            super(ctxt, R.layout.list_host, R.id.list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_host, null);
                holder = new ViewHolder();
                holder.host = (TextView) convertView.findViewById(R.id.list);
                holder.mac = (TextView) convertView.findViewById(R.id.mac);
                //holder.vendor = (TextView) convertView.findViewById(R.id.vendor);
               // holder.logo = (ImageView) convertView.findViewById(R.id.logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final HostBean host = hosts.get(position);
            /*if (host.deviceType == HostBean.TYPE_GATEWAY) {
                holder.logo.setImageResource(R.drawable.about_icon_function);
            } else if (host.isAlive == 1 || !host.hardwareAddress.equals(NetInfo.NOMAC)) {
                holder.logo.setImageResource(R.drawable.about_icon_privacy);
            } else {
                holder.logo.setImageResource(R.drawable.about_icon_service);
            }*/
            /*if (host.hostname != null && !host.hostname.equals(host.ipAddress)) {
                holder.host.setText(host.hostname + " (" + host.ipAddress + ")");
            } else {*/
                holder.host.setText("IP:"+host.ipAddress);
            //}

            if (!host.hardwareAddress.equals(NetInfo.NOMAC)) {
                holder.mac.setText(host.hardwareAddress);


               /* if(host.nicVendor != null){
                    holder.vendor.setText(host.nicVendor);
                } else {
                    holder.vendor.setText(R.string.info_unknown);
                }*/
                holder.mac.setVisibility(View.VISIBLE);
               // holder.vendor.setVisibility(View.VISIBLE);
            } else {
                holder.mac.setVisibility(View.GONE);
                //holder.vendor.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    /**
     * Discover hosts
     */
    public void startDiscovering() {
        int method = 0;
        try {
            method = Integer.parseInt(prefs.getString(Prefs.KEY_METHOD_DISCOVER,
                    Prefs.DEFAULT_METHOD_DISCOVER));
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        switch (method) {
            case 1:
                mDiscoveryTask = new DnsDiscovery(ActivityDiscovery.this);
                break;
            case 2:
                // Root
                break;
            case 0:
            default:
                mDiscoveryTask = new DefaultDiscovery(ActivityDiscovery.this);
        }
        mDiscoveryTask.setNetwork(network_ip, network_start, network_end);
        mDiscoveryTask.execute();
       // btn_discover.setText(R.string.btn_discover_cancel);
       // setButton(btn_discover, R.drawable.cancel, false);
        /*btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelTasks();
            }
        });*/
        setProgressBarVisibility(true);
        setProgressBarIndeterminateVisibility(true);
        initList();
    }

    public void stopDiscovering() {
        Log.e(TAG, "stopDiscovering()");
        mDiscoveryTask = null;
       // setButtonOn(btn_discover, R.drawable.detail_icon);
       /* btn_discover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDiscovering();
            }
        });*/
        setProgressBarVisibility(false);
        setProgressBarIndeterminateVisibility(false);
        //btn_discover.setText(R.string.btn_discover);
        discovery_loading_tv.setVisibility(View.GONE);

        //加载动画XML文件,生成动画指令
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        //开始执行动画
        discovery_loading_lay.startAnimation(animation);
        discovery_loading_lay.setVisibility(View.GONE);


        //加载动画XML文件,生成动画指令
        Animation animation0 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        //开始执行动画
        discovery_finsh_lay.startAnimation(animation0);
        discovery_finsh_lay.setVisibility(View.VISIBLE);

        discovery_finsh_tv.setText(info_in_str);
        discovery_finsh_tv.setVisibility(View.VISIBLE);
    }



    private void initList() {
        // setSelectedHosts(false);
        adapter.clear();
        hosts = new ArrayList<HostBean>();
    }

    public void addHost(HostBean host) {
        host.position = hosts.size();

        //List<Vendor> Vendors = DataSupport.where("macAddress = ?", host.hardwareAddress.substring(0,8).replace(":","-").toUpperCase()).find(Vendor.class);

        db =  SQLiteDatabase.openOrCreateDatabase(SystemUtil.DB_PATH+"/macbrand.db",null);
        String[] counl = {"netCardName"};
        String sql = "select netCardName from Vendor where macAddress = '" + host.hardwareAddress.substring(0,8).replace(":","-").toUpperCase()+"'";
       Cursor cursor =  db.rawQuery(sql,null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (host.ipAddress.equals(info_ip_str)){
                    host.hardwareAddress = cursor.getString(0)+"(本机)";
                }else {
                    host.hardwareAddress = cursor.getString(0);
                }
            }
        }
        hosts.add(host);
        adapter.add(null);
        equipment_num.setText("允许联网设备:"+hosts.size());
    }



    private void setButton(Button btn, int res, boolean disable) {
        if (disable) {
            setButtonOff(btn, res);
        } else {
            setButtonOn(btn, res);
        }
    }

    private void setButtonOff(Button b, int drawable) {
        b.setClickable(false);
        b.setEnabled(false);
        b.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }

    private void setButtonOn(Button b, int drawable) {
        b.setClickable(true);
        b.setEnabled(true);
        b.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTasks();
    }
}
