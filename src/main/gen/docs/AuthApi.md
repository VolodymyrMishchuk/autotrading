# AuthApi

All URIs are relative to *https://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authLoginPost**](AuthApi.md#authLoginPost) | **POST** /auth/login | Log in and receive JWT token |
| [**authSignupPost**](AuthApi.md#authSignupPost) | **POST** /auth/signup | Register new user |


<a id="authLoginPost"></a>
# **authLoginPost**
> AuthToken authLoginPost(authLogin)

Log in and receive JWT token

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost:8080");

    AuthApi apiInstance = new AuthApi(defaultClient);
    AuthLogin authLogin = new AuthLogin(); // AuthLogin | 
    try {
      AuthToken result = apiInstance.authLoginPost(authLogin);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#authLoginPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **authLogin** | [**AuthLogin**](AuthLogin.md)|  | |

### Return type

[**AuthToken**](AuthToken.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successful login |  -  |
| **401** | Unauthorized |  -  |

<a id="authSignupPost"></a>
# **authSignupPost**
> authSignupPost(userCreateRequest)

Register new user

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://localhost:8080");

    AuthApi apiInstance = new AuthApi(defaultClient);
    UserCreateRequest userCreateRequest = new UserCreateRequest(); // UserCreateRequest | 
    try {
      apiInstance.authSignupPost(userCreateRequest);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#authSignupPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userCreateRequest** | [**UserCreateRequest**](UserCreateRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | User created successfully |  -  |
| **400** | Invalid input |  -  |

