package com.patria.apps.users.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Users;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.response.MenuResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.response.RoleMenuAccess;
import com.patria.apps.response.RoleResponse;
import com.patria.apps.users.request.UserRetrieveRequest;
import com.patria.apps.users.response.UsersAddressResponse;
import com.patria.apps.users.response.UsersResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersSerializerService {

    private final AESHelperService aesService;

    public UsersResponse serialize(
            Users user
    ) {
        try {
            UsersResponse.UsersResponseBuilder builder = UsersResponse.builder()
                    .key(aesService.encrypt(String.valueOf(user.getId())))
                    .country(user.getCountry())
                    .isActive(user.getIsActive())
                    .email(user.getEmail())
                    .lastLoginAt(user.getLastLoginAt())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .phone(user.getPhone());

            List<RoleMenuAccess> roleMenuAccess = new ArrayList<>();
            user.getRole().getRoleMenuModules().stream().forEach(menu -> {
                try {
                    MenuResponse menuResponse = MenuResponse.builder()
                            .key(aesService.encrypt(String.valueOf(menu.getMenu().getId())))
                            .title(menu.getMenu().getTitle())
                            .icon(menu.getMenu().getIcon())
                            .to(menu.getMenu().getTo())
                            .parent(menu.getMenu().getParent() == null ? null : MenuResponse.builder()
                                    .key(aesService.encrypt(String.valueOf(menu.getMenu().getParent().getId())))
                                    .title(menu.getMenu().getParent().getTitle())
                                    .build())
                            .build();
                    roleMenuAccess.add(RoleMenuAccess.builder().menuResponse(menuResponse).build());
                } catch (Exception ex) {
                    throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!", ex);
                }
            });

            RoleResponse roles = RoleResponse.builder()
                    .roleName(user.getRole().getRoleName())
                    .roleMenuAccess(roleMenuAccess)
                    .build();

            builder.role(roles);

            List<UsersAddressResponse> userAddressList = new ArrayList<>();
            user.getUsersAddress().stream().forEach(address -> {
                try {
                    userAddressList.add(UsersAddressResponse.builder()
                            .key(aesService.encrypt(String.valueOf(address.getId())))
                            .address(address.getAddress())
                            .build());
                } catch (Exception ex) {
                    throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!", ex);
                }
            });
            
            builder.userAddress(userAddressList);

            return builder.build();
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!", e);
        }
    }

    public ReadResponse<UsersResponse> serializeRetrieve(Users user, UserRetrieveRequest request) {
        UsersResponse userResponse = this.serialize(
                user
        );
        return ReadResponse.<UsersResponse>builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .build();
    }

    public ReadResponse serializeTransaction(String message) {
        return ReadResponse.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }

}
