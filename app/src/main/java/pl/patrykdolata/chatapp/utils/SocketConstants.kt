package pl.patrykdolata.chatapp.utils

object SocketConstants {
    const val REGISTER_EVENT = "register"
    const val LOGIN_EVENT = "login"
    const val LOGOUT_EVENT = "logout"
    const val CHECK_USERNAME_EVENT = "check_username"
    const val CHECK_USERNAME_RESPONSE_EVENT = "check_username_response"
    const val GET_USERS_EVENT = "get_users"
    const val GET_USERS_RESPONSE_EVENT = "get_users_response"
    const val FIND_USERS_EVENT = "find_users"
    const val FIND_USERS_RESPONSE_EVENT = "find_users_response"
    const val FRIEND_REQUEST_EVENT = "friend_request";
    const val GET_FRIEND_REQUESTS_EVENT = "get_friend_requests";
    const val GET_FRIEND_REQUESTS_RESPONSE_EVENT = "get_friend_requests_response";
    const val GET_FRIENDS_EVENT = "get_friends";
    const val GET_FRIENDS_RESPONSE_EVENT = "get_friends_response";
    const val ACCEPT_FRIEND_REQUEST_EVENT = "accept_friend_request";
}