package com.finalandroid.project.hotelbooking.apis;

import com.finalandroid.project.hotelbooking.modelClass.BookedRoomResult;
import com.finalandroid.project.hotelbooking.modelClass.BookingRooms;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;
import com.finalandroid.project.hotelbooking.modelClass.HotelResult;
import com.finalandroid.project.hotelbooking.modelClass.NotificationModel;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.modelClass.Room;
import com.finalandroid.project.hotelbooking.modelClass.RoomResult;
import com.finalandroid.project.hotelbooking.modelClass.User;
import com.finalandroid.project.hotelbooking.modelClass.UserResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {

    /*
     عندما يتم إستدعاء الدالة يقوم ال ResetApiConnection بإحضار ال BaseUrl وإضافة إسم ال php file(API) عليه من قيمة ال POST/GET/DELETE/PUT...etc
     */

    /** the SignUp call */
    @FormUrlEncoded
    @POST("InsertUser.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<UserResult> insertUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("userTypes") String  userTypes);

    /** the SignIn call */
    @FormUrlEncoded
    @POST("userSignIn.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<UserResult> userLogin(
            @Field("phone") String phone,
            @Field("password") String password );// Abstract method

    /*
     ال Fields يلي حددناهم ("email", "password") لاااازم يتطابقوا مع القيم يلي إحنا محددينها
     بداخل ال Post array بداخل ملف ال API يلي من خلالها بنم إستقبال البيانات بداخل ال API ومعالجتها
     */

    /** the getHotels call */
    @GET("getHotels.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<List<Hotel>> getHotels();// Abstract method


    /** the getRooms call */
    @FormUrlEncoded
    @POST("getRooms.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<List<Room>> getRooms(
            @Field("hotelID") int hotelID);// Abstract method

    /** the booking call */
    @FormUrlEncoded
    @POST("booking.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> booking(
            @Field("hotelId") int hotelId,
            @Field("roomId") int roomId,
            @Field("userId") int userId );


    /** the updateUserInfo call */
    @FormUrlEncoded
    @POST("updateUserInfo.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<UserResult> updateUserInfo(
            @Field("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password );

    /** the deleteAccount call */
    @FormUrlEncoded
    @POST("deleteAccount.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> deleteAccount( @Field("userId") int userId );

    /** the getMyBookings call */
    @FormUrlEncoded
    @POST("getMyBookings.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<List<BookingRooms>> getMyBookings(@Field("userId") int userId );


    /** the deleteBookingRoom call */
    @FormUrlEncoded
    @POST("cancelBookingRoom.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> cancelBookingRoom(
            @Field("roomId") int roomId,
            @Field("userId") int userId );  


    /** the addNotification call */
    @FormUrlEncoded
    @POST("addNotification.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> addNotification(
            @Field("user_id") int userId,
            @Field("title") String title,
            @Field("text") String text,
            @Field("date_time") String DateTime );

    /** the getNotifications call */
    @FormUrlEncoded
    @POST("getNotifications.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<List<NotificationModel>> getNotifications(
            @Field("user_id") int userId);// Abstract method


    /** the deleteNotification call */
    @FormUrlEncoded
    @POST("deleteNotification.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> deleteNotification(
            @Field("NotificationId") int NotificationId );


    /** the getCreatedHotels call */
    @FormUrlEncoded
    @POST("getCreatedHotels.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<List<Hotel>> getCreatedHotels(
            @Field("userId") int hotelOwnerId );// Abstract method

    /** the createNewHotel call */
    @FormUrlEncoded
    @POST("createNewHotel.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<HotelResult> createNewHotel(
            @Field("userId") int hotelOwnerId,
            @Field("name") String hotelName,
            @Field("img") String hotelImg,
            @Field("location") String hotelLocation );

    /** the deleteRoom call */
    @FormUrlEncoded
    @POST("deleteRoom.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> deleteRoom(
            @Field("roomId") int roomIUd );


    /** the createNewRoom call */
    @FormUrlEncoded
    @POST("createNewRoom.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<RoomResult> createNewRoom(
            @Field("hotelId") int hotelId,
            @Field("img") String img,
            @Field("numDays") String numDays,
            @Field("priceFANight") String priceFANight,
            @Field("description") String description );


    /** the deleteHotel call */
    @FormUrlEncoded
    @POST("deleteHotel.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<Result> deleteHotel(
            @Field("hotelId") int hotelId );


    /** the updateRoomInfo call */
    @FormUrlEncoded
    @POST("updateRoomInfo.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<RoomResult> updateRoomInfo(
            @Field("roomId") int roomId,
            @Field("hotelId") int hotelId,
            @Field("img") String img,
            @Field("numDays") String numDays,
            @Field("priceNight") String priceNight,
            @Field("description") String description );

    /** the getBookedRooms call */
    @POST("getBookedRooms.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    @FormUrlEncoded
    Call<List<BookedRoomResult>> getBookedRooms(
            @Field("hotelId") int hotelId );

    /** the getUser call */
    @FormUrlEncoded
    @POST("getUser.php") // The value that will added to the BaseUrl by the RestApiConnection class.
    Call<User> getUser(
            @Field("userId") int userId );
}