package com.tokenbank.tokeninformation.util.upgrade.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tokenbank.tokeninformation.R;
import com.tokenbank.tokeninformation.dialog.PromptDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */
public class UpgradeUtils {
    private static final int WIFI_MAX_STRENGTH = 5;

    public static boolean needUpgrade(UpgradeInfo upgradeInfo) {
        if (upgradeInfo == null || upgradeInfo.getUpgradeMode() == Constants.UPGRADE_MODE_NO) {
            return false;
        }
        return true;
    }

    public static void clearDownloadApk(String path) {
        File file = new File(path);

        if (file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                if (f.isFile() && f.getName().contains("TokenPocket")) {
                    if (!f.delete()) {
                        Log.w(Constants.TAG, "clear apk failed!");
                    }
                }
            }
        }
    }

    public static void startInstall(Context context, String apkName) {
        if (apkName == null || context == null) {
            Log.w(Constants.TAG, "startInstall apkName or context is null");
            return;
        }
        File apkFile = new File(UpgradeUtils.getDownloadPath(context), apkName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!checkPermission(context)) {
                //弹框提示用户开启权限
                showPermissionAskDialog(context);
                return;
            }
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(context,
                    "com.tokenbank.tokeninformation.fileprovider", apkFile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(context,
                    "com.tokenbank.tokeninformation.fileprovider", apkFile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    /**
     * 弹框询问开启权限
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void showPermissionAskDialog(final Context context) {
        //弹框询问获取安装权限
        //弹框询问获取安装权限
        new PromptDialog.Builder(context)
                .drawable(R.drawable.ic_warn)
                .content(context.getString(R.string.ask_install_app_permission))
                .positiveText(context.getString(R.string.confirm))
                .positiveListener(new PromptDialog.Builder.OnClickListener() {
                    @Override
                    public void onPositiveClick(Dialog dialog, View view) {
                        dialog.dismiss();
                        startInstallPermissionSettingActivity(context);
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent,
                Constants.UNKNOWN_APP_INSTALL_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean checkPermission(Context context) {
        return context.getPackageManager().canRequestPackageInstalls();
    }

    public static boolean isNewestApkDownloaded(String downloadPath, String hashCode) {
        if (downloadPath == null || hashCode == null) {
            return false;
        }
        File dir = new File(downloadPath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                return false;
            }
            for (File f : files) {
                if (hashCode.equals(getMd5ByFile(f))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取文件的hashCode
     */
    public static String getMd5ByFile(File file) {
        String value = "";
        if (file == null) {
            return value;
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.w(Constants.TAG, "file not found exception!");
        }

        if (in == null) {
            return value;
        }
        try {
            MappedByteBuffer byteBuffer =
                    in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.w(Constants.TAG, "no such algorithm exception!");
        } catch (IOException e) {
            Log.w(Constants.TAG, "io exception!");
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.w(Constants.TAG, "close file exception!");
                }
            }
        }
        return value;
    }

    /**
     * 格式化文件的大小
     */
    public static String getFileSize(double size) {
        DecimalFormat formatter = new DecimalFormat("0.00");
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return Double.valueOf(formatter.format(size)) + " B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return Double.valueOf(formatter.format(size)) + " KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return Double.valueOf(formatter.format(size)) + " MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size / 1024;
            return Double.valueOf(formatter.format(size)) + " GB";
        }
    }

    /**
     * 获取下载的路径：这里使用内部路径存储，不要使用外部路径，避免读写权限
     */
    public static String getDownloadPath(Context context) {
        return context.getExternalFilesDir(null) + Constants.DOWNLOAD_DIR;
    }

}
