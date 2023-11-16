

import retrofit2.Call;
import retrofit2.http.GET;


public interface IAutoUpdateService {
    @GET("/json file path")
    Call<AppFileData> getLatestAppInfo();
}
