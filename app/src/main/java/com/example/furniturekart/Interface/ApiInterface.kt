package com.example.furniturekart.Interface


import com.example.furniturekart.Model.CartModel
import com.example.furniturekart.Model.CategoryModel
import com.example.furniturekart.Model.ProductDetailModel
import com.example.furniturekart.Model.RegModel
import com.example.furniturekart.Model.UserViewModel
import com.example.furniturekart.Model.WishlistModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface
{
    @FormUrlEncoded
    @POST("registration.php")
    fun registeruser(
        @Field("fname") fname:String,
        @Field("lname") lname:String,
        @Field("gender") gender:String,
        @Field("email") email:String,
        @Field("phone") phone:String,
        @Field("password") password:String
    ):Call<Void>

    @FormUrlEncoded
    @POST("login.php")
    fun logindata(
        @Field("phone") phone:String,
        @Field("password") pass:String,
    ):Call<RegModel>

    @GET("UserView.php")
    fun userviewdata(): Call<List<UserViewModel>>

    @FormUrlEncoded
    @POST("forgotpassword.php")
    fun forgotpass(
        @Field("id")id:Int,
        @Field("password")password:String,
    ): Call<Void>

    @GET("categoryview.php")
    fun categoryviewdata(): Call<List<CategoryModel>>

    @FormUrlEncoded
    @POST("product_details.php")
    fun ProductDetailsviewdata(
        @Field("data") data:Int
    ): Call<List<ProductDetailModel>>


    @FormUrlEncoded
    @POST("wishlist.php")
    fun wishlistdata(
        @Field("c_id") c_id:Int,
        @Field("p_brand") p_brand:String,
        @Field("p_name") p_name:String,
        @Field("p_mrp") p_mrp:Int,
        @Field("p_image") p_image:String,
    ):Call<Void>

    @GET("wishlistview.php")
    fun wishlistviewdata(): Call<List<WishlistModel>>

    @FormUrlEncoded
    @POST("cart.php")
    fun cartdata(
        @Field("c_id") c_id:Int,
        @Field("p_brand") p_brand:String,
        @Field("p_name") p_name:String,
        @Field("p_mrp") p_mrp:Int,
        @Field("p_image") p_image:String,
    ):Call<Void>

    @GET("cartview.php")
    fun cartviewdata(): Call<List<CartModel>>

    @FormUrlEncoded
    @POST("deletefromwishlist.php")
    fun deletewishlistdata(
        @Field("id") id:Int
    ): Call<Void>

    @FormUrlEncoded
    @POST("deletefromcart.php")
    fun deletecarttdata(
        @Field("id") id:Int
    ): Call<Void>

    @FormUrlEncoded
    @POST("placeorder.php")
    fun palceorder(
        @Field("pbrnad") pbrnad:String,
        @Field("pname") pname:String,
        @Field("pquantity") pquantity:String,
        @Field("pprice") pprice:String,
        @Field("ptotalprice") ptotalprice:String,
        @Field("uname") uname:String,
        @Field("usurname") usurname:String,
        @Field("ucontact") ucontact:String,
        @Field("uaddress") uaddress:String,
        @Field("paymentmethod") paymentmethod:String
    ):Call<Void>
}