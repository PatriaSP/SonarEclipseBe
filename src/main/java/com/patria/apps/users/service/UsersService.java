package com.patria.apps.users.service;

import com.patria.apps.entity.Users;
import com.patria.apps.response.JWTResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.users.request.UserDeleteRequest;
import com.patria.apps.users.request.UserDestroyRequest;
import com.patria.apps.users.request.UserListRequest;
import com.patria.apps.users.request.UserPasswordChangeRequest;
import com.patria.apps.users.request.UserRestoreRequest;
import com.patria.apps.users.request.UserRetrieveRequest;
import com.patria.apps.users.request.UserUpdateRequest;
import com.patria.apps.users.request.UserCreateRequest;
import com.patria.apps.users.request.UserLoginRequest;
import com.patria.apps.users.response.UsersResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsersService {

    JWTResponse login(UserLoginRequest request);

    UserDetails loadUserByUsername(String username);

    ReadResponse<List<UsersResponse>> listUser(UserListRequest request);

    ReadResponse delete(HttpServletRequest servletRequest, UserDeleteRequest request, Users deletedBy);

    ReadResponse restore(HttpServletRequest servletRequest, UserRestoreRequest request, Users updatedBy);

    ReadResponse destroy(UserDestroyRequest request);

    ReadResponse<UsersResponse> retrieveSingleData(String id, UserRetrieveRequest request);

    ReadResponse update(HttpServletRequest servletRequest, UserUpdateRequest request, Users updatedBy);

    ReadResponse create(HttpServletRequest servletRequest, UserCreateRequest request);

    ReadResponse updatePassword(HttpServletRequest servletRequest, UserPasswordChangeRequest request, Users updatedBy);

}
