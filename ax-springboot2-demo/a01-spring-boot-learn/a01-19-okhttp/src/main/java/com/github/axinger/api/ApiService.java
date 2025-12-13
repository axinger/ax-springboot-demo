package com.github.axinger.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

@Service
public interface ApiService {
    @GET("users/{id}")
        // GET 请求 + 路径参数
    Call<User> getUser(@Path("id") int userId);

    User getUser2(@Path("id") int userId);

    @POST("users/create")
        // POST 请求 + JSON 请求体
    Call<ResponseBody> createUser(@Body User user);

    @GET("search")
        // GET 请求 + Query 参数
    Call<List<?>> searchRepos(@Query("q") String query);


    @GET
    Call<User> getUser3(@Url String url);  // 允许传入完整 URL

    @GET("search")
    Call<List<?>> search(
            @Header("Authorization") String token,  // 请求头
            @Query("q") String query,              // Query 参数
            @Query("page") int page
    );

    //文件上传（Multipart）：
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );


//    @GET("users/{id}")
//    Observable<User> getUserRx(@Path("id") int userId);  // 返回 Observable
}
