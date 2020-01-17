package main.java.com.termux.weblinux;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExecCmd implements Runnable {

	private List<String> params;
	private List<String> output;

	public ExecCmd(List<String> params) {
		this.params = params;
		output = new ArrayList<String>();
	}

	public List<String> getOutput() {
		return output;
	}

	public void run() {
		try {
			Process process = Runtime.getRuntime().exec(params.get(0));
			params.remove(0);
			OutputStream stdin = process.getOutputStream();
			DataOutputStream os = new DataOutputStream(stdin);
			for (String cmd : params) {
				os.writeBytes(cmd + "\n");
			}
			os.flush();
			os.close();

			final InputStream stdout = process.getInputStream();
			final InputStream stderr = process.getErrorStream();

			(new Thread() {
				public void run() {
					setLogger(stdout);
				}
			}).start();

			(new Thread() {
				public void run() {
					setLogger(stderr);
				}
			}).start();

			process.waitFor();

			stdin.close();
			stdout.close();
			stderr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setLogger(InputStream stdstream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stdstream));
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				output.add(line);
                Log.e("XINHAO_HAN", line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
