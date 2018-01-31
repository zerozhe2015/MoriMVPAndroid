package com.moriarty.base.sc;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.moriarty.base.BaseApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;


/**
 */
public class SecurityCheck {


    public static final int SIGNATURE_HASH_CODE = Keys.SIGNATURE_HASH; //-1549026082;


    public static boolean check() {


        Application application = BaseApplication.getApplication();
        boolean debuggable = false; // BuildConfig.DEBUG;
        if (!debuggable) {
            if (isDebugConnect()) {
                return false;
            }
            if (isDebugModify(application)) {
                return false;
            }
        }
//
//        if (isRunningInEmualtor()) {
//            return false;
//        }
        if (checkSignatureHash(application)) {
            return false;
        }
//        if (checkCRC(application)) {
//            return false;
//        }
        return true;
    }


    /**
     * 获取签名hash
     *
     * @param packageName
     * @param context
     * @return
     */
    public static int getSignatureHash(String packageName, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        int sig = 0;
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] s = pi.signatures;
            sig = s[0].hashCode();
        } catch (Exception e1) {
            sig = 0;
            e1.printStackTrace();
        }
        return sig;
    }

    /**
     * 检查程序签名hash
     *
     * @param context
     * @return
     */
    public static boolean checkSignatureHash(Context context) {
        boolean beModified = true;
        if (SIGNATURE_HASH_CODE == (getSignatureHash(context.getPackageName(), context))) {
            beModified = false;
        }
        return beModified;
    }

    /**
     * 检查程序安装后classes.dex文件的Hash
     *
     * @param context
     * @return
     */
/*    public static boolean checkCRC(Context context) {
        boolean beModified = true;
        long crc = Long.parseLong(context.getString(R.string.fb_chb));
        ZipFile zf;
        try {
            zf = new ZipFile(context.getPackageCodePath());
            ZipEntry ze = zf.getEntry("classes.dex");
            if (ze.getCrc() == crc) {
                beModified = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            beModified = true;
        }
        return beModified;
    }*/

    /**
     * 检测模拟器
     *
     * @return
     */
    public static boolean isRunningInEmualtor() {
        boolean qemuKernel = false;
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()
//                    , "GBK"
            ));
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            // 模拟器
            qemuKernel = (Integer.valueOf(in.readLine()) == 1);
        } catch (Exception e) {
            qemuKernel = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
        }
        return qemuKernel;
    }


    /**
     * 是否debug模式
     *
     * @return
     */
    public static boolean isDebugConnect() {
        return android.os.Debug.isDebuggerConnected();
    }


    /**
     * debuggable是否被修改
     *
     * @param context
     * @return
     */
    public static boolean isDebugModify(Context context) {
        if (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
            return true;
        }
        return false;
    }

}
