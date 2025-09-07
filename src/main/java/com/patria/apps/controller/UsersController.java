package com.patria.apps.controller;

import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.users.request.UserDeleteRequest;
import com.patria.apps.users.request.UserDestroyRequest;
import com.patria.apps.users.request.UserListRequest;
import com.patria.apps.users.request.UserPasswordChangeRequest;
import com.patria.apps.users.request.UserRestoreRequest;
import com.patria.apps.users.request.UserRetrieveRequest;
import com.patria.apps.users.request.UserUpdateRequest;
import com.patria.apps.users.response.UsersResponse;
import com.patria.apps.users.service.UsersService;
import com.patria.apps.vo.CountryEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/user")
@Tag(name = "Users Controller")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<UsersResponse>> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "country", required = false) CountryEnum country,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "isTrash", required = true, defaultValue = "false") Boolean isTrash
    ) {
        UserListRequest request = UserListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .name(name)
                .country(country == null ? "" : country.toString())
                .email(email)
                .role(role)
                .isTrash(isTrash)
                .build();

        return usersService.listUser(request);
    }
    
    @PostMapping(
            path = "/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse changePassword(
            HttpServletRequest servletRequest,
            @RequestBody UserPasswordChangeRequest request
    ) {
        return usersService.updatePassword(servletRequest, request, SecurityHelperService.getPrincipal());
    }

    @PostMapping(
            path = "/update-profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse updateProfile(
            HttpServletRequest servletRequest,
            @RequestBody UserUpdateRequest request
    ) {
        return usersService.update(servletRequest, request, SecurityHelperService.getPrincipal());
    }
    
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReadResponse<UsersResponse> retrieveSingleData(
            @PathVariable String id
    ) {
        UserRetrieveRequest request = UserRetrieveRequest.builder()
                .build();

        return usersService.retrieveSingleData(id, request);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReadResponse delete(
            HttpServletRequest servletRequest,
            @RequestBody UserDeleteRequest request
    ) {
        return usersService.delete(servletRequest, request, SecurityHelperService.getPrincipal());
    }

    @DeleteMapping(
            path = "/destroy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse destroy(
            @RequestBody UserDestroyRequest request
    ) {
        return usersService.destroy(request);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse restore(
            HttpServletRequest servletRequest,
            @RequestBody UserRestoreRequest request
    ) {
        return usersService.restore(servletRequest, request, SecurityHelperService.getPrincipal());
    }
    
}
