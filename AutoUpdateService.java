

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutoUpdateService {
    private String TAG = "AutoUpdateService";
    private Context _context = null;
    private String _strApkFileUrl = "";

    public boolean DoUpdate(Context pCtx, String strServerAdd, int nPort) {
        this._context = pCtx;
        String strUpdateUrl = String.format("http://%s:%d", strServerAdd, nPort);
        Log.d(TAG,"AutoUpdateService.DoUpdate() : "+ strUpdateUrl);

        //Access to the server using Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(strUpdateUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IAutoUpdateService intAppUpdate = retrofit.create(IAutoUpdateService.class);

        // Get the Latest app file info
        Call<AppFileData> pServerFileInfo = intAppUpdate.getLatestAppInfo();


        pServerFileInfo.enqueue(new Callback<AppFileData>() {
            @Override
            public void onResponse(Call<AppFileData> call, Response<AppFileData> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "AppUpdateService.DoUpdate().onResponse() : " + response.code());
                    return;
                }
                AppFileData pFileInfoOnServer = response.body();
                AppFileData.Element[] elements = pFileInfoOnServer.getElements();
                AppFileData pFileInfoOnLocal = getLocalAppInfo();

                // Server file URL
                _strApkFileUrl = String.format("http://%s:%d/%s", strServerAdd, nPort, "APK FILE PATH");

                Log.d(TAG, "ServerVersion "+  elements[0].getElementVersionName());
                Log.d(TAG, "ServerCode"+  elements[0].getElementVersionCode());
                Log.d(TAG, "LocalVersion"+  pFileInfoOnLocal.getVersionName());
                Log.d(TAG, "LocalCode"+  pFileInfoOnLocal.getVersionCode());

                // version compare
                if(!(elements[0].getElementVersionName().equals(pFileInfoOnLocal.getVersionName()) && elements[0].getElementVersionCode() == pFileInfoOnLocal.getVersionCode())){
                    Log.d(TAG, "AppUpdateService.DoUpdate().onResponse() : Update Start =========> ");
                    DownloadAppDialog();
                }
            }

            @Override
            public void onFailure(Call<AppFileData> call, Throwable t) {
                Log.d("TAG","AppUpdateService.DoUpdate().onFailure() : " + t.getMessage());
            }
        });

        return true;
    }

    private AppFileData getLocalAppInfo(){
        AppFileData pReturn = new AppFileData();

        final PackageManager pm = this._context.getPackageManager();
        final String strPackageName = this._context.getPackageName();

        try{
            PackageInfo pPI = pm.getPackageInfo(strPackageName, 0);
            pReturn.setVersionName(pPI.versionName);
            pReturn.setVersionCode(pPI.versionCode);
        }
        catch (Exception e){
            Log.d(TAG, "getLocalAppInfo().Exception : " + e.getMessage());
        }
        return  pReturn;
    }

    private void DownloadAppDialog(){
        AlertDialog pDlg = new AlertDialog.Builder(this._context)
                .setTitle("New version available.")
                .setMessage("Please, Update app to new version.")
                .setPositiveButton("UPDATE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                redirectStore(_strApkFileUrl);
                                Log.d(TAG, "AppUpdateService.DownloadAppDialog() : click update");
                            }
                        })
                .setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ((Activity)_context).finish();
                    }
                }).create();
        pDlg.show();
    }

    private void redirectStore(String strUrl){
        final Intent  pIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl));

        pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this._context.startActivity(pIntent);
    }
}
