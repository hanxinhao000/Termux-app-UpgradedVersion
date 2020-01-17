package main.java.com.termux.weblinux;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
		NavigationView.OnNavigationItemSelectedListener {

	private static final String SHELL_IN_A_BOX = "shellinaboxd";
	private static String FILES_DIR;
	private static String PORT;
	private static Boolean LOCALHOST;
	private static Boolean ROOT;
	private static Boolean AUTOSTART;
	private static String SHELL;
	private static String USERNAME;
	private static String PASSWORD;
	private static Boolean ACTIVE;
	private static Boolean SCREEN_LOCK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity_main);

        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

		DrawerLayout drawer =  findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		FILES_DIR = getApplicationInfo().dataDir;
		extractData();

		TextView tv = (TextView) findViewById(R.id.addressBox);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				runURL();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_start:
			start(false);
			printStatus();
			break;
		case R.id.menu_stop:
			stop();
			printStatus();
			break;
        case android.R.id.home:
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            break;
        default:
            return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav_settings:
				Intent intent_settings = new Intent(this, SettingsActivity.class);
				startActivity(intent_settings);
				break;
			case R.id.nav_about:
				Intent intent_about = new Intent(this, AboutActivity.class);
				startActivity(intent_about);
				break;
			case R.id.nav_exit:
				finish();
				break;
		}
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadPrefs();
		// screen lock
		if (SCREEN_LOCK)
			this.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			this.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// start daemon
		if (AUTOSTART && !ACTIVE) {
			start(false);
		}
		printStatus();
	}

	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		PORT = sp.getString("port", getString(R.string.port));
		LOCALHOST = sp.getBoolean("localhost", getString(R.string.localhost)
				.equals("true"));
		ROOT = sp.getBoolean("root", getString(R.string.root).equals("true"));
		AUTOSTART = sp.getBoolean("autostart", getString(R.string.autostart)
				.equals("true"));
		SHELL = sp.getString("shell", getString(R.string.shell));
		USERNAME = sp.getString("username", getString(R.string.username));
		PASSWORD = sp.getString("password", getString(R.string.password));
		ACTIVE = isAlive();
		SCREEN_LOCK = sp.getBoolean("screenlock",
				getString(R.string.screenlock).equals("true") ? true : false);
	}

	private boolean copyFile(String sourceFile, File targetFile) {
		boolean result = true;
		AssetManager assetManager = getBaseContext().getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(sourceFile);
			out = new FileOutputStream(targetFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private String getLocalIpAddress() {
        String ip = "127.0.0.1";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
	}

	private void extractData() {
		File file = new File(FILES_DIR + File.separator + SHELL_IN_A_BOX);
		if (!file.exists()) {
			copyFile(SHELL_IN_A_BOX, file);
			file.setExecutable(true);
		}
		file = new File(FILES_DIR + File.separator + "pkill");
		if (!file.exists()) {
			copyFile("pkill", file);
			file.setExecutable(true);
		}
	}

	private void start(boolean restart) {
		List<String> list = new ArrayList<String>();
		String cmd = FILES_DIR + File.separator + SHELL_IN_A_BOX + " -t -p "
				+ PORT + " --shell=" + SHELL + ":" + USERNAME + ":" + PASSWORD;
		if (LOCALHOST) {
			cmd += " --localhost-only";
		}
		if (ROOT) {
			list.add("su");
			cmd += " -u 0 -g 0";
		} else {
			list.add("sh");
		}
		if (restart) {
			list.add(FILES_DIR + File.separator + "pkill -9 " + SHELL_IN_A_BOX);
			list.add("sleep 1");
		}
		list.add(cmd);
		new Thread(new ExecCmd(list)).start();
		ACTIVE = true;
	}

	private void stop() {
		List<String> list = new ArrayList<String>();
		if (ROOT) {
			list.add("su");
		} else {
			list.add("sh");
		}
		;
		String cmd = FILES_DIR + File.separator + "pkill -9 " + SHELL_IN_A_BOX;
		list.add(cmd);
		new Thread(new ExecCmd(list)).start();
		ACTIVE = false;
	}

	private boolean isAlive() {
		boolean active = false;
		List<String> list = new ArrayList<String>();
		String cmd = "ps | grep " + SHELL_IN_A_BOX;
		list.add(cmd);
		ExecCmd r = new ExecCmd(list);
		r.run();
		if (r.getOutput().size() > 1) {
			active = true;
		}
		return active;
	}

	private void runURL() {
		if (ACTIVE) {
			TextView tv = (TextView) findViewById(R.id.addressBox);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://" + (String) tv.getText()));
			startActivity(browserIntent);
		}
	}

	private void printStatus() {
		String address = "???";
		if (ACTIVE) {
			String ipaddress = "127.0.0.1";
			if (!LOCALHOST)
				ipaddress = getLocalIpAddress();
			address = ipaddress + ":" + PORT;
		}
		TextView addressBox = (TextView) findViewById(R.id.addressBox);
		addressBox.setText(address);
		TextView loginBox = (TextView) findViewById(R.id.loginBox);
		loginBox.setText(USERNAME);
		TextView passwordBox = (TextView) findViewById(R.id.passwordBox);
		passwordBox.setText(PASSWORD);
	}


    private void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }
}
