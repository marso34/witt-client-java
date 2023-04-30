
package com.example.healthappttt.Fragment;

        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private final static String BASE_URL = "http://43.200.245.144:3000/";
    private static Retrofit retrofit = null;

    private ApiClient() {
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}